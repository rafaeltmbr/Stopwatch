class ApplicationContainerFactoryImpl: ApplicationContainerFactory {
    typealias Stores = ApplicationStores<StoresStopwatch>
    typealias StoresStopwatch = MutableStateStoreImpl<StopwatchState, EventEmitterImpl<StopwatchState>>
    typealias Services = ApplicationServices<ServicesTimer>
    typealias ServicesTimer = TimerServiceImpl<EventEmitterImpl<TimerState>>
    typealias UseCases = ApplicationUseCases
    typealias Presentation = ApplicationPresentation<PresentationHVMF>
    typealias PresentationHVMF = HomeViewModelFactoryImpl<MutableStateStoreImpl<StopwatchState, EventEmitterImpl<StopwatchState>>>
    typealias Container = ApplicationContainer<
        Stores,
        StoresStopwatch,
        Services,
        ServicesTimer,
        Presentation,
        PresentationHVMF
    >
    
    func make() -> Container {
        let stores = ApplicationStores(MutableStateStoreImpl(StopwatchState(), EventEmitterImpl<StopwatchState>()))
        let services = ApplicationServices(TimerServiceImpl(EventEmitterImpl()))
        let useCases = ApplicationUseCases(
            StartStopwatchUseCaseImpl(stores.stopwatch, services.timer),
            PauseStopwatchUseCaseImpl(stores.stopwatch, services.timer),
            ResetStopwatchUseCaseImpl(stores.stopwatch, services.timer)
        )
        let presentation = ApplicationPresentation(
            HomeViewModelFactoryImpl(
                stores.stopwatch,
                useCases.startStopwatch,
                useCases.pauseStopwatch,
                useCases.resetStopwatch,
                ViewTimerMapperImpl()
            )
        )

        let container = ApplicationContainer(
            stores,
            services,
            useCases,
            presentation
        )
        
        return container
    }
}
