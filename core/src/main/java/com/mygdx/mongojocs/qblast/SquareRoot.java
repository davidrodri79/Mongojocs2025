package com.mygdx.mongojocs.qblast;
/*
 * Integer Square Root function
 * Contributors include Arne Steinarson for the basic approximation idea, Dann 
 * Corbit and Mathew Hendry for the first cut at the algorithm, Lawrence Kirby 
 * for the rearrangement, improvments and range optimization, Paul Hsieh 
 * for the round-then-adjust idea, and Tim Tyler, for the Java port.
 */


/**
 * A faster replacement for (int)(java.lang.Math.sqrt(x)).  Completely accurate for x < 2147483648 (i.e. 2^31)...
 */
   public class SquareRoot {
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
      static int fast_sqrt(int x) {
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
      }
   
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
