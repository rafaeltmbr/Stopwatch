import SwiftUI


@main
struct StopwatchApp: App {
    private(set) var container = ApplicationContainerFactoryImpl().make()
    
    var body: some Scene {
        WindowGroup {
            let _ = container.services.timer.events.susbcribe {timerState in
                Task {
                    await container.useCases.updateStopwatchTimeAndLaps.execute(timerState)
                }
            }
            
            ViewsRouter(
                container.presentation.homeViewModelFactory,
                container.presentation.lapsViewModelFactory
            )
        }
    }
}
