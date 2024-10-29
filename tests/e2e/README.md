
# e2e
        
[![:tests:e2e](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/e2e-build.yml/badge.svg)](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/e2e-build.yml)


## Ownership
This module is maintained by *TEAM1*

## Dependencies

### Library

- [core](https://github.com/albertlatacz/shift-left-kotlin/tree/main/libraries/core) [![:libraries:core](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/core-build.yml/badge.svg)](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/core-build.yml)
- [slack](https://github.com/albertlatacz/shift-left-kotlin/tree/main/libraries/slack) [![:libraries:slack](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/slack-build.yml/badge.svg)](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/slack-build.yml)

### Service

- [api](https://github.com/albertlatacz/shift-left-kotlin/tree/main/services/api) [![:services:api](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/api-build.yml/badge.svg)](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/api-build.yml)
- [filestore](https://github.com/albertlatacz/shift-left-kotlin/tree/main/services/filestore) [![:services:filestore](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/filestore-build.yml/badge.svg)](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/filestore-build.yml)

## Dependency Diagram

```mermaid
C4Context        
   UpdateLayoutConfig($c4ShapeInRow="2")                           
   Container(e2e, "e2e")
   Component(core, "core")
   Component(slack, "slack")
   System(api, "api")
   System(filestore, "filestore")
   Rel(e2e, core, " ") 
   UpdateRelStyle(e2e, core, $lineColor="blue")
   Rel(e2e, slack, " ") 
   UpdateRelStyle(e2e, slack, $lineColor="blue")
   Rel(e2e, api, " ") 
   UpdateRelStyle(e2e, api, $lineColor="blue")
   Rel(e2e, filestore, " ") 
   UpdateRelStyle(e2e, filestore, $lineColor="blue")                
```