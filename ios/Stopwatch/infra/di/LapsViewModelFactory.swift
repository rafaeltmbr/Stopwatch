protocol LapsViewModelFactory {
    associatedtype ViewModel: LapsViewModel
    
    func make() -> ViewModel
}
