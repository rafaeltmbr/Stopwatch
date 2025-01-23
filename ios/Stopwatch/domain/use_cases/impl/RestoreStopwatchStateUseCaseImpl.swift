class RestoreStopwatchStateUseCaseImpl<MSS, TS>: RestoreStopwatchStateUseCase
where MSS: MutableStateStore, MSS.State == StopwatchState, TS: TimerService
{
    private(set) var stopwatchStore: MSS
    private(set) var stopwatchRepository: StopwatchRepository
    private(set) var timerService: TS
    private(set) var loggingService: LoggingService
    
    init(
        _ stopwatchState: MSS,
        _ stopwatchRepository: StopwatchRepository,
        _ timerService: TS,
        _ loggingService: LoggingService
    ) {
        self.stopwatchStore = stopwatchState
        self.stopwatchRepository = stopwatchRepository
        self.timerService = timerService
        self.loggingService = loggingService
    }
    
    func execute() async {
        if let state = await stopwatchRepository.load() {
            guard state.milliseconds > 0 && state.completedLaps.count > 1 else { return }
            loggingService.info(tag: "StopwatchApp", message: "State restored")

            await stopwatchStore.update {_ in
                StopwatchState(
                    status: .paused,
                    milliseconds: state.milliseconds,
                    completedLaps: state.completedLaps,
                    completedLapsMilliseconds: state.completedLapsMilliseconds
                )
            }
           
            timerService.set(TimerState(isRunning: false, milliseconds: state.milliseconds))
        }
    }
}
