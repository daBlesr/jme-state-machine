package JmeStateMachine;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;

import java.util.LinkedList;
import java.util.List;

public class Layer implements ActionListener, AnalogListener, PhysicsTickListener {

    private ModelStateMachine modelStateMachine;
    private LinkedList<State> states = new LinkedList<>();

    public void setInitialState (State state) {
        this.states.add(state);
        state.setSpatial(modelStateMachine.getSpatial());
        state.onEnter();
    }

    protected void setModelStateMachine (ModelStateMachine modelStateMachine) {
        this.modelStateMachine = modelStateMachine;
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        State state = states.getFirst().handleActionInput(name, isPressed, tpf);
        enterState(state);
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        State state = states.getFirst().handleAnalogInput(name, value, tpf);
        enterState(state);
    }

    @Override
    public void prePhysicsTick(PhysicsSpace space, float timeStep) {
        State state = states.getFirst().prePhysicsTick(space, timeStep);
        enterState(state);
    }

    @Override
    public void physicsTick(PhysicsSpace space, float timeStep) {
        states.forEach(s -> s.physicsTick(space, timeStep));
    }

    protected void controlUpdate (float tpf) {
        states.getFirst().controlUpdate(tpf);
    }

    private void enterState (State state) {
        if (state == null) {
            if (states.getFirst().popState) {
                states.removeFirst();
            }
        } else {
            state.onExit();
            states.addFirst(state);
            state.setSpatial(modelStateMachine.getSpatial());
            state.onEnter();
        }
    }

    public State getCurrentState() {
        return states.getFirst();
    }
}
