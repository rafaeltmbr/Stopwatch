class LoggingUseCaseImpl: LoggingUseCase {
    private let loggingService: LoggingService
    
    init(_ loggingService: LoggingService) {
        self.loggingService = loggingService
    }
    
    func debug(tag: String, message: String) {
        loggingService.debug(tag: tag, message: message)
    }
    
    func info(tag: String, message: String) {
        loggingService.info(tag: tag, message: message)
    }
    
    func warn(tag: String, message: String) {
        loggingService.warn(tag: tag, message: message)
    }
    
    func error(tag: String, message: String, error: (any Error)? = nil) {
        loggingService.error(tag: tag, message: message, error: error)
    }
}
