package com.mygdx.mongojocs.bravewar;

public class Matrix4x4 {

	long m[][]={{1024,0,0,0},
		   {0,1024,0,0},
		   {0,0,1024,0},
		   {0,0,0,1024}
		};

	public Matrix4x4()
	{
	
	}
	
	public void translationMatrix(int tx, int ty, int tz)
	{
		m[0][3]=tx<<10;
		m[1][3]=ty<<10;
		m[2][3]=tz<<10;
	}
	
	public void XrotationMatrix(int a)
	{
		m[1][1]= GameCanvas.cos(a);
		m[1][2]=-GameCanvas.sin(a);
		m[2][1]= GameCanvas.sin(a);
		m[2][2]= GameCanvas.cos(a);
	}
	
	public void YrotationMatrix(int a)
	{
		m[0][0]= GameCanvas.cos(a);
		m[0][2]= GameCanvas.sin(a);
		m[2][0]=-GameCanvas.sin(a);
		m[2][2]= GameCanvas.cos(a);
	}	
	
	public void ZrotationMatrix(int a)
	{
		m[0][0]= GameCanvas.cos(a);
		m[0][1]=-GameCanvas.sin(a);
		m[1][0]= GameCanvas.sin(a);
		m[1][1]= GameCanvas.cos(a);
	}	
	
	public Matrix4x4 product(Matrix4x4 mat)
	{
		long v;
		Matrix4x4 mat2=new Matrix4x4();
		
		for(int i=0; i<4; i++)
		for(int j=0; j<4; j++){
			
			v=0;			
			for(int k=0; k<4; k++){
				v+=(m[k][i]*mat.m[j][k]);
				//System.out.println(""+m[k][i]+"x"+mat.m[j][k]+"="+v);
			}
			
			mat2.m[j][i]=(int)(v>>10);
		}	
							
		return mat2;
	}
	
	/*public void dump()
	{
		for(int i=0; i<4; i++){
		for(int j=0; j<4; j++)
			System.out.print(m[i][j]+",");
		System.out.println("");
		}
		System.out.println("");				
	}*/
	
}