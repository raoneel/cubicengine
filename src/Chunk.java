import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.GL11;


public class Chunk {
	
	int[][][] worldArray;
	ArrayList<Cube> cubes;
	int x;
	int y;
	int z;
	Random random;
	public long seed;
	
	int chunkX;
	int chunkY;
	
	public Chunk(int x, int y, int z) {
		worldArray = new int[x][y][z];
		cubes = new ArrayList<Cube>();
		this.x = x;
		this.y = y;
		this.z = z;
		random = new Random();
	    this.seed = System.currentTimeMillis();
	}
	
	public String getChunkID() {
		return chunkX + "-" + chunkY;
	}
	
	public void setBlock(int x, int y, int z, int type) {
		//Use this for testing only, draws columns given a y coordinate
		for (int i = 0; i < y; i++) {
			worldArray[x][i][z] = type;
		}
		
		//worldArray[x][y][z] = type;
	
	}
	
	public void make() {
	    for (int i = 0; i < this.x; i++) {
	    	
	    	for (int j = 0; j < this.y; j++) {
	    		
	    		for (int k = 0; k < this.z;k++) {
	    			
	    			if (this.drawGetBlock(i, j, k) == 1) {
		    			Cube c = new Cube(i * 200, j * 200, k * 200, 200);
		    			c.xx = i;
		    			c.yy = j;
		    			c.zz = k;
		    			this.cubes.add(c);
	    			}


	    		}
	    	}

	    }

	}
	
	
	public void draw() {
		for (Cube c : cubes) {
			c.draw(this);
		}
	}

	
	/**
	 * Just like getBlock but returns 0 for blocks that are completely obscured
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public int drawGetBlock(int x, int y, int z) {
		//Return 0 for out of bounds
		if (x < 0 || y < 0 || z < 0) {
			return 0;
		}
		
		if (x >= this.x || y >= this.y || z >= this.z) {
			return 0;
		}
		
		
		// check if the block is visible
		boolean edge = false;
		if (x - 1 < 0 || x+1 > this.x -1) {
			edge = true;
		}
		if (y - 1 < 0 || y+1 > this.y -1) {
			edge = true;
		}
		if (z - 1 < 0 || z+1 > this.z -1) {
			edge = true;
		}
		
		//Block is completely obscured
		if (!edge && worldArray[x+1][y][z] == 1 &&
				worldArray[x-1][y][z] == 1 &&
				worldArray[x][y+1][z] == 1 &&
				worldArray[x][y-1][z] == 1 &&
				worldArray[x][y][z+1] == 1 &&
				worldArray[x][y][z-1] == 1) {
			return 0;
		}
		
		
		return worldArray[x][y][z];
	
	}
	
	public int getBlock(int x, int y, int z) {
		//Return 0 for out of bounds
		if (x < 0 || y < 0 || z < 0) {
			return 0;
		}
		
		if (x >= this.x || y >= this.y || z >= this.z) {
			return 0;
		}
		
		
		return worldArray[x][y][z];
	}

}
