package ch.bfh.proj1.trafficlightsimulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public class TrafficLight implements DrawableObject {
	
	private Point origin;
	private Dimension dimension;
	private Lane lane;
	private trafficLightStatus currentStatus;
	
	public enum trafficLightStatus {
	    RED,
	    ORANGE,
	    GREEN
	}

	public TrafficLight (Lane aLane) {
		this.lane = aLane;
		currentStatus = trafficLightStatus.RED;
		// TODO : size of the traficlight should be known during object instantiation
		// TODO : maybe red should not be in the constructor...
		}
	
	@Override
	public void paintObject(Graphics g) {		
		//TODO: draw the trafficLight based on the direction of the lane (changes way to calculate coordinates)
		//TODO: dynamic color selection
		if(currentStatus.name().equalsIgnoreCase("RED")) {g.setColor(Color.RED);}
		else if(currentStatus.name().equalsIgnoreCase("GREEN")) {g.setColor(Color.GREEN);}
		else if(currentStatus.name().equalsIgnoreCase("ORANGE")) {g.setColor(Color.ORANGE);}
		else{}
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
	
	public trafficLightStatus getCurrentStatus () {return currentStatus;}

	public void setCurrentStatus(trafficLightStatus currentStatus) {
		this.currentStatus = currentStatus;
	}

}
