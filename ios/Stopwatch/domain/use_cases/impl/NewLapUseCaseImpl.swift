class NewLapUseCaseImpl<MSS>: NewLapUseCase
where MSS: MutableStateStore, MSS.State == StopwatchState
{
    private let store: MSS
    private let calculateLapsStatuses: CalculateLapsStatuses
    
    init(_ store: MSS, _ calculateLapsStatuses: CalculateLapsStatuses) {
        self.store = store
        self.calculateLapsStatuses = calculateLapsStatuses
    }
    
    func execute() async {
        await store.update {currentState in
            var laps = currentState.laps.map { $0 }
            
            laps.append(
                Lap(
                    index: currentState.laps.count + 1,
                    milliseconds: 0,
                    status: .current
                )
            )

            return StopwatchState(
                status: currentState.status,
                milliseconds: currentState.milliseconds,
                laps: calculateLapsStatuses.execute(laps)
            )
        }
    }
}
