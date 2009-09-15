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
