struct LapJson: Codable {
    let index: Int
    let milliseconds: Int
    let status: Int
    
    init(index: Int, milliseconds: Int, status: Int) {
        self.index = index
        self.milliseconds = milliseconds
        self.status = status
    }
}
