class RestoreStopwatchStateUseCaseImpl<MSS, TS>: RestoreStopwatchStateUseCase
where MSS: MutableStateStore, MSS.State == StopwatchState, TS: TimerService
{
    private(set) var stopwatchStore: MSS
    private(set) var stopwatchRepository: StopwatchRepository
    private(set) var timerService: TS
    
    init(_ stopwatchState: MSS, _ stopwatchRepository: StopwatchRepository, _ timerService: TS) {
        self.stopwatchStore = stopwatchState
        self.stopwatchRepository = stopwatchRepository
        self.timerService = timerService
    }
    
    func execute() async {
        if let state = await stopwatchRepository.load() {
            guard state.milliseconds > 0 && state.laps.count > 1 else { return }
            
            await stopwatchStore.update {_ in
                StopwatchState(
                    status: .paused,
                    milliseconds: state.milliseconds,
                    laps: state.laps
                )
            }
           
            timerService.set(TimerState(isRunning: false, milliseconds: state.milliseconds))
        }
    }
}
