import Foundation
import SwiftUI


struct HomeView<ViewModel>: View where ViewModel: HomeViewModel {
    @StateObject var viewModel: ViewModel
    
    var body: some View {
        NavigationView {
            VStack {
                timer.padding(.bottom)
            
                switch (viewModel.state.status) {
                case .initial: HStack {
                    Button("Start") { viewModel.handleAction(.start) }
                }
                case .running: HStack {
                    Button("Pause") { viewModel.handleAction(.pause) }
                }
                case .paused: HStack(spacing: 30) {
                    Button("Resume") { viewModel.handleAction(.resume) }
                    Button("Reset") { viewModel.handleAction(.reset) }
                        .foregroundStyle(.red)
                }
                }
            }
            .padding()
            .navigationTitle("Stopwatch")
        }
    }
    
    var timer: some View {
        HStack(alignment: .bottom) {
            let minutes = viewModel.state.time.minutes.enumerated().map { ($0, $1)}
            ForEach(minutes, id: \.0) {
                Text($1)
                    .font(.system(size: 54))
                    .frame(width: 30)
            }
            
            Text(":").font(.system(size: 54)).padding(.leading, 4)

            let seconds = viewModel.state.time.seconds.enumerated().map { ($0, $1)}
            ForEach(seconds, id: \.0) {
                Text($1)
                    .font(.system(size: 54))
                    .frame(width: 30)
            }
            
            Text(".").font(.system(size: 60)).padding(.leading, 4)
            
            let fraction = viewModel.state.time.fraction.enumerated().map { ($0, $1)}
            ForEach(fraction, id: \.0) {
                Text($1)
                    .font(.system(size: 30)).padding(.bottom, 8)
                    .frame(width: 18)
            }
        }
    }
}


#Preview {
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
