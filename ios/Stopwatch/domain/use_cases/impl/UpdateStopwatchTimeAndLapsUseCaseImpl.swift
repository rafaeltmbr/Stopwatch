class UpdateStopwatchTimeAndLapsUseCaseImpl<MSS>: UpdateStopwatchTimeAndLapsUseCase
where MSS: MutableStateStore, MSS.State == StopwatchState {
    private let store: MSS
    private let calculateLapsStatuses: CalculateLapsStatuses
    
    init(_ store: MSS, _ calculateLapsStatuses: CalculateLapsStatuses) {
        self.store = store
        self.calculateLapsStatuses = calculateLapsStatuses
    }
    
    func execute(_ timerState: TimerState) async {
        await store.update {
            StopwatchState(
                status: $0.status,
                milliseconds: timerState.milliseconds,
                laps: getNextLaps(timerState.milliseconds, $0.laps)
            )
        }
    }
    
    private func getNextLaps(_ milliseconds: Int, _ laps: [Lap]) -> [Lap] {
        if laps.count < 2 {
            return [
                Lap(
                    index: 1,
                    milliseconds: milliseconds,
                    status: .current
                )
            ]
        }
        
        let currentLap = Lap(
            index: laps.last!.index,
            milliseconds: milliseconds - laps[0..<laps.count-1].reduce(0) { $0 + $1.milliseconds },
            status: .current
        )
        
        let lapsWithTimeUpdated = laps.map { $0.index == laps.last!.index ? currentLap : $0 }
        
        return calculateLapsStatuses.execute(lapsWithTimeUpdated)
    }
}
