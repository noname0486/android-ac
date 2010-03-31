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

import android.app.*;
import android.content.*;
import android.os.*;
import android.text.*;
import android.view.*;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;
import de.rothbayern.android.ac.camera.CameraCompassView;
import de.rothbayern.android.ac.misc.Util;
import de.rothbayern.android.ac.pref.CompassPreferences;


/**
 * @author Dieter Roth
 *
 * Main activity which holds the compass.
 */
public class ACActivity extends Activity {

	// Controls
	private IAnimCompass compassView;
	private SmoothDirectionProducer animThread;
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.exit(0);		// Close if there are any resource left. 
	}

	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);
//		Log.d("cycle","onCreate"+this);
		// Do first to init preferences. This is a singleton.
		CompassPreferences prefs = CompassPreferences.getPreferences(this);
		prefs.checkVersion();

		
		setContentView(R.layout.main);
		ViewGroup viewGroup = (ViewGroup) findViewById(R.id.layoutAnimCompass);

		if(viewGroup==null){
		  CameraCompassView cv = new CameraCompassView(this);
		  viewGroup.addView(cv, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		  compassView = cv;
		}
		else {
			  CompassSurfaceView cv = new CompassSurfaceView(this);
			  viewGroup.addView(cv, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			  compassView = cv;
		}
		
		// load preferences
		takePreferences();
		Toast.makeText(this, R.string.hint_tap_settings, Toast.LENGTH_LONG).show();
		
		

	}

	// Show messages boxes at start time
	// messages are delayed, showed only once and loosely bound to the activity 
	private static final int MSG_SHOW_NO_HARDWARE_COMPASS = 100;
	public static final int MSG_SHOW_CHANGE_VERSION = 101;
	public Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MSG_SHOW_NO_HARDWARE_COMPASS: {
					showWarnNoHardwareCompass();
					break;
				}
				case MSG_SHOW_CHANGE_VERSION: {
					String params[] = (String[]) msg.obj;
					showChangeVersion(params[0], params[1]);
					break;
				}
			}
		}
	};
	
	// change version is showed only once
	private boolean shownChangeVersion = false;
	private void showChangeVersion(String oldVersion, String newVersion) {
		if (!shownChangeVersion) {
			shownChangeVersion = true;
			LayoutInflater factory = LayoutInflater.from(this);
			View greetingView = factory.inflate(R.layout.greeting, null);
			TextView rateView = (TextView) greetingView.findViewById(R.id.greeting_ask_for_rate);
			Util.addLink(this, rateView, R.string.greet_ask_for_rate_link_marker, R.string.market);
			TextView hintView = (TextView) greetingView.findViewById(R.id.greeting_hint_internal_compass);
			Util.addLink(this, hintView, R.string.greet_hint_internal_compass_link_marker,
					R.string.greet_hint_internal_compass_link_url);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.app_name);
			builder.setIcon(R.drawable.compass_icon);
			builder.setNeutralButton(R.string.close, null);
			builder.setView(greetingView);
			AlertDialog alert = builder.create();
			alert.show();
		}
	}

	private boolean shownWarnNoHardwareCompass = false;
	private void showWarnNoHardwareCompass() {
		if (!shownWarnNoHardwareCompass) {
			shownWarnNoHardwareCompass = true;
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(android.R.string.dialog_alert_title);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setNeutralButton(R.string.close, null);
			String msg = Util.loadStringFromRawResource(getResources(), R.raw.no_orientation_sensor_warning);
			Spanned sMsg = Html.fromHtml(msg);
			builder.setMessage(sMsg);
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	}

	private void fireShowNoHardwareCompass() {
		Message m = new Message();
		m.what = MSG_SHOW_NO_HARDWARE_COMPASS;
		myHandler.sendMessageDelayed(m, 2200);
	}

	
	




	private void startAnim() {
		if (animThread == null) {
			Runtime.getRuntime().gc();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
			animThread = new SmoothDirectionProducer(compassView,this);
			if(!animThread.isSensorOk()){
				fireShowNoHardwareCompass();
			} 
			animThread.start();
		}
	}
	

	private void stopAnim() {
		if (animThread != null) {
			animThread.setRunning(false);
			try {
				animThread.join(SmoothDirectionProducer.LONG_SPAN_MILLIS * 2);
			} catch (InterruptedException e) {
			}
			animThread = null;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		compassView.loadPrefs();
		startAnim();
	}

	@Override
	protected void onPause() {
		// Stop display
		stopAnim();
		super.onPause();
	}

	// Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options, menu);
		return true;
	}

	private static final int CALIBRATION_ACTIVITY_REQUEST = 100;
	private static final int PREFERENCES_ACTIVITY_REQUEST = 101;

	// TODO need to do this ?
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CALIBRATION_ACTIVITY_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
			float offset = data.getFloatExtra(CalibrationActivity.RESULT_NAME_OFFSET, 0.0f);
			CompassPreferences prefs = CompassPreferences.getPreferences();
			prefs.setFloat(prefs.PREFS_COMPASS_OFFSET_KEY, offset);
			takePreferences();
		}
		if (requestCode == PREFERENCES_ACTIVITY_REQUEST) {
			takePreferences();
		}

	}

	private void takePreferences() {
		CompassPreferences prefs = CompassPreferences.getPreferences();

		

		if(animThread != null){
		  animThread.setSpeedMode(prefs.getInt(prefs.PREFS_COMPASS_SPEED_KEY));
		  animThread.setOffset(prefs.getFloat(prefs.PREFS_COMPASS_OFFSET_KEY));
		}

	}

	/* Handles item selections */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.mnuCalibrate: {
				// Start Calibration
				stopAnim();
				Intent intent = new Intent(this, CalibrationActivity.class);
				CompassPreferences prefs = CompassPreferences.getPreferences();
				intent.putExtra("offset", prefs.getFloat(prefs.PREFS_COMPASS_OFFSET_KEY));
				startActivityForResult(intent, CALIBRATION_ACTIVITY_REQUEST);
				// Look at this.onActivityResult() for result.
				return true;
			}
			case R.id.mnuLayout: {
				stopAnim();
				Intent intent = new Intent(this, PreferencesActivity.class);
				startActivityForResult(intent, PREFERENCES_ACTIVITY_REQUEST);
				// startActivity(intent);
				return true;
			}
			case R.id.mnuInfo: {
				stopAnim();
				Intent intent = new Intent(this, InfoActivity.class);
				startActivity(intent);
				return true;
			}
		}
		return false;
	}
	
	

}