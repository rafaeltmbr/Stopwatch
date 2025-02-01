import Testing
@testable import Stopwatch

struct StringTimeTest {
    @Test func mapFractions_paddingMinutesAndSecondsWithZeros() {
        let mapper = StringTimeMapperImpl(ViewTimeMapperImpl())
        let milliseconds = 760
        let expected = "00:00.76"
        let stringTime = mapper.map(milliseconds)
        #expect(stringTime == expected)
    }
    
    @Test func mapFractions_mapToClosestHundredthPart() {
        let mapper = StringTimeMapperImpl(ViewTimeMapperImpl())
        let milliseconds = 477
        let expected = "00:00.48"
        let stringTime = mapper.map(milliseconds)
        #expect(stringTime == expected)
    }
    
    @Test func mapSeconds_paddingMinutesWithZeros() {
        let mapper = StringTimeMapperImpl(ViewTimeMapperImpl())
        let milliseconds = 21_520
        let expected = "00:21.52"
        let stringTime = mapper.map(milliseconds)
        #expect(stringTime == expected)
    }
    
    @Test func mapMinutes_paddingTwoDigitsWithZeros() {
        let mapper = StringTimeMapperImpl(ViewTimeMapperImpl())
        let milliseconds = 107_120
        let expected = "01:47.12"
        let stringTime = mapper.map(milliseconds)
        #expect(stringTime == expected)
    }
    
    @Test func mapMinutes_shouldNotTruncateMinutesIntoHours() {
        let mapper = StringTimeMapperImpl(ViewTimeMapperImpl())
        let milliseconds = 4_107_430
        let expected = "68:27.43"
        let stringTime = mapper.map(milliseconds)
        #expect(stringTime == expected)
    }
    
    @Test func map_shouldMapNegativeTimeToZero() {
        let mapper = StringTimeMapperImpl(ViewTimeMapperImpl())
        let milliseconds = -4_107_430
        let expected = "00:00.00"
        let viewTime = mapper.map(milliseconds)
        #expect(viewTime == expected)
    }
}
