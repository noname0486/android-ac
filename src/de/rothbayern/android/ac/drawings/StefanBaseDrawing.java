package de.rothbayern.android.ac.drawings;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.util.FloatMath;
import de.rothbayern.android.ac.R;
import de.rothbayern.android.ac.geometry.*;

public class StefanBaseDrawing extends CompassDrawing {

	private int roseColor1 = Color.GRAY;
	private int roseColor2 = Color.WHITE;

	private static final float LABEL_RADIUS = MAX * 0.9f;
	private static final float TRIANGLE_LENGTH = MAX * 0.8f;

	private static final float TRIANGLE_WIDTH = MAX * 0.2f;

	private static final float LABEL_TEXT_SIZE = MAX * 0.16f;
	
	private int labelCardinalDirectionColor = Color.BLACK;
	private int labelIntermediateDirectionColor = Color.BLACK;
	private int labelIntermediate2DirectionColor = Color.BLACK;


	private Context context = null;
	private String directionLabels[];

	public StefanBaseDrawing(Context c) {
		context = c;
		directionLabels = context.getResources().getStringArray(
				R.array.orientations);

	}

	private void loadPrefColors() {
		labelCardinalDirectionColor = getColorPreference(searchByName(LABEL_CARDINAL_DIRECTION_NAME));
		labelIntermediateDirectionColor = getColorPreference(searchByName(LABEL_INTERMEDIATE_DIRECTION_NAME));
		labelIntermediate2DirectionColor = getColorPreference(searchByName(LABEL_INTERMEDIATE2_DIRECTION_NAME));
		roseColor1 = getColorPreference(searchByName(ROSE1_NAME));
		roseColor2 = getColorPreference(searchByName(ROSE2_NAME));

	}

	
	@Override
	public Bitmap getDrawing(int width, int height) {

		loadPrefColors();
		
		int minpx = Math.min(width, height);

		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);

		Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bm);

		c.scale(minpx / (2 * MAX), minpx / (2 * MAX));
		c.translate(MAX, MAX);

		final float HS2 = 0.5f * FloatMath.sqrt(2);
		Path triangleR = new Path();
		triangleR.moveTo(0, 0);
		triangleR.lineTo(HS2 * TRIANGLE_WIDTH / 2, -HS2 * TRIANGLE_WIDTH / 2);
		triangleR.lineTo(0, -TRIANGLE_LENGTH);
		triangleR.close();

		Path triangleL = new Path();
		triangleL.moveTo(0, 0);
		triangleL.lineTo(-HS2 * TRIANGLE_WIDTH / 2, -HS2 * TRIANGLE_WIDTH / 2);
		triangleL.lineTo(0, -TRIANGLE_LENGTH);
		triangleL.close();

		// main directions
		drawCross(paint, c, triangleR, triangleL, 2, 22.5f, new String[] {
				directionLabels[0 * 4 + 1], directionLabels[1 * 4 + 1],
				directionLabels[2 * 4 + 1], directionLabels[3 * 4 + 1] },
				labelIntermediate2DirectionColor);
		drawCross(paint, c, triangleR, triangleL, 2, 67.5f, new String[] {
				directionLabels[0 * 4 + 3], directionLabels[1 * 4 + 3],
				directionLabels[2 * 4 + 3], directionLabels[3 * 4 + 3] },
				labelIntermediate2DirectionColor);
		drawCross(paint, c, triangleR, triangleL, 1, 45f, new String[] {
				directionLabels[0 * 4 + 2], directionLabels[1 * 4 + 2],
				directionLabels[2 * 4 + 2], directionLabels[3 * 4 + 2] },
				labelIntermediateDirectionColor);
		drawCross(paint, c, triangleR, triangleL, 0, 0f, new String[] {
				directionLabels[0 * 4], directionLabels[1 * 4],
				directionLabels[2 * 4], directionLabels[3 * 4] },
				labelCardinalDirectionColor);

		return (bm);
	}

	private void drawCross(Paint paint, Canvas c, Path pTriangleR,
			Path pTriangleL, int oType, float startRotateAngle, String labels[], int pLabelColor) {

		float triangleScale;
		float textScale;
		float labelStrokeWidth;
		switch (oType) {
			case 0:
				triangleScale = 1.0f;
				textScale = 1.0f;
				labelStrokeWidth = LABEL_TEXT_SIZE / 10;
				break;
			case 1:
				triangleScale = 0.9f;
				textScale = 0.75f;
				labelStrokeWidth = LABEL_TEXT_SIZE / 20;
				break;

			case 2:
			default:
				triangleScale = 0.8f;
				textScale = 0.5f;
				labelStrokeWidth = LABEL_TEXT_SIZE / 40;
				break;
		}

		paint.setTextAlign(Paint.Align.CENTER);
		paint.setTypeface(Typeface.SERIF);
		paint.setTextSize(textScale * LABEL_TEXT_SIZE);

		Matrix shrink = new Matrix();
		shrink.setScale(triangleScale, triangleScale);
		Path triangleL = new Path(pTriangleL);
		Path triangleR = new Path(pTriangleR);
		triangleL.transform(shrink);
		triangleR.transform(shrink);
		c.rotate(startRotateAngle);
		for (int i = 0; i < 4; i++) {

			paint.setStyle(Paint.Style.FILL);
			paint.setColor(roseColor2);
			triangleR.offset(0, 0);
			c.drawPath(triangleR, paint);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(1);
			paint.setColor(Color.BLACK);
			triangleR.offset(0, 0);
			c.drawPath(triangleR, paint);

			paint.setStyle(Paint.Style.FILL);
			paint.setColor(roseColor1);
			triangleL.offset(0, 0);
			c.drawPath(triangleL, paint);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(1);
			paint.setColor(Color.BLACK);
			triangleL.offset(0, 0);
			c.drawPath(triangleL, paint);

			c.rotate(90);
		}
		c.rotate(-startRotateAngle);

		// !!!! sin cos (counter clockwise) <=> Compass clockwise
		// !!!! sin cos (start on right side) <=> Compass starts on top
		float rad = (float)(Math.PI/2 - (startRotateAngle / 180.0)*Math.PI);  // 0° = North +90
		rad = (float)(Math.PI/2 - (startRotateAngle / 180.0)*Math.PI);
		float label_distance_scale = LABEL_RADIUS * triangleScale;
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setStrokeWidth(labelStrokeWidth);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setColor(pLabelColor);
		for (int i = 0; i < 4; i++) {
			float x = FloatMath.cos(rad) * label_distance_scale;
			float y = -FloatMath.sin(rad) * label_distance_scale;

			c.drawText(labels[i], x, y + LABEL_TEXT_SIZE / 3, paint);

			rad -= Math.PI / 2;
		}

	}

	
	public final static String LABEL_CARDINAL_DIRECTION_NAME = "direction_cardinal_label";
	public final static String LABEL_INTERMEDIATE_DIRECTION_NAME = "direction_intermediate_label";
	public final static String LABEL_INTERMEDIATE2_DIRECTION_NAME = "direction_intermediate2_label";
	public final static String ROSE1_NAME = "rose1_label";
	public final static String ROSE2_NAME = "rose2_label";

	private DrawingComponent components[] = null;


	@Override
	public DrawingComponent[] getComponents() {
		if (components == null) {
			
			Resources resources = context.getResources();
			
			FArray cardinalLabels = new FArray(4);
			addLabelFigures(cardinalLabels, 4, 0.0f, LABEL_RADIUS*1.0f);
			DrawingComponent compCardinalLabels = new DrawingComponent(0,LABEL_CARDINAL_DIRECTION_NAME,cardinalLabels,labelCardinalDirectionColor,resources.getString(R.string.color_choose_DIRECTIONS_CARDINALE_LABEL_NAME));
			
			FArray intermediateLabels = new FArray(4);
			addLabelFigures(intermediateLabels, 4, 45.0f, LABEL_RADIUS*0.9f);
			DrawingComponent compIntermediateLabels = new DrawingComponent(0,LABEL_INTERMEDIATE_DIRECTION_NAME,intermediateLabels,labelIntermediateDirectionColor,resources.getString(R.string.color_choose_DIRECTIONS_INTERMEDIATE_LABEL_NAME));
			
			FArray intermediate2Labels = new FArray(8);
			addLabelFigures(intermediate2Labels, 8, 22.5f, LABEL_RADIUS*0.8f);
			DrawingComponent compIntermediate2Labels = new DrawingComponent(0,LABEL_INTERMEDIATE2_DIRECTION_NAME,intermediate2Labels,labelIntermediate2DirectionColor,resources.getString(R.string.color_choose_DIRECTIONS_INTERMEDIATE2_LABEL_NAME));
			
			FCircle rose1 = new FCircle(-LABEL_RADIUS/2,-LABEL_RADIUS,LABEL_TEXT_SIZE);
			FPointerDecorater pointerRose1 = new FPointerDecorater(rose1,-TRIANGLE_WIDTH/5.0f,-TRIANGLE_LENGTH/2);
			DrawingComponent compRose1 = new DrawingComponent(0,ROSE1_NAME,pointerRose1,roseColor1,resources.getString(R.string.color_choose_ROSE1_NAME));
			
			FCircle rose2 = new FCircle(LABEL_RADIUS/2,-LABEL_RADIUS,LABEL_TEXT_SIZE);
			FPointerDecorater pointerRose2 = new FPointerDecorater(rose2,TRIANGLE_WIDTH/5.0f,-TRIANGLE_LENGTH/2);
			DrawingComponent compRose2 = new DrawingComponent(0,ROSE2_NAME,pointerRose2,roseColor2,resources.getString(R.string.color_choose_ROSE2_NAME));
			
			
			
			components = new DrawingComponent[] {compRose1,compRose2,compCardinalLabels,compIntermediateLabels,compIntermediate2Labels};
			int l = components.length;
			for (int i = 0; i < l; i++) {
				components[i].setPos(i);
			}
		}
		return (components);
	}

	private static void addLabelFigures(FArray labels, int count, float startAngle, float label_distance_scale) {
		// !!!! sin cos (counter clockwise) <=> Compass clockwise
		// !!!! sin cos (start on right side) <=> Compass starts on top
		float startRad = (float)(Math.PI/2 - (startAngle / 180.0)*Math.PI);  // 0° = North +90
		for (int i = 0; i < count; i++) {
			float x = FloatMath.cos(startRad) * label_distance_scale;
			float y = -FloatMath.sin(startRad) * label_distance_scale;
			FRect r = FRect.fromMiddle(x, y, 2.0f*LABEL_TEXT_SIZE,LABEL_TEXT_SIZE*1.5f);
			labels.add(r);
			startRad -= (2*Math.PI) / count;
		}
	}

	
	private final static String DRAWING_NAME = "Stefan";

	@Override
	public String getPrefNamePrefix() {
		return DRAWING_NAME;
	}

}
