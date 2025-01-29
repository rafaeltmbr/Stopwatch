class ApplicationContainerFactoryImpl: ApplicationContainerFactory {
    typealias Data = ApplicationData<DataStopwatchStore>
    typealias DataStopwatchStore = MutableStateStoreImpl<StopwatchState, EventEmitterImpl<StopwatchState>>
    typealias Repositories = ApplicationData
    typealias Services = ApplicationServices<ServicesTimer>
    typealias ServicesTimer = TimerServiceImpl<EventEmitterImpl<TimerState>>
    typealias UseCases = ApplicationUseCases
    typealias Presentation = ApplicationPresentation<PresentationHVMF, PresentationLVMF, PresentationSN>
    typealias PresentationHVMF = HomeViewModelFactoryImpl<DataStopwatchStore, PresentationSN>
    typealias PresentationLVMF = LapsViewModelFactoryImpl<DataStopwatchStore, PresentationSN>
    typealias PresentationSN = StackNavigatorImpl<EventEmitterImpl<[StackNavigatorScreen]>>
    typealias Container = ApplicationContainer<
        Data,
        DataStopwatchStore,
        Services,
        ServicesTimer,
        Presentation,
        PresentationHVMF,
        PresentationLVMF,
        PresentationSN
    >
    
    func make() -> Container {
        let services = ApplicationServices(
            TimerServiceImpl(EventEmitterImpl()),
            LoggingServiceImpl()
        )
        let data = ApplicationData(
            MutableStateStoreImpl(StopwatchState(), EventEmitterImpl<StopwatchState>()),
            StopwatchRepositoryImpl(FileManagerStopwatchDataSourceAdapter(services.logging))
        )
        let calculateLapsStatuses = CalculateLapsStatusesImpl()
        let useCases = ApplicationUseCases(
            LoggingUseCaseImpl(services.logging),
            StartStopwatchUseCaseImpl(data.stopwatchStore, services.timer),
            PauseStopwatchUseCaseImpl(data.stopwatchStore, services.timer),
            ResetStopwatchUseCaseImpl(data.stopwatchStore, services.timer),
            NewLapUseCaseImpl(data.stopwatchStore, calculateLapsStatuses),
            UpdateStopwatchTimeUseCaseImpl(data.stopwatchStore),
            SaveStopwatchStateUseCaseImpl(data.stopwatchStore, data.stopwatchRepository),
            RestoreStopwatchStateUseCaseImpl(data.stopwatchStore, data.stopwatchRepository, services.timer, services.logging)
        )
        
        let viewTimeMapper = ViewTimeMapperImpl()
        let stringTimeMapper = StringTimeMapperImpl(viewTimeMapper)
        let stackNavigator = StackNavigatorImpl(stack: [.home], eventEmitter: EventEmitterImpl())
        let homeViewModelFactory = HomeViewModelFactoryImpl(
            data.stopwatchStore,
            useCases.logging,
            useCases.startStopwatch,
            useCases.pauseStopwatch,
            useCases.resetStopwatch,
            useCases.newLap,
            viewTimeMapper,
            stringTimeMapper,
            stackNavigator
        )
        let lapsViewModelFactory = LapsViewModelFactoryImpl(
            data.stopwatchStore,
            useCases.logging,
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
            data,
            services,
            useCases,
            presentation
        )
        
        return container
    }
}
