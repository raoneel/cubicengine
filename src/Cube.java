import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Cube {
	
	public Vector3f pos;
	public float width;
	public Vector3f color;
	public AABB aabb;
	
	public Cube(float x, float y, float z, float width) {
		this.pos = new Vector3f();
		this.color = new Vector3f();
		pos.x = x;
		pos.y = y;
		pos.z = z;
		this.width = width;
		
		//34-139-34 grass
		//139-69-19 dirt
		// Give the cube a random color for now
		this.color.x = (float) Math.random();
		this.color.y = (float) Math.random();
		this.color.z = (float) Math.random();
		
//		this.color.x = 139/255.0f + (float) Math.random() / 10;
//		this.color.y = 69/255.0f + (float) Math.random() / 10;
//		this.color.z = 19/255.0f + (float) Math.random() / 10;
		makeAABB();
	}
	
	private void makeAABB() {
		Vector3f pos = new Vector3f(this.pos.x + width/2.0f, this.pos.y - width/2.0f, this.pos.z + width/2.0f);
		Vector3f extent = new Vector3f(width / 2.0f, width / 2.0f, width / 2.0f);
		AABB aabb = new AABB(pos, extent);
		this.aabb = aabb;
		
	}
	
	public static void drawCube(float x, float y, float z, float width) {
		// Give the cube a random color for now
		
//		GL11.glColor3f((float) Math.random(),(float) Math.random(),(float) Math.random());
		
		
		//front
		GL11.glNormal3f(0, 0, 1);
	    GL11.glVertex3f(x, y, z);
	    GL11.glVertex3f(x + width, y, z);
	    GL11.glVertex3f(x + width, y - width, z);
	    GL11.glVertex3f(x, y - width, z);

		GL11.glNormal3f(0, 0, -1);
	    //back
	    GL11.glVertex3f(x, y, z + width);
	    GL11.glVertex3f(x + width, y, z + width);
	    GL11.glVertex3f(x + width, y - width, z + width);
	    GL11.glVertex3f(x, y - width, z + width);
	    

		GL11.glNormal3f(0, -1, 0);
	    //bottom
	    GL11.glVertex3f(x, y - width, z);
	    GL11.glVertex3f(x, y - width, z + width);
	    GL11.glVertex3f(x + width, y - width, z + width);
	    GL11.glVertex3f(x + width, y - width, z);
	    
	    
	    GL11.glNormal3f(0, 1, 0);
	    //top
	    GL11.glVertex3f(x, y, z);
	    GL11.glVertex3f(x, y, z + width);
	    GL11.glVertex3f(x + width, y, z + width);
	    GL11.glVertex3f(x + width, y, z);
	    

	    GL11.glNormal3f(1, 0, 0);
	    //left
	    GL11.glVertex3f(x, y, z);
	    GL11.glVertex3f(x, y, z + width);
	    GL11.glVertex3f(x, y - width, z + width);
	    GL11.glVertex3f(x, y - width, z);
	    
	    GL11.glNormal3f(-1, 0, 0);
	    //right
	    GL11.glVertex3f(x + width, y, z);
	    GL11.glVertex3f(x + width, y, z + width);
	    GL11.glVertex3f(x + width, y - width, z + width);
	    GL11.glVertex3f(x + width, y - width, z);
	}
	
	public void draw() {
	    
		GL11.glColor3f(color.x,color.y,color.z);
		GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE);
		Cube.drawCube(pos.x, pos.y, pos.z, width);

	}


}
