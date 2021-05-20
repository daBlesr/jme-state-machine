package JmeStateMachine;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.scene.Spatial;

public abstract class State implements PhysicsTickListener {

    protected Layer layer;
    protected Spatial spatial;

    protected void setLayer (Layer layer) {
        this.layer = layer;
    }

    abstract protected void onEnter ();

    abstract protected StateChange handleActionInput (String input, boolean isPressed, float tpf);
    abstract protected StateChange handleAnalogInput (String input, float value, float tpf);

    public abstract StateChange controlUpdate(float tpf);

    abstract protected void onExit ();

    public Spatial getSpatial() { return spatial; }

    protected void setSpatial (Spatial spatial) {
        this.spatial = spatial;
    }
}
