import SwiftUI


@main
struct StopwatchApp: App {
    private(set) var container = ApplicationContainerFactoryImpl().make()
    
    var body: some Scene {
        WindowGroup {
            let _ = container.services.timer.events.susbcribe {timerState in
                Task {
                    await container.stores.stopwatch.update {currentState in
                        return StopwatchState(status: currentState.status, milliseconds: timerState.milliseconds)
                    }
                }
            }

            return HomeView(viewModel: container.presentation.homeViewModelFactory.make())
        }
    }
}
