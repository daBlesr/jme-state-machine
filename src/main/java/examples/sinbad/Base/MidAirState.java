package examples.sinbad.Base;

import JmeStateMachine.StateChange;
import com.jme3.anim.AnimComposer;
import com.jme3.bullet.PhysicsSpace;

import java.util.List;

import static com.jme3.bullet.PhysicsSpace.getPhysicsSpace;

public class MidAirState extends SinbadBaseState {

    private boolean isWalkingForward = false;

    @Override
    protected void onEnter() {
        super.onEnter();
        AnimComposer animComposer = getSpatial().getControl(AnimComposer.class);

        animComposer.setCurrentAction("JumpLoop", "top");
        animComposer.setCurrentAction("JumpLoop", "bot");

        getPhysicsSpace().addTickListener(this);
    }

    @Override
    protected StateChange handleActionInput(String input, boolean isPressed, float tpf) {
        return  null;
    }

    @Override
    protected StateChange handleAnalogInput(String input, float value, float tpf) {
        if (input.equals("Walk Forward")) {
            isWalkingForward = true;
        }
        return null;
    }

    @Override
    public void prePhysicsTick(PhysicsSpace space, float timeStep) {
        super.prePhysicsTick(space, timeStep);
    }

    @Override
    public void physicsTick(PhysicsSpace space, float timeStep) {
        super.physicsTick(space, timeStep);
    }

    @Override
    public StateChange controlUpdate(float tpf, List<StateChange> stateChanges) {
        super.controlUpdate(tpf, stateChanges);

        if (onGround()) {
            return StateChange.to(isWalkingForward ? new RunningState() : new IdleState());
        }

        return null;
    }

    @Override
    protected void onExit() {
        getPhysicsSpace().removeTickListener(this);
    }
}
