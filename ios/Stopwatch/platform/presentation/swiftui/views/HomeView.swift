import Foundation
import SwiftUI


struct HomeView<ViewModel>: View where ViewModel: HomeViewModel {
    @StateObject var viewModel: ViewModel
    @Environment(\.colorScheme) var colorScheme: ColorScheme
    
    var body: some View {
        GeometryReader {geometry in
            let isLandscape = geometry.size.width > geometry.size.height
            
            switch isLandscape {
            case true: landscapeLayout
            case false: portraitLayout
            }
        }
        .navigationTitle("Stopwatch")
    }
    
    private var landscapeLayout: some View {
        HStack {
            VStack {
                timer.padding(.bottom)
                buttons
            }.frame(maxHeight: .infinity)
            
            if viewModel.state.showLaps {
                laps.transition(.move(edge: .bottom).combined(with: .opacity))
            }
        }.frame(maxWidth: .infinity)
            .animation(.default, value: viewModel.state.showLaps)
    }

    private var portraitLayout: some View {
        VStack {
            VStack {
                timer.padding(.bottom)
                buttons
            }.frame(maxHeight: .infinity)

            
            if viewModel.state.showLaps {
                laps.transition(.move(edge: .bottom).combined(with: .opacity))
            }
        }.frame(maxWidth: .infinity)
            .animation(.default, value: viewModel.state.showLaps)
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
                ButtonView(.start) { viewModel.handleAction(.start) }
            }
            case .running: HStack {
                Spacer()
                HStack {
                    ButtonView(.pause) { viewModel.handleAction(.pause) }
                }
                
                Spacer()
                HStack {
                    ButtonView(.lap) { viewModel.handleAction(.lap)}
                }
                
                Spacer()
            }
            case .paused: HStack {
                Spacer()
                HStack {
                    ButtonView(.resume) { viewModel.handleAction(.resume) }
                }
                
                Spacer()
                HStack {
                    ButtonView(.reset) { viewModel.handleAction(.reset) }
                }
                
                Spacer()
            }
            }
        }.padding(.horizontal)
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
                
                VStack {
                    ForEach(viewModel.state.laps, id: \.index) {lap in
                        LapsListItem(lap: lap, isFirst: lap.status == .current)
                    }
                }.padding(.top, 4)
            }
        }.padding().frame(minHeight: 220)
    }
}


#Preview {
    let container = ApplicationContainerFactoryImpl().make()
    
    let _ = container.services.timer.events.subscribe {timerState in
        Task {
            await container.useCases.updateStopwatchTimeAndLaps.execute(timerState)
        }
    }
    
    return NavigationStack {
        HomeView(viewModel: container.presentation.homeViewModelFactory.make())
    }
}


private enum ButtonType {
    case start, pause, resume, reset, lap
}

private struct ButtonView: View {
    private var type: ButtonType
    private var onAction: () -> Void
    
    init(_ type: ButtonType, _ onAction: @escaping () -> Void) {
        self.type = type
        self.onAction = onAction
    }
    
    
    var body: some View {
        let (text, image, color, size) = switch type {
        case .start: ("start", "play", Color.blue, 26.0)
        case .pause: ("pause", "pause", Color.blue, 22.0)
        case .resume: ("resume", "play", Color.blue, 22.0)
        case .reset: ("reset", "arrow.trianglehead.clockwise", Color.red, 20.0)
        case .lap: ("lap", "plus", Color.green, 22.0)
        }
        
        return Button(action: onAction) {
            VStack(spacing: 4) {
                Image(systemName: image).font(.system(size: size))
                Text(text).font(.system(size: 13))
            }
            .frame(width: 80, height: 80)
            .background(.ultraThickMaterial)
            .background(color.opacity(0.5))
            .foregroundColor(color)
            .clipShape(Circle())
        }
    }
}

#Preview {
    ButtonView(.start) {}
}
