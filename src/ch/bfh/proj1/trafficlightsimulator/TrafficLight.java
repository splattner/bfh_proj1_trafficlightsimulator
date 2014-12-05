/*
 * Copyright 2014
 * Sebastian Plattner, Donatello Gallucci
 * Bern University of applied Science
 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ch.bfh.proj1.trafficlightsimulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import ch.bfh.proj1.trafficlightsimulator.vehicles.Vehicle;

public class TrafficLight implements DrawableObject {
	
	private Point origin;
	private Dimension dimension;
	private Lane lane;
	private trafficLightStatus currentStatus = trafficLightStatus.RED;
	
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
	
	public void paintObject(Graphics g) {		
		if(currentStatus.name().equalsIgnoreCase("RED")) {
			g.setColor(Color.RED);
		} else if(currentStatus.name().equalsIgnoreCase("GREEN")) {
			
			/*if (this.getTimeLastChange() > TrafficLightSimulator.minimumGreenLightPhase * 0.8) {
				g.setColor(Color.ORANGE);
			} else {*/
				g.setColor(Color.GREEN);
			/*}*/
			
		} else if (currentStatus.name().equalsIgnoreCase("ORANGE")) {
			g.setColor(Color.ORANGE);
		}
		g.fillRect(origin.x , origin.y, dimension.width, dimension.height);
		
		
	}


	public Point getOrigin() {return origin;}
	public void setOrigin(Point origin) {this.origin = origin;}


	public void setDimension(Dimension dimension) {this.dimension = dimension;}
	public Dimension getDimension() {return dimension;}
	
	public trafficLightStatus getCurrentStatus () {return currentStatus;}
	public void setCurrentStatus(trafficLightStatus currentStatus) {this.currentStatus = currentStatus;}

	public Lane getLane() { return lane; }
	public void setLane(Lane lane) { this.lane = lane;	}

	public int getTimeLastChange() { return timeLastChange;	}
	public void setTimeLastChange(int timeLastChange) {	this.timeLastChange = timeLastChange;}
	
	/**
	 * Get the number of vehicles that are close to this traffic light
	 * @return numberOfVehiclesNearLight
	 */
	public int getNumOfVehiclesNearLight() {
		int numberOfVehiclesNearLight = 0;
		for (Vehicle v : this.getLane().getVerhiclesOnLane()) {
			// All vehicles closer as 35% of Street Lenghta
			if (v.getCurrentPosOnLane() > this.getLane().getStreet().getPositionsOnStreet() * 0.65) {
				numberOfVehiclesNearLight++;
			}
		}
		
		return numberOfVehiclesNearLight;
	}

}
