class NewLapUseCaseImpl<MSS>: NewLapUseCase
where MSS: MutableStateStore, MSS.State == StopwatchState
{
    private let store: MSS
    
    init(_ store: MSS) {
        self.store = store
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
                laps: UpdateStopwatchTimeAndLapsUseCaseImpl<MSS>.updateLapsStatuses(laps)
            )
        }
    }
}
