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
