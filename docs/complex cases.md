## Complex cases

In some cases the player of the game changes two keys at once in a certain state.
Consider we have the following code, and the gamer pressed both W (walk forward) as well as Spacebar (jump):

```java
@Override
protected StateChange handleActionInput(String input, boolean isPressed, float tpf) {
    if (input.equals("Walk Forward")) {
        return StateChange.to(new RunningState());
    } else if (input.equals("Jump")) {
        return StateChange.to(new JumpState());
    }
    return null;
}
```
What should now happen?

The answer is simple. Since in this game tick two State Changes have occured, we need to simply choose what to do in `controlUpdate`.
We could do for instance:

```java
if (stateChanges.size() == 1) {
    return stateChanges.get(0);
}
// if multiple state changes were returned during this tick,
// we prioritize jump.
return toStateClass(JumpState.class).orElse(null);
```
or we could create our own more complex logic:
```java
if (contains(JumpState.class) && contains(RunningState.class)) {
    return StateChange.to(new RunningJumpState());
}
```

`toStateClass` and `contains` are available helper functions to simplify logic of finding and choosing statesChanges.
