import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.GL11;


public class World {
	
	int[][][] worldArray;
	ArrayList<Cube> cubes;
	int x;
	int y;
	int z;
	Random random;
	public long seed;
	
	public World(int x, int y, int z) {
		worldArray = new int[x][y][z];
		cubes = new ArrayList<Cube>();
		this.x = x;
		this.y = y;
		this.z = z;
		random = new Random();
	    this.seed = System.currentTimeMillis();
	}
	
	public void setBlock(int x, int y, int z, int type) {
		worldArray[x][y][z] = type;
	}
	
	public void make() {
	    for (int i = 0; i < this.x; i++) {
	    	
	    	for (int j = 0; j < this.y; j++) {
	    		
	    		for (int k = 0; k < this.z;k++) {
	    			
	    			if (this.getBlock(i, j, k) == 1) {
		    			Cube c = new Cube(i * 200, j * 200, k * 200, 200);
		    			this.cubes.add(c);
	    			}


	    		}
	    	}

	    }

	}
	
	public void genFloor() {
		
	    for (int x = 0; x < this.x; x++) {
	    	for (int z = 0; z < this.z; z++) {
	    		this.setBlock(x, 0, z, 1);
	    	}
	    }
	}
	
	public void genCeiling() {
		
	    for (int x = 0; x < this.x; x++) {
	    	for (int z = 0; z < this.z; z++) {
	    		this.setBlock(x, this.y - 1, z, 1);
	    	}
	    }
	}
	
	public void genBlock() {
		this.setBlock(0,0,0,1);
	}
	
	public void drawWorld() {
//		random.setSeed(this.seed);
//	    for (int x = 0; x < this.x; x++) {
//	    	for (int z = 0; z < this.z; z++) {
//	    		for (int y = 0; y < this.y; y++) {
//	    			
//	    			if (this.getBlock(x, y, z) == 1) {
//	    				
//	    				GL11.glColor3f(random.nextFloat(), random.nextFloat(), random.nextFloat());
//	    				Cube.drawCube(x * 200, y*200, z*200, 200);
//	    			}
//	    		}
//	    	}
//	    }
		for (Cube c : cubes) {
			c.draw();
		}
	}
	
	public void genShell() {
	    for (int i = 0; i < this.x; i++) {
	    	
	    	for (int j = 0; j < this.y; j++) {
	    		
	    		for (int k = 0; k < this.z;k++) {

	    			if (i == 0 || j == 0) {
	    				this.setBlock(i, j, k, 1);
	    			}
	    			if (i == this.x - 1 || j == this.y - 1 ) {
	    				this.setBlock(i, j, k, 1);
	    			}
	    			

	    		}
	    	}

	    }
	}
	
	public void initCave() {
		random.setSeed(seed);
	    for (int i = 0; i < this.x; i++) {
	    	
	    	for (int j = 0; j < this.y; j++) {
	    		
	    		for (int k = 0; k < this.z;k++) {

	    			if (random.nextFloat() > 0.5) {
	    				this.setBlock(i, j, k, 1);
	    			}
	    			

	    		}
	    	}

	    }
	}
	
	public void genCave3D() {
		int[][][] newWorld = new int[this.x][this.y][this.z];
	    
	    for (int i = 1; i < this.x - 1; i++) {
	    	
	    	for (int j = 1; j < this.y - 1; j++) {
	    		
	    		for (int k = 1; k < this.z - 1;k++) {

	    			int score = 
	    				this.getBlock(i + 1, j + 1, k) +
	    				this.getBlock(i + 1, j, k) +
	    				this.getBlock(i + 1, j - 1, k) +
	    				this.getBlock(i, j + 1, k) + 
	    				this.getBlock(i, j - 1, k) +  
	    				this.getBlock(i - 1, j - 1, k) + 
	    				this.getBlock(i - 1, j, k) + 
	    				this.getBlock(i - 1, j + 1, k);
	    			
	    			score += 
	    				this.getBlock(i,j,k+1) +
		    			this.getBlock(i + 1, j + 1, k+1) +
	    				this.getBlock(i + 1, j, k+1) +
	    				this.getBlock(i + 1, j - 1, k+1) +
	    				this.getBlock(i, j + 1, k+1) + 
	    				this.getBlock(i, j - 1, k+1) +  
	    				this.getBlock(i - 1, j - 1, k+1) + 
	    				this.getBlock(i - 1, j, k+1) + 
	    				this.getBlock(i - 1, j + 1, k+1);
	    			
	    			score += 
	    				this.getBlock(i,j,k-1) +
		    			this.getBlock(i + 1, j + 1, k-1) +
	    				this.getBlock(i + 1, j, k-1) +
	    				this.getBlock(i + 1, j - 1, k-1) +
	    				this.getBlock(i, j + 1, k-1) + 
	    				this.getBlock(i, j - 1, k-1) +  
	    				this.getBlock(i - 1, j - 1, k-1) + 
	    				this.getBlock(i - 1, j, k-1) + 
	    				this.getBlock(i - 1, j + 1, k-1);
	    			
	    			
	    			
	    			if (score < 13) {
	    				newWorld[i][j][k] = 0;
	    			}
	    			else if (score == 13) {
	    				newWorld[i][j][k] = this.getBlock(i, j, k);
	    			}
	    			else if (score > 13) {
	    				newWorld[i][j][k] = 1;
	    			}
	    			

	    		}
	    	}

	    }
	    
	    this.worldArray = newWorld;
	}
	
	public void genCave2D() {
		int[][][] newWorld = new int[this.x][this.y][this.z];
	    
	    for (int i = 1; i < this.x - 1; i++) {
	    	
	    	for (int j = 1; j < this.y - 1; j++) {
	    		
	    		for (int k = 0; k < this.z;k++) {

	    			int score = this.getBlock(i + 1, j + 1, k) +
	    				this.getBlock(i + 1, j, k) +
	    				this.getBlock(i + 1, j - 1, k) +
	    				this.getBlock(i, j + 1, k) + 
	    				this.getBlock(i, j - 1, k) +  
	    				this.getBlock(i - 1, j - 1, k) + 
	    				this.getBlock(i - 1, j, k) + 
	    				this.getBlock(i - 1, j + 1, k);
	    			
	    			if (score < 4) {
	    				newWorld[i][j][k] = 0;
	    			}
	    			else if (score == 4) {
	    				newWorld[i][j][k] = this.getBlock(i, j, k);
	    			}
	    			else if (score > 4) {
	    				newWorld[i][j][k] = 1;
	    			}
	    			

	    		}
	    	}

	    }
	    
	    this.worldArray = newWorld;
	}
	
	public int getBlock(int x, int y, int z) {
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

}
