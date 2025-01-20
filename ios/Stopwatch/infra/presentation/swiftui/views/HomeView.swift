import Foundation
import SwiftUI


struct HomeView<ViewModel>: View where ViewModel: HomeViewModel {
    @StateObject var viewModel: ViewModel
    
    var body: some View {
        NavigationView {
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
    }
    
    private var timer: some View {
        HStack(alignment: .bottom, spacing: 0) {
            let minutes = viewModel.state.time.minutes.enumerated().map { ($0, $1)}
            ForEach(minutes, id: \.0) {
                Text($1)
                    .font(.system(size: 70, weight: Font.Weight.light))
                    .frame(width: 40)
            }
            
            Text(":")
                .font(.system(size: 70, weight: Font.Weight.light))
                .padding(.leading, 4)
            
            let seconds = viewModel.state.time.seconds.enumerated().map { ($0, $1)}
            ForEach(seconds, id: \.0) {
                Text($1)
                    .font(.system(size: 70, weight: Font.Weight.light))
                    .frame(width: 40)
            }
            
            Text(".")
                .font(.system(size: 70, weight: Font.Weight.light))
                .padding(.leading, 4)
            
            let fraction = viewModel.state.time.fraction.enumerated().map { ($0, $1)}
            ForEach(fraction, id: \.0) {
                Text($1)
                    .font(.system(size: 36)).padding(.bottom, 8)
                    .frame(width: 24)
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
                Text("Laps").font(.title2)
                ForEach(viewModel.state.laps, id: \.index) {lap in
                    HStack {
                        Text("#\(lap.index)").foregroundStyle(.gray)
                        Text(lap.time).font(.title2)
                        Spacer().frame(maxWidth: .infinity)
                        Text("\(lap.status)").foregroundStyle(.gray)
                    }.padding(4)
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
    
    return HomeView(viewModel: container.presentation.homeViewModelFactory.make())
}
