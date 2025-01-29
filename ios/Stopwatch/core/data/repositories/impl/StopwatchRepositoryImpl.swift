struct StopwatchRepositoryImpl: StopwatchRepository {
    private let stopwatchDataSource: StopwatchDataSourcePort
    
    init(_ stopwatchDataSource: StopwatchDataSourcePort) {
        self.stopwatchDataSource = stopwatchDataSource
    }
    
    func load() async -> StopwatchState? {
        return await stopwatchDataSource.load()
    }
    
    func save(_ state: StopwatchState) async {
        await stopwatchDataSource.save(state)
    }
}
