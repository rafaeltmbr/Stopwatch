class CalculateLapsStatusesImpl: CalculateLapsStatuses {
    func execute(_ laps: [Lap]) -> [Lap] {
        if laps.count < 2 {
            return laps.map {
                Lap(
                    index: $0.index,
                    milliseconds: $0.milliseconds,
                    status: .done
                )
            }
        }
        
        var best = laps[0]
        var worst = laps[0]
        
        for lap in laps {
            if lap.milliseconds < best.milliseconds {
                best = lap
            } else if lap.milliseconds > worst.milliseconds {
                worst = lap
            }
        }
        
        return laps.map {
            let status: LapStatus = switch($0.index) {
            case best.index: .best
            case worst.index: .worst
            default: .done
            }
            
            return Lap(
                index: $0.index,
                milliseconds: $0.milliseconds,
                status: status
            )
        }
    }
}
