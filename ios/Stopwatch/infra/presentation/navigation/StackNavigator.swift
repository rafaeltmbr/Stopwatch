import Foundation

enum StackNavigatorPath {
    case home, laps
}

protocol StackNavigator{
    associatedtype Events: EventSubscriber where Events.Event == [StackNavigatorPath]
    
    var stack: [StackNavigatorPath] { get }
    var events: Events { get }
    
    func push(_ path: StackNavigatorPath)
    func pop()
}
