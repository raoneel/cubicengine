import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;


public class World {
	
	ArrayList<Chunk> drawChunks;
	HashMap<String, Long> chunkSeeds;
	Player player;
	Chunk[][] chunkArray;
	Chunk[][] tempArray;
    
	int chunkX = 16;
	int chunkY = 100;
	int chunkZ = 16;
	int xPosCenter;
	int yPosCenter;
	int stepSize = 5;
	int displaySize = 21;
	public long seed;
	
	public World(Player player) {
		chunkSeeds = new HashMap<String, Long>();
		chunkArray = new Chunk[displaySize][displaySize];
		tempArray = new Chunk[displaySize][displaySize];
		this.initChunks(player.camera);
		
		xPosCenter = displaySize / 2;
		yPosCenter = displaySize / 2;
		chunkArray[xPosCenter][yPosCenter].genPlayerPosition(player);
		this.seed = System.currentTimeMillis();
		//this.stepSize = 3;
		//this.list = GL11.glGenLists(1);
        
	}
    
	/**
	 * Redraws the chunks, assuming that cx and cy are the chunk coordinates of the center chunk
	 * Later, optimize so that all 8 border chunks are not redrawn (we can probably re-use the old ones)
	 * @param cx New center chunk x
	 * @param cz New center chunk z
	 */
	public void redrawChunks(int cx, int cz) {
		for (int i = 0; i < displaySize; i++) {
			for (int j = 0; j < displaySize; j++) {
				
				if (j==xPosCenter & i == yPosCenter) {
					// Don't need to re-generate this since we are already here.
					continue;
				}
				
				Chunk newChunk = null;
				int newChunkX = cx + i - xPosCenter;
				int newChunkZ = cz + j - yPosCenter;
				
				// Check if the chunk is already in chunkArray, we don't need to regenerate it
				boolean doesExist = false;
				for (int k = 0; k < displaySize; k++) {
					for (int h = 0; h < displaySize; h++) {
						if (chunkArray[k][h].chunkX == newChunkX && chunkArray[k][h].chunkZ == newChunkZ) {
							doesExist = true;
							newChunk = chunkArray[k][h];
//							System.out.println("using old chunk");
							break;
						}
					}
					
					if (doesExist) {
						break;
					}
				}
				
				if (!doesExist) {
					newChunk = new Chunk(chunkX, chunkY, chunkZ, this);
					newChunk.chunkX = newChunkX;
					newChunk.chunkZ = newChunkZ;
					newChunk.genSeed();
					newChunk.make();
					newChunk.makeList();
					
				}
                
				
				if (chunkSeeds.containsKey(newChunk.getChunkID())) {
					newChunk.seed = chunkSeeds.get(newChunk.getChunkID());
				}
				else {
					chunkSeeds.put(newChunk.getChunkID(), newChunk.seed);
				}
				
				tempArray[i][j] = newChunk;
				
			}
		}
        
		for (int i = 0; i < displaySize; i++) {
			for (int j = 0; j <displaySize; j++) {
				chunkArray[i][j] = tempArray[i][j];
			}
		}
		
	}
	
	public void initChunks(Vector3f playerPosition) {
		int iteration = 0;
		int chunkOffsetX = 10000;
		int chunkOffsetY = 10000;
		
		for (int i = 0; i < displaySize; i++) {
			for (int j = 0; j < displaySize; j++) {
				iteration++;
				float loading = iteration / (displaySize * displaySize * 1.0f);
				System.out.println("Initializing Chunks: " + loading);
				
				Chunk newChunk = new Chunk(chunkX, chunkY, chunkZ, this);
				newChunk.chunkX = i + chunkOffsetX;
				newChunk.chunkZ = j + chunkOffsetY;
				newChunk.genSeed();
				
				chunkSeeds.put(newChunk.getChunkID(), newChunk.seed);
				chunkArray[i][j] = newChunk;
				
			}
		}
	}
	
	public int getBlock(Chunk chunk, int x, int y, int z) {
		return 1;
	}
	
	public void update(Player player, int list) {
		//this.list = list;
		//Find what chunk the player is in
		for (int i = 0; i < displaySize; i++) {
			for (int j = 0; j < displaySize; j++) {
				Chunk aChunk = chunkArray[i][j];
				if (aChunk.inChunk(player)) {
                    
                    
					if (!(i == xPosCenter && j == yPosCenter)) {
//						System.out.println("Updating chunks...");
						//System.out.println("Not in center");
						//We left the center chunk here, so we need to redraw everything
						//First, make the current chunk the center chunk
						tempArray[xPosCenter][yPosCenter] = aChunk;
						this.redrawChunks(aChunk.chunkX, aChunk.chunkZ);
						return;
					}
				}
			}
		}
		
	}
	
	
	public void draw() {
		for (int i = 0; i < displaySize; i++) {
			for (int j = 0; j < displaySize; j++) {
				Chunk aChunk = chunkArray[i][j];
				aChunk.draw();
			}
		}
	}
	
	public void make() {
		
		int iterations = 0;
		for (int i = 0; i < displaySize; i++) {
			for (int j = 0; j < displaySize; j++) {
				iterations++;
				float loading = iterations / (displaySize * displaySize * 1.0f);
				
				System.out.println("Making world: " + loading);
				Chunk aChunk = chunkArray[i][j];
				aChunk.make();
				aChunk.makeList();
			}
		}
	}
    
    
}
