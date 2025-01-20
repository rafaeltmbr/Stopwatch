import Foundation

class HomeViewModelFactoryImpl<SS>: HomeViewModelFactory
where SS: MutableStateStore, SS.State == StopwatchState
{
    private let stopwatchStore: SS
    private let startStopwatchUseCase: StartStopwatchUseCase
    private let pauseStopwatchUseCase: PauseStopwatchUseCase
    private let resetStopwatchUseCase: ResetStopwatchUseCase
    private let viewTimeMapper: ViewTimeMapper
    
    init(
        _ stopwatchStore: SS,
        _ startStopwatchUseCase: StartStopwatchUseCase,
        _ pauseStopwatchUseCase: PauseStopwatchUseCase,
        _ resetStopwatchUseCase: ResetStopwatchUseCase,
        _ viewTimerMapper: ViewTimeMapper
    ) {
        self.stopwatchStore = stopwatchStore
        self.startStopwatchUseCase = startStopwatchUseCase
        self.pauseStopwatchUseCase = pauseStopwatchUseCase
        self.resetStopwatchUseCase = resetStopwatchUseCase
        self.viewTimeMapper = viewTimerMapper
    }
    
    func make() -> some HomeViewModel {
        HomeViewModelImpl(
            stopwatchStore,
            startStopwatchUseCase,
            pauseStopwatchUseCase,
            resetStopwatchUseCase,
            viewTimeMapper
        )
    }
}
