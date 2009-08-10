package de.rothbayern.android.ac.drawings;

import de.rothbayern.android.ac.R;
import android.content.Context;
import android.graphics.*;
import android.test.FlakyTest;
import android.util.FloatMath;


public class RobABaseDrawing implements CompassDrawing{

	private static final float MAX = 500;
	
	private static final float OUTER_RING_RADIUS = MAX*0.96f;
	private static final float OUTER_RING_WIDTH = MAX*0.03f;
	private static final int OUTER_RING_COLOR1 = Color.argb(0xff, 0xDD, 0xDD, 0xDD);
	private static final int OUTER_RING_COLOR2 = Color.DKGRAY;

	private static final float DEGREES_RADIUS = MAX*0.75f;
	private static final float DEGREES_TEXT_SIZE = MAX*0.14f;
	private static final float DEGREES_TEXT_WIDTH = DEGREES_TEXT_SIZE/16;
	private static final int DEGREES_TEXT_COLOR = Color.BLACK;
	
	private static final float MIDDLE_RING_RADIUS = MAX*0.73f;
	private static final float MIDDLE_RING_WIDTH = MAX*0.007f;
	private static final int MIDDLE_RING_COLOR = Color.BLACK;
	private static final float MIDDLE_RING_MARKER_LENGTH = MAX*0.07f;

	
	private static final float INNER_RING_RADIUS = MAX*0.5f;
	private static final float INNER_RING_WIDTH = MAX*0.1f;
	private static final int INNER_RING_COLOR = Color.BLACK;
	private static final float INNER_RING_MARKER_LENGTH = INNER_RING_WIDTH;
	private static final float INNER_RING_MARKER_WIDTH = INNER_RING_WIDTH*1.2f;
	
	private static final float DIRECTION_TEXT_SIZE = MAX*0.1f;
	private static final int DIRECTION_NORTH_TEXT_COLOR = Color.RED;
	private static final int DIRECTION_TEXT_COLOR = Color.GRAY;
	
	
	
	Context context;
	
	public RobABaseDrawing(Context c) {
		context = c;
	}
	
	@Override
	public Bitmap getDrawing(int width, int height) {
		
		int minpx = Math.min(width, height);
		
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);
		
		
		
		Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bm);
		
		c.scale(minpx/(2*MAX), minpx/(2*MAX));
		c.translate(MAX, MAX);
		
		// outer ring
		Shader shader = new LinearGradient(-OUTER_RING_RADIUS,0,OUTER_RING_RADIUS,0,OUTER_RING_COLOR1,OUTER_RING_COLOR2,
				           				Shader.TileMode.CLAMP);
		paint.setShader(shader);
		paint.setStrokeWidth(OUTER_RING_WIDTH);
		c.drawCircle(0, 0, OUTER_RING_RADIUS, paint);
		
		shader = new LinearGradient(-OUTER_RING_RADIUS,0,OUTER_RING_RADIUS,0,OUTER_RING_COLOR2,OUTER_RING_COLOR1,
   				Shader.TileMode.CLAMP);
		paint.setShader(shader);
		c.drawCircle(0, 0, OUTER_RING_RADIUS-OUTER_RING_WIDTH, paint);
		paint.setShader(null);
		
		// middle ring
		paint.setColor(MIDDLE_RING_COLOR);
		paint.setStrokeWidth(MIDDLE_RING_WIDTH);
		c.drawCircle(0, 0, MIDDLE_RING_RADIUS, paint);
		
		float p12 = (float)Math.PI/12;
		for (int i = 0; i < 24; i++) {
			float rad = p12*i;
			float x = FloatMath.cos(rad);
			float y = FloatMath.sin(rad);
			c.drawLine(x*MIDDLE_RING_RADIUS, y*MIDDLE_RING_RADIUS, 
					   x*(MIDDLE_RING_RADIUS-MIDDLE_RING_MARKER_LENGTH), 
					   y*(MIDDLE_RING_RADIUS-MIDDLE_RING_MARKER_LENGTH), 
					   paint);			
		}
		
		// inner ring
		paint.setColor(INNER_RING_COLOR);
		paint.setStrokeWidth(INNER_RING_MARKER_WIDTH);
		c.drawCircle(0, 0, INNER_RING_RADIUS, paint);

		float p6 = (float)Math.PI/6;
		for (int i = 0; i < 12; i++) {
			float rad = p6*i;
			float x = FloatMath.cos(rad);
			float y = FloatMath.sin(rad);
			c.drawLine(x*INNER_RING_RADIUS, y*INNER_RING_RADIUS, 
					   x*(INNER_RING_RADIUS+INNER_RING_MARKER_LENGTH), 
					   y*(INNER_RING_RADIUS+INNER_RING_MARKER_LENGTH), 
					   paint);			
		}

		// Symbols
		paint.setColor(DEGREES_TEXT_COLOR);
		paint.setTextSize(DEGREES_TEXT_SIZE);
		paint.setStrokeWidth(0);
		paint.setTextAlign(Paint.Align.CENTER);
		//paint.setAntiAlias(false);
		

		c.drawText("0°", 0+DEGREES_TEXT_SIZE/5, -DEGREES_RADIUS-DEGREES_TEXT_SIZE*0.2f, paint);
		c.drawText("90°", DEGREES_RADIUS, DEGREES_TEXT_SIZE/3, paint);
		c.drawText("180°", 0+DEGREES_TEXT_SIZE/5, DEGREES_RADIUS+DEGREES_TEXT_SIZE*0.8f, paint);
		c.drawText("270°", -DEGREES_RADIUS, DEGREES_TEXT_SIZE/3, paint);
		
		paint.setTextSize(DIRECTION_TEXT_SIZE);
		Typeface tf = paint.getTypeface();
		paint.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD));
		paint.setColor(DIRECTION_NORTH_TEXT_COLOR);
		String orientationLabels[] = context.getResources().getStringArray(R.array.orientations);
		c.drawText(orientationLabels[0*4], 0, -INNER_RING_RADIUS+DIRECTION_TEXT_SIZE/6, paint);
		paint.setColor(DIRECTION_TEXT_COLOR);
		c.drawText(orientationLabels[1*4], INNER_RING_RADIUS+DIRECTION_TEXT_SIZE/3, 0+DIRECTION_TEXT_SIZE/3, paint);
		c.drawText(orientationLabels[2*4], 0, INNER_RING_RADIUS+DIRECTION_TEXT_SIZE/2, paint);
		c.drawText(orientationLabels[3*4], -INNER_RING_RADIUS-DIRECTION_TEXT_SIZE/4, 0+DIRECTION_TEXT_SIZE/3, paint);
		
		
		return(bm);
	}

}
