# Welcome to the Montyhall Game
### This is a console game of the classic Monty Hall problem


## Thoughts
### Classes

#### GameService
I have chosen to keep all the game logic in the `GameService`, and keep it in a fully testable state.

#### GameSetup
The `GameSetup` contains the entities of Boxes and the ability to create new Boxes for new new games in case of being able to play over and over again.

#### Box
The `Box` class contains the information and states that are relevant to the box itself, such as if it's active or not, or containing the prize.
#### MontyhallGame
The class `MontyhallGame` should only act as a "frontend" and also contains the validation for inputs. 

### Validation and other 'smelly' code stuff
There are places that could contain more validation in the `GameService`, but since this is such a small scaled game where all the inputs are restricted and known beforehand, I felt this was enough.

There is a questionable `Thread.sleep`, but again, since this is a small scaled game this was a fast and well-known way of pausing the execution in a synchronous way. If it was a bigger scale and in production I would probably have looked more into using something like a `ScheduledExecutorService` instead.