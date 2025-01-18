class PauseStopwatchUseCaseImpl<MSS, TS>: PauseStopwatchUseCase
where MSS: MutableStateStore, MSS.State == StopwatchState, TS: TimerService {
    private let store: MSS
    private let timer: TS
    
    init(_ store: MSS, _ timer: TS) {
        self.store = store
        self.timer = timer
    }
    
    func execute() async {
        guard timer.state.isRunning else { return }
        
        timer.pause()
        await store.update { StopwatchState(status: .paused, milliseconds: $0.milliseconds) }
    }
}
