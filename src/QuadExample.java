import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;
 
public class QuadExample {
	
	Vector3f camera;
	Vector3f lookAt;
	Vector3f up;

	ArrayList<Cube> myCubes;
	
	/** time at last frame */
	long lastFrame;
	
	/** frames per second */
	int fps;
	/** last fps time */
	long lastFPS;
	 
    float dx        = 0.0f;
    float dy        = 0.0f;
 
    float mouseSensitivity = 2.0f;
    float movementSpeed = 5000.0f; //move 10 units per second
	
 
    public void start() {
    	
	    try {
		    Display.setDisplayMode(new DisplayMode(800,600));
		    Display.create();
	
		} catch (LWJGLException e) {
		    e.printStackTrace();
		    System.exit(0);
		}
		
		camera = new Vector3f(0, 600, 500);
		lookAt = new Vector3f(0, 600, 0);
		up = new Vector3f(0, 1, 0);
		
		initGL();
		
	    //hide the mouse
	    Mouse.setGrabbed(true);
	    
	    // call once before loop to initialise lastFrame
	    getDelta(); 
	    // call before loop to initialise fps timer
		lastFPS = getTime(); 

		//Create random cubes
		myCubes = new ArrayList<Cube>();

	    for (int i = 0; i < 100; i++) {
	    	
	    	for (int j = 0; j < 100; j++) {
	    		int randomHeight = 1;;
	    		for (int k = 0; k < randomHeight;k++) {
//	    			Cube c = new Cube(i * 200, (float) Math.sin( (i * 200) / 2000.0f) * 8000, j*200, 200);
	    			Cube c = new Cube(i * 200, 0, j*200, 200);
	    	    	myCubes.add(c);
	    		}
	    	}

	    }
    
    
		while (!Display.isCloseRequested()) {
		    // Clear the screen and depth buffer
		    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);	
			

		    
		    int delta = getDelta();
			update(delta);

		    updateFPS(); // update FPS Counter		     
		    Display.update();
		    Display.sync(60); // cap fps to 60fps
		}
 
		Display.destroy();
    }
    
    public void update(int delta) {
        //distance in mouse movement from the last getDX() call.
        dx = Mouse.getDX();
        //distance in mouse movement from the last getDY() call.
        dy = Mouse.getDY();
        
       
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
        // Flying off if line below
        view.y = 0;
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
        	Vector3f.add(lookAt, view, lookAt);
        	Vector3f.add(camera, view, camera);
        }
        
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
        	Vector3f.sub(lookAt, view, lookAt);
        	Vector3f.sub(camera, view, camera);
        }
        
        boolean isHit = false;
        
        for (Cube c: myCubes) {
        	if (camera.y - c.pos.y < 800 && camera.x - c.pos.x < 10 && camera.z - c.pos.z < 10) {
        		isHit = true;
        		break;
        	}
        }
        //Gravity
        if (!isHit) {
            lookAt.y -= 500.0f * deltaT;
            camera.y -= 500.0f * deltaT;
        }

    
//    while (Keyboard.next()) {
//	    if (Keyboard.getEventKeyState()) {
//	        if (Keyboard.getEventKey() == Keyboard.KEY_A) {
//		    System.out.println("A Key Pressed");
//		    cameraX -= 10f;
//		}
//		if (Keyboard.getEventKey() == Keyboard.KEY_S) {
//		    System.out.println("S Key Pressed");
//		}
//		if (Keyboard.getEventKey() == Keyboard.KEY_D) {
//			cameraX += 10f;
//		    
//		    System.out.println("D Key Pressed");
//		}
//	    } else {
//	        if (Keyboard.getEventKey() == Keyboard.KEY_A) {
//		    System.out.println("A Key Released");
//	        }
//	    	if (Keyboard.getEventKey() == Keyboard.KEY_S) {
//		    System.out.println("S Key Released");
//		}
//		if (Keyboard.getEventKey() == Keyboard.KEY_D) {
//		    System.out.println("D Key Released");
//		}
//	    }
//	}
    
        GL11.glLoadIdentity();
    	GLU.gluLookAt(camera.x, camera.y, camera.z, lookAt.x, lookAt.y, lookAt.z, up.x, up.y, up.z);
	    
	    // Begin drawing 
	    GL11.glBegin(GL11.GL_QUADS);

	    for (Cube cube : myCubes) {
	    	cube.draw();
	    }
		
		
	    GL11.glEnd();
    }
    
    public void initGL() {
		
		
		
		// init OpenGL
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(60.0f, 800f/600f, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		float lightAmbient[] = { 1.0f, 0.3f, 0.3f, 1.0f };  // Ambient Light Values
	    float lightDiffuse[] = { 1.0f, 1.0f, 0.7f, 1.0f };      // Diffuse Light Values
	    float lightPosition[] = { 100.0f, 300.0f, 20.0f, 0 }; // Light Position
    
	    ByteBuffer temp = ByteBuffer.allocateDirect(16);
	    temp.order(ByteOrder.nativeOrder());
	    GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, (FloatBuffer)temp.asFloatBuffer().put(lightAmbient).flip());              // Setup The Ambient Light
	    GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, (FloatBuffer)temp.asFloatBuffer().put(lightDiffuse).flip());              // Setup The Diffuse Light
	    GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION,(FloatBuffer)temp.asFloatBuffer().put(lightPosition).flip());         // Position The Light
	    GL11.glEnable(GL11.GL_LIGHT1);                          // Enable Light One

	
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glShadeModel (GL11.GL_FLAT);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
    }
    
	/** 
	 * Calculate how many milliseconds have passed 
	 * since last frame.
	 * 
	 * @return milliseconds passed since last frame 
	 */
	public int getDelta() {
	    long time = getTime();
	    int delta = (int) (time - lastFrame);
	    lastFrame = time;
	 
	    return delta;
	}
	
	/**
	 * Get the accurate system time
	 * 
	 * @return The system time in milliseconds
	 */
	public long getTime() {
	    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	/**
	 * Calculate the FPS and set it in the title bar
	 */
	public void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			Display.setTitle("FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
 
    public static void main(String[] argv) {
        QuadExample quadExample = new QuadExample();
        quadExample.start();
    }
}