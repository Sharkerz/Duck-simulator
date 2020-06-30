/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import static com.jme3.bullet.PhysicsSpace.getPhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.Random;

/**
 *
 * @author maxence
 */
public class Duck {
    
    Spatial duck;
    private AssetManager assetManager;
    private Node rootNode;
    private Vector3f walkDirection = new Vector3f();
    private BetterCharacterControl moveDuck;
    private int proba;
    private float positionX;
    private float positionY;
    private float positionZ;
    Node DuckNode;
    BulletAppState bulletAppState;
    Boolean dead = false;
    
    /* Nourriture et taille du canard */
    int food = 2;
    float size = 0.05f;
    int TimeNoFood = 2200;
    
    /* Son canard chef */
    private AudioNode audio_duck;
    int audioChef=0;
    
    public Duck()
    {
        
    }
    
    public void spawn(AssetManager assetManager, Node rootNode, BulletAppState bulletAppState, int x, int y, float size) {
        
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        this.bulletAppState = bulletAppState;
        
        Spatial duck = assetManager.loadModel("Models/Duck2/Duck.j3o");

        DuckNode = new Node();
        DuckNode.attachChild(duck);
        
        if(size == 0.05f) {
            duck.move(0,0.8f,0);   
        }
        
        duck.setLocalScale(size);      
        
        moveDuck = new BetterCharacterControl(1f,5f,1f);
        DuckNode.addControl(moveDuck);
        
        /* Rotation axe X de 280 degrés */
        Quaternion roll180 = new Quaternion();
        roll180.fromAngleAxis( FastMath.PI * 280 / 180 , new Vector3f(1,0,0) );
        duck.setLocalRotation( roll180 );
        
        /* Proprietes basiques */
        moveDuck.warp(new Vector3f(x,-1.1f,y));
        
        /* Physique */
        bulletAppState.getPhysicsSpace().add(moveDuck);
        
        /* Gravité */
        moveDuck.setGravity(new Vector3f(0f,-0.000000000000000001f,0f));   
        
        /* lumière des canards */
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White.mult(0.7f));
        DuckNode.addLight(ambient);
        
        rootNode.attachChild(DuckNode);
        
    }
    
        public void move() {   
        /* Cordonnées */
        positionX = DuckNode.getWorldTranslation().x;   
        positionY = DuckNode.getWorldTranslation().y;
        positionZ = DuckNode.getWorldTranslation().z;
        
        Random rand = new Random();       
        proba = rand.nextInt(750);
        
        if (proba == 1) {
             moveDuck.setWalkDirection(new Vector3f(5f,0f,0f));
             moveDuck.setViewDirection(new Vector3f(1f,0f,0.1f));
         }
        else if (proba == 2) {
             moveDuck.setWalkDirection(new Vector3f(-5f,0f,0f));
             moveDuck.setViewDirection(new Vector3f(-1f,0f,0.1f));
         }
        else if (proba == 3) {
             moveDuck.setWalkDirection(new Vector3f(0f,0f,5f));
             moveDuck.setViewDirection(new Vector3f(0f,0f,1f));
         }
        else if (proba == 4) {
             moveDuck.setWalkDirection(new Vector3f(0f,0f,-5f));
             moveDuck.setViewDirection(new Vector3f(0f,0f,-1f));
         }
        
        /* Bords du lac */
        if(positionX > 80.0) {
            moveDuck.setWalkDirection(new Vector3f(-5f,0f,0f));
            moveDuck.setViewDirection(new Vector3f(-1f,0f,0.1f));
        }
        if(positionX < -80.0) {
            moveDuck.setWalkDirection(new Vector3f(5f,0f,0f));
            moveDuck.setViewDirection(new Vector3f(1f,0f,0.1f));
        }
        if(positionZ > 72.0) {
            moveDuck.setWalkDirection(new Vector3f(0f,0f,-5f));
            moveDuck.setViewDirection(new Vector3f(0f,0f,-1f));
        }
        if(positionZ < -13.0) {
            moveDuck.setWalkDirection(new Vector3f(0f,0f,5f));
            moveDuck.setViewDirection(new Vector3f(0f,0f,1f));
        }
        
        /* Affichage coordonnées si nécessaire */
        //System.out.println("x: " + playerNode.getWorldTranslation().x);
        //System.out.println("y: " + playerNode.getWorldTranslation().y);
        //System.out.println("z: " + playerNode.getWorldTranslation().z);
               
        /* Son si chef */
        if(audioChef == 1) {
            if(proba == 8) {
                audio_duck.play();
            }
        }
        
    }
        
    public void eat(nenuphar nenuphar) 
    {
        //Coordonnées du canard
        float xEatDuck = DuckNode.getWorldTranslation().x;
        float zEatDuck = DuckNode.getWorldTranslation().z;
        
        //Coordonnées du nenuphar
        float xEatNenu = nenuphar.getNenu().getWorldTranslation().x;
        float zEatNenu = nenuphar.getNenu().getWorldTranslation().z;
        
        //System.out.println("x nenu: " + xEatNenu);
        //System.out.println("z nenu: " + zEatNenu);
      
        //Si un canard mange un nenuphar
        if((xEatDuck - xEatNenu < 5) && (xEatDuck - xEatNenu > -5) && (zEatDuck - zEatNenu < 5) && (zEatDuck - zEatNenu > -5)) {
            food+=1;
            TimeNoFood = 2200;
            nenuphar.die();
            sizeUpdate();
        }
    }
    
    public void sizeUpdate() {
        if(food <= 2 ) {
                //Taille du Canard après avoir mangé
                size = 0.05f;
                moveDuck.setEnabled(false);
                rootNode.detachChild(DuckNode);
                int xRespawn = (int) positionX;
                int yRespawn = (int) positionZ;
                spawn(assetManager, rootNode, bulletAppState, xRespawn, yRespawn, size);
                moveDuck.setWalkDirection(new Vector3f(0f,0f,5f));
            }
            else if(food >= 3 && food < 6) {
                //Taille du Canard après avoir mangé
                size = 0.09f;
                moveDuck.setEnabled(false);
                rootNode.detachChild(DuckNode);
                int xRespawn = (int) positionX;
                int yRespawn = (int) positionZ;
                spawn(assetManager, rootNode, bulletAppState, xRespawn, yRespawn, size);
                moveDuck.setWalkDirection(new Vector3f(0f,0f,5f));
            }
            else if(food >= 6 && food < 8) {
                //Taille du Canard après avoir mangé
                size = 0.10f;
                moveDuck.setEnabled(false);
                rootNode.detachChild(DuckNode);
                int xRespawn = (int) positionX;
                int yRespawn = (int) positionZ;
                spawn(assetManager, rootNode, bulletAppState, xRespawn, yRespawn, size);
                moveDuck.setWalkDirection(new Vector3f(0f,0f,5f));
            }
            else if(food >= 8 && food < 12) {
                //Taille du Canard après avoir mangé
                size = 0.12f;
                audioChef = 0;
                moveDuck.setEnabled(false);
                rootNode.detachChild(DuckNode);
                int xRespawn = (int) positionX;
                int yRespawn = (int) positionZ;
                spawn(assetManager, rootNode, bulletAppState, xRespawn, yRespawn, size);
                moveDuck.setWalkDirection(new Vector3f(0f,0f,5f));
            }
            /* Canard Chef */
            else if(food >= 12) {
                //Taille du Canard après avoir mangé
                size = 0.21f;
                moveDuck.setEnabled(false);
                rootNode.detachChild(DuckNode);
                int xRespawn = (int) positionX;
                int yRespawn = (int) positionZ;
                spawn(assetManager, rootNode, bulletAppState, xRespawn, yRespawn, size);
                moveDuck.setWalkDirection(new Vector3f(0f,0f,5f));
                
                /* Audio */
                initAudio();
                
            }
    }
    
    /* mise a jour de la faim */
    public void foodUpdate() {
        
        if (TimeNoFood > 0) {
            TimeNoFood -= 1;
        }
        
        /* Si le canard n'a pas mangé depuis un moment */
        if (TimeNoFood == 0 && food > 0) {
            food -= 1;
            TimeNoFood = 2200;
            /* Pour que les bébés ne meurent pas trop vite */
            if (food == 1) {
                TimeNoFood += 1100;
            }
            
            /* Mise à jour de la taille du canard */
            sizeUpdate();
        }
        /* Le canard meurt si food est à 0 */
        else if (food == 0) {
            die();
        }
        
    }
    
    public void die() {
        moveDuck.setEnabled(false);
        rootNode.detachChild(DuckNode);
        dead = true;
    }
       
    public Boolean getDead() {
        return dead;
    }
    
        public void initAudio() {
        /* Cri Canard Chef */
        audio_duck = new AudioNode(assetManager, "Sounds/duck.wav", AudioData.DataType.Stream);
        audio_duck.setLooping(false);
        audio_duck.setPositional(true);
        audio_duck.setVolume(1f);
        DuckNode.attachChild(audio_duck);
        audio_duck.play();
        audioChef = 1; 
    }
    
}
