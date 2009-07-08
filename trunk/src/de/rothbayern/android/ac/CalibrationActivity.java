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
				i.putExtra(RESULT_NAME_OFFSET, -direction);
				setResult(RESULT_OK,i);
		        CalibrationActivity.this.finish();
			}
		}
	};
}