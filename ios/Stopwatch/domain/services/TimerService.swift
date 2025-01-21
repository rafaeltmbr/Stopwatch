import Foundation

struct TimerState {
    let isRunning: Bool
    let milliseconds: Int
    
    init(isRunning: Bool = false, milliseconds: Int = 0) {
        self.isRunning = isRunning
        self.milliseconds = milliseconds
    }
}

protocol TimerService {
    associatedtype Events: EventSubscriber where Events.Event == TimerState
    
    var state: TimerState { get }
    var events: Events { get }
    
    func set(_ state: TimerState)
    func start()
    func pause()
    func reset()
}
