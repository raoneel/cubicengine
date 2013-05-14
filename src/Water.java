import java.util.ArrayList;
import java.util.Random;


public class Water {
	
	class Point {
		int x;
		int y;
		int z;
		public Point(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}
	
	Chunk chunk;
	Random random;
	
	/** Probability that chunk will have water in it **/
	float frequency = 0.05f;
	
	
	public Water(Chunk chunk, float frequency) {
		this.chunk = chunk;
		random = new Random();
		this.frequency = frequency;
	}
	
	public void genFloorWater(int floor) {
		for (int x = 0; x < chunk.x; x++) {
			for (int z = 0; z < chunk.z; z++) {
				int topBlock = chunk.topBlock(x, z) + 1;
				if (topBlock < floor) {
					for (int y = topBlock; y < floor; y++) {
						chunk.setBlock(x, y, z, CubeType.WATER);
					}
					
				}
			}
		}
	}
	
	public void genWater() {
		random.setSeed(chunk.seed);
		
		int randomX = random.nextInt() % chunk.x;
		int randomZ = random.nextInt() % chunk.z;
		int waterY = chunk.topBlock(randomX, randomZ) + 1;
		chunk.setBlock(randomX, waterY, randomZ, CubeType.WATER);
		
		ArrayList<Point> fringe = new ArrayList<Point>();
		ArrayList<Point> toAdd = new ArrayList<Point>();
		fringe.add(new Point(randomX, waterY, randomZ));
		
		
		boolean action = true;
		int maxDepth = 30;
		int depth = 0;
		while (action) {
			depth++;
			
			if (depth > maxDepth) {
				break;
			}
			action = false;
			for (Point water : fringe) {
				
				// Water falls if nothing is below it
				if (chunk.getBlock(water.x, water.y - 1, water.z) == 0) {
					action = true;
					int y = chunk.topBlock(water.x, water.z);
					chunk.setBlock(water.x, y, water.z, CubeType.WATER);

					continue;
				}
				
				
				if (chunk.getBlock(water.x + 1, water.y, water.z) == 0) {
					action = true;
					chunk.setBlock(water.x + 1, water.y, water.z, CubeType.WATER);
					toAdd.add(new Point(water.x + 1, water.y, water.z));
				}
				
				if (chunk.getBlock(water.x -1, water.y, water.z) == 0) {
					action = true;
					chunk.setBlock(water.x - 1, water.y, water.z, CubeType.WATER);
					toAdd.add(new Point(water.x - 1, water.y, water.z));
				}
				
				if (chunk.getBlock(water.x, water.y, water.z + 1) == 0) {
					action = true;
					chunk.setBlock(water.x, water.y, water.z + 1, CubeType.WATER);
					toAdd.add(new Point(water.x, water.y, water.z + 1));
				}
				
				if (chunk.getBlock(water.x, water.y, water.z - 1) == 0) {
					action = true;
					chunk.setBlock(water.x, water.y, water.z - 1, CubeType.WATER);
					toAdd.add(new Point(water.x, water.y, water.z - 1));
				}

			}
			
			for (Point p : toAdd) {
				fringe.add(p);
			}
			toAdd.clear();
		}
		
	}

}
