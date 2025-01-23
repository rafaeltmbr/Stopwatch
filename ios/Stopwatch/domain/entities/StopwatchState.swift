struct StopwatchState {
    let status: Status
    let milliseconds: Int
    let completedLaps: [Lap]
    let completedLapsMilliseconds: Int
    
    init(
        status: Status = .initial,
        milliseconds: Int = 0,
        completedLaps: [Lap] = [],
        completedLapsMilliseconds: Int = 0
    ) {
        self.status = status
        self.milliseconds = milliseconds
        self.completedLaps = completedLaps
        self.completedLapsMilliseconds = completedLapsMilliseconds
    }
}
