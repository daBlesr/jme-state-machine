package examples.sinbad.Head;

import JmeStateMachine.State;
import JmeStateMachine.StateChange;
import com.jme3.bullet.PhysicsSpace;

public class IdleState extends State {
    @Override
    protected void onEnter() {

    }

    @Override
    protected StateChange handleActionInput(String input, boolean isPressed, float tpf) {
        return null;
    }

    @Override
    protected StateChange handleAnalogInput(String input, float value, float tpf) {
        return null;
    }

    @Override
    public StateChange controlUpdate(float tpf) {
        return null;
    }

    @Override
    protected void onExit() {

    }

    @Override
    public void prePhysicsTick(PhysicsSpace space, float timeStep) {

    }

    @Override
    public void physicsTick(PhysicsSpace space, float timeStep) {

    }
}
