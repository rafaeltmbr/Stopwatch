import Testing
@testable import Stopwatch

struct TimerServiceTest {
    @Test func state_shouldHaveValidInitialState() async throws {
        let service = TimerServiceImpl(EventEmitterImpl())
        let expected = TimerState(
            isRunning: false,
            milliseconds: 0
        )
        
        #expect(service.state == expected)
    }
    
    @Test func state_shouldStartTimer() async throws {
        let service = TimerServiceImpl(EventEmitterImpl())
        service.start()
        #expect(service.state.milliseconds == 0)
        #expect(service.state.isRunning)
        
        try await Task.sleep(for: .milliseconds(100))
        #expect((90...110).contains(service.state.milliseconds))
    }
    
    @Test func state_shouldPauseTimer() async throws {
        let service = TimerServiceImpl(EventEmitterImpl())
        service.start()
        
        try await Task.sleep(for: .milliseconds(200))
        service.pause()
        
        try await Task.sleep(for: .milliseconds(300))
        #expect((190...210).contains(service.state.milliseconds))
        #expect(!service.state.isRunning)
    }
    
    @Test func state_shouldResetTimer() async throws {
        let service = TimerServiceImpl(EventEmitterImpl())
        service.start()
        
        try await Task.sleep(for: .milliseconds(200))
        #expect((190...210).contains(service.state.milliseconds))
        #expect(service.state.isRunning)
        
        service.pause()
        service.reset()
        
        #expect(!service.state.isRunning)
        #expect(service.state.milliseconds == 0)
    }
}
