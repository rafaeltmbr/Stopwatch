# Stopwatch

Android Stopwatch App

![App](./docs/assets/images/home-screen.png)

## Architecture

App UML diagram of an Unidirectional Dataflow Architecture

![Architecture Diagram](./docs/assets/images/architecture-diagram.png)

## Implementation

During architecture implementation, some exceptions were found:

- On Android, *ViewModel* depends on platform specific code, because the
  `androidx.lifecycle.ViewModel` base class must be extended. Otherwise, coroutines won't be
  lifecycle sensitive, possibly leading to memory leaks or unwanted background process running.
