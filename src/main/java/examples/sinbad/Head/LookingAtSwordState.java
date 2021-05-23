package examples.sinbad.Head;

import JmeStateMachine.State;
import JmeStateMachine.StateChange;
import com.jme3.anim.Armature;
import com.jme3.anim.Joint;
import com.jme3.anim.SkinningControl;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import examples.SinbadExample.TestSinbadStateMachine;
import examples.sinbad.Base.PickUpSwordState;

public class LookingAtSwordState extends State {

    Armature armature;
    private SkinningControl skinningControl;
    private Joint neck;
    private Transform transform;

    private static float maxYrotationRad = 90f / 180f * FastMath.PI;

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

        if (getLayer().getLayers().stream().anyMatch(l -> l.hasState(PickUpSwordState.class))) {
            return StateChange.to(new IdleState());
        }

        Vector3f swordLocationWorld = TestSinbadStateMachine.sword.getWorldTranslation();

        Vector3f worldDirectionVector = new Vector3f()
            .set(getSpatial().getWorldTranslation().add(0, 2, 0))
            .subtract(swordLocationWorld)
            .normalize();

        Vector3f directionInLocalSpace = new Vector3f();

        transform
            .clone()
            .combineWithParent(getSpatial().getWorldTransform())
            .getRotation()
            .inverse()
            .mult(worldDirectionVector, directionInLocalSpace);

        Quaternion swordLookAtRotation = new Quaternion()
            .lookAt(
                directionInLocalSpace,
                Vector3f.UNIT_Y
            );

        Quaternion clampedRotation = clampHeadRotation(swordLookAtRotation);

        neck.getLocalRotation().slerp(clampedRotation, tpf * 2f);

        return null;
    }

    @Override
    protected void onExit() {
        neck.setLocalRotation(new Quaternion());
    }

    @Override
    public void prePhysicsTick(PhysicsSpace space, float timeStep) {
    }

    @Override
    public void physicsTick(PhysicsSpace space, float timeStep) {

    }

    private Quaternion clampHeadRotation(Quaternion rotation) {
        float[] angles = new float[3];
        rotation.toAngles(angles);

        angles[1] = FastMath.clamp(angles[1], -maxYrotationRad, maxYrotationRad);
        return new Quaternion().fromAngles(angles);
    }
}
