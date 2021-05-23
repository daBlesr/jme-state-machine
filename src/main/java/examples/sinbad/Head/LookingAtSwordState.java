package examples.sinbad.Head;

import JmeStateMachine.State;
import JmeStateMachine.StateChange;
import com.jme3.anim.Armature;
import com.jme3.anim.Joint;
import com.jme3.anim.SkinningControl;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import examples.SinbadExample.TestSinbadStateMachine;

public class LookingAtSwordState extends State {

    Armature armature;
    private SkinningControl skinningControl;
    private Joint neck;
    private Transform transform;

    @Override
    protected void onEnter() {
        skinningControl = getSpatial().getControl(SkinningControl.class);
        armature = skinningControl.getArmature();
        neck = armature.getJoint("Neck");

        transform = neck.getModelTransform().clone();
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
        Vector3f swordLocationWorld = TestSinbadStateMachine.sword.getWorldTranslation();

        Vector3f worldDirectionVector = new Vector3f()
            .set(getSpatial().getWorldTranslation().add(0, 2, 0))
            .subtract(swordLocationWorld)
            .normalize();

        Vector3f directionInLocalSpace = new Vector3f();
        System.out.println(getSpatial().getWorldTransform());
        transform.clone().combineWithParent(getSpatial().getWorldTransform())
            .getRotation()
            .inverse()
            .mult(worldDirectionVector, directionInLocalSpace);

        Quaternion swordLookAtRotation = new Quaternion()
            .lookAt(
                directionInLocalSpace,
                Vector3f.UNIT_Y
            );
//        swordLookAtRotation.multLocal(new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Z));

        neck.setLocalRotation(swordLookAtRotation);

        return null;
    }

    @Override
    protected void onExit() {

    }

    @Override
    public void prePhysicsTick(PhysicsSpace space, float timeStep) {
//        PhysicsLink neck = dac.findLink("Bone:Neck");
//        neck.getRigidBody().setPhysicsRotation(new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Y));
    }

    @Override
    public void physicsTick(PhysicsSpace space, float timeStep) {

    }
}
