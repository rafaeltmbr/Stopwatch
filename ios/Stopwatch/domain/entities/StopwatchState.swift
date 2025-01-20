struct StopwatchState {
    let status: Status
    let milliseconds: Int
    let laps: [Lap]
    
    init(status: Status = .initial, milliseconds: Int = 0, laps: [Lap] = []) {
        self.status = status
        self.milliseconds = milliseconds
        self.laps = laps
    }
}
