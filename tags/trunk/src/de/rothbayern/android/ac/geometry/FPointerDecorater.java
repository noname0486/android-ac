package de.rothbayern.android.ac.geometry;

import android.graphics.*;

public class FPointerDecorater implements Figure {

	private Figure f;
	private float toX;
	private float toY;
	
	
	public FPointerDecorater(Figure f, float toX, float toY) {
		this.f = f;
		this.toX = toX;
		this.toY = toY;
	}

	@Override
	public boolean contains(float pX, float pY) {
		return(f.contains(pX, pY));
	}



	@Override
	public void draw(Canvas c, Paint p) {
		f.draw(c, p);
		c.drawLine(f.getMiddleX(), f.getMiddleY(), toX, toY, p);
		
	}

	@Override
	public float getMiddleX() {
		return(f.getMiddleX());
	}

	@Override
	public float getMiddleY() {
		return(f.getMiddleY());
	}




}
