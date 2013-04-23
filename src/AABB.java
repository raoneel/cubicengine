import org.lwjgl.util.vector.Vector3f;
public class AABB {

	public Vector3f pos;
	public Vector3f extent;

	public AABB(Vector3f pos, Vector3f extent) {
		this.pos = pos;
		this.extent = extent;
	}

	public boolean intersects(AABB b) {
		Vector3f length = new Vector3f();
		Vector3f.sub(b.pos, this.pos, length);

		return (Math.abs(length.x) <= extent.x + b.extent.x) &&
		(Math.abs(length.y) <= extent.y + b.extent.y) &&
		(Math.abs(length.z) <= extent.z + b.extent.z);


	}
	Vector3f hitWallz(AABB start, Vector3f velocity){
		Vector3f length = new Vector3f();
		Vector3f newSpeed = velocity;
		Vector3f.sub(start.pos, this.pos, length);
		if((Math.abs(length.x) > extent.x + start.extent.x)){
			newSpeed.x = 0;			
		}
		if((Math.abs(length.y) > extent.y + start.extent.y)){
			newSpeed.y = 20;			
		}
		if((Math.abs(length.z) > extent.z + start.extent.z)){
			newSpeed.z = 0;			
		}
		newSpeed.x = 0;
		newSpeed.z = 0;
		return newSpeed;
	}
	/*
	Vector3f hitWall(AABB b, Vector3f velocity, boolean onGround){
		Vector3f length = new Vector3f();
		Vector3f newSpeed = velocity;
		Vector3f.sub(b.pos, this.pos, length);
		float ux = (Math.abs(length.x) - (extent.x + b.extent.x))/velocity.x;
		float uy = (Math.abs(length.y) - (extent.y + b.extent.y))/velocity.y;
		float uz = (Math.abs(length.z) - (extent.z + b.extent.z))/velocity.z;
		if(onGround){
			newSpeed.y = 0;
			if(ux > uz){
				newSpeed.x = 0;
			}
			else{
				newSpeed.z = 0;
			}
			newSpeed.x = 0;
			newSpeed.z = 0;
		}
		else{
			if(ux > uy && ux > uz){
				newSpeed.x = 0;
			}
			if(uy > ux && uy > uz){
				newSpeed.y = 50;
			}
			else{
				newSpeed.z = 0;
			}
			newSpeed.x = 0;
			newSpeed.z = 0;
		}
		return newSpeed;
	}
	Vector3f lastAxis(AABB b, Vector3f lastSpeed){
		Vector3f length = new Vector3f();
		Vector3f newSpeed = lastSpeed;
		Vector3f.sub(b.pos, this.pos, length);
		float ux = (Math.abs(length.x) - (extent.x + b.extent.x))/lastSpeed.x;
		float uy = (Math.abs(length.y) - (extent.y + b.extent.y))/lastSpeed.y;
		float uz = (Math.abs(length.z) - (extent.z + b.extent.z))/lastSpeed.z;
		if(ux > uy && ux > uz){
			newSpeed.x = 0;
		}
		if(uy > ux && uy > uz){
			newSpeed.y = 0;
		}
		else{
			newSpeed.z = -50;
		}
		return newSpeed;
	}
	*/
	
	boolean onGround(AABB b){
		Vector3f length = new Vector3f();
		Vector3f.sub(b.pos, this.pos, length);

		if( (Math.abs(length.x) <= extent.x + b.extent.x) && (Math.abs(length.z) <= extent.z + b.extent.z)){
			if((Math.abs(length.y) - (extent.y + b.extent.y)) <= 100){
				return true;
			}
		}
		return false;


		}
	/*
	Vector3f gravityFix(AABB b, float gravity){
		Vector3f length = new Vector3f();
		Vector3f.sub(b.pos, this.pos, length);
		Vector3f newSpeed = new Vector3f();
		newSpeed.x = 0;
		newSpeed.y = 0;
		float uz = (Math.abs(length.z) - (extent.z + b.extent.z))/gravity;
		newSpeed.z = uz - 80;
		return newSpeed;
	}*/
}