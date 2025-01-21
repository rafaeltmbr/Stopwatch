import Foundation
import SwiftUI

class StackNavigatorImpl : StackNavigator, ObservableObject {
    @Published var path = NavigationPath()
    
    var size: Int {
        get { path.count }
    }
    
    var isEmpty: Bool {
        get { path.isEmpty }
    }
    
    func push(_ navigationPath: StackNavigatorPath) {
        Task{@MainActor in
            path.append(navigationPath)
        }
    }
    
    func pop() {
        Task{@MainActor in
            path.removeLast()
        }
    }
}
