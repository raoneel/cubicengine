import java.util.Random;


public class Cloud {
	
	Random random;
	Chunk chunk;
	float frequency;
	
	public Cloud(Chunk chunk, float frequency) {
		this.chunk = chunk;
		random = new Random();
		this.frequency = frequency;
	}

	public void genClouds() {
		random.setSeed(chunk.seed);
		int randomX = random.nextInt() % chunk.x;
		int randomZ = random.nextInt() % chunk.z;
		int cloudY = chunk.y - 1;
		int cloudSize = 50;
		
		chunk.setBlock(randomX, cloudY, randomZ, CubeType.CLOUD);
		
		for (int i = 0; i < cloudSize; i++) {
			
			//Up/down
			if (random.nextFloat() > 0.5f) {
				if (random.nextFloat() > 0.5f) {
					randomX += 1;
				}
				else {
					randomX -= 1;
				}
			}
			//left right
			else {
				if (random.nextFloat() > 0.5f) {
					randomZ += 1;
				}
				else {
					randomZ -= 1;
				}
			}
			chunk.setBlock(randomX, cloudY, randomZ, CubeType.CLOUD);
		}
	}
}
