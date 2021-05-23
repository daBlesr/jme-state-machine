# jme-state-machine

jMonkeyEngine state machine helps to define complex behaviour using states. This way, one can avoid cluttered code with lots of if-else branches checking if the model is walking or running, then do x or y.. This way complicated physics/input handling that are tight to some states, need only there be implemented.

Implementation and architecture are based on the book "Game Programming Patterns", chapter [State](https://gameprogrammingpatterns.com/state.html).
In the examples directory, the code behind the video [demo](https://www.youtube.com/watch?v=UY6encoXvIE) is listed.

The State Machine consists of four entities: *ModelStateMachine*, *Layer*, *State*, *StateChange*.
One creates a state machine in jMonkeyEngine using the following:
```
ModelStateMachine modelStateMachine = new ModelStateMachine();
spatial.addControl(modelStateMachine);
```        
To add a layer, one needs to set the default state.
```
Layer modelBaseLayer = new Layer();
modelStateMachine.addLayer(modelBaseLayer);
modelBaseLayer.setInitialState(new IdleState());
```
The idea is to go to a new state by one of the following methods:
* `handleActionInput`
* `handleAnalogInput`
* `controlUpdate`

There are multiple ways to change state. return on one of the above methods a StateChange instance by using either
* `StateChange.to(new SomeOtherState())`
* `StateChange.push(new TemporaryState())`
* `StateChange.pop()`

The state machine is a pushdown automaton, meaning it is possible to push a state temporarily on the stack (and afterwards pop it from that state). 
In the video this happens, when Sinbad runs into the sword, it picks it up, and then returns back to the state it was doing.

Create a class that extends the abstract State class, and you are good to go.
When a state is entered, automatically the `onEnter` method is called by the state machine. Likewise, on leaving a state, the `onExit` method is called.
In these methods, initialize (and detach) bindings to physics tick listeners and start (and stop) animations.

Layers are used when different states need to be defined for different parts of the model, for instance equipment versus torso behavior. In the example in this project, a layer is created for the "look at sword behavior". It uses states from the other layer to know when the sword is attach to the model using:
```
if (getLayer()
       .getLayers()
       .stream()
       .anyMatch(l -> l.hasState(PickUpSwordState.class))
) {
    return StateChange.to(new IdleState());
}
```  


