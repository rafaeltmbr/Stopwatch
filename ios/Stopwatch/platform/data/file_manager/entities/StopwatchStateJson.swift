struct StopwatchStateJson: Codable {
    let status: Int
    let milliseconds: Int
    let laps: [LapJson]
    
    init(status: Int, milliseconds: Int, laps: [LapJson]) {
        self.status = status
        self.milliseconds = milliseconds
        self.laps = laps
    }
}
