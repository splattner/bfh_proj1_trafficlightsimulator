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
import java.util.Collection;
import java.util.LinkedList;

public class Street implements DrawableObject{
	
	private int length;
	private int positionsOnStreet;
	private Collection<Lane> lanes;
	
	private Junction startJunction;
	private Junction endJunction;
	
	private Point origin;
	private Dimension dimension;
	
	private int id;
	
	/**
	 * Possible orientation of the street
	 */
	public enum orientation {
		horizontal,
		vertical
	}
	
	/**
	 * Current orientation for the Street
	 */
	private orientation orientaion;
	
	public static final Color laneColor = Color.WHITE;
	public static final Color streetColor = Color.GRAY;
	
	public Street(int id) {
		this.lanes = new LinkedList<Lane>();
		this.length = TrafficLightSimulator.defaultStreetLenght;
		this.positionsOnStreet = TrafficLightSimulator.defaultPositinOnStreet;
		this.id=id;
	}


	@Override
	public void paintObject(Graphics g) {
		
		int numOfLanes = lanes.size();
				
		// Paint the lanes and give origin point to the latter
		int i=0;
		
		// Ask the lanes to draw themselves
		for (Lane l : lanes) {
			
			if(l.getOrigin() == null) // calculate origins and dimension the first time only
			{
				if (this.getOrientaion() == orientation.horizontal) {
					// Horizontal street
					l.setOrigin(new Point (origin.x, origin.y+TrafficLightSimulator.defaultLaneWidth*i));
					l.setDimension(new Dimension (TrafficLightSimulator.defaultStreetLenght, TrafficLightSimulator.defaultLaneWidth));
				} else {
					// Vertical Street
					l.setOrigin(new Point (origin.x +TrafficLightSimulator.defaultLaneWidth*i, origin.y));
					l.setDimension(new Dimension (TrafficLightSimulator.defaultLaneWidth, TrafficLightSimulator.defaultStreetLenght));
				}
				
				i++;
			}
			
			// Draw Lane
			l.paintObject(g);

		}
		
		// Draw the separation line
		if (numOfLanes > 1) {
			for (int j = 1 ; j < numOfLanes ; j++) {
				if (this.getOrientaion() == orientation.horizontal) {
					g.setColor(laneColor);
					g.drawLine(origin.x, origin.y + (TrafficLightSimulator.defaultLaneWidth * j), origin.x + dimension.width, origin.y + (TrafficLightSimulator.defaultLaneWidth * j));
					
				} else {
					g.setColor(laneColor);
					g.drawLine(origin.x + (TrafficLightSimulator.defaultLaneWidth * j), origin.y , origin.x + (TrafficLightSimulator.defaultLaneWidth * j), origin.y + dimension.height);

				}
			}
		}
	}

	/**
	 * Add a lane to the Street
	 * and set Street for the lane
	 * @param lane The lane to add
	 */
	public void addLane(Lane lane) {
		lanes.add(lane);
		lane.setStreet(this);
	}

	public Junction getStartJunction() {return startJunction;}
	public void setStartJunction(Junction startJunction) {this.startJunction = startJunction;}

	public Junction getEndJunction() {return endJunction;}
	public void setEndJunction(Junction endJunction) {this.endJunction = endJunction;}

	public Point getOrigin() {return this.origin;}
	
	/**
	 * Set Origin
	 * If there is already a origin, this is a movement of street 
	 * So you have to move also the origin of the lanes
	 */
	public void setOrigin(Point origin) {
		
		// Get difference to old origin for lane movement
		if (this.origin != null) {
			
			int move_x = origin.x - this.origin.x;
			int move_y = origin.y - this.origin.y;
			
			// Move all lanes
			for (Lane l : this.getLanes()) {
				l.setOrigin(new Point(l.getOrigin().x + move_x, l.getOrigin().y + move_y));
				
			}
		}
		
		this.origin = origin;
		
	}

	public int getLenght() {return length;}
	public void setLenght(int lenght) {this.length = lenght;}

	public Dimension getDimension() {return this.dimension;}
	public void setDimension(Dimension dimension) {this.dimension = dimension;}
	
	public Collection<Lane> getLanes() {return this.lanes;}

	public orientation getOrientaion() {return orientaion;}
	public void setOrientaion(orientation orientaion) {this.orientaion = orientaion;}

	public int getPositionsOnStreet() {return positionsOnStreet;}
	public void setPositionsOnStreet(int positionsOnStreet) {this.positionsOnStreet = positionsOnStreet;}
	
	public int getId() {return id;}
	
	public boolean isHorizontal() { return this.orientaion == orientaion.horizontal; }
	public boolean isVertical() { return this.orientaion == orientaion.vertical; }

}
