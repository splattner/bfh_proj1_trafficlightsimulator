package ch.bfh.proj1.trafficlightsimulator;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public interface DrawableObject {
	
	public void paintObject(Graphics g);
	
	public Point getOrigin();
	public void setOrigin(Point origin);
	
	public void setDimension(Dimension dimension);
	public Dimension getDimension();

}
