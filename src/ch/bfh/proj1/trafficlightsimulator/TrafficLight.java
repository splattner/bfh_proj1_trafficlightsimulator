package ch.bfh.proj1.trafficlightsimulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public class TrafficLight implements DrawableObject {
	
	private Point origin;
	private Dimension dimension;
	private Lane lane;
	private trafficLightStatus currentStatus = trafficLightStatus.RED; // TODO : remove that later
	
	public enum trafficLightStatus {
	    RED,
	    ORANGE,
	    GREEN
	}

	public TrafficLight (Lane aLane) {
		this.lane = aLane;
		}
	
	@Override
	public void paintObject(Graphics g) {		
		if(currentStatus.name().equalsIgnoreCase("RED")) {g.setColor(Color.RED);}
		else if(currentStatus.name().equalsIgnoreCase("GREEN")) {g.setColor(Color.GREEN);}
		else if(currentStatus.name().equalsIgnoreCase("ORANGE")) {g.setColor(Color.ORANGE);}
		g.fillRect(origin.x , origin.y, dimension.width, dimension.height);
	}

	@Override
	public Point getOrigin() {return origin;}

	@Override
	public void setOrigin(Point origin) {this.origin = origin;}

	@Override
	public void setDimension(Dimension dimension) {this.dimension = dimension;}

	@Override
	public Dimension getDimension() {return dimension;}
	
	public String getCurrentStatus () {return currentStatus.toString();}

}
