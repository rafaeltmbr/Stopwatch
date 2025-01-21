protocol LapsViewModelFactory {
    associatedtype ViewModel: LapsViewModel
    associatedtype Navigator: StackNavigator
    
    func make(_ navigator: Navigator) -> ViewModel
}
