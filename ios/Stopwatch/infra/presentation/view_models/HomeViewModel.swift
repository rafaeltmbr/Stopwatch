import Foundation

struct HomeState {
    let status: Status
    let time: Int
    
    init(status: Status = .initial, time: Int = 0) {
        self.status = status
        self.time = time
    }
}

enum HomeAction {
    case start, pause
}

protocol HomeViewModel: ObservableObject {
    var state: HomeState { get }

    func handleAction(_ action: HomeAction)
}
