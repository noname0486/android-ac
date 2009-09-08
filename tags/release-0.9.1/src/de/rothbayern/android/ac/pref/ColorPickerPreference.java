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


package de.rothbayern.android.ac.pref;


import android.app.Activity;
import android.content.*;
import android.graphics.Color;
import android.preference.*;
import android.util.*;
import android.view.View;
import android.widget.EditText;
import de.rothbayern.android.ac.keithwiley.*;

/**
 * A {@link Preference} that allows for string
 * input.
 * <p>
 * It is a subclass of {@link DialogPreference} and shows the {@link EditText}
 * in a dialog. This {@link EditText} can be modified either programmatically
 * via {@link #getEditText()}, or through XML by setting any EditText
 * attributes on the EditTextPreference.
 * <p>
 * This preference will store a string into the SharedPreferences.
 * <p>
 */
public class ColorPickerPreference extends DialogPreference {
    /**
     * The edit text shown in the dialog.
     */
    
	ColorPickerPreferenceView mPickerView;
	int mColor;
	
    public ColorPickerPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public ColorPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private Activity context;
	private void init(Context context, AttributeSet attrs) {
		this.context = (Activity)context;
	}

    public ColorPickerPreference(Context context) {
        this(context, null);
        init(context, null);
    }
   
    
    @Override
    protected View onCreateDialogView() {
    	
		//mPickerView = new ColorPickerFromDemoView(getContext(),null,mColor);
    	OnColorChangedListener l = new OnColorChangedListener() {
			public void colorChanged(int color) {
				setColor(""+color);
			}
		};
		
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);

		int screenWidth = dm.widthPixels;
		int screenHeight = dm.heightPixels; 
		

    	try {
			mPickerView = new ColorPickerView(getContext(), l, screenWidth, screenHeight, mColor);
		} catch (Exception e) {
			throw new IllegalStateException("Can't open ColorPicker",e);
		}
        mPickerView.setEnabled(true);
    	return (View)mPickerView;
    }
    
    @Override
    protected void onSetInitialValue(boolean restorePersistedValue,
    		Object defaultValue) {
    	if(defaultValue instanceof String){
    		String value = (String)defaultValue;
            setColor(restorePersistedValue ? getPersistedString(Integer.toString(mColor)) : value);
    	}
    	else {
            setColor(restorePersistedValue ? getPersistedString(Integer.toString(mColor)) : Integer.toString(Color.WHITE));
    	}
    }

    private void setColor(String sColor) {
    	try {
			mColor = Integer.parseInt(sColor);
		} catch (NumberFormatException e) {
			mColor = Color.WHITE;
		}
	}

	@Override
    protected void onDialogClosed(boolean positiveResult) {
    	super.onDialogClosed(positiveResult);
        if (positiveResult) {
            Integer value = new Integer(mPickerView.getColor());
            if (callChangeListener(value)) {
                storeColor(value);
            }
        }
    }
   
    
    /**
     * Saves the color to the {@link SharedPreferences}.
     * 
     * @param color Color to save
     */
    public void storeColor(int color) {
    	mColor = color;
        final boolean wasBlocking = shouldDisableDependents();
		// Cause some Preferences can't work with numeric values => do it with strings
        persistString(Integer.toString(color));
        final boolean isBlocking = shouldDisableDependents(); 
        if (isBlocking != wasBlocking) {
            notifyDependencyChange(isBlocking);
        }

    }

}
