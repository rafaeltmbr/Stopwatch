class UpdateStopwatchTimeUseCaseImpl<MSS>: UpdateStopwatchTimeUseCase
where MSS: MutableStateStore, MSS.State == StopwatchState {
    private let store: MSS
    
    init(_ store: MSS) {
        self.store = store
    }
    
    func execute(_ timerState: TimerState) async {
        await store.update {
            StopwatchState(
                status: $0.status,
                milliseconds: timerState.milliseconds,
                completedLaps: $0.completedLaps,
                completedLapsMilliseconds: $0.completedLapsMilliseconds
            )
        }
    }
}
