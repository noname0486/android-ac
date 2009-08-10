package de.rothbayern.android.ac.drawings;

import de.rothbayern.android.ac.R;
import android.content.Context;
import android.graphics.*;
import android.util.FloatMath;

public class StefanBaseDrawing implements CompassDrawing {

	private static final float MAX = 500;

	private static final int ROSE_COLOR1 = Color.GRAY;
	private static final int ROSE_COLOR2 = Color.WHITE;

	private static final float LABEL_RADIUS = MAX * 0.9f;
	private static final float TRIANGLE_LENGTH = MAX * 0.8f;

	private static final float TRIANGLE_WIDTH = MAX * 0.2f;

	private static final float LABEL_TEXT_SIZE = MAX * 0.16f;

	private Context context = null;
	private String orientationLabels[];

	public StefanBaseDrawing(Context c) {
		context = c;
		orientationLabels = context.getResources().getStringArray(
				R.array.orientations);

	}

	@Override
	public Bitmap getDrawing(int width, int height) {

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
				orientationLabels[0 * 4 + 1], orientationLabels[1 * 4 + 1],
				orientationLabels[2 * 4 + 1], orientationLabels[3 * 4 + 1] });
		drawCross(paint, c, triangleR, triangleL, 2, 67.5f, new String[] {
				orientationLabels[0 * 4 + 3], orientationLabels[1 * 4 + 3],
				orientationLabels[2 * 4 + 3], orientationLabels[3 * 4 + 3] });
		drawCross(paint, c, triangleR, triangleL, 1, 45f, new String[] {
				orientationLabels[0 * 4 + 2], orientationLabels[1 * 4 + 2],
				orientationLabels[2 * 4 + 2], orientationLabels[3 * 4 + 2] });
		drawCross(paint, c, triangleR, triangleL, 0, 0f, new String[] {
				orientationLabels[0 * 4], orientationLabels[1 * 4],
				orientationLabels[2 * 4], orientationLabels[3 * 4] });

		return (bm);
	}

	private void drawCross(Paint paint, Canvas c, Path pTriangleR,
			Path pTriangleL, int oType, float rotate, String labels[]) {

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
		c.rotate(rotate);
		for (int i = 0; i < 4; i++) {

			paint.setStyle(Paint.Style.FILL);
			paint.setColor(ROSE_COLOR1);
			triangleR.offset(0, 0);
			c.drawPath(triangleR, paint);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(1);
			paint.setColor(Color.BLACK);
			triangleR.offset(0, 0);
			c.drawPath(triangleR, paint);

			paint.setStyle(Paint.Style.FILL);
			paint.setColor(ROSE_COLOR2);
			triangleL.offset(0, 0);
			c.drawPath(triangleL, paint);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(1);
			paint.setColor(Color.BLACK);
			triangleL.offset(0, 0);
			c.drawPath(triangleL, paint);

			c.rotate(90);
		}
		c.rotate(-rotate);

		float angle = (float) (Math.PI * rotate / 180.0 - Math.PI / 2);
		float label_distance_scale = LABEL_RADIUS * triangleScale;
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setStrokeWidth(labelStrokeWidth);
		paint.setTextAlign(Paint.Align.CENTER);
		for (int i = 0; i < 4; i++) {
			float x = FloatMath.cos(angle) * label_distance_scale;
			float y = FloatMath.sin(angle) * label_distance_scale;

			c.drawText(labels[i], x, y + LABEL_TEXT_SIZE / 3, paint);

			angle += Math.PI / 2;
		}

	}

}
