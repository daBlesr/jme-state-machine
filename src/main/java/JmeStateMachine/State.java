package JmeStateMachine;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.scene.Spatial;

public abstract class State {

    private Layer layer;
    protected Spatial spatial;
    protected boolean popState = false;

    protected void setLayer (Layer layer) {
        this.layer = layer;
    }

    abstract protected void onEnter ();

    abstract protected State handleActionInput (String input, boolean isPressed, float tpf);
    abstract protected State handleAnalogInput (String input, float value, float tpf);

    abstract protected State prePhysicsTick(PhysicsSpace space, float timeStep);
    abstract protected void physicsTick(PhysicsSpace space, float timeStep);

    public abstract void controlUpdate(float tpf);

    protected Void toPreviousState () {
        popState = true;
        return null;
    }

    abstract protected void onExit ();

    public Spatial getSpatial() { return spatial; }

    protected void setSpatial (Spatial spatial) {
        this.spatial = spatial;
    }
}
