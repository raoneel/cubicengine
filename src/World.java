import java.util.ArrayList;
import java.util.HashMap;


public class World {
	
	ArrayList<Chunk> drawChunks;
	HashMap<String, Long> chunkSeeds;
	Player player;
	
	public World(Player player) {
		Chunk c = new Chunk(400, 255, 400);
		drawChunks = new ArrayList<Chunk>();
		drawChunks.add(c);
	}
	
	public void update(Player player) {
		
		
	}
	
	public void draw() {
		for (Chunk c : drawChunks) {
			c.draw();
		}
	}
	
	public void make() {
		for (Chunk c : drawChunks) {
			c.make();
		}
	}
    
}
