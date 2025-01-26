# Stopwatch

<img src="./docs/assets/images/android-mockups.png" title="Android app" style="max-height: 600px">
<img src="./docs/assets/images/ios-mockups.png" title="iOS app" style="max-height: 600px">

## Architecture

### Data Flow

<img src="./docs/assets/images/data-flow-diagram.png" title="Data Flow" style="max-height: 700px">

#### 1. User Interaction and View
The user interface (UI) is presented through Views, which are responsible for displaying information and handling user interactions. When a user interacts with a View (e.g., clicking a button), it emits an Action event. This event encapsulates relevant information about the interaction, such as Action.Start when the Start button is clicked.

#### 2. ViewModel and Action Handling
The ViewModel acts as an intermediary between the View and the application logic. It receives Action events from the View and processes them accordingly. The ViewModel is responsible for:
Executing relevant Use Cases based on the received Action.
Instructing the Navigator to transition between different Views.
Updating the View State to reflect changes in the application's data.

#### 3. Navigation with the Navigator
The Navigator handles all screen navigation within the application. It interacts directly with the UI framework and libraries to manage View transitions and rendering. The ViewModel directs the Navigator to navigate to specific Views based on user actions or application logic.

#### 4. Business Logic and Use Cases
Use Cases encapsulate the core business logic of the application. They may delegate tasks to Services for accessing external resources (via the [Facade Pattern](https://refactoring.guru/design-patterns/facade)) or performing specialized operations. Use Cases utilize Data Repositories to persist data beyond the application's lifecycle. During execution, Use Cases may update the application's state by emitting a new Domain State to the Domain State Store.

#### 5. Domain State Store: The Single Source of Truth
The Domain State Store serves as the central repository for the application's state. Components interested in state changes can subscribe to updates using the [Observer Pattern](https://refactoring.guru/design-patterns/observer). This ensures that all parts of the application have a consistent view of the current state.

#### 6. View State Updates and UI Mappers
The ViewModel subscribes to Domain State changes and updates the View State accordingly. To transform Domain State into a format suitable for display, the ViewModel utilizes UI Mappers. These mappers handle data transformations and ensure the View State is optimized for UI rendering. The View, also subscribing to View State changes, receives updates via the [Observer Pattern](https://refactoring.guru/design-patterns/observer) and refreshes its display.

### Architecture UML Diagram

![Architecture Diagram](./docs/assets/images/architecture-diagram.png)


## Implementation

During architecture implementation, some exceptions were found:

- On Android, *ViewModel* depends on platform specific code, because the
  `androidx.lifecycle.ViewModel` base class must be extended. Otherwise, coroutines won't be
  lifecycle sensitive, possibly leading to memory leaks or unwanted background process running.
- Instead of using **Command Pattern** to update *MutableStateStore*, lambdas were used.
  Thanks to closure, all command parameters can be implied from the lambda creation context.
