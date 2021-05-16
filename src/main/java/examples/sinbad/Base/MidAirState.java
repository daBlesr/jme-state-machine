package examples.sinbad.Base;

import JmeStateMachine.State;
import com.jme3.anim.AnimComposer;
import com.jme3.bullet.PhysicsSpace;

public class MidAirState extends SinbadBaseState {
    @Override
    protected void onEnter() {
        AnimComposer animComposer = getSpatial().getControl(AnimComposer.class);

        animComposer.setCurrentAction("JumpLoop");
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
        super.prePhysicsTick(space, timeStep);

        System.out.println("on ground mid air: " + onGround());
        if (onGround()) {
            return new IdleState();
        }

        return null;
    }

    @Override
    protected void physicsTick(PhysicsSpace space, float timeStep) {

    }

    @Override
    public State controlUpdate(float tpf) {
        return null;
    }

    @Override
    protected void onExit() {

    }
}
