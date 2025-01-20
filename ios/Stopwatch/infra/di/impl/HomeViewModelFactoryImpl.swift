import Foundation

class HomeViewModelFactoryImpl<SS>: HomeViewModelFactory
where SS: MutableStateStore, SS.State == StopwatchState
{
    private let stopwatchStore: SS
    private let startStopwatchUseCase: StartStopwatchUseCase
    private let pauseStopwatchUseCase: PauseStopwatchUseCase
    private let resetStopwatchUseCase: ResetStopwatchUseCase
    private let newLapUseCase: NewLapUseCase
    private let viewTimeMapper: ViewTimeMapper
    private let stringTimeMapper: StringTimeMapper
    
    init(
        _ stopwatchStore: SS,
        _ startStopwatchUseCase: StartStopwatchUseCase,
        _ pauseStopwatchUseCase: PauseStopwatchUseCase,
        _ resetStopwatchUseCase: ResetStopwatchUseCase,
        _ newLapUseCase: NewLapUseCase,
        _ viewTimerMapper: ViewTimeMapper,
        _ stringTimeMapper: StringTimeMapper
    ) {
        self.stopwatchStore = stopwatchStore
        self.startStopwatchUseCase = startStopwatchUseCase
        self.pauseStopwatchUseCase = pauseStopwatchUseCase
        self.resetStopwatchUseCase = resetStopwatchUseCase
        self.newLapUseCase = newLapUseCase
        self.viewTimeMapper = viewTimerMapper
        self.stringTimeMapper = stringTimeMapper
    }
    
    func make() -> some HomeViewModel {
        HomeViewModelImpl(
            stopwatchStore,
            startStopwatchUseCase,
            pauseStopwatchUseCase,
            resetStopwatchUseCase,
            newLapUseCase,
            viewTimeMapper,
            stringTimeMapper
        )
    }
}
