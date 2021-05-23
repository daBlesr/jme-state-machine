package examples.sinbad.Base;

import JmeStateMachine.State;
import JmeStateMachine.StateChange;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsRayTestResult;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import java.util.List;

import static com.jme3.bullet.PhysicsSpace.getPhysicsSpace;

public abstract class SinbadBaseState extends State implements PhysicsCollisionListener, PhysicsTickListener {

    private boolean onGround = false;
    private Vector3f location = new Vector3f();
    private Vector3f ray = new Vector3f();
    protected Quaternion characterRotation = new Quaternion();
    private Quaternion physicsRotation = new Quaternion();
    protected PhysicsRigidBody physicsRigidBody;
    protected Spatial collisionWithSword;

    @Override
    protected void onEnter() {
        physicsRigidBody = getSpatial().getControl(RigidBodyControl.class);
        characterRotation.set(physicsRigidBody.getPhysicsRotation());
        physicsRotation.set(characterRotation);

        getPhysicsSpace().addCollisionListener(this);
    }

    @Override
    public void prePhysicsTick(PhysicsSpace space, float timeStep) {
        checkOnGround();

        if (!characterRotation.isSimilar(physicsRotation, 0.001f)) {
            physicsRigidBody.setPhysicsRotation(characterRotation);
        }
    }

    @Override
    public void physicsTick(PhysicsSpace space, float timeStep) {
        physicsRotation.set(characterRotation);
    }

    @Override
    public StateChange controlUpdate(float tpf) {
        if (collisionWithSword != null) {
            Spatial sword = collisionWithSword;
            collisionWithSword = null;
            if (!layer.hasState(PickUpSwordState.class)) {
                return StateChange.push(new PickUpSwordState(sword));
            }
        }
        return null;
    }

    private void checkOnGround () {
        location
            .set(physicsRigidBody.getPhysicsLocation());

        ray
            .set(location)
            .addLocal(new Vector3f(0, -2.6f, 0));

        List<PhysicsRayTestResult> results = getPhysicsSpace().rayTestRaw(location, ray);


        for (PhysicsRayTestResult physicsRayTestResult : results) {
            if (!physicsRayTestResult.getCollisionObject().equals(physicsRigidBody)) {
                onGround = true;
                return;
            }
        }
        onGround = false;
    }

    protected boolean onGround () {
        return onGround;
    }

    protected void rotateRight () { rotate(-0.02f); }
    protected void rotateLeft () { rotate(0.02f);}

    protected void rotate (float angle) {
        characterRotation.multLocal(new Quaternion().fromAngleAxis(angle, Vector3f.UNIT_Y));
    }

    @Override
    public void collision(PhysicsCollisionEvent event) {
        if (event.getObjectA() == physicsRigidBody || event.getObjectB() == physicsRigidBody) {
            if (event.getNodeA() != null && "Sword-ogremesh".equals(event.getNodeA().getName())) {
                collisionWithSword = event.getNodeA();
            } else if (event.getNodeB() != null && "Sword-ogremesh".equals(event.getNodeB().getName())) {
                collisionWithSword = event.getNodeB();
            }
        }
    }
}
