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
