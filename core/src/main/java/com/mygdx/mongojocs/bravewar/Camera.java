package com.mygdx.mongojocs.bravewar;

public class Camera extends Point3D {

	int x, y, z, rotx, roty, rotz;
	Matrix4x4 mat;

	public Camera(int xx, int yy, int zz, int rx, int ry, int rz)
	{
		x=xx; y=yy; z=zz;
		rotx=rx; roty=ry; rotz=rz;
		recalculateMatrix();
	}
	
	public void recalculateMatrix()
	{
		Matrix4x4 m2;
		
		mat=new Matrix4x4();
		m2=new Matrix4x4();
		mat.translationMatrix(-x,-y,-z);
		m2.YrotationMatrix(-roty);
		mat=mat.product(m2);
		m2.ZrotationMatrix(-rotz);
		mat=mat.product(m2);
		m2.XrotationMatrix(-rotx);
		mat=mat.product(m2);				
		//System.gc();
	}
}