import Testing
@testable import Stopwatch

struct NewLapUseCaseTest {
    @Test func newLap_shouldCreateNewLap() async throws {
        let store = MutableStateStoreImpl(
            StopwatchState(
                status: .running,
                milliseconds: 20
            ),
            EventEmitterImpl()
        )
        let useCase = NewLapUseCaseImpl(store, CalculateLapsStatusesImpl())
        
        await useCase.execute()
        
        let expected = StopwatchState(
            status: .running,
            milliseconds: 20,
            completedLaps: [
                Lap(
                    index: 1,
                    milliseconds: 20,
                    status: .done
                )
            ],
            completedLapsMilliseconds: 20
        )
        
        #expect(store.state == expected)
    }
    
    @Test func newLap_appendNewLapAndUpdateStatus() async throws {
        let store = MutableStateStoreImpl(
            StopwatchState(
                status: .running,
                milliseconds: 1_800,
                completedLaps: [
                    Lap(
                        index: 1,
                        milliseconds: 1_000,
                        status: .done
                    )
                ],
                completedLapsMilliseconds: 1_000
            ),
            EventEmitterImpl()
        )
        let useCase = NewLapUseCaseImpl(store, CalculateLapsStatusesImpl())
        
        await useCase.execute()
        
        let expected = StopwatchState(
            status: .running,
            milliseconds: 1_800,
            completedLaps: [
                Lap(
                    index: 1,
                    milliseconds: 1_000,
                    status: .worst
                ),
                Lap(
                    index: 2,
                    milliseconds: 800,
                    status: .best
                )
            ],
            completedLapsMilliseconds: 1_800
        )
        
        #expect(store.state == expected)
    }
}
