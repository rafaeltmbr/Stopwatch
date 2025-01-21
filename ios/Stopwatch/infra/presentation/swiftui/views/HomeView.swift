import Foundation
import SwiftUI


struct HomeView<ViewModel>: View where ViewModel: HomeViewModel {
    @StateObject var viewModel: ViewModel
    
    var body: some View {
        VStack {
            VStack {
                timer.padding(.bottom)
                buttons
            }.frame(maxHeight: .infinity)
            
            if viewModel.state.showLaps {
                laps
            }
        }
        .navigationTitle("Stopwatch")
    }
    
    private var timer: some View {
        HStack(alignment: .bottom, spacing: 0) {
            let minutes = viewModel.state.time.minutes.enumerated().map { ($0, $1)}
            ForEach(minutes, id: \.0) {
                Text($1)
                    .font(.system(size: 80, weight: Font.Weight.light))
                    .monospaced()
            }
            
            Text(":")
                .font(.system(size: 80, weight: Font.Weight.light))
                .padding(.leading, 4)
            
            let seconds = viewModel.state.time.seconds.enumerated().map { ($0, $1)}
            ForEach(seconds, id: \.0) {
                Text($1)
                    .font(.system(size: 80, weight: Font.Weight.light))
                    .monospaced()
            }
            
            Text(".")
                .font(.system(size: 70, weight: Font.Weight.light))
                .padding(.leading, 4)
            
            let fraction = viewModel.state.time.fraction.enumerated().map { ($0, $1)}
            ForEach(fraction, id: \.0) {
                Text($1)
                    .font(.system(size: 40)).padding(.bottom, 8)
                    .monospaced()
            }
        }
    }
    
    private var buttons: some View {
        HStack {
            switch (viewModel.state.status) {
            case .initial: HStack {
                Button("Start") { viewModel.handleAction(.start) }
            }
            case .running: HStack(spacing: 40) {
                Button("Pause") { viewModel.handleAction(.pause) }
                Button("Lap") { viewModel.handleAction(.lap)}
                    .foregroundStyle(.green)
            }
            case .paused: HStack(spacing: 40) {
                Button("Resume") { viewModel.handleAction(.resume) }
                Button("Reset") { viewModel.handleAction(.reset) }
                    .foregroundStyle(.red)
            }
            }
        }
    }
    
    private var laps: some View {
        HStack(alignment: .bottom) {
            VStack(alignment: .leading) {
                HStack {
                    Text("Laps").font(.title2)
                    Spacer()
                    if viewModel.state.showSeeAll {
                        HStack {
                            Button(action: { viewModel.handleAction(.seeAll)}) {
                                Text("Sell All")
                                Image(systemName: "chevron.right")
                            }
                        }
                    }
                }
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
                    }.padding(.vertical, 2)
                }
            }
        }.padding().frame(minHeight: 200)
    }
}


#Preview {
    let container = ApplicationContainerFactoryImpl().make()
    
    let _ = container.services.timer.events.susbcribe {timerState in
        Task {
            await container.useCases.updateStopwatchTimeAndLaps.execute(timerState)
        }
    }
    
    return NavigationStack {
        HomeView(viewModel: container.presentation.homeViewModelFactory.make(StackNavigatorImpl()))
    }
}
