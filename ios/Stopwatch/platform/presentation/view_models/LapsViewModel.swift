import Foundation

struct LapsState {
    let status: Status
    let milliseconds: String
    let lapsCount: Int
    
    init(status: Status = .initial, milliseconds: String = "00:00.00", lapsCount: Int = 0) {
        self.status = status
        self.milliseconds = milliseconds
        self.lapsCount = lapsCount
    }
}

enum LapsAction {
    case resume, lap
}

protocol LapsViewModel: ObservableObject {
    var state: LapsState { get }
    
    func handleAction(_ action: LapsAction)
    func getViewLapByReversedArrayIndex(_ index: Int) -> ViewLap
}
