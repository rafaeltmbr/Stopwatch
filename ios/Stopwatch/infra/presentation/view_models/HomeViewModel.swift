import Foundation

struct HomeState {
    let status: Status
    let time: ViewTime
    
    init(status: Status = .initial, time: ViewTime = ViewTime()) {
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
