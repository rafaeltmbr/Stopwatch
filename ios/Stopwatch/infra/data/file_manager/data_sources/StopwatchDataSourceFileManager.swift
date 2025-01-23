import SwiftUI

class StopwatchDataSourceFileManager: StopwatchDataSource {
    private let loggingService: LoggingService
    
    init(_ loggingService: LoggingService) {
        self.loggingService = loggingService
    }
    
    func load() async -> StopwatchState? {
        let task = Task {
            let url = try Self.getFileUrl()
            let data = try Data(contentsOf: url)
            let stopwatchState = try JSONDecoder().decode(StopwatchStateJson.self, from: data)
            return try stopwatchState.toDomainEntity()
        }
        
        do {
            return try await task.value
        } catch {
            loggingService.error(
                tag: "StopwatchDataSourceFileManager",
                message: "Failed to load state",
                error: error
            )
            return nil
        }
    }
    
    func save(_ state: StopwatchState) async {
        let task = Task {
            let url = try Self.getFileUrl()
            let data = try JSONEncoder().encode(state.toJson())
            try data.write(to: url)
        }
        
        do {
            try await task.value
        } catch {
            loggingService.error(
                tag: "StopwatchDataSourceFileManager",
                message: "Failed to save state",
                error: error
            )
        }
    }
    
    private static func getFileUrl() throws -> URL {
        try FileManager.default.url(
            for: .documentDirectory,
            in: .userDomainMask,
            appropriateFor: nil,
            create: false
        ).appendingPathComponent("stopwatch_state.json")
    }
}

private extension StopwatchState {
    func toJson() -> StopwatchStateJson {
        let status = switch status {
        case .initial: 1
        case .running: 2
        case .paused: 3
        }
        
        return StopwatchStateJson(
            status: status,
            milliseconds: milliseconds,
            laps: completedLaps.map { $0.toJson() }
        )
    }
}

private enum DataSourceError: Error {
    case invalidStopwatchStatus, invalidLapStatus
}

private extension StopwatchStateJson {
    func toDomainEntity() throws -> StopwatchState {
        let status: Status = switch status {
        case 1: .initial
        case 2: .running
        case 3: .paused
        default: throw DataSourceError.invalidStopwatchStatus
        }
        
        return StopwatchState(
            status: status,
            milliseconds: milliseconds,
            completedLaps: try laps.map { try $0.toDomainEntity() },
            completedLapsMilliseconds: laps.reduce(0) {acc, lap in acc + lap.milliseconds }
        )
    }
}

private extension Lap {
    func toJson() -> LapJson {
        let statusIndex = switch status {
        case .current: 1
        case .best: 2
        case .worst: 3
        case .done: 4
        }
        
        return LapJson(
            index: index,
            milliseconds: milliseconds,
            status: statusIndex
        )
    }
}

private extension LapJson {
    func toDomainEntity() throws -> Lap {
        let lapStatus: LapStatus = switch status {
        case 1: .current
        case 2: .best
        case 3: .worst
        case 4: .done
        default: throw DataSourceError.invalidLapStatus
        }
        
        return Lap(
            index: index,
            milliseconds: milliseconds,
            status: lapStatus
        )
    }
}
