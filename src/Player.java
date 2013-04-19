import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Player {
	public Vector3f camera;
	public Vector3f lookAt;
	public boolean isFly;
	
    float mouseSensitivity = 2.0f;
    float movementSpeed = 5000.0f; //move 10 units per second
    
    public void update(int delta) {
    	
    }
}
