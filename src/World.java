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
	int chunkY = 128;
	int chunkZ = 16;
	int xPosCenter;
	int yPosCenter;
	int stepSize = 3;
	int displaySize = 17;
	public long seed;
	
	public World(Player player) {
		chunkSeeds = new HashMap<String, Long>();
		chunkArray = new Chunk[displaySize][displaySize];
		tempArray = new Chunk[displaySize][displaySize];
		this.initChunks(player.camera);
		
		xPosCenter = displaySize / 2;
		yPosCenter = displaySize / 2;
		chunkArray[xPosCenter][yPosCenter].genPlayerPosition(player);
		//this.seed = System.currentTimeMillis();
		this.seed = 123423L;
		//this.stepSize = 3;
		//this.list = GL11.glGenLists(1);
        
	}
    
	/**
	 * Redraws the chunks, assuming that cx and cy are the chunk coordinates of the center chunk
	 * Later, optimize so that all 8 border chunks are not redrawn (we can probably re-use the old ones)
	 * @param cx New center chunk x
	 * @param cz New center chunk z
	 */
	public void redrawChunks(int cx, int cz, int offsetX, int offsetZ) {
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
					
				}
                
				
				if (chunkSeeds.containsKey(newChunk.getChunkID())) {
					newChunk.seed = chunkSeeds.get(newChunk.getChunkID());
				}
				else {
					chunkSeeds.put(newChunk.getChunkID(), newChunk.seed);
				}
				
				newChunk.arrayX = i;
				newChunk.arrayZ = j;
				tempArray[i][j] = newChunk;
				
			}
		}
        
		for (int i = 0; i < displaySize; i++) {
			for (int j = 0; j <displaySize; j++) {
				chunkArray[i][j] = tempArray[i][j];
			}
		}
		
		if (offsetX - xPosCenter == 1) {
			System.out.println("optimizing");
			for (int j = 0; j < displaySize; j++) {
				chunkArray[displaySize - 1][j].makeList();
				chunkArray[displaySize - 2][j].makeList();
			}
		}
		
		if (offsetX - xPosCenter == -1) {
			System.out.println("optimizing");
			for (int j = 0; j < displaySize; j++) {
				chunkArray[0][j].makeList();
				chunkArray[1][j].makeList();
			}
		}
		
		if (offsetZ - yPosCenter == -1) {
			System.out.println("optimizing");
			for (int j = 0; j < displaySize; j++) {
				chunkArray[j][0].makeList();
				chunkArray[j][1].makeList();
			}
		}
		
		if (offsetZ - yPosCenter == 1) {
			System.out.println("optimizing");
			for (int j = 0; j < displaySize; j++) {
				chunkArray[j][displaySize - 1].makeList();
				chunkArray[j][displaySize - 2].makeList();
			}
		}
		
//		makeList();
		
	}
	
	public void initChunks(Vector3f playerPosition) {
		int iteration = 0;
		int chunkOffsetX = 1000;
		int chunkOffsetY = 1000;
		
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
				newChunk.arrayX = i;
				newChunk.arrayZ = j;
				chunkArray[i][j] = newChunk;
				
			}
		}
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
//						System.out.println("In chunk " + i + "-" + j);
						//We left the center chunk here, so we need to redraw everything
						//First, make the current chunk the center chunk
						tempArray[xPosCenter][yPosCenter] = aChunk;
						aChunk.arrayX = xPosCenter;
						aChunk.arrayZ = yPosCenter;
						this.redrawChunks(aChunk.chunkX, aChunk.chunkZ, i, j);
						return;
					}
				}
			}
		}
		
	}
	
	/** Allows chunks to access blocks from other chunks, especially for optimizing performance
	 * for face occlusion.
	 * @param chunk
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public int getBlock(Chunk chunk, int x, int y, int z) {
		if (x >= 0 && x < chunk.x && y >= 0 && y < chunk.y && z >= 0 && z < chunk.z) {
			return chunk.getBlock(x, y, z);
		}
		else {
			
			int newX = x;
			int newY = y;
			int newZ = z;
			
			int chunkX = chunk.arrayX;
			int chunkZ = chunk.arrayZ;
			
			if (x >= chunk.x) {
				newX = x - chunk.x;
				chunkX += 1;
			}
			else if (x < 0) {
				newX = chunk.x + x;
				chunkX -= 1;
			}
			
			if (z >= chunk.z) {
				newZ = z - chunk.z;
				chunkZ += 1;
//				return 1;
			}
			else if (z < 0) {
				newZ = chunk.z + z;
				chunkZ -= 1;
//				return 1;
			}
			
			if (chunkX >= displaySize || chunkX < 0 || chunkZ < 0 || chunkZ >= displaySize) {
				return 1;
			}
			
			if (y < 0) {
				return 1;
			}
			else if (y >= this.chunkY) {
				return -1;
			}
			
//			return -1;
			return chunkArray[chunkX][chunkZ].getBlock(newX, newY, newZ);
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
	
	public void makeList() {
		int iterations = 0;
		for (int i = 0; i < displaySize; i++) {
			for (int j = 0; j < displaySize; j++) {
				iterations++;
				float loading = iterations / (displaySize * displaySize * 1.0f);
				
//				System.out.println("Caching world: " + loading);
				Chunk aChunk = chunkArray[i][j];
				aChunk.makeList();
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
		
		makeList();
		
	}
    
    
}
