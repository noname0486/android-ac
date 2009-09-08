package de.rothbayern.android.ac.keithwiley;



import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.Toast;

/**
 * UberColorPickerDialog is a seriously enhanced version of the UberColorPickerDialog class provided in the Android API Demos.
 * Improvements include:
 * <ul>
 * 		<li> Multiple color spaces and chooser methods (dimension combinations) for manipulating those color spaces, including:
 * 		<ul>
 * 			<li> HSV with angular H and radial S combined in 2D and a 1D V slider.
 * 			<li> HSV with angular H and radial V combined in 2D and a 1D S slider (this one's kinda silly).
 * 			<li> HSV with cardinal S and V combined in 2D and a 1D H slider.
 * 			<li> YUV with cardinal U and V combined in 2D and a 1D Y slider.
 * 			<li> RGB with three 1D sliders.
 * 			<li> HSV with three 1D sliders.
 *		</ul>
 *		<li> Simple switch-based compile-time configuration of which chooser methods are provided (search for ENABLED_METHODS near the top of the code).
 * 		<li> Numerical feedback of precise color values.
 * 		<li> Two sample swatches, one the original which can be used to revert to the initial color, the other to show the currently chosen color.
 * 		<li> Trackball input for precise color control.
 * 		<li> Automatic detection of portrait/landscape orientation and adjustment of the widget layout to make best use of that orientation.
 * 		<li> The option of showing or hiding the window title.  Showing it wastes a lot of space of course, hiding it is augmented with an introductory toast message.
 *		<li> Realtime feedback of color changes, not only in the sample swatch but also in all relevant palettes and sliders.
 *		<li> Hilighted borders to show which widget has trackball focus.
 *		<li> Position markers on the palettes and sliders to show the current value in each dimension (the value of each parameter).
 * </ul>
 * <p>
 * Version History:
 * <ul>
 * 		<li>v1.1, 090408
 * 		<ul>
 * 			<li>Added hex numerical output (HTML colors).
 * 			<li>All colorspace parameters (HSV, RGB, YUV, Hex) are now updated and shown at all times.
 * 			<li>Converted to GradientDrawable Bitmaps for drawing the 1D sliders.  They're much smoother, less blocky.  Note that 2D palettes are still constructed from the less smooth gradients.
 * 			<li>Did some general refactoring.
 * 			<li>Made the UV palette slightly more color-accurate (a little ligher and darker at extreme Y values).
 * 			<li>Added a "hilighted" border around the currently selected color chooser method.
 * 		</ul>
 * 		<li>v1.0, 090405
 * 		<ul>
 * 			<li>First public release
 * 		</ul>
 * </ul>
 * 
 * @author Keith Wiley, kwiley@keithwiley.com, http://keithwiley.com
 */
public class UberColorPickerDialog extends Dialog {
	private OnColorChangedListener mListener;
	private int mInitialColor;
	private boolean mShowTitle;

	/**
	 * Ctor
	 * @param context
	 * @param listener
	 * @param initialColor
	 * @param showTitle If true, a title is shown across the top of the dialog.  If false a toast is shown instead.
	 */
	public UberColorPickerDialog(Context context,
							OnColorChangedListener listener,
							int initialColor,
							boolean showTitle) {
		super(context);
		
		mListener = listener;
		mInitialColor = initialColor;
		mShowTitle = showTitle;
	}
	
	/**
	 * Activity entry point
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		OnColorChangedListener l = new OnColorChangedListener() {
			public void colorChanged(int color) {
				mListener.colorChanged(color);
				//dismiss(); Don't need to close
			}
		};
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		int screenHeight = dm.heightPixels;
		
		if (!mShowTitle) {
			getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			Toast.makeText(getContext(), "Pick a color (try the trackball)", Toast.LENGTH_SHORT).show();
		}
		else setTitle("Pick a color (try the trackball)");
		
		try {
			setContentView(new ColorPickerView(getContext(), l, screenWidth, screenHeight, mInitialColor));
		}
		catch (Exception e) {
			//There is currently only one kind of ctor exception, that where no methods are enabled.
			dismiss();	//This doesn't work!  The dialog is still shown (its title at least, the layout is empty from the exception being thrown).  <sigh>
		}
	}
	
	/**
	 * Android's Color.colorToHSV() has what I assume is a bug, such that on a desaturated color it sets H,S,V all to V.
	 * While ambiguous w.r.t. hue, saturation should certainly be 0 in such a case.  Detect and fix.
	 * @param color 4-byte ARGB
	 * @return true if fully desaturated, indicating that if this color was passed to Color.colorToHSV(), then the resulting HSV's S should be explicitly set to 0
	 */
	static public boolean isGray(int color) {
		return (((color >> 16) & 0x00000000FF) == (color & 0x000000FF)
			&& ((color >> 8) & 0x00000000FF) == (color & 0x000000FF));
	}

	/**
	 * Android's Color.colorToHSV() has what I assume is a bug, such that on a desaturated color it sets H,S,V all to V.
	 * While ambiguous w.r.t. hue, saturation should certainly be 0 in such a case.  Detect and fix.
	 * @param color 4-elm rgb of indeterminate range
	 * @return true if fully desaturated, indicating that if this color was passed to Color.colorToHSV(), then the resulting HSV's S should be explicitly set to 0
	 */
	static public boolean isGray(int[] rgb) {
		return (rgb[1] == rgb[0] && rgb[2] == rgb[0]);
	}
}

