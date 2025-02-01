import Testing
@testable import Stopwatch

struct UpdateStopwatchTimeUseCaseTest {
    @Test func update_shouldCorrectlyUpdateStopwatchState() async throws {
        let store = MutableStateStoreImpl(
            StopwatchState(
                status: .running,
                milliseconds: 1_000,
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
        let useCase = UpdateStopwatchTimeUseCaseImpl(store)
        
        await useCase.execute(
            TimerState(
                isRunning: true,
                milliseconds: 1_700
            )
        )
        
        let expected = StopwatchState(
            status: .running,
            milliseconds: 1_700,
            completedLaps: [
                Lap(
                    index: 1,
                    milliseconds: 1_000,
                    status: .done
                )
            ],
            completedLapsMilliseconds: 1_000
        )
        
        #expect(store.state == expected)
    }
}
