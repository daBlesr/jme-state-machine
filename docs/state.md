## State

Create a class that extends the abstract State class, and you are good to go.
When a state is entered, automatically the `onEnter` method is called by the state machine. Likewise, on leaving a state, the `onExit` method is called.
In these methods, initialize (and detach) bindings to physics tick listeners and start (and stop) animations.

A simple state would look like:

```
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
