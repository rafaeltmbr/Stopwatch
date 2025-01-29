enum LapStatus {
    case current, best, worst, done
}
struct Lap {
    let index: Int
    let milliseconds: Int
    let status: LapStatus
    
    init(index: Int, milliseconds: Int, status: LapStatus) {
        self.index = index
        self.milliseconds = milliseconds
        self.status = status
    }
}
