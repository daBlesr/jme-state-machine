package examples.sinbad.Base;

import JmeStateMachine.State;
import com.jme3.bullet.PhysicsSpace;

public class GroundState extends State {
    @Override
    protected void onEnter() {

    }

    @Override
    protected State handleActionInput(String input, boolean isPressed, float tpf) {
        return null;
    }

    @Override
    protected State handleAnalogInput(String input, float value, float tpf) {
        return null;
    }

    @Override
    protected State prePhysicsTick(PhysicsSpace space, float timeStep) {
        return null;
    }

    @Override
    protected void physicsTick(PhysicsSpace space, float timeStep) {

    }

    @Override
    public void controlUpdate(float tpf) {

    }

    @Override
    protected void onExit() {

    }
}
