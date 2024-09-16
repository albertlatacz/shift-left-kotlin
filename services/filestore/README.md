
# filestore
        
[![:services:filestore](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/filestore-build.yml/badge.svg)](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/filestore-build.yml)


## Ownership
This module is maintained by *TEAM1*

## Dependencies

### Library

- [![:libraries:core](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/core-build.yml/badge.svg)](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/core-build.yml)

## Dependency Diagram

```mermaid
C4Context        
   UpdateLayoutConfig($c4ShapeInRow="2")                           
   System(filestore, "filestore")
   Component(core, "core")
   Rel(filestore, core, " ") 
   UpdateRelStyle(filestore, core, $lineColor="blue")                
```