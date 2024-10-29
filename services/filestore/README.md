
# filestore
        
[![:services:filestore](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/filestore-build.yml/badge.svg)](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/filestore-build.yml)


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
   System(filestore, "filestore")
   Component(core, "core")
   Component(slack, "slack")
   Rel(filestore, core, " ") 
   UpdateRelStyle(filestore, core, $lineColor="blue")
   Rel(filestore, slack, " ") 
   UpdateRelStyle(filestore, slack, $lineColor="blue")                
```