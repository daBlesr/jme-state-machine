## State Change

The idea is to go to from a State to a new State by one of the following methods:
* `handleActionInput`
* `handleAnalogInput`
* `controlUpdate`

There are multiple ways to change state. return on one of the above methods a StateChange instance by using either
* `StateChange.to(new SomeOtherState())`
* `StateChange.push(new TemporaryState())`
* `StateChange.pop()`

The state machine is a pushdown automaton, meaning it is possible to push a state temporarily on the stack (and afterwards pop it from that state). 
In the video this happens, when Sinbad runs into the sword, it picks it up, and then returns back to the state it was doing.

An example on how to change to a new state:

```
@Override
protected StateChange handleActionInput(String input, boolean isPressed, float tpf) {
    if (input.equals("Walk Forward") && !isPressed) {
        return StateChange.to(new IdleState());
    } else if (input.equals("Jump")) {
        return StateChange.to(new JumpState());
    }
    return null;
}
```
returning `null` means the current state is remained.
