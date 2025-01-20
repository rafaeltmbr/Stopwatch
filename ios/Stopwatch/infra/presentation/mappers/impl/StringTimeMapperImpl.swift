class StringTimeMapperImpl: StringTimeMapper {
    var viewTimeMapper: ViewTimeMapper
    
    init(_ viewTimeMapper: ViewTimeMapper) {
        self.viewTimeMapper = viewTimeMapper
    }
    
    func map(_ milliseconds: Int) -> String {
        let viewTime = viewTimeMapper.map(milliseconds)
        let minutes = viewTime.minutes.joined()
        let seconds = viewTime.seconds.joined()
        let fraction = viewTime.fraction.joined()
        return "\(minutes):\(seconds).\(fraction)"
    }
}
