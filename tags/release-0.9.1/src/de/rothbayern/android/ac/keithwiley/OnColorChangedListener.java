package de.rothbayern.android.ac.keithwiley;

/**
 * Callback to the creator of the dialog, informing the creator of a new color and notifying that the dialog is about to dismiss.
 */
public interface OnColorChangedListener {
	void colorChanged(int color);
}