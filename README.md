# Stopwatch

<img src="./docs/assets/images/android-mockups.png" title="Android app" style="max-height: 600px">
<img src="./docs/assets/images/ios-mockups.png" title="iOS app" style="max-height: 600px">

## Architecture

### Data Flow

<img src="./docs/assets/images/data-flow-diagram.png" title="Data Flow" style="max-height: 700px">


### Architecture UML Diagram
App UML diagram of an Unidirectional Dataflow Architecture

![Architecture Diagram](./docs/assets/images/architecture-diagram.png)


## Implementation

During architecture implementation, some exceptions were found:

- On Android, *ViewModel* depends on platform specific code, because the
  `androidx.lifecycle.ViewModel` base class must be extended. Otherwise, coroutines won't be
  lifecycle sensitive, possibly leading to memory leaks or unwanted background process running.
- Instead of using **Command Pattern** to update *MutableStateStore*, lambdas were used.
  Thanks to closure, all command parameters can be implied from the lambda creation context.
