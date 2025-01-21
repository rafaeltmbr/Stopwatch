import Foundation

enum StackNavigatorPath {
    case home, laps
}

protocol StackNavigator: ObservableObject {
    var size: Int { get }
    var isEmpty: Bool { get }
    
    func push(_ path: StackNavigatorPath)
    func pop()
}
