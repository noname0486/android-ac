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

package de.rothbayern.android.ac.pref;



import android.app.Activity;
import android.content.*;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Message;
import android.preference.PreferenceManager;
import de.rothbayern.android.ac.*;

public class CompassPreferences {

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
		SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME_OLD,Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putFloat(key, val);
		editor.commit();
	}

	public static void setIntOld(Context context, String key, int val) {
		SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME_OLD,Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(key, val);
		editor.commit();
	}
	

	public int getInt(String key) {
		String sVal = prefs.getString(key, "0");
		int intVal=0;
		// ListPreference can't work with numeric values => load as string
		try {intVal = Integer.parseInt(sVal);} catch (NumberFormatException nfe) {}
		return intVal;
	}

	public int getInt(String key, int defValue) {
		String sVal = prefs.getString(key, ""+defValue);
		int intVal=0;
		// ListPreference can't work with numeric values => load as string
		try {intVal = Integer.parseInt(sVal);} catch (NumberFormatException nfe) {}
		return intVal;
	}

	public float getFloat(String key) {
		String sVal = prefs.getString(key, "0");
		float floatVal=0.0f;
		// ListPreference can't work with numeric values => load as string
		try {floatVal = Float.parseFloat(sVal);} catch (NumberFormatException nfe) {}
		return floatVal;
	}

	public String getString(String key) {
		String strVal = prefs.getString(key, "");
		return strVal;
	}

	
	public void setFloat(String key, float val) {
		SharedPreferences.Editor editor = prefs.edit();
		// ListPreference can't work with numeric values => save as string
		editor.putString(key, Float.toString(val));		 
		editor.commit();
	}

	public void setInt(String key, int val) {
		SharedPreferences.Editor editor = prefs.edit();
		// ListPreference can't work with numeric values => save as string
		editor.putString(key, Integer.toString(val));
		editor.commit();
	}
	
	public void setString(String key, String val) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key, val);
		editor.commit();
	}
	
	
	

	
	
	
	// Singleton for Preferences
	private ACActivity theContext;
	private SharedPreferences prefs;
	private static CompassPreferences thePrefernces=null;
	private CompassPreferences(ACActivity c){
		theContext = c;
		prefs=PreferenceManager.getDefaultSharedPreferences(theContext);
		PREFS_COMPASS_LAYOUT_KEY=c.getString(R.string.prefs_layout_key);
		PREFS_COMPASS_BACKGROUNDCOLOR_KEY=c.getString(R.string.prefs_backgroundcolor_key);
		PREFS_COMPASS_OFFSET_KEY=c.getString(R.string.prefs_offset_key);		
		PREFS_COMPASS_SPEED_KEY=c.getString(R.string.prefs_speed_key);		
		PREFS_COMPASS_VERSION_KEY=c.getString(R.string.prefs_version_key);		
	}
	
	public static CompassPreferences getPreferences(ACActivity c){
		if(thePrefernces==null){
			thePrefernces = new CompassPreferences(c);
		}
		return(thePrefernces);
	}
	
	
	public static CompassPreferences getPreferences(){
		if(thePrefernces==null){
			throw new IllegalStateException("Problem with preferences");
		}
		return(thePrefernces);
	}

	public void checkVersion() {
		String oldVersion = getString(PREFS_COMPASS_VERSION_KEY);
		String newVersion = fetchVersionName();
		
		
		
		// Old format of preferences => migrate to new one
		if(oldVersion.equals("")){
			oldVersion = "0.9.0";
			float offset = getFloatOld(theContext, PREFS_COMPASS_OFFSET_KEY);
			setFloat(PREFS_COMPASS_OFFSET_KEY, offset);
			int layout = getIntOld(theContext, PREFS_COMPASS_LAYOUT_KEY);
			setInt(PREFS_COMPASS_LAYOUT_KEY, layout);
			
			setInt(PREFS_COMPASS_BACKGROUNDCOLOR_KEY, Color.WHITE);
			setInt(PREFS_COMPASS_SPEED_KEY, 1);
		}
		
		
		if(!oldVersion.equals(newVersion)){
			Message m = new Message();
			m.what=ACActivity.MSG_SHOW_CHANGE_VERSION;
			m.obj=new String[] {oldVersion,newVersion};
			theContext.myHandler.sendMessageDelayed(m, 1900);

			setString(PREFS_COMPASS_VERSION_KEY, newVersion);
		}
		
	}


	
	private String fetchVersionName() {
		String versionName = null;
		try {
			versionName = theContext.getPackageManager().getPackageInfo(theContext.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			versionName = "x.y.z";
		}
		return versionName;
	}
	
	
	
	
	

}
