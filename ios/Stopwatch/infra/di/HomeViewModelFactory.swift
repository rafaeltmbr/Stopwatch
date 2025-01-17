import Foundation

protocol HomeViewModelFactory {
    associatedtype ViewModel: HomeViewModel
    
    func make() -> ViewModel
}
