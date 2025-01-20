import SwiftUI

class HomeViewModelImpl<SS>: HomeViewModel
where SS: StateStore, SS.State == StopwatchState {
    @Published private(set) var state = HomeState()
    
    private let stopwatchStore: SS
    private let startStopwatchUseCase: StartStopwatchUseCase
    private let pauseStopwatchUseCase: PauseStopwatchUseCase
    private let resetStopwatchUseCase: ResetStopwatchUseCase
    private let newLapUseCase: NewLapUseCase
    private let viewTimeMapper: ViewTimeMapper
    private var subscriptionId: UUID? = nil

    init(
        _ stopwatchStore: SS,
        _ startStopwatchUseCase: StartStopwatchUseCase,
        _ pauseStopwatchUseCase: PauseStopwatchUseCase,
        _ resetStopwatchUseCase: ResetStopwatchUseCase,
        _ newLapUseCase: NewLapUseCase,
        _ viewTimeMapper: ViewTimeMapper
    ) {
        self.stopwatchStore = stopwatchStore
        self.startStopwatchUseCase = startStopwatchUseCase
        self.pauseStopwatchUseCase = pauseStopwatchUseCase
        self.resetStopwatchUseCase = resetStopwatchUseCase
        self.newLapUseCase = newLapUseCase
        self.viewTimeMapper = viewTimeMapper
        
        subscriptionId = stopwatchStore.events.susbcribe {newState in
            Task {@MainActor in
                self.state = HomeState(
                    status: newState.status,
                    time: viewTimeMapper.map(newState.milliseconds),
                    laps: newState.laps.reversed()[0..<min(newState.laps.count, 3)].map {
                        ViewLap(
                            $0.index,
                            "\($0.milliseconds)",
                            $0.status
                        )
                    },
                    showLaps: !newState.laps.isEmpty
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
            }
        }
    }
}

