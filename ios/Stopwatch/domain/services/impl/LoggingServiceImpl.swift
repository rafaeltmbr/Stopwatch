import Foundation

class LoggingServiceImpl: LoggingService {
    func debug(tag: String, message: String) {
        print(formatMessage(severity: "debug", tag: tag, message: message))
    }
    
    func info(tag: String, message: String) {
        print(formatMessage(severity: "info", tag: tag, message: message))
    }
    
    func warn(tag: String, message: String) {
        print(formatMessage(severity: "warn", tag: tag, message: message))
    }
    
    func error(tag: String, message: String, error: (any Error)? = nil) {
        let formatted = formatMessage(severity: "error", tag: tag, message: message)
        if error == nil {
            print(formatted)
            return
        }
        
        print("\(formatted) \(error.debugDescription)")
    }
    
    private func formatMessage(severity: String, tag: String, message: String) -> String {
        let time = ISO8601DateFormatter().string(from: Date())
        return "\(time) \(severity.uppercased()) [\(tag)] \(message)"
    }
}
