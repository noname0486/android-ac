package de.rothbayern.android.ac.geometry;

import android.graphics.*;

public class FRing implements Figure {

	private float x;
	private float y;
	private float r1;
	private float r2;
	
	
	
	public FRing(float x, float y, float r1, float r2) {
		this.x = x;
		this.y = y;
		this.r1 = r1;
		this.r2 = r2;
	}



	@Override
	public boolean contains(float pX, float pY) {
		float dx = pX-x;
		float dy = pY-y;
		float d2 = dx*dx+dy*dy;
		return(d2>=r1*r1 && d2<=r2*r2);
	}
	
	@Override
	public void draw(Canvas c, Paint p) {
		c.drawCircle(x, y, r1, p);
		c.drawCircle(x, y, r2, p);
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
