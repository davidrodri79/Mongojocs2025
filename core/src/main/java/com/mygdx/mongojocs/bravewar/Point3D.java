package com.mygdx.mongojocs.bravewar;

public class Point3D {

	int x, y, z;
	
	public static int CANVX, CANVY, SCALE;
	public static Point3D center;
	public boolean ok;
			
	public Point3D()		
	{
		
	}

	public Point3D(int xx, int yy)
	{
		x=xx; y=yy; ok=true;			
	}
			
	public Point3D(int xx, int yy, int zz)
	{
		x=xx; y=yy; z=zz;			
	}
	
	public static void constantInit(int cx, int cy)
	{
		CANVX=cx; CANVY=cy; SCALE=12*CANVX/176; center=new Point3D(cx/2,cy/2);			
	}
	
	public Point3D transform2D(Camera cam)
	{
		Point3D p;
		Point3D np;
		long x2, y2, zz;
		
						
		np=this.applyMatrix4x4(cam.mat);
										
		zz=np.z/32;				
		if(zz==0) zz=1;
				
		x2=center.x+((SCALE*np.x)/zz);
		y2=center.y-((SCALE*np.y)/zz);
		
		p=new Point3D((int)x2,(int)y2);
		
		//System.out.println("("+x+","+y+","+z+")->("+x2+","+y2+")");
		if(zz<0) p.ok=false;
	
		return p;
	}
	
	public Point3D applyMatrix4x4(Matrix4x4 m)
	{
		Point3D p=new Point3D();
		long xx, yy, zz;
		
		xx=m.m[0][0]*x+m.m[0][1]*y+m.m[0][2]*z+m.m[0][3]*1;
		yy=m.m[1][0]*x+m.m[1][1]*y+m.m[1][2]*z+m.m[1][3]*1;
		zz=m.m[2][0]*x+m.m[2][1]*y+m.m[2][2]*z+m.m[2][3]*1;
	
		p.x=(int)(xx>>10);
		p.y=(int)(yy>>10);			
		p.z=(int)(zz>>10);
		
		return p;
		
	}
	
	public static Point3D normalVector( Point3D p1, Point3D p2, Point3D p3)
	{
		Point3D n=new Point3D(), u=new Point3D(), v=new Point3D();
		long m, aux;
		
		u.x=p2.x-p1.x; u.y=p2.y-p1.y; u.z=p2.z-p1.z;
		v.x=p3.x-p2.x; v.y=p3.y-p2.y; v.z=p3.z-p2.z;		
		
		n.x=(u.y*v.z-v.y*u.z)>>10;
		n.y=(v.x*u.z-u.x*v.z)>>10;
		n.z=(u.x*v.y-v.x*u.y)>>10;
		
		

		n.unit();
								
		return n;
	}
	
	public void unit()
	{
		long aux;
		int m;
		
		aux=(x*x)+(y*y)+(z*z);	

		m=(int)sqrt((int)aux);
		
		x=(x<<10)/m;
		y=(y<<10)/m;
		z=(z<<10)/m;			
	}
		
	public long scalarProduct(Point3D p)
	{
		return ((x*p.x)+(y*p.y)+(z*p.z))>>10;	
	}
	
	
	//Square root stuff
      final static int[] table = {
         0,    16,  22,  27,  32,  35,  39,  42,  45,  48,  50,  53,  55,  57,
         59,   61,  64,  65,  67,  69,  71,  73,  75,  76,  78,  80,  81,  83,
         84,   86,  87,  89,  90,  91,  93,  94,  96,  97,  98,  99, 101, 102,
         103, 104, 106, 107, 108, 109, 110, 112, 113, 114, 115, 116, 117, 118,
         119, 120, 121, 122, 123, 124, 125, 126, 128, 128, 129, 130, 131, 132,
         133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 144, 145,
         146, 147, 148, 149, 150, 150, 151, 152, 153, 154, 155, 155, 156, 157,
         158, 159, 160, 160, 161, 162, 163, 163, 164, 165, 166, 167, 167, 168,
         169, 170, 170, 171, 172, 173, 173, 174, 175, 176, 176, 177, 178, 178,
         179, 180, 181, 181, 182, 183, 183, 184, 185, 185, 186, 187, 187, 188,
         189, 189, 190, 191, 192, 192, 193, 193, 194, 195, 195, 196, 197, 197,
         198, 199, 199, 200, 201, 201, 202, 203, 203, 204, 204, 205, 206, 206,
         207, 208, 208, 209, 209, 210, 211, 211, 212, 212, 213, 214, 214, 215,
         215, 216, 217, 217, 218, 218, 219, 219, 220, 221, 221, 222, 222, 223,
         224, 224, 225, 225, 226, 226, 227, 227, 228, 229, 229, 230, 230, 231,
         231, 232, 232, 233, 234, 234, 235, 235, 236, 236, 237, 237, 238, 238,
         239, 240, 240, 241, 241, 242, 242, 243, 243, 244, 244, 245, 245, 246,
         246, 247, 247, 248, 248, 249, 249, 250, 250, 251, 251, 252, 252, 253,
         253, 254, 254, 255
      };
   
      static int sqrt(int x) {
         int xn;
      
         if (x >= 0x10000) {
            if (x >= 0x1000000) {
               if (x >= 0x10000000) {
                  if (x >= 0x40000000) {
                     if (x >= 65535*65535) {
                        return 65535;
                     }
                  
                     xn = table[x >> 24] << 8;
                  } 
                  else
                  {
                     xn = table[x >> 22] << 7;
                  }
               }
               else {
                  if (x >= 0x4000000) {
                     xn = table[x >> 20] << 6;
                  }
                  else
                  {
                     xn = table[x >> 18] << 5;
                  }
               }
            
               xn = (xn + 1 + (x / xn)) >> 1;
               xn = (xn + 1 + (x / xn)) >> 1;
               return ((xn * xn) > x) ? --xn : xn;
            }
            else
            {
               if (x >= 0x100000) {
                  if (x >= 0x400000) {
                     xn = table[x >> 16] << 4;
                  }
                  else
                  {
                     xn = table[x >> 14] << 3;
                  }
               }
               else
               {
                  if (x >= 0x40000) {
                     xn = table[x >> 12] << 2;
                  }
                  else
                  {
                     xn = table[x >> 10] << 1;
                  }
               }
            
               xn = (xn + 1 + (x / xn)) >> 1;
            
               return ((xn * xn) > x) ? --xn : xn;
            }
         
            // return xn; // not the original spot for this line...
         }
         else
         {
            if (x >= 0x100) {
               if (x >= 0x1000) {
                  if (x >= 0x4000) {
                     xn = (table[x >> 8]     ) + 1;
                  }
                  else
                  {
                     xn = (table[x >> 6] >> 1) + 1;
                  }
               }
               else
               {
                  if (x >= 0x400) {
                     xn = (table[x >> 4] >> 2) + 1;
                  }
                  else
                  {
                     xn = (table[x >> 2] >> 3) + 1;
                  }
               }
            
               return ((xn * xn) > x) ? --xn : xn;
            } 
            else
            {
               if (x >= 0) {
                  return table[x] >> 4;
               }
               else
               {
                  return -1; // negative argument...
               }
            }
         }
      }
   
   
   /*
    * Fast Integer Square Root function...
    * Contributors include Tim Tyler, for the Java version...
    */
   
   /**
    * A *much* faster replacement for (int)(java.lang.Math.sqrt(x)).  Completely accurate for x < 289...
    */
    /*static int fast_sqrt(int x) {
         if (x >= 0x10000)
            if (x >= 0x1000000)
               if (x >= 0x10000000)
                  if (x >= 0x40000000)
                     return (table[x >> 24] << 8);
                  else
                     return (table[x >> 22] << 7);
               else if (x >= 0x4000000)
                  return (table[x >> 20] << 6);
               else
                  return (table[x >> 18] << 5);
            else if (x >= 0x100000)
               if (x >= 0x400000)
                  return (table[x >> 16] << 4);
               else
                  return (table[x >> 14] << 3);
            else if (x >= 0x40000)
               return (table[x >> 12] << 2);
            else
               return (table[x >> 10] << 1);
         else if (x >= 0x100)
            if (x >= 0x1000)
               if (x >= 0x4000)
                  return (table[x >> 8]);
               else
                  return (table[x >> 6] >> 1);
            else if (x >= 0x400)
               return (table[x >> 4] >> 2);
            else
               return (table[x >> 2] >> 3);
         else
            if (x >=0)
               return table[x] >> 4;
         return -1; // negative argument...
      }*/
   
   /**
    * Mark Borgerding's algorithm...
    * Not terribly speedy...
    */
   
   /*
      static int mborg_sqrt(int val) {
         int guess=0;
         int bit = 1 << 15;
         do {
            guess ^= bit;  
            // check to see if we can set this bit without going over sqrt(val)...
            if (guess * guess > val )
               guess ^= bit;  // it was too much, unset the bit...
         } while ((bit >>= 1) != 0);
      
         return guess;
      }
   	*/
   
   
   /** 
    * Taken from http://www.jjj.de/isqrt.cc
    * Code not tested well...
    * Attributed to: http://www.tu-chemnitz.de/~arndt/joerg.html / email: arndt@physik.tu-chemnitz.de
    * Slow.
    */
   
   /*
      final static int BITS = 32;
      final static int NN = 0;  // range: 0...BITSPERLONG/2
   
      final static int test_sqrt(int x) {
         int i;
         int a = 0;                   // accumulator...
         int e = 0;                   // trial product...
         int r;
      
         r=0;                         // remainder...
      
         for (i=0; i < (BITS/2) + NN; i++)
         {
            r <<= 2;
            r +=  (x >> (BITS - 2));
            x <<= 2;
         
            a <<= 1;
            e = (a << 1)+1;
         
            if(r >= e)
            {
               r -= e;
               a++;
            }
         }
      
         return a;
      }
   */
   
   
   /*
   // Totally hopeless performance...
      static int test_sqrt(int n) {
         float r = 2.0F;
         float s = 0.0F;
         for(; r < (float)n / r; r *= 2.0F);
         for(s = (r + (float)n / r) / 2.0F; r - s > 1.0F; s = (r + (float)n / r) / 2.0F) {
            r = s;
         }
      
         return (int)s;
      }
   	*/	
}