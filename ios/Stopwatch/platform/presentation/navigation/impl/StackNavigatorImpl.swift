import Foundation

class StackNavigatorImpl<EE> : StackNavigator
where EE: EventEmitter, EE.Event == [StackNavigatorScreen]
{
    private(set) var stack: [StackNavigatorScreen]
    private(set) var events: EE
    
    init(stack: [StackNavigatorScreen], eventEmitter: EE) {
        self.stack = stack
        self.events = eventEmitter
    }
    
    func push(_ screen: StackNavigatorScreen) {
        guard !stack.contains(screen) else { return }
        
        var newStack = stack.map { $0 }
        newStack.append(screen)
        update(newStack)
    }
    
    func pop() {
        guard stack.count > 1 else { return }
        
        var newStack = stack.map { $0 }
        newStack.removeLast()
        update(newStack)
    }
    
    private func update(_ stack: [StackNavigatorScreen]) {
        self.stack = stack
        events.emit(stack)
    }
}
