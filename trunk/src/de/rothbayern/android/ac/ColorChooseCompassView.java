package de.rothbayern.android.ac;


import android.content.Context;
import android.graphics.*;
import android.util.*;
import android.view.*;
import de.rothbayern.android.ac.drawings.*;

public class ColorChooseCompassView extends CompassStandardView {

	DrawingComponent components[] = null;
	Paint paintSelected = null;
	Paint paintNotSelected1 = null;
	Paint paintNotSelected2 = null;
	Matrix m = null;

	DrawingComponent selectedComponent = null;
	public void setSelectedComponent(DrawingComponent selectedComponent) {
		this.selectedComponent = selectedComponent;
	}
	
	
	public ColorChooseCompassView(Context context) {
		super(context);
		init();
	}

	public ColorChooseCompassView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private CompassDrawing drawing = null;

	public void setCompassLayout(int compassLayout, CompassDrawing drawing) {
		super.setCompassLayout(compassLayout);
		this.drawing = drawing;
		components = drawing.getComponents();
	}


	
	private void init() {
		calcMatrix();

		paintSelected = new Paint();
		paintSelected.setStyle(Paint.Style.STROKE);
		paintSelected.setColor(Color.BLUE);
		
		paintNotSelected1 = new Paint();
		paintNotSelected1.setStyle(Paint.Style.STROKE);
		paintNotSelected1.setColor(Color.DKGRAY);

		makeScrollPaint(0);
		
	}


	private void calcMatrix() {
		m = new Matrix();
		PointF middle = helper.getMiddle();
		//Log.d("middle-calc"," "+middle.x+", "+middle.y);
		m.postTranslate(middle.x, middle.y);
		m.invert(m);
	}

	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		calcMatrix();
	}


	private void makeScrollPaint(float phase) {
		PathEffect pe2 = new DashPathEffect(new float[] {5, 5}, phase);
		paintNotSelected2 = new Paint(paintNotSelected1);
		paintNotSelected2.setColor(Color.WHITE);
		paintNotSelected2.setPathEffect(pe2);
	}  
	
	float phase = 0;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if(selectedComponent!=null){
			selectedComponent.getF().draw(canvas, paintSelected);
			//selectedComponent.getF().draw(canvas, paintSelected2);
		}
		else {
			makeScrollPaint(phase);
			phase+=1;
			int len = components.length;
			for (int i = 0; i < len; i++) {
				components[i].getF().draw(canvas, paintNotSelected1);
				components[i].getF().draw(canvas, paintNotSelected2);
			}
			invalidate();
		}
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// Only work, if there is nothing selected
		if(selectedComponent == null){ 
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: 
				case MotionEvent.ACTION_MOVE:{
					// TODO don't works on first call
						float src[] = { event.getX(), event.getY() };
						float dst[] = new float[2];
						m.mapPoints(dst, src);
						DrawingComponent comp = searchComponent(dst[0], dst[1]);
						if (comp != null) {
//							Log.d("comp found","yes =>["+src[0]+", "+src[1]+"], ["+dst[0]+", "+dst[1]+"]");
							invalidate();
							if(onComponentSelectedListener != null){
								setSelectedComponent(comp);
								onComponentSelectedListener.onSelected(this,comp);
							}
						}
						else {
//							Log.d("comp found","no =>["+src[0]+", "+src[1]+"], ["+dst[0]+", "+dst[1]+"]");
						}
				}
			}
		}

		return true;
	}


	DrawingComponent searchComponent(float x, float y) {
		int len = components.length;
		for (int i = 0; i < len; i++) {
			DrawingComponent cur = components[i];
			if (cur.getF().contains(x, y)) {
				return (cur);
			}
		}
		return (null);

	}
	
	OnComponentSelectedListener onComponentSelectedListener = null;
	
	public void setOnComponentSelectedListener(OnComponentSelectedListener onComponentSelectedListener) {
		this.onComponentSelectedListener = onComponentSelectedListener;
	}
	
	public interface OnComponentSelectedListener {
		void onSelected(View f,DrawingComponent p);
	}

}
