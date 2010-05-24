package de.rothbayern.android.ac.geometry;

import android.graphics.*;

public class FRect implements Figure {

	private float top;
	private float left;
	private float width;
	private float height;
	
	public static FRect fromTopLeft(float left, float top, float width, float height){
		return new FRect(left,top,width,height);
	}
	
	public static FRect fromMiddle(float x, float y, float length){
		float l2 = length/2;
		return new FRect(x-l2,y-l2,length,length);
	}
	
	public static FRect fromMiddle(float x, float y, float width, float height){
		float w2 = width/2;
		float h2 = height/2;
		return new FRect(x-w2,y-h2,width,height);
	}
	
	private FRect(float left, float top, float width, float height) {
		this.top = top;
		this.left = left;
		this.width = width;
		this.height = height;
	}

	@Override
	public boolean contains(float x, float y) {
		boolean xOk = x>=left && x <=left+width;
		boolean yOk = y>=top && y <=top+height;
		return(yOk&&xOk);
		
	}
	
	@Override
	public String toString() {
		StringBuilder res = new StringBuilder(100);
		res.append("FRect[");
		res.append(left);
		res.append(", ");
		res.append(top);
		res.append(", ");
		res.append(width);
		res.append(", ");
		res.append(height);
		res.append("]");
		return(res.toString());

	}

	@Override
	public void draw(Canvas c, Paint p) {
		c.drawRect(left, top, left+width, top+height, p);
	}

	@Override
	public float getMiddleX() {
		return(left+width/2);
	}

	@Override
	public float getMiddleY() {
		return(top+height/2);
	}
	
	
	
	

}
