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
	private float offset = 0;				// Offset in degree for direction
	
	/** Build activity */
	@Override
	public void onCreate(Bundle savedState) { 
		super.onCreate(savedState);

		// Do first to init preferences (Singleton)
		Preferences prefs = Preferences.getPreferences(this);
		prefs.checkVersion();

		
		setContentView(R.layout.main);
		
		
		

		// Get settings
		int cLayout = prefs.getInt(prefs.PREFS_COMPASS_LAYOUT_KEY);
		offset = prefs.getFloat(prefs.PREFS_COMPASS_OFFSET_KEY);
		int bgColor = prefs.getInt(prefs.PREFS_COMPASS_BACKGROUNDCOLOR_KEY);

		// Prepare for start
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		viewCompass = (CompassView) findViewById(R.id.viewWorld);
		viewCompass.setCompassLayout(cLayout);
		viewCompass.setBgColor(bgColor);

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
			float value = values[0]+offset;
			
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
			offset = data.getFloatExtra(CalibrationActivity.RESULT_NAME_OFFSET,0.0f);
			Preferences prefs = Preferences.getPreferences();
			prefs.setFloat(prefs.PREFS_COMPASS_OFFSET_KEY, offset);
		}
		if (requestCode == PREFERENCES_ACTIVITY_REQUEST) {
			Preferences prefs = Preferences.getPreferences();
			int bgColor = prefs.getInt(prefs.PREFS_COMPASS_BACKGROUNDCOLOR_KEY);
			viewCompass.setBgColor(bgColor);
			
			int rose = prefs.getInt(prefs.PREFS_COMPASS_LAYOUT_KEY);
			viewCompass.setCompassLayout(rose);
		}
		
	}
 
	/* Handles item selections */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mnuCalibrate: {
			// Start Calibration
			viewCompass.stopAnim();
			Intent intent = new Intent(this, CalibrationActivity.class);
			intent.putExtra("offset", offset);
			startActivityForResult(intent, CALIBRATION_ACTIVITY_REQUEST);
			// Look at this.onActivityResult() for result.
			return true;
		}
		case R.id.mnuLayout:{
			Intent intent = new Intent(this, PreferencesActivity.class);
			startActivityForResult(intent, PREFERENCES_ACTIVITY_REQUEST);
			//startActivity(intent);
			askLayout();
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

	/*
	 * Change Layout
	 */
	private void askLayout() {

	/*	
		Resources resources = getResources();
		final CharSequence[] items = resources.getTextArray(R.array.layouts);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.msg_PickALayout);

		// TODO lets work with icons
		builder.setSingleChoiceItems(items, viewCompass.getCompassLayout(),
				new AskLayoutListener());
		AlertDialog alert = builder.create();
		alert.show();
	*/
	}

	private void saveLayout(int compassLayout) {
		viewCompass.setCompassLayout(compassLayout);
		Preferences prefs = Preferences.getPreferences();
		prefs.setInt(prefs.PREFS_COMPASS_LAYOUT_KEY, compassLayout);

	}

	class AskLayoutListener implements DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int item) {
			ACActivity.this.saveLayout(item);
			dialog.dismiss();
		}
	}

}