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

import android.content.Context;
import android.graphics.*;
import de.rothbayern.android.ac.drawings.*;
import de.rothbayern.android.ac.misc.LogUtil;
import de.rothbayern.android.ac.pref.CompassPreferences;

public class CompassViewHelper {

	public static final int LAYOUT_FROM_PREFS = -1;
	public static final int LAYOUT_ROBA = 0;
	public static final int LAYOUT_STEFAN = 1;
	public static final int LAYOUT_NICOLAS = 2;
	public static final int LAYOUT_CALIBRATION = -100;
	public static final int LAYOUT_NEEDLE = -101;
	public static final int LAYOUT_NICOLAS_NEEDLE = -102;
	
	private Paint mPaint = new Paint();
	private Paint mPaintBm = new Paint();

	private Bitmap bmNeedle;
    private Bitmap bmRose;
	
	
    //private Bitmap bmStefanRose;
	//private Bitmap bmRobaRose;
	private int bgColor = Color.WHITE;
	
	Context context;
	private final int WIDTH = 320;

	// Middle of the view
	protected int middleX = WIDTH/2;
	protected int middleY = 215;
	

	/**
	 * Prepare for drawing
	 */
	protected void init(Context context) {
		this.context = context;
		mPaint.setAntiAlias(true);
		mPaint.setARGB(255, 0, 0, 0);
		mPaint.setStrokeWidth(1);
		mPaint.setTextScaleX(3);
		mPaint.setTextAlign(Paint.Align.CENTER);
		setCompassLayout(LAYOUT_FROM_PREFS);

	}
	
/* TODO delete	
	private void loadPrefs() {
		
		CompassPreferences prefs = CompassPreferences.getPreferences();
		
		int bgColor = prefs.getInt(prefs.PREFS_COMPASS_BACKGROUNDCOLOR_KEY);
		setBgColor(bgColor);

		// rebuild compass layout
		setCompassLayout(getCompassLayout());
		
	}
*/	

	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		middleX = w / 2;
		middleY = h / 2;
	}
	
	public void setDirection(float direction) {
		this.direction = direction;
	}
	
	/**
	 * Setpoint for Direction. AnimThread will adjust the needle smoothly
	 */
	private float direction = 0;

	/**
	 * see constants LAYOUT_XXX
	 */
	private int compassLayout = 0;

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
			case CompassViewHelper.LAYOUT_ROBA:
			case CompassViewHelper.LAYOUT_NICOLAS:
				drawRoseWithNeedle(canvas, direction);
				break;
			case CompassViewHelper.LAYOUT_STEFAN:
				drawRoseWithoutNeedle(canvas, direction);
				break;
			case CompassViewHelper.LAYOUT_CALIBRATION:
				drawCalibration(canvas);
				break;
			case CompassViewHelper.LAYOUT_NEEDLE:
				drawNeedle(canvas);
				break;
			case CompassViewHelper.LAYOUT_NICOLAS_NEEDLE:
				drawNeedle(canvas);
				break;
			default:
				drawRoseWithNeedle(canvas, direction);

		}
		
		
	}
	

	protected void onDraw(Canvas canvas) {
		onDraw(canvas, direction);
	}



	/**
	 * Draw compass with the help of SVG of Stefan http://www.xpofpc.de/
	 * 
	 * @param canvas
	 * @param direction
	 */
	private void drawRoseWithoutNeedle(Canvas canvas, float direction) {
		canvas.translate(middleX, middleY);
		canvas.drawColor(bgColor);
		float x = -bmRose.getWidth() / 2;
		float y = -bmRose.getHeight() / 2;
		canvas.rotate(-direction);
		canvas.drawBitmap(bmRose, x, y, mPaintBm);
		canvas.rotate(direction);
	}

	
	/**
	 * Draw compass with the help of SVG of RobA http://ffaat.pointclark.net
	 * 
	 * @param canvas
	 * @param direction
	 */
	private void drawRoseWithNeedle(Canvas canvas, float direction) {
		canvas.translate(middleX, middleY);
		canvas.drawColor(bgColor);
		float x = -bmRose.getWidth() / 2;
		float y = -bmRose.getHeight() / 2;
		canvas.drawBitmap(bmRose, x, y, mPaintBm);
		canvas.rotate(-direction);
		x = -bmNeedle.getWidth() / 2;
		y = -bmNeedle.getHeight() / 2;
		canvas.drawBitmap(bmNeedle, x, y, mPaintBm);
		canvas.rotate(direction);
	}


	/**
	 * Only draw basic for calibration.
	 * 
	 * @param canvas
	 */
	private void drawCalibration(Canvas canvas) {
		canvas.translate(middleX, 0);
		canvas.drawColor(Color.WHITE);
		float x = -bmNeedle.getWidth() / 2;
		/* TODO "N" get from  strings.xml */
		canvas.drawText("N", 0, 30, mPaint);
		canvas.drawBitmap(bmNeedle, x, 60, mPaintBm);
	}

	/**
	 * Only draw the needle.
	 * 
	 * @param canvas
	 */
	private void drawNeedle(Canvas canvas) {
		canvas.translate(middleX, middleY);
		canvas.drawColor(bgColor);
		float x = -bmNeedle.getWidth() / 2;
		float y = -bmNeedle.getHeight() / 2;
		canvas.drawBitmap(bmNeedle, x, y, mPaintBm);
	}

	public void setCompassLayout(int compassLayout) {

		// get background color
        CompassPreferences prefs = CompassPreferences.getPreferences();
		int bgColor = prefs.getInt(prefs.PREFS_COMPASS_BACKGROUNDCOLOR_KEY);
		setBgColor(bgColor);

		// get layout from prefs
		if(compassLayout == LAYOUT_FROM_PREFS){
			compassLayout = prefs.getInt(prefs.PREFS_COMPASS_LAYOUT_KEY);
		}
		
		this.compassLayout = compassLayout;
		int minWidth = (int)Math.min(middleX*2, middleY*2);
		
		bmNeedle = new RobANeedleDrawing(context).getDrawing(minWidth, minWidth);
		switch (compassLayout) {
			case CompassViewHelper.LAYOUT_ROBA:
				bmRose = new RobARoseDrawing(context).getDrawing(minWidth, minWidth);
				bmNeedle = new RobANeedleDrawing(context).getDrawing(minWidth, minWidth);
				break;
			case CompassViewHelper.LAYOUT_NICOLAS:
				bmRose = new NicolasRoseDrawing(context).getDrawing(minWidth, minWidth);
				bmNeedle = new NicolasNeedleDrawing(context).getDrawing(minWidth, minWidth);
				break;
			case CompassViewHelper.LAYOUT_STEFAN:
				bmRose = new StefanRoseDrawing(context).getDrawing(minWidth, minWidth);
				// No needle needed
				break;
			case CompassViewHelper.LAYOUT_CALIBRATION:
			case CompassViewHelper.LAYOUT_NEEDLE:
				bmNeedle = new RobANeedleDrawing(context).getDrawing(minWidth, minWidth);
				// no rose => noting to do
				break;
			case CompassViewHelper.LAYOUT_NICOLAS_NEEDLE:
				bmNeedle = new NicolasNeedleDrawing(context).getDrawing(minWidth, minWidth);
				// no rose => noting to do
				break;
			default:
				bmRose = new RobARoseDrawing(context).getDrawing(minWidth, minWidth);
				bmNeedle = new RobANeedleDrawing(context).getDrawing(minWidth, minWidth);
				LogUtil.w(LogUtil.TAG, "Rose not known");
		}
		
		
	}

	public int getCompassLayout() {
		return compassLayout;
	}

	public int getBgColor() {
		return bgColor;
	}

	public void setBgColor(int bgColor) {
		this.bgColor = bgColor;
	}
	
	PointF getMiddle(){
		return(new PointF(middleX,middleY));
	}

	
}
