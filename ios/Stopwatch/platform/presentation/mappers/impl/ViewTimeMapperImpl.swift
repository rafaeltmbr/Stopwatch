class ViewTimeMapperImpl: ViewTimeMapper {
    func map(_ milliseconds: Int) -> ViewTime {
        let time = max(0, milliseconds)
        
        let fraction = String(format: "%02d", Int((Double(time) / 10.0).rounded()) % 100).split(separator: "").map { "\($0)"}
        let seconds = String(format: "%02d", (time / 1_000) % 60).split(separator: "").map { "\($0)"}
        let minutes = String(format: "%02d", (time / 60_000)).split(separator: "").map { "\($0)" }
        
        return ViewTime(
            minutes: minutes,
            seconds: seconds,
            fraction: fraction
        )
    }
}
