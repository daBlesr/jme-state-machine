package examples.sinbad.Head;

import JmeStateMachine.State;
import JmeStateMachine.StateChange;
import com.jme3.anim.*;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import examples.SinbadExample.TestSinbadStateMachine;

public class LookingAtSwordState extends State {

    Armature armature;
    private SkinningControl skinningControl;

    @Override
    protected void onEnter() {
        skinningControl = getSpatial().getControl(SkinningControl.class);
        armature = skinningControl.getArmature();

        AnimComposer animComposer = getSpatial().getControl(AnimComposer.class);

        animComposer.makeLayer("head", ArmatureMask.createMask(
            getSpatial().getControl(SkinningControl.class).getArmature(),
            "Head"
        ));

        animComposer.setCurrentAction("IdleTop", "head");

//        Joint head = armature.getJoint("Head");
//        skinningControl.getAttachmentsNode()

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
        Transform transform = armature.getJoint("Head")
            .getModelTransform()
            .clone()
            .combineWithParent(skinningControl.getSpatial().getWorldTransform());

        Quaternion worldRotation = new Quaternion()
            .lookAt(
                TestSinbadStateMachine.sword.getWorldTranslation(),
                Vector3f.UNIT_Y
            );
        Quaternion localRotation = transform.getRotation().inverse().mult(worldRotation);


        armature.getJoint("Head")
            .setLocalRotation(
            localRotation
        );
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
