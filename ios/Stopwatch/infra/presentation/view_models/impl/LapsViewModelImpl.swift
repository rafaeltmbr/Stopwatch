import Foundation
import SwiftUI

class LapsViewModelImpl<MSS>: ObservableObject, LapsViewModel
where MSS: MutableStateStore, MSS.State == StopwatchState {
    @Published private(set) var state: LapsState = LapsState()
    
    private var subscriptionId: UUID? = nil
    private let stopwatchStore: MSS
    private let startStopwatchUseCase: StartStopwatchUseCase
    private let newLapUseCase: NewLapUseCase
    private let stringTimeMapper: StringTimeMapper
    
    init(
        _ stopwatchStore: MSS,
        _ startStopwatchUseCase: StartStopwatchUseCase,
        _ newLapUseCase: NewLapUseCase,
        _ stringTimeMapper: StringTimeMapper
    ) {
        self.stopwatchStore = stopwatchStore
        self.startStopwatchUseCase = startStopwatchUseCase
        self.newLapUseCase = newLapUseCase
        self.stringTimeMapper = stringTimeMapper
        
        subscriptionId = stopwatchStore.events.susbcribe {newState in
            Task {@MainActor in
                self.state = LapsState(
                    status: newState.status,
                    milliseconds: stringTimeMapper.map(newState.milliseconds),
                    laps: newState.laps.reversed().map {
                        ViewLap(
                            index: $0.index,
                            time: stringTimeMapper.map($0.milliseconds),
                            status: $0.status
                        )
                    }
                )
            }
        }
    }
    
    deinit {
        if let id = subscriptionId {
            stopwatchStore.events.unsubscribe(id)
        }
    }
    
    func handleAction(_ action: LapsAction) {
        print("Action \(action)")
        
        Task {
            switch (action) {
            case .resume: await startStopwatchUseCase.execute()
            case .lap: await newLapUseCase.execute()
            case .back: break
            }
        }
    }
}
