import SwiftUI

struct LapsList: View {
    @Environment(\.colorScheme) var colorScheme: ColorScheme
    var laps: [ViewLap]
    
    var body: some View {
        VStack {
            ForEach(laps, id: \.index) {lap in
                if lap.index != laps.first?.index {
                    let opacity = self.colorScheme == .light ? 0.05 : 0.12
                    
                    HStack{}
                        .frame(height: 1)
                        .frame(maxWidth: .infinity)
                        .background(Color(UIColor.label).opacity(opacity))
                        .padding(.vertical, 4)
                }
                
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
                }
            }
        }
    }
}
