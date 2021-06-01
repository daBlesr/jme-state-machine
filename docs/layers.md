## Layers

Layers are used when different states need to be defined for different parts of the model, for instance equipment versus torso behavior. 
In the example in this project, a layer is created for the "look at sword behavior". 
It uses states from the other layer to know when the sword is attach to the model using:

```java
if (getLayer()
       .getLayers()
       .stream()
       .anyMatch(l -> l.hasState(PickUpSwordState.class))
) {
    return StateChange.to(new IdleState());
}
```  
