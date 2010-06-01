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

package de.rothbayern.android.ac.drawings;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.util.FloatMath;
import de.rothbayern.android.ac.R;
import de.rothbayern.android.ac.geometry.*;
import de.rothbayern.android.ac.misc.Util;

public class NicolasRoseDrawing extends RoseDrawing {

	static final float OUTER_RING_RADIUS = MAX * 0.96f;
	private int bottomColor = Color.parseColor("#830000");

	private static final float OUTER_RING_WIDTH = MAX * 0.03f;
	private int outerRingColor1 = Color.LTGRAY;
	private int outerRingColor2 = Util.lightenColor(outerRingColor1, 0.5f);;


	static final float DIRECTION_RING_RADIUS = MAX * 0.60f;
	static final float DIRECTION_CIRCLE_AREA_RADIUS = MAX * 0.20f;
	private int directionCircleAreaColor = Color.LTGRAY;
	int directionTextColor = Color.WHITE;
	static final float DIRECTION_TEXT_SIZE = MAX * 0.25f;



//----------
	
	
	static final float DEGREES_RADIUS = MAX * 0.75f;
	static final float DEGREES_TEXT_SIZE = MAX * 0.14f;
	int degressTextColor = Color.BLACK;
	

	

	static final float MIDDLE_RING_RADIUS = MAX * 0.73f;
	private static final float MIDDLE_RING_WIDTH = MAX * 0.007f;
	static final float MIDDLE_RING_MARKER_LENGTH = MAX * 0.07f;
	int middleRingColor = Color.BLACK;

	static final float INNER_RING_RADIUS = MAX * 0.5f;
	private static final float INNER_RING_WIDTH = MAX * 0.1f;
	static final float INNER_RING_MARKER_LENGTH = INNER_RING_WIDTH;
	private static final float INNER_RING_MARKER_WIDTH = INNER_RING_WIDTH * 1.2f;
	int innerRingColor = Color.BLACK;

	int directionNorthTextColor = Color.RED;
	
	Context context;

	public NicolasRoseDrawing(Context c) {
		context = c;
	}

	private void loadPrefColors() {
		bottomColor = getColorPreference(searchByName(BOTTOM_NAME));
		
		outerRingColor1 = getColorPreference(searchByName(OUTER_RING_NAME));
		int red = 2*Color.red(outerRingColor1)/3;
		int green = 2*Color.green(outerRingColor1)/3;
		int blue = 2*Color.blue(outerRingColor1)/3;
		outerRingColor2 = Util.lightenColor(outerRingColor1, 0.7f);

		middleRingColor = getColorPreference(searchByName(MIDDLE_RING_NAME));

		innerRingColor = getColorPreference(searchByName(INNER_RING_NAME));

		degressTextColor = getColorPreference(searchByName(DEGREE_LABEL_NAME));
		directionNorthTextColor = getColorPreference(searchByName(NORTH_LABEL_NAME));
		directionTextColor = getColorPreference(searchByName(DIRECTION_LABEL_NAME));

	}

	@Override
	public Bitmap getDrawing(int width, int height) {
		//loadPrefColors();
		
		int minpx = Math.min(width, height);

		Paint paint = new Paint();
		paint.setAntiAlias(true);

		Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bm);

		c.scale(minpx / (2 * MAX), minpx / (2 * MAX));
		c.translate(MAX, MAX);

		// bottom
		paint.setColor(bottomColor);
		paint.setStyle(Paint.Style.FILL);
		c.drawCircle(0, 0, OUTER_RING_RADIUS, paint);

		
		// The rest are lines
		paint.setStyle(Paint.Style.STROKE);
		
		// outer ring
		Shader shader = new LinearGradient(-OUTER_RING_RADIUS, -OUTER_RING_RADIUS, OUTER_RING_RADIUS, OUTER_RING_RADIUS, outerRingColor1, outerRingColor2,
				Shader.TileMode.CLAMP);
		paint.setShader(shader);
		paint.setStrokeWidth(OUTER_RING_WIDTH);
		c.drawCircle(0, 0, OUTER_RING_RADIUS, paint);
		paint.setShader(null);

		// orientation labels include circle areas around
		String orientationLabels[] = context.getResources().getStringArray(R.array.orientations);
		drawOrientation(paint, c, orientationLabels[0 * 4], 0, -DIRECTION_RING_RADIUS);
		drawOrientation(paint, c, orientationLabels[1 * 4], DIRECTION_RING_RADIUS,0);
		drawOrientation(paint, c, orientationLabels[2 * 4], 0, DIRECTION_RING_RADIUS);
		drawOrientation(paint, c, orientationLabels[3 * 4], -DIRECTION_RING_RADIUS,0);
		
		
		
/*
		
		shader = new LinearGradient(-OUTER_RING_RADIUS, 0, OUTER_RING_RADIUS, 0, outerRingColor2, outerRingColor1,
				Shader.TileMode.CLAMP);
		paint.setShader(shader);
		c.drawCircle(0, 0, OUTER_RING_RADIUS - OUTER_RING_WIDTH, paint);
		

		// middle ring
		paint.setColor(middleRingColor);
		paint.setStrokeWidth(MIDDLE_RING_WIDTH);
		c.drawCircle(0, 0, MIDDLE_RING_RADIUS, paint);

		float p12 = (float) Math.PI / 12;
		for (int i = 0; i < 24; i++) {
			float rad = p12 * i;
			float x = FloatMath.cos(rad);
			float y = FloatMath.sin(rad);
			c.drawLine(x * MIDDLE_RING_RADIUS, y * MIDDLE_RING_RADIUS, x * (MIDDLE_RING_RADIUS - MIDDLE_RING_MARKER_LENGTH), y
					* (MIDDLE_RING_RADIUS - MIDDLE_RING_MARKER_LENGTH), paint);
		}

		// inner ring
		paint.setColor(innerRingColor);
		paint.setStrokeWidth(INNER_RING_MARKER_WIDTH);
		c.drawCircle(0, 0, INNER_RING_RADIUS, paint);

		float p6 = (float) Math.PI / 6;
		for (int i = 0; i < 12; i++) {
			float rad = p6 * i;
			float x = FloatMath.cos(rad);
			float y = FloatMath.sin(rad);
			c.drawLine(x * INNER_RING_RADIUS, y * INNER_RING_RADIUS, x * (INNER_RING_RADIUS + INNER_RING_MARKER_LENGTH), y
					* (INNER_RING_RADIUS + INNER_RING_MARKER_LENGTH), paint);
		}

		// Symbols
		paint.setColor(degressTextColor);
		paint.setTextSize(DEGREES_TEXT_SIZE);
		paint.setStrokeWidth(0);
		paint.setTextAlign(Paint.Align.CENTER);
		// paint.setAntiAlias(false);


		c.drawText("0°", 0 + DEGREES_TEXT_SIZE / 5, -DEGREES_RADIUS - DEGREES_TEXT_SIZE * 0.2f, paint);
		c.drawText("90°", DEGREES_RADIUS, DEGREES_TEXT_SIZE / 3, paint);
		c.drawText("180°", 0 + DEGREES_TEXT_SIZE / 5, DEGREES_RADIUS + DEGREES_TEXT_SIZE * 0.8f, paint);
		c.drawText("270°", -DEGREES_RADIUS, DEGREES_TEXT_SIZE / 3, paint);

		paint.setTextSize(DIRECTION_TEXT_SIZE);
		paint.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD));
		paint.setColor(directionNorthTextColor);
		String orientationLabels[] = context.getResources().getStringArray(R.array.orientations);
		c.drawText(orientationLabels[0 * 4], 0, -INNER_RING_RADIUS + DIRECTION_TEXT_SIZE / 6, paint);
		paint.setColor(directionTextColor);
		c.drawText(orientationLabels[1 * 4], INNER_RING_RADIUS + DIRECTION_TEXT_SIZE / 3, 0 + DIRECTION_TEXT_SIZE / 3, paint);
		c.drawText(orientationLabels[2 * 4], 0, INNER_RING_RADIUS + DIRECTION_TEXT_SIZE / 2, paint);
		c.drawText(orientationLabels[3 * 4], -INNER_RING_RADIUS - DIRECTION_TEXT_SIZE / 4, 0 + DIRECTION_TEXT_SIZE / 3, paint);

		c.drawText("Nicolas", -MIDDLE_RING_RADIUS/2, -MIDDLE_RING_RADIUS/2, paint);
*/		
		return (bm);
	}

	private void drawOrientation(Paint paint, Canvas c, String orientationLabel, float x, float y) {
		// circle area
		float diameter = DIRECTION_CIRCLE_AREA_RADIUS*2.0f;
        RectF rectCircle = new RectF(0,0,diameter,diameter);
        rectCircle.offset(x-DIRECTION_CIRCLE_AREA_RADIUS, y-DIRECTION_CIRCLE_AREA_RADIUS);
		paint.setColor(directionCircleAreaColor);
		paint.setStyle(Paint.Style.FILL);
	    c.drawOval(rectCircle, paint);	

	    // text measure
		paint.setTextSize(DIRECTION_TEXT_SIZE);
		paint.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD));
		paint.setColor(directionTextColor);
		Rect    bounds = new Rect();
        paint.getTextBounds(orientationLabel, 0, orientationLabel.length(), bounds);
        
        // draw text
        RectF boundsF = new RectF(bounds);
		c.drawText(orientationLabel, x-boundsF.left-boundsF.width()/2.0f, y-boundsF.bottom+boundsF.height()/2.0f, paint);
	}

	// insert in strings.xml
	public final static String COLOR_CHOOSE_PREFIX = "color_choose_prefix";
	public final static String BOTTOM_NAME = "bottom";
	public final static String OUTER_RING_NAME = "outer_ring";
	public final static String MIDDLE_RING_NAME = "middle_ring";
	public final static String INNER_RING_NAME = "inner_ring";
	public final static String DIRECTION_LABEL_NAME = "direction_label";
	public final static String NORTH_LABEL_NAME = "north_label";
	public final static String DEGREE_LABEL_NAME = "degree_label";

	private DrawingComponent components[] = null;

	private static final float S2H = FloatMath.sqrt(2.0f)/2.0f;
	
	public DrawingComponent[] getComponents() {
		if (components == null) {
			final float TEXT_AREA_FACTOR = 3.0f;

			Resources resources = context.getResources();
			
			// north label
			Figure northLabel = FRect.fromMiddle(0, -INNER_RING_RADIUS-DIRECTION_TEXT_SIZE*0.3f, DIRECTION_TEXT_SIZE * TEXT_AREA_FACTOR);
			DrawingComponent compNorthLabel = new DrawingComponent(0, NORTH_LABEL_NAME, northLabel,directionNorthTextColor,resources.getString(R.string.color_choose_NORTH_LABEL_NAME));

			// remaining labels
			Figure southLabel = FRect.fromMiddle(0, INNER_RING_RADIUS+DIRECTION_TEXT_SIZE*0.3f, DIRECTION_TEXT_SIZE * TEXT_AREA_FACTOR);
			Figure eastLabel = FRect.fromMiddle(INNER_RING_RADIUS, 0, DIRECTION_TEXT_SIZE * TEXT_AREA_FACTOR);
			Figure westLabel = FRect.fromMiddle(-INNER_RING_RADIUS, 0, DIRECTION_TEXT_SIZE * TEXT_AREA_FACTOR);
			FArray directionLabels = new FArray(3);
			directionLabels.add(southLabel);
			directionLabels.add(westLabel);
			directionLabels.add(eastLabel);
			DrawingComponent compOrientationLabels = new DrawingComponent(0, DIRECTION_LABEL_NAME, directionLabels,directionTextColor,resources.getString(R.string.color_choose_DIRECTIONS_LABEL_NAME));

			// degree labels
			Figure d0Label = FRect.fromMiddle(0, -DEGREES_RADIUS-DEGREES_TEXT_SIZE, DIRECTION_TEXT_SIZE * TEXT_AREA_FACTOR);
			Figure d90Label = FRect.fromMiddle(DEGREES_RADIUS+DEGREES_TEXT_SIZE*0.5f, 0, DIRECTION_TEXT_SIZE * TEXT_AREA_FACTOR);
			Figure d180Label = FRect.fromMiddle(0, DEGREES_RADIUS+DEGREES_TEXT_SIZE, DIRECTION_TEXT_SIZE * TEXT_AREA_FACTOR);
			Figure d270Label = FRect.fromMiddle(-DEGREES_RADIUS-DEGREES_TEXT_SIZE*0.5f, 0, DIRECTION_TEXT_SIZE * TEXT_AREA_FACTOR);
			FArray degreeLabels = new FArray(4);
			degreeLabels.add(d0Label);
			degreeLabels.add(d90Label);
			degreeLabels.add(d180Label);
			degreeLabels.add(d270Label);
			DrawingComponent compDegreeLabels = new DrawingComponent(0, DEGREE_LABEL_NAME, degreeLabels,degressTextColor,resources.getString(R.string.color_choose_DEGREES_LABEL_NAME));

			
			// rings
			FCircle outerRing = new FCircle(OUTER_RING_RADIUS*0.85f,-OUTER_RING_RADIUS,DEGREES_TEXT_SIZE*1.2f);
			FPointerDecorater pointerOuterRing = new FPointerDecorater(outerRing,OUTER_RING_RADIUS*S2H,-OUTER_RING_RADIUS*S2H);
			DrawingComponent compOuterRing = new DrawingComponent(0, OUTER_RING_NAME, pointerOuterRing,outerRingColor1,resources.getString(R.string.color_choose_OUTER_RING_NAME));

			FCircle middleRing = new FCircle(-OUTER_RING_RADIUS*0.8f,-OUTER_RING_RADIUS,DEGREES_TEXT_SIZE*1.2f);
			FPointerDecorater pointerMiddleRing = new FPointerDecorater(middleRing,-MIDDLE_RING_RADIUS*S2H,-MIDDLE_RING_RADIUS*S2H);
			DrawingComponent compMiddleRing = new DrawingComponent(0, MIDDLE_RING_NAME, pointerMiddleRing, middleRingColor,resources.getString(R.string.color_choose_MIDDLE_RING_NAME));

			FCircle innerRing = new FCircle(-OUTER_RING_RADIUS*0.8f,OUTER_RING_RADIUS,DEGREES_TEXT_SIZE*1.2f);
			FPointerDecorater pointerInnerRing = new FPointerDecorater(innerRing,-INNER_RING_RADIUS*S2H,INNER_RING_RADIUS*S2H);
			DrawingComponent compInnerRing = new DrawingComponent(0, INNER_RING_NAME, pointerInnerRing, innerRingColor,resources.getString(R.string.color_choose_INNER_RING_NAME));
			
			FCircle bottom = new FCircle(0,0,INNER_RING_RADIUS - DIRECTION_TEXT_SIZE*2.0f);
			DrawingComponent compBottom = new DrawingComponent(0,BOTTOM_NAME,bottom,bottomColor,resources.getString(R.string.color_choose_BOTTOM_NAME));
			
			components = new DrawingComponent[] { compNorthLabel, compOrientationLabels, compDegreeLabels, compOuterRing,
					compMiddleRing, compInnerRing ,compBottom};
			int l = components.length;
			for (int i = 0; i < l; i++) {
				components[i].setPos(i);
			}
		}
		return (components);
	}

	private final static String DRAWING_NAME = "Nicolas";

	@Override
	public String getPrefNamePrefix() {
		return DRAWING_NAME;
	}
 


}
