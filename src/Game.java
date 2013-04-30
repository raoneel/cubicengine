import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

public class Game {
    
	ArrayList<Cube> myCubes;
	
	/** time at last frame */
	long lastFrame;
	
	/** frames per second */
	int fps;
	/** last fps time */
	long lastFPS;
    
    Player player;
    
    World world;
    
    
    int size = 253;
    float frequency = 0.06666f;
    int step = (int)(1/frequency);
    int amplitude = 3;
    int height = 50;
    int floor = 1;
    
    float heightMap[][] = new float[size][size];
    float tempValue = 0.0f;
    int randomNums[][] = new int[size][size];
    long seed = 122L;
    private int displayList;
    
    Random gen = new Random(seed);
	
    public void start() {
    	
	    try {
		    Display.setDisplayMode(new DisplayMode(800,600));
		    Display.create();
            
		} catch (LWJGLException e) {
		    e.printStackTrace();
		    System.exit(0);
		}
		
		player = new Player();
		player.translate(1000, 6000, 1000);
		//world = new World(50, 20, 50);
		//world = new Chunk(100, 100, 100);
		initGL();
		world = new World(player);
		
	    //hide the mouse
	    Mouse.setGrabbed(true);
	    
	    // call once before loop to initialise lastFrame
	    getDelta();
	    // call before loop to initialise fps timer
		lastFPS = getTime();

	    world.make();

		while (!Display.isCloseRequested()) {
		    // Clear the screen and depth buffer
			GL11.glClearColor(0, 191/255.0f, 1, 1);
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
        
    	world.update(player, displayList);
    	player.update(delta, world);
    	world.draw();
        
    }
    
    public void initGL() {
        
		// init OpenGL
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(60.0f, 800f/600f, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		float lightAmbient[] = { 0.5f, 0.5f, 0.5f, 1.0f };  // Ambient Light Values
	    float lightDiffuse[] = { 1.0f, 1.0f, 0.7f, 1.0f };      // Diffuse Light Values
	    float lightPosition[] = { 100.0f, 300.0f, 20.0f, 0 }; // Light Position
        
	    ByteBuffer temp = ByteBuffer.allocateDirect(16);
	    temp.order(ByteOrder.nativeOrder());
	    GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, (FloatBuffer)temp.asFloatBuffer().put(lightAmbient).flip());              // Setup The Ambient Light
	    GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, (FloatBuffer)temp.asFloatBuffer().put(lightDiffuse).flip());              // Setup The Diffuse Light
	    GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION,(FloatBuffer)temp.asFloatBuffer().put(lightPosition).flip());         // Position The Light
	    
	    
	    GL11.glLightf(GL11.GL_LIGHT1, GL11.GL_LINEAR_ATTENUATION, 0.3f);
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
        Game game = new Game();
        game.start();
    }
}