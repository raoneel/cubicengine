import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Cube {
	
	public Vector3f pos;
	public float width;
	public Vector3f color;
	public AABB aabb;
	int xx, yy, zz;
	
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
//		this.color.x = (float) Math.random();
//		this.color.y = (float) Math.random();
//		this.color.z = (float) Math.random();
		
		this.color.x = 34/255.0f + (float) Math.random() / 10;
		this.color.y = 139/255.0f + (float) Math.random() / 10;
		this.color.z = 34/255.0f + (float) Math.random() / 10;
		makeAABB();
	}
	
	private void makeAABB() {
		Vector3f pos = new Vector3f(this.pos.x + width/2.0f, this.pos.y - width/2.0f, this.pos.z + width/2.0f - 200);
		Vector3f extent = new Vector3f(width / 2.0f, width / 2.0f, width / 2.0f);
		AABB aabb = new AABB(pos, extent);
		this.aabb = aabb;
		
	}
	
	/**
	 * Draws a cube, hides faces which are not seen according to the world array.
	 * @param x The floating point real coordinate
	 * @param y Real y coordinate
	 * @param z Real z coordinate
	 * @param width Real width of cube
	 * @param world Array of ints for the world
	 * @param xx world x coord
	 * @param yy world y coord
	 * @param zz world z coord
	 */
	public static void drawCube(float x, float y, float z, float width, Chunk world, int xx, int yy, int zz) {
		// Give the cube a random color for now
		
//		GL11.glColor3f((float) Math.random(),(float) Math.random(),(float) Math.random());
		
		
		if (world.getBlock(xx, yy, zz - 1) == 0) {
			//front - Along z axis
			GL11.glNormal3f(0, 0, 1);
		    GL11.glVertex3f(x, y, z);
		    GL11.glVertex3f(x + width, y, z);
		    GL11.glVertex3f(x + width, y - width, z);
		    GL11.glVertex3f(x, y - width, z);
		}
		
		if (world.getBlock(xx, yy, zz + 1) == 0) {
			GL11.glNormal3f(0, 0, -1);
		    //back
		    GL11.glVertex3f(x, y, z + width);
		    GL11.glVertex3f(x + width, y, z + width);
		    GL11.glVertex3f(x + width, y - width, z + width);
		    GL11.glVertex3f(x, y - width, z + width);
		}
	    
		if (world.getBlock(xx, yy - 1, zz) == 0) {
			GL11.glNormal3f(0, -1, 0);
		    //bottom - Along y axis
		    GL11.glVertex3f(x, y - width, z);
		    GL11.glVertex3f(x, y - width, z + width);
		    GL11.glVertex3f(x + width, y - width, z + width);
		    GL11.glVertex3f(x + width, y - width, z);
		}

		if (world.getBlock(xx, yy + 1, zz) == 0) {
		    GL11.glNormal3f(0, 1, 0);
		    //top
		    GL11.glVertex3f(x, y, z);
		    GL11.glVertex3f(x, y, z + width);
		    GL11.glVertex3f(x + width, y, z + width);
		    GL11.glVertex3f(x + width, y, z);
		}
	    
	    
		if (world.getBlock(xx - 1, yy, zz) == 0) {
		    GL11.glNormal3f(1, 0, 0);
		    //left - Along x axis
		    GL11.glVertex3f(x, y, z);
		    GL11.glVertex3f(x, y, z + width);
		    GL11.glVertex3f(x, y - width, z + width);
		    GL11.glVertex3f(x, y - width, z);
		}
	    
		if (world.getBlock(xx + 1, yy, zz) == 0) {
		    
		    GL11.glNormal3f(-1, 0, 0);
		    //right
		    GL11.glVertex3f(x + width, y, z);
		    GL11.glVertex3f(x + width, y, z + width);
		    GL11.glVertex3f(x + width, y - width, z + width);
		    GL11.glVertex3f(x + width, y - width, z);
		}

	}
	
	public void draw(Chunk world) {
	    
		GL11.glColor3f(color.x,color.y,color.z);
		GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE);
		Cube.drawCube(pos.x, pos.y, pos.z, width, world, xx, yy, zz);

	}


}
