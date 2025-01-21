import Foundation
import SwiftUI

class LapsViewModelImpl<MSS, SN>: ObservableObject, LapsViewModel
where MSS: MutableStateStore, MSS.State == StopwatchState, SN: StackNavigator {
    @Published private(set) var state: LapsState = LapsState()
    
    private var subscriptionId: UUID? = nil
    private let stopwatchStore: MSS
    private let startStopwatchUseCase: StartStopwatchUseCase
    private let newLapUseCase: NewLapUseCase
    private let stringTimeMapper: StringTimeMapper
    private let stackNavigator: SN
    
    init(
        _ stopwatchStore: MSS,
        _ startStopwatchUseCase: StartStopwatchUseCase,
        _ newLapUseCase: NewLapUseCase,
        _ stringTimeMapper: StringTimeMapper,
        _ stackNavigator: SN
    ) {
        self.state = Self.getNextState(stopwatchStore.state, stringTimeMapper)
        self.stopwatchStore = stopwatchStore
        self.startStopwatchUseCase = startStopwatchUseCase
        self.newLapUseCase = newLapUseCase
        self.stringTimeMapper = stringTimeMapper
        self.stackNavigator = stackNavigator
        
        subscriptionId = stopwatchStore.events.susbcribe {stopwatchState in
            Task {@MainActor in
                self.state = Self.getNextState(stopwatchState, stringTimeMapper)
            }
        }
    }
    
    private static func getNextState(_ stopwatchState: StopwatchState, _ stringTimeMapper: StringTimeMapper) -> LapsState {
            LapsState(
                status: stopwatchState.status,
                milliseconds: stringTimeMapper.map(stopwatchState.milliseconds),
                laps: stopwatchState.laps.reversed().map {
                    ViewLap(
                        index: $0.index,
                        time: stringTimeMapper.map($0.milliseconds),
                        status: $0.status
                    )
                }
            )
    }
    
    deinit {
        if let id = subscriptionId {
            stopwatchStore.events.unsubscribe(id)
        }
    }
    
    func handleAction(_ action: LapsAction) {
        Task {
            switch (action) {
            case .resume: await startStopwatchUseCase.execute()
            case .lap: await newLapUseCase.execute()
            }
        }
    }
}
