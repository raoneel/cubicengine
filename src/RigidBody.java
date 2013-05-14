import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector3f;
public class RigidBody {
	float width;
	
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

public RigidBody(float width, Vector3f center){
	this.width = width;
	this.X = new Vector3f();
	this.X = center;
	R = new Matrix3f();
	R.setIdentity();
	m = 800;
	P = new Vector3f(0,0,0);
	L = new Vector3f(10*m,10*m,10*m);
	Iinv = new Matrix3f();
	v = new Vector3f(0,0,0);
	omega = new Vector3f(0.00f,0.00f,0.00f);
	Rdot = new Matrix3f();
	force = new Vector3f(0,0,0);
	torque = new Vector3f(0,0,0);
	
	Ibody = new Matrix3f();
	Ibody.setIdentity();
	Ibody.m00 = width*width*2*m/12;
	Ibody.m11 = width*width*2*m/12;
	Ibody.m22 = width*width*2*m/12;
	
	Ibodyinv = new Matrix3f();
	Matrix3f.invert(Ibody, Ibodyinv);
	
	
}

RigidBody copyBody(){
	RigidBody body = new RigidBody(0, new Vector3f(0,0,0));
	body.width = width;
	body.m = m;
	body.Ibody = Ibody;
	body.Ibodyinv = Ibodyinv;
	body.X = X;
	body.R = R;
	body.P = P;
	body.L = L;
	body.Iinv = Iinv;
	body.v = v;
	body.omega = omega;
	body.force = force;
	body.torque = torque;
	body.Rdot = Rdot;
	return body;
}

public void drawCube(){
	Vector3f xAxis = new Vector3f(R.m00, R.m10, R.m20);
	Vector3f yAxis = new Vector3f(R.m01, R.m11, R.m21);
	Vector3f zAxis = new Vector3f(R.m02, R.m12, R.m22);
	xAxis.normalise();
	yAxis.normalise();
	zAxis.normalise();
	xAxis.scale(width/2);
	yAxis.scale(width/2);
	zAxis.scale(width/2);
	
	Vector3f vert1 = new Vector3f();
	Vector3f vert2 = new Vector3f();
	Vector3f vert3 = new Vector3f();
	Vector3f vert4 = new Vector3f();
	
	//right face
	Vector3f.add(X, xAxis, vert1);
	Vector3f.add(X, xAxis, vert2);
	Vector3f.add(X, xAxis, vert3);
	Vector3f.add(X, xAxis, vert4);
	
	Vector3f.add(vert1, yAxis, vert1);
	Vector3f.add(vert1, zAxis, vert1);
	Vector3f.add(vert2, yAxis, vert2);
	Vector3f.sub(vert2, zAxis, vert2);
	Vector3f.sub(vert3, yAxis, vert3);
	Vector3f.add(vert3, zAxis, vert3);
	Vector3f.sub(vert4, yAxis, vert4);
	Vector3f.sub(vert4, zAxis, vert4);
	GL11.glNormal3f(xAxis.x/(width/2),xAxis.y/(width/2),xAxis.z/(width/2));
	GL11.glVertex3f(vert1.x,vert1.y,vert1.z);
	GL11.glVertex3f(vert2.x,vert2.y,vert2.z);
	GL11.glVertex3f(vert4.x,vert4.y,vert4.z);
	GL11.glVertex3f(vert3.x,vert3.y,vert3.z);
	
	//left face
	Vector3f.sub(X, xAxis, vert1);
	Vector3f.sub(X, xAxis, vert2);
	Vector3f.sub(X, xAxis, vert3);
	Vector3f.sub(X, xAxis, vert4);
	
	Vector3f.add(vert1, yAxis, vert1);
	Vector3f.add(vert1, zAxis, vert1);
	Vector3f.add(vert2, yAxis, vert2);
	Vector3f.sub(vert2, zAxis, vert2);
	Vector3f.sub(vert3, yAxis, vert3);
	Vector3f.add(vert3, zAxis, vert3);
	Vector3f.sub(vert4, yAxis, vert4);
	Vector3f.sub(vert4, zAxis, vert4);
	GL11.glNormal3f(-1*xAxis.x/(width/2),-1*xAxis.y/(width/2),-1*xAxis.z/(width/2));
	GL11.glVertex3f(vert1.x,vert1.y,vert1.z);
	GL11.glVertex3f(vert2.x,vert2.y,vert2.z);
	GL11.glVertex3f(vert4.x,vert4.y,vert4.z);
	GL11.glVertex3f(vert3.x,vert3.y,vert3.z);
	
	//top
	Vector3f.add(X, yAxis, vert1);
	Vector3f.add(X, yAxis, vert2);
	Vector3f.add(X, yAxis, vert3);
	Vector3f.add(X, yAxis, vert4);
	
	Vector3f.add(vert1, xAxis, vert1);
	Vector3f.add(vert1, zAxis, vert1);
	Vector3f.add(vert2, xAxis, vert2);
	Vector3f.sub(vert2, zAxis, vert2);
	Vector3f.sub(vert3, xAxis, vert3);
	Vector3f.add(vert3, zAxis, vert3);
	Vector3f.sub(vert4, xAxis, vert4);
	Vector3f.sub(vert4, zAxis, vert4);
	GL11.glNormal3f(yAxis.x/(width/2),yAxis.y/(width/2),yAxis.z/(width/2));
	GL11.glVertex3f(vert1.x,vert1.y,vert1.z);
	GL11.glVertex3f(vert2.x,vert2.y,vert2.z);
	GL11.glVertex3f(vert4.x,vert4.y,vert4.z);
	GL11.glVertex3f(vert3.x,vert3.y,vert3.z);
	
	//bottom
	Vector3f.sub(X, yAxis, vert1);
	Vector3f.sub(X, yAxis, vert2);
	Vector3f.sub(X, yAxis, vert3);
	Vector3f.sub(X, yAxis, vert4);
	
	Vector3f.add(vert1, xAxis, vert1);
	Vector3f.add(vert1, zAxis, vert1);
	Vector3f.add(vert2, xAxis, vert2);
	Vector3f.sub(vert2, zAxis, vert2);
	Vector3f.sub(vert3, xAxis, vert3);
	Vector3f.add(vert3, zAxis, vert3);
	Vector3f.sub(vert4, xAxis, vert4);
	Vector3f.sub(vert4, zAxis, vert4);
	GL11.glNormal3f(-1*yAxis.x/(width/2), -1*yAxis.y/(width/2), -1*yAxis.z/(width/2));
	GL11.glVertex3f(vert1.x,vert1.y,vert1.z);
	GL11.glVertex3f(vert2.x,vert2.y,vert2.z);
	GL11.glVertex3f(vert4.x,vert4.y,vert4.z);
	GL11.glVertex3f(vert3.x,vert3.y,vert3.z);
	
	//back
	Vector3f.add(X, zAxis, vert1);
	Vector3f.add(X, zAxis, vert2);
	Vector3f.add(X, zAxis, vert3);
	Vector3f.add(X, zAxis, vert4);
	
	Vector3f.add(vert1, xAxis, vert1);
	Vector3f.add(vert1, yAxis, vert1);
	Vector3f.add(vert2, xAxis, vert2);
	Vector3f.sub(vert2, yAxis, vert2);
	Vector3f.sub(vert3, xAxis, vert3);
	Vector3f.add(vert3, yAxis, vert3);
	Vector3f.sub(vert4, xAxis, vert4);
	Vector3f.sub(vert4, yAxis, vert4);
	GL11.glNormal3f(zAxis.x/(width/2),zAxis.y/(width/2),zAxis.z/(width/2));
	GL11.glVertex3f(vert1.x,vert1.y,vert1.z);
	GL11.glVertex3f(vert2.x,vert2.y,vert2.z);
	GL11.glVertex3f(vert4.x,vert4.y,vert4.z);
	GL11.glVertex3f(vert3.x,vert3.y,vert3.z);
	
	//front
	Vector3f.sub(X, zAxis, vert1);
	Vector3f.sub(X, zAxis, vert2);
	Vector3f.sub(X, zAxis, vert3);
	Vector3f.sub(X, zAxis, vert4);
	
	Vector3f.add(vert1, xAxis, vert1);
	Vector3f.add(vert1, yAxis, vert1);
	Vector3f.add(vert2, xAxis, vert2);
	Vector3f.sub(vert2, yAxis, vert2);
	Vector3f.sub(vert3, xAxis, vert3);
	Vector3f.add(vert3, yAxis, vert3);
	Vector3f.sub(vert4, xAxis, vert4);
	Vector3f.sub(vert4, yAxis, vert4);
	GL11.glNormal3f(-1*zAxis.x/(width/2),-1*zAxis.y/(width/2),-1*zAxis.z/(width/2));
	GL11.glVertex3f(vert1.x,vert1.y,vert1.z);
	GL11.glVertex3f(vert2.x,vert2.y,vert2.z);
	GL11.glVertex3f(vert4.x,vert4.y,vert4.z);
	GL11.glVertex3f(vert3.x,vert3.y,vert3.z);
	
	
	
	
}

public void stepTime(){
	//add force and torque to momentum, then recompute velocities
	Vector3f.add(P, force, P);
	//System.out.println(force);
	Vector3f.add(L, torque, L);
	//System.out.println(P);
	v.x = P.x/m;
	v.y = P.y/m;
	v.z = P.z/m;
	//L.x = m;
	//L.y = m;
	//L.z = m;
	Matrix3f.transform(Iinv, L, omega);
	
	//v.y = 1;
	//System.out.println(v.x);
	Vector3f.add(X, v, X);
	
	//calculate Rdot
	//omega.x = 0;
	//omega.y = 0;
	//omega.z = 0.01f;
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
	Matrix3f.mul(omegastar, R, Rdot);
	Matrix3f.add(R, Rdot, R);
	
	
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
	//System.out.println(R.m22);
	 
	 
	
	force.set(0,0,0);
	torque.set(0,0,0);
}

public void addGravity(){
	force.y -= m*1;
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
	
	//calculate omega
	//Matrix3f.transform(Iinv, L, omega);
	
}

public ArrayList<Vector3f> isCollideGround(RigidBody prevBody, float ground){ 
	//if(v.y >= 0){
	//	return;
	//}
	Vector3f diff = new Vector3f();
	Vector3f.sub(prevBody.X, X, diff);
	Vector3f xAxis = new Vector3f(R.m00, R.m10, R.m20);
	Vector3f yAxis = new Vector3f(R.m01, R.m11, R.m21);
	Vector3f zAxis = new Vector3f(R.m02, R.m12, R.m22);
	xAxis.normalise();
	yAxis.normalise();
	zAxis.normalise();
	xAxis.scale(width/2);
	yAxis.scale(width/2);
	zAxis.scale(width/2);
	
	Vector3f vert1 = new Vector3f();
	Vector3f vert2 = new Vector3f();
	Vector3f vert3 = new Vector3f();
	Vector3f vert4 = new Vector3f();
	Vector3f vert5 = new Vector3f();
	Vector3f vert6 = new Vector3f();
	Vector3f vert7 = new Vector3f();
	Vector3f vert8 = new Vector3f();
	
	Vector3f.add(X, xAxis, vert1);
	Vector3f.add(X, xAxis, vert2);
	Vector3f.add(X, xAxis, vert3);
	Vector3f.add(X, xAxis, vert4);
	Vector3f.sub(X, xAxis, vert5);
	Vector3f.sub(X, xAxis, vert6);
	Vector3f.sub(X, xAxis, vert7);
	Vector3f.sub(X, xAxis, vert8);
	
	Vector3f.add(vert1, yAxis, vert1);
	Vector3f.add(vert1, zAxis, vert1);
	
	Vector3f.add(vert2, yAxis, vert2);
	Vector3f.sub(vert2, zAxis, vert2);
	
	Vector3f.sub(vert3, yAxis, vert3);
	Vector3f.add(vert3, zAxis, vert3);
	
	Vector3f.sub(vert4, yAxis, vert4);
	Vector3f.sub(vert4, zAxis, vert4);
	
	Vector3f.add(vert5, yAxis, vert5);
	Vector3f.add(vert5, zAxis, vert5);
	
	Vector3f.add(vert6, yAxis, vert6);
	Vector3f.sub(vert6, zAxis, vert6);
	
	Vector3f.sub(vert7, yAxis, vert7);
	Vector3f.add(vert7, zAxis, vert7);
	
	Vector3f.sub(vert8, yAxis, vert8);
	Vector3f.sub(vert8, zAxis, vert8);
	
	ArrayList<Vector3f> verts = new ArrayList<Vector3f>();
	ArrayList<Vector3f> collidedVerts = new ArrayList<Vector3f>();
	verts.add(vert1);
	verts.add(vert2);
	verts.add(vert3);
	verts.add(vert4);
	verts.add(vert5);
	verts.add(vert6);
	verts.add(vert7);
	verts.add(vert8);
	
	Vector3f vert = new Vector3f();
	for(int i = 0; i < 8; i++){
		vert = verts.get(i);
		if(vert.y <= ground + 100){
			Vector3f.add(vert, diff, vert);
			collidedVerts.add(vert);
		}
	}
	
	return collidedVerts;
	
	//for(int i = 0; i < collidedVerts.size(); i++){
	//	groundCollision(collidedVerts.get(i));
	//}
	//if(collidedVerts.size() != 0){
	//	System.out.println(collidedVerts.size());
	//}
	
	
	
	
	
}
public void groundCollision(Vector3f point, int numVerts){
	Vector3f normal = new Vector3f(0,1,0);
	
	Vector3f pv = new Vector3f();
	Vector3f temp = new Vector3f();
	float epsilon = 0.7f;
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
		force.y+= 1*m;
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


}