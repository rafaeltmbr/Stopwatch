import SwiftUI


struct HomeView<ViewModel>: View where ViewModel: HomeViewModel {
    @StateObject var viewModel: ViewModel
    
    var body: some View {
        NavigationView {
            VStack {
                Text("\(viewModel.state.time) ms").font(.system(size: 40))
            }
            .padding()
            .navigationTitle("Stopwatch")
        }
    }
}

#Preview {
    HomeView(viewModel: HomeViewModelFactoryImpl().make())
}
