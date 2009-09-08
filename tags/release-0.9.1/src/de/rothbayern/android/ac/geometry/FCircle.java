package de.rothbayern.android.ac.geometry;

import android.graphics.*;

public class FCircle implements Figure {

	private float x;
	private float y;
	private float r;
	
	
	
	public FCircle(float x, float y, float r) {
		this.x = x;
		this.y = y;
		this.r = r;
	}



	@Override
	public boolean contains(float pX, float pY) {
		float dx = pX-x;
		float dy = pY-y;
		float d2 = dx*dx+dy*dy;
		return(d2<=r*r);
	}



	@Override
	public void draw(Canvas c, Paint p) {
		c.drawCircle(x, y, r, p);
		
	}



	@Override
	public float getMiddleX() {
		return x;
	}



	@Override
	public float getMiddleY() {
		return y;
	}




}
