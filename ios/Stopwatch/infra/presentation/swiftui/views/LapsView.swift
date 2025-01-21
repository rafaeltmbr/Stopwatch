import SwiftUI

struct LapsView<ViewModel: LapsViewModel>: View {
    @StateObject var viewModel: ViewModel
    
    var body: some View {
        ScrollView {
            ForEach(viewModel.state.laps, id: \.index) {lap in
                HStack {
                    let color: Color? = switch lap.status {
                    case .best: Color.green
                    case .worst: Color.red
                    default: nil
                    }
                    
                    Text("#\(lap.index)").foregroundStyle(.gray).monospaced()
                    
                    if let color = color {
                        Text(lap.time).font(.title2).foregroundStyle(color).monospaced()
                    } else {
                        Text(lap.time).font(.title2).monospaced()
                    }
                    
                    Spacer().frame(maxWidth: .infinity)
                    
                    if let color = color {
                        Text("\(lap.status)").foregroundStyle(color)
                    }
                }.padding(.vertical, 2).padding(.horizontal)
            }
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
    
    let _ = container.services.timer.events.susbcribe {timerState in
        Task {
            await container.useCases.updateStopwatchTimeAndLaps.execute(timerState)
        }
    }

    return NavigationStack {
        LapsView(viewModel: container.presentation.lapsViewModelFactory.make(StackNavigatorImpl()))
    }
}
