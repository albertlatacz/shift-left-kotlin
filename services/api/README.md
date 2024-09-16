
# api
        
[![:services:api](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/api-build.yml/badge.svg)](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/api-build.yml)


## Ownership
This module is maintained by *TEAM1*


## Dependencies
- [core](https://github.com/albertlatacz/shift-left-kotlin/tree/main/libraries/core)

## Dependency Diagram

```mermaid
C4Context        
   UpdateLayoutConfig($c4ShapeInRow="2")                           
   System(api, "api")
   Component(core, "core")
   Rel(api, core, " ") 
   UpdateRelStyle(api, core, $lineColor="blue")                
```