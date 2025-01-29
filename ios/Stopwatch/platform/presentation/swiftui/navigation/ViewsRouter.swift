import SwiftUI

struct ViewsRouter<HVMF, LVMF, SN>: View
where
HVMF: HomeViewModelFactory,
LVMF: LapsViewModelFactory,
SN: StackNavigatorImpl<EventEmitterImpl<[StackNavigatorScreen]>>
{
    @StateObject var navigatorAdapter: StackNavigatorAdapter
    private let lapsViewModelFactory: LVMF
    private let homeViewModel: HVMF.ViewModel
    
    init(_ homeViewModelFactory: HVMF, _ lapsViewModelFactory: LVMF, _ stackNavigator: SN) {
        _navigatorAdapter = StateObject(wrappedValue: StackNavigatorAdapter(stackNavigator))
        self.lapsViewModelFactory = lapsViewModelFactory
        homeViewModel = homeViewModelFactory.make()
    }
    
    var body: some View {
        NavigationStack(path: navigatorAdapter.pathBinding) {
            HomeView(viewModel: homeViewModel)
                .navigationDestination(for: StackNavigatorScreen.self) { path in
                    switch path {
                    case .home: EmptyView()
                    case .laps: LapsView(viewModel: lapsViewModelFactory.make())
                    }
                }
        }
    }
}

class StackNavigatorAdapter: ObservableObject {
    @Published private(set) var path = NavigationPath()
    let stackNavigator: StackNavigatorImpl<EventEmitterImpl<[StackNavigatorScreen]>>
    private var subscription: UUID? = nil
    
    init(_ stackNavigator: StackNavigatorImpl<EventEmitterImpl<[StackNavigatorScreen]>>) {
        self.stackNavigator = stackNavigator
        self.subscription = stackNavigator.events.subscribe {stack in
            Task {@MainActor in
                if self.path.count < stack.count - 1 {
                    self.path.append(stack.last!)
                } else if self.path.count > stack.count - 1 {
                    self.path.removeLast()
                }
            }
        }
    }
    
    deinit {
        if let id = subscription {
            stackNavigator.events.unsubscribe(id)
        }
    }
    
    var pathBinding: Binding<NavigationPath> {
        get {
            Binding<NavigationPath>(
                get: { self.path },
                set: {
                    if $0.count < self.path.count {
                        self.stackNavigator.pop()
                    }
                    
                    self.path = $0
                }
            )
        }
    }
}

#Preview {
    let container = ApplicationContainerFactoryImpl().make()
    
    let _ = container.services.timer.events.subscribe {timerState in
        Task {
            await container.useCases.updateStopwatchTimeAndLaps.execute(timerState)
        }
    }
    
    ViewsRouter(
        container.presentation.homeViewModelFactory,
        container.presentation.lapsViewModelFactory,
        container.presentation.stackNavigator
    )
}
