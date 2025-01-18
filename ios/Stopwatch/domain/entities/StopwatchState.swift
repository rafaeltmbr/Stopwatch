struct StopwatchState {
    let status: Status
    let milliseconds: Int
    
    init(status: Status = .initial, milliseconds: Int = 0) {
        self.status = status
        self.milliseconds = milliseconds
    }
}
