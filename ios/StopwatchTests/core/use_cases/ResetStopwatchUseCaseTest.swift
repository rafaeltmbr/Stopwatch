import Testing
@testable import Stopwatch

struct ResetStopwatchUseCaseTest {
    @Test func reset_shouldResetTimerAndUpdateStateStore() async throws {
        let store = MutableStateStoreImpl(StopwatchState(), EventEmitterImpl())
        let service = TimerServiceImpl(EventEmitterImpl())
        let startStopwatch = StartStopwatchUseCaseImpl(store, service)
        let pauseStopwatch = PauseStopwatchUseCaseImpl(store, service)
        let resetStopwatch = ResetStopwatchUseCaseImpl(store, service)

        await startStopwatch.execute()
        #expect(store.state.status == .running)
        #expect(service.state.isRunning)
        
        await pauseStopwatch.execute()
        #expect(store.state.status == .paused)
        #expect(!service.state.isRunning)
        
        await resetStopwatch.execute()
        #expect(store.state.status == .initial)
        #expect(!service.state.isRunning)
    }
    
    @Test func reset_shouldNotResetWhenStopwatchIsAlreadyRunning() async throws {
        let store = MutableStateStoreImpl(StopwatchState(), EventEmitterImpl())
        let service = TimerServiceImpl(EventEmitterImpl())
        let startStopwatch = StartStopwatchUseCaseImpl(store, service)
        let resetStopwatch = ResetStopwatchUseCaseImpl(store, service)

        await startStopwatch.execute()
        #expect(store.state.status == .running)
        #expect(service.state.isRunning)
        
        await resetStopwatch.execute()
        #expect(store.state.status == .running)
        #expect(service.state.isRunning)
    }
}
