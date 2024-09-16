# shift-left-kotlin
Example of shift-left engineering practices in Kotlin       

## Build Overview

### Library

- [![:libraries:core](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/core-build.yml/badge.svg)](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/core-build.yml)

### Service

- [![:services:api](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/api-build.yml/badge.svg)](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/api-build.yml)
- [![:services:filestore](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/filestore-build.yml/badge.svg)](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/filestore-build.yml)


## Dependency Diagram

```mermaid
C4Context        
   UpdateLayoutConfig($c4ShapeInRow="2")                           
   Component(core, "core")
   System(api, "api")
   System(filestore, "filestore")
   Rel(api, core, " ") 
   UpdateRelStyle(api, core, $lineColor="blue")
   Rel(filestore, core, " ") 
   UpdateRelStyle(filestore, core, $lineColor="blue")                
```