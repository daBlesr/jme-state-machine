package JmeStateMachine;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

import java.util.LinkedList;
import java.util.List;

public class ModelStateMachine extends AbstractControl {

    private List<Layer> layers = new LinkedList<>();

    public void addLayer (Layer layer) {
        this.layers.add(layer);
        layer.setModelStateMachine(this);
    }

    public State getCurrentState (Layer layer) {
        return layer.getCurrentState();
    }

    @Override
    protected void controlUpdate(float tpf) {
        this.layers.forEach(layer -> layer.controlUpdate(tpf));
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) { }

    public List<Layer> getLayers() {
        return layers;
    }
}
