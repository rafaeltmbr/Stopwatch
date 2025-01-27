# Architecture

Table of contents
1. [Overview](#1-overview)

2. [Data Flow](#2-data-flow)
    - [2.1. User Interaction and View](#21-user-interaction-and-view)
    - [2.2. ViewModel and Action Handling](#22-viewmodel-and-action-handling)
    - [2.3. Navigation with Navigator](#23-navigation-with-the-navigator)
    - [2.4. Business Logic and Use Cases](#24-business-logic-and-use-cases)
    - [2.5. Domain State Store: The Single Source of Truth](#25-domain-state-store-the-single-source-of-truth)
    - [2.6. View State Updates and UI Mappers](#26-view-state-updates-and-ui-mappers)
3. [Dependency Diagram](#3-dependency-diagram)
4. [Code Structure](#4-code-structure)
    - 4.1. domain/
      - 4.1.1. data/
        - 4.1.1.1. data_sources/
        - 4.1.1.2. repositories/
        - 4.1.1.3. stores/
      - 4.1.2. entities/
      - 4.1.3. services/
        - 4.1.3.1. external_resources/
      - 4.1.4. use_cases/
      - 4.1.5. utils/
    - 4.2. infra/
      - 4.2.1. data/
        - 4.2.1.1. persistency_library/
          - 4.2.1.1.1. data_sources/
          - 4.2.1.1.2. entities/
      - 4.2.2. di/
      - 4.2.3. presentation/
        - 4.2.3.1. entities/
        - 4.2.3.2. mappers/
        - 4.2.3.3. navigation/
        - 4.2.3.4. view_models/
        - 4.2.3.5. ui_library/
          - 4.2.3.5.1. components/
          - 4.2.3.5.2. navigation/
          - 4.2.3.5.3. views/
      - 4.2.4. services/
          - 4.2.4.1. external_resources/
5. [Tests](#6-tests)
6. [Implementation Details](#5-implementation-details)
7. [Essential Concepts](#7-references)
    - [7.1. Unified Modeling Language (UML)](#71-unified-modeling-language-uml)
    - [7.2. Clean Architecture](#72-clean-architecture)
    - [7.4. Unidirectional Data Flow (UDF)](#74-unidirectional-data-flow-udf)
    - [7.5. Dependency Inversion Principle (DIP)](#75-dependency-inversion-principle-dip)
    - [7.6. Design Patterns](#76-design-patterns)
      - [7.6.1. Abstract Factory](#761-abstract-factory)
      - [7.6.2. Facade](#762-facade)
      - [7.6.3. Observer](#763-observer)
      - [7.6.4. Command](#764-command)

## 1. Overview
Despite its seemingly straightforward functionality, a stopwatch application presents a significant challenge in managing a high volume of events within short timeframes. The proposed implementation updates the stopwatch state every 10 milliseconds during operation, while concurrently handling user interactions. To address this demanding event processing requirement, the application leverages a [Unidirectional Data Flow (UDF)](#74-unidirectional-data-flow-udf) architecture.

The presented architecture is designed to be platform-agnostic. To validate this design principle, the architecture was implemented on both Android and iOS platforms, demonstrating its adaptability and portability across different environments.

## 2. Data Flow
The application's data flow usually begins with user interactions on the Graphical User Interface (GUI). These interactions trigger a series of processing steps, which result in data visualizations being presented back to the user through the GUI. This cyclical process is illustrated in the following diagram and described in detail by the subsequent sections:

<img src="../assets/images/data-flow-diagram.png" style="max-height: 700px">

### 2.1. User Interaction and View
The user interface (UI) is presented through Views, which are responsible for displaying information and handling user interactions. When a user interacts with a View (e.g., clicking a button), it emits an Action event. This event encapsulates relevant information about the interaction, such as Action.Start when the Start button is clicked.

### 2.2. ViewModel and Action Handling
The ViewModel acts as an intermediary between the View and the application logic. It receives Action events from the View and processes them accordingly. The ViewModel is responsible for:
- Executing relevant Use Cases based on the received Action.
- Instructing the Navigator to transition between different Views.
- Updating the View State to reflect changes in the application's data.

### 2.3. Navigation with the Navigator
The Navigator handles all screen navigation within the application. It interacts directly with the UI framework and libraries to manage View transitions and rendering. The ViewModel directs the Navigator to navigate to specific Views based on user actions or application logic.

### 2.4. Business Logic and Use Cases
Use Cases encapsulate the core business logic of the application. They may delegate tasks to Services for performing specilized operations or accessing external resources though the [Facade](#762-facade) pattern. Use Cases utilize Data Repositories to persist data beyond the application's lifecycle. During execution, Use Cases may update the application's state by emitting a new Domain State to the Domain State Store using the [Command](#764-command) pattern.

### 2.5. Domain State Store: The Single Source of Truth
The Domain State Store serves as the central repository for the application's state. Components interested in state changes can subscribe to updates using the [Observer](#763-observer) pattern. This ensures that all parts of the application have a consistent view of the current state.

### 2.6. View State Updates and UI Mappers
The ViewModel subscribes to Domain State changes and updates the View State accordingly. To transform Domain State into a format suitable for display, the ViewModel utilizes UI Mappers. These mappers handle data transformations and ensure the View State is optimized for UI rendering. The View, also subscribing to View State changes, receives updates via the [Observer](#763-observer) pattern and refreshes its display.

## 3. Dependency Diagram
To minimize coupling between system components, all dependencies are mediated through interfaces. Furthermore, dependencies flow inward, originating from input/output components (e.g., GUI, data sources) and directed towards core domain logic and entities, aligning with principles of [Clean Architecture](#72-clean-architecture). When the flow of control necessitates a reversal of this dependency direction, the [Dependency Inversion Principle (DIP)](#75-dependency-inversion-principle-dip) is applied. Additionally, to decouple object creation from utilization, the [Abstract Factory](#761-abstract-factory) pattern is employed. The relationships between application components are visualized in the following UML diagram:

![Dependency Diagram](../assets/images/dependency-diagram.png)

## 4. Code Structure
The Android and iOS applications were developed independently, utilizing the recommended languages and frameworks for each platform: [Kotlin with Jetpack Compose](https://developer.android.com/courses/android-basics-compose/course) for Android and [Swift with SwiftUI](https://developer.apple.com/tutorials/app-dev-training/) for iOS. Despite the technological differences, both implementations adhere to a common architecture and code structure. The Android codebase resides in the `android` folder, while the iOS codebase is located in the `ios` folder.


## 5. Tests
Unit tests were implemented for Use Cases, ViewModels, and UI Mappers, leveraging the respective testing frameworks for Android (i.e., JUnit) and iOS (i.e., XCTest). The testing strategy focused on covering critical logic within these components, prioritizing areas with higher potential for errors. This approach aims to ensure the quality and reliability of core application functionalities.
For more information about unit testing on a specific platform, check the following guides:
- [Unit tests on Android](https://developer.android.com/training/testing/local-tests)
- [Unit test on iOS](https://developer.apple.com/documentation/xcode/adding-tests-to-your-xcode-project)

## 6. Implementation Details
During architecture implementation, some exceptions were found:

- On Android, *ViewModel* depends on platform specific code, because the
  `androidx.lifecycle.ViewModel` base class must be extended. Otherwise, coroutines won't be
  lifecycle sensitive, possibly leading to memory leaks or unwanted background process running.
- Instead of using [Command](#764-command) pattern to update *MutableStateStore*, lambdas were used.
  Thanks to closure, all command parameters can be implied from the lambda creation context.

## 7. Essential Concepts

### 7.1. Unified Modeling Language (UML)
Unified Modeling Language (UML) is a standardized visual modeling language used to specify, visualize, construct, and document the artifacts of software systems. It provides a set of diagrams and notations for representing various aspects of a system, including its structure, behavior, and interactions. UML diagrams help developers and stakeholders understand, communicate, and collaborate on software design and development. [Read more](https://www.geeksforgeeks.org/unified-modeling-language-uml-class-diagrams/).

### 7.2. Clean Architecture
Clean Architecture Clean Architecture is a software design philosophy that emphasizes the separation of concerns and the independence of the core business logic from external frameworks and infrastructure. It promotes a layered architecture where dependencies flow inward, from outer layers like the UI and databases, towards the inner core domain logic. This approach enhances testability, maintainability, and portability of software systems. [Read more](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html).

### 7.4. Unidirectional Data Flow (UDF)
Unidirectional Data Flow (UDF) is an architectural pattern where data flows in a single direction, creating a predictable and manageable data flow within an application. It typically involves components like views, view models, and data stores, with data flowing from the View to the ViewModel and updates propagating back through defined channels. UDF simplifies state management, reduces complexity, and enhances the testability of applications. [Read more](https://developer.android.com/develop/ui/compose/architecture#udf).

### 7.5. Dependency Inversion Principle (DIP)
Dependency Inversion Principle (DIP) is a software design principle that promotes loose coupling between components by inverting the direction of dependencies. Instead of high-level modules depending on low-level modules, both depend on abstractions (interfaces). This allows for greater flexibility, maintainability, and testability by enabling components to be easily swapped or modified without affecting other parts of the system. [Read more](https://en.wikipedia.org/wiki/Dependency_inversion_principle).

### 7.6. Design Patterns 
Design Patterns Design patterns are reusable solutions to recurring design problems in software development. They provide proven approaches for structuring code, managing dependencies, and implementing common functionalities. Design patterns are categorized into creational, structural, and behavioral patterns, each addressing a specific type of design challenge. [Read more](https://refactoring.guru/design-patterns/what-is-pattern).

#### 7.6.1. Abstract Factory
The Abstract Factory pattern provides an interface for creating families of related or dependent objects without specifying their concrete classes. It allows for the creation of objects without knowing their specific types, promoting flexibility and loose coupling. This pattern is useful when a system needs to support multiple variations of objects or when the concrete types of objects should be determined at runtime. [Read more](https://refactoring.guru/design-patterns/abstract-factory).

#### 7.6.2. Facade
The Facade pattern provides a simplified interface to a complex subsystem, hiding its internal complexities and making it easier to use. It acts as a single point of entry for interacting with the subsystem, reducing dependencies and improving code clarity. This pattern is useful when working with large or complex libraries or when you want to provide a simplified API for a specific use case. [Read more](https://refactoring.guru/design-patterns/facade).

#### 7.6.3. Observer
The Observer pattern defines a one-to-many dependency between objects, where one object (the subject) notifies all its dependents (observers) of any state changes. This allows for loose coupling between objects, as the subject doesn't need to know the specific types of its observers. This pattern is commonly used for event handling, notifications, and data synchronization.[Read more](https://refactoring.guru/design-patterns/observer).

#### 7.6.4. Command
The Command pattern encapsulates a request as an object, thereby letting you parameterize clients with different requests, queue or log requests, and support undoable operations. It decouples the object that invokes the operation from the one that knows how to perform it. This pattern is useful for implementing undo/redo functionality, transaction management, and macro recording. [Read more](https://refactoring.guru/design-patterns/command).
