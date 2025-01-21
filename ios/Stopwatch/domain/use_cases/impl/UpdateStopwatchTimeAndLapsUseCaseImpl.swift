class UpdateStopwatchTimeAndLapsUseCaseImpl<MSS>: UpdateStopwatchTimeAndLapsUseCase
where MSS: MutableStateStore, MSS.State == StopwatchState {
    private let store: MSS
    
    init(_ store: MSS) {
        self.store = store
    }
    
    func execute(_ timerState: TimerState) async {
        await store.update {
            StopwatchState(
                status: $0.status,
                milliseconds: timerState.milliseconds,
                laps: getNextLaps(timerState.milliseconds, $0.laps)
            )
        }
    }
    
    private func getNextLaps(_ milliseconds: Int, _ laps: [Lap]) -> [Lap] {
        if laps.count < 2 {
            return [
                Lap(
                    index: 1,
                    milliseconds: milliseconds,
                    status: .current
                )
            ]
        }
        
        let currentLap = Lap(
            index: laps.last!.index,
            milliseconds: milliseconds - laps[0..<laps.count-1].reduce(0) { $0 + $1.milliseconds },
            status: .current
        )
        
        let lapsWithTimeUpdated = laps.map { $0.index == laps.last!.index ? currentLap : $0 }
        
        return Self.updateLapsStatuses(lapsWithTimeUpdated)
    }
    
    static func updateLapsStatuses(_ laps: [Lap]) -> [Lap] {
        if laps.count < 3 {
            return laps.map {
                Lap(
                    index: $0.index,
                    milliseconds: $0.milliseconds,
                    status: $0.index == laps.last!.index ? .current : .done
                )
            }
        }
        
        let current = laps.last!
        var best = laps[0]
        var worst = laps[0]
        
        for lap in laps[0..<laps.count-1] {
            if lap.milliseconds < best.milliseconds {
                best = lap
            } else if lap.milliseconds > worst.milliseconds && laps.count > 2 {
                worst = lap
            }
        }
        
        return laps.map {
            let status: LapStatus = switch($0.index) {
                case current.index: .current
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
