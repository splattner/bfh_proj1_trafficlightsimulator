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

public class TrafficLight implements DrawableObject, Comparable<TrafficLight> {
	
	private Point origin;
	private Dimension dimension;
	private Lane lane;
	private TrafficLightStatus currentStatus = TrafficLightStatus.RED;
	
	/**
	 * Number of Simulation steps since we changed this to green
	 */
	private int timeLastChange;
	
	private enum TrafficLightStatus {
	    RED,
	    ORANGE,
	    GREEN
	}

	public TrafficLight (Lane aLane) {
		this.lane = aLane;
	}
	
	public void paintObject(Graphics g) {		
		if(this.isRed()) {
			g.setColor(Color.RED);
		} else if(this.isGreen()) {
			
			/*if (this.getTimeLastChange() > TrafficLightSimulator.minimumGreenLightPhase * 0.8) {
				g.setColor(Color.ORANGE);
			} else {*/
				g.setColor(Color.GREEN);
			/*}*/
			
		} else if (this.isOrange()) {
			g.setColor(Color.ORANGE);
		}
		g.fillRect(origin.x , origin.y, dimension.width, dimension.height);
		
		
	}


	public Point getOrigin() {return origin;}
	public void setOrigin(Point origin) {this.origin = origin;}


	public void setDimension(Dimension dimension) {this.dimension = dimension;}
	public Dimension getDimension() {return dimension;}
	

	public int getTimeLastChange() { return timeLastChange;	}
	
	/**
	 * Get the number of vehicles that are close to this traffic light
	 * @return numberOfVehiclesNearLight
	 */
	public int getNumOfVehiclesNearLight() {
		int numberOfVehiclesNearLight = 0;
		for (Vehicle v : this.lane.getVerhiclesOnLane()) {
			// All vehicles closer as 50% of Street Lenght
			if (v.getCurrentPosOnLane() > this.lane.getStreet().getPositionsOnStreet() * 0.50) {
				numberOfVehiclesNearLight++;
			}
		}
		
		return numberOfVehiclesNearLight;
	}
	
	public boolean isRed() {
		return this.currentStatus == TrafficLightStatus.RED;
	}
	
	public boolean isOrange() {
		return this.currentStatus == TrafficLightStatus.ORANGE;
	}
	
	public boolean isGreen() {
		return this.currentStatus == TrafficLightStatus.GREEN;
	}
	
	public void setRed() {
		if (isRed()) {
			this.timeLastChange++;
		} else {
			this.currentStatus = TrafficLightStatus.RED;
			this.timeLastChange = 0;
		}
	}
	
	public void setGreen() {
		if (isGreen()) {
			this.timeLastChange++;
		} else {
			this.currentStatus = TrafficLightStatus.GREEN;
			this.timeLastChange = 0;
		}
	}

	@Override
	public int compareTo(TrafficLight o) {
		if (this.getNumOfVehiclesNearLight() > o.getNumOfVehiclesNearLight()) {
			return -1;
		} else if (this.getNumOfVehiclesNearLight() == o.getNumOfVehiclesNearLight()) {
			return 0;
		} else {
			return 1;
		}

	}

}
