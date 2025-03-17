package com.mygdx.mongojocs.poyogarden;


import com.mygdx.mongojocs.midletemu.Graphics;

class mariposa
{
    boolean active;
    int timing;	
    int x, y, sx, sy;
	final int DESP = 100;
    byte off[] = {0,1,2,3,3,3,2,1,0,0,0,-1,-2,-3,-3,-3,-2,-1,0,0};
    int col[] = {0xffff80, 0xff6060, 0x60ff60, 0x6060ff, 0xff00ff};
	int sel;
	
	public mariposa()
	{			
	}


		
    public void Update(unit prota, int tilesleft)
    {
        if (!active)
        {
            if (tilesleft < 90 && prota.tag == 14)
            {
                x = prota.x+8;
                y = prota.y+14;
                sx = 0;
                sy = 0;
                active = true;                 
                timing = 0;
                sel = Math.abs(Game.rnd.nextInt())%col.length;
            }
            return;
        }
   	    timing++;
   	    sx += Game.rnd.nextInt()%DESP;
   	    sy += Game.rnd.nextInt()%DESP;
   	    if ((Math.abs(prota.x - x) < 64 && Math.abs(prota.y - y) < 64) &&
   	        (Math.abs(prota.x - x) > 16 && Math.abs(prota.y - y) > 16))
   	    {
       	    if (prota.x < x) sx -= DESP;
       	    if (prota.x > x) sx += DESP;
       	    if (prota.y < y) sy -= DESP;
       	    if (prota.y > y) sy += DESP;
   	    }
   	
   	    int tx = x + (sx>>10);
   	    int ty = y + (sy>>10);
   	    if (14 != Game.GetTile(tx,y)) sx = -sx;
   	    if (14 != Game.GetTile(x,ty)) sy = -sy;
   	    
   	    x += sx>>10;
   	    y += sy>>10;
   	    if (timing > 200 || x < 0 || x > Game.MAXTX*8 || y < 0 || y > Game.MAXTY*8) active = false;        
   }
   
   
   public void blit(Graphics gr)
   {
        if (!active) return;   
        if (Game.offsetx+x > Game.cw || Game.offsety+y > Game.ch || Game.offsetx+x < -3 || Game.offsety+y < -2) return;
        gr.setColor(col[sel]);
        gr.setClip(0, 0, Game.cw, Game.ch);
        int tx = Game.offsetx+x+ off[(timing+5)%off.length];        
        int ty = Game.offsety+y+ off[timing%off.length];        
        if (timing%2 == 0)
        {
            gr.drawLine(tx,ty,tx,ty+2);
            gr.drawLine(tx+1,ty+1,tx+1,ty+1);
            gr.drawLine(tx+2,ty,tx+2,ty+2);
        }
        else
            gr.drawLine(tx+1,ty,tx+1,ty+2);
   }
   
}