import SwiftUI

struct LapsView<ViewModel: LapsViewModel>: View {
    @StateObject var viewModel: ViewModel
    
    var body: some View {
        NavigationStack {
            VStack(alignment: .leading) {
                ForEach(viewModel.state.laps, id: \.index) {lap in
                    HStack {
                        Text("#\(lap.index)").foregroundStyle(.gray)
                        Text(lap.time).font(.title2)
                        Spacer().frame(maxWidth: .infinity)
                        Text("\(lap.status)").foregroundStyle(.gray)
                    }.padding(4)
                }
                Spacer()
            }.padding().frame(minHeight: 200)
                .navigationTitle("Laps")
                .toolbar {
                    Button("00:00.00", action: {
                        let action: LapsAction = switch(viewModel.state.status) {
                        case .initial, .paused: .resume
                        case .running: .resume
                        }
                        viewModel.handleAction(action)
                    })
                }
        }
    }
}

#Preview {
    let container = ApplicationContainerFactoryImpl().make()
    
    Task {
        await container.stores.stopwatch.update {_ in
            StopwatchState(
                status: .paused,
                milliseconds: 365823,
                laps: [
                    Lap(index: 1, milliseconds: 3571, status: .worst),
                    Lap(index: 2, milliseconds: 1123, status: .best),
                    Lap(index: 3, milliseconds: 1139, status: .done),
                    Lap(index: 4, milliseconds: 242, status: .current),
                ]
            )
        }
    }
    
    return LapsView(viewModel: container.presentation.lapsViewModelFactory.make())
}
