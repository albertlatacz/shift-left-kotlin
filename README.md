# shift-left-kotlin
Example of shift-left engineering practices in Kotlin       

## Build Overview

### Library

- [core](https://github.com/albertlatacz/shift-left-kotlin/tree/main/libraries/core) [![:libraries:core](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/core-build.yml/badge.svg)](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/core-build.yml)
- [slack](https://github.com/albertlatacz/shift-left-kotlin/tree/main/libraries/slack) [![:libraries:slack](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/slack-build.yml/badge.svg)](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/slack-build.yml)

### Service

- [api](https://github.com/albertlatacz/shift-left-kotlin/tree/main/services/api) [![:services:api](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/api-build.yml/badge.svg)](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/api-build.yml)
- [filestore](https://github.com/albertlatacz/shift-left-kotlin/tree/main/services/filestore) [![:services:filestore](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/filestore-build.yml/badge.svg)](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/filestore-build.yml)

### Tests

- [e2e](https://github.com/albertlatacz/shift-left-kotlin/tree/main/tests/e2e) [![:tests:e2e](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/e2e-build.yml/badge.svg)](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/e2e-build.yml)


## Dependency Diagram

```mermaid
C4Context        
   UpdateLayoutConfig($c4ShapeInRow="2")                           
   Component(core, "core")
   Component(slack, "slack")
   System(api, "api")
   System(filestore, "filestore")
   Container(e2e, "e2e")
   Rel(api, core, " ") 
   UpdateRelStyle(api, core, $lineColor="blue")
   Rel(e2e, core, " ") 
   UpdateRelStyle(e2e, core, $lineColor="blue")
   Rel(e2e, api, " ") 
   UpdateRelStyle(e2e, api, $lineColor="blue")
   Rel(e2e, filestore, " ") 
   UpdateRelStyle(e2e, filestore, $lineColor="blue")
   Rel(filestore, core, " ") 
   UpdateRelStyle(filestore, core, $lineColor="blue")
   Rel(slack, core, " ") 
   UpdateRelStyle(slack, core, $lineColor="blue")                
```