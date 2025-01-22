class ApplicationContainerFactoryImpl: ApplicationContainerFactory {
    typealias Stores = ApplicationStores<StoresStopwatch>
    typealias StoresStopwatch = MutableStateStoreImpl<StopwatchState, EventEmitterImpl<StopwatchState>>
    typealias Repositories = ApplicationRepositories
    typealias Services = ApplicationServices<ServicesTimer>
    typealias ServicesTimer = TimerServiceImpl<EventEmitterImpl<TimerState>>
    typealias UseCases = ApplicationUseCases
    typealias Presentation = ApplicationPresentation<PresentationHVMF, PresentationLVMF, PresentationSN>
    typealias PresentationHVMF = HomeViewModelFactoryImpl<StoresStopwatch, PresentationSN>
    typealias PresentationLVMF = LapsViewModelFactoryImpl<StoresStopwatch, PresentationSN>
    typealias PresentationSN = StackNavigatorImpl<EventEmitterImpl<[StackNavigatorPath]>>
    typealias Container = ApplicationContainer<
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
    
    func make() -> Container {
        let stores = ApplicationStores(MutableStateStoreImpl(StopwatchState(), EventEmitterImpl<StopwatchState>()))
        let repositories = ApplicationRepositories(StopwatchRepositoryImpl(StopwatchDataSourceFileManager()))
        let services = ApplicationServices(TimerServiceImpl(EventEmitterImpl()))
        let useCases = ApplicationUseCases(
            StartStopwatchUseCaseImpl(stores.stopwatch, services.timer),
            PauseStopwatchUseCaseImpl(stores.stopwatch, services.timer),
            ResetStopwatchUseCaseImpl(stores.stopwatch, services.timer),
            NewLapUseCaseImpl(stores.stopwatch),
            UpdateStopwatchTimeAndLapsUseCaseImpl(stores.stopwatch),
            SaveStopwatchStateUseCaseImpl(stores.stopwatch, repositories.stopwatch),
            RestoreStopwatchStateUseCaseImpl(stores.stopwatch, repositories.stopwatch, services.timer)
        )
        
        let viewTimeMapper = ViewTimeMapperImpl()
        let stringTimeMapper = StringTimeMapperImpl(viewTimeMapper)
        let stackNavigator = StackNavigatorImpl(stack: [.home], eventEmitter: EventEmitterImpl())
        let homeViewModelFactory = HomeViewModelFactoryImpl(
            stores.stopwatch,
            useCases.startStopwatch,
            useCases.pauseStopwatch,
            useCases.resetStopwatch,
            useCases.newLap,
            viewTimeMapper,
            stringTimeMapper,
            stackNavigator
        )
        let lapsViewModelFactory = LapsViewModelFactoryImpl(
            stores.stopwatch,
            useCases.startStopwatch,
            useCases.newLap,
            stringTimeMapper,
            stackNavigator
        )

        let presentation = ApplicationPresentation(
            homeViewModelFactory,
            lapsViewModelFactory,
            stackNavigator
        )

        let container = ApplicationContainer(
            stores,
            repositories,
            services,
            useCases,
            presentation
        )
        
        return container
    }
}
