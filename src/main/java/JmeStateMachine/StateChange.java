package JmeStateMachine;

public abstract class StateChange {

    public static StateChange push (State s) {
        return new PushedStateChange(s);
    }

    static class PushedStateChange extends StateChange {
        private State state;

        public PushedStateChange(State s) {
            state = s;
        }

        public State getState() {
            return state;
        }
    }

    public static StateChange pop () {
        return new PoppedStateChange();
    }

    static class PoppedStateChange extends StateChange {

    }

    public static StateChange to (State s) {
        return new ToStateChange(s);
    }

    static class ToStateChange extends StateChange {
        private State state;

        public ToStateChange (State s) {
            state = s;
        }

        public State getState() {
            return state;
        }
    }

    public boolean hasNextStateOfClass (Class<? extends State> stateClass) {
        if (this instanceof StateChange.ToStateChange) {
            State state = ((StateChange.ToStateChange) this).getState();
            return state.getClass().equals(stateClass);
        } else if (this instanceof StateChange.PushedStateChange) {
            State state = ((StateChange.PushedStateChange) this).getState();
            return state.getClass().equals(stateClass);
        }
        return false;
    }
}
