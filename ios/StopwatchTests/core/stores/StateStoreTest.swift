import Testing
import Foundation
@testable import Stopwatch

struct StateStoreTest {
    @Test func state_shouldBeInitializedWithInitialValue() async throws {
        let initial = StopwatchState()
        let store = MutableStateStoreImpl(initial, EventEmitterImpl())
        #expect(store.state == initial)
    }
    
    @Test func update_shouldPassTheCorrectStateToTheUpdateCallback() async throws {
        let initial = UUID()
        let store = MutableStateStoreImpl(initial, EventEmitterImpl())
        
        await store.update {current in
            #expect(current == initial)
            return current
        }
    }
    
    @Test func update_shouldUpdateStateImmediatly() async throws {
        let store = MutableStateStoreImpl(UUID(), EventEmitterImpl())
        let nextState = UUID()
        await store.update { _ in nextState }
        #expect(nextState == store.state)
    }
    
    @Test func update_shouldEmitStateChanges() async throws {
        let store = MutableStateStoreImpl("Initial value", EventEmitterImpl())
        var emitted = ""
        
        let subscription = store.events.subscribe {newState in
            emitted = newState
        }
        
        await store.update { current in current.uppercased() }
        #expect(store.state == emitted)
        
        store.events.unsubscribe(subscription)
        await store.update { current in current.lowercased() }
        #expect(store.state != emitted)
    }
}
