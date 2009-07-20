/*
 * Copyright (C) 2009 Analog Compass
 * 
 * This file is part of Analog Compass 
 * 
 * Analog Compass is free software; you can redistribute it and/or modify
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


import android.app.*;
import android.content.*;
import android.preference.PreferenceManager;

public class Preferences {

	public final String PREFS_COMPASS_VERSION_KEY;

	public final String PREFS_COMPASS_OFFSET_KEY;

	public final String PREFS_COMPASS_LAYOUT_KEY;
	public final String PREFS_COMPASS_BACKGROUNDCOLOR_KEY;
	public final String PREFS_COMPASS_SPEED_KEY;
	
	
	// First Time I don't know. 
	// Now using standard preferences to get work with PreferencesView 
	private static final String PREFS_NAME_OLD = "preferences";
	
	
	public static int getIntOld(Context context, String key) {
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME_OLD,ACActivity.MODE_PRIVATE);
		int intVal = settings.getInt(key, 0);
		return intVal;
	}

	public static float getFloatOld(Context context, String key) {
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME_OLD,ACActivity.MODE_PRIVATE);
		float floatVal = settings.getFloat(key, 0);
		return floatVal;
	}

	public static void setFloatOld(Context context, String key, float val) {
		SharedPreferences prefernces = context.getSharedPreferences(PREFS_NAME_OLD,Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefernces.edit();
		editor.putFloat(key, val);
		editor.commit();
	}

	public static void setIntOld(Context context, String key, int val) {
		SharedPreferences prefernces = context.getSharedPreferences(PREFS_NAME_OLD,Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefernces.edit();
		editor.putInt(key, val);
		editor.commit();
	}
	

	public int getInt(String key) {
		int intVal = prefs.getInt(key, 0);
		return intVal;
	}

	public float getFloat(String key) {
		float floatVal = prefs.getFloat(key, 0.0f);
		return floatVal;
	}

	public String getString(String key) {
		String strVal = prefs.getString(key, "");
		return strVal;
	}

	public void setFloat(String key, float val) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putFloat(key, val);
		editor.commit();
	}

	public void setInt(String key, int val) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(key, val);
		editor.commit();
	}
	
	public void setString(String key, String val) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key, val);
		editor.commit();
	}
	

	
	
	
	// Singleton for Preferences
	private Context theContext;
	private SharedPreferences oldPrefs;
	private SharedPreferences prefs;
	private static Preferences thePrefernces=null;
	private Preferences(Context c){
		theContext = c;
		oldPrefs = theContext.getSharedPreferences(PREFS_NAME_OLD,ACActivity.MODE_PRIVATE);
		prefs=PreferenceManager.getDefaultSharedPreferences(theContext);
		PREFS_COMPASS_LAYOUT_KEY=c.getString(R.string.prefs_layout_key);
		PREFS_COMPASS_BACKGROUNDCOLOR_KEY=c.getString(R.string.prefs_backgroundcolor_key);
		PREFS_COMPASS_OFFSET_KEY=c.getString(R.string.prefs_offset_key);		
		PREFS_COMPASS_SPEED_KEY=c.getString(R.string.prefs_speed_key);		
		PREFS_COMPASS_VERSION_KEY=c.getString(R.string.prefs_version_key);		
	}
	
	public static Preferences getPreferences(Context c){
		if(thePrefernces==null){
			thePrefernces = new Preferences(c);
		}
		return(thePrefernces);
	}
	
	
	public static Preferences getPreferences(){
		if(thePrefernces==null){
			throw new IllegalStateException("Problem with preferences");
		}
		return(thePrefernces);
	}

	public void checkVersion() {
		String oldVersion = getString(PREFS_COMPASS_VERSION_KEY);
		String newVersion = theContext.getString(R.string.prefs_version);
		
		// Old format of preferences => migrate to new one
		if(oldVersion.equals("")){
			float offset = getFloatOld(theContext, PREFS_COMPASS_OFFSET_KEY);
			setFloat(PREFS_COMPASS_OFFSET_KEY, offset);
			int layout = getIntOld(theContext, PREFS_COMPASS_LAYOUT_KEY);
			setInt(PREFS_COMPASS_LAYOUT_KEY, layout);
			
		}
		
		if(!oldVersion.equals(newVersion)){
			doChangeVersion(oldVersion,newVersion);
			setString(PREFS_COMPASS_VERSION_KEY, newVersion);
		}
		
	}

	private void doChangeVersion(String oldVersion, String newVersion) {
		AlertDialog.Builder builder = new AlertDialog.Builder(theContext);
		builder.setTitle("Achtung");
		builder.setMessage(oldVersion +" => "+newVersion);
		builder.setNeutralButton("Schliessen", null);

		AlertDialog alert = builder.create();
		alert.show();

		
	}
	
	
	
	
	
	

}
