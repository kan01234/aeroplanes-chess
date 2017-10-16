# Aeroplanes Chess
Aeroplane Chess game implement by Spring WebSocket. More about aeroplane chess can view this [link](https://en.wikipedia.org/wiki/Aeroplane_Chess). 

## Getting Started
mvn spring boot:run

## Prerequisites
* JAVA 8 Runtime
* Maven 3.3
* Web browser support WebSocket client

### Game Rule
1. Roll 2, 4, 6 can move one of the plane from base to take off point
2. Can send back opposing plane after jump
3. if destination of the move has more than two opposing planes, moved plane back to the base
4. Roll 6 can continue the turn, however if the third roll is 6, all of the plane of that player need to back to the base
5. An additional shortcut when the plane land exactly on that cell, but it will not send back opposing planes on the lane
6. have fun

#### short	cut table
| Tables        | Are           | Cool  |
| ------------- |:-------------:| -----:|
| col 3 is      | right-aligned | $1600 |
| col 2 is      | centered      |   $12 |
| zebra stripes | are neat      |    $1 |

### How to win the game?
1. Plane must go into the center goal with exactly roll. The first player to finish all of the plane will win the game. And the game end
2. You are the last player remaining in the playing game

### TODO
- [ ] draw shortcut line on the board
- [ ] change turn continued field to support multi-thread
- [ ] move player service and other room logic to another project
