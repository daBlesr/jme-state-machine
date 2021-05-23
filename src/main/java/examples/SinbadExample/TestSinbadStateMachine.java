package examples.SinbadExample;

import JmeStateMachine.Layer;
import JmeStateMachine.ModelStateMachine;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.Armature;
import com.jme3.anim.ArmatureMask;
import com.jme3.anim.SkinningControl;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
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

    public static Node sword;

    public static AssetManager ASSETMANAGER;
    public static Node ROOTNODE;

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
        ROOTNODE = rootNode;
        ASSETMANAGER = assetManager;

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
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        stateManager.attach(bulletAppState);

        Box box = new Box(40, 0.1f, 40);
        Geometry boxGeom = new Geometry("box", box);
        boxGeom.move(0, -0.4f, 0);
        Material boxMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");

        boxGeom.setMaterial(boxMat);
        boxGeom.addControl(new RigidBodyControl(CollisionShapeFactory.createBoxShape(boxGeom), 0f));
        rootNode.attachChild(boxGeom);
        bulletAppState.getPhysicsSpace().add(boxGeom);

        for (int i = 0; i < 10; i++) {
            Box boxSmall = new Box(1, 1f, 1);
            Geometry smallBoxGeom = new Geometry("box", boxSmall);
            smallBoxGeom.move(2*i + 3, 0, 0);
            Material smallBoxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            smallBoxGeom.setMaterial(smallBoxMat);
            smallBoxMat.setColor("Color", ColorRGBA.Blue);
            RigidBodyControl rbc = new RigidBodyControl(CollisionShapeFactory.createBoxShape(smallBoxGeom), 0f);
            rbc.setLinearVelocity(new Vector3f(5, 0, 0));
            smallBoxGeom.addControl(rbc);
            rootNode.attachChild(smallBoxGeom);
            bulletAppState.getPhysicsSpace().add(smallBoxGeom);
        }

        Node sinbad = (Node) assetManager.loadModel("Models/Sinbad/Sinbad.j3o");
        sinbad.getControl(SkinningControl.class).getArmature().applyBindPose();
        sinbad.scale(0.5f);
        sinbad.move(0, 5, 0);

        setupKeys();
        createSinbadStateMachine(sinbad);

        cam.lookAt(sinbad.getWorldTranslation(), Vector3f.UNIT_Y);
        rootNode.attachChild(sinbad);

        addSword();
    }

    private void setupKeys() {
        inputManager.addMapping("Rotate Left",
            new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("Rotate Right",
            new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("Walk Forward",
            new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("Jump",
            new KeyTrigger(KeyInput.KEY_SPACE));
    }

    private void createSinbadStateMachine (Node sinbad) {

        Armature armature = sinbad.getControl(SkinningControl.class).getArmature();
        AnimComposer animComposer = sinbad.getControl(AnimComposer.class);

        animComposer.makeLayer("bot", getLowerArmature(armature));
        animComposer.makeLayer("top", getChestArmature(armature));
        animComposer.makeLayer("neck", getNeckArmature(armature));

        CapsuleCollisionShape capsuleCollisionShape = new CapsuleCollisionShape(1.2f, 2.5f);
        RigidBodyControl rbc = new RigidBodyControl(capsuleCollisionShape, 20.0f);
        rbc.setEnableSleep(false);

        sinbad.addControl(rbc);

        ModelStateMachine modelStateMachine = new ModelStateMachine();
        sinbad.addControl(modelStateMachine);

        Layer modelBaseLayer = new Layer();
        modelStateMachine.addLayer(modelBaseLayer);
        modelBaseLayer.setInitialState(new IdleState());

        inputManager.addListener(modelBaseLayer, "Rotate Left", "Rotate Right");
        inputManager.addListener(modelBaseLayer, "Walk Forward", "Walk Backward");
        inputManager.addListener(modelBaseLayer, "Jump");

        Layer headLayer = new Layer();
        modelStateMachine.addLayer(headLayer);
        headLayer.setInitialState(new examples.sinbad.Head.LookingAtSwordState());

        getPhysicsSpace().add(sinbad);

        rbc.setAngularFactor(0);
        rbc.setFriction(0);
        rbc.setAngularDamping(0);

        ChaseCamera chaseCam = new ChaseCamera(cam, sinbad, inputManager);
        chaseCam.setEnabled(true);
        chaseCam.setInvertHorizontalAxis(true);
        chaseCam.setSmoothMotion(false);
        chaseCam.setTrailingEnabled(false);
        chaseCam.setLookAtOffset(new Vector3f(0, 0, 0));
        chaseCam.setDefaultDistance(15);
        chaseCam.setDefaultHorizontalRotation(0);
        chaseCam.setToggleRotationTrigger(new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE),new KeyTrigger(KeyInput.KEY_L));
        chaseCam.setZoomSensitivity(0);
        chaseCam.setMaxDistance(15);
        chaseCam.setMinDistance(15);
    }

    private void addSword() {
        sword = (Node) assetManager.loadModel("Models/Sinbad/Sword.j3o");
        sword.scale(0.5f);

        rootNode.attachChild(sword);
        sword.move(4, 0.2f, 4);

        GhostControl ghostControl = new GhostControl(CollisionShapeFactory.createBoxShape(sword));
        sword.addControl(ghostControl);

        getPhysicsSpace().add(sword);

    }

    public static ArmatureMask getNeckArmature (Armature armature) {
        ArmatureMask mask = new ArmatureMask();
        mask.addBones(armature, "Neck");
        return mask;
    }

    public static ArmatureMask getChestArmature(Armature armature) {
        ArmatureMask mask = ArmatureMask.createMask(armature, "Chest");
        mask.removeJoints(armature, "Neck");
        return mask;
    }

    public static ArmatureMask getLowerArmature(Armature armature) {
        ArmatureMask mask = new ArmatureMask(armature);
        mask.remove(getChestArmature(armature));
        mask.remove(getNeckArmature(armature));
        return mask;
    }


}
