struct ViewLap {
    let index: Int
    let time: String
    let status: LapStatus
    
    init(index: Int, time: String, status: LapStatus) {
        self.index = index
        self.time = time
        self.status = status
    }
}
