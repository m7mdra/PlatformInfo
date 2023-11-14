# PlatformInfo

PlatformInfo is a Kotlin Multiplatform library that provides information about the current software package for both Android and iOS platforms.

## Overview

The library includes functionality to retrieve details such as the application name, package name, version, build, installer store, and more. It is designed for Kotlin Multiplatform projects, making it easy to obtain consistent package information across different platforms.

### Properties of `Package` 
Almost all propties of project are avaiable except for `buildSignature` 
| Property    | Android | iOS |
| :---:        |   :----:   |          :---: |
| `appName`      | ✅       | ✅   |
| `packageName`   | ✅        | ✅      |
| `version`   | ✅        | ✅      |
| `build`   | ✅        | ✅      |
| `installerStore`   | ✅        | ✅      |
| `buildSignature`   | ✅        |       |

## Features

- Retrieve comprehensive package information on Android and iOS platforms.
- Determine the installer store based on the app's origin.

## Usage

```kotlin
val packageInfo = PackageInfo.current()
println("Package Information: $packageInfo")
```
## Installation

> TODO
