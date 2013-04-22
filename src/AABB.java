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
	

}
