import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Cube {
	
	public Vector3f pos;
	public float width;
	public Vector3f color;
	public Cube(float x, float y, float z, float width) {
		this.pos = new Vector3f();
		this.color = new Vector3f();
		pos.x = x;
		pos.y = y;
		pos.z = z;
		this.width = width;
		
		//34-139-34
		// Give the cube a random color for now
		this.color.x = (float) Math.random();
		this.color.y = (float) Math.random();
		this.color.z = (float) Math.random();
		
		this.color.x = 34/255.0f + (float) Math.random() / 10;
		this.color.y = 139/255.0f + (float) Math.random() / 10;
		this.color.z = 34/255.0f + (float) Math.random() / 10;
	}
	
	public void draw() {
	    
		GL11.glColor3f(color.x,color.y,color.z);
		GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE);
		
		//front
		GL11.glNormal3f(0, 0, 1);
	    GL11.glVertex3f(pos.x, pos.y, pos.z);
	    GL11.glVertex3f(pos.x + width, pos.y, pos.z);
	    GL11.glVertex3f(pos.x + width, pos.y - width, pos.z);
	    GL11.glVertex3f(pos.x, pos.y - width, pos.z);

		GL11.glNormal3f(0, 0, -1);
	    //back
	    GL11.glVertex3f(pos.x, pos.y, pos.z + width);
	    GL11.glVertex3f(pos.x + width, pos.y, pos.z + width);
	    GL11.glVertex3f(pos.x + width, pos.y - width, pos.z + width);
	    GL11.glVertex3f(pos.x, pos.y - width, pos.z + width);
	    

		GL11.glNormal3f(0, -1, 0);
	    //bottom
	    GL11.glVertex3f(pos.x, pos.y - width, pos.z);
	    GL11.glVertex3f(pos.x, pos.y - width, pos.z + width);
	    GL11.glVertex3f(pos.x + width, pos.y - width, pos.z + width);
	    GL11.glVertex3f(pos.x + width, pos.y - width, pos.z);
	    
	    
	    GL11.glNormal3f(0, 1, 0);
	    //top
	    GL11.glVertex3f(pos.x, pos.y, pos.z);
	    GL11.glVertex3f(pos.x, pos.y, pos.z + width);
	    GL11.glVertex3f(pos.x + width, pos.y, pos.z + width);
	    GL11.glVertex3f(pos.x + width, pos.y, pos.z);
	    

	    GL11.glNormal3f(0, 1, 0);
	    //left
	    GL11.glVertex3f(pos.x, pos.y, pos.z);
	    GL11.glVertex3f(pos.x, pos.y, pos.z + width);
	    GL11.glVertex3f(pos.x, pos.y - width, pos.z + width);
	    GL11.glVertex3f(pos.x, pos.y - width, pos.z);
	    
	    GL11.glNormal3f(0, -1, 0);
	    //right
	    GL11.glVertex3f(pos.x + width, pos.y, pos.z);
	    GL11.glVertex3f(pos.x + width, pos.y, pos.z + width);
	    GL11.glVertex3f(pos.x + width, pos.y - width, pos.z + width);
	    GL11.glVertex3f(pos.x + width, pos.y - width, pos.z);
	}


}
