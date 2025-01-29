import Foundation

enum StackNavigatorScreen {
    case home, laps
}

protocol StackNavigator{
    associatedtype Events: EventSubscriber where Events.Event == [StackNavigatorScreen]
    
    var stack: [StackNavigatorScreen] { get }
    var events: Events { get }
    
    func push(_ screen: StackNavigatorScreen)
    func pop()
}
