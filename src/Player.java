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
	public Vector3f up;
	
	public boolean isFly;
	
    float mouseSensitivity = 2.0f;
    float movementSpeed = 5000.0f; //move 10 units per second
    
    public Player() {
    	camera = new Vector3f(0, 600, 500);
		lookAt = new Vector3f(0, 600, 0);
		up = new Vector3f(0, 1, 0);
		isFly = true;
    }
    
    private void processInput() {
        

        
//      while (Keyboard.next()) {
//  	    if (Keyboard.getEventKeyState()) {
//  	        if (Keyboard.getEventKey() == Keyboard.KEY_A) {
//  		    System.out.println("A Key Pressed");
//  		    cameraX -= 10f;
//  		}
//  		if (Keyboard.getEventKey() == Keyboard.KEY_S) {
//  		    System.out.println("S Key Pressed");
//  		}
//  		if (Keyboard.getEventKey() == Keyboard.KEY_D) {
//  			cameraX += 10f;
//  		    
//  		    System.out.println("D Key Pressed");
//  		}
//  	    } else {
//  	        if (Keyboard.getEventKey() == Keyboard.KEY_A) {
//  		    System.out.println("A Key Released");
//  	        }
//  	    	if (Keyboard.getEventKey() == Keyboard.KEY_S) {
//  		    System.out.println("S Key Released");
//  		}
//  		if (Keyboard.getEventKey() == Keyboard.KEY_D) {
//  		    System.out.println("D Key Released");
//  		}
//  	    }
//  	}
    }
    
    public void update(int delta) {
    	//distance in mouse movement from the last getDX() call.
        int dx = Mouse.getDX();
        //distance in mouse movement from the last getDY() call.
        int dy = Mouse.getDY();
        
       
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
            lookAt.y += 500.0f;
            camera.y += 500.0f;
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
        
        GL11.glLoadIdentity();
    	GLU.gluLookAt(camera.x, camera.y, camera.z, lookAt.x, lookAt.y, lookAt.z, up.x, up.y, up.z);
    	
    }
}
