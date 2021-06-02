package JmeStateMachine;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Layer implements ActionListener, AnalogListener {

    private ModelStateMachine modelStateMachine;
    private final LinkedList<State> states = new LinkedList<>();
    private final LinkedList<StateChange> stateChangesQueue = new LinkedList<>();

    public void setInitialState (State state) {
        enterState(state);
    }

    protected void setModelStateMachine (ModelStateMachine modelStateMachine) {
        this.modelStateMachine = modelStateMachine;
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        StateChange stateChange = getCurrentState().handleActionInput(name, isPressed, tpf);
        if (stateChange != null) {
            stateChangesQueue.add(stateChange);
        }
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        StateChange stateChange = getCurrentState().handleAnalogInput(name, value, tpf);
        if (stateChange != null) {
            stateChangesQueue.add(stateChange);
        }
    }

    protected void controlUpdate (float tpf) {
        StateChange stateChange = getCurrentState().controlUpdate(tpf, stateChangesQueue);
        if (stateChange != null) {
            changeState(stateChange);
        }

        stateChangesQueue.clear();
    }

    private void changeState(StateChange stateChange) {
        State currentState = getCurrentState();

        if (stateChange instanceof StateChange.PoppedStateChange) {
            State poppedState = states.removeFirst();
            poppedState.onExit();

            if (states.size() == 0) {
                throw new IllegalStateException("popped last state. No state left.");
            }

            enterState(getCurrentState());

        } else if (stateChange instanceof StateChange.ToStateChange) {
            currentState.onExit();

            State newState = ((StateChange.ToStateChange) stateChange).getState();
            enterState(newState);

        } else if (stateChange instanceof StateChange.PushedStateChange) {
            getCurrentState().onExit();

            State pushedState = ((StateChange.PushedStateChange) stateChange).getState();
            enterState(pushedState);
        }
    }

    private void enterState (State s) {
        s.setSpatial(modelStateMachine.getSpatial());
        s.setLayer(this);
        s.onEnter();
        states.addFirst(s);
    }

    public State getCurrentState() {
        return states.getFirst();
    }

    public boolean hasState(Class<?> stateClass) {
        return states.stream().anyMatch(s -> s.getClass().equals(stateClass));
    }

    public Optional<StateChange> prioritizeClass(Class<?> ...stateClasses) {
        for (Class<?> stateClass : stateClasses) {
            Optional<StateChange> s = stateChangesQueue
                .stream()
                .filter(stateChange -> stateChange.hasNextStateOfClass(stateClass))
                .findFirst();
            if (s.isPresent()) {
                return s;
            }
        }
        return Optional.empty();
    }

    public boolean contains(Class<? extends State> stateClass) {
        return stateChangesQueue
            .stream()
            .anyMatch(stateChange -> stateChange.hasNextStateOfClass(stateClass));
    }

    public List<Layer> getLayers() {
        return modelStateMachine.getLayers();
    }
}
