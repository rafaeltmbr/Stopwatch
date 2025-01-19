class ResetStopwatchUseCaseImpl<MSS, TS>: ResetStopwatchUseCase
where MSS: MutableStateStore, MSS.State == StopwatchState, TS: TimerService {
    private let store: MSS
    private let timer: TS
    
    init(_ store: MSS, _ timer: TS) {
        self.store = store
        self.timer = timer
    }
    
    func execute() async {
        guard !timer.state.isRunning && store.state.status == .paused else { return }
        
        timer.reset()
        await store.update {_ in StopwatchState() }
    }
}
