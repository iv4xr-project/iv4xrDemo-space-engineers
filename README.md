# ⚠ This project has been moved elsewhere!

**⚠ This project has been moved into the plugin repository**: [Space Engineers iv4XR plugin](https://github.com/iv4xr-project/iv4xr-se-plugin). You can find it in the [JvmClient](https://github.com/iv4xr-project/iv4xr-se-plugin/tree/main/JvmClient) subdirectory of the plugin repository. **⚠**

The merge of the repositories allows us to do interface changes across both repositories in a single pull request and keep the projects compatible.

(This repository will be removed in the future.)

.

.

.

# Original readme follows

## Space Engineers Demo

This is a demo for the [iv4XR testing framework](https://github.com/iv4xr-project/aplib), demonstrating that iv4XR test agents can control [_Space Engineers_](https://www.spaceengineersgame.com/) (a game by [Keen Software House](https://www.keenswh.com/)) to perform some testing tasks. This repository started as a fork of the [*Lab Recruits* demo](https://github.com/iv4xr-project/iv4xrDemo), but has been significantly modified since.

It is not intended for general use, other than as a testing project for the development of the [Space Engineers iv4XR plugin](https://github.com/iv4xr-project/iv4xr-se-plugin). For more details, please refer to the plugin repository README. 

# Setup

## How to build

For easy copy-paste, here's the git clone command for this repository:

```
git clone git@github.com:iv4xr-project/iv4xrDemo-space-engineers.git
```

We are using Gradle as the build system. To build the project, run the Gradle task `build`:

```
./gradlew build
```

## How to run unit tests

To build and run unit tests, run:

```
./gradlew :cleanJvmTest :jvmTest --tests "spaceEngineers.mock.*"
```

## Running iv4xr tests

The tests require Space Engineers running with the iv4XR plugin enabled.


```
./gradlew :cleanJvmTest :jvmTest --tests "spaceEngineers.iv4xr.*"
```


## Running BDD feature tests

Test scenarios also require Space Engineers running with the iv4XR plugin enabled.

For now, we run BDD tests from IDEA.

* Make sure you have installed [plugins](https://www.jetbrains.com/help/idea/enabling-cucumber-support-in-project.html#cucumber-plugin) `Gherkin` and `Cucumber for Java`
* Right-click [.feature file](https://github.com/iv4xr-project/iv4xrDemo-space-engineers/tree/se-dev/src/jvmTest/resources/features) in IDEA and select "Run".

## Using Eclipse

Eclipse does not support Kotlin multiplatform projects and so far we haven't been able to configure it to run with Kotlin JVM.
We recommend using the project with JetBrains [IDEA](https://www.jetbrains.com/idea/download/).
