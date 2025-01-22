import Foundation

class StackNavigatorImpl<EE> : StackNavigator
where EE: EventEmitter, EE.Event == [StackNavigatorPath]
{
    private(set) var stack: [StackNavigatorPath]
    private(set) var events: EE
    
    init(stack: [StackNavigatorPath], eventEmitter: EE) {
        self.stack = stack
        self.events = eventEmitter
    }
    
    func push(_ navigationPath: StackNavigatorPath) {
        guard !stack.contains(navigationPath) else { return }
        
        var newStack = stack.map { $0 }
        newStack.append(navigationPath)
        update(newStack)
    }
    
    func pop() {
        guard stack.count > 1 else { return }
        
        var newStack = stack.map { $0 }
        newStack.removeLast()
        update(newStack)
    }
    
    private func update(_ stack: [StackNavigatorPath]) {
        self.stack = stack
        events.emit(stack)
    }
}
