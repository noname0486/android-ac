package de.rothbayern.android.ac.drawings;

import de.rothbayern.android.ac.geometry.Figure;

public class DrawingComponent {
	private int pos;
	private String name;
	private Figure f;
	private int defaultColor;
	private String title;
	
	
	
	public DrawingComponent(int pos, String name, Figure f, int defaultColor, String title) {
		super();
		this.pos = pos;
		this.name = name;
		this.f = f;
		this.defaultColor = defaultColor;
		this.title = title;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Figure getF() {
		return f;
	}

	public void setF(Figure f) {
		this.f = f;
	}
	
	public int getDefaultColor() {
		return defaultColor;
	}
	
	public void setDefaultColor(int defaultColor) {
		this.defaultColor = defaultColor;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	
}
