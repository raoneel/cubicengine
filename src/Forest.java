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
		int numTrees = 5;
		
		for (int i = 0; i < numTrees; i++) {
			int randomX = random.nextInt() % chunk.x;
			int randomZ = random.nextInt() % chunk.z;
			
			int start = chunk.topBlock(randomX, randomZ);
			
			if (chunk.getBlock(randomX, start, randomZ) == CubeType.WATER) {
				continue;
			}
			
			int randHeight = random.nextInt() % (chunk.y - start);
			for (int y = start; y < randHeight; y++) {
				chunk.setBlock(randomX, y, randomZ, CubeType.DIRT);
				
				if (y == randHeight - 1) {
					chunk.setBlock(randomX,y,randomZ,CubeType.LEAVES);
					
					chunk.setBlock(randomX,y - 1,randomZ,CubeType.LEAVES);
					chunk.setBlock(randomX+1,y - 1,randomZ,CubeType.LEAVES);
					chunk.setBlock(randomX-1,y - 1,randomZ,CubeType.LEAVES);
					chunk.setBlock(randomX,y - 1,randomZ-1,CubeType.LEAVES);
					chunk.setBlock(randomX,y - 1,randomZ+1,CubeType.LEAVES);
					
					chunk.setBlock(randomX,y - 2,randomZ,CubeType.LEAVES);
					chunk.setBlock(randomX+1,y - 2,randomZ,CubeType.LEAVES);
					chunk.setBlock(randomX-1,y - 2,randomZ,CubeType.LEAVES);
					chunk.setBlock(randomX,y - 2,randomZ-1,CubeType.LEAVES);
					chunk.setBlock(randomX,y - 2,randomZ+1,CubeType.LEAVES);
					
		    		for (int j = randomX - 2; j < randomX + 2; j++) {
		    			for (int k = randomZ - 2; k < randomZ+2; k++) {
			    			for (int l = y - 2; l < y+2; l++) {
			    				if (random.nextFloat() > 0.8f) {
			    					chunk.setBlock(j,l,k,CubeType.LEAVES);
			    				}
			    				
			    			}
		    			}
		    		}

				}
			}
			
		}
		


	}

}
