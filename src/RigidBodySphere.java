import java.util.ArrayList;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.glu.Quadric;
import org.lwjgl.util.glu.Sphere;

public class RigidBodySphere {
	float radius;
	
	float m;
	Matrix3f Ibody;
	Matrix3f Ibodyinv;
	
	//these variables represent the state of the rigid body
	Vector3f X; //position
	Matrix3f R; //rotation matrix																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		
	Vector3f P; //linear momentum
	Vector3f L; //angular momentum
	
	//these variables are neccesary to compute the next state of the rigid body
	Matrix3f Iinv;
	Vector3f v;  //velocity
	Vector3f omega; //angular velocity
	Matrix3f Rdot;
	Vector3f force;
	Vector3f torque;
	boolean[] hitSides = new boolean[6];
	boolean friction = false;
	
public RigidBodySphere(float radius, Vector3f center){
	m = 100;
	this.radius = radius;
	this.X = new Vector3f();
	this.X = center;
	R = new Matrix3f();
	R.setIdentity();
	P = new Vector3f(10*m,10*m,10*m);
	L = new Vector3f(0,0,0);
	Iinv = new Matrix3f();
	v = new Vector3f(0,0,0);
	omega = new Vector3f(0.0f,0.00f,0.00f);
	Rdot = new Matrix3f();
	force = new Vector3f(0,0,0);
	torque = new Vector3f(0,0,0);
	
	Ibody = new Matrix3f();
	Ibody.setIdentity();
	Ibody.m00 = (2/5)*m*radius*radius;
	Ibody.m11 = (2/5)*m*radius*radius;
	Ibody.m22 = (2/5)*m*radius*radius;
	
	Ibodyinv = new Matrix3f();
	Matrix3f.invert(Ibody, Ibodyinv);
	
	
}

public void drawSphere(){
	GL11.glPushMatrix();
	//R.transpose();
	Vector3f xAxis = new Vector3f(R.m00, R.m10, R.m20);
	Vector3f yAxis = new Vector3f(R.m01, R.m11, R.m21);
	Vector3f zAxis = new Vector3f(R.m02, R.m12, R.m22);
	xAxis.normalise();
	yAxis.normalise();
	zAxis.normalise();
	
	
	FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
	buffer.put(xAxis.x);
	buffer.put(xAxis.y);
	buffer.put(xAxis.z);
	buffer.put(0);
	buffer.put(yAxis.x);
	buffer.put(yAxis.y);
	buffer.put(yAxis.z);
	buffer.put(0);
	buffer.put(zAxis.x);
	buffer.put(zAxis.y);
	buffer.put(zAxis.z);
	buffer.put(0);
	buffer.put(0);
	buffer.put(0);
	buffer.put(0);
	buffer.put(1);
	buffer.flip();
	//R.transpose();
	
	GL11.glTranslatef(X.x, X.y, X.z);
	GL11.glMultMatrix(buffer);
	Sphere sphere = new Sphere();
	sphere.draw(radius, 10,10);
	GL11.glPopMatrix();
	
}

public void scaleMatrix(Matrix3f mat, float f){
	mat.m00*= f;
	mat.m01*= f;
	mat.m02*= f;
	mat.m10*=f;
	mat.m11 *=f;
	mat.m12 *=f;
	mat.m20 *=f;
	mat.m21 *=f;
	mat.m22 *= f;
}

public void orthoNormalize(Matrix3f mat){
	// It probably should be going 11, 12, 13 for x
	Vector3f xAxis = new Vector3f(R.m00, R.m10, R.m20);
	Vector3f yAxis = new Vector3f(R.m01, R.m11, R.m21);
	Vector3f zAxis = new Vector3f();
	xAxis.normalise();
	Vector3f.cross(xAxis, yAxis, zAxis);
	zAxis.normalise();
	Vector3f.cross(zAxis, xAxis, yAxis);
	yAxis.normalise();
	mat.m00 = xAxis.x;
	mat.m10 = xAxis.y;
	mat.m20 = xAxis.z;
	mat.m01 = yAxis.x;
	mat.m11 = yAxis.y;
	mat.m21 = yAxis.z;
	mat.m02 = zAxis.x;
	mat.m12 = zAxis.y;
	mat.m22 = zAxis.z;
	


   

}
public void stepTime(float t){
	for(int i = 0; i < 6; i++){
		hitSides[i] = false;
	}
	//L.x = 0;
	//L.y = 0;
	//L.z = 0;
//	System.out.println(R);
	//System.out.println(R);
	//System.out.println(torque);
//	System.out.println(L);
	//System.out.println(X);
	//add force and torque to momentum, then recompute velocities
	Vector3f.add(P, force, P);
	//System.out.println(force);
	Vector3f.add(L, torque, L);
//	System.out.println(P);
	v.x = P.x/m;
	v.y = P.y/m;
	v.z = P.z/m;
	Matrix3f.transform(Iinv, L, omega);
	//v.scale(t);
	//v.y = 1;
	//System.out.println(v.x);
	Vector3f.add(X, v, X);
	//v.scale(1/t);
	
	//calculate Rdot
	//omega.x = 0;
	//omega.y = 0;
	//omega.z = 0.01f;
	/*
	Vector3f r = new Vector3f(t*omega.x, t*omega.y, t*omega.z);
	float theta = r.length();
	float temp1 = (float) Math.sin(theta);
	Matrix3f temp = new Matrix3f();
	*/
	Matrix3f omegastar = new Matrix3f();
	omegastar.m00 = 0.0f;
	omegastar.m01 = -1*omega.z;
	omegastar.m02 = omega.y;
	omegastar.m10 = omega.z;
	omegastar.m11 = 0.0f;
	omegastar.m12 = -1*omega.x;
	omegastar.m20 = -1*omega.y;
	omegastar.m21 = omega.x;
	omegastar.m22 = 0.0f;
	
	/*
	scaleMatrix(omegastar, temp1);
	temp.setIdentity();
	Matrix3f.add(omegastar, temp, temp);
	
	scaleMatrix(omegastar, 1/temp1);
	Matrix3f.mul(omegastar, omegastar, omegastar);
	temp1 = (1.0f - (float) Math.cos(theta));
	scaleMatrix(omegastar, temp1);
	Matrix3f.add(temp, omegastar, temp);
	Matrix3f.mul(temp, R, R);
	*/
	
	Matrix3f.mul(omegastar, R, Rdot);
//	System.out.println("omega");
//	System.out.println(omega);
	Matrix3f.add(R, Rdot, R);
	orthoNormalize(R);
	
	/*
	Vector3f xAxis = new Vector3f(R.m00, R.m10, R.m20);
	Vector3f yAxis = new Vector3f(R.m01, R.m11, R.m21);
	Vector3f zAxis = new Vector3f(R.m02, R.m12, R.m22);
	xAxis.normalise();
	yAxis.normalise();
	zAxis.normalise();
	R.m00 = xAxis.x;
	R.m10 = xAxis.y;
	R.m20 = xAxis.z;
	R.m01 = yAxis.x;
	R.m11 = yAxis.y;
	R.m21 = yAxis.z;
	R.m02 = zAxis.x;
	R.m12 = zAxis.y;
	R.m22 = zAxis.z;
	*/
	//System.out.println(Rdot);
	
	//System.out.println(R.m22);
	
	force.set(0,0,0);
	torque.set(0,0,0);
	//R.setIdentity();
}

public void addGravity(){
	force.y -= m*3;
}

public void computeVariables(){
	//calculate v
	v.x = P.x/m;
	v.y = P.y/m;
	v.z = P.z/m;
	
	//caculate Iinv
	Matrix3f Rt = new Matrix3f();
	Matrix3f temp = new Matrix3f();
	Matrix3f.transpose(R, Rt);
	Matrix3f.mul(R, Ibodyinv, temp);
	Matrix3f.mul(temp, Rt, Iinv);
//	System.out.println("Iinv");
//	System.out.println(Iinv);
	
	//calculate omega
	Matrix3f.transform(Iinv, L, omega);
	
}

public void isCollideGround(float ground){
	//if(v.y >= 0){
	//	return;
	//}
	Vector3f point = new Vector3f(X.x, X.y - radius, X.z);
	if(X.y - radius <= ground + 100){
		groundCollision(point);
	}
	
	
	
	
	
}
public void groundCollision(Vector3f point){
	Vector3f normal = new Vector3f(0,1,0);
	Vector3f pv = new Vector3f();
	Vector3f temp = new Vector3f();
	float epsilon = 0.8f;
	float vrelative;
	
	//compute point velocity for collision point
	Vector3f.sub(point, X, temp);
	Vector3f.cross(omega, temp, temp);
	Vector3f.add(v, temp, pv);
	vrelative = Vector3f.dot(normal, pv);
	if(vrelative > 0){ //object moving away
		return;
	}
	else if(vrelative > -5){  //resting contact
		force.y+= 3*m;
		return;
	}
	float num = -(1+epsilon) * vrelative;
	float num2 = 1/m;
	
	Vector3f r = new Vector3f();
	Vector3f.sub(point, X, r);
	Vector3f.cross(r, normal, temp);
	Matrix3f.transform(Iinv, temp, temp);
	Vector3f.cross(temp, r, temp);
	float num3 = Vector3f.dot(normal, temp);
	
	float j = num/(num2+num3);
	Vector3f f = new Vector3f(0,j,0);
	
	//add force to momentum/velocity
	Vector3f.add(force, f, force);
	Vector3f.cross(r, f, temp);
	Vector3f.add(torque, temp, torque);
	
}

public void sphereBoxCollisionTest(AABB box){
	Vector3f closestPoint = new Vector3f(X.x, X.y, X.z);
	float largestClamp = 0;
	int clampType = 0;
	float temp = box.pos.y + box.extent.y;
	if(X.y > temp){
		largestClamp = X.y - temp;
		clampType = 1;
		closestPoint.y = temp;
	}
	else{
		temp = box.pos.y - box.extent.y;
		if(X.y < temp){
			if(temp-X.y > largestClamp){
				largestClamp = temp-X.y;
				clampType = 2;
			}
			closestPoint.y = temp;
		}
	}
	temp = box.pos.x + box.extent.x;
	if(X.x > temp){
		if(X.x - temp > largestClamp){
			largestClamp = X.x - temp;
			clampType = 3;
		}
		closestPoint.x = temp;
	}
	else{
		temp = box.pos.x - box.extent.x;
		if(X.x < temp){
			if(temp - X.x > largestClamp){
				largestClamp = temp - X.x;
				clampType = 4;
			}
			closestPoint.x = temp;
		}
	}
	temp = box.pos.z + box.extent.z;
	if(X.z > temp){
		if(X.z - temp > largestClamp){
			largestClamp = X.z - temp;
			clampType = 5;
		}
		closestPoint.z = temp;
	}
	else{
		temp = box.pos.z - box.extent.z;
		if(X.z < temp){
			if(temp - X.z > largestClamp){
				largestClamp = temp - X.z;
				clampType = 6;
			}
			closestPoint.z = temp;
		}
	}
	Vector3f vec = new Vector3f();
	Vector3f.sub(X, closestPoint, vec);
	Vector3f normal = new Vector3f(0,0,0);
	float dist = vec.length();
	if(dist < radius){  //there be collision
		//System.out.println(clampType);
		switch(clampType){
		case 1: normal.y = 1; break;
		case 2: normal.y = -1; break;
		case 3: normal.x = 1; break;
		case 4: normal.x = -1; break;
		case 5: normal.z = 1; break;
		case 6: normal.z = -1; break;
		}
		if(clampType != 0 && hitSides[clampType-1] == false){
			//hitSides[clampType-1] = true;
			sphereBoxCollision(closestPoint, normal);
		}
	}
}

public void sphereBoxCollision(Vector3f point, Vector3f normal){
	//System.out.println(normal);
	//Vector3f normal = new Vector3f(0,1,0);
	Vector3f pv = new Vector3f();
	Vector3f temp = new Vector3f();
	float epsilon = 0.8f;
	float vrelative;
	
	//compute point velocity for collision point
	Vector3f.sub(point, X, temp);
	Vector3f.cross(omega, temp, temp);
	Vector3f.add(v, temp, pv);
	vrelative = Vector3f.dot(normal, pv);
	
	
	/*
	if(normal.y == 1){
		if(force.y >=0){
			return;
		}
		//hitSides[0] = true;
	}
	
	if(normal.y == -1){
		if(force.y < 0){
			return;
		}
		//hitSides[1] = true;
	}
	if(normal.x == 1){
		if(force.x > 0){
			return;
		}
		//hitSides[2] = true;
	}
	if(normal.x == -1){
		if(force.x < 0){
			return;
		}
		//hitSides[3] = true;
	}
	if(normal.z == 1){
		if(force.z >0){
			return;
		}
		//hitSides[4] = true;
	}
	if(normal.z == -1){
		if(force.z <0){
			return;
		}
		//hitSides[5] = true;
	}
	*/
	
	
	if(vrelative < 2 && vrelative > -2 && normal.y == 1){//resting contact
		force.y+= 3*m;
		P.y = 0;
		v.y = 0;
		if(friction){
			P.x *= 0.98;
			P.z *= 0.98;
			L.scale(0.98f);
		}
		return;
	}
	if(vrelative > 0){ //object moving away
		return;
	}
	float num = -(1+epsilon) * vrelative;
	float num2 = 1/m;
	
	Vector3f r = new Vector3f();
	Vector3f.sub(point, X, r);
	Vector3f.cross(r, normal, temp);
	Matrix3f.transform(Iinv, temp, temp);
	Vector3f.cross(temp, r, temp);
	float num3 = Vector3f.dot(normal, temp);
	
	float j = num/(num2+num3);
	Vector3f f = new Vector3f(j*normal.x,j*normal.y,j*normal.z);
	
	//add force to momentum/velocity
	Vector3f.add(force, f, force);
	Vector3f.cross(r, f, temp);
	Vector3f.add(torque, temp, torque);
	
}

public void sphereSphereCollisionTest(RigidBodySphere sphere2){
	Vector3f temp = new Vector3f();
	Vector3f.sub(X, sphere2.X, temp);
	float dist = temp.length();
	if(dist > (radius + sphere2.radius) || dist < radius){
		return;
	}
//	System.out.println("SphereCollision");
	temp.normalise();
	Vector3f normal = new Vector3f(temp.x, temp.y, temp.z);
	//normal.scale(-1);
	temp.scale(radius);
	Vector3f point = new Vector3f();
	Vector3f.add(X, temp, point);
	sphereSphereCollision(sphere2, point, normal);
}

public void sphereSphereCollision(RigidBodySphere s2, Vector3f point, Vector3f normal){
//	System.out.println(normal);
	Vector3f pva = new Vector3f();
	Vector3f pvb = new Vector3f();
	Vector3f temp = new Vector3f();
	float epsilon = 0.8f;
	float vrelative;
	
	//compute point velocity for collision point
	Vector3f.sub(point, X, temp);
	Vector3f.cross(omega, temp, temp);
	Vector3f.add(v, temp, pva);
	
	Vector3f.sub(point, s2.X, temp);
	Vector3f.cross(s2.omega, temp, temp);
	Vector3f.add(s2.v, temp, pvb);
	
	Vector3f.sub(pva, pvb, temp);
	vrelative = Vector3f.dot(normal, temp);
	if(vrelative > 0){
		return;
	}
//	System.out.println("hit each other");
	//System.out.println(vrelative);
	float num = -(1+epsilon) * vrelative;
	float num2 = 1/m;
	float num3 = 1/s2.m;
	
	Vector3f ra = new Vector3f();
	Vector3f.sub(point, X, ra);
	Vector3f rb = new Vector3f();
	Vector3f.sub(point, s2.X, rb);
	
	
	Vector3f.cross(ra, normal, temp);
	Matrix3f.transform(Iinv, temp, temp);
	Vector3f.cross(temp, ra, temp);
	float num4 = Vector3f.dot(normal, temp);
//	System.out.println(num4);
	
	Vector3f.cross(rb, normal, temp);
	Matrix3f.transform(s2.Iinv, temp, temp);
	Vector3f.cross(temp, rb, temp);
	float num5 = Vector3f.dot(normal, temp);
	
	float j = num/(num2 + num3 + num4 + num5);
	//j*= 1000000;
	Vector3f f = new Vector3f(normal.x*j, normal.y*j, normal.z*j);
	
//	System.out.println(j);
	//System.out.println(j);
//	System.out.println(f);
	Vector3f.add(force, f, force);
	Vector3f.sub(s2.force, f, s2.force);
	
	Vector3f.cross(ra, f, temp);
	//Vector3f.add(torque, temp, torque);
	Vector3f.cross(rb, f, temp);
	//Vector3f.sub(s2.torque, temp, s2.torque);
	
	
	
}


}