import SwiftUI

struct ViewsRouter<HVMF, LVMF>: View
where
    HVMF: HomeViewModelFactory,
    LVMF: LapsViewModelFactory,
    HVMF.Navigator == StackNavigatorImpl,
    LVMF.Navigator == StackNavigatorImpl
{
    @StateObject var stackNavigator = StackNavigatorImpl()
    private(set) var homeViewModelFactory: HVMF
    private(set) var lapsViewModelFactory: LVMF
    
    init(_ homeViewModelFactory: HVMF, _ lapsViewModelFactory: LVMF) {
        self.homeViewModelFactory = homeViewModelFactory
        self.lapsViewModelFactory = lapsViewModelFactory
    }
    
    var body: some View {
        NavigationStack(path: $stackNavigator.path) {
            HomeView(viewModel: homeViewModelFactory.make(stackNavigator))
                .navigationDestination(for: StackNavigatorPath.self) { path in
                    switch path {
                    case .home: EmptyView()
                    case .laps: LapsView(viewModel: lapsViewModelFactory.make(stackNavigator))
                    }
                }
        }
    }
}

#Preview {
    let container = ApplicationContainerFactoryImpl().make()
    
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
