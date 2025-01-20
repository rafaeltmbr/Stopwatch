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
    
    init(
        _ startStopwatch: StartStopwatchUseCase,
        _ pauseStopwatch: PauseStopwatchUseCase,
        _ resetStopwatch: ResetStopwatchUseCase
    ) {
        self.startStopwatch = startStopwatch
        self.pauseStopwatch = pauseStopwatch
        self.resetStopwatch = resetStopwatch
    }
}

class ApplicationPresentation<HVMF> where HVMF: HomeViewModelFactory {
    let homeViewModelFactory: HVMF
    
    init(_ homeViewModelFactory: HVMF) {
        self.homeViewModelFactory = homeViewModelFactory
    }
}

class ApplicationContainer<Stores, StoresStopwatch, Services, ServicesTimer, Presentation, PresentationHomeVMF>
where
    Stores: ApplicationStores<StoresStopwatch>,
    StoresStopwatch: MutableStateStore,
    Services: ApplicationServices<ServicesTimer>,
    ServicesTimer: TimerService,
    Presentation: ApplicationPresentation<PresentationHomeVMF>,
    PresentationHomeVMF: HomeViewModelFactory
{
    let stores: Stores
    let services: Services
    let useCases: ApplicationUseCases
    let presentation: Presentation
    
    init(_ stores: Stores, _ services: Services, _ useCases: ApplicationUseCases, _ presentation: Presentation) {
        self.stores = stores
        self.services = services
        self.useCases = useCases
        self.presentation = presentation
    }
}

protocol ApplicationContainerFactory {
    associatedtype Stores: ApplicationStores<StoresStopwatch>
    associatedtype StoresStopwatch: MutableStateStore where StoresStopwatch.State == StopwatchState
    
    associatedtype Services: ApplicationServices<ServicesTimer>
    associatedtype ServicesTimer: TimerService
    
    associatedtype UseCases: ApplicationUseCases
    
    associatedtype Presentation: ApplicationPresentation<PresentationHVMF>
    associatedtype PresentationHVMF: HomeViewModelFactory
    
    associatedtype Container: ApplicationContainer<
        Stores,
        StoresStopwatch,
        Services,
        ServicesTimer,
        Presentation,
        PresentationHVMF
    >
    
    func make() -> Container
}
