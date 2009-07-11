/*
 * Copyright (C) 2009 The Analog Compass
 * 
 * This file is part of Analog Compass 
 * 
 * Analalog Compass is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this source code; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package de.rothbayern.android.ac;

import android.app.Activity;
import android.content.SharedPreferences;

public class Settings {
	
	public static final String PREFS_KEY_COMPASS_LAYOUT = "compass.layout";
	public static final String PREFS_KEY_COMPASS_OFFSET = "compass.offset";

	private static final String PREFS_NAME = "preferences";
	
	
	public static int getInt(Activity activity, String key) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME,ACActivity.MODE_PRIVATE);
		int intVal = settings.getInt(key, 0);
		return intVal;
	}

	public static float getFloat(Activity activity, String key) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME,ACActivity.MODE_PRIVATE);
		float floatVal = settings.getFloat(key, 0);
		return floatVal;
	}

	public static void setFloat(Activity activity, String key, float val) {
		SharedPreferences prefernces = activity.getSharedPreferences(PREFS_NAME,Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefernces.edit();
		editor.putFloat(key, val);
		editor.commit();
	}

	public static void setInt(Activity activity, String key, int val) {
		SharedPreferences prefernces = activity.getSharedPreferences(PREFS_NAME,Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefernces.edit();
		editor.putInt(key, val);
		editor.commit();
	}

}
