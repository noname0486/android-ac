package de.rothbayern.android.ac;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public interface IAnimCompass {

	public abstract void setDirection(float direction);

	public boolean doAnim(boolean forcePaint);

	public void loadPrefs();

	public void setBgColor(int bgColor);

	public void setCompassLayout(int compassLayout);

}