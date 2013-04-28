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
	int chunkZ;
	float heightMap[][];
	Noise noise;
	Cave cave;
	Forest forest;
	private int list;
	
	public Chunk(int x, int y, int z) {
		worldArray = new int[x][y][z];
		cubes = new ArrayList<Cube>();
		this.x = x;
		this.y = y;
		this.z = z;
		random = new Random();
		//this.heightMap[][];// = new float[255][255];
//	    this.seed = System.currentTimeMillis();
//		this.seed = 127;
	    this.seed = System.currentTimeMillis();
//		this.seed = 127;
		this.list = GL11.glGenLists(1);
        
	}
	
	public String getChunkID() {
		return chunkX + "-" + chunkZ;
	}
	
	public void genTerrain() {
		// Put all of the custom terrain functions in here
		
		cave = new Cave(this);
		forest = new Forest(this, 0.02f);
		cave.gen(0,10);
		forest.genTrees();
		this.setNoiseParam(x, y, 10, 0.05f);
		this.noise.createHeightMap();
		this.noise.setBlocks(this);
		
	}
	
	public int topBlock(int x, int z) {
		for (int y = this.y; y > 0; y--) {
			if (this.getBlock(x, y, z) == 1) {
				return y;
			}
		}
		
		return 0;
	}
	
	public void setBlock(int x, int y, int z, int type) {
		//Use this for testing only, draws columns given a y coordinate
		//System.out.println(this.heightMap[x][z]);
        //		for (int i = 0; i < this.heightMap[x][z]; i++) {
        //			worldArray[x][i][z] = type;
        //		}
		
		if (x < 0 || y < 0 || z < 0) {
			return;
		}
		
		if (x >= this.x || y >= this.y || z >= this.z) {
			return;
		}
		
		
		
		worldArray[x][y][z] = type;
	}
    
	public void setNoiseParam(int sizeInput, int heightInput, int floorInput, float frequency){
		this.noise = new Noise(sizeInput, this.seed, heightInput, floorInput, frequency);
	}
	
	public boolean inChunk(Player player) {
		float playerX = player.camera.x;
		float playerZ = player.camera.z;
		
		
		if (playerX < (200 * this.x) * chunkX || playerX > (this.x-1) * 200 + (200 * this.x) * chunkX) {
			return false;
		}
		
		if (playerZ < (200 * this.z) * chunkZ || playerZ > (this.z - 1) * 200 + (200 * this.z) * chunkZ) {
			return false;
		}
		return true;
	}
    
	
	public void make() {
	    for (int i = 0; i < this.x; i++) {
	    	
	    	for (int j = 0; j < this.y; j++) {
	    		
	    		for (int k = 0; k < this.z;k++) {
	    			
	    			if (this.drawGetBlock(i, j, k) > 0) {
	    				//Offset the cubes by the chunk offset
		    			Cube c = new Cube(i * 200 + (200 * this.x) * chunkX, j * 200, k * 200 + (200 * this.z) * chunkZ, 200,this.getBlock(i, j, k) );
		    			c.xx = i;
		    			c.yy = j;
		    			c.zz = k;

		    			
		    			this.cubes.add(c);
	    			}
                    
                    
	    		}
	    	}
            
	    }
        
	}
	
	public void makeList(){
		//System.out.println(isMade);
		//if (!isMade){
        GL11.glNewList(list, GL11.GL_COMPILE);
        GL11.glBegin(GL11.GL_QUADS);
        for (Cube c: cubes){
            c.draw(this);
        }
        GL11.glEnd();
        GL11.glEndList();
        //  isMade = true;
		//}
        
	}
	
	
	public void draw() {
		/*
         for (Cube c : cubes) {
         c.draw(this);
         }
         */
		GL11.glCallList(list);
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
		
        //		if (x*y*z == 0) {
        //			return 0;
        //		}
        //
		
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
        
		//Block is obscured
		if (!edge && getBlock(x+1, y, z) == 1 &&
            getBlock(x-1, y, z) == 1 &&
            getBlock(x, y+1, z) == 1 &&
            getBlock(x, y-1, z) == 1 &&
            getBlock(x, y, z+1) == 1 &&
            getBlock(x, y, z-1) == 1) {
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
	
	public void genPlayerPosition(Player player){
		player.translate((200 * this.x) * chunkX,0,(200 * this.z) * chunkZ);
	}
    
	
    
}
