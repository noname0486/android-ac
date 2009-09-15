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
package de.rothbayern.android.ac.misc;

import java.io.*;
import java.util.regex.*;

import android.content.Context;
import android.content.res.Resources;
import android.text.util.Linkify;
import android.widget.TextView;


public class Util {

	public static float calcNormDiff(float lastDirection, float curSetPoint) {
		float diff = curSetPoint-lastDirection;
		return Util.normAngle(diff);
	}

	public static float normAngle(float diff) {
		while (diff < -180) {
			diff += 360;
		}
		while (diff > 180) {
			diff -= 360;
		}
		return diff;
	}


	public static String loadStringFromRawResource(Resources resources, int resId) {
		InputStream rawResource = resources.openRawResource(resId);
	    String content = streamToString(rawResource);
	    try {rawResource.close();} catch (IOException e) {}
		return content;
	}

	private static String streamToString(InputStream in) {
	    String l;
	    BufferedReader r = new BufferedReader(new InputStreamReader(in));
	    StringBuilder s = new StringBuilder();
	    try {
	        while ((l = r.readLine()) != null) {
	            s.append(l + "\n");
	        }
	    } catch (IOException e) {} 
	    return s.toString();
	}

	public static void addLink(Context pContext, TextView voteView, int linkMarkerId, int urlId) {
		String patS = pContext.getResources().getString(linkMarkerId);
		Pattern pat = Pattern.compile(patS);
		String scheme =pContext.getResources().getString(urlId);  
		 Linkify.addLinks(voteView, pat,scheme, null, new Linkify.TransformFilter(){  
				   
				 public String transformUrl(Matcher matcher, String url) {  
				 return "";  
				 }
	
				   
		 });
	}

}
