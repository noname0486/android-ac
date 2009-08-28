package de.rothbayern.android.ac.drawings;


import android.graphics.Bitmap;
import de.rothbayern.android.ac.pref.CompassPreferences;

public abstract class CompassDrawing {
	static final float MAX = 150;
	public abstract Bitmap getDrawing(int width, int height);
	public void setColorPreference(DrawingComponent c, int color) {
		CompassPreferences prefs = CompassPreferences.getPreferences();
		String prefName = toPrefName(c);
		prefs.setInt(prefName, color);
	}

	public int getColorPreference(DrawingComponent c) {
		CompassPreferences prefs = CompassPreferences.getPreferences();
		String prefName = toPrefName(c);
		int prColor = prefs.getInt(prefName,-1);
		if(prColor==-1){
			prColor = c.getDefaultColor();
		}
		return(prColor);
	}
	
	public DrawingComponent searchByName(String name){
		DrawingComponent dcs[] = getComponents();
		for (int i = 0; i < dcs.length; i++) {
			if(dcs[i].getName().equals(name)){
				return(dcs[i]);
			}
		}
		return(null);
	}

	public String toPrefName(DrawingComponent c) {
		StringBuilder res = new StringBuilder(30);
		res.append(getPrefNamePrefix());
		res.append("_");
		res.append(c.getName());
		return (res.toString());
	}

	public abstract String getPrefNamePrefix();
	public abstract DrawingComponent[] getComponents();
	
}
