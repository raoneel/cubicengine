import java.util.Random;


public class Noise {
	int size;
	int randomNums[][];
	Random gen;
	int height;
	int floor;
	long seed;
    int frequency;// = 0.05f;
    int step;// = (int)(1/frequency);
    int amplitude;// = 3;
    float heightMap[][];// = new float[size][size];
    float smoothMap[][];
    int chunkSize;
    Chunk chunk;
    
    // float tempValue = 0.0f
    
    //int randomNums[][] = new int[size][size];
    //long seed = 122L;
	
	
	
	public Noise(long seedInput, int heightInput, int floorInput, int frequencyInput, Chunk chunk){
		//this.size = sizeInput;
		//this.size = chunk.x;
		this.seed = seedInput;
		this.gen = new Random(seedInput);
		this.height = heightInput;
		this.floor = floorInput;
		//this.step = (int)(1/frequency) -1;
		this.frequency = frequencyInput;
		this.randomNums = new int[chunk.x][chunk.x];
		this.heightMap = new float[chunk.x][chunk.x];
		this.smoothMap = new float[chunk.x][chunk.x];
		this.chunk = chunk;
		height = chunk.height;
		chunkSize = chunk.chunkSize;
		//this.generateRandom();
	}
	
	public void interpolate2D(){
		Chunk tempChunkS = new Chunk(chunk.x,chunk.y,chunk.z,chunk.world);
		tempChunkS.chunkX = chunk.chunkX - (chunk.chunkX%frequency);
		tempChunkS.chunkZ = chunk.chunkZ - (chunk.chunkZ%frequency);
		tempChunkS.genSeed();
		tempChunkS.setNoiseParam(height,0);
		tempChunkS.noise.generateRandom();
		Chunk tempChunkX = new Chunk(chunk.x,chunk.y,chunk.z,chunk.world);
		tempChunkX.chunkX = chunk.chunkX + frequency - (chunk.chunkX%frequency);
		tempChunkX.chunkZ = chunk.chunkZ - (chunk.chunkZ%frequency);
		tempChunkX.genSeed();
		tempChunkX.setNoiseParam(height,0);
		tempChunkX.noise.generateRandom();
		Chunk tempChunkZ = new Chunk(chunk.x,chunk.y,chunk.z,chunk.world);
		tempChunkZ.chunkX = chunk.chunkX - (chunk.chunkX%frequency);
		tempChunkZ.chunkZ = chunk.chunkZ + frequency - (chunk.chunkZ%frequency);
		tempChunkZ.genSeed();
		tempChunkZ.setNoiseParam(height,0);
		tempChunkZ.noise.generateRandom();
		Chunk tempChunkXZ = new Chunk(chunk.x,chunk.y,chunk.z,chunk.world);
		tempChunkXZ.chunkX = chunk.chunkX + frequency - (chunk.chunkX%frequency);
		tempChunkXZ.chunkZ = chunk.chunkZ + frequency - (chunk.chunkZ%frequency);
		tempChunkXZ.genSeed();
		tempChunkXZ.setNoiseParam(height,0);
		tempChunkXZ.noise.generateRandom();
		
		/*
         Chunk tempChunkX = new Chunk ((int)((chunk.chunkX + frequency - (chunk.chunkX%frequency))*chunk.x),chunk.y,chunk.z,chunk.world);
         //System.out.println(chunk.x + frequency - (chunk.x%frequency));
         Chunk tempChunkZ = new Chunk (chunk.x, chunk.y,(int)((chunk.chunkZ + frequency - (chunk.chunkZ%frequency))*chunk.z),chunk.world);
         Chunk tempChunkXZ = new Chunk ((int)((chunk.chunkX + frequency - (chunk.chunkX%frequency))*chunk.z),chunk.y,
         (int)((chunk.chunkZ + frequency - (chunk.chunkZ%frequency))*chunk.z),chunk.world);
         */
		
		int v1 = tempChunkS.noise.randomNums[0][0];
    	int v2 = tempChunkZ.noise.randomNums[0][0];
    	int v3 = tempChunkX.noise.randomNums[0][0];
    	int v4 = tempChunkXZ.noise.randomNums[0][0];
        
    	float intermediateZ = ((float)chunk.chunkZ%frequency * chunk.z)/(frequency * chunk.z);
    	float intermediateStep = (1.0f/(float)(frequency * chunk.z));
    	for (int i = 0; i < chunkSize; i++){
    		float i1 = cosineInterp(v1,v2,intermediateZ);
    		float i2 = cosineInterp(v3,v4,intermediateZ);
    		intermediateZ += intermediateStep;
    		float intermediateX = ((float)chunk.chunkX%frequency * chunk.x)/(frequency * chunk.x);
    		for (int j = 0; j < chunkSize; j++){
    			float ans = cosineInterp(i1, i2, intermediateX);
    			heightMap[j][i] = Math.round(ans);
    			intermediateX += intermediateStep;
    		}
    	}
    	
    	
    	/*
         //this.step = (int)(1/frequency)-1;
         for (int c = 0; c < 1; c++){
         for (int i = 0; i < this.size - this.step; i += this.step){
         
         for (int j = 0; j < this.size - this.step; j += this.step){
         int v1 = this.randomNums[i][j];
         int v2 = this.randomNums[i+this.step][j];
         int v3 = this.randomNums[i][j+this.step];
         int v4 = this.randomNums[i+this.step][j+this.step];
         this.heightMap[i][j] = v1; //Math.round(v1);
         //heightMap[i+step][j] = Math.round(v2);
         this.heightMap[i][j+this.step] = v3; //Math.round(v3);
         //heightMap[i+step][j+step] = Math.round(v4);
         intermediateX = 0.0f;
         for (int k = i; k<= i + this.step; k++){
         intermediateX += (1.0f/(float)(this.step));
         float i1 = cosineInterp(v1,v2,intermediateX);
         float i2 = cosineInterp(v3,v4,intermediateX);
         this.heightMap[k][j] = i1; //Math.round(i1);
         this.heightMap[k][j+this.step] = i2;//Math.round(i2);
         intermediateY = 0.0f;
         for (int l = j + 1; l < j + this.step; l++){
         intermediateY += (1.0f/(float)(this.step));
         float ans = cosineInterp(i1,i2,intermediateY);
         //heightMap[b][0] = Noise1D(heightMap[a-step][0],intermediate);
         //tempValue = cosineInterp(heightMap[i-step][0],n,intermediate);
         //heightMap[b][0] += tempValue;
         this.heightMap[k][l] = ans;//Math.round(ans);
         //System.out.println(heightMap[k][l]);
         //System.out.println(ans);
         
         }
         }
         }
         }
         }
         */
	}
	
	public float cosineInterp(float a, float b, float x){
    	float ft = x * 3.1415927f;
    	float f = (float) (1f - Math.cos(ft)) * .5f;
    	return a*(1-f) + b*f;
    }
	public void generateRandom(){
    	for (int i = 0; i < chunkSize; i++){
    		for (int j = 0; j < chunkSize; j++){
    			this.randomNums[i][j] = this.gen.nextInt(this.height -1 +1) +this.floor;
    		}
    	}
    }
	
	public int getRandomValue(int x, int y){
		return randomNums[x][y];
	}
	public float[][] smoothNoise(){
		for (int i = 0; i < this.size; i++){
			for (int j = 0; j < this.size; j++){
				
                
				this.smoothMap[i][j] = Math.round((heightMap[checkOFB(i-1)][checkOFB(j-1)] +
                                                   heightMap[checkOFB(i+1)][checkOFB(j-1)] +
                                                   heightMap[checkOFB(i-1)][checkOFB(j+1)] +
                                                   heightMap[checkOFB(i+1)][checkOFB(j+1)])/16) +
                Math.round((heightMap[checkOFB(i-1)][j] +
                            heightMap[checkOFB(i+1)][j] +
                            heightMap[i][checkOFB(j-1)] +
                            heightMap[i][checkOFB(j+1)])/8) +
                Math.round(heightMap[i][j]/4);
			}
		}
		return smoothMap;
	}
    
	
	public void createHeightMap(){
		//this.heightMap = new float[size][size];
		this.interpolate2D();
		//this.heightMap;// = this.smoothNoise();
		
	}
	
	public void setBlocks(Chunk chunk) {
		for (int x = 0; x < chunkSize; x++) {
			for (int z = 0; z < chunkSize; z++) {
				int height = (int)heightMap[x][z];
				for (int y = 0; y < height; y++) {
                    
					chunk.setBlock(x, y, z, 1);
				}
				
			}
		}
	}
	
	public int checkOFB(int i){
		if ((i) < 0){
			return i + 1;
		}else if ((i) > 0 && (i) < this.size){
			return i;
		}else if (i >= this.size){
			return this.size - 1;
		}
		return i;
        
	}
}
