
# filestore
        
[![:services:filestore](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/filestore-build.yml/badge.svg)](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/filestore-build.yml)


## Ownership
This module is maintained by *TEAM1*


## Dependencies
- [core](https://github.com/albertlatacz/shift-left-kotlin/tree/main/libraries/core)

## Dependency Diagram

```mermaid
C4Context        
   UpdateLayoutConfig($c4ShapeInRow="2")                           
   System(filestore, "filestore")
   Component(core, "core")
   Rel(filestore, core, " ") 
   UpdateRelStyle(filestore, core, $lineColor="blue")                
```