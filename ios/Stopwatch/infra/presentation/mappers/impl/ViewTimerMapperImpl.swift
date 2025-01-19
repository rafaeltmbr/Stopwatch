class ViewTimerMapperImpl: ViewTimeMapper {
    func map(_ milliseconds: Int) -> ViewTime {
        let fraction = String(format: "%02d", milliseconds / 10 % 100).split(separator: "").map { "\($0)"}
        let seconds = String(format: "%02d", (milliseconds / 1_000) % 60).split(separator: "").map { "\($0)"}
        let minutes = String(format: "%02d", (milliseconds / 60_000)).split(separator: "").map { "\($0)" }
        
        return ViewTime(
            minutes: minutes,
            seconds: seconds,
            fraction: fraction
        )
    }
}
