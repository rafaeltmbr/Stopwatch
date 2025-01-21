import Foundation

struct LapsState {
    let status: Status
    let milliseconds: String
    let laps: [ViewLap]
    
    init(status: Status = .initial, milliseconds: String = "00:00.00", laps: [ViewLap] = []) {
        self.status = status
        self.milliseconds = milliseconds
        self.laps = laps
    }
}

enum LapsAction {
    case resume, lap
}

protocol LapsViewModel: ObservableObject {
    var state: LapsState { get }
    
    func handleAction(_ action: LapsAction)
}
