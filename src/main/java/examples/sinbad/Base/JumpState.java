package examples.sinbad.Base;

import JmeStateMachine.State;
import JmeStateMachine.StateChange;
import JmeStateMachine.anim.DelayedConsumer;
import JmeStateMachine.anim.OnFinishedEventAction;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.tween.action.BlendAction;
import com.jme3.anim.tween.action.ClipAction;
import com.jme3.anim.tween.action.LinearBlendSpace;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;

import static com.jme3.bullet.PhysicsSpace.getPhysicsSpace;

public class JumpState extends State {
    private DelayedConsumer delayedConsumer;
    private boolean hasJumped = false;

    @Override
    protected void onEnter() {
        AnimComposer animComposer = getSpatial().getControl(AnimComposer.class);

        OnFinishedEventAction onFinishJumpLoopAction = new OnFinishedEventAction(animComposer.getAnimClip("JumpLoop"));

        BlendAction blendedStartToLoop = new BlendAction(
            new LinearBlendSpace(0, 1f),
            new ClipAction(animComposer.getAnimClip("JumpStart")),
            onFinishJumpLoopAction
        );
        blendedStartToLoop.setSpeed(3f);
        delayedConsumer = new DelayedConsumer(onFinishJumpLoopAction);
        animComposer.addAction("EnterJump", blendedStartToLoop);
        animComposer.setCurrentAction("EnterJump");

        getPhysicsSpace().addTickListener(this);
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
    public void prePhysicsTick(PhysicsSpace space, float timeStep) {
        if (!hasJumped) {
            RigidBodyControl rbc = getSpatial().getControl(RigidBodyControl.class);
            rbc.applyCentralImpulse(
                rbc.getPhysicsRotation()
                    .mult(new Vector3f(0, rbc.getMass() * 8f, 0))
            );
            hasJumped = true;
        }
    }

    @Override
    public void physicsTick(PhysicsSpace space, float timeStep) {

    }

    @Override
    public StateChange controlUpdate(float tpf) {
        if (delayedConsumer.getAsBoolean()) {
            return StateChange.to(new MidAirState());
        }
        return null;
    }

    @Override
    protected void onExit() {
        getPhysicsSpace().removeTickListener(this);
    }
}
