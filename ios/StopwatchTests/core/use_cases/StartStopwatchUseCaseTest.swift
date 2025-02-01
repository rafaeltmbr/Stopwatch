import Testing
@testable import Stopwatch

struct StartStopwatchUseCaseTest {
    @Test func start_shouldStartTimerAndUpdateStateStore() async throws {
        let store = MutableStateStoreImpl(StopwatchState(), EventEmitterImpl())
        let service = TimerServiceImpl(EventEmitterImpl())
        let useCase = StartStopwatchUseCaseImpl(store, service)
        
        await useCase.execute()
        #expect(store.state.status == .running)
        #expect(service.state.isRunning)
    }
    
    @Test func start_shouldIgnoreStartWhenStopwatchIsAlreadyRunning() async throws {
        let store = MutableStateStoreImpl(StopwatchState(), EventEmitterImpl())
        let service = TimerServiceImpl(EventEmitterImpl())
        let useCase = StartStopwatchUseCaseImpl(store, service)
        
        await useCase.execute()
        await useCase.execute()
        #expect(store.state.status == .running)
        #expect(service.state.isRunning)
    }
}
