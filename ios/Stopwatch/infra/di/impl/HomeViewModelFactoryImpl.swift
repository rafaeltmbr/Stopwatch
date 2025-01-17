import Foundation

class HomeViewModelFactoryImpl: HomeViewModelFactory {
    func make() -> some HomeViewModel {
        HomeViewModelImpl()
    }
}
