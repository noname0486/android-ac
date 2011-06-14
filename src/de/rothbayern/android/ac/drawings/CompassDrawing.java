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


import android.graphics.Bitmap;
import de.rothbayern.android.ac.pref.CompassPreferences;

public abstract class CompassDrawing {
	public static final float MAX = 256;
	public abstract Bitmap getDrawing(int width, int height);
	public void setColorPreference(DrawingComponent c, int color) {
		CompassPreferences prefs = CompassPreferences.getPreferences();
		String prefName = toPrefName(c);
		prefs.setInt(prefName, color);
	}

	public int getColorPreference(DrawingComponent c) {
		CompassPreferences prefs = CompassPreferences.getPreferences();
		String prefName = toPrefName(c);
		int prColor = prefs.getInt(prefName,-2);
		if(prColor==-2){
			prColor = c.getDefaultColor();
		}
		return(prColor);
	}
	
	public DrawingComponent searchByName(String name){
		DrawingComponent dcs[] = getComponents();
		for (int i = 0; i < dcs.length; i++) {
			if(dcs[i].getName().equals(name)){
				return(dcs[i]);
			}
		}
		return(null);
	}

	public String toPrefName(DrawingComponent c) {
		StringBuilder res = new StringBuilder(30);
		res.append(getPrefNamePrefix());
		res.append("_");
		res.append(c.getName());
		return (res.toString());
	}

	public abstract String getPrefNamePrefix();
	public abstract DrawingComponent[] getComponents();
	
}
