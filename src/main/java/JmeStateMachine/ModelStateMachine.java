package JmeStateMachine;

import com.jme3.anim.AnimComposer;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

import java.util.*;

import static com.jme3.bullet.PhysicsSpace.getPhysicsSpace;

public class ModelStateMachine extends AbstractControl implements ActionListener, AnalogListener, PhysicsTickListener {

    private List<Layer> layers = new LinkedList<>();

    public ModelStateMachine() {
        getPhysicsSpace().addTickListener(this);
    }

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
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        this.layers.forEach(layer -> layer.onAction(name, isPressed, tpf));
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        this.layers.forEach(layer -> layer.onAnalog(name, value, tpf));
    }

    @Override
    public void prePhysicsTick(PhysicsSpace space, float timeStep) {
        this.layers.forEach(layer -> layer.prePhysicsTick(space, timeStep));
    }

    @Override
    public void physicsTick(PhysicsSpace space, float timeStep) {
        this.layers.forEach(layer -> layer.physicsTick(space, timeStep));
    }

    private AnimComposer getAnimComposer () {
        return getSpatial().getControl(AnimComposer.class);
    }
}
