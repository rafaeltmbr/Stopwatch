class LapsViewModelFactoryImpl<SS, SN>: LapsViewModelFactory
where SS: MutableStateStore, SS.State == StopwatchState, SN: StackNavigator {
    private let stopwatchStore: SS
    private let startStopwatchUseCase: StartStopwatchUseCase
    private let newLapUseCase: NewLapUseCase
    private let stringTimeMapper: StringTimeMapper
    private let stackNavigator: SN
    
    init(
        _ stopwatchStore: SS,
        _ startStopwatchUseCase: StartStopwatchUseCase,
        _ newLapUseCase: NewLapUseCase,
        _ stringTimeMapper: StringTimeMapper,
        _ stackNavigator: SN
    ) {
        self.stopwatchStore = stopwatchStore
        self.startStopwatchUseCase = startStopwatchUseCase
        self.newLapUseCase = newLapUseCase
        self.stringTimeMapper = stringTimeMapper
        self.stackNavigator = stackNavigator
    }
    
    func make() -> some LapsViewModel {
        return LapsViewModelImpl(
            stopwatchStore,
            startStopwatchUseCase,
            newLapUseCase,
            stringTimeMapper,
            stackNavigator
        )
    }
}
