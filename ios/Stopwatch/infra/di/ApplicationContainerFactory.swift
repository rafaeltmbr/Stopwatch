class ApplicationData<MSS>
where MSS: MutableStateStore, MSS.State == StopwatchState {
    let stopwatchRepository: StopwatchRepository
    let stopwatchStore: MSS

    init(_ stopwatch: MSS, _ stopwatchRepository: StopwatchRepository) {
        self.stopwatchRepository = stopwatchRepository
        self.stopwatchStore = stopwatch
    }
}

class ApplicationServices<TS>
where TS: TimerService {
    let timer: TS
    let logging: LoggingService
    
    init(_ timer: TS, _ logging: LoggingService) {
        self.timer = timer
        self.logging = logging
    }
}

class ApplicationUseCases {
    let startStopwatch: StartStopwatchUseCase
    let pauseStopwatch: PauseStopwatchUseCase
    let resetStopwatch: ResetStopwatchUseCase
    let newLap: NewLapUseCase
    let updateStopwatchTimeAndLaps: UpdateStopwatchTimeAndLapsUseCase
    let saveStopwatchState: SaveStopwatchStateUseCase
    let restoreStopwatchState: RestoreStopwatchStateUseCase
    
    init(
        _ startStopwatch: StartStopwatchUseCase,
        _ pauseStopwatch: PauseStopwatchUseCase,
        _ resetStopwatch: ResetStopwatchUseCase,
        _ newLap: NewLapUseCase,
        _ updateStopwatchTimeAndLaps: UpdateStopwatchTimeAndLapsUseCase,
        _ saveStopwatchState: SaveStopwatchStateUseCase,
        _ restoreStopwatchState: RestoreStopwatchStateUseCase
    ) {
        self.startStopwatch = startStopwatch
        self.pauseStopwatch = pauseStopwatch
        self.resetStopwatch = resetStopwatch
        self.newLap = newLap
        self.updateStopwatchTimeAndLaps = updateStopwatchTimeAndLaps
        self.saveStopwatchState = saveStopwatchState
        self.restoreStopwatchState = restoreStopwatchState
    }
}

class ApplicationPresentation<HVMF, LVMF, SN>
where
    HVMF: HomeViewModelFactory,
    LVMF: LapsViewModelFactory,
    SN: StackNavigator
{
    let homeViewModelFactory: HVMF
    let lapsViewModelFactory: LVMF
    let stackNavigator: SN
    
    init(_ homeViewModelFactory: HVMF, _ lapsViewModelFactory: LVMF, _ stackNavigator: SN) {
        self.homeViewModelFactory = homeViewModelFactory
        self.lapsViewModelFactory = lapsViewModelFactory
        self.stackNavigator = stackNavigator
    }
}

class ApplicationContainer<
    Data,
    DataStopwatchStore,
    Services, ServicesTimer,
    Presentation,
    PresentationHVMF,
    PresentationLVMF,
    PresentationSN
>
where
    Data: ApplicationData<DataStopwatchStore>,
    DataStopwatchStore: MutableStateStore,
    Services: ApplicationServices<ServicesTimer>,
    ServicesTimer: TimerService,
    Presentation: ApplicationPresentation<PresentationHVMF, PresentationLVMF, PresentationSN>,
    PresentationHVMF: HomeViewModelFactory,
    PresentationLVMF: LapsViewModelFactory,
    PresentationSN: StackNavigator
{
    let data: Data
    let services: Services
    let useCases: ApplicationUseCases
    let presentation: Presentation
    
    init(_ data: Data, _ services: Services, _ useCases: ApplicationUseCases, _ presentation: Presentation) {
        self.data = data
        self.services = services
        self.useCases = useCases
        self.presentation = presentation
    }
}

protocol ApplicationContainerFactory {
    associatedtype Data: ApplicationData<DataStopwatchStore>
    associatedtype DataStopwatchStore: MutableStateStore where DataStopwatchStore.State == StopwatchState
    
    associatedtype Services: ApplicationServices<ServicesTimer>
    associatedtype ServicesTimer: TimerService
    
    associatedtype UseCases: ApplicationUseCases
    
    associatedtype Presentation: ApplicationPresentation<PresentationHVMF, PresentationLVMF, PresentationSN>
    associatedtype PresentationHVMF: HomeViewModelFactory
    associatedtype PresentationLVMF: LapsViewModelFactory
    associatedtype PresentationSN: StackNavigator

    associatedtype Container: ApplicationContainer<
        Data,
        DataStopwatchStore,
        Services,
        ServicesTimer,
        Presentation,
        PresentationHVMF,
        PresentationLVMF,
        PresentationSN
    >
    
    func make() -> Container
}
