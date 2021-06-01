## How to create a Model State Machine

One creates a state machine in jMonkeyEngine using the following:
```
ModelStateMachine modelStateMachine = new ModelStateMachine();
spatial.addControl(modelStateMachine);
```        
To add a layer, one needs to set the default state.
```
Layer modelBaseLayer = new Layer();
modelStateMachine.addLayer(modelBaseLayer);
modelBaseLayer.setInitialState(new IdleState());
```

How to create a state like the IdleState, is explained in the chapter about [State](https://github.com/daBlesr/jme-state-machine/blob/main/docs/state.md).
More information on Layers is explained on the [Layers docs](https://github.com/daBlesr/jme-state-machine/blob/main/docs/layers.md)
