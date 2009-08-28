package de.rothbayern.android.ac.geometry;

import android.graphics.*;

public interface Figure {
	boolean contains(float x, float y);
	void draw(Canvas c, Paint p);
	float getMiddleX();
	float getMiddleY();
}
