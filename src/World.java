import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;


public class World {
	
	ArrayList<Chunk> drawChunks;
	HashMap<String, Long> chunkSeeds;
	Player player;
	Chunk[][] chunkArray;
	Chunk[][] displayArray;
	

	int chunkX = 50;
	int chunkY = 50;
	int chunkZ = 50;
	int xPos;
	int yPos;
	int worldSize = 10;
	private int list;
	
	public World(Player player) {
		chunkSeeds = new HashMap<String, Long>();
		chunkArray = new Chunk[worldSize][worldSize];
		displayArray = new Chunk[3][3];
		this.initChunks(player.camera);
		xPos = worldSize / 2;
		yPos = worldSize / 2;
		chunkArray[xPos][yPos].genPlayerPosition(player);
		
		//this.list = GL11.glGenLists(1);
        
	}
	
	public void redrawDisplayList() {
		this.draw();
		/*
         GL11.glNewList(list, GL11.GL_COMPILE);
         // Begin drawing
         GL11.glBegin(GL11.GL_QUADS);
         
         
         this.draw();
         //	    world.drawFloor();
         
         GL11.glEnd();
         GL11.glEndList();
         */
	}
	
	
	/**
	 * Redraws the chunks, assuming that cx and cy are the chunk coordinates of the center chunk
	 * Later, optimize so that all 8 border chunks are not redrawn (we can probably re-use the old ones)
	 * @param cx
	 * @param cz
	 */
	public void redrawChunks(int cx, int cz) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				
				if (j==1 & i == 1) {
					// Don't need to re-generate this since we are already here.
					//continue;
				}
				//System.out.println(cx + i - 1);
				displayArray[i][j] = chunkArray[cx + i - 1][cz + j - 1];
				//Chunk newChunk = new Chunk(chunkX, chunkY, chunkZ);
				//newChunk.chunkX = cx + i - 1;
				//newChunk.chunkZ = cz + j - 1;
				//chunkArray[i][j] = newChunk;
				

				Chunk newChunk = new Chunk(chunkX, chunkY, chunkZ);
				newChunk.chunkX = cx + i - 1;
				newChunk.chunkZ = cz + j - 1;
				
				if (chunkSeeds.containsKey(newChunk.getChunkID())) {
					newChunk.seed = chunkSeeds.get(newChunk.getChunkID());
				}
				else {
					chunkSeeds.put(newChunk.getChunkID(), newChunk.seed);
				}
				
				chunkArray[i][j] = newChunk;

				
			}
		}
		//this.draw();
//		this.genTerrain();
//		this.make();
		//this.redrawDisplayList();
	}
	
	public void initChunks(Vector3f playerPosition) {
		int iteration = 0;
		for (int i = 0; i < worldSize; i++) {
			for (int j = 0; j < worldSize; j++) {
				iteration++;
				float loading = iteration / (worldSize * worldSize * 1.0f);
				
				
				System.out.println("Initializing Chunks: " + loading);
				
				Chunk newChunk = new Chunk(chunkX, chunkY, chunkZ);
				newChunk.chunkX = i;
				newChunk.chunkZ = j;
				
				chunkSeeds.put(newChunk.getChunkID(), newChunk.seed);
				chunkArray[i][j] = newChunk;
				
			}
		}
	}
	
	public void update(Player player, int list) {
		//this.list = list;
		//Find what chunk the player is in
		for (int i = 0; i < worldSize; i++) {
			for (int j = 0; j < worldSize; j++) {
				Chunk aChunk = chunkArray[i][j];
				if (aChunk.inChunk(player)) {

					//System.out.println("in chunk: " + i + "- " + j);
					if (!(i == xPos && j == yPos)) {
						//System.out.println("Not in center");
						//We left the center chunk here, so we need to redraw everything
						//First, make the current chunk the center chunk
						displayArray[1][1] = aChunk;
						this.redrawChunks(aChunk.chunkX, aChunk.chunkZ);
						return;
					}
				}
			}
		}
		
	}
	
	public void genTerrain() {
		for (int i = 0; i < worldSize; i++) {
			for (int j = 0; j < worldSize; j++) {
				Chunk aChunk = chunkArray[i][j];
				aChunk.genTerrain();
			}
		}
	}
	
	public void draw() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				Chunk aChunk = displayArray[i][j];
				aChunk.draw();
			}
		}
	}
	
	public void make() {
		int iterations = 0;
		for (int i = 0; i < worldSize; i++) {
			for (int j = 0; j < worldSize; j++) {
				iterations++;
				float loading = iterations / (worldSize * worldSize * 1.0f);
				
				System.out.println("Making world: " + loading);
				Chunk aChunk = chunkArray[i][j];
				aChunk.make();
				aChunk.makeList();
			}
		}
	}
	
	public void initDisplay(){
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				displayArray[i][j] = chunkArray[xPos+i-1][yPos+j-1];
			}
		}
	}
    
}
