![test](https://github.com/aleksrutins/station/actions/workflows/test.yml/badge.svg)
![publish](https://github.com/aleksrutins/station/actions/workflows/gradle-publish.yml/badge.svg)
![latest](https://shields.io/github/v/tag/aleksrutins/station?sort=semver)

# Station
Station is an atomic, thread-safe, observable state library based on Redux-like reducers for Java (>= 11). It is fully unit-tested.

## Features
- Thread-safe
- Atomic
- Mutations are controlled using reducers
- Easily serializable
- Observable mutations

## Installation
Use [GitHub Packages](https://github.com/aleksrutins/station/packages/1275693).
To add the repository, some gymnastics are required; I use [0ffz/gpr-for-gradle](https://github.com/0ffz/gpr-for-gradle), but there are probably other options.

Then, once the repository is added, add the package to your dependencies. \
Gradle:
```groovy
dependencies {
    implementation 'com.rutins.aleks:station:1.2.0'
}
```
Maven:
```xml
<dependency>
  <groupId>com.rutins.aleks</groupId>
  <artifactId>station</artifactId>
  <version>1.2.0</version>
</dependency>
```
Replace `1.2.0` with whichever version you would like to use - the latest should be listed in a badge above.

## Usage
### Basics
To create a piece of mutable state, use `State.constructor()`, which returns a `StateConstructor`, which can be used to add utilities and then create the state object. To create immutable state (I am fully aware that that is an oxymoron), use `State.immutable()`. Use `State#get()` to retrieve the current value. Both `State` and `StateConstructor` are in the `com.rutins.aleks.station` package.
```java
// Mutable state
var myState = State.constructor().create(reducer, value);

// Immutable value
var myImmutable = State.immutable(value);

// Retrieve the value
myState.get();
```

### Reducers
State objects use [reducers](https://redux.js.org/tutorials/fundamentals/part-2-concepts-data-flow#reducers) to control mutability. A reducer is just a lambda or functional object that takes in the current state and a message and returns the new state. The message is of a user-defined type. A reducer is passed as the first argument to `StateConstructor.create()`. Use `State#mutate()` to send a message to the reducer. The `Reducer` type is in the `com.rutins.aleks.station.callbacks` package.
```java
enum Message {
    Increment,
    Decrement
}

Reducer<Integer, Message> reducer = (value, message) -> switch message {
    Increment -> value + 1;
    Decrement -> value - 1;
};

State<Integer, Message> state = State.constructor().create(reducer, 5);

state.get(); // 5

state.mutate(Message.Increment);

state.get(); // 6

state.mutate(Message.Decrement);

state.get(); // 5
```

### Observers
You can also observe state for changes using observers, which are lambdas or functional objects implementing `com.rutins.aleks.station.callbacks.Observer`. An observer takes two parameters, the previous value and the value after the mutation. Attach them using `State#observe()`.
```java
Observer<Integer> observer = (oldValue, newValue) -> {
    System.out.println("Old value: " + oldValue + ", new value: " + newValue);
}

state.observe(observer);

state.mutate(Message.Increment); // Prints: "Old value: 5, new value: 6"
```