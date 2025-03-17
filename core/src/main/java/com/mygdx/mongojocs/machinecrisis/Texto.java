package com.mygdx.mongojocs.machinecrisis;

import com.mygdx.mongojocs.iapplicationemu.Graphics;

public class Texto {


	int x,y,color,id;
	String txt;
	Game m_game;
	long timer;

/******************************************************************************
 *	CONSTRUCTORA
 ******************************************************************************/
	public Texto(Game m_game, int x, int y, String txt, int id) {
		this.m_game = m_game;
		this.x = x;
		this.y = y;
		this.txt = txt;
		this.id = id;
		
		try{
			if(Integer.parseInt(txt)>0) {
				color = Graphics.getColorOfName(Graphics.RED);
				this.txt = "+"+txt;
			} else color = Graphics.getColorOfName(Graphics.GREEN);
		} catch(NumberFormatException e) {
			System.out.println("Texto::Texto() -> NaN!!");
			m_game.destroyText(id);
		}
		timer = System.currentTimeMillis();
	}

/******************************************************************************
 *	RUN - Gestion de la entrada y movimiento
 ******************************************************************************/

	public void RUN() {
		if(System.currentTimeMillis() - timer > 500) m_game.destroyText(id);
		y -= 2;
	}
	
} //CLASS Texto

