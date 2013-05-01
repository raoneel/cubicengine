import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Cube {
	
	public Vector3f pos;
	public float width;
	public Vector3f color;
	public AABB aabb;
	int xx, yy, zz;
	public Random random;
	public int type = 0;
	
	public Cube(float x, float y, float z, float width, int type) {
		this.pos = new Vector3f();
		this.color = new Vector3f();
		pos.x = x;
		pos.y = y;
		pos.z = z;
		this.width = width;
		this.type = type;
		random = new Random();
		
		//34-139-34 grass
		//139-69-19 dirt
		// Give the cube a random color for now
//		this.color.x = (float) Math.random();
//		this.color.y = (float) Math.random();
//		this.color.z = (float) Math.random();
		

		this.refreshColor();
		makeAABB();
	}
	
	public void refreshColor() {
		if (type == 0 || type == 1) {
			this.color.x = CubeColor.GRASS.x + (float) Math.random() / 10;
			this.color.y = CubeColor.GRASS.y + (float) Math.random() / 10;
			this.color.z = CubeColor.GRASS.z + (float) Math.random() / 10;
		}
		else if (type == CubeType.DIRT) {
			this.setColor(CubeColor.DIRT);
		}
		else if (type == CubeType.SNOW) {
			this.setColor(CubeColor.SNOW);
		}
		else if (type == CubeType.LEAVES) {
			this.setColor(CubeColor.LEAVES);
		}
		else if (type == CubeType.WATER) {
			this.setColor(CubeColor.WATER);
		}
		else if (type == CubeType.CLOUD) {
			this.setColor(CubeColor.CLOUD);
		}
	}
	
	public void setColor(Vector3f newColor) {
		this.color.x = newColor.x + (float) Math.random() / 10;
		this.color.y = newColor.y + (float) Math.random() / 10;
		this.color.z = newColor.z + (float) Math.random() / 10;
	}
	
	
	private void makeAABB() {
		Vector3f pos = new Vector3f(this.pos.x + width/2.0f, this.pos.y - width/2.0f, this.pos.z + width/2.0f);
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
	public void drawCube(float x, float y, float z, float width, Chunk world, int xx, int yy, int zz) {
		// Give the cube a random color for now
		
//		GL11.glColor3f((float) Math.random(),(float) Math.random(),(float) Math.random());
		

		
		
		if (world.getBlock(xx, yy, zz - 1) < 1) {
			//front - Along z axis
			GL11.glNormal3f(0, 0, 1);
		    GL11.glVertex3f(x, y, z);
		    GL11.glVertex3f(x + width, y, z);
		    GL11.glVertex3f(x + width, y - width, z);
		    GL11.glVertex3f(x, y - width, z);
		}
		
		
		if (world.getBlock(xx, yy, zz + 1) < 1) {
			GL11.glNormal3f(0, 0, -1);
		    //back
		    GL11.glVertex3f(x, y, z + width);
		    GL11.glVertex3f(x + width, y, z + width);
		    GL11.glVertex3f(x + width, y - width, z + width);
		    GL11.glVertex3f(x, y - width, z + width);
		}
	    
		if (world.getBlock(xx, yy - 1, zz) < 1) {

			GL11.glNormal3f(0, -1, 0);
		    //bottom - Along y axis
		    GL11.glVertex3f(x, y - width, z);
		    GL11.glVertex3f(x, y - width, z + width);
		    GL11.glVertex3f(x + width, y - width, z + width);
		    GL11.glVertex3f(x + width, y - width, z);
		}

		if (world.getBlock(xx, yy + 1, zz)< 1) {


		    GL11.glNormal3f(0, 1, 0);
		    //top
		    GL11.glVertex3f(x, y, z);
		    GL11.glVertex3f(x, y, z + width);
		    GL11.glVertex3f(x + width, y, z + width);
		    GL11.glVertex3f(x + width, y, z);
		}

		if (world.getBlock(xx - 1, yy, zz) < 1) {
			
		    GL11.glNormal3f(1, 0, 0);
		    //left - Along x axis
		    GL11.glVertex3f(x, y, z);
		    GL11.glVertex3f(x, y, z + width);
		    GL11.glVertex3f(x, y - width, z + width);
		    GL11.glVertex3f(x, y - width, z);
		}
	    
		if (world.getBlock(xx + 1, yy, zz) < 1) {
			
		    GL11.glNormal3f(-1, 0, 0);
		    //right
		    GL11.glVertex3f(x + width, y, z);
		    GL11.glVertex3f(x + width, y, z + width);
		    GL11.glVertex3f(x + width, y - width, z + width);
		    GL11.glVertex3f(x + width, y - width, z);
		}

	}
	
	public void draw(Chunk world) {
		
		if (this.type == CubeType.WATER) {
			this.refreshColor();
		}
		GL11.glColor3f(color.x,color.y,color.z);
		GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_DIFFUSE);
		
		
		this.drawCube(pos.x, pos.y, pos.z, width, world, xx, yy, zz);

	}


}
