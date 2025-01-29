struct ViewTime {
    var minutes: [String]
    var seconds: [String]
    var fraction: [String]
    
    init(
        minutes: [String] = ["0", "0"],
        seconds: [String] = ["0", "0"],
        fraction: [String] = ["0", "0"]
    ) {
        self.minutes = minutes
        self.seconds = seconds
        self.fraction = fraction
    }
}
