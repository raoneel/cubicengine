import java.util.Random;


public class Forest {
	
	Chunk chunk;
	Random random;
	float frequency = 0.05f;
	
	public Forest(Chunk chunk, float frequency) {
		this.chunk = chunk;
		random = new Random();
		this.frequency = frequency;
	}
	
	public void genTrees() {
		this.random.setSeed(chunk.seed + 4);
	    for (int x = 0; x < chunk.x; x++) {
	    	for (int z = 0; z < chunk.z; z++) {
	    		
	    		
	    		if (random.nextFloat() > frequency) {
	    			continue;
	    		}

	    		int start = chunk.topBlock(x, z);
	    		int randHeight = random.nextInt() % (chunk.y - start);
	    		for (int y = start; y < randHeight; y++) {
	    			chunk.setBlock(x, y, z, CubeType.DIRT);
	    			
	    			if (y == randHeight - 1) {
	    				chunk.setBlock(x,y,z,CubeType.GRASS);
	    				
	    				chunk.setBlock(x,y - 1,z,CubeType.GRASS);
	    				chunk.setBlock(x+1,y - 1,z,CubeType.GRASS);
	    				chunk.setBlock(x-1,y - 1,z,CubeType.GRASS);
	    				chunk.setBlock(x,y - 1,z-1,CubeType.GRASS);
	    				chunk.setBlock(x,y - 1,z+1,CubeType.GRASS);
	    				
	    				chunk.setBlock(x,y - 2,z,CubeType.GRASS);
	    				chunk.setBlock(x+1,y - 2,z,CubeType.GRASS);
	    				chunk.setBlock(x-1,y - 2,z,CubeType.GRASS);
	    				chunk.setBlock(x,y - 2,z-1,CubeType.GRASS);
	    				chunk.setBlock(x,y - 2,z+1,CubeType.GRASS);

	    			}
	    		}
	    		
	    	}
	    }
	}

}
