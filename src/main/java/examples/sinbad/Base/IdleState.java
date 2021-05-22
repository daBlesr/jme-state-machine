package examples.sinbad.Base;

import JmeStateMachine.State;
import JmeStateMachine.StateChange;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.ArmatureMask;
import com.jme3.anim.SkinningControl;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import static com.jme3.bullet.PhysicsSpace.getPhysicsSpace;

public class IdleState extends SinbadBaseState {

    Vector3f velocity = new Vector3f();

    @Override
    protected void onEnter() {
        super.onEnter();
        AnimComposer animComposer = getSpatial().getControl(AnimComposer.class);
        animComposer.setCurrentAction("IdleBase");

        animComposer.makeLayer(
            "top",
            ArmatureMask.createMask(
                getSpatial().getControl(SkinningControl.class).getArmature(),
                "Chest"
            )
        );

        animComposer.setCurrentAction("IdleTop", "top");
        getPhysicsSpace().addTickListener(this);
    }

    @Override
    protected StateChange handleActionInput(String input, boolean isPressed, float tpf) {

        if (input.equals("Walk Forward")) {
            return StateChange.to(new RunningState());
        } else if (input.equals("Jump")) {
            return StateChange.to(new JumpState());
        }
        return null;
    }

    @Override
    protected StateChange handleAnalogInput(String input, float value, float tpf) {
        if (input.equals("Walk Forward")) {
            return StateChange.to(new RunningState());
        } else if (input.equals("Rotate Left")) {
            rotateLeft();
        } else if (input.equals("Rotate Right")) {
            rotateRight();
        }
        return null;
    }

    @Override
    public void prePhysicsTick(PhysicsSpace space, float timeStep) {
        super.prePhysicsTick(space, timeStep);
        if (velocity.length() > FastMath.ZERO_TOLERANCE) {
            rbc.setLinearVelocity(Vector3f.ZERO);
        }
    }

    @Override
    public void physicsTick(PhysicsSpace space, float timeStep) {
        super.physicsTick(space, timeStep);
        rbc.getLinearVelocity(velocity);
    }

    @Override
    public StateChange controlUpdate(float tpf) {
        return null;
    }

    @Override
    protected void onExit() {
        AnimComposer animComposer = getSpatial().getControl(AnimComposer.class);
        animComposer.removeLayer("top");
        getPhysicsSpace().removeTickListener(this);
    }
}
