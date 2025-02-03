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
    
    @Test func start_shouldResumeStopwathAfterPause() async throws {
        let store = MutableStateStoreImpl(StopwatchState(), EventEmitterImpl())
        let service = TimerServiceImpl(EventEmitterImpl())
        let startUseCase = StartStopwatchUseCaseImpl(store, service)
        let pauseUseCase = PauseStopwatchUseCaseImpl(store, service)

        await startUseCase.execute()
        #expect(store.state.status == .running)
        #expect(service.state.isRunning)
        
        await pauseUseCase.execute()
        #expect(store.state.status == .paused)
        #expect(!service.state.isRunning)
        
        await startUseCase.execute()
        #expect(store.state.status == .running)
        #expect(service.state.isRunning)
    }
}
