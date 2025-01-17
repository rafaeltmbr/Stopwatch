import Foundation
import Combine

class HomeViewModelImpl: HomeViewModel {
    @Published var state = HomeState()
    
    init() {
        Task {@MainActor in
            state = HomeState(status: .running, time: state.time)
            
            while (state.status == .running) {
                try? await Task.sleep(nanoseconds: 10_000)
                state = HomeState(status: .running, time: state.time + 1)
            }
        }
    }
    
    func getState() -> any Publisher<HomeState, Never> {
        return _state.projectedValue
    }
    
    func handleAction(action: HomeAction) {
    }
}

