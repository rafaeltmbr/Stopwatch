# Architecture

Table of Contents

1.  [Introduction](#1-introduction)
    *   [1.1. Overview](#11-overview)
    *   [1.2. Architectural Goals](#12-architectural-goals)
    *   [1.3. Architectural Approach](#13-architectural-approach)
        *   1.3.1. Ports and Adapters
        *   1.3.2. Unidirectional Data Flow (UDF)
2.  [Essencial Concepts](#2-essential-concepts)
    *   2.1. Ports and Adapters
    *   2.2. Unidirectional Data Flow (UDF)
    *   2.3. Dependency Injection (DI)
    *   2.4. Dependency Inversion Principle (DIP)
    *   2.5. Design Patterns
        *   2.5.1. Abstract Factory
        *   2.5.2. Observer
        *   2.5.3. Command
        *   2.5.4. Adapter
3.  [Layers](#3-layers)
    *   [3.1. Core](#31-core)
        *   [3.1.1. Core Entities](#311-core-entities)
        *   [3.1.2. Use Cases](#312-use-cases)
        *   [3.1.3. Services](#313-services)
        *   [3.1.4. Data](#314-data)
            *   [3.1.4.1. Repositories](#3141-repositories)
            *   [3.1.4.2. State Stores](#3142-state-stores)
        *   [3.1.5. Utilities](#315-utilities)
    *   [3.2. Platform](#32-platform)
        *   [3.2.1. Presentation](#321-presentation)
            *   [3.2.1.1. Presentation Entities](#3211-presentation-entities)
            *   [3.2.1.2. Views and View Components](#3212-views-and-view-components)
            *   [3.2.1.3. ViewModels](#3213-viewmodels)
            *   [3.2.1.4. UI Mappers](#2214-ui-mappers)
            *   [3.2.1.5. Navigators](#3215-navigators)
        *   [3.2.2. Data](#322-data)
            *   [3.2.2.1. Data Entities](#3221-data-entities)
            *   [3.2.2.2. Data Sources](#3222-data-sources)
        *   [3.2.3. Services](#323-services)
            *   [3.2.3.1. External Resource Adapters](#3231-external-resource-adapters)
        *   [3.2.4. Dependency Injection (DI)](#324-dependency-injection-di)
            *   [3.2.4.1. Dependency Container](#3241-dependency-container)
            *   [3.2.4.2. Factories](#3242-factories)
        *   [3.2.5. Application Entry Point](#325-application-entry-point)
    *   [3.3. External](#33-external)
4.  Stopwatch Specifics
    *   4.1. Time Tracking
    *   4.2. Lap Management
    *   4.3. Stopwatch States
    *   4.4. UI/UX Considerations
5.  [Data Flow](#5-data-flow)
6.  State Management
7.  [Dependency Diagram](#7-dependency-diagram)
8.  [Folder Structure](#8-folder-structure)
9.  [Implementation](#9-implementation)
    *   9.1. Android Implementation
    *   9.2. iOS Implementation
    *   9.3. Platform-Specific Considerations
10. Error Handling Strategy
11. [Testing Strategy](#11-testing-strategy)
    *   11.1. Android Tests
    *   11.2. iOS Tests
12. [Appendix](#12-appendix)
    *   12.1. Unified Modeling Language (UML)

## 1. Introduction
### 1.1 Overview
This document presents the architectural design of the Stopwatch, a mobile application for precisely tracking elapsed time and lap intervals. The architecture is based on the Ports and Adapters architectural pattern and is designed to support the app's core functionality: starting, pausing, resuming, resetting, and lap management, while ensuring long-term maintainability and the ability to adapt to evolving user needs.

### 1.2. Architectural Goals 
The primary architectural goals are:

*   **Efficient Data Processing:** Enable the application to efficiently process high-volume data streams.
*   **Separation of Concerns:** Isolate core business logic from implementation details.
*   **Maintainability:** Provide a system that is easy to maintain and modify over time.
*   **Scalability:** Provide a system that can adapt to evolving user needs.
*   **Testability:** Provide a system that is easy to test.

These goals are achieved through a Unidirectional Data Flow (UDF) approach based on the Ports and Adapters pattern.

### 1.3 Architectural approach
#### 1.3.1 Ports and Adapters
To achieve a high degree of flexibility and testability, the Stopwatch application utilizes the Ports and Adapters Architecture (Ports and Adapters) pattern. This approach separates the core business logic from external concerns, such as the user interface, data storage, and external APIs.

The Core Layer defines "Ports," which are interfaces that specify how the core interacts with the outside world. The Platform Layer contains "Adapters," which implement these ports and handle the specific details of interacting with external systems. This separation allows us to change external systems (e.g., switch databases) without affecting the core business logic.

The application is designed to be testable, maintainable, and scalable. The use of Ports and Adapters Architecture allows us to test the core business logic in isolation, without needing to set up complex external dependencies. The platform-agnostic core allows us to reuse the same logic across all supported platforms, reducing development time and ensuring consistency.

#### 1.3.2 Unidirectional Data Flow (UDF)

## 2. Essential Concepts

### 2.1. Ports and Adapters

### 2.2. Unidirectional Data Flow (UDF)
Unidirectional Data Flow (UDF) is an architectural pattern where data flows in a single direction, creating a predictable and manageable data flow within an application. It typically involves components like views, view models, and data stores, with data flowing from the View to the ViewModel and updates propagating back through defined channels. UDF simplifies state management, reduces complexity, and enhances the testability of applications. [Read more](https://developer.android.com/develop/ui/compose/architecture#udf).

### 2.3. Dependency Injection (DI)

### 2.4. Dependency Inversion Principle (DIP)
Dependency Inversion Principle (DIP) is a software design principle that promotes loose coupling between components by inverting the direction of dependencies. Instead of high-level modules depending on low-level modules, both depend on abstractions (interfaces). This allows for greater flexibility, maintainability, and testability by enabling components to be easily swapped or modified without affecting other parts of the system. [Read more](https://en.wikipedia.org/wiki/Dependency_inversion_principle).

### 2.5. Design Patterns 
Design Patterns Design patterns are reusable solutions to recurring design problems in software development. They provide proven approaches for structuring code, managing dependencies, and implementing common functionalities. Design patterns are categorized into creational, structural, and behavioral patterns, each addressing a specific type of design challenge. [Read more](https://refactoring.guru/design-patterns/what-is-pattern).

#### 2.5.1. Abstract Factory
The Abstract Factory pattern provides an interface for creating families of related or dependent objects without specifying their concrete classes. It allows for the creation of objects without knowing their specific types, promoting flexibility and loose coupling. This pattern is useful when a system needs to support multiple variations of objects or when the concrete types of objects should be determined at runtime. [Read more](https://refactoring.guru/design-patterns/abstract-factory).

#### 2.5.2. Observer
The Observer pattern defines a one-to-many dependency between objects, where one object (the subject) notifies all its dependents (observers) of any state changes. This allows for loose coupling between objects, as the subject doesn't need to know the specific types of its observers. This pattern is commonly used for event handling, notifications, and data synchronization.[Read more](https://refactoring.guru/design-patterns/observer).

#### 2.5.3. Command
The Command pattern encapsulates a request as an object, thereby letting you parameterize clients with different requests, queue or log requests, and support undoable operations. It decouples the object that invokes the operation from the one that knows how to perform it. This pattern is useful for implementing undo/redo functionality, transaction management, and macro recording. [Read more](https://refactoring.guru/design-patterns/command).

#### 2.5.3. Adapter

## 3. Layers
The architecture divides the application into three distinct layers: Core, Platform and External.

![Architecture Layers](../assets/images/architecture-layers.png)

### 3.1. Core
The Core layer contains the core business logic and entities of the application, independent of any specific platform or framework. It represents the pure, platform-agnostic essence of the stopwatch functionality. The Core layer contains elements like Core Entities, Use Cases, Services, State Stores, Repositories and Utils.

#### 3.1.1. Core Entities
Core Entities represent the core data structures and types within the application. These data structures are primarily data containers, encapsulating the essential information without inherent behavior. They are utilized throughout the Core layer and may also be accessed by the Platform layer for platform-specific adaptations.

In the context of the stopwatch application, a Core Entity representing the application state could be defined as follows:

```
enum StopwatchStatus:
  initial
  running
  paused

class StopwatchState:
  status: StopwatchStatus
  timeMilliseconds: Integer
```

#### 3.1.2. Use Cases
Use Cases encapsulate the core business logic of the application. They orchestrate operations by delegating tasks to Services and accessing data through State Stores and Repositories. Each Use Case focuses on a specific business operation, promoting modularity and maintainability. Even a simple application may comprise numerous, specialized Use Cases to address various functionalities.

The following code snippet demonstrates a potential implementation of a Use Case for starting a stopwatch:

```
class StartStopwatchUseCase:
  stateStore: StateStore<StopwatchState>
  timerService: TimerService

  execute():
    if stateStore.state.status == StopwatchStatus.initial:
      timerService.start() 
      newState = StopwatchState(status = running, timeMilliseconds = 0)
      stateStore.update(newState)
```

#### 3.1.3. Services
Services encapsulate specialized business operations, often involving complex algorithms or interactions with external resources. Use Cases leverage Services to delegate specific tasks or access specialized functionalities.

The following code snippet illustrates a potential implementation of a timer Core Service:

```
class TimeState:
  isRunning: Boolean
  timeMilliseconds: Integer

class TimerService:
  state: TimerState

  start():
    if !state.isRunning:
      state = TimerState(isRunning = true, timeMilliseconds = 0)
      loop()

  loop():
    while state.isRunning:
      delay(milliseconds = 10)
      newTime = state.timeMilliseconds + 10
      state = TimerState(isRunning = state.isRunning, timeMilliseconds = newTime)

  pause():
  ...

  resume():
  ...

  reset():
  ...
```

#### 3.1.4. Data
The Core Data layer comprises components that Use Cases utilize for updating application state or accessing external data sources. These components provide an abstraction layer, allowing Use Cases to interact with data without being tightly coupled to specific implementation details.

#### 3.1.4.1. Repositories
Repositories abstract data access for Use Cases, enabling local data persistence and potential integration with external Data Sources. They provide a well-defined interface, allowing Use Cases to interact with data without depending on specific implementation details. This abstraction promotes flexibility and maintainability by decoupling the business logic from the underlying data storage mechanisms.

The following code snippet demonstrates a potential implementation of a Repository for a weather application. This implementation retrieves weather information from two sources: a remote server and a local cache. If the remote server fails to provide predictions (e.g., due to network connectivity issues), the repository falls back to the local cache, providing the most recent predictions available for the specified date. This strategy ensures data availability even in offline scenarios, enhancing the application's resilience.

```
class WeatherRepository:
  cache: LocalWeatherDataSource
  server: RemoteWeatherDataSource

  getPredictionsForToday():
    today = Date()

    try:
      predicitons = server.getPredictions(date = today)
      cache.setPredictions(date = today, predictions = predictions)
    catch:
      // do nothing

    return cache.getPredictions(date = today)
```

#### 3.1.4.2. State Stores
State Stores serve as the single source of truth for application state, providing a centralized and consistent representation of data. Use Cases interact with State Stores to update and retrieve the application's state. To notify interested components about state changes, State Stores employ a publish-subscribe mechanism, often implemented using an Observer pattern-like approach. This allows components to react to state updates in a decoupled and efficient manner.

The following code snippet demonstrates a generic implementation of a State Store using the Observer pattern:

```
class Listener<T>:
  handleUpdate(newState: T)

class StateStore<T>:
  state: T
  listeners: Listener<T>[]

  updateState(newState: T):
    state = newState 
    for listener in listeners:
      listener.handleUpdate(state)

  addListener(listener: Listener<T>):
    if !listeners.contains(listener):
      listeners.add(listener)
  
  removeListener(listener: Listener<T>):
    if listernes.contains(listener)
      listeners.remove(listener)
```

#### 3.1.5. Utilities
Utility (Utils) classes and functions, often referred to as "Utils," provide reusable helper functionalities that can be accessed by various components across the application. These utilities typically encapsulate common operations or logic that are not specific to any particular domain or layer.

The following code snippet illustrates the types of functionalities that might be included within utility classes:

```
class DateUtilities
  static diff(date1: Date, date2: Date) -> Date:
    diff = date1.toMilliseconds() - date2.toMilliseconds()
    return Date(milliseconds = Math.abs(diff))
  
// Usage
championshipMatch = Date(...) // some arbitrary date
today = Date()
remainingDate = DateUtilities.diff(today, championshipMatch)
print("Remaining days: ", remainingDate.days)
```

### 3.2. Platform
The Platform layer houses platform-specific implementations and dependencies on frameworks and libraries. It bridges the gap between the Core layer and the external environment, providing platform-specific adaptations.  The Platform layer contains Presentation elements, Data Sources, External Resource Adapters, Dependency Injection classes and the Application's Entry Point. 

#### 3.2.1. Presentation
The Presentation layer exhibits platform-specific variations in its implementation. In some applications, it encompasses graphical user interface (GUI) elements, while in others, it may involve simple text-based input/output through a command-line interface (CLI). It could even be realized as a network protocol like HTTP. In the context of this stopwatch mobile application, the Presentation layer utilizes platform-specific GUI libraries for rendering data, handling user interactions, and managing navigation within the application. It encapsulates all the logic necessary for presentation, ensuring a clear separation of concerns from the underlying business logic and data access layers.

#### 3.2.1.1. Presentation Entities
Presentation Entities represent data structures and types that are shared and utilized by various components within the Presentation layer. These entities primarily serve as data containers, encapsulating information relevant to the presentation logic without inherent behavior. They promote consistency and maintainability by providing a unified representation of data across different parts of the user interface. Furthermore, Presentation Entities are tailored to represent data in a format that is most convenient for the View, facilitating efficient rendering and data binding.

The following code snippet illustrates an example of a Presentation Entity representing the stopwatch time:

```
class StopwatchViewTime:
  minutes: String[]
  seconds: String[]
  fraction: String[]
```

#### 3.2.1.2. Views and View Components
**Views** are responsible for rendering the graphical user interface (GUI) and handling user interactions. They act as the visual representation of the application's state. Views can be composed of smaller, reusable UI elements called **View Components**, which encapsulate specific UI functionality. Views observe changes in the **ViewModel's** state and update their display accordingly. This observation is facilitated through an observer-like pattern, ensuring that the UI remains synchronized with the underlying data.

The following code snippet demonstrates a basic counter View that uses a PrimaryButton View Component:

```
import PrimaryButton from platform/presentation/view_components/PrimaryButton

class CounterView:
  context: UiLibraryContext
  viewModel: CounterViewModel

  init():
    viewModel.addListener(this)

  render(state: CounterViewState):
    text = Text(value = state.count)
    button = PrimaryButton(text = "Increment", onClick = handleIncrement)

    container = Column(alignment = "center", spacing = "10px")
    container.append(element = text)
    container.append(element = button)

    context.updateUI(element = container)

  handleIncrement():
    viewModel.handleAction(Action.Increment)

  handleUpdate(state: CounterViewState):
    render(state)


// View Component (platform/presentation/view_components/PrimaryButton)
class PrimaryButton:
  text: String
  onClick: () -> Void

  render():
    Button(
      text = text,
      onClick = onClick,
      color = Colors.primary,
      padding = "10px",
      border = "rounded"
    )
```

#### 3.2.1.3. ViewModels
A ViewModel connects the View to the application's business logic, acting as an intermediary that receives Action events from the View and processes them accordingly. It manages the View State for display and handles View Actions by delegating to Use Cases or Navigators. The ViewModel subscribes to the Core State Store (Observer pattern) and updates the View State accordingly. It may delegate formatting to UI Mappers. Views observe ViewModel state changes.

The ViewModel is responsible for:
- Executing relevant Use Cases based on received Actions.
- Instructing the Navigator to transition between Views.
- Updating the View State to reflect data changes.

The following code snippet demonstrates a simple stopwatch ViewModel:

```
class StopwatchViewState:
  status: StopwatchState
  time: String

enum StopwatchAction:
  start
  pause
  resume
  reset
  seeDetails

class StopwatchViewModel:
  state: StopwatchViewState
  listeners: Listener[]
  stateStore: CoreStateStore

  startStopwatch: StartStopwatchUseCase
  pauseStopwatch: PauseStopwatchUseCase
  ResumeStopwatch: ResumeStopwatchUseCase
  ResetStopwatch: ResetStopwatchUseCase
  timeUiMapper: StopwatchTimeUiMapper
  navigator: Navigator

  init():
    stateStore.addListener(this)

  handleAction(action: StopwatchAction):
    switch action:
      case start: startStopwatch.execute()
      case pause: pauseStopwatch.execute()
      case resume: resumeStopwatch.execute()
      case reset: resetStopwatch.execute()
      case seetDetails: navigator.navigate(screen = "details") 

  handleUpdate(newState: CoreState):
    state = StopwatchViewState(
      status = newState.status,
      time = timeUiMapper.map(newState.timeMilliseconds)
    )

    for listener in listeners:
      listener.update(state)

  addListener(listener: Listener)
  ...

  removeListener(listener: Listener)
  ...
```

#### 2.2.1.4. UI Mappers
UI Mappers transform data into a format suitable for presentation, easing the ViewModel's formatting burden.

The following code demonstrates a stopwatch time UI Mapper:

```
class StopwatchTimeUiMapper:
  map(milliseconds: Integer) -> String:
    fraction = milliseconds / 10 % 100
    seconds = milliseconds / 1000 % 60
    minutes = milliseconds / 60000

    return format("%02d:02d.%02d", minutes, seconds, fraction)
```

#### 3.2.1.5. Navigators
Navigators handle screen transitions, relieving ViewModels of navigation logic. They typically wrap underlying UI library navigation code, adding convenience and abstracting implementation details.

The following code demonstrates a basic Navigator:

```
class Navigator:
  uiLibraryNavigator: UILibraryNavigator
  navigationStack: String[]

  navigate(screen: String):
    if screen in navigationStack:
      return

    navigationStack.append(screen)
    uiLibraryNavigator.navigate(screen)

  back():
  ...
```

#### 3.2.2. Data
The Platform layer implements Data Sources defined in the Core layer, connecting to external data resources and hiding platform-specific details. This promotes loose coupling and isolates the Core layer from data access implementation specifics.

#### 3.2.2.1. Data Entities
Platform Data Entities represent data structures used by third-party libraries for Data Source implementation, reflecting the library's data format and serialization.

The following code demonstrates an Platform Data Entity. Note the introduction of third-party library specific code:

```
import Table, Column from database_library

class StopwatchStateEntity: Table(name = "stopwatch_state"):
  id: Column(name = "id", type = "INTEGER", primary = true) 

  status: Column(name = "status", type = "INTEGER", nullable = false)

  timeMilliseconds: Column(name = "time", type = "INTEGER", nullable = false)
```

#### 3.2.2.2. Data Sources
Data Sources provide an abstraction for Repositories to access external data persistence mechanisms, such as databases, files, or network services. Different implementations can be provided for the same interface, hiding data access details from the calling code.

The following code demonstrates a Data Source implementation for saving and restoring stopwatch state: 

```
import Connection from database_library

interface StopwatchStateDataSource:
  save(state: StopwatchState)
  load() -> StopwatchState?

class StopwatchStateDatabaseDataSource implements StopwatchStateDataSource:
  connection: Connection

  save(state: StopwatchState):
    entity = StopwatchStateEntity(
      id = 1,
      status = state.status.toInt(),
      timeMilliseconds = state.timeMilliseconds
    )
    
    connection.save(entity)
    
  load() -> StopwatchState?:
    entity = connection.findById(1)
    if entity == null:
      return null 

    return StopwatchState(
      status = Status.fromInt(entity.status),
      timeMilliseconds = entity.timeMilliseconds
    )
```

#### 3.2.3. Services
The Platform Services layer provides concrete implementations for External Resource Adapters defined in the Core Services layer.

#### 3.2.3.1. External Resource Adapters
External Resource Adapters bridge the Core Services layer with external resources, such as third-party libraries or operating system functionalities. This decouples the Services from external dependencies through the Adapter pattern. 

The following code demonstrates an External Resource Adapter of a email service:

```
// core layer (core/services/external_resources/EmailAdapter)
class EmailMessage:
  from: String
  to: String
  subject: String
  body: String

interface EmailAdapter:
  send(message: EmailMessage)


// platform layer (platform/services/external_resources/EmailAdapterImpl) 
import MailService from third_party_email_library
import credentianls from platform/config/mail

class EmailAdapterImpl implements EmailAdapter:
  mail = MailService(credentials = credentials)

  send(message: EmailMessage):
    mail.send(
      from = credentations.user, 
      to = message.to,
      subject = message.subject,
      body = message.body
    )
```

#### 3.2.4. Dependency Injection (DI)
The Dependency Injection (DI) layer manages resource initialization and object creation, primarily using Abstract Factories to decouple client code from concrete implementations.

#### 3.2.4.1. Dependency Container
The Dependency Container stores and provides dependencies required for object creation.

The following code illustrates an potential Dependency Container for a stopwatch application: 

```
class DataContainer:
  stopwatchRepository: StopwatchDataRepository
  stateStore: StateStore<StopwatchState>

class ServicesContainer:
  timer: TimerService

class UseCasesContainer:
  startStopwatch: StartStopwatchUseCase
  pauseStopwatch: PauseStopwatchUseCase
  resumeStopwatch: ResumeStopwatchUseCase
  resetStopwatch: ResetStopwatchUseCase

class PresentationContainer:
  navigator: Navigator
  timeUiMapper: TimeUiMapper

class StopwatchDependencyContainer:
  data: DataContainer
  services: ServicesContainer
  useCases: UseCasesContainer
  presentation: PresentationContainer

  init():
    data = DataContainer(
      stopwatchRepository = StopwatchDataRepositoryImpl(),
      stateStore = StateStoreImpl(StopwatchState())
    )

    services = ServicesContainer(
      timer = TimerServiceImpl()
    )

    useCases = UseCasesContainer(
      startStopwatch = StartStopwatchUseCaseImpl(data.stateStore, services.timer),
      pauseStopwatch = PauseStopwatchUseCaseImpl(data.stateStore, services.timer),
      resumeStopwatch = ResumeStopwatchUseCaseImpl(data.stateStore, services.timer),
      resetStopwatch = ResetStopwatchUseCaseImpl(data.stateStore, services.timer),
    )

    presentation = PresentationContainer(
      navigator = Navigator(),
      timeUiMapper = TimeUiMapper()
    )
```

#### 3.2.4.2. Factories
Factories decouple object creation from usage, typically implementing the Abstract Factory pattern and leveraging dependency injection. They are passed as arguments to consuming classes and functions.

The following code demonstrates a ViewModel Factory implementation:

```
class ViewModelFactory:
  container: StopwatchDependencyContainer

  make():
    return ViewModel(
      stateStore = container.data.stateStore,
      startStopwatch = container.useCases.startStopwatch,
      pauseStopwatch = container.useCases.pauseStopwatch,
      ResumeStopwatch = container.useCases.resumeStopwatch,
      ResetStopwatch = container.useCases.resetStopwatch,
      timeUiMapper = container.presentation.timeUiMapper,
      navigator = container.presentation.navigator
    )
```

#### 3.2.5. Application Entry Point
Application Entry Points differ significantly across platforms, ranging from a simple `main` function to complex class hierarchies. In Android, the entry point is the class specified in the `AndroidManifest.xml` file, while in iOS, it's a struct with the `@main` annotation. The Entry Point may reside in the project's root or within the platform layer directory. The Application Entry Point is responsible for creating the dependency container and performing all necessary initializations.

### 3.3. External
The External layer represents the environment outside of the application's Core and Platform layers, encompassing the underlying platform, frameworks, and external libraries that the application relies upon for various functionalities. It acts as the interface between the application and the external world. This layer typically comprises a diverse set of components, including:

- **Operating System (OS)**: Provides fundamental services and resources, such as process management, memory allocation, and file system access.

- **Presentation Framework**: Handles the user interface and interaction, including UI elements, layout management, and event handling (e.g., Jetpack Compose, SwiftUI).

- **Persistence Mechanism**: Manages data storage and retrieval, encompassing databases (e.g., Room, SQLite), file systems, and other storage solutions.

- **Network Interfaces**: Facilitates communication with external systems and services, enabling data transfer and interaction with remote resources (e.g., Retrofit, OkHttp).

- **Dependency Injection (DI) Library**: Manages object creation and dependency resolution, promoting loose coupling and modularity (e.g., Hilt, Koin).

- **Third-Party Libraries**: Provides specialized functionalities, such as image loading, analytics, or payment processing, that are not part of the core application or platform (e.g., Glide, Firebase).

- **Hardware Interfaces**: Provides access to device hardware components, such as the camera, GPS, sensors, and Bluetooth.

- **Cloud Services**: Integrates with cloud-based platforms for services like data storage, authentication, and push notifications (e.g., AWS, Google Cloud, Azure).

- **System Services**: Provides access to platform-specific services, such as location services, notification management, and background tasks.
Security Frameworks: Provides security features such as encryption, authentication, and authorization.

## 5. Data Flow 
![Data Flow](../assets/images/data-flow-diagram.gif)

The application employs a unidirectional data flow, ensuring that data flows in a single direction. The data flow is as follows:

1.  The user interacts with the Graphical User Interface (GUI) through **Views**. These interactions can include actions like tapping a button, entering text, or swiping.
2.  The **View** captures the user interaction and triggers a corresponding **Action**. This action is then communicated to the associated **ViewModel**.
3.  The **ViewModel** receives the **Action** and processes it. Based on the action, the ViewModel may perform one or both of the following:
    *   a. The ViewModel invokes one or more **Use Cases** to execute the necessary business logic.
    *   b. The ViewModel instructs the **Navigator** to navigate to a different screen or destination within the application.
4.  The **Use Case** executes the required business logic. During this process, it may perform one or both of the following:
    *   a. The Use Case utilizes **Services** to perform specialized operations or to interact with external resources (e.g., network, database, sensors).
    *   b. The Use Case accesses and manipulates data through **Repositories**.
5.  After executing the business logic, the **Use Case** updates the **Core State Store** to reflect any changes to the application's core state.
6.  The **ViewModel** observes the **Core State Store** for changes.
7.  When the **ViewModel** detects a change in the **Core State Store**, it updates its internal state. This may involve one or both of the following:
    *   a. The ViewModel uses **UI Mappers** to transform the core state data into a format suitable for the UI, resulting in an updated **View State**.
    *   b. The ViewModel instructs the **Navigator** to navigate to a different screen or destination.
8.  The **View** observes the **ViewModel's View State** for changes.
9.  When the **View** detects a change in the **View State**, it updates the GUI to reflect the new state. The user sees the updated GUI, reflecting the current state of the application.


## 6. State Management

## 7. Dependency Diagram
To minimize coupling between system components, all dependencies are mediated through interfaces. Furthermore, dependencies flow inward, originating from input/output components (e.g., GUI, data sources) and directed towards core domain logic and entities, aligning with principles of [Clean Architecture](#). When the flow of control necessitates a reversal of this dependency direction, the [Dependency Inversion Principle (DIP)](#) is applied. Additionally, to decouple object creation from utilization, the [Abstract Factory](#) pattern is employed. The relationships between application components are visualized in the following UML diagram:

![Dependency Diagram](../assets/images/dependency-diagram.png)

## 8. Folder Structure
The Android and iOS applications were developed independently, utilizing the recommended languages and frameworks for each platform: [Kotlin with Jetpack Compose](https://developer.android.com/courses/android-basics-compose/course) for Android and [Swift with SwiftUI](https://developer.apple.com/tutorials/app-dev-training/) for iOS. Despite the technological differences, both implementations adhere to a common architecture and folder structure. The Android codebase resides in the `android` folder, while the iOS codebase is located in the `ios` folder.

The following diagram illustrates the application's folder structure:

```
-- src
   |-- core
   |   |-- data
   |   |   |-- data_source_ports
   |   |   |-- repositories
   |   |   \-- stores
   |   |
   |   |-- entities
   |   |
   |   |-- services
   |   |   \-- external_resource_ports
   |   |
   |   |-- use_cases
   |   |
   |   \-- utils
   | 
   \-- platform
       |-- data
       |   \-- [persistency library]
       |       |-- data_source_adapters
       |       \-- entities
       |
       |-- di
       |
       |-- presentation
       |   |-- entities
       |   |-- mappers
       |   |-- navigation
       |   |-- view_models
       |   |
       |   \-- [ui library]
       |       |--- components
       |       |--- navigation
       |       \--- views
       |
       \-- services
           \-- external_resource_adapters

-- tests
```

While platform-specific nuances may influence the final implementation details, the overall project structure and organization should remain consistent across platforms.

## 9. Implementation 
During architecture implementation, some exceptions were found:

- On Android, *ViewModel* depends on platform specific code, because the
  `androidx.lifecycle.ViewModel` base class must be extended. Otherwise, coroutines won't be
  lifecycle sensitive, possibly leading to memory leaks or unwanted background process running.
- Instead of using the [Command](#764-command) pattern to update the Core State Store, lambdas were applied.
  Thanks to closure, all command parameters can be implied from the lambda's creation context.

## 10. Error Handling Strategy

## 11. Testing Strategy
Unit tests were implemented for Use Cases, ViewModels, and UI Mappers, leveraging the respective testing frameworks for Android (i.e., JUnit) and iOS (i.e., XCTest). The testing strategy focused on covering critical logic within these components, prioritizing areas with higher potential for errors. This approach aims to ensure the quality and reliability of core application functionalities.
For more information about unit testing on a specific platform, check the following guides:
- [Unit tests on Android](https://developer.android.com/training/testing/local-tests)
- [Unit test on iOS](https://developer.apple.com/documentation/xcode/adding-tests-to-your-xcode-project)

## 12. Appendix

### 12.1. Unified Modeling Language (UML)
Unified Modeling Language (UML) is a standardized visual modeling language used to specify, visualize, construct, and document the artifacts of software systems. It provides a set of diagrams and notations for representing various aspects of a system, including its structure, behavior, and interactions. [Read more](https://www.geeksforgeeks.org/unified-modeling-language-uml-class-diagrams/).
