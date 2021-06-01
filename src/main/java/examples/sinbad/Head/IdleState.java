package examples.sinbad.Head;

import JmeStateMachine.State;
import JmeStateMachine.StateChange;
import com.jme3.anim.AnimComposer;

import java.util.List;

public class IdleState extends State {
    @Override
    protected void onEnter() {
        AnimComposer animComposer = getSpatial().getControl(AnimComposer.class);
        animComposer.setCurrentAction("IdleTop", "neck");
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
    public StateChange controlUpdate(float tpf, List<StateChange> stateChanges) {
        return null;
    }

    @Override
    protected void onExit() {

    }
}
