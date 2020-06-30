package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResults;
import com.jme3.input.CameraInput;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        
        /* Paramètres */
        AppSettings newSettings = new AppSettings(true);
        newSettings.setFrameRate(144); //FPS Max
        newSettings.setResolution(1920, 1080); // Resolution
        app.setSettings(newSettings);
        
        app.start(); //Demarrage du jeu
    }
    
    
    /* Initialisation des liste d'objets */
    //Canards
    List<Duck> DuckList = new ArrayList<Duck>();
    List<nenuphar> NenuList = new ArrayList<nenuphar>();
    
    private BulletAppState bulletAppState;
    private Spatial sceneModel;
    private RigidBodyControl landscape;
    CollisionResults results = new CollisionResults();
    
    int nb_duck;
    int nb_nenu;
    
    private boolean setInput = true;
    int loop_count=1;
    
    /* Son */
    private AudioNode audio_nature;
    private AudioNode audio_water;
    

    @Override
    public void simpleInitApp() {
                
        /* Initialisation de la scene */
        sceneModel = assetManager.loadModel("Scenes/scene.j3o");
        sceneModel.setLocalScale(1f);
        
        /* Collision map */
        CollisionShape sceneShape =
        CollisionShapeFactory.createMeshShape(sceneModel);
        landscape = new RigidBodyControl(sceneShape, 0);
        sceneModel.addControl(landscape);
        rootNode.attachChild(sceneModel);
        
        /* Physique */
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.getPhysicsSpace().add(landscape);
        
        /* Paramètres camera et controles */
        flyCam.setMoveSpeed(65);
        
        /* eau scene */
        FilterPostProcessor water;
        water = assetManager.loadFilter("Models/water.j3f");
        viewPort.addProcessor(water);
        
        /* Creation des canards de base */
        nb_duck = 20; //Nombre de canards à créer de base
        
        for (int i=0; i < nb_duck; i++) 
        {
            Duck duck = new Duck();
            DuckList.add(duck);
        }
        
        /* Creation des nenuphars de base */
        nb_nenu = 70; //Nombre de nenuphars à créer de base
        
        for (int i=0; i < nb_nenu; i++) 
        {
            nenuphar nenu = new nenuphar();
            NenuList.add(nenu);
        }
      
        /* Spawn des canards */
        for(int i = 0; i < DuckList.size(); i++)
        {
            Random rand = new Random();       
            int x = rand.nextInt(85 + 85) -85;
            int y = rand.nextInt(85 + 25) -25;
            DuckList.get(i).spawn(assetManager, rootNode, bulletAppState, x, y, 0.05f);
        }
        
        /* Spawn des nenuphars */
        for(int i = 0; i < NenuList.size(); i++)
        {
            Random rand = new Random();       
            int x = rand.nextInt(82 + 80) -80;
            int y = rand.nextInt(80 + 20) -20;
            NenuList.get(i).spawn(assetManager, rootNode, bulletAppState, x, y);
        }
        
        /* Lancement de l'audio */
        initAudio();
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        
        /* Deplacement clavier Azerty */
        if(setInput){
            inputManager.deleteMapping(CameraInput.FLYCAM_STRAFELEFT);
            inputManager.deleteMapping(CameraInput.FLYCAM_FORWARD);
            inputManager.deleteMapping(CameraInput.FLYCAM_RISE);
            inputManager.deleteMapping(CameraInput.FLYCAM_LOWER);
            inputManager.addMapping("FLYCAM_StrafeLeft", new KeyTrigger(KeyInput.KEY_Q));
            inputManager.addMapping("FLYCAM_Forward", new KeyTrigger(KeyInput.KEY_Z));
            inputManager.addMapping("FLYCAM_Lower", new KeyTrigger(KeyInput.KEY_LSHIFT));
            inputManager.addMapping("FLYCAM_Rise", new KeyTrigger(KeyInput.KEY_SPACE));
            inputManager.addListener(flyCam, new String[]{
                "FLYCAM_StrafeLeft", "FLYCAM_Forward", "FLYCAM_Lower", "FLYCAM_Rise"
            });
            setInput = false;
        }
        
        /* Boucle excluant l'initialisation */
        if(loop_count != 1) {
            for(int i = 0; i < DuckList.size(); i++)  
            {
                /* Deplacement aléatoire des canards */
                DuckList.get(i).move();
                
                /* Mise a jour du temps  */
                DuckList.get(i).foodUpdate();
                
                for (int j = 0; j < NenuList.size(); j++) 
                {
                    if(DuckList.size() > 0) {
                        DuckList.get(i).eat(NenuList.get(j));
                        if(NenuList.get(j).getDead()) {
                            NenuList.remove(j);
                        }
                    }
                }
                /* Suppression canard de la liste si mort */
                if(DuckList.get(i).getDead()) {
                    DuckList.remove(i);
                }
        }
    }
        
        /* Naissance Canards */
        Random rand = new Random();       
        if(rand.nextInt(3000) == 5) {
            Duck duck = new Duck();
            DuckList.add(duck);
            int x = rand.nextInt(84 + 82) -82;
            int y = rand.nextInt(84 + 23) -23;
            duck.spawn(assetManager, rootNode, bulletAppState, x, y, 0.05f);
        }
        
        /* Nouveaux nenuphars */
        Random rand2 = new Random();       
        if(rand2.nextInt(170) == 5) {
            nenuphar nenu = new nenuphar();
            NenuList.add(nenu);
            int x = rand.nextInt(82 + 80) -80;
            int y = rand.nextInt(80 + 20) -20;
            nenu.spawn(assetManager, rootNode, bulletAppState, x, y);
        }       
      
        loop_count += 1;
    }
    
    
    public void initAudio() {
        /* Fond nature */
        audio_nature = new AudioNode(assetManager, "Sounds/nature.wav", DataType.Stream);
        audio_nature.setLooping(true);
        audio_nature.setPositional(false);
        audio_nature.setVolume(0.07f);
        rootNode.attachChild(audio_nature);
        audio_nature.play();
        
        /* Fond eau */
        audio_water = new AudioNode(assetManager, "Sounds/water.wav", DataType.Stream);
        audio_water.setLooping(true);
        audio_water.setPositional(false);
        audio_water.setVolume(0.002f);
        rootNode.attachChild(audio_water);
        audio_water.play();
         
    }
     
}
