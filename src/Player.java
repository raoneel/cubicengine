import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;
/**
 * This class controls the first-person camera and also the keypress actions for the player
 * @author neelrao
 *
 */
public class Player {
	public Vector3f camera;
	public Vector3f lookAt;
	
	public Vector3f oldCamera;
	public Vector3f oldLookAt;
	
	public Vector3f up;
	public AABB aabb;
	
	public boolean isFly;
	public boolean wasHit;
	public boolean wasRestored;
	public boolean noClip;
	
    float mouseSensitivity = 2.0f;
    float movementSpeed = 5000.0f; //move 10 units per second
    
    private float vx;
    private float vy;
    
    public Player() {
    	camera = new Vector3f(0, 600, 500);
		lookAt = new Vector3f(0, 600, 0);
		oldCamera = new Vector3f();
		oldLookAt = new Vector3f();
		
		up = new Vector3f(0, 1, 0);
		isFly = false;
		noClip = false;
		this.makeAABB();
    }
    
    public AABB getAABB() {
    	this.aabb.pos.x = this.camera.x;
    	this.aabb.pos.y = this.camera.y;
    	this.aabb.pos.z = this.camera.z - 200;
    	
    	this.aabb.extent.x = 50;
    	this.aabb.extent.y = 50;
    	this.aabb.extent.z = 50;
    	return this.aabb;
    }
    
    private void makeAABB() {
    	Vector3f min = new Vector3f(this.camera.x - 200, this.camera.y - 200, this.camera.z - 200);
    	Vector3f max = new Vector3f(this.camera.x, this.camera.y, this.camera.z);
    	AABB aabb = new AABB(min, max);
    	this.aabb = aabb;
    }
    
    private void processInput() {

      while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
			    if (Keyboard.getEventKey() == Keyboard.KEY_A) {
			//  		    System.out.println("A Key Pressed");
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_S) {
			//  		    System.out.println("S Key Pressed");
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_D) {
				    
			//  		    System.out.println("D Key Pressed");
				}
			} 
			else {
			    if (Keyboard.getEventKey() == Keyboard.KEY_F) {
			//  		    System.out.println("A Key Released");
			    	this.isFly = !isFly;
			    }
				if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) {
					//this.translate(0, 400, 0);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_V) {
					noClip = !noClip;
					
				}
			}
  		}

    }
    
    public void translate(float x, float y, float z) {
    	Vector3f t = new Vector3f(x, y, z);
    	Vector3f.add(camera, t, camera);
    	Vector3f.add(lookAt, t, lookAt);
    }
    
    public void savePosition() {
    	this.oldCamera.x = camera.x;
    	this.oldCamera.y = camera.y;
    	this.oldCamera.z = camera.z;
    	
    	this.oldLookAt.x = lookAt.x;
    	this.oldLookAt.y = lookAt.y;
    	this.oldLookAt.z = lookAt.z;
    }
    
    public void restorePosition() {
    	this.camera.x = this.oldCamera.x;
    	this.camera.y = this.oldCamera.y;
    	this.camera.z = this.oldCamera.z;
    	
    	this.lookAt.x = this.oldLookAt.x;
    	this.lookAt.y = this.oldLookAt.y;
    	this.lookAt.z = this.oldLookAt.z;

    }
    
    public void checkIntersection() {
    	
    }
    
    public void update(int delta, ArrayList<Cube> cubes) {
    	this.processInput();
    	//distance in mouse movement from the last getDX() call.
        int dx = Mouse.getDX();
        //distance in mouse movement from the last getDY() call.
        int dy = Mouse.getDY();
        

        if (!wasHit) {
        	this.savePosition();
        }

        lookAt.y += dy * mouseSensitivity;
        
        Vector3f view = new Vector3f();
        Vector3f.sub(lookAt, camera, view);
        view.normalise();
        
        Vector3f right = new Vector3f(0, 0, 0);
        Vector3f.cross(view, up, right);
        
        lookAt.x += right.x * dx * mouseSensitivity;
        lookAt.z += right.z * dx * mouseSensitivity;
        
        float deltaT = delta / 1000.0f;
        right.scale(movementSpeed * deltaT);
        
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
        	movementSpeed = 50000f;
        }
        else {
        	movementSpeed = 5000;
        }
        

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            this.translate(0, 200, 0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_X)){
        	this.translate(0, -200, 0);
        }

        
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
        	Vector3f.sub(lookAt, right, lookAt);
        	Vector3f.sub(camera, right, camera);
        }
        
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
        	Vector3f.add(lookAt, right, lookAt);
        	Vector3f.add(camera, right, camera);
        }
        
        view.scale(movementSpeed * deltaT);
        // Flying
        if (!isFly) {
        	view.y = 0;
        }
        
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
        	Vector3f.add(lookAt, view, lookAt);
        	Vector3f.add(camera, view, camera);
        }
        
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
        	Vector3f.sub(lookAt, view, lookAt);
        	Vector3f.sub(camera, view, camera);
        }
        
        AABB aabb = this.getAABB();
        boolean isHit = false;
       
        if (!noClip) {
            for (Cube c : cubes) {
            	if (c.aabb.intersects(aabb)) {
            		isHit = true;
            		
            		break;
            	}
            }
        }
        else {
        	isHit = false;
        }

        
        //Stop gravity
        if (!wasRestored && !isHit && !isFly) {
        	this.translate(0, -2000*deltaT, 0);
        }
        
        //Revert movement to pre update
        if (!wasHit && isHit) {
        	this.restorePosition();
        	wasHit = false;
        	wasRestored = true;
        }
        else {
        	 wasHit = isHit;
        	 wasRestored = false;
        }
        
       
        
        GL11.glLoadIdentity();
    	GLU.gluLookAt(camera.x, camera.y, camera.z, lookAt.x, lookAt.y, lookAt.z, up.x, up.y, up.z);
    	
    }
}
