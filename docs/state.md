## State

Create a class that extends the abstract State class, and you are good to go.
When a state is entered, automatically the `onEnter` method is called by the state machine. Likewise, on leaving a state, the `onExit` method is called.
In these methods, initialize (and detach) bindings to physics tick listeners and start (and stop) animations.
Each state is represented by an instance of a class, which makes it easy to extend behaviour of a state by subclassing. For instance, all the states of the character on ground, could extend a GroundState which itself extends State.

It is important to note that `controlUpdate` has an argument `List<StateChange> statechanges`. After a statechange from `handleActionInput` or `handleAnalogInput`, the state is not directly changed to that new state. First, the state machine waits for the control update tick. From experience, I can tell in many cases this is favourable. A `onGround()`  check in `controlUpdate` could be more important than a statechange from `handleActionInput`, for example.
The developer has control on what behaviour is run first. To see the State Changes from `handleActionInput` or `handleAnalogInput`, use the argument `stateChanges`. 

A simple state would look like:

```java
public class IdleState extends State {

    Vector3f velocity = new Vector3f();

    @Override
    protected void onEnter() {
        super.onEnter();
        AnimComposer animComposer = getSpatial().getControl(AnimComposer.class);
        animComposer.setCurrentAction("IdleBase", "bot");

        getPhysicsSpace().addTickListener(this);
    }

    @Override
    protected StateChange handleActionInput(String input, boolean isPressed, float tpf) {
        if (input.equals("Walk Forward")) {
            return StateChange.to(new RunningState());
        }
        return null;
    }

    @Override
    protected StateChange handleAnalogInput(String input, float value, float tpf) {
        if (input.equals("Rotate Left")) {
            rotateLeft();
        } else if (input.equals("Rotate Right")) {
            rotateRight();
        }
        return null;
    }

    @Override
    public void prePhysicsTick(PhysicsSpace space, float timeStep) {}

    @Override
    public void physicsTick(PhysicsSpace space, float timeStep) {}

    @Override
    public StateChange controlUpdate(float tpf, List<StateChange> stateChanges) {
        if (stateChanges.size() >= 1) {
            return stateChanges.get(0);
        }
        return null;
    }

    @Override
    protected void onExit() {
        getPhysicsSpace().removeTickListener(this);
    }
}
```
---
Next Topic: [State Change](https://github.com/daBlesr/jme-state-machine/blob/main/docs/change%20state.md)
