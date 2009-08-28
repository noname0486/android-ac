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
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class CompassStandardView extends View{

	CompassViewHelper helper = new CompassViewHelper();
	
	
	

	public CompassStandardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		init();
		
	}

	public CompassStandardView(Context context) {
		super(context);
		init();
	}

	private void init(){
		helper.init(this.getContext());
	}
	
	public void loadPrefs(){
		helper.loadPrefs();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		helper.onSizeChanged(w,h,oldw,oldh);
	}


	@Override
	protected void onDraw(Canvas canvas) {
		helper.onDraw(canvas);
	}

	public void setCompassLayout(int compassLayout) {
		helper.setCompassLayout(compassLayout);
	}

	public int getCompassLayout() {
		return helper.getCompassLayout();
	}

	public int getBgColor() {
		return helper.getBgColor();
	}

	public void setBgColor(int bgColor) {
		helper.setBgColor(bgColor);
	}

	public void setDirection(float direction) {
		helper.setDirection(direction);
		
	}



}
