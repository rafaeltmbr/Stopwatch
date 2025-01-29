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
        self.state = Self.handleStopwatchStateUpdate(stopwatchStore.state, stringTimeMapper)
        self.stopwatchStore = stopwatchStore
        self.startStopwatchUseCase = startStopwatchUseCase
        self.newLapUseCase = newLapUseCase
        self.stringTimeMapper = stringTimeMapper
        self.stackNavigator = stackNavigator
        
        subscriptionId = stopwatchStore.events.subscribe {stopwatchState in
            Task {@MainActor in
                self.state = Self.handleStopwatchStateUpdate(stopwatchState, stringTimeMapper)
            }
        }
    }
    
    private static func handleStopwatchStateUpdate(_ stopwatchState: StopwatchState, _ stringTimeMapper: StringTimeMapper) -> LapsState {
        LapsState(
            status: stopwatchState.status,
            milliseconds: stringTimeMapper.map(stopwatchState.milliseconds),
            lapsCount: stopwatchState.completedLaps.count + 1
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
    
    func getViewLapByReversedArrayIndex(_ index: Int) -> ViewLap {
        let computedIndex = stopwatchStore.state.completedLaps.count - index
        if computedIndex >= stopwatchStore.state.completedLaps.count {
            return ViewLap(
                index: stopwatchStore.state.completedLaps.count + 1,
                time: stringTimeMapper.map(
                    stopwatchStore.state.milliseconds - stopwatchStore.state.completedLapsMilliseconds
                ),
                status: .current
            )
        }
        
        let lap = stopwatchStore.state.completedLaps[computedIndex]
        return ViewLap(
            index: lap.index,
            time: stringTimeMapper.map(lap.milliseconds),
            status: lap.status
        )
    }
}
