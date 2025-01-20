protocol UpdateStopwatchTimeAndLapsUseCase {
    func execute(_ timerState: TimerState) async
}
