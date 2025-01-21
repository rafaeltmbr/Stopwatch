struct StopwatchRepositoryImpl: StopwatchRepository {
    private let stopwatchDataSource: StopwatchDataSource
    
    init(_ stopwatchDataSource: StopwatchDataSource) {
        self.stopwatchDataSource = stopwatchDataSource
    }
    
    func load() async -> StopwatchState? {
        return await stopwatchDataSource.load()
    }
    
    func save(_ state: StopwatchState) async {
        await stopwatchDataSource.save(state)
    }
}
