package ch.bfh.proj1.trafficlightsimulator;

import java.awt.Graphics;
import java.awt.Point;

public interface DrawableObject {
	
	public void paintObject(Graphics g);
	
	public void setOrigin(Point origin);

}
