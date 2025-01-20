struct ViewLap {
    let index: Int
    let time: String
    let status: LapStatus
    
    init(_ index: Int, _ time: String, _ status: LapStatus) {
        self.index = index
        self.time = time
        self.status = status
    }
}
