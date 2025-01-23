import SwiftUI

struct LapsView<ViewModel: LapsViewModel>: View {
    @StateObject var viewModel: ViewModel
    @Environment(\.colorScheme) var colorScheme: ColorScheme
    
    var body: some View {
        ScrollView {
            Spacer()
            LapsList(laps: viewModel.state.laps).padding(.vertical, 2).padding(.horizontal)
        }
        .navigationTitle("Laps")
        .toolbar {
            let action: LapsAction = viewModel.state.status == .running ? .lap : .resume
            
            Button(action: { viewModel.handleAction(action) }) {
                Text(viewModel.state.milliseconds).monospaced()
                Image(systemName: viewModel.state.status == .running ? "plus" : "play")
            }
        }
    }
}

#Preview {
    let container = ApplicationContainerFactoryImpl().make()
    
    Task {
        await container.data.stopwatchStore.update {_ in
            StopwatchState(
                status: .paused,
                milliseconds: 365823,
                completedLaps: [
                    Lap(index: 1, milliseconds: 3571, status: .worst),
                    Lap(index: 2, milliseconds: 1123, status: .best),
                    Lap(index: 3, milliseconds: 1139, status: .done),
                    Lap(index: 4, milliseconds: 242, status: .current),
                ],
                completedLapsMilliseconds: 6075
            )
        }
    }
    
    let _ = container.services.timer.events.subscribe {timerState in
        Task {
            await container.useCases.updateStopwatchTimeAndLaps.execute(timerState)
        }
    }
    
    return NavigationStack {
        LapsView(viewModel: container.presentation.lapsViewModelFactory.make())
    }
}
