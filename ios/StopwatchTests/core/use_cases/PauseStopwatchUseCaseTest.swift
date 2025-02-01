import Testing
@testable import Stopwatch

struct PauseStopwatchUseCaseTest {
    @Test func pause_shouldStopTimerAndUpdateStateStore() async throws {
        let store = MutableStateStoreImpl(StopwatchState(), EventEmitterImpl())
        let service = TimerServiceImpl(EventEmitterImpl())
        let startStopwatch = StartStopwatchUseCaseImpl(store, service)
        let pauseStopwatch = PauseStopwatchUseCaseImpl(store, service)
        
        await startStopwatch.execute()
        #expect(store.state.status == .running)
        #expect(service.state.isRunning)
        
        await pauseStopwatch.execute()
        #expect(store.state.status == .paused)
        #expect(!service.state.isRunning)
    }
    
    @Test func pause_shouldNotPauseWhenStopwatchIsNotRunning() async throws {
        let store = MutableStateStoreImpl(StopwatchState(), EventEmitterImpl())
        let service = TimerServiceImpl(EventEmitterImpl())
        let pauseStopwatch = PauseStopwatchUseCaseImpl(store, service)
        
        await pauseStopwatch.execute()
        #expect(store.state.status == .initial)
        #expect(!service.state.isRunning)
    }
}
