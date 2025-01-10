# Stopwatch

Android Stopwatch App

![App](./docs/assets/images/home-screen.png)

## Architecture

App UML diagram of an Unidirectional Dataflow Architecture

![Architecture Diagram](./docs/assets/images/architecture-diagram.png)

## Implementation

During architecture implementation, some exceptions were found:

- On Android, *ViewModel* depends on the platform because, the `ViewModel` superclass must
  be extended. Otherwise, coroutine scopes WILL NOT be lifecycle sensitive. This platform dependency
  prevents the *ViewModel* to be unit tested. Instrumentation tests are an alternative to unit
  tests.
