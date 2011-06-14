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

	private static final float OUTER_RING_WIDTH = MAX * 0.04f;
	private int outerRingColor1 = Color.LTGRAY;
	private int outerRingColor2 = Util.lightenColor(outerRingColor1, 0.5f);;

	static final float OUTER_GLASS_PLATE_RADIUS = MAX*0.885f;
	static final float INNER_GLASS_PLATE_RADIUS = MAX*0.795f;
	
	private int glassPlateColor1 = Color.parseColor("#00FFFFFF");
	private int glassPlateColor2 = Color.parseColor("#A0FFFFFF");
	private int glassPlateColor = Color.parseColor("#C0FFFFFF");

	
	static final float DIRECTION_RING_RADIUS = MAX * 0.58f;
	static final float DIRECTION_CIRCLE_AREA_RADIUS = (MAX * 0.20f);
	private int directionCircleAreaColor = Color.DKGRAY&0xa0FFFFFF;
	
	int directionTextColor = Color.TRANSPARENT; 
	static final float DIRECTION_TEXT_SIZE = DIRECTION_CIRCLE_AREA_RADIUS*1.5f;

	private int bottomColor = Color.parseColor("#830000");

	// ----------



	int directionNorthTextColor = Color.RED;

	Context context;

	public NicolasRoseDrawing(Context c) {
		context = c;
	}

	private void loadPrefColors() {

		bottomColor = getColorPreference(searchByName(BOTTOM_NAME));
		outerRingColor1 = getColorPreference(searchByName(OUTER_RING_NAME));
		outerRingColor2 = Util.lightenColor(outerRingColor1, 0.7f);
/*
		directionCircleAreaColor = getColorPreference(searchByName(DIRECTION_CIRCLE_AREA_NAME));
		
		directionNorthTextColor = getColorPreference(searchByName(NORTH_LABEL_NAME));
		directionTextColor = getColorPreference(searchByName(DIRECTION_LABEL_NAME));
*/
	}

	@Override
	public Bitmap getDrawing(int width, int height) {
	   loadPrefColors();

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


		// outer ring
		paint.setStyle(Paint.Style.STROKE);
		Shader shader = new LinearGradient(-OUTER_RING_RADIUS, -OUTER_RING_RADIUS, OUTER_RING_RADIUS, OUTER_RING_RADIUS,
				outerRingColor1, outerRingColor2, Shader.TileMode.CLAMP);
		paint.setShader(shader);
		paint.setStrokeWidth(OUTER_RING_WIDTH);
		c.drawCircle(0, 0, OUTER_RING_RADIUS, paint);

		
		// transparent glass plate
		paint.setStyle(Paint.Style.FILL);

		shader = new LinearGradient(0, -INNER_GLASS_PLATE_RADIUS, 0,
				INNER_GLASS_PLATE_RADIUS, glassPlateColor1, glassPlateColor2, Shader.TileMode.CLAMP);
		paint.setShader(shader);
		c.drawCircle(0, 0, OUTER_GLASS_PLATE_RADIUS, paint);

		paint.setShader(null);
		paint.setColor(glassPlateColor);
		c.drawCircle(0, 0, INNER_GLASS_PLATE_RADIUS, paint);

	

		// orientation labels include circle areas around
		String orientationLabels[] = context.getResources().getStringArray(R.array.orientations);
		drawOrientation(c, orientationLabels[0 * 4], 0, -DIRECTION_RING_RADIUS,0);
		drawOrientation( c, orientationLabels[1 * 4], DIRECTION_RING_RADIUS, 0,90);
		drawOrientation( c, orientationLabels[2 * 4], 0, DIRECTION_RING_RADIUS,180);
		drawOrientation( c, orientationLabels[3 * 4], -DIRECTION_RING_RADIUS, 0,270);

		return (bm);
	}


	private void drawOrientation(Canvas pCanvas, String orientationLabel, float x, float y,float angle) {
		// TODO match to screen resolution!!
		final float diameter = DIRECTION_CIRCLE_AREA_RADIUS*2.0f;
		
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setMaskFilter(new BlurMaskFilter(2, BlurMaskFilter.Blur.SOLID));
		Bitmap bm = Bitmap.createBitmap((int)diameter+2, (int)diameter+2, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bm);
		c.translate(DIRECTION_CIRCLE_AREA_RADIUS, DIRECTION_CIRCLE_AREA_RADIUS);
		c.rotate(angle);
		
		// circle area
		paint.setColor(directionCircleAreaColor);
		paint.setStyle(Paint.Style.FILL);

		RectF circleRect = new RectF(-DIRECTION_CIRCLE_AREA_RADIUS,-DIRECTION_CIRCLE_AREA_RADIUS, DIRECTION_CIRCLE_AREA_RADIUS, DIRECTION_CIRCLE_AREA_RADIUS);
		
		paint.setMaskFilter(new BlurMaskFilter(2, BlurMaskFilter.Blur.SOLID));
		c.drawOval(circleRect, paint);
		paint.setMaskFilter(null);

		// text measure
		paint.setTextSize(DIRECTION_TEXT_SIZE);
		paint.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD));
		paint.setColor(directionTextColor);
		Rect bounds = new Rect();
		paint.getTextBounds(orientationLabel, 0, orientationLabel.length(), bounds);

		// draw text
		RectF boundsF = new RectF(bounds);
		
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		c.drawText(orientationLabel, 0 - boundsF.left - boundsF.width() / 2.0f,
						0 - boundsF.bottom + boundsF.height() / 2.0f, paint);
		
		Paint pPaint = new Paint();
		pPaint.setAntiAlias(true);
		pPaint.setFilterBitmap(false);
		pPaint.setStyle(Paint.Style.FILL);
		pPaint.setFilterBitmap(false);
		 
		pCanvas.drawBitmap(bm, x-DIRECTION_CIRCLE_AREA_RADIUS, y-DIRECTION_CIRCLE_AREA_RADIUS, pPaint);
		
	}
	// create a bitmap with a circle, used for the "dst" image
    static Bitmap makeDst(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        c.drawColor(0xFFa0a0ff);
        p.setColor(0xFF000000 | Color.DKGRAY);    
        c.drawOval(new RectF(0, 0, w*3/4, h*3/4), p);
        return bm;
    }
 
	// insert in strings.xml
	//public final static String COLOR_CHOOSE_PREFIX = "color_choose_prefix";

	public final static String BOTTOM_NAME = "bottom";
	public final static String OUTER_RING_NAME = "outer_ring";
	//public final static String DIRECTION_CIRCLE_AREA_NAME = "direction_circle_area";
	//public final static String MIDDLE_RING_NAME = "middle_ring";
	//public final static String INNER_RING_NAME = "inner_ring";
	//public final static String DIRECTION_LABEL_NAME = "direction_label";
	//public final static String NORTH_LABEL_NAME = "north_label";
	//public final static String DEGREE_LABEL_NAME = "degree_label";

	private DrawingComponent components[] = null;

	private static final float S2H = FloatMath.sqrt(2.0f) / 2.0f;

	public DrawingComponent[] getComponents() {
		if (components == null) {

			Resources resources = context.getResources();

			// rings
			FCircle outerRing = new FCircle(OUTER_RING_RADIUS * 0.85f, -OUTER_RING_RADIUS, MAX * 0.15f);
			FPointerDecorater pointerOuterRing = new FPointerDecorater(outerRing, OUTER_RING_RADIUS * S2H, -OUTER_RING_RADIUS * S2H);
			DrawingComponent compOuterRing = new DrawingComponent(0, OUTER_RING_NAME, pointerOuterRing, outerRingColor1, resources
					.getString(R.string.color_choose_OUTER_RING_NAME));
	
			FCircle bottom = new FCircle(-OUTER_GLASS_PLATE_RADIUS*0.85f, -OUTER_GLASS_PLATE_RADIUS, MAX*0.15f);
			FPointerDecorater pointerBottom = new FPointerDecorater(bottom, -OUTER_GLASS_PLATE_RADIUS * S2H, -OUTER_GLASS_PLATE_RADIUS * S2H);
			DrawingComponent compBottom = new DrawingComponent(0, BOTTOM_NAME, pointerBottom, bottomColor, resources
					.getString(R.string.color_choose_BOTTOM_NAME));

			components = new DrawingComponent[] { compOuterRing, compBottom };
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
