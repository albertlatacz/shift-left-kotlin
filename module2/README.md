
# module2
        
[![:module2](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/module2-build.yml/badge.svg)](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/module2-build.yml)


## Ownership
This module is maintained by *TEAM2*


## Dependencies
- [core](https://github.com/albertlatacz/shift-left-kotlin/tree/main/libraries/core)
- [module1](https://github.com/albertlatacz/shift-left-kotlin/tree/main/module1)

### Dependency Diagram

```mermaid
C4Context        
   UpdateLayoutConfig($c4ShapeInRow="2")                           
   Container(module2, "module2")
   Rel(module2, core, " ") 
   UpdateRelStyle(module2, core, $lineColor="blue")
   Rel(module2, module1, " ") 
   UpdateRelStyle(module2, module1, $lineColor="blue")                
```