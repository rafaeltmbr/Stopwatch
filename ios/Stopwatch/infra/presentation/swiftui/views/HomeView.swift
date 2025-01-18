import SwiftUI


struct HomeView<ViewModel>: View where ViewModel: HomeViewModel {
    @StateObject var viewModel: ViewModel
    
    var body: some View {
        NavigationView {
            VStack {
                Text(String(format: "%0.2f s", Float(viewModel.state.time) / 1000.0))
                    .font(.system(size: 40))
                    .padding(.bottom)
            
                switch (viewModel.state.status) {
                case .initial, .paused: HStack {
                    Button("Start") { viewModel.handleAction(.start) }
                }
                case .running: HStack {
                    Button("Pause") { viewModel.handleAction(.pause) }
                }
                }
            }
            .padding()
            .navigationTitle("Stopwatch")
        }
    }
}

#Preview {
    let timerService = TimerServiceImpl()
    let stopwatchStore = MutableStateStoreImpl(StopwatchState())
    let startStopwatchUseCase = StartStopwatchUseCaseImpl(stopwatchStore, timerService)
    let pauseStopwatchUseCase = PauseStopwatchUseCaseImpl(stopwatchStore, timerService)
    let homeViewModelFactory = HomeViewModelFactoryImpl(
        timerService,
        stopwatchStore,
        startStopwatchUseCase,
        pauseStopwatchUseCase
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
