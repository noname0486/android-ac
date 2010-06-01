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


import android.app.Activity;
import android.content.*;
import android.hardware.*;
import android.os.*;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class CalibrationActivity extends Activity {

	

	// Controls
	private CompassStandardView viewCompass;
	private SensorManager mSensorManager;

	private Button cmdCalibrate;
	private Button cmdClear;

	public static final String PARAM_NAME_OFFSET = "offset";
	public static final String RESULT_NAME_OFFSET = "result_offset";
	
	private float direction=0;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		setContentView(R.layout.calibration);

		cmdCalibrate = (Button) findViewById(R.id.cmdCalibrate);
		cmdCalibrate.setOnClickListener(cl);
		cmdClear = (Button) findViewById(R.id.cmdClearCalibration);
		cmdClear.setOnClickListener(cl);
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		viewCompass = (CompassStandardView) findViewById(R.id.viewWorld);
	    viewCompass.setCompassLayout(CompassViewHelper.LAYOUT_CALIBRATION);

	    direction = -getIntent().getFloatExtra(PARAM_NAME_OFFSET, 0);
	    

	   
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(mListener, mSensorManager
				.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_GAME);
		viewCompass.setDirection(0.0f);
		// viewCompass.loadPrefs(); ??
		viewCompass.invalidate();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(mListener);
		super.onPause();
	}

	
	private final SensorEventListener mListener = new SensorEventListener() {


		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}

		public void onSensorChanged(SensorEvent event) {
			direction=event.values[0];
		}
	};

	private static final int MSG_CALIBRATE = 100;
	private static final int MSG_CALIBRATE_OFF = 101;
	Handler myHandler = new Handler(){
		private static final int SPAN_NEXT_VALUE_MS = 100;
		private static final int STEPS = 12;

		@Override
		public void handleMessage(Message msg) {
			Intent i = getIntent();
			switch(msg.what){
				case MSG_CALIBRATE:{
					float sum = 0;
					for (int j = 0; j < STEPS; j++) {
						sum+=direction;
						try {Thread.sleep(SPAN_NEXT_VALUE_MS);	} catch (InterruptedException e) {}
					}
					float delta = sum/STEPS;	
					i.putExtra(RESULT_NAME_OFFSET, -delta);
					setResult(RESULT_OK,i);
			        CalibrationActivity.this.finish();
					break;
				}
				case MSG_CALIBRATE_OFF:{
					i.putExtra(RESULT_NAME_OFFSET, 0.0f);
					setResult(RESULT_OK,i);
			        CalibrationActivity.this.finish();
				}
			}
		}
	};

	private ClickListener cl = new ClickListener(); 
	class ClickListener implements OnClickListener {



		public void onClick(View v) {

			if (v == cmdClear) {
				Toast.makeText(CalibrationActivity.this, R.string.no_calibration, Toast.LENGTH_SHORT).show();
				Message m = Message.obtain(myHandler, MSG_CALIBRATE_OFF);
				m.sendToTarget();
			}
			if (v == cmdCalibrate) {
				Toast.makeText(CalibrationActivity.this, R.string.calibrating, Toast.LENGTH_SHORT).show();
				Message m = Message.obtain(myHandler, MSG_CALIBRATE);
				m.sendToTarget();
			}
		}
	};
	
	
	
}