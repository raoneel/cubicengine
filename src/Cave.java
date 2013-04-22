
public class Cave {
	
	Chunk chunk;
	
	public Cave(Chunk chunk) {
		this.chunk = chunk;
	}
	
	public void genFloor() {
		
	    for (int x = 0; x < chunk.x; x++) {
	    	for (int z = 0; z < chunk.z; z++) {
	    		chunk.setBlock(x, 0, z, 1);
	    	}
	    }
	}
	
	public void genCeiling() {
		
	    for (int x = 0; x < chunk.x; x++) {
	    	for (int z = 0; z < chunk.z; z++) {
	    		chunk.setBlock(x, chunk.y - 1, z, 1);
	    	}
	    }
	}
	
	public void genBlock() {
		chunk.setBlock(0,0,0,1);
	}
	
	/**
	 * Generates a XZ shell around the world
	 */
	public void genShell() {
	    for (int i = 0; i < chunk.x; i++) {
	    	
	    	for (int j = 0; j < chunk.y; j++) {
	    		
	    		for (int k = 0; k < chunk.z;k++) {

	    			if (i == 0 || k == 0) {
	    				chunk.setBlock(i, j, k, 1);
	    			}
	    			if (i == chunk.x - 1 || k == chunk.z - 1 ) {
	    				chunk.setBlock(i, j, k, 1);
	    			}
	    			

	    		}
	    	}

	    }
	}
	
	public void initCave() {
		chunk.random.setSeed(chunk.seed);
	    for (int i = 0; i < chunk.x; i++) {
	    	
	    	for (int j = 0; j < chunk.y; j++) {
	    		
	    		for (int k = 0; k < chunk.z;k++) {

	    			if (chunk.random.nextFloat() > 0.5) {
	    				chunk.setBlock(i, j, k, 1);
	    			}
	    			

	    		}
	    	}

	    }
	}
	
	public void genCave3D() {
		int[][][] newWorld = new int[chunk.x][chunk.y][chunk.z];
	    
	    for (int i = 1; i < chunk.x - 1; i++) {
	    	
	    	for (int j = 1; j < chunk.y - 1; j++) {
	    		
	    		for (int k = 1; k < chunk.z - 1;k++) {

	    			int score = 
	    				chunk.getBlock(i + 1, j + 1, k) +
	    				chunk.getBlock(i + 1, j, k) +
	    				chunk.getBlock(i + 1, j - 1, k) +
	    				chunk.getBlock(i, j + 1, k) + 
	    				chunk.getBlock(i, j - 1, k) +  
	    				chunk.getBlock(i - 1, j - 1, k) + 
	    				chunk.getBlock(i - 1, j, k) + 
	    				chunk.getBlock(i - 1, j + 1, k);
	    			
	    			score += 
	    				chunk.getBlock(i,j,k+1) +
		    			chunk.getBlock(i + 1, j + 1, k+1) +
	    				chunk.getBlock(i + 1, j, k+1) +
	    				chunk.getBlock(i + 1, j - 1, k+1) +
	    				chunk.getBlock(i, j + 1, k+1) + 
	    				chunk.getBlock(i, j - 1, k+1) +  
	    				chunk.getBlock(i - 1, j - 1, k+1) + 
	    				chunk.getBlock(i - 1, j, k+1) + 
	    				chunk.getBlock(i - 1, j + 1, k+1);
	    			
	    			score += 
	    				chunk.getBlock(i,j,k-1) +
		    			chunk.getBlock(i + 1, j + 1, k-1) +
	    				chunk.getBlock(i + 1, j, k-1) +
	    				chunk.getBlock(i + 1, j - 1, k-1) +
	    				chunk.getBlock(i, j + 1, k-1) + 
	    				chunk.getBlock(i, j - 1, k-1) +  
	    				chunk.getBlock(i - 1, j - 1, k-1) + 
	    				chunk.getBlock(i - 1, j, k-1) + 
	    				chunk.getBlock(i - 1, j + 1, k-1);
	    			
	    			
	    			
	    			if (score < 13) {
	    				newWorld[i][j][k] = 0;
	    			}
	    			else if (score == 13) {
	    				newWorld[i][j][k] = chunk.getBlock(i, j, k);
	    			}
	    			else if (score > 13) {
	    				newWorld[i][j][k] = 1;
	    			}
	    			

	    		}
	    	}

	    }
	    
	    chunk.worldArray = newWorld;
	}
	
	public void genCave2D() {
		int[][][] newWorld = new int[chunk.x][chunk.y][chunk.z];
	    
	    for (int i = 1; i < chunk.x - 1; i++) {
	    	
	    	for (int j = 1; j < chunk.y - 1; j++) {
	    		
	    		for (int k = 0; k < chunk.z;k++) {

	    			int score = chunk.getBlock(i + 1, j + 1, k) +
	    				chunk.getBlock(i + 1, j, k) +
	    				chunk.getBlock(i + 1, j - 1, k) +
	    				chunk.getBlock(i, j + 1, k) + 
	    				chunk.getBlock(i, j - 1, k) +  
	    				chunk.getBlock(i - 1, j - 1, k) + 
	    				chunk.getBlock(i - 1, j, k) + 
	    				chunk.getBlock(i - 1, j + 1, k);
	    			
	    			if (score < 4) {
	    				newWorld[i][j][k] = 0;
	    			}
	    			else if (score == 4) {
	    				newWorld[i][j][k] = chunk.getBlock(i, j, k);
	    			}
	    			else if (score > 4) {
	    				newWorld[i][j][k] = 1;
	    			}
	    			

	    		}
	    	}

	    }
	    
	    chunk.worldArray = newWorld;
	}
}
