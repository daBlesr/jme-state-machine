package examples.sinbad.Base;

import JmeStateMachine.StateChange;
import JmeStateMachine.anim.DelayedConsumer;
import JmeStateMachine.anim.OnFinishedEventAction;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.SkinningControl;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import static com.jme3.bullet.PhysicsSpace.getPhysicsSpace;

public class PickUpSwordState extends SinbadBaseState {

    private final Spatial sword;
    private DelayedConsumer drawSwordsDone;

    public PickUpSwordState(Spatial sword) {
        this.sword = sword;
    }

    @Override
    protected void onEnter() {
        super.onEnter();

        AnimComposer animComposer = getSpatial().getControl(AnimComposer.class);

        OnFinishedEventAction onFinishJumpLoopAction = new OnFinishedEventAction(animComposer.getAnimClip("DrawSwords"));
        onFinishJumpLoopAction.setSpeed(0.2f);
        drawSwordsDone = new DelayedConsumer(onFinishJumpLoopAction);

        animComposer.addAction("DrawSwordsAction", onFinishJumpLoopAction);
        animComposer.setCurrentAction("DrawSwordsAction", "top");
        animComposer.setCurrentAction("IdleBase", "bot");

        physicsRigidBody.setLinearVelocity(Vector3f.ZERO);
        getPhysicsSpace().remove(sword);

        SkinningControl skinningControl = getSpatial().getControl(SkinningControl.class);
        Node attachmentNode = skinningControl.getAttachmentsNode("Hand.L");
        attachmentNode.attachChild(sword);
        sword.setLocalTranslation(new Vector3f(0.8f, 1f, -0.3f));
        sword.scale(2.4f);
        sword.rotate(0, FastMath.HALF_PI, FastMath.PI);
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
        if (drawSwordsDone.getAsBoolean()) {
            return StateChange.pop();
        }
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
