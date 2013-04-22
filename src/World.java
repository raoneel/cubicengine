import java.util.ArrayList;
import java.util.HashMap;


public class World {
	
	ArrayList<Chunk> drawChunks;
	HashMap<String, Long> chunkSeeds;
	Player player;
	
	public World(Player player) {
<<<<<<< HEAD
		Chunk c = new Chunk(400, 135, 400);
=======
		Chunk c = new Chunk(300, 125, 300);
>>>>>>> a9f82697745c6df000a17459422319db69e0783e
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
