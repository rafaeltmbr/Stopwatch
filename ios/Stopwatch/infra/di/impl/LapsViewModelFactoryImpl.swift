class LapsViewModelFactoryImpl<SS>: LapsViewModelFactory
where SS: MutableStateStore, SS.State == StopwatchState {
    private let stopwatchStore: SS
    private let startStopwatchUseCase: StartStopwatchUseCase
    private let newLapUseCase: NewLapUseCase
    private let stringTimeMapper: StringTimeMapper
    
    init(
        _ stopwatchStore: SS,
        _ startStopwatchUseCase: StartStopwatchUseCase,
        _ newLapUseCase: NewLapUseCase,
        _ stringTimeMapper: StringTimeMapper
    ) {
        self.stopwatchStore = stopwatchStore
        self.startStopwatchUseCase = startStopwatchUseCase
        self.newLapUseCase = newLapUseCase
        self.stringTimeMapper = stringTimeMapper
    }
    
    func make() -> some LapsViewModel {
        return LapsViewModelImpl(
            stopwatchStore,
            startStopwatchUseCase,
            newLapUseCase,
            stringTimeMapper
        )
    }
}
