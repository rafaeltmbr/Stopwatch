import Foundation

class HomeViewModelFactoryImpl<SS, SN>: HomeViewModelFactory
where SS: MutableStateStore, SS.State == StopwatchState, SN: StackNavigator
{
    private let stopwatchStore: SS
    private let loggingUseCase: LoggingUseCase
    private let startStopwatchUseCase: StartStopwatchUseCase
    private let pauseStopwatchUseCase: PauseStopwatchUseCase
    private let resetStopwatchUseCase: ResetStopwatchUseCase
    private let newLapUseCase: NewLapUseCase
    private let viewTimeMapper: ViewTimeMapper
    private let stringTimeMapper: StringTimeMapper
    private let stackNavigator: SN
    
    init(
        _ stopwatchStore: SS,
        _ loggingUseCase: LoggingUseCase,
        _ startStopwatchUseCase: StartStopwatchUseCase,
        _ pauseStopwatchUseCase: PauseStopwatchUseCase,
        _ resetStopwatchUseCase: ResetStopwatchUseCase,
        _ newLapUseCase: NewLapUseCase,
        _ viewTimerMapper: ViewTimeMapper,
        _ stringTimeMapper: StringTimeMapper,
        _ stackNavigator: SN
    ) {
        self.stopwatchStore = stopwatchStore
        self.loggingUseCase = loggingUseCase
        self.startStopwatchUseCase = startStopwatchUseCase
        self.pauseStopwatchUseCase = pauseStopwatchUseCase
        self.resetStopwatchUseCase = resetStopwatchUseCase
        self.newLapUseCase = newLapUseCase
        self.viewTimeMapper = viewTimerMapper
        self.stringTimeMapper = stringTimeMapper
        self.stackNavigator = stackNavigator
    }
    
    func make() -> some HomeViewModel {
        HomeViewModelImpl(
            stopwatchStore,
            loggingUseCase,
            startStopwatchUseCase,
            pauseStopwatchUseCase,
            resetStopwatchUseCase,
            newLapUseCase,
            viewTimeMapper,
            stringTimeMapper,
            stackNavigator
        )
    }
}
