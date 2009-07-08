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
