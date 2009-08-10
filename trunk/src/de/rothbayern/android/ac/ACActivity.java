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


import de.rothbayern.android.ac.pref.*;
import android.app.*;
import android.content.*;
import android.content.res.Resources;
import android.hardware.*;
import android.os.Bundle;
import android.view.*;

public class ACActivity extends Activity {
 
	// Controls
	private CompassView viewCompass;		
	private SensorManager mSensorManager;
	
	/** Build activity */
	@Override
	public void onCreate(Bundle savedState) { 
		super.onCreate(savedState);

		// Do first to init preferences (Singleton)
		Preferences prefs = Preferences.getPreferences(this);
		prefs.checkVersion();

		
		setContentView(R.layout.main);
		
		
		


		// Prepare for start
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		viewCompass = (CompassView) findViewById(R.id.viewWorld);
		
		takePreferences();

	}


	@Override
	protected void onResume() {
		super.onResume();
		
		// Start Display
		mSensorManager.registerListener(mListener, mSensorManager
				.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_NORMAL);
		viewCompass.startAnim();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Stop display
		mSensorManager.unregisterListener(mListener);
		viewCompass.stopAnim();
	}

	private final SensorEventListener mListener = new SensorEventListener() {

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// Do the best. Do nothing.
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			// Get direction and adjust with calibration value  
			float values[] = event.values;
			float value = values[0];
			
			viewCompass.setDirection(value);
		}
	};

	// Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options, menu);
		return true;
	}

	private static final int CALIBRATION_ACTIVITY_REQUEST = 100;
	private static final int PREFERENCES_ACTIVITY_REQUEST = 101;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CALIBRATION_ACTIVITY_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
			float offset = data.getFloatExtra(CalibrationActivity.RESULT_NAME_OFFSET,0.0f);
			Preferences prefs = Preferences.getPreferences();
			prefs.setFloat(prefs.PREFS_COMPASS_OFFSET_KEY, offset);
			takePreferences();
		}
		if (requestCode == PREFERENCES_ACTIVITY_REQUEST) {
			takePreferences();			
		}
		
	}


	private void takePreferences() {
		Preferences prefs = Preferences.getPreferences();
		
		int bgColor = prefs.getInt(prefs.PREFS_COMPASS_BACKGROUNDCOLOR_KEY);
		viewCompass.setBgColor(bgColor);
		
		int rose = prefs.getInt(prefs.PREFS_COMPASS_LAYOUT_KEY);
		viewCompass.setCompassLayout(rose);
			
		int speed = prefs.getInt(prefs.PREFS_COMPASS_SPEED_KEY);
		viewCompass.setSpeed(speed);

		float offset = prefs.getFloat(prefs.PREFS_COMPASS_OFFSET_KEY);
		viewCompass.setOffset(offset);
		
		
	}
 
	/* Handles item selections */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mnuCalibrate: {
			// Start Calibration
			viewCompass.stopAnim();
			Intent intent = new Intent(this, CalibrationActivity.class);
			Preferences prefs = Preferences.getPreferences();
			intent.putExtra("offset",prefs.getFloat(prefs.PREFS_COMPASS_OFFSET_KEY) );
			startActivityForResult(intent, CALIBRATION_ACTIVITY_REQUEST);
			// Look at this.onActivityResult() for result.
			return true;
		}
		case R.id.mnuLayout:{
			Intent intent = new Intent(this, PreferencesActivity.class);
			startActivityForResult(intent, PREFERENCES_ACTIVITY_REQUEST);
			//startActivity(intent);
			return true;
		}
		case R.id.mnuInfo: {
			Intent intent = new Intent(this, InfoActivity.class);
			startActivity(intent);
			return true;
		}
		}
		return false;
	}

}