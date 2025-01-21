import SwiftUI

class HomeViewModelImpl<SS, SN>: HomeViewModel
where SS: StateStore, SS.State == StopwatchState, SN: StackNavigator {
    @Published private(set) var state = HomeState()
    
    private let stopwatchStore: SS
    private let startStopwatchUseCase: StartStopwatchUseCase
    private let pauseStopwatchUseCase: PauseStopwatchUseCase
    private let resetStopwatchUseCase: ResetStopwatchUseCase
    private let newLapUseCase: NewLapUseCase
    private let viewTimeMapper: ViewTimeMapper
    private let stringTimeMapper: StringTimeMapper
    private let stackNavigator: SN
    private var subscriptionId: UUID? = nil
    
    init(
        _ stopwatchStore: SS,
        _ startStopwatchUseCase: StartStopwatchUseCase,
        _ pauseStopwatchUseCase: PauseStopwatchUseCase,
        _ resetStopwatchUseCase: ResetStopwatchUseCase,
        _ newLapUseCase: NewLapUseCase,
        _ viewTimeMapper: ViewTimeMapper,
        _ stringTimeMapper: StringTimeMapper,
        _ stackNavigator: SN
    ) {
        self.stopwatchStore = stopwatchStore
        self.startStopwatchUseCase = startStopwatchUseCase
        self.pauseStopwatchUseCase = pauseStopwatchUseCase
        self.resetStopwatchUseCase = resetStopwatchUseCase
        self.newLapUseCase = newLapUseCase
        self.viewTimeMapper = viewTimeMapper
        self.stringTimeMapper = stringTimeMapper
        self.stackNavigator = stackNavigator
        
        subscriptionId = stopwatchStore.events.susbcribe {newState in
            Task {@MainActor in
                self.state = HomeState(
                    status: newState.status,
                    time: viewTimeMapper.map(newState.milliseconds),
                    laps: newState.laps.reversed()[0..<min(newState.laps.count, 3)].map {
                        ViewLap(
                            index: $0.index,
                            time: stringTimeMapper.map($0.milliseconds),
                            status: $0.status
                        )
                    },
                    showLaps: !newState.laps.isEmpty,
                    showSeeAll: newState.laps.count > 3
                )
            }
        }
    }
    
    deinit {
        if let id = subscriptionId {
            stopwatchStore.events.unsubscribe(id)
        }
    }
    
    func handleAction(_ action: HomeAction) {
        print("Action \(action)")
        
        Task {
            switch action {
            case .start: await startStopwatchUseCase.execute()
            case .pause: await pauseStopwatchUseCase.execute()
            case .resume: await startStopwatchUseCase.execute()
            case .reset: await resetStopwatchUseCase.execute()
            case .lap: await newLapUseCase.execute()
            case .seeAll: stackNavigator.push(.laps)
            }
        }
    }
}

