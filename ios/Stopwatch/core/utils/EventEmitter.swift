import Foundation

protocol EventSubscriber {
    associatedtype Event
    
    func subscribe(_ subscriber: @escaping (Event) -> Void) -> UUID
    func unsubscribe(_ subscriptionId: UUID)
    func unsubscribeAll()
}

protocol EventEmitter: EventSubscriber {
    associatedtype Event
    
    func emit(_ event: Event)
}
