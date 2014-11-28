package ch.bfh.proj1.trafficlightsimulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;

/**
 * @author sebastianplattner
 *
 */
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
	
	private Arrow arrow = new Arrow();
	
	private Street street;
		
	public Lane(int id, laneOrientations laneOrientation) {
		this.setVerhiclesOnLane(new LinkedList<Vehicle>());
		this.setLaneOrientation(laneOrientation);
		this.id=id;
		
		this.trafficLight = new TrafficLight(this);

	}

	public laneOrientations getLaneOrientation() {return laneOrientation;}

	public void setLaneOrientation(laneOrientations laneOrientation) {
		this.laneOrientation = laneOrientation;}
	
	public TrafficLight getTrafficLight () {return trafficLight;}

	@Override
	public void paintObject(Graphics g) {
				
		g.setColor(Street.streetColor);
		g.fillRect(origin.x, origin.y, dimension.width, dimension.height);		
		
		// on horizontal lane
		if (this.getStreet().getOrientaion() == Street.orientation.horizontal) {			
			if (this.getLaneOrientation() == laneOrientations.startToEnd){

				if (this.trafficLight != null && this.trafficLight.getOrigin() == null) {
					trafficLight.setOrigin(new Point (origin.x+dimension.width-10, origin.y));
					trafficLight.setDimension(new Dimension (10,TrafficLightSimulator.defaultLaneWidth));
				}

				if (this.arrow.getOrigin() == null) {
					arrow.setOrigin(new Point (origin.x+dimension.width/2, origin.y+dimension.height/2));
					arrow.setDimension(new Dimension (dimension.width/6,dimension.height/6));

					arrow.setXPoints(
							arrow.getOrigin().x+arrow.getDimension().width/2,
							arrow.getOrigin().x+arrow.getDimension().width/2,
							arrow.getOrigin().x+arrow.getDimension().width/2+10);
					arrow.setYPoints(
							arrow.getOrigin().y-5,
							arrow.getOrigin().y+5,
							arrow.getOrigin().y+arrow.getDimension().height/2);
				}
			}

			else {
				if (this.trafficLight != null && this.trafficLight.getOrigin() == null) {
					trafficLight.setOrigin(new Point (origin.x, origin.y));
					trafficLight.setDimension(new Dimension (10,TrafficLightSimulator.defaultLaneWidth));
				}

				if (this.arrow.getOrigin() == null) {
					arrow.setOrigin(new Point (origin.x+dimension.width/2, origin.y+dimension.height/2));
					arrow.setDimension(new Dimension (dimension.width/6,dimension.height/6));

					arrow.setXPoints(
							arrow.getOrigin().x-arrow.getDimension().width/2,
							arrow.getOrigin().x-arrow.getDimension().width/2,
							arrow.getOrigin().x-arrow.getDimension().width/2-10);
					arrow.setYPoints(
							arrow.getOrigin().y-5,
							arrow.getOrigin().y+5,
							arrow.getOrigin().y+arrow.getDimension().height/2);
				}
			}

		}

		// on vertical lane
		else {			
			if (this.getLaneOrientation() == laneOrientations.endToStart){

				if (this.trafficLight != null && this.trafficLight.getOrigin() == null) {
					trafficLight.setOrigin(new Point (origin.x, origin.y+dimension.height-10));
					trafficLight.setDimension(new Dimension (TrafficLightSimulator.defaultLaneWidth,10));
				}

				if (this.arrow.getOrigin() == null) {
					arrow.setOrigin(new Point (origin.x+dimension.width/2, origin.y+dimension.height/2));
					arrow.setDimension(new Dimension (dimension.width/6,dimension.height/6));

					arrow.setXPoints(	
							arrow.getOrigin().x-5,
							arrow.getOrigin().x+5,
							arrow.getOrigin().x+arrow.getDimension().width/2);

					arrow.setYPoints(
							arrow.getOrigin().y+arrow.getDimension().height/2,
							arrow.getOrigin().y+arrow.getDimension().height/2,
							arrow.getOrigin().y+arrow.getDimension().height/2+10);
				}

			}

			else {
				if (this.trafficLight != null && this.trafficLight.getOrigin() == null) {
					trafficLight.setOrigin(new Point (origin.x, origin.y));
					trafficLight.setDimension(new Dimension (TrafficLightSimulator.defaultLaneWidth,10));
				}

				if (this.arrow.getOrigin() == null) {
					arrow.setOrigin(new Point (origin.x+dimension.width/2, origin.y+dimension.height/2));
					arrow.setDimension(new Dimension (dimension.width/6,dimension.height/6));

					arrow.setXPoints(	
							arrow.getOrigin().x-5,
							arrow.getOrigin().x+5,
							arrow.getOrigin().x+arrow.getDimension().width/2);

					arrow.setYPoints(
							arrow.getOrigin().y-arrow.getDimension().height/2,
							arrow.getOrigin().y-arrow.getDimension().height/2,
							arrow.getOrigin().y-arrow.getDimension().height/2-10);
				}
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
		if (this.trafficLight != null) {trafficLight.paintObject(g);}
		
		// Draw arrow
		arrow.paintObject(g);
	}
	
	
	@Override
	public void setOrigin (Point aPoint) {origin = aPoint;}
	
	@Override
	public Point getOrigin() {return origin;}

	@Override
	public void setDimension(Dimension dimension) {this.dimension = dimension;}

	@Override
	public Dimension getDimension() {return dimension;}

	public LinkedList<Vehicle> getVerhiclesOnLane() {return verhiclesOnLane;}

	public void setVerhiclesOnLane(LinkedList<Vehicle> verhiclesOnLane) {this.verhiclesOnLane = verhiclesOnLane;}

	public Street getStreet() {return street;}

	public void setStreet(Street street) {
		this.street = street;
	}
	
	public int getId() {return id;}
	
	private class Arrow implements DrawableObject {
		
		private Dimension dimension;

		private Point origin;
		
		private int[] triangleXPoints = new int[3];
		
		private int[] triangleYPoints = new int[3];
		

		@Override
		public void paintObject(Graphics g) {
			g.setColor(Color.WHITE);
			g.fillRect(origin.x-dimension.width/2, origin.y-dimension.height/2, dimension.width, dimension.height);
			g.fillPolygon(triangleXPoints, triangleYPoints, 3);
		}
		
		public void setXPoints (int first, int second, int third)
		{
			triangleXPoints[0] = first;
			triangleXPoints[1] = second;
			triangleXPoints[2] = third;
		}
		
		public void setYPoints (int first, int second, int third)
		{
			triangleYPoints[0] = first;
			triangleYPoints[1] = second;
			triangleYPoints[2] = third;		
		}

		@Override
		public void setOrigin (Point aPoint) {origin = aPoint;}
		
		@Override
		public Point getOrigin() {return origin;}

		@Override
		public void setDimension(Dimension dimension) {this.dimension = dimension;}

		@Override
		public Dimension getDimension() {return dimension;}
		
	}

}
