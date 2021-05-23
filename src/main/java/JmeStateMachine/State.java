package JmeStateMachine;

import com.jme3.bullet.PhysicsTickListener;
import com.jme3.scene.Spatial;

public abstract class State {

    protected Layer layer;
    protected Spatial spatial;

    abstract protected void onEnter ();

    abstract protected StateChange handleActionInput (String input, boolean isPressed, float tpf);
    abstract protected StateChange handleAnalogInput (String input, float value, float tpf);

    public abstract StateChange controlUpdate(float tpf);

    abstract protected void onExit ();

    public Layer getLayer() {
        return layer;
    }

    protected void setLayer (Layer layer) {
        this.layer = layer;
    }

    public Spatial getSpatial() { return spatial; }

    protected void setSpatial (Spatial spatial) {
        this.spatial = spatial;
    }

}
