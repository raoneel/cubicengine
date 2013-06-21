import java.util.Random;

public class Forest {
	
	Chunk chunk;
	Random random;
	float frequency = 0.05f;
	int treeHeight = 50;
	
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
	    		randHeight = randHeight % treeHeight;
	    		if (chunk.getBlock(x, start, z) == CubeType.WATER) {
					continue;
				}
	    		for (int y = start; y < randHeight; y++) {
	    			chunk.setBlock(x, y, z, CubeType.WOOD);
	    			
	    			if (y == randHeight - 1) {
	    				chunk.setBlock(x,y,z,CubeType.LEAVES);
	    				
	    				chunk.setBlock(x,y - 1,z,CubeType.LEAVES);
	    				chunk.setBlock(x+1,y - 1,z,CubeType.LEAVES);
	    				chunk.setBlock(x-1,y - 1,z,CubeType.LEAVES);
	    				chunk.setBlock(x,y - 1,z-1,CubeType.LEAVES);
	    				chunk.setBlock(x,y - 1,z+1,CubeType.LEAVES);
	    				
	    				chunk.setBlock(x,y - 2,z,CubeType.LEAVES);
	    				chunk.setBlock(x+1,y - 2,z,CubeType.LEAVES);
	    				chunk.setBlock(x-1,y - 2,z,CubeType.LEAVES);
	    				chunk.setBlock(x,y - 2,z-1,CubeType.LEAVES);
	    				chunk.setBlock(x,y - 2,z+1,CubeType.LEAVES);

	    				
			    		for (int j = x - 2; j < x + 2; j++) {
			    			for (int k =z - 2; k < z+2; k++) {
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

}
/*
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
			int randomX = random.nextInt() % (chunk.x - 2) + 1;
			int randomZ = random.nextInt() % (chunk.z - 2) + 1;
			
			int start = chunk.topBlock(randomX, randomZ);
			
			if (chunk.getBlock(randomX, start, randomZ) == CubeType.WATER) {
				continue;
			}
			
			int randHeight = random.nextInt() % (chunk.y - start);
		
			if (randHeight - start < 2){
				continue;
			}
			//System.out.println(randHeight-start);
			//System.out.println("START");
			int counter = 0;
			for (int y = start; y < randHeight; y++) {
				//System.out.println("MAKE TRUNK");
				counter++;
				chunk.setBlock(randomX, y, randomZ, CubeType.DIRT);
				//System.out.println(y);
				if (y == randHeight - 1) {
					if (counter < 5){
						break;
					}
					
				//System.out.println("MAKE LEAVES");
					//System.out.println(counter);
					//counter = 0;
					
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
*/
