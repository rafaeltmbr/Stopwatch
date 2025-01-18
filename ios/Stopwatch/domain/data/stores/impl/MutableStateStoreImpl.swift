import Foundation

class MutableStateStoreImpl<T>: MutableStateStore {
    private(set) var state: T
    private(set) var events = EventEmitterImpl<T>()
    
    init(_ initialState: T) {
        state = initialState
    }
    
    func update(_ cb: (T) -> T) async {
        state = cb(state)
        events.emit(state)
    }
}
