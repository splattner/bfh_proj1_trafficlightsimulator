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
	
	/*
	 * Number of Simulation steps since we changed this to green
	 */
	private int timeLastChange;
	
	public enum trafficLightStatus {
	    RED,
	    ORANGE,
	    GREEN
	}

	public TrafficLight (Lane aLane) {
		this.setLane(aLane);
	}
	
	@Override
	public void paintObject(Graphics g) {		
		if(currentStatus.name().equalsIgnoreCase("RED")) {g.setColor(Color.RED);}
		else if(currentStatus.name().equalsIgnoreCase("GREEN")) {g.setColor(Color.GREEN);}
		else if(currentStatus.name().equalsIgnoreCase("ORANGE")) {g.setColor(Color.ORANGE);}
		g.fillRect(origin.x , origin.y, dimension.width, dimension.height);
		
		
		/*
		// Just Debug
		g.setColor(Color.WHITE);
		g.drawString(Integer.toString(timeLastChange) , this.origin.x, this.origin.y);
		*/
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

	public Lane getLane() {
		return lane;
	}

	public void setLane(Lane lane) {
		this.lane = lane;
	}

	public int getTimeLastChange() {
		return timeLastChange;
	}

	public void setTimeLastChange(int timeLastChange) {
		this.timeLastChange = timeLastChange;
	}
	
	/**
	 * Get the number of vehicles that are close to this traffic light
	 * @return
	 */
	public int getNumOfVehiclesNearLight() {
		int numberOfVehiclesNearLight = 0;
		for (Vehicle v : this.getLane().getVerhiclesOnLane()) {
			// All vehicles closer as 35% of Street Lenght

			if (v.currentPosOnLane > this.getLane().getStreet().getPositionsOnStreet() * 0.65) {
				numberOfVehiclesNearLight++;
			}
		}
		
		return numberOfVehiclesNearLight;
	}

}
