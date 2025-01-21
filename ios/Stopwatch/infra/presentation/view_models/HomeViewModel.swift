import Foundation

struct HomeState {
    let status: Status
    let time: ViewTime
    let laps: [ViewLap]
    let showLaps: Bool
    let showSeeAll: Bool
    
    init(
        status: Status = .initial,
        time: ViewTime = ViewTime(),
        laps: [ViewLap] = [],
        showLaps: Bool = false,
        showSeeAll: Bool = false
    ) {
        self.status = status
        self.time = time
        self.laps = laps
        self.showLaps = showLaps
        self.showSeeAll = showSeeAll
    }
}

enum HomeAction {
    case start, pause, resume, reset, lap, seeAll
}

protocol HomeViewModel: ObservableObject {
    var state: HomeState { get }

    func handleAction(_ action: HomeAction)
}
