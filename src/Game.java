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
    
    Player player;
    int size = 129;
    float frequency = 0.05f;
    int step = (int)(1/frequency);
    int amplitude = 10;
    float heightMap[][] = new float[size][size];
    float tempValue = 0.0f;
    int randomNums[][] = new int[size][size];
    long seed = 12344L;
    Random gen = new Random(seed);
	
    public void start() {
    	generateRandom();
    	float intermediateX = 0.0f;
    	float intermediateY = 0.0f;
    	step = (int)(1/frequency);
    	
    	for (int i = 0; i < size - step; i += step){
    		//System.out.println(size);
    		for (int j = 0; j < size - step; j += step){
    			int v1 = randomNums[i][j];
    			int v2 = randomNums[i+step][j];
    			int v3 = randomNums[i][j+step];
    			int v4 = randomNums[i+step][j+step];
    			heightMap[i][j] = Math.round(v1);
    			heightMap[i+step][j] = Math.round(v2);
    			heightMap[i][j] = Math.round(v3);
    			heightMap[i+step][j+step] = Math.round(v4);
    			intermediateX = 0.0f;
    			for (int k = i; k< i + step; k++){
    				intermediateX += (1.0f/(float)(step));
    				float i1 = cosineInterp(v1,v2,intermediateX);
 	    	    	float i2 = cosineInterp(v3,v4,intermediateX);
 	    	    	heightMap[k][j] = Math.round(i1);
 	    	    	heightMap[k][j+step] = Math.round(i2);
 	    	    	intermediateY = 0.0f;
    				for (int l = j + 1; l < j + step; l++){
	 	    	    	intermediateY += (1.0f/(float)(step));
	 	    	    	float ans = cosineInterp(i1,i2,intermediateY);
	 	    	    	//heightMap[b][0] = Noise1D(heightMap[a-step][0],intermediate);
	 	    	        //tempValue = cosineInterp(heightMap[i-step][0],n,intermediate);
	 	    	        //heightMap[b][0] += tempValue;
	 	    	    	heightMap[k][l] = Math.round(ans);
	 	    	    	System.out.println(ans);
	 	    	    	
    				}
 	    	    }
                
                
    		}
    	}
    	
	    try {
		    Display.setDisplayMode(new DisplayMode(800,600));
		    Display.create();
            
		} catch (LWJGLException e) {
		    e.printStackTrace();
		    System.exit(0);
		}
		
		player = new Player();
		player.translate(1000, 0, 1000);
		initGL();
		
	    //hide the mouse
	    Mouse.setGrabbed(true);
	    
	    // call once before loop to initialise lastFrame
	    getDelta();
	    // call before loop to initialise fps timer
		lastFPS = getTime();
        
		//Create random cubes
		myCubes = new ArrayList<Cube>();
        
	    for (int i = 0; i < 129; i++) {
	    	
	    	for (int j = 0; j < 129; j++) {
	    		int randomHeight = 1;;
	    		for (int k = 0; k < randomHeight;k++) {
                    //	    			Cube c = new Cube(i * 200, (float) Math.sin( (i * 200) / 2000.0f) * 8000, j*200, 200);
	    			Cube c = new Cube(i * 200, heightMap[i][j]*200, j*200, 200);
	    	    	myCubes.add(c);
	    		}
	    	}
            
	    }
        
        
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
        
    	player.update(delta);
	    
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
    
	public float cosineInterp(float a, float b, float x){
    	float ft = x * 3.1415927f;
    	float f = (float) (1f - Math.cos(ft)) * .5f;
    	return a*(1-f) + b*f;
    }
	public void generateRandom(){
    	for (int i = 0; i < size; i++){
    		for (int j = 0; j < size; j++){
    			randomNums[i][j] = gen.nextInt(amplitude -1 +1) +1;
    		}
    	}
    }
	public float interpolate(float a, float step, float intermediate){
		return 0.0f;
	}
	
    public static void main(String[] argv) {
        Game game = new Game();
        game.start();
    }
}