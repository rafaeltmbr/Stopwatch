import Foundation


protocol StateStore {
    associatedtype State
    associatedtype Events: EventSubscriber where Events.Event == State
    
    var state: State { get }
    var events: Events { get }
}

protocol MutableStateStore: StateStore {
    associatedtype State
    
    func update(_ cb: (State) -> State) async
}
