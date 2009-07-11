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
import android.content.*;
import android.hardware.*;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CalibrationActivity extends Activity {

	

	// Controls
	private CompassView viewCompass;
	private SensorManager mSensorManager;

	Button cmdCalibrate;
	Button cmdClear;
	Button cmdSpeichern;

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
		viewCompass = (CompassView) findViewById(R.id.viewWorld);
	    viewCompass.setCompassLayout(CompassView.LAYOUT_CALIBRATION);

	    direction = -getIntent().getFloatExtra(PARAM_NAME_OFFSET, 0);
	    

	   
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(mListener, mSensorManager
				.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_NORMAL);
		viewCompass.startAnim();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(mListener);
		viewCompass.stopAnim();
	}

	
	private final SensorEventListener mListener = new SensorEventListener() {

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			float values[] = event.values;
			direction = values[0];
			viewCompass.setDirection(0);
		}
	};

	private ClickListener cl = new ClickListener(); 
	class ClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			Intent i = getIntent();
			if (v == cmdClear) {
				i.putExtra(RESULT_NAME_OFFSET, 0.0f);
				setResult(RESULT_OK,i);
		        CalibrationActivity.this.finish();
			}
			if (v == cmdCalibrate) {
				final int STEPS = 12;
				float sum = 0;
				for (int j = 0; j < STEPS; j++) {
					sum+=direction;
					try {Thread.sleep(100);	} catch (InterruptedException e) {}
				}
				float delta = sum/STEPS;	
				i.putExtra(RESULT_NAME_OFFSET, -delta);
				setResult(RESULT_OK,i);
		        CalibrationActivity.this.finish();
			}
		}
	};
}