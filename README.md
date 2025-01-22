# Stopwatch

Stopwatch App

<img src="./docs/assets/images/home-android.png" title="Android app" height=600>
<img src="./docs/assets/images/home-ios.png" title="iOS app" height=600>

## Architecture

App UML diagram of an Unidirectional Dataflow Architecture

![Architecture Diagram](./docs/assets/images/architecture-diagram.png)

## Implementation

During architecture implementation, some exceptions were found:

- On Android, *ViewModel* depends on platform specific code, because the
  `androidx.lifecycle.ViewModel` base class must be extended. Otherwise, coroutines won't be
  lifecycle sensitive, possibly leading to memory leaks or unwanted background process running.
- Instead of using **Command Pattern** to update *MutableStateStore*, lambdas were used.
  Thanks to closure, all command parameters can be implied from the lambda creation context.
