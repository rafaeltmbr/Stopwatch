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
    
    init(
        _ startStopwatch: StartStopwatchUseCase,
        _ pauseStopwatch: PauseStopwatchUseCase,
        _ resetStopwatch: ResetStopwatchUseCase,
        _ newLap: NewLapUseCase,
        _ updateStopwatchTimeAndLaps: UpdateStopwatchTimeAndLapsUseCase
    ) {
        self.startStopwatch = startStopwatch
        self.pauseStopwatch = pauseStopwatch
        self.resetStopwatch = resetStopwatch
        self.newLap = newLap
        self.updateStopwatchTimeAndLaps = updateStopwatchTimeAndLaps
    }
}

class ApplicationPresentation<HVMF, LVMF>
where HVMF: HomeViewModelFactory, LVMF: LapsViewModelFactory {
    let homeViewModelFactory: HVMF
    let lapsViewModelFactory: LVMF
    
    init(_ homeViewModelFactory: HVMF, _ lapsViewModelFactory: LVMF) {
        self.homeViewModelFactory = homeViewModelFactory
        self.lapsViewModelFactory = lapsViewModelFactory
    }
}

class ApplicationContainer<
    Stores,
    StoresStopwatch,
    Services, ServicesTimer,
    Presentation,
    PresentationHVMF,
    PresentationLVMF
>
where
    Stores: ApplicationStores<StoresStopwatch>,
    StoresStopwatch: MutableStateStore,
    Services: ApplicationServices<ServicesTimer>,
    ServicesTimer: TimerService,
    Presentation: ApplicationPresentation<PresentationHVMF, PresentationLVMF>,
    PresentationHVMF: HomeViewModelFactory,
    PresentationLVMF: LapsViewModelFactory
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
    
    associatedtype Presentation: ApplicationPresentation<PresentationHVMF, PresentationLVMF>
    associatedtype PresentationHVMF: HomeViewModelFactory
    associatedtype PresentationLVMF: LapsViewModelFactory

    associatedtype Container: ApplicationContainer<
        Stores,
        StoresStopwatch,
        Services,
        ServicesTimer,
        Presentation,
        PresentationHVMF,
        PresentationLVMF
    >
    
    func make() -> Container
}
