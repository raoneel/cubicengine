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
		
		this.color.x = (float) Math.random();
		this.color.y = (float) Math.random();
		this.color.z = (float) Math.random();
	}
	
	public void draw() {
	    //bottom
		GL11.glColor3f(color.x,color.y,color.z);
		GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE);
		Vector3f edge1 = new Vector3f();
		Vector3f edge2 = new Vector3f();
		Vector3f normal = new Vector3f();
		
		edge1.x = -width;
		edge1.y = 0;
		edge1.z = 0;
		edge2.x = 0;
		edge2.y = -width;
		edge2.z = 0;
		Vector3f.cross(edge1, edge2, normal);
		normal.normalise();
		GL11.glNormal3f(normal.x, normal.y, normal.z);
		
	    GL11.glVertex3f(pos.x, pos.y, pos.z);
	    GL11.glVertex3f(pos.x + width, pos.y, pos.z);
	    GL11.glVertex3f(pos.x + width, pos.y - width, pos.z);
	    GL11.glVertex3f(pos.x, pos.y - width, pos.z);
	    normal.normalise();
		GL11.glNormal3f(-normal.x, -normal.y, -normal.z);
		
	    //top
	    GL11.glVertex3f(pos.x, pos.y, pos.z + width);
	    GL11.glVertex3f(pos.x + width, pos.y, pos.z + width);
	    GL11.glVertex3f(pos.x + width, pos.y - width, pos.z + width);
	    GL11.glVertex3f(pos.x, pos.y - width, pos.z + width);
	    
		edge1.x = 0;
		edge1.y = 0;
		edge1.z = -width;
		edge2.x = width;
		edge2.y = 0;
		edge2.z = 0;
		Vector3f.cross(edge1, edge2, normal);
		normal.normalise();
		GL11.glNormal3f(normal.x, normal.y, normal.z);
	    
	    //front
	    GL11.glVertex3f(pos.x, pos.y - width, pos.z);
	    GL11.glVertex3f(pos.x, pos.y - width, pos.z + width);
	    GL11.glVertex3f(pos.x + width, pos.y - width, pos.z + width);
	    GL11.glVertex3f(pos.x + width, pos.y - width, pos.z);
	    
	    normal.normalise();
	    GL11.glNormal3f(-normal.x, -normal.y, -normal.z);
	    //back
	    GL11.glVertex3f(pos.x, pos.y, pos.z);
	    GL11.glVertex3f(pos.x, pos.y, pos.z + width);
	    GL11.glVertex3f(pos.x + width, pos.y, pos.z + width);
	    GL11.glVertex3f(pos.x + width, pos.y, pos.z);
	    
		edge1.x = 0;
		edge1.y = 0;
		edge1.z = -width;
		edge2.x = 0;
		edge2.y = -width;
		edge2.z = 0;
		Vector3f.cross(edge1, edge2, normal);
		normal.normalise();
		GL11.glNormal3f(normal.x, normal.y, normal.z);
	    
	    //left
	    GL11.glVertex3f(pos.x, pos.y, pos.z);
	    GL11.glVertex3f(pos.x, pos.y, pos.z + width);
	    GL11.glVertex3f(pos.x, pos.y - width, pos.z + width);
	    GL11.glVertex3f(pos.x, pos.y - width, pos.z);
	    
	    normal.normalise();
	    GL11.glNormal3f(-normal.x, -normal.y, -normal.z);
	    //right
	    GL11.glVertex3f(pos.x + width, pos.y, pos.z);
	    GL11.glVertex3f(pos.x + width, pos.y, pos.z + width);
	    GL11.glVertex3f(pos.x + width, pos.y - width, pos.z + width);
	    GL11.glVertex3f(pos.x + width, pos.y - width, pos.z);
	}


}
