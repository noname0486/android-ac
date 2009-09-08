package de.rothbayern.android.ac.geometry;

import android.graphics.*;

public class FArray implements Figure{

	Figure figures[] = null;
	int pos = 0;
	public FArray(int length) {
		figures = new Figure[length];
	}
	
	public void add(Figure f){
		figures[pos]=f;
		pos++;
	}
	
	@Override
	public boolean contains(float x, float y) {
		int len = figures.length;
		for (int i = 0; i < len; i++) {
			Figure cur = figures[i]; 
			if(cur!=null && cur.contains(x, y)){
				return(true);
			}
		}
		return false;
	}

	@Override
	public void draw(Canvas c, Paint p) {
		int len = figures.length;
		for (int i = 0; i < len; i++) {
			Figure cur = figures[i]; 
			if(cur!=null){
				cur.draw(c, p);
			}
		}
	}

	@Override
	public float getMiddleX() {
		if(pos>0) return figures[0].getMiddleX();
		return(0.0f);
	}

	@Override
	public float getMiddleY() {
		if(pos>0) return figures[0].getMiddleY();
		return(0.0f);
	}

}
