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


import android.app.*;
import android.content.*;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.*;
import android.view.View;
import android.widget.TextView;
import de.rothbayern.android.ac.ColorChooseCompassView.OnComponentSelectedListener;
import de.rothbayern.android.ac.drawings.*;
import de.rothbayern.android.ac.keithwiley.*;
import de.rothbayern.android.ac.pref.CompassPreferences;

public class ColorsActivity extends Activity  {

	private static final int MSG_INVALIDATE_COMPASS = 100;
	private static final int MSG_DELAY_START_COLOR_CHOOSE = 101;
	
	Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				case MSG_INVALIDATE_COMPASS:{
					viewCompass.invalidate();
					break;
				}
				case MSG_DELAY_START_COLOR_CHOOSE: {
					try {Thread.sleep(750);} catch (InterruptedException e) {}
					if(dlgColorCoose!=null){
					   dlgColorCoose.show();
					}

					break;
				}
			}
		}
	};
	
	public ColorsActivity() {
//		if(Config.LOGD){
//			Log.d("construct","ColorsActivity");
//		}
	}

	// Controls
	private ColorChooseCompassView viewCompass;
	
	private int drawingKey=0;
	
	private CompassDrawing drawing;

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		Intent intent = getIntent();
		drawingKey = intent.getIntExtra("key", 0);
		switch(drawingKey){
			case CompassViewHelper.LAYOUT_STEFAN:{
				drawing = new StefanBaseDrawing(ColorsActivity.this);
				break;
			}
			case CompassViewHelper.LAYOUT_NEEDLE:{
				drawing = new NeedleBaseDrawing(ColorsActivity.this);
				break;
			}
			case CompassViewHelper.LAYOUT_ROBA:
										default:{
				drawing = new RobABaseDrawing(ColorsActivity.this);
				break;
			}
		}
		setContentView(R.layout.color_choose);
		viewCompass = (ColorChooseCompassView) findViewById(R.id.viewWorld);
	    viewCompass.setCompassLayout(drawingKey,drawing);
	    if(drawingKey == CompassViewHelper.LAYOUT_ROBA){
	    	viewCompass.setDirection(30);
	    }
		viewCompass.setOnComponentSelectedListener(onComponentSelectedListener);
		
		TextView lblHint = (TextView) findViewById(R.id.lbl_hint_tab_colors);
		CompassPreferences prefs = CompassPreferences.getPreferences();
		int bgColor = prefs.getInt(prefs.PREFS_COMPASS_BACKGROUNDCOLOR_KEY);
		float hsv[] = new float[3];
		Color.colorToHSV(bgColor, hsv);
		if(hsv[2]<0.35f){
			lblHint.setTextColor(Color.WHITE);
		}
		else {
			lblHint.setTextColor(Color.BLACK);
		}
	

		
	}
	

	private DialogListener mOcl = new DialogListener();
	private class DialogListener implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			
			if (which == DialogInterface.BUTTON_POSITIVE) {
				drawing.setColorPreference(drawingComponent, choiceColor);
			}
			if(which == DialogInterface.BUTTON_NEGATIVE || which == DialogInterface.BUTTON_POSITIVE){
				cleanDialog();
			}
			else {
//				Log.w("shouldn't be here", this.getClass().toString());
			}
			
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			cleanDialog();
		}


		private void cleanDialog() {
			drawingComponent = null;
			viewCompass.setSelectedComponent(null);
			viewCompass.loadPrefs();
			Message m = Message.obtain(myHandler, MSG_INVALIDATE_COMPASS);
			m.sendToTarget();
			dlgColorCoose = null;
		}

	};
	
	private int choiceColor = 0;
	OnColorChangedListener onColorChangedListener = new OnColorChangedListener (){

		@Override
		public void colorChanged(int color) {
			ColorsActivity.this.choiceColor=color;
		}
		
	};

	private DrawingComponent drawingComponent = null;
	OnComponentSelectedListener onComponentSelectedListener = new OnComponentSelectedListener(){
		@Override
		public void onSelected(View f, DrawingComponent comp) {
			
			if (dlgColorCoose == null) {
				drawingComponent = comp;
				AlertDialog.Builder builder = new AlertDialog.Builder(ColorsActivity.this);
				builder.setTitle(comp.getTitle());
				builder.setPositiveButton(android.R.string.ok, mOcl);
				builder.setNegativeButton(android.R.string.cancel, mOcl);
				int startColor = drawing.getColorPreference(drawingComponent);
				ColorPickerView cpView = new ColorPickerView(ColorsActivity.this, onColorChangedListener, 90*f.getWidth()/100, 90*f.getHeight()/100, startColor);
				builder.setView(cpView);
				builder.setOnCancelListener(mOcl);
				dlgColorCoose = builder.create();
				Message m = Message.obtain(myHandler, MSG_DELAY_START_COLOR_CHOOSE);
				m.sendToTarget();
			}
		}
		
	};

	
	AlertDialog dlgColorCoose;
	
	



}