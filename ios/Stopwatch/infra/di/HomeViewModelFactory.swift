protocol HomeViewModelFactory {
    associatedtype ViewModel: HomeViewModel
    associatedtype Navigator: StackNavigator
    
    func make(_ navigator: Navigator) -> ViewModel
}
