# shift-left-kotlin
Example of shift-left engineering practices in Kotlin       

### Build Overview

- [![:libraries:core](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/core-build.yml/badge.svg)](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/core-build.yml)
- [![:module1](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/module1-build.yml/badge.svg)](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/module1-build.yml)
- [![:module2](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/module2-build.yml/badge.svg)](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/module2-build.yml)
- [![:services:auth](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/auth-build.yml/badge.svg)](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/auth-build.yml)

### Dependency Diagram

```mermaid
C4Context        
   UpdateLayoutConfig($c4ShapeInRow="3")           
   System(app, "app")
   System(auth, "auth")        
   Rel(app, auth, " ")
   UpdateRelStyle(app, auth, $lineColor="blue")        
```