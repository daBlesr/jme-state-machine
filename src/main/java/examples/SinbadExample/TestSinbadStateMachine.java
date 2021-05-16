package examples.SinbadExample;

import JmeStateMachine.Layer;
import JmeStateMachine.ModelStateMachine;
import com.jme3.anim.SkinningControl;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import examples.sinbad.Base.IdleState;

import static com.jme3.bullet.PhysicsSpace.getPhysicsSpace;

public class TestSinbadStateMachine extends SimpleApplication {

    public static void main(String[] args) {
        TestSinbadStateMachine app = new TestSinbadStateMachine();
        AppSettings settings = new AppSettings(true);
        settings.setFullscreen(false);
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setGammaCorrection(true);
        settings.setVSync(true);
        app.setShowSettings(false);
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setDragToRotate(true);
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(0, -0.5f, 0).normalizeLocal());
        dl.setColor(new ColorRGBA(0.9f, 0.9f, 0.9f, 1f).mult(0.5f));
        rootNode.addLight(dl);

        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(0.1f));
        rootNode.addLight(al);

        viewPort.setBackgroundColor(ColorRGBA.LightGray);

        cam.setLocation(new Vector3f(-6, 3, 0));

        BulletAppState bulletAppState = new BulletAppState();
        bulletAppState.setDebugEnabled(true);
        stateManager.attach(bulletAppState);

        Box box = new Box(100, 0.1f, 100);
        Geometry boxGeom = new Geometry("box", box);
        boxGeom.move(0, -0.4f, 0);
        Material boxMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        boxMat.setColor("Diffuse", ColorRGBA.DarkGray);
        boxGeom.setMaterial(boxMat);
        boxGeom.addControl(new RigidBodyControl(CollisionShapeFactory.createBoxShape(boxGeom), 0f));
        rootNode.attachChild(boxGeom);
        bulletAppState.getPhysicsSpace().add(boxGeom);


        Node n = new Node();
        
        Node sinbad = (Node) assetManager.loadModel("Models/Sinbad/Sinbad.j3o");
        sinbad.getControl(SkinningControl.class).getArmature().applyBindPose();
        sinbad.scale(0.5f);
        sinbad.move(0, 2, 0);

        setupKeys();
        createSinbadStateMachine(sinbad);

        cam.lookAt(sinbad.getWorldTranslation(), Vector3f.UNIT_Y);

        rootNode.attachChild(sinbad);
    }

    private void setupKeys() {
        inputManager.addMapping("Strafe Left",
            new KeyTrigger(KeyInput.KEY_U),
            new KeyTrigger(KeyInput.KEY_Z));
        inputManager.addMapping("Strafe Right",
            new KeyTrigger(KeyInput.KEY_O),
            new KeyTrigger(KeyInput.KEY_X));
        inputManager.addMapping("Rotate Left",
            new KeyTrigger(KeyInput.KEY_J),
            new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("Rotate Right",
            new KeyTrigger(KeyInput.KEY_L),
            new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("Walk Forward",
            new KeyTrigger(KeyInput.KEY_I),
            new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("Walk Backward",
            new KeyTrigger(KeyInput.KEY_K),
            new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("Jump",
            new KeyTrigger(KeyInput.KEY_F),
            new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Duck",
            new KeyTrigger(KeyInput.KEY_G),
            new KeyTrigger(KeyInput.KEY_LSHIFT),
            new KeyTrigger(KeyInput.KEY_RSHIFT));
    }

    private void createSinbadStateMachine (Node sinbad) {
        CapsuleCollisionShape capsuleCollisionShape = new CapsuleCollisionShape(1.2f, 2.5f);
        RigidBodyControl rbc = new RigidBodyControl(capsuleCollisionShape, 8.0f);
        sinbad.addControl(rbc);

        getPhysicsSpace().add(rbc);

        ModelStateMachine modelStateMachine = new ModelStateMachine();

        // first we add the control to Sinbad
        sinbad.addControl(modelStateMachine);

        Layer modelBaseLayer = new Layer();
        modelStateMachine.addLayer(modelBaseLayer);
        modelBaseLayer.setInitialState(new IdleState());
//        modelStateMachine.addLayer(modelTopLayer);

        inputManager.addListener(modelStateMachine, "Strafe Left", "Strafe Right");
        inputManager.addListener(modelStateMachine, "Rotate Left", "Rotate Right");
        inputManager.addListener(modelStateMachine, "Walk Forward", "Walk Backward");
        inputManager.addListener(modelStateMachine, "Jump", "Duck");
    }
}
