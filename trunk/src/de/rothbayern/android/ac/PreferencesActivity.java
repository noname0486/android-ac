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

package de.rothbayern.android.ac;


import de.rothbayern.android.ac.pref.CompassPreferences;
import afzkl.development.mColorPicker.ColorPickerActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.*;

public class PreferencesActivity extends PreferenceActivity {
     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          addPreferencesFromResource(R.xml.preferences);
          setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // for portrait 
     }
     
      
     @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
     Resources resources = this.getResources();
     final String BACKGROUNDCOLOR_KEY = resources.getString(R.string.prefs_backgroundcolor_key);
     final String ROBAS_ROSE_KEY = resources.getString(R.string.prefs_color_robas_rose_key);
	 final String ROBAS_NEEDLE_KEY = resources.getString(R.string.prefs_color_robas_needle_key);
	 final String STEFAN_KEY = resources.getString(R.string.prefs_stefans_color_key);
   	 final String NICOLAS_ROSE_KEY = resources.getString(R.string.prefs_color_nicolas_rose_key);
	 final String NICOLAS_NEEDLE_KEY = resources.getString(R.string.prefs_color_nicolas_needle_key);

	 
	 if(preference.getKey().equals(BACKGROUNDCOLOR_KEY)){
		 	Intent i = new Intent(this, ColorPickerActivity.class);
		 	int startcolor = CompassPreferences.getPreferences().getInt(BACKGROUNDCOLOR_KEY);
			i.putExtra(ColorPickerActivity.INTENT_DATA_INITIAL_COLOR, startcolor);
			i.putExtra(ColorPickerActivity.INTENT_PREF_KEY, resources.getString(R.string.prefs_backgroundcolor_key));
			i.putExtra(ColorPickerActivity.INTENT_TITLE, resources.getString(R.string.prefs_backgroundcolor_dialog_title));
			startActivityForResult(i, PREF_COLOR_ACTIVITY_REQUEST);
			return(true);
	 }
	 if(preference.getKey().equals(ROBAS_ROSE_KEY)){
			Intent intent = new Intent(this, ColorsActivity.class);
			intent.putExtra("key", CompassViewHelper.LAYOUT_ROBA);
			startActivityForResult(intent, COLORS_ACTIVITY_REQUEST);
			return(true);
 	 }
	 if(preference.getKey().equals(ROBAS_NEEDLE_KEY)){
			Intent intent = new Intent(this, ColorsActivity.class);
			intent.putExtra("key", CompassViewHelper.LAYOUT_NEEDLE);
			startActivityForResult(intent, COLORS_ACTIVITY_REQUEST);
			return(true);
	 }
	 if(preference.getKey().equals(STEFAN_KEY)){
			Intent intent = new Intent(this, ColorsActivity.class);
			intent.putExtra("key", CompassViewHelper.LAYOUT_STEFAN);
			startActivityForResult(intent, COLORS_ACTIVITY_REQUEST);
			return(true);
 	 }
	 if(preference.getKey().equals(NICOLAS_ROSE_KEY)){
			Intent intent = new Intent(this, ColorsActivity.class);
			intent.putExtra("key", CompassViewHelper.LAYOUT_NICOLAS);
			startActivityForResult(intent, COLORS_ACTIVITY_REQUEST);
			return(true);
	 }
	 if(preference.getKey().equals(NICOLAS_NEEDLE_KEY)){
			Intent intent = new Intent(this, ColorsActivity.class);
			intent.putExtra("key", CompassViewHelper.LAYOUT_NICOLAS_NEEDLE);
			startActivityForResult(intent, COLORS_ACTIVITY_REQUEST);
			return(true);
	 }
	
	 
    	return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
     
  	private static final int COLORS_ACTIVITY_REQUEST = 102;
 	private static final int PREF_COLOR_ACTIVITY_REQUEST = 102;
 	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == COLORS_ACTIVITY_REQUEST) {
//			if(Config.LOGD){
//				Log.d("color activity","closed");
//			}

		}
		if (requestCode == PREF_COLOR_ACTIVITY_REQUEST) {
			
			
			if(resultCode == RESULT_OK ){
				String key = data.getStringExtra(ColorPickerActivity.INTENT_PREF_KEY);
				if ( key != null && !key.equals("")) {
					int newColor = data.getIntExtra(ColorPickerActivity.RESULT_COLOR, 0);
					CompassPreferences.getPreferences().setInt(key, newColor);
				}
			}

		}
		
	}


}