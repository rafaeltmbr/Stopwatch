import Foundation

class TimerServiceImpl: TimerService {
    private(set) var state: TimerState = TimerState()
    private(set) var events = EventEmitterImpl<TimerState>()
    
    private var task: Task<(), Never>? = nil
    
    func start() {
        guard !state.isRunning else { return }
        
        update(TimerState(isRunning: true, milliseconds: state.milliseconds))
        task = Task {
            while (state.isRunning) {
                do {
                    try await Task.sleep(nanoseconds: 10_000_000)
                    update(TimerState(isRunning: state.isRunning, milliseconds: state.milliseconds + 10))
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
    
    private func update(_ newState: TimerState) {
        state = newState
        events.emit(newState)
    }
}
