import SwiftUI

@main
struct StopwatchApp: App {
    var body: some Scene {
        WindowGroup {
            HomeView(viewModel: HomeViewModelFactoryImpl().make())
        }
    }
}
