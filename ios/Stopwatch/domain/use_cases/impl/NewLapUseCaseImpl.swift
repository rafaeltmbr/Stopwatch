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
            var laps = currentState.completedLaps.map { $0 }
            
            laps.append(
                Lap(
                    index: currentState.completedLaps.count + 1,
                    milliseconds: currentState.milliseconds - currentState.completedLapsMilliseconds,
                    status: .done
                )
            )

            return StopwatchState(
                status: currentState.status,
                milliseconds: currentState.milliseconds,
                completedLaps: calculateLapsStatuses.execute(laps),
                completedLapsMilliseconds: currentState.milliseconds
            )
        }
    }
}
