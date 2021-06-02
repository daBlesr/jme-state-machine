package examples.sinbad.Base;

import JmeStateMachine.StateChange;
import com.jme3.anim.AnimComposer;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

import java.util.List;

import static com.jme3.bullet.PhysicsSpace.getPhysicsSpace;

public class RunningState extends SinbadBaseState {

    private Vector3f velocity = new Vector3f();

    @Override
    protected void onEnter() {
        super.onEnter();
        AnimComposer animComposer = getSpatial().getControl(AnimComposer.class);

        animComposer.setCurrentAction("RunBase", "bot");
        animComposer.setCurrentAction("RunTop", "top");

        getPhysicsSpace().addTickListener(this);
    }

    @Override
    protected StateChange handleActionInput(String input, boolean isPressed, float tpf) {
        if (input.equals("Walk Forward") && !isPressed) {
            return StateChange.to(new IdleState());
        } else if (input.equals("Jump")) {
            return StateChange.to(new JumpState());
        }
        return null;
    }

    @Override
    protected StateChange handleAnalogInput(String input, float value, float tpf) {
        if (input.equals("Rotate Left")) {
            rotateLeft();
        } else if (input.equals("Rotate Right")) {
            rotateRight();
        }
        return null;
    }

    @Override
    public void prePhysicsTick(PhysicsSpace space, float timeStep) {
        super.prePhysicsTick(space, timeStep);

        // from better character control

        Vector3f currentVelocity = velocity.clone();

        float existingLeftVelocity = velocity.dot(characterRotation.mult(Vector3f.UNIT_X));
        float existingForwardVelocity = velocity.dot(characterRotation.mult(Vector3f.UNIT_Z));

        Vector3f counter = new Vector3f();
        existingLeftVelocity *= 0.9f;
        existingForwardVelocity *= 0.9f;
        counter.set(-existingLeftVelocity, 0, -existingForwardVelocity);
        characterRotation.multLocal(counter);
        velocity.addLocal(counter);

        Vector3f walkDirection = characterRotation.mult(Vector3f.UNIT_Z).mult(6);

        float designatedVelocity = walkDirection.length();

        Vector3f localWalkDirection = new Vector3f();
        //normalize walkdirection
        localWalkDirection.set(walkDirection).normalizeLocal();
        //check for the existing velocity in the desired direction
        float existingVelocity = velocity.dot(localWalkDirection);
        //calculate the final velocity in the desired direction
        float finalVelocity = designatedVelocity - existingVelocity;
        localWalkDirection.multLocal(finalVelocity);
        //add resulting vector to existing velocity
        velocity.addLocal(localWalkDirection);

        if (currentVelocity.distance(velocity) > FastMath.ZERO_TOLERANCE) {
            physicsRigidBody.setLinearVelocity(velocity);
        }
    }

    @Override
    public void physicsTick(PhysicsSpace space, float timeStep) {
        super.physicsTick(space, timeStep);
        physicsRigidBody.getLinearVelocity(velocity);
    }

    @Override
    public StateChange controlUpdate(float tpf, List<StateChange> stateChanges) {
        StateChange possibleStateChange = super.controlUpdate(tpf, stateChanges);
        if (possibleStateChange != null) {
            return possibleStateChange;
        }

        if (stateChanges.size() == 1) {
            return stateChanges.get(0);
        }
        // if multiple state changes were returned during this tick,
        // we prioritize jump.
        return prioritizeClass(JumpState.class, IdleState.class).orElse(null);
    }

    @Override
    protected void onExit() {
        getPhysicsSpace().removeTickListener(this);
    }
}
