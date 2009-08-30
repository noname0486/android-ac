package de.rothbayern.android.ac;

import android.content.Context;
import android.graphics.*;
import android.util.*;
import de.rothbayern.android.ac.drawings.*;
import de.rothbayern.android.ac.pref.CompassPreferences;

public class CompassViewHelper {

	public static final int LAYOUT_ROBA = 0;
	public static final int LAYOUT_STEFAN = 1;
	public static final int LAYOUT_CALIBRATION = -100;
	public static final int LAYOUT_NEEDLE = -101;
	private Paint mPaint = new Paint();
	private Paint mPaintBm = new Paint();

	private Bitmap bmNeedle;
	private Bitmap bmStefanRose;
	private Bitmap bmRobaRose;
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
		loadPrefs();

	}
	
	
	public void loadPrefs() {
		int minWidth = (int)Math.min(middleX*2, middleY*2);
		bmNeedle = new NeedleBaseDrawing(context).getDrawing(minWidth, minWidth);
		bmRobaRose = new RobABaseDrawing(context).getDrawing(minWidth, minWidth);
		bmStefanRose = new StefanBaseDrawing(context).getDrawing(minWidth, minWidth);
		CompassPreferences prefs = CompassPreferences.getPreferences();
		bgColor = prefs.getInt(prefs.PREFS_COMPASS_BACKGROUNDCOLOR_KEY);

	}
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		middleX = w / 2;
		middleY = h / 2;
		loadPrefs();
		Log.d("middle-helper"," "+middleX+", "+middleY);
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
				drawRoba(canvas, direction);
				break;
			case CompassViewHelper.LAYOUT_STEFAN:
				drawStefan(canvas, direction);
				break;
			case CompassViewHelper.LAYOUT_CALIBRATION:
				drawCalibration(canvas);
				break;
			case CompassViewHelper.LAYOUT_NEEDLE:
				drawNeedle(canvas);
				break;
			default:
				drawRoba(canvas, direction);

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
	private void drawStefan(Canvas canvas, float direction) {
		canvas.translate(middleX, middleY);
		canvas.drawColor(bgColor);
		float x = -bmStefanRose.getWidth() / 2;
		float y = -bmStefanRose.getHeight() / 2;
		canvas.rotate(-direction);
		canvas.drawBitmap(bmStefanRose, x, y, mPaintBm);
		canvas.rotate(direction);
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
		canvas.drawBitmap(bmRobaRose, x, y, mPaintBm);
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
		this.compassLayout = compassLayout;

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
