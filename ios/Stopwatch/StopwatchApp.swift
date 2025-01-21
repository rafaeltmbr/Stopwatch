import SwiftUI


@main
struct StopwatchApp: App {
    private(set) var container = ApplicationContainerFactoryImpl().make()
    
    var body: some Scene {
        WindowGroup {
            let _ = Task {
                await self.container.useCases.restoreStopwatchState.execute()
            }
            
            let _ = container.services.timer.events.susbcribe {timerState in
                Task {
                    await container.useCases.updateStopwatchTimeAndLaps.execute(timerState)
                }
            }
            
            let _ = container.stores.stopwatch.events.susbcribe {_ in
                Task {
                    await container.useCases.saveStopwatchState.execute()
                }
            }
            
            ViewsRouter(
                container.presentation.homeViewModelFactory,
                container.presentation.lapsViewModelFactory
            )
        }
    }
}
