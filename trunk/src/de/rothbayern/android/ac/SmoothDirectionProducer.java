package de.rothbayern.android.ac;

import android.content.Context;
import android.hardware.*;
import de.rothbayern.android.ac.misc.Util;
import de.rothbayern.android.ac.pref.CompassPreferences;
/**
 * Adjusts the needle smoothly to the setpoint.
 * @author Dieter Roth 
 */
public class SmoothDirectionProducer extends Thread {

	private SensorManager mSensorManager;
	
	// Possible speeds for needle movement
	public static final int SPEED_SLOW = 0;
	public static final int SPEED_NORMAL = 1;
	public static final int SPEED_FAST = 2;
	public static final int SPEED_DIRECT = 3;
	public static final int SPEED_SWING = 4;
	private int speedMode = SPEED_NORMAL;

	public int getSpeedMode() {
		return speedMode;
	}

	public void setSpeedMode(int speedMode) {
		this.speedMode = speedMode;
	}

	private static final float ARRIVED_EPS = 0.7f; // tolerance to become arrived
	private static final float LEAVED_EPS = 2.5f; // tolerance to leave arrived
	private static final float SPEED_EPS = 0.5f; // tolerance of speed

	public static final int LONG_SPAN_MILLIS = 100;	// time to sleep if the needle is in state arrived
	public static final int STD_SPAN_MILLIS = 20;		// time to sleep if the neelde have to move to the setpoint.
														// about 50 FPS

	private boolean running = true; 	// Thread has to do his work
	private boolean finished = false; 	// Thread has finished his work
	private float setPoint = 0.0f;		// direction the needle should point (sometime in the future)

	private IAnimCompass compassView;
	private Context context;
	private boolean sensorOk = false;

	
	
	public SmoothDirectionProducer(IAnimCompass compassView, Context context) {
		this.compassView = compassView;
		this.context = context;
		
		// Prepare sensor to deliver data. Show message if there is no orientation sensor
		
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		Sensor testSensorOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		if (testSensorOrientation == null) {
			sensorOk = false;
		} else {
			boolean isPresent = mSensorManager
					.registerListener(mListener, testSensorOrientation, SensorManager.SENSOR_DELAY_NORMAL);
			sensorOk = isPresent; 
			mSensorManager.unregisterListener(mListener);

		}
		
	}
	
	public boolean isSensorOk(){
		return(sensorOk);
	}

	// offset for calibration
	private float offset = 0.0f;
	public float getOffset() {
		return offset;
	}

	public void setOffset(float offset) {
		this.offset = offset;
	}

	private float avgDirection = 0;
	private final SensorEventListener mListener = new SensorEventListener() {

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// if(accuracy>5){
			// Log.w("accuracy low", Integer.toString(accuracy));
			// }
		}

		public void onSensorChanged(SensorEvent event) {
			float newDirection = event.values[0] + offset;
			float diff = newDirection - avgDirection;
			diff = Util.normAngle(diff);
			if (Math.abs(diff) < 5) {
				newDirection = avgDirection + diff / 4;
			} else {
				SmoothDirectionProducer.this.preferSensorListenerState(SmoothDirectionProducer.SENSOR_LISTENER_STATE_ACTION);

			}
			newDirection = Util.normAngle(newDirection);
			avgDirection = newDirection;
			SmoothDirectionProducer.this.setSetPoint(avgDirection);
		}
	};


	
	public void setSetPoint(float setPoint) {
		this.setPoint = setPoint;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	@Override
	public void run() {
		finished = false;
		float speed = 0;
		float lastDirection = 0;
		boolean forcePaint = true;
		boolean arrived = false; // The needle has not arrived the setpoint

		
		CompassPreferences prefs = CompassPreferences.getPreferences();
		this.setSpeedMode(prefs.getInt(prefs.PREFS_COMPASS_SPEED_KEY));
		this.setOffset(prefs.getFloat(prefs.PREFS_COMPASS_OFFSET_KEY));
		
		
		setSensorListenerState(SmoothDirectionProducer.SENSOR_LISTENER_STATE_ACTION);
		
		// repaint all the time
		while (running) {
			// copy to local variable for one iteration
			float curSetPoint = setPoint;
			boolean needPainting = calcNeedPainting(arrived, speed, lastDirection, curSetPoint) || forcePaint;
			// Something to do?
			if (needPainting) {
				arrived = false;
				
				// !! important calculation, which do a smooth movement
				// diff to setpoint normalized [-180, 180]
				// to control clockwise or counterclockwise aproximation
				float diff = Util.calcNormDiff(lastDirection, curSetPoint);
				speed = calcSpeed(diff, speed, speedMode);
				curSetPoint = lastDirection + speed;
				lastDirection = curSetPoint;
				// !!

				// set the direction where the needle points in the next step
				// an paint the needle
				boolean successPaint = compassView.setDirection(curSetPoint);
				forcePaint = !successPaint;

			} else {
				arrived = true;
			}

			// Wait for next drawing
			try {
				if (arrived) {
					// Save battery if there will be nothing to do.
					preferSensorListenerState(SENSOR_LISTENER_STATE_SLEEP);
					Thread.sleep(LONG_SPAN_MILLIS);
				} 
				else {
					preferSensorListenerState(SENSOR_LISTENER_STATE_ACTION);
					Thread.sleep(STD_SPAN_MILLIS);

				}
			} catch (InterruptedException e) {
			}
		} // while
		setSensorListenerState(SmoothDirectionProducer.SENSOR_LISTENER_STATE_OFF);
		finished = true; // indicator => thread has finished
	}


	/**
	 * calculates the new speed the needle moves to the setpoint
	 * @param diff		distance to the setpoint
	 * @param oldSpeed		
	 * @param speedMode	determines which physics to use
	 * @return	the new speed
	 */
	private float calcSpeed(float diff, float oldSpeed, int speedMode) {
		switch (speedMode) {
			case SPEED_DIRECT: {
				oldSpeed = oldSpeed * 0f; // friction
				oldSpeed += diff; // acceleration
				return oldSpeed;
			}
			case SPEED_FAST: {
				oldSpeed = oldSpeed * 0.75f; // friction
				oldSpeed += diff / 8.0f; // acceleration
				return oldSpeed;
			}
			case SPEED_SLOW: {
				oldSpeed = oldSpeed * 0.75f; // friction
				oldSpeed += diff / 40.0f; // acceleration
				return oldSpeed;
			}

			case SPEED_SWING: {
				oldSpeed = oldSpeed * 0.97f; // friction
				oldSpeed += diff / 10.0f; // acceleration
				return oldSpeed;
			}

			case SPEED_NORMAL:
			default: {
				oldSpeed = oldSpeed * 0.75f; // friction
				oldSpeed += diff / 20.0f; // acceleration
				return oldSpeed;
			}
		}
	}
	
	/**
	 * say if needle needs repainting
	 * 
	 * @param pArrived				needle was near setpoint 
	 * @param pSpeed				current speed of needle
	 * @param curNeedleDirection	current direction of the needle
	 * @param setPoint				direction to where the needle will point (sometime in the future)
	 * @return true if needle should be repainted
	 */
	private static boolean calcNeedPainting(boolean pArrived, float pSpeed, float curNeedleDirection, float setPoint) {
		if (pArrived) {
			if (Math.abs(curNeedleDirection - setPoint) > SmoothDirectionProducer.LEAVED_EPS) {
				// if (Config.LOGD) {
				// Log.d("arrived", "leave: " + pArrived + ", " + pSpeed + ", "
				// + pLastDirection + ", " + pCurSetPoint);
				// }
				return (true);
			}
			return (false);
		} else {
			if (Math.abs(curNeedleDirection - setPoint) < SmoothDirectionProducer.ARRIVED_EPS && Math.abs(pSpeed) < SmoothDirectionProducer.SPEED_EPS) {
				// if (Config.LOGD) {
				// Log.d("arrived", "arrived: " + pArrived + ", " + pSpeed +
				// ", " + pLastDirection + ", " + pCurSetPoint);
				// }
				return (false);
			}
			return (true);
		}
	}

	// Handling of sensormanager and sensor values
	// sensor can be delivered with a high rate (sucks battery)
	// or can deliver with a low rate (saving battery)
	// here is some intelligence for switching these modes
	public static final int SENSOR_LISTENER_STATE_OFF = 0;
	public static final int SENSOR_LISTENER_STATE_SLEEP = 1;
	public static final int SENSOR_LISTENER_STATE_ACTION = 2;
	private int sensorListenerState = SENSOR_LISTENER_STATE_OFF;
	
	public void setSensorListenerState(int newState) {
		if (newState == sensorListenerState) // nothing to do if settings are already similar
			return;
		// if (Config.LOGD) {
		// Log.d("sensor register", "SENSOR_LISTENER_STATE_OFF");
		// }
		
		// stop old mode
		mSensorManager.unregisterListener(mListener);
		sensorListenerState = SENSOR_LISTENER_STATE_OFF;

		// set the desired new mode
		if (newState == SENSOR_LISTENER_STATE_SLEEP) {
			// if (Config.LOGD) {
			// Log.d("sensor register", "SENSOR_LISTENER_STATE_NORMAL");
			// }
			mSensorManager.registerListener(mListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
					SensorManager.SENSOR_DELAY_NORMAL);
			sensorListenerState = newState;
		}
		if (newState == SENSOR_LISTENER_STATE_ACTION) {
			// if (Config.LOGD) {
			// Log.d("sensor register", "SENSOR_LISTENER_STATE_ACTION");
			// }
			mSensorManager.registerListener(mListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
					SensorManager.SENSOR_DELAY_UI);
			sensorListenerState = newState;
		}

	}

	public void preferSensorListenerState(int newState) {
		if (sensorListenerState != SENSOR_LISTENER_STATE_OFF) {
			setSensorListenerState(newState);
		}
	}



}

