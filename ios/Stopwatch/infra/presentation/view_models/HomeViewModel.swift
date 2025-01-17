import Foundation
import Combine
import SwiftUI

struct HomeState {
    let status: Status
    let time: Int
    
    init(status: Status = .initial, time: Int = 0) {
        self.status = status
        self.time = time
    }
}

enum Status {
   case initial, running, paused
}

enum HomeAction {
    case start, pause, resume, reset, lap, seeAll
}

protocol HomeViewModel: ObservableObject {
    var state: HomeState { get }

    func handleAction(action: HomeAction)
}
