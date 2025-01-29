class SaveStopwatchStateUseCaseImpl<MSS>: SaveStopwatchStateUseCase
where MSS: MutableStateStore, MSS.State == StopwatchState
{
    private(set) var stopwatchStore: MSS
    private(set) var stopwatchRepository: StopwatchRepository
    
    init(_ stopwatchState: MSS, _ stopwatchRepository: StopwatchRepository) {
        self.stopwatchStore = stopwatchState
        self.stopwatchRepository = stopwatchRepository
    }
    
    func execute() async {
        await stopwatchRepository.save(stopwatchStore.state)
    }
}
