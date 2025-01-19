import Foundation

class TimerServiceImpl<EE>: TimerService where EE: EventEmitter, EE.Event == TimerState {
    private(set) var state: TimerState = TimerState()
    private(set) var events: EE
    
    private var task: Task<(), Never>? = nil
    
    init(_ eventEmitter: EE) {
        self.events = eventEmitter
    }
    
    func start() {
        guard !state.isRunning else { return }
        
        let startMilliseconds = state.milliseconds
        let startEpoch = Date().epochMilliseconds
        
        update(TimerState(isRunning: true, milliseconds: state.milliseconds))
        task = Task {
            while (state.isRunning) {
                do {
                    try await Task.sleep(nanoseconds: 10_000_000)
                    let milliseconds = Date().epochMilliseconds - startEpoch + startMilliseconds
                    update(TimerState(isRunning: state.isRunning, milliseconds: milliseconds))
                } catch {
                    break
                }
            }
        }
    }
    
    func pause() {
        guard state.isRunning else { return }
        
        task?.cancel()
        state = TimerState(isRunning: false, milliseconds: state.milliseconds)
    }
    
    func reset() {
        guard !state.isRunning else { return }
        
        task?.cancel()
        state = TimerState(isRunning: false, milliseconds: 0)
    }
    
    private func update(_ newState: TimerState) {
        state = newState
        events.emit(newState)
    }
}

private extension Date {
    var epochMilliseconds: Int {
        Int(timeIntervalSince1970 * 1_000)
    }
}
