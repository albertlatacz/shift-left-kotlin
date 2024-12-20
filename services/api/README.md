
# api
        
[![:services:api](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/api-build.yml/badge.svg)](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/api-build.yml)


## Ownership
This module is maintained by *TEAM1*

## Dependencies

### Library

- [core](https://github.com/albertlatacz/shift-left-kotlin/tree/main/libraries/core) [![:libraries:core](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/core-build.yml/badge.svg)](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/core-build.yml)
- [slack](https://github.com/albertlatacz/shift-left-kotlin/tree/main/libraries/slack) [![:libraries:slack](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/slack-build.yml/badge.svg)](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/slack-build.yml)

## Dependency Diagram

```mermaid
C4Context        
   UpdateLayoutConfig($c4ShapeInRow="2")                           
   System(api, "api")
   Component(core, "core")
   Component(slack, "slack")
   Rel(api, core, " ") 
   UpdateRelStyle(api, core, $lineColor="blue")
   Rel(api, slack, " ") 
   UpdateRelStyle(api, slack, $lineColor="blue")                
```