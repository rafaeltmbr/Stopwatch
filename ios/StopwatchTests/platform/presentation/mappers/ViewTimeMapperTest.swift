import Testing
@testable import Stopwatch

struct ViewTimeTest {
    @Test func mapFractions_paddingMinutesAndSecondsWithZeros() {
        let mapper = ViewTimeMapperImpl()
        let milliseconds = 760
        let expected = ViewTime(
            minutes: ["0", "0"],
            seconds: ["0", "0"],
            fraction: ["7", "6"]
        )
        let viewTime = mapper.map(milliseconds)
        #expect(viewTime == expected)
    }
    
    @Test func mapFractions_mapToClosestHundredthPart() {
        let mapper = ViewTimeMapperImpl()
        let milliseconds = 477
        let expected = ViewTime(
            minutes: ["0", "0"],
            seconds: ["0", "0"],
            fraction: ["4", "8"]
        )
        let viewTime = mapper.map(milliseconds)
        #expect(viewTime == expected)
    }
    
    @Test func mapSeconds_paddingMinutesWithZeros() {
        let mapper = ViewTimeMapperImpl()
        let milliseconds = 21_520
        let expected = ViewTime(
            minutes: ["0", "0"],
            seconds: ["2", "1"],
            fraction: ["5", "2"]
        )
        let viewTime = mapper.map(milliseconds)
        #expect(viewTime == expected)
    }
    
    @Test func mapMinutes_paddingTwoDigitsWithZeros() {
        let mapper = ViewTimeMapperImpl()
        let milliseconds = 107_120
        let expected = ViewTime(
            minutes: ["0", "1"],
            seconds: ["4", "7"],
            fraction: ["1", "2"]
        )
        let viewTime = mapper.map(milliseconds)
        #expect(viewTime == expected)
    }
    
    @Test func mapMinutes_shouldNotTruncateMinutesIntoHours() {
        let mapper = ViewTimeMapperImpl()
        let milliseconds = 4_107_430
        let expected = ViewTime(
            minutes: ["6", "8"],
            seconds: ["2", "7"],
            fraction: ["4", "3"]
        )
        let viewTime = mapper.map(milliseconds)
        #expect(viewTime == expected)
    }
    
    @Test func map_shouldMapNegativeTimeToZero() {
        let mapper = ViewTimeMapperImpl()
        let milliseconds = -4_107_430
        let expected = ViewTime(
            minutes: ["0", "0"],
            seconds: ["0", "0"],
            fraction: ["0", "0"]
        )
        let viewTime = mapper.map(milliseconds)
        #expect(viewTime == expected)
    }
}
