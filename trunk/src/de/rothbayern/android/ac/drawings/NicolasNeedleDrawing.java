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
import de.rothbayern.android.ac.R;
import de.rothbayern.android.ac.geometry.*;

public class NicolasNeedleDrawing extends NeedleDrawing {

	private static final int NEEDLE_NORTH_COLOR = Color.RED&0x60FFFFFF;
	private static final int NEEDLE_SOUTH_COLOR = Color.BLACK;
	private static final int NEEDLE_BUTTON_COLOR = Color.GRAY;
	
	private int needleNorthColor = NEEDLE_NORTH_COLOR;
	private int needleSouthColor = NEEDLE_SOUTH_COLOR;
	private int needleButtonColor = NEEDLE_BUTTON_COLOR;

	private static final float TRIANGLE_LENGTH = MAX * 0.71f;
	private static final float TRIANGLE_WIDTH = MAX * 0.18f;

	private static final float BUTTON_RADIUS = MAX * 0.08f;

	private Context context = null;

	public NicolasNeedleDrawing(Context c) {
		context = c;
	}

	private void loadPrefColors() {
		needleNorthColor = getColorPreference(searchByName(NEEDLE_NORTH_NAME));
		needleSouthColor = getColorPreference(searchByName(NEEDLE_SOUTH_NAME));
		needleButtonColor = getColorPreference(searchByName(NEEDLE_BUTTON_NAME));
	}

	
	@Override
	public Bitmap getDrawing(int width, int height) {
		//loadPrefColors();
		int minpx = Math.min(width, height);

		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);

		float fMaxWidth=Math.max(BUTTON_RADIUS,TRIANGLE_WIDTH);
		int iMaxWidth=(int)((fMaxWidth/MAX)*width);
		float fMaxHeight=TRIANGLE_LENGTH;
		int iMaxHeight=(int)((fMaxHeight/MAX)*height);
		Bitmap bm = Bitmap.createBitmap(iMaxWidth, iMaxHeight, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bm);

		c.scale(minpx / (2 * MAX), minpx / (2 * MAX));
		c.translate((int)fMaxWidth, (int)fMaxHeight);

		Path triangle = new Path();
		triangle.moveTo(TRIANGLE_WIDTH, 0);
		triangle.lineTo(0,-TRIANGLE_LENGTH);
		triangle.lineTo(-TRIANGLE_WIDTH,0);
		triangle.close();

		paint.setStyle(Paint.Style.FILL);
		paint.setColor(needleNorthColor);
		triangle.offset(0, 0);
		c.drawPath(triangle, paint);

		c.rotate(180);
		paint.setColor(needleSouthColor);
		triangle.offset(0, 0);
		c.drawPath(triangle, paint);

		paint.setColor(needleButtonColor);
		c.drawCircle(0, 0, BUTTON_RADIUS, paint);
		
		return (bm);
	}


	public final static String	NEEDLE_NORTH_NAME = "needle_north";
	public final static String 	NEEDLE_SOUTH_NAME = "needle_south";
	public final static String  NEEDLE_BUTTON_NAME = "needle_button";

	
	DrawingComponent components[] = null;

	public DrawingComponent[] getComponents() {
		if (components == null) {

			final float TRIANGLE_FRAME_LENGTH = TRIANGLE_LENGTH*1.2f;
			final float TRIANGLE_FRAME_WIDTH = TRIANGLE_WIDTH*2.0f;
			
			Resources resources = context.getResources();
			
			// north label
			Figure needleNorth = FRect.fromMiddle(0, -TRIANGLE_FRAME_LENGTH/2, TRIANGLE_FRAME_WIDTH*2.0f,TRIANGLE_FRAME_LENGTH);
			DrawingComponent compNeedleNorth = new DrawingComponent(0, NEEDLE_NORTH_NAME, needleNorth,needleNorthColor,resources.getString(R.string.color_choose_NEEDLE_NORTH_NAME));

			Figure needleSouth = FRect.fromMiddle(0, TRIANGLE_FRAME_LENGTH/2, TRIANGLE_FRAME_WIDTH*2.0f,TRIANGLE_FRAME_LENGTH);
			DrawingComponent compNeedleSouth = new DrawingComponent(0, NEEDLE_SOUTH_NAME, needleSouth,needleSouthColor,resources.getString(R.string.color_choose_NEEDLE_SOUTH_NAME));
			
			Figure needleButton = new FCircle(0, 0, BUTTON_RADIUS*4);
			DrawingComponent compNeedleButton = new DrawingComponent(0, NEEDLE_BUTTON_NAME, needleButton,needleButtonColor,resources.getString(R.string.color_choose_NEEDLE_BUTTON_NAME));


			components = new DrawingComponent[] { compNeedleButton,compNeedleNorth, compNeedleSouth};
			int l = components.length;
			for (int i = 0; i < l; i++) {
				components[i].setPos(i);
			}
		}
		return (components);
	}

	private final static String DRAWING_NAME = "NicolasNeedle";

	@Override
	public String getPrefNamePrefix() {
		return DRAWING_NAME;
	}


}
