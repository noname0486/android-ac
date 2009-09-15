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

package de.rothbayern.android.ac.drawings;

import de.rothbayern.android.ac.geometry.Figure;

public class DrawingComponent {
	private int pos;
	private String name;
	private Figure f;
	private int defaultColor;
	private String title;
	
	
	
	public DrawingComponent(int pos, String name, Figure f, int defaultColor, String title) {
		super();
		this.pos = pos;
		this.name = name;
		this.f = f;
		this.defaultColor = defaultColor;
		this.title = title;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Figure getF() {
		return f;
	}

	public void setF(Figure f) {
		this.f = f;
	}
	
	public int getDefaultColor() {
		return defaultColor;
	}
	
	public void setDefaultColor(int defaultColor) {
		this.defaultColor = defaultColor;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	
}
