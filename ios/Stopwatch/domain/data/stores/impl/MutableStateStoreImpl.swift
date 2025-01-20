import Foundation

class MutableStateStoreImpl<T, EE>: MutableStateStore where EE: EventEmitter, EE.Event == T {
    private(set) var state: T
    private(set) var events: EE
    
    init(_ initialState: T, _ eventEmitter: EE) {
        state = initialState
        events = eventEmitter
    }
    
    func update(_ cb: (T) -> T) async {
        state = cb(state)
        events.emit(state)
    }
}
