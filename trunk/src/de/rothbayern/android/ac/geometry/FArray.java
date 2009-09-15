/*
 *  "Analog Compass" is an application for devices based on android os. 
 *  The application shows the orientation based on the intern magnetic sensor.   
 *  Copyright (C) 2009  Dieter Roth
 *
 *  This program is free software; you can redistribute it and/or modify it under the terms of the 
 *  GNU General Public License as published by the Free Software Foundation; either version 3 of 
 *  the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 *  See the GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License along with this program; 
 *  if not, see <http://www.gnu.org/licenses/>.
 */

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
