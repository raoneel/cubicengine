import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;


public class World {
	
	ArrayList<Chunk> drawChunks;
	HashMap<String, Long> chunkSeeds;
	Player player;
	Chunk[][] chunkArray;
	
	int chunkX = 10;
	int chunkY = 10;
	int chunkZ = 10;
	
	private int list;
	
	public World(Player player) {
		chunkArray = new Chunk[3][3];
		this.initChunks(player.camera);
		

	}
	
	public void redrawDisplayList() {

	    GL11.glNewList(list, GL11.GL_COMPILE);
	    // Begin drawing
	    GL11.glBegin(GL11.GL_QUADS);
        
		
	    this.draw();
        //	    world.drawFloor();
	    
	    GL11.glEnd();
	    GL11.glEndList();
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
					continue;
				}
				
				Chunk newChunk = new Chunk(chunkX, chunkY, chunkZ);
				newChunk.chunkX = cx + i - 1;
				newChunk.chunkZ = cz + j - 1;
				chunkArray[i][j] = newChunk;
				
			}
		}
		
		this.genTerrain();
		this.make();
		this.redrawDisplayList();
	}
	
	public void initChunks(Vector3f playerPosition) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				Chunk newChunk = new Chunk(chunkX, chunkY, chunkZ);
				newChunk.chunkX = i;
				newChunk.chunkZ = j;
				chunkArray[i][j] = newChunk;
				
			}
		}
	}
	
	public void update(Player player, int list) {
		this.list = list;
		//Find what chunk the player is in
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				Chunk aChunk = chunkArray[i][j];
				
				
				
				if (aChunk.inChunk(player)) {
					System.out.println("in chunk: " + i + "- " + j);
					if (!(i == 1 && j == 1)) {
						System.out.println("Not in center");
						//We left the center chunk here, so we need to redraw everything
						//First, make the current chunk the center chunk
						chunkArray[1][1] = aChunk;
						this.redrawChunks(aChunk.chunkX, aChunk.chunkZ);
						return;
					}
				}
			}
		}
		
	}
	
	public void genTerrain() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				Chunk aChunk = chunkArray[i][j];
				aChunk.genTerrain();
			}
		}
	}
	
	public void draw() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				Chunk aChunk = chunkArray[i][j];
				aChunk.draw();
			}
		}
	}
	
	public void make() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				Chunk aChunk = chunkArray[i][j];
				aChunk.make();
			}
		}
	}
    
}
