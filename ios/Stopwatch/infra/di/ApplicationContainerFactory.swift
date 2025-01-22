class ApplicationRepositories {
    let stopwatch: StopwatchRepository
    
    init(_ stopwatchRepository: StopwatchRepository) {
        self.stopwatch = stopwatchRepository
    }
}

class ApplicationStores<MSS>
where MSS: MutableStateStore, MSS.State == StopwatchState {
    let stopwatch: MSS
    
    init(_ stopwatch: MSS) {
        self.stopwatch = stopwatch
    }
}

class ApplicationServices<TS>
where TS: TimerService {
    let timer: TS
    
    init(_ timer: TS) {
        self.timer = timer
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
    Stores,
    StoresStopwatch,
    Repositories,
    Services, ServicesTimer,
    Presentation,
    PresentationHVMF,
    PresentationLVMF,
    PresentationSN
>
where
    Stores: ApplicationStores<StoresStopwatch>,
    StoresStopwatch: MutableStateStore,
    Repositories: ApplicationRepositories,
    Services: ApplicationServices<ServicesTimer>,
    ServicesTimer: TimerService,
    Presentation: ApplicationPresentation<PresentationHVMF, PresentationLVMF, PresentationSN>,
    PresentationHVMF: HomeViewModelFactory,
    PresentationLVMF: LapsViewModelFactory,
    PresentationSN: StackNavigator
{
    let stores: Stores
    let repositories: Repositories
    let services: Services
    let useCases: ApplicationUseCases
    let presentation: Presentation
    
    init(_ stores: Stores, _ repositories: Repositories, _ services: Services, _ useCases: ApplicationUseCases, _ presentation: Presentation) {
        self.stores = stores
        self.repositories = repositories
        self.services = services
        self.useCases = useCases
        self.presentation = presentation
    }
}

protocol ApplicationContainerFactory {
    associatedtype Stores: ApplicationStores<StoresStopwatch>
    associatedtype StoresStopwatch: MutableStateStore where StoresStopwatch.State == StopwatchState
    
    associatedtype Repositories: ApplicationRepositories
    
    associatedtype Services: ApplicationServices<ServicesTimer>
    associatedtype ServicesTimer: TimerService
    
    associatedtype UseCases: ApplicationUseCases
    
    associatedtype Presentation: ApplicationPresentation<PresentationHVMF, PresentationLVMF, PresentationSN>
    associatedtype PresentationHVMF: HomeViewModelFactory
    associatedtype PresentationLVMF: LapsViewModelFactory
    associatedtype PresentationSN: StackNavigator

    associatedtype Container: ApplicationContainer<
        Stores,
        StoresStopwatch,
        Repositories,
        Services,
        ServicesTimer,
        Presentation,
        PresentationHVMF,
        PresentationLVMF,
        PresentationSN
    >
    
    func make() -> Container
}
