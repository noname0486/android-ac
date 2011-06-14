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
import android.util.AttributeSet;
import android.view.*;
import de.rothbayern.android.ac.drawings.*;
import de.rothbayern.android.ac.misc.CorrectMatrix;

public class ColorChooseCompassView extends CompassStandardView {

	DrawingComponent components[] = null;
	Paint paintSelected = null;
	Paint paintNotSelected1 = null;
	Paint paintNotSelected2 = null;
	CorrectMatrix m = null;

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


	public void setCompassLayout(int compassLayout, CompassDrawing drawing) {
		super.setCompassLayout(compassLayout);
		components = drawing.getComponents();
	}


	
	private void init() {
		calcMatrix((int)CompassDrawing.MAX, (int)CompassDrawing.MAX);

		paintSelected = new Paint();
		paintSelected.setStyle(Paint.Style.STROKE);
		paintSelected.setColor(Color.BLUE);
		
		paintNotSelected1 = new Paint();
		paintNotSelected1.setStyle(Paint.Style.STROKE);
		paintNotSelected1.setColor(Color.DKGRAY);

		makeScrollPaint(0);
		
	}


	private void calcMatrix(int width, int height) {
		m = new CorrectMatrix();
		PointF middle = helper.getMiddle();
		//Log.d("middle-calc"," "+middle.x+", "+middle.y);
		m.postTranslate(-middle.x, -middle.y);
		int minpx = Math.min(width, height);
		m.postScale(1/(minpx / (2 * CompassDrawing.MAX)), 1/(minpx / (2 * CompassDrawing.MAX)));
	}

	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		calcMatrix(w,h);
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
	
		int minpx = Math.min(canvas.getWidth(), canvas.getHeight());
		canvas.scale(minpx / (2 * CompassDrawing.MAX), minpx / (2 * CompassDrawing.MAX));
		//canvas.translate(CompassDrawing.MAX, CompassDrawing.MAX);

		
		
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
						PointF src = new PointF(event.getX(), event.getY());
						PointF dst = new PointF();
						m.mapPoints(dst, src);
						DrawingComponent comp = searchComponent(dst);
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


	DrawingComponent searchComponent(PointF point) {
		int len = components.length;
		for (int i = 0; i < len; i++) {
			DrawingComponent cur = components[i];
			if (cur.getF().contains(point.x, point.y)) {
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
