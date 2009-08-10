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

import de.rothbayern.android.ac.drawings.*;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.*;

public class CompassView extends SurfaceView {

	public static final int LAYOUT_ROBA = 0;
	public static final int LAYOUT_STEFAN = 1;
	public static final int LAYOUT_CALIBRATION = -100;

	public static final int SPEED_SLOW = 0;
	public static final int SPEED_NORMAL = 1;
	public static final int SPEED_FAST = 2;

	private Paint mPaint = new Paint();
	private Bitmap bmNeedle;
	private Bitmap bmStefanRose;
	private Bitmap bmRobaRose;
	private int bgColor = Color.WHITE;
	private float offset = 0.0f;
	private int speed = SPEED_NORMAL;

	/**
	 * Setpoint for Direction. AnimThread will adjust the needle smoothly
	 */
	private float setPointDirection = 0;

	/**
	 * see constants LAYOUT_XXX
	 */
	private int compassLayout = 0;

	/**
	 * Sets the setpoint. AnimThread will control the Needle
	 * 
	 * @param setPoint
	 * 
	 */
	public void setDirection(float setPoint) {
		this.setPointDirection = setPoint + offset;
	}

	public CompassView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CompassView(Context context) {
		super(context);
		init();
	}

	/**
	 * Prepare fpr drawing
	 */
	private void init() {
		mPaint.setAntiAlias(true);
		mPaint.setARGB(255, 0, 0, 0);
		mPaint.setStrokeWidth(1);
		mPaint.setTextScaleX(3);
		mPaint.setTextAlign(Paint.Align.CENTER);
		bmNeedle = BitmapFactory.decodeResource(getResources(),
				R.drawable.needle);
		bmRobaRose = new RobABaseDrawing(this.getContext()).getDrawing(300, 300);
		bmStefanRose = new StefanBaseDrawing(this.getContext()).getDrawing(300, 300);

	}

	// Middle of the view
	private int middleX = 160;
	private int middleY = 200;

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// Set new middle of view
		middleX = w / 2;
		middleY = h / 2;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		onDraw(canvas, setPointDirection);
	}

	/**
	 * Draw compass
	 * 
	 * @param canvas
	 *            where to paint
	 * @param direction
	 *            direction where the needle shows (real value no setpoint)
	 */
	protected void onDraw(Canvas canvas, float direction) {
		switch (compassLayout) {
			case LAYOUT_ROBA:
				drawRoba(canvas, direction);
				break;
			case LAYOUT_STEFAN:
				drawStefan(canvas, direction);
				break;
			case LAYOUT_CALIBRATION:
				drawCalibration(canvas);
				break;
			default:
				drawRoba(canvas, direction);

		}
	}

	/**
	 * Draw compass with the help of SVG of Stefan http://www.xpofpc.de/
	 * 
	 * @param canvas
	 * @param direction
	 */
	private void drawStefan(Canvas canvas, float direction) {
		canvas.translate(middleX, middleY);
		canvas.drawColor(bgColor);
		float x = -bmStefanRose.getWidth() / 2;
		float y = -bmStefanRose.getHeight() / 2;
		canvas.rotate(-direction);
		canvas.drawBitmap(bmStefanRose, x, y, null);
	}

	
	/**
	 * Draw compass with the help of SVG of RobA http://ffaat.pointclark.net
	 * 
	 * @param canvas
	 * @param direction
	 */
	private void drawRoba(Canvas canvas, float direction) {
		canvas.translate(middleX, middleY);
		canvas.drawColor(bgColor);
		float x = -bmRobaRose.getWidth() / 2;
		float y = -bmRobaRose.getHeight() / 2;
		canvas.drawBitmap(bmRobaRose, x, y, null);
		canvas.rotate(-direction);
		x = -bmNeedle.getWidth() / 2;
		y = -bmNeedle.getHeight() / 2;
		canvas.drawBitmap(bmNeedle, x, y, null);
	}

	/**
	 * Only draw the needle.
	 * 
	 * @param canvas
	 */
	private void drawCalibration(Canvas canvas) {
		canvas.translate(middleX, 0);
		canvas.drawColor(Color.WHITE);
		float x = -bmNeedle.getWidth() / 2;
		canvas.drawText("N", 0, 30, mPaint);
		canvas.drawBitmap(bmNeedle, x, 60, null);
	}

	/**
	 * Brings the needle smoothly to the setpoint
	 */
	private AnimThread animThread = null;

	public void startAnim() {
		if (animThread == null /* || !animThread.isRunning() */) {
			SurfaceHolder holder = getHolder();
			animThread = new AnimThread(holder);
			animThread.start();
		} else {
			throw new IllegalStateException("Don't start a running thread");
		}
	}

	/**
	 * Stops animation. Wait until animation has stopped.
	 */
	public void stopAnim() {
		if (animThread != null) {
			animThread.setRunning(false);
			while (!animThread.isFinished()) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
				}
			}
			animThread = null;
		} else {
			// Yeah, kill the zombie.
		}
	}

	/**
	 * @author dieter Adjusts the needle smoothly to the setpoint.
	 */
	class AnimThread extends Thread {

		boolean running = true; // Thread has to do his work
		boolean finished = false; // Thread has finished his work
		private float lastDirection = 0; // The last direction the needle has
		// been painted
		SurfaceHolder holder; // Necessary to draw in a different thread
		private long lastActionStamp; // stamp for last drawing or checking to
		// paint
		private int lastSleep = STD_SPAN_MILLIS; // timespan of last sleep

		private boolean arrived = false; // The needle has arrived the setpoint
		private static final float ARRIVED_EPS = 1; // tolerance to become
		// arrived
		private static final float LEAVED_EPS = 4; // tolerance to leave arrived

		private static final int LONG_SPAN_MILLIS = 200;
		private static final int STD_SPAN_MILLIS = 20;
		private static final int STD_SPAN_MILLIS_PLUS_DELTA = STD_SPAN_MILLIS
				+ STD_SPAN_MILLIS / 2;

		public boolean isFinished() {
			return finished;
		}

		public AnimThread(SurfaceHolder holder) {
			this.holder = holder;
		}

		public void setRunning(boolean running) {
			this.running = running;
		}

		public boolean isRunning() {
			return running;
		}

		public void setArrived(boolean arrived) {
			this.arrived = arrived;
		}

		@Override
		public void run() {
			finished = false;
			lastActionStamp = System.currentTimeMillis();
			while (running) {
				float actDirection = CompassView.this.setPointDirection;
				// Something to do?
				if (!arrived || actDirection != lastDirection) {

					// Calculate if modification of the needle is necessary
					boolean modified = false;
					float newDirection = lastDirection;

					// correction if there was no processor time for this thread
					boolean moreCorrection = true; // => one time is obligate
					while (moreCorrection) {
						// diff to setpoint normalized [-180, 180]
						// to control clockwise or counterclockwise aproximation
						float diff = actDirection - lastDirection;
						while (diff < -180) {
							diff += 360;
						}
						while (diff > 180) {
							diff -= 360;
						}
						int direction = (diff < 0) ? -1 : 1;
						float diffAbs = Math.abs(diff); // diff to setpoint

						if (!arrived) { // Still has to act.
							newDirection = calcNewDirection(lastDirection,
									direction, diffAbs, CompassView.this
											.getSpeed());

							modified = true;
							if (diffAbs < ARRIVED_EPS) {
								arrived = true;
							}
						}

						else {
							if (diffAbs > LEAVED_EPS) { // Needle has left
								// tolerance
								newDirection = calcNewDirection(lastDirection,
										direction, diffAbs, CompassView.this
												.getSpeed());
								modified = true;
								arrived = false;
							} else {
								newDirection = lastDirection; // Super, nothing
								// to do.
							}
						}
						while (newDirection >= 360) {
							newDirection -= 360;
						}
						while (newDirection < 0) {
							newDirection += 360;
						}

						// Check for correction of lack of processor time
						if (lastSleep == LONG_SPAN_MILLIS
								|| arrived
								|| !modified
								|| lastActionStamp + STD_SPAN_MILLIS_PLUS_DELTA > System
										.currentTimeMillis()) {
							moreCorrection = false;
						}
						lastActionStamp += STD_SPAN_MILLIS;

					} // end: correction no processor time
					// If it is necessary, do new painting
					if (modified) {
						Canvas c = holder.lockCanvas();
						if (c != null) {
							lastDirection = newDirection;
							CompassView.this.onDraw(c, newDirection);
							holder.unlockCanvasAndPost(c);
						} else {
							arrived = false; // if you couldn't paint try next
							// time.
						}
					}
				}

				// Wait for next drawing
				try {
					lastActionStamp = System.currentTimeMillis();
					if (arrived) {
						Thread.sleep(LONG_SPAN_MILLIS);
						lastSleep = LONG_SPAN_MILLIS;
					} // Save battery if there will be nothing to do.
					else {
						Thread.sleep(STD_SPAN_MILLIS);
						lastSleep = STD_SPAN_MILLIS;
					}
				} catch (InterruptedException e) {
				}
			}
			finished = true; // indicator => thread has finished
		}

		private float calcNewDirection(float lastDirection, int direction,
				float diffAbs, int speed) {
			switch (speed) {
				case CompassView.SPEED_FAST: {
					float deltaAbs = diffAbs / 6; // next step for needle
					deltaAbs = Math.max(deltaAbs, 1);
					deltaAbs = Math.min(deltaAbs,diffAbs);
					float res = lastDirection + (direction * deltaAbs);
					return res;
				}
				case CompassView.SPEED_SLOW: {
					float deltaAbs = diffAbs / 15; // next step for needle
					float res = lastDirection + (direction * deltaAbs);
					return res;
				}
				case CompassView.SPEED_NORMAL:
				default: {
					float deltaAbs = diffAbs / 8; // next step for needle
					float res = lastDirection + (direction * deltaAbs);
					return res;
				}

			}
		}

	}

	public void setCompassLayout(int compassLayout) {
		this.compassLayout = compassLayout;
		askForPaint();

	}

	public int getCompassLayout() {
		return compassLayout;
	}

	public int getBgColor() {
		return bgColor;
	}

	public void setBgColor(int bgColor) {
		this.bgColor = bgColor;
		askForPaint();
	}

	public float getOffset() {
		return offset;
	}

	public void setOffset(float offset) {
		this.offset = offset;
		askForPaint();
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	private void askForPaint() {
		if (animThread != null) {
			animThread.setArrived(false); // Force to paint
		}
	}

}
