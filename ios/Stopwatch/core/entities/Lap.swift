enum LapStatus {
    case current, best, worst, done
}
struct Lap: Equatable {
    let index: Int
    let milliseconds: Int
    let status: LapStatus
}
