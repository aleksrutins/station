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
Coming soon. For now, just look at the [tests](https://github.com/aleksrutins/station/tree/master/station/src/test/java/com/rutins/aleks/station).