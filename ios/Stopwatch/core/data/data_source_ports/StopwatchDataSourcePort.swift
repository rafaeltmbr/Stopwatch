protocol StopwatchDataSourcePort {
    func load() async -> StopwatchState?
    func save(_ state: StopwatchState) async
}
