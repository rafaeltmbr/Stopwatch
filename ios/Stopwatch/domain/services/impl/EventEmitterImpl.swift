import Foundation

class EventEmitterImpl<T>: EventEmitter {
    private(set) var subscriptions: [UUID: (T) -> Void] = [:]
    
    func susbcribe(_ subscriber: @escaping (T) -> Void) -> UUID {
        let subscriptionId = UUID()
        subscriptions[subscriptionId] = subscriber
        return subscriptionId
    }
    
    func unsubscribe(_ subscriptionId: UUID) {
        subscriptions[subscriptionId] = nil
    }
    
    func unsubscribeAll() {
        subscriptions = [:]
    }
    
    func emit(_ event: T) {
        for (_, subscriber) in subscriptions {
            subscriber(event)
        }
    }
}
