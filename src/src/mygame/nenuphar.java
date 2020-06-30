/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import static com.jme3.bullet.PhysicsSpace.getPhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author maxen
 */
public class nenuphar {
    
    Spatial nenuphar;
    private AssetManager assetManager;
    private Node rootNode;
    private BetterCharacterControl nenupharControl;
    BulletAppState bulletAppState;
    Node nenupharNode;
    Boolean dead = false;
   
    
    public void spawn(AssetManager assetManager, Node rootNode, BulletAppState bulletAppState, int x, int y) {
        
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        this.bulletAppState = bulletAppState;
        
        Spatial nenuphar = assetManager.loadModel("Models/nenuphar/clover.j3o");
        
        Material mat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Models/nenuphar/nenu.png")); // with Unshaded.j3md
        nenuphar.setMaterial(mat);
        nenupharNode = new Node();
        nenupharNode.attachChild(nenuphar);
        nenuphar.setLocalScale(1.6f);       
        nenupharControl = new BetterCharacterControl(1f,1f,1f);
        nenupharNode.addControl(nenupharControl);
        
        /* Proprietes basiques */
        nenupharControl.warp(new Vector3f(x,0.39f,y));
        
        /* Physique */
        bulletAppState.getPhysicsSpace().add(nenupharControl);
        
        /* Gravité */
        nenupharControl.setGravity(new Vector3f(0f,-0.00000000001f,0f));   
        
        rootNode.attachChild(nenupharNode);
              
    }
    
    public Node getNenu() {
        return nenupharNode;
    }
    
    public void die() {
       nenupharControl.setEnabled(false);
       rootNode.detachChild(nenupharNode);
       dead = true;
    }
    
    public Boolean getDead() {
        return dead;
    }
}
