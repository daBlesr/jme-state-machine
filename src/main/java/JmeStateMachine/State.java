package JmeStateMachine;

import com.jme3.scene.Spatial;

import java.util.List;
import java.util.Optional;

public abstract class State {

    protected Layer layer;
    protected Spatial spatial;

    abstract protected void onEnter ();

    abstract protected StateChange handleActionInput (String input, boolean isPressed, float tpf);
    abstract protected StateChange handleAnalogInput (String input, float value, float tpf);

    public abstract StateChange controlUpdate(float tpf, List<StateChange> stateChanges);

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

    protected Optional<StateChange> prioritizeClass(Class<?> ...stateClasses) {
        return layer.prioritizeClass(stateClasses);
    }

    protected boolean contains(Class<? extends State> stateClass) {
        return layer.contains(stateClass);
    }
}
