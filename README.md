# jme-state-machine

jMonkeyEngine state machine helps to define complex behaviour using states. This way, one can avoid cluttered code with lots of if-else branches checking if the model is walking or running, then do x or y.. This way complicated physics/input handling that are tight to some states, need only there be implemented.

Implementation and architecture are based on the book "Game Programming Patterns", chapter [State](https://gameprogrammingpatterns.com/state.html).
In the examples directory, the code behind the video [demo](https://www.youtube.com/watch?v=UY6encoXvIE) is listed.

The State Machine consists of four entities: *ModelStateMachine*, *Layer*, *State*, *StateChange*.

The Documentation explains jme-state-machine in the following chapters:
1. [Create a ModelStateMachine](https://github.com/daBlesr/jme-state-machine/blob/main/docs/create-a-state-machine.md)
3. [Create a State](https://github.com/daBlesr/jme-state-machine/blob/main/docs/state.md)
4. [Change to a new State](https://github.com/daBlesr/jme-state-machine/blob/main/docs/change%20state.md)
5. [Layers](https://github.com/daBlesr/jme-state-machine/blob/main/docs/layers.md)
6. [Complex cases](https://github.com/daBlesr/jme-state-machine/blob/main/docs/layers.md)




