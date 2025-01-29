class StartStopwatchUseCaseImpl<MSS, TS>: StartStopwatchUseCase
where MSS: MutableStateStore, MSS.State == StopwatchState, TS: TimerService {
    private let store: MSS
    private let timer: TS
    
    init(_ store: MSS, _ timer: TS) {
        self.store = store
        self.timer = timer
    }
    
    func execute() async {
        guard !timer.state.isRunning else { return }
        
        await store.update {
            StopwatchState(
                status: .running,
                milliseconds: $0.milliseconds,
                completedLaps: $0.completedLaps,
                completedLapsMilliseconds: $0.completedLapsMilliseconds
            )
        }
        timer.start()
    }
}
