import SwiftUI


@main
struct StopwatchApp: App {
    var body: some Scene {
        WindowGroup {
            let timerService = TimerServiceImpl(EventEmitterImpl())
            let stopwatchStore = MutableStateStoreImpl(StopwatchState(), EventEmitterImpl())
            let startStopwatchUseCase = StartStopwatchUseCaseImpl(stopwatchStore, timerService)
            let pauseStopwatchUseCase = PauseStopwatchUseCaseImpl(stopwatchStore, timerService)
            let resetStopwatchUseCase = ResetStopwatchUseCaseImpl(stopwatchStore, timerService)
            let viewTimerMapper = ViewTimerMapperImpl()
            let homeViewModelFactory = HomeViewModelFactoryImpl(
                timerService,
                stopwatchStore,
                startStopwatchUseCase,
                pauseStopwatchUseCase,
                resetStopwatchUseCase,
                viewTimerMapper
            )
            
            let _ = timerService.events.susbcribe {timerState in
                Task {
                    await stopwatchStore.update {currentState in
                        return StopwatchState(status: currentState.status, milliseconds: timerState.milliseconds)
                    }
                }
            }

            return HomeView(viewModel: homeViewModelFactory.make())
        }
    }
}
