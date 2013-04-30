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
	public boolean onGround;
    
    float mouseSensitivity = 2.0f;
    float movementSpeed = 5000.0f; //move 10 units per second
    float gravity = 100;
    float jumpDistance = 2000;
    
    private float vx;
    private float vy;
    
    public Vector3f velocity;
    public Vector3f accel;
    
    public Player() {
    	camera = new Vector3f(0, 0, 0);
		lookAt = new Vector3f(0, 0, 500);
		oldCamera = new Vector3f();
		oldLookAt = new Vector3f();
		velocity = new Vector3f(0,0,0);
		accel = new Vector3f(0,0,0);
		up = new Vector3f(0, 1, 0);
		isFly = false;
		noClip = false;
		onGround = false;
		this.makeAABB();
    }
    
    public AABB getAABB() {
    	this.aabb.pos.x = this.camera.x;
    	this.aabb.pos.y = this.camera.y;
    	//this.aabb.pos.z = this.camera.z;
    	this.aabb.pos.z = this.camera.z;;
    	
    	this.aabb.extent.x = 50;
    	this.aabb.extent.y = 50;
    	this.aabb.extent.z = 100;
    	return this.aabb;
    }
    
    public AABB getNextAABB(Vector3f velocity){
    	Vector3f posi = new Vector3f(this.camera.x + velocity.x, this.camera.y + velocity.y, this.camera.z - 200 + velocity.z);
    	Vector3f extent = this.aabb.extent;
    	AABB newaabb = new AABB(posi, extent);
    	return newaabb;
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
    
    public void update(int delta, World world) {
    	this.processInput();
    	//distance in mouse movement from the last getDX() call.
        int dx = Mouse.getDX();
        //distance in mouse movement from the last getDY() call.
        int dy = Mouse.getDY();
        AABB aabb = this.getAABB();
        
        if (!wasHit) {
        	this.savePosition();
        }
        
        lookAt.y += dy * mouseSensitivity;
        
        Vector3f view = new Vector3f();
        //
        Vector3f.sub(lookAt, camera, view);
        view.normalise();
        
        Vector3f right = new Vector3f(0, 0, 0);
        Vector3f.cross(view, up, right);
        
        lookAt.x += right.x * dx * mouseSensitivity;
        lookAt.z += right.z * dx * mouseSensitivity;
        
        float deltaT = delta / 1000.0f;
        right.scale(movementSpeed * deltaT);
        if(onGround){
        	velocity.y = 0;
        }
        if(!onGround && !isFly && velocity.y >= -2000){ //gravity
        	Vector3f.add(velocity, new Vector3f(0,-1*gravity*deltaT,0), velocity);
        }
        
        if(isFly){
        	velocity.x = 0;
        	velocity.y = 0;
        	velocity.z = 0;
        }
        
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
        	movementSpeed = 50000f;
        }
        else {
        	movementSpeed = 2000f;
        }
        Vector3f lastSpeed = new Vector3f();
        Vector3f addedSpeed = new Vector3f(0,0,0);
        
        if (isFly  && Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            this.translate(0, 200, 0);
            lastSpeed = null;
        }
        
        if(!isFly && onGround && Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
        	Vector3f.add(velocity, new Vector3f(0,jumpDistance*deltaT,0), velocity);
        	lastSpeed = null;
        }
        
        //if(!isFly && !onGround){
        	//right.scale(0.1f);
        	//view.scale(0.1f);
        	
        //}
        float scale = 1;
        if(!onGround && !isFly){
        	if(velocity.z == 0 && velocity.x == 0){
        		scale = 0.5f;
        	}
        	else{
        		scale = 0;
        	}
        }
        if(isFly){
        	scale = 1;
        }
        Vector3f prevVelocity = new Vector3f(velocity.x, velocity.y, velocity.z);
        if(scale != 0){
        	right.scale(scale);
        	view.scale(scale);
	        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
	        	//Vector3f.sub(lookAt, right, lookAt);
	        	//Vector3f.sub(camera, right, camera);
	        	Vector3f.sub(addedSpeed, right, addedSpeed);
	        	//lastSpeed = (Vector3f) right.scale(-1);
	        	Vector3f.sub(velocity, right, velocity);
	        }
	        
	        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
	        	//Vector3f.add(lookAt, right, lookAt);
	        	//Vector3f.add(camera, right, camera);
	        	//lastSpeed = right;
	        	Vector3f.add(addedSpeed, right , addedSpeed);
	        	Vector3f.add(velocity, right, velocity);
	        }
	        
	        view.scale(movementSpeed * deltaT);
	        // Flying
	        if (!isFly || onGround) {
	        	view.y = 0;
	        }
	        
	        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
	        	//Vector3f.add(lookAt, view, lookAt);
	        	//Vector3f.add(camera, view, camera);
	        	//lastSpeed = view;
	        	Vector3f.add(velocity, view, velocity);
	        	Vector3f.add(addedSpeed, view, addedSpeed);
	        }
	        
	        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
	        	//Vector3f.sub(lookAt, view, lookAt);
	        	//Vector3f.sub(camera, view, camera);
	        	//lastSpeed = (Vector3f) view.scale(-1);
	        	Vector3f.sub(velocity, view, velocity);
	        	Vector3f.sub(addedSpeed, view, addedSpeed);
	        }
        }
        
        AABB nextaabb = getNextAABB(velocity);
        boolean isHit = false;
        Cube hitCube = null;
        boolean hitGround = false;
        if (!noClip) {
        	
        	//TODO Replace with a AABB hierarchy cube test which could be done in the chunk
            
        	Chunk aChunk = world.chunkArray[world.xPosCenter][world.yPosCenter];
        	ArrayList<Cube> cubes = aChunk.cubes;
            
        	
        	
            for (Cube c : cubes) {
            	if(c.aabb.onGround(nextaabb)){
            		hitGround = true;
            	}
            	if (c.aabb.intersects(nextaabb)) {
            		isHit = true;
            		hitCube = c;
            		break;
            	}
            	if(onGround == false && hitGround == true){
            		onGround = true;
            	}
            	if(onGround == true && hitGround == false && isHit == false){
            		onGround = false;
            	}
            }
        }
        else {
        	isHit = false;
        }
        
        boolean gravity = false;
        Vector3f speedChange = new Vector3f();
        //Stop gravity
        if(isHit && !isFly){
        	Vector3f newSpeed = hitCube.aabb.hitWallz(this.aabb, this.velocity);
        	Vector3f.sub(velocity, newSpeed, speedChange);
        	velocity = newSpeed;
        }
        /*if (!wasRestored && !isHit && !isFly) {
         this.translate(0, -2000*deltaT, 0);
         gravity = true;
         }*/
        
        //Revert movement to pre update
        /*
         if (!wasHit && isHit && lastSpeed == null && gravity == true) {
         this.restorePosition();
         lastSpeed.x = 0;
         lastSpeed.y = -2000*deltaT;
         lastSpeed.z = 0;
         Vector3f newSpeed = this.aabb.gravityFix(hitCube.aabb, -2000*deltaT);
         Vector3f.add(lookAt, newSpeed, lookAt);
         Vector3f.add(camera, newSpeed, camera);
         wasHit = false;
         wasRestored = true;
         lastSpeed = null;
         
         }*/
        /*
         if(isHit && lastSpeed != null){
         this.restorePosition();
         Vector3f newSpeed =  this.aabb.lastAxis(hitCube.aabb, lastSpeed);
         Vector3f.add(lookAt, newSpeed, lookAt);
         Vector3f.add(camera, newSpeed, camera);
         
         }*/
        /*
         else {
         wasHit = isHit;
         wasRestored = false;
         }*/
        if(onGround){
            velocity.y = 0;
        }
        Vector3f.add(lookAt, velocity, lookAt);
        Vector3f.add(camera, velocity, camera);
        //if(speedChange.z == 0){
    	//   Vector3f.sub(addedSpeed,speedChange, addedSpeed);
        // }
        if(onGround){
            velocity.x = 0;
            velocity.z = 0;
        }
        //Vector3f.sub(velocity, addedSpeed, velocity);
        //  velocity.x = 0;
        // velocity.z = 0;
        GL11.glLoadIdentity();
    	GLU.gluLookAt(camera.x, camera.y, camera.z, lookAt.x, lookAt.y, lookAt.z, up.x, up.y, up.z);
    	
    }
}