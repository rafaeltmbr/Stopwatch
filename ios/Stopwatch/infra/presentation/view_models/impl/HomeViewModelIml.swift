import SwiftUI

class HomeViewModelImpl<SS>: HomeViewModel
where SS: StateStore, SS.State == StopwatchState {
    @Published private(set) var state = HomeState()
    
    private let stopwatchStore: SS
    private let startStopwatchUseCase: StartStopwatchUseCase
    private let pauseStopwatchUseCase: PauseStopwatchUseCase
    private var subscriptionId: UUID? = nil

    init(
        _ stopwatchStore: SS,
        _ startStopwatchUseCase: StartStopwatchUseCase,
        _ pauseStopwatchUseCase: PauseStopwatchUseCase
    ) {
        self.stopwatchStore = stopwatchStore
        self.startStopwatchUseCase = startStopwatchUseCase
        self.pauseStopwatchUseCase = pauseStopwatchUseCase
        
        subscriptionId = stopwatchStore.events.susbcribe {newState in
            Task {@MainActor in
                self.state = HomeState(status: newState.status, time: newState.milliseconds)
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
            }
        }
    }
}

