import Foundation

class HomeViewModelFactoryImpl<TS, SS>: HomeViewModelFactory
where TS: TimerService, SS: MutableStateStore, SS.State == StopwatchState
{
    private(set) var subscriptionId: UUID? = nil
    
    private let timerService: TS
    private let stopwatchStore: SS
    private let startStopwatchUseCase: StartStopwatchUseCase
    private let pauseStopwatchUseCase: PauseStopwatchUseCase
    private let viewTimeMapper: ViewTimeMapper
    
    init(
        _ timerService: TS,
        _ stopwatchStore: SS,
        _ startStopwatchUseCase: StartStopwatchUseCase,
        _ pauseStopwatchUseCase: PauseStopwatchUseCase,
        _ viewTimerMapper: ViewTimeMapper
    ) {
        self.timerService = timerService
        self.stopwatchStore = stopwatchStore
        self.startStopwatchUseCase = startStopwatchUseCase
        self.pauseStopwatchUseCase = pauseStopwatchUseCase
        self.viewTimeMapper = viewTimerMapper
    }
    
    deinit {
        if let id = subscriptionId {
            timerService.events.unsubscribe(id)
        }
    }
    
    func make() -> some HomeViewModel {
        HomeViewModelImpl(
            stopwatchStore,
            startStopwatchUseCase,
            pauseStopwatchUseCase,
            viewTimeMapper
        )
    }
}
