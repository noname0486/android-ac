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

import afzkl.development.mColorPicker.ColorPickerActivity;
import android.app.*;
import android.content.*;
import android.graphics.Color;
import android.os.*;
import android.view.View;
import android.widget.TextView;
import de.rothbayern.android.ac.ColorChooseCompassView.OnComponentSelectedListener;
import de.rothbayern.android.ac.drawings.*;
import de.rothbayern.android.ac.pref.CompassPreferences;

public class ColorsActivity extends Activity {

	
	private static final int PREF_COLOR_ACTIVITY_REQUEST = 102;
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == PREF_COLOR_ACTIVITY_REQUEST && resultCode == RESULT_OK){
			String key = data.getStringExtra(ColorPickerActivity.INTENT_PREF_KEY);
			if ( key != null && !key.equals("")) {
				int newColor = data.getIntExtra(ColorPickerActivity.RESULT_COLOR, 0);
				CompassPreferences.getPreferences().setInt(key, newColor);
			}
		}
		viewCompass.setSelectedComponent(null);
		initView();
	};


	public ColorsActivity() {
		// if(Config.LOGD){
		// Log.d("construct","ColorsActivity");
		// }
	}

	// Controls
	private ColorChooseCompassView viewCompass;

	private int drawingKey = 0;

	private CompassDrawing drawing;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		initView();

	}

	private void initView() {
		Intent intent = getIntent();
		drawingKey = intent.getIntExtra("key", 0);
		switch (drawingKey) {
			case CompassViewHelper.LAYOUT_ROBA:
			default: {
				drawing = new RobARoseDrawing(ColorsActivity.this);
				break;
			}
			case CompassViewHelper.LAYOUT_NEEDLE: {
				drawing = new RobANeedleDrawing(ColorsActivity.this);
				break;
			}
			case CompassViewHelper.LAYOUT_STEFAN: {
				drawing = new StefanRoseDrawing(ColorsActivity.this);
				break;
			}
			case CompassViewHelper.LAYOUT_NICOLAS:
			{
				drawing = new NicolasRoseDrawing(ColorsActivity.this);
				break;
			}
			case CompassViewHelper.LAYOUT_NICOLAS_NEEDLE: {
				drawing = new NicolasNeedleDrawing(ColorsActivity.this);
				break;
			}

		}
		setContentView(R.layout.color_choose);
		viewCompass = (ColorChooseCompassView) findViewById(R.id.viewWorld);
		viewCompass.setCompassLayout(drawingKey, drawing);
		if (drawingKey == CompassViewHelper.LAYOUT_ROBA || drawingKey == CompassViewHelper.LAYOUT_NICOLAS) {
			viewCompass.setDirection(ACActivity.STD_ANGLE_START);
		}
		viewCompass.setOnComponentSelectedListener(onComponentSelectedListener);

		TextView lblHint = (TextView) findViewById(R.id.lbl_hint_tap_colors);
		CompassPreferences prefs = CompassPreferences.getPreferences();
		int bgColor = prefs.getInt(prefs.PREFS_COMPASS_BACKGROUNDCOLOR_KEY);
		float hsv[] = new float[3];
		Color.colorToHSV(bgColor, hsv);
		if (hsv[2] < 0.35f) {
			lblHint.setTextColor(Color.WHITE);
		} else {
			lblHint.setTextColor(Color.BLACK);
		}
	}

	OnComponentSelectedListener onComponentSelectedListener = new OnComponentSelectedListener() {
		public void onSelected(View f, DrawingComponent comp) {
			Intent i = new Intent(ColorsActivity.this, ColorPickerActivity.class);
			int startColor = drawing.getColorPreference(comp);
			i.putExtra(ColorPickerActivity.INTENT_DATA_INITIAL_COLOR, startColor);
			i.putExtra(ColorPickerActivity.INTENT_PREF_KEY, drawing.toPrefName(comp));
			i.putExtra(ColorPickerActivity.INTENT_TITLE, comp.getTitle());
			startActivityForResult(i, PREF_COLOR_ACTIVITY_REQUEST);
		}

	};


}