protocol LoggingService {
    func debug(tag: String, message: String)
    func info(tag: String, message: String)
    func warn(tag: String, message: String)
    func error(tag: String, message: String, error: Error?)
}
