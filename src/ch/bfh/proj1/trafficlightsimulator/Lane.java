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
import java.util.LinkedList;

import ch.bfh.proj1.trafficlightsimulator.vehicles.Vehicle;


public class Lane implements DrawableObject {

	/**
	 * Orientation for this Lane
	 * From the streets start Junction to the streets end Junction
	 * ore vice versa
	 *
	 */
	public enum laneOrientations {
		startToEnd,
		endToStart
	}

	public enum arrow_head {
		up,
		down,
		right,
		left
	}

	public enum marker {
		none,
		green,
		red,
	}

	private int id;

	/**
	 * Current lane orientation
	 */
	private laneOrientations laneOrientation;

	private Dimension dimension;
	private Point origin;

	/**
	 * All vehicles on this lane
	 */
	private LinkedList<Vehicle> verhiclesOnLane;


	/**
	 * The traffic light for this lane
	 */
	private TrafficLight trafficLight;

	/**
	 * The arrow indicationdirection on the Lane
	 */
	private Arrow arrow;

	/**
	 * The street this lane is on
	 */
	private Street street;


	private marker currentMarker = marker.none;
	public marker getCurrentMarker() {return currentMarker; }
	public void setCurrentMarker(marker newMarker) { this.currentMarker = newMarker;}

	public Lane(int id, laneOrientations laneOrientation) {
		this.setVerhiclesOnLane(new LinkedList<Vehicle>());
		this.setLaneOrientation(laneOrientation);
		this.id=id;

		this.trafficLight = new TrafficLight(this);
		this.arrow = new Arrow();

	}

	public laneOrientations getLaneOrientation() {return laneOrientation;}
	public void setLaneOrientation(laneOrientations laneOrientation) {this.laneOrientation = laneOrientation;}

	/**
	 * Return the traffic light for this lane
	 * @return TafficLight
	 */
	public TrafficLight getTrafficLight () {return trafficLight;}

	@Override
	public void paintObject(Graphics g) {


		switch (this.currentMarker) {
		case none:
			g.setColor(Street.streetColor);
			break;
		case green:
			g.setColor(new Color(0, 200, 0));
			break;
		case red:
			g.setColor(new Color(200, 0, 0));
			break;
		}
		g.fillRect(this.origin.x, this.origin.y, this.dimension.width, this.dimension.height);		


		// Set Origin and Dimension for Arrow and Traffic Light
		if (this.arrow.getOrigin() == null) {
			arrow.setOrigin(new Point (origin.x+dimension.width/2, origin.y+dimension.height/2));
			arrow.setDimension(new Dimension (dimension.width/6,dimension.height/6));
		}

		// on horizontal lane
		if (this.getStreet().isHorizontal()) {			
			if (this.isLeftToRight()){

				if (this.trafficLight != null && this.trafficLight.getOrigin() == null) {
					trafficLight.setOrigin(new Point (origin.x+dimension.width-10, origin.y));
					trafficLight.setDimension(new Dimension (10,TrafficLightSimulator.defaultLaneWidth));
				}
				arrow.setHead(arrow_head.right);
			}
			else {
				if (this.trafficLight != null && this.trafficLight.getOrigin() == null) {
					trafficLight.setOrigin(new Point (origin.x, origin.y));
					trafficLight.setDimension(new Dimension (10,TrafficLightSimulator.defaultLaneWidth));
				}
				arrow.setHead(arrow_head.left);
			}

		}

		// on vertical lane
		else {			
			if (this.isTopToBottom()){

				if (this.trafficLight != null && this.trafficLight.getOrigin() == null) {
					trafficLight.setOrigin(new Point (origin.x, origin.y+dimension.height-10));
					trafficLight.setDimension(new Dimension (TrafficLightSimulator.defaultLaneWidth,10));
				}
				arrow.setHead(arrow_head.down);
			}
			else {
				if (this.trafficLight != null && this.trafficLight.getOrigin() == null) {
					trafficLight.setOrigin(new Point (origin.x, origin.y));
					trafficLight.setDimension(new Dimension (TrafficLightSimulator.defaultLaneWidth,10));
				}
				arrow.setHead(arrow_head.up);
			}
		}			

		// Remove traffic light if non is necessary, e.g. at a start/end without a junction
		if (this.getLaneOrientation() == laneOrientations.startToEnd && this.getStreet().getEndJunction() == null) {
			this.trafficLight = null;
		}

		if (this.getLaneOrientation() == laneOrientations.endToStart && this.getStreet().getStartJunction() == null) {
			this.trafficLight = null;
		}

		// Draw Traffic light if any
		if (this.trafficLight != null) { trafficLight.paintObject(g); }

		// Draw arrow
		this.arrow.paintObject(g);
	}


	@Override
	public void setOrigin (Point aPoint) {

		// Get difference to old origin for arrow movement
		if (this.origin != null) {
			int move_x = aPoint.x - origin.x;
			int move_y = aPoint.y - origin.y;

			// Move Traffic Light
			TrafficLight tf = this.getTrafficLight();
			if (tf != null) {
				tf.setOrigin(new Point(tf.getOrigin().x + move_x, tf.getOrigin().y + move_y));
			}

			// Move Arrow
			if (this.arrow != null) {
				this.arrow.setOrigin(new Point(this.arrow.getOrigin().x + move_x, this.arrow.getOrigin().y + move_y));
			}
		}

		origin = aPoint;
	}

	@Override
	public Point getOrigin() {return origin;}

	@Override
	public void setDimension(Dimension dimension) {this.dimension = dimension;}
	@Override
	public Dimension getDimension() {return dimension;}

	public LinkedList<Vehicle> getVerhiclesOnLane() {return verhiclesOnLane;}
	public void setVerhiclesOnLane(LinkedList<Vehicle> verhiclesOnLane) {this.verhiclesOnLane = verhiclesOnLane;}

	public Street getStreet() {return street;}
	public void setStreet(Street street) { this.street = street;}

	public int getId() {return id;}

	public boolean isLeftToRight() { return this.getStreet().isHorizontal() && this.laneOrientation == laneOrientations.startToEnd; }
	public boolean isRightToLeft() { return this.getStreet().isHorizontal() && this.laneOrientation == laneOrientations.endToStart; }

	public boolean isTopToBottom() { return this.getStreet().isVertical() &&this.laneOrientation == laneOrientations.startToEnd; }
	public boolean isBottomToTop() { return this.getStreet().isVertical() && this.laneOrientation == laneOrientations.endToStart; }

	
	/**
	 * Return the next Junction for a vehicle on this Lane
	 */
	public Junction getNextJunction() {
		Junction j = null;


		if (this.isLeftToRight()) {
			// Next Junction is on the End Side
			j = this.getStreet().getEndJunction();

		} 
		if (this.isRightToLeft()){
			// Next Junction is on the Start Side
			j = this.getStreet().getStartJunction();
		}
		
		if (this.isTopToBottom()) {
			// Next Junction is on the End Side
			j = this.getStreet().getEndJunction();
		}
		if (this.isBottomToTop()){
			// Next Junction is on the Start Side
			j = this.getStreet().getStartJunction();
		}



		return j;
	}



	private class Arrow implements DrawableObject {

		private Dimension dimension;
		private Point origin;

		private int[] triangleXPoints = new int[3];
		private int[] triangleYPoints = new int[3];


		@Override
		public void paintObject(Graphics g) {
			g.setColor(Color.WHITE);


			g.fillRect(this.origin.x-this.dimension.width/2, this.origin.y-this.dimension.height/2, this.dimension.width, this.dimension.height);
			g.fillPolygon(this.triangleXPoints, this.triangleYPoints, 3);
		}

		private void setXPoints (int first, int second, int third)
		{
			triangleXPoints[0] = first;
			triangleXPoints[1] = second;
			triangleXPoints[2] = third;
		}

		private void setYPoints (int first, int second, int third)
		{
			triangleYPoints[0] = first;
			triangleYPoints[1] = second;
			triangleYPoints[2] = third;		
		}

		@Override
		public void setOrigin (Point aPoint) {

			// Get difference to old origin for head movement
			if (this.origin != null) {
				int move_x = aPoint.x - this.origin.x;
				int move_y = aPoint.y - this.origin.y;

				// Move Head
				this.triangleXPoints[0] += move_x;
				this.triangleXPoints[1] += move_x;
				this.triangleXPoints[2] += move_x;
				this.triangleYPoints[0] += move_y;
				this.triangleYPoints[1] += move_y;
				this.triangleYPoints[2] += move_y;

			}

			origin = aPoint;

		}

		public void setHead(arrow_head head_direction) {

			switch (head_direction) {
			case right:
				this.setXPoints(
						this.getOrigin().x + this.getDimension().width/2,
						this.getOrigin().x + this.getDimension().width/2,
						this.getOrigin().x + this.getDimension().width/2+10);
				this.setYPoints(
						this.getOrigin().y - 5,
						this.getOrigin().y + 5,
						this.getOrigin().y + this.getDimension().height/2);
				break;
			case left:
				this.setXPoints(
						this.getOrigin().x-this.getDimension().width/2,
						this.getOrigin().x-this.getDimension().width/2,
						this.getOrigin().x-this.getDimension().width/2-10);
				this.setYPoints(
						this.getOrigin().y-5,
						this.getOrigin().y+5,
						this.getOrigin().y+this.getDimension().height/2);
				break;
			case down:
				this.setXPoints(	
						this.getOrigin().x-5,
						this.getOrigin().x+5,
						this.getOrigin().x+this.getDimension().width/2);

				this.setYPoints(
						this.getOrigin().y+this.getDimension().height/2,
						this.getOrigin().y+this.getDimension().height/2,
						this.getOrigin().y+this.getDimension().height/2+10);
				break;
			case up:
				this.setXPoints(	
						this.getOrigin().x-5,
						this.getOrigin().x+5,
						this.getOrigin().x+this.getDimension().width/2);

				this.setYPoints(
						this.getOrigin().y-this.getDimension().height/2,
						this.getOrigin().y-this.getDimension().height/2,
						this.getOrigin().y-this.getDimension().height/2-10);
				break;
			}

		}

		@Override
		public Point getOrigin() {return origin;}

		@Override
		public void setDimension(Dimension dimension) {this.dimension = dimension;}

		@Override
		public Dimension getDimension() {return dimension;}

	}

}
