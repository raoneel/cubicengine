import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.GL11;


public class Chunk {
	
	int[][][] worldArray;
	ArrayList<Cube> cubes;
	ArrayList<Cube> spawnedCubes;
	int x;
	int y;
	int z;
	Random random;
	public long seed;
	
	//The real world coordinate offset of the chunk
	int chunkX;
	int chunkZ;
	
	//The array indices of the chunk
	int arrayX;
	int arrayZ;
	
	//int chunkY;
	float heightMap[][];
	Noise noise;
	Cave cave;
	Forest forest;
	Water water;
	Cloud cloud;
	private int list;
	World world;
	float worldStep;
    int chunkSize;
    int height;
    int waterHeight;
	//float worldStepX;
	//float worldStepY;
	
	public Chunk(int x, int y, int z, World world) {
		this.world = world;
		worldArray = new int[x][y][z];
		cubes = new ArrayList<Cube>();
		spawnedCubes = new ArrayList<Cube>();
		this.x = x;
		this.y = y;
		this.z = z;
		//this.genSeed();
		random = new Random();
		this.worldStep = world.stepSize;
		//worldStepX = chunkX;
		//worldStepY = chunkZ;
		//this.setNoiseParam(y, 10);
		//this.noise.generateRandom();
        //= this.seed = System.currentTimeMillis();
		this.list = GL11.glGenLists(1);
        chunkSize = world.chunkX;
        height = world.chunkY-50;
        waterHeight = 25;
	}
	
	
	
	public String getChunkID() {
		return chunkX + "-" + chunkZ;
	}
	
	public void genSeed() {
		this.seed = (world.seed + " " + this.getChunkID()).hashCode();
		//System.out.println(world.seed);
	}
	
	public void genTerrain() {
		// Put all of the custom terrain functions in here

		cloud = new Cloud(this, 0.05f);
		random.setSeed(seed);
		cave = new Cave(this);
		forest = new Forest(this, 0.005f);
		cave.gen(0,10);
		
		cloud.genClouds();
        this.setNoiseParam(height,0);
		this.noise.createHeightMap();
		this.noise.setBlocks(this);
		this.water = new Water(this, 0.05f);
		this.water.genFloorWater(waterHeight);
		forest.genTrees();

	}
	
	public int topBlock(int x, int z) {
		for (int y = this.y; y > 0; y--) {
			if (this.getBlock(x, y, z) > 0 && this.getBlock(x, y, z) != CubeType.CLOUD) {
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
		
		
		//System.out.println(worldArray[x][y][z]);
		worldArray[x][y][z] = type;
	}
	public void removeBlock(int x, int y, int z){

		if (x < 0 || y < 0 || z < 0) {
			return;
		}
		
		if (x >= this.x || y >= this.y || z >= this.z) {
			return;
		}
		worldArray[x][y][z] = 0;
	}
    
	public void setNoiseParam(int heightInput,int floorInput){
		//System.out.println(world.stepSize);
		this.noise = new Noise(this.seed, this.y, floorInput, world.stepSize,this);
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
		this.genSeed();
		this.genTerrain();
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
        //for (Cube c: spawnedCubes){
        	//c.draw(this);
        //}
        GL11.glEnd();
        GL11.glEndList();
        //  isMade = true;
		//}
        
	}
	
	
	public void draw() {

         
		GL11.glCallList(list);
	}
	
	public int getWorldBlock(int x, int y, int z) {
		return world.getBlock(this, x, y, z);
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
        /*
		//Block is obscured
		if (!edge && getBlock(x+1, y, z) == 1 &&
            getBlock(x-1, y, z) == 1 &&
            getBlock(x, y+1, z) == 1 &&
            getBlock(x, y-1, z) == 1 &&
            getBlock(x, y, z+1) == 1 &&
            getBlock(x, y, z-1) == 1) {
			
			//HERE
			return worldArray[x][y][z];
			//return 0;
		}
		*/
		
		return worldArray[x][y][z];
        
        
	}
	
	public int getBlock(int x, int y, int z) {
		//Return 0 for out of bounds
		if (x < 0 || y < 0 || z < 0) {
			return -1;
		}
		
		if (x >= this.x || y >= this.y || z >= this.z) {
			return -1;
		}
		
		return worldArray[x][y][z];
        
	}
	
	public void genPlayerPosition(Player player){
		player.translate((200 * this.x) * chunkX,(200 * this.y),(200 * this.z) * chunkZ);
	}
	public void spawnCube(Cube newCube){
		boolean cubeFound = false;
		for (Cube c: cubes){
			  if(c.pos.x == newCube.pos.x && c.pos.y == newCube.pos.y && c.pos.z == newCube.pos.z){
				  System.out.println("CUBE ALREADY THERE");
				  cubeFound = true;
				  break;
			  }
		}
		if(!cubeFound){
			cubes.add(newCube);
			//genSeed();
			//make();
			//System.out.println("MADE");
			this.makeList();
			 System.out.println("BLOCK SPAWNED");
		}
		
		
	}
	public void destroyCube(int index){
		//this.make();
		cubes.remove(index);
		
		this.makeList();
	}
    
	
    
}
