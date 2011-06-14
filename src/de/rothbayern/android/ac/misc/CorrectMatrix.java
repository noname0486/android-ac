package de.rothbayern.android.ac.misc;

import android.graphics.*;



/**
 * @author dieter
 *	Cause there seems to be a bug (mapPoints in Samsung 9100) in class Matrix, 
 *  I have to write a better one
 *  
 */
public class CorrectMatrix  {
	Matrix m = new Matrix();

	public void postTranslate(float f, float g) {
		m.postTranslate(f, g);
	}

	public void postScale(float f, float g) {
		m.postScale(f,g);
		
	}
	
	
	public void mapPoints(PointF dst, PointF src) {
		float values[] = new float[9];
		m.getValues(values);
		dst.x = src.x*(values[0]+values[1])+values[2];
		dst.y = src.y*(values[3]+values[4])+values[5];
	}


	
	
}
