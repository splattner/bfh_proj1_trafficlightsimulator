package ch.bfh.proj1.trafficlightsimulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;

public class Lane implements DrawableObject {
	
	public enum laneOrientations {
		startToEnd,
		endToStart
	}

	private Dimension dimension;

	private laneOrientations laneOrientation;
	
	private Point origin;
	
	private LinkedList<Vehicle> verhiclesOnLane;
	
	private TrafficLight trafficLight;
	
	private Street street;
	
	private static final Color laneColor = Color.GRAY;
	
	
	public Lane(laneOrientations laneOrientation) {
		this.setVerhiclesOnLane(new LinkedList<Vehicle>());
		this.setLaneOrientation(laneOrientation);
		this.trafficLight = new TrafficLight(this);
		this.laneOrientation = laneOrientation;
	}

	public laneOrientations getLaneOrientation() {return laneOrientation;}

	public void setLaneOrientation(laneOrientations laneOrientation) {
		this.laneOrientation = laneOrientation;}
	
	public TrafficLight getTrafficLight () {return trafficLight;}

	@Override
	public void paintObject(Graphics g) {
		
		g.setColor(Street.streetColor);
		g.fillRect(origin.x, origin.y, dimension.width, dimension.height);
		
		//TODO: Arrow
		
		
		if (dimension.width > dimension.height) {
			// Vertical			
			
			if (this.getLaneOrientation() == laneOrientations.startToEnd){
				trafficLight.setOrigin(new Point (origin.x+dimension.width-10, origin.y));
				trafficLight.setDimension(new Dimension (10,TrafficLightSimulator.defaultLaneWidth));
			}
			
			else {
				trafficLight.setOrigin(new Point (origin.x, origin.y));
				trafficLight.setDimension(new Dimension (10,TrafficLightSimulator.defaultLaneWidth));
									
			}
			
			
			trafficLight.paintObject(g);
						
		}
		
		else {
			
			//TODO:Horizontal line

		
//		if (this.getLaneOrientation() == laneOrientations.startToEnd){
//			trafficLight.setOrigin(new Point (origin.x+dimension.width-10, origin.y));
//			trafficLight.setDimension(new Dimension (10,TrafficLightSimulator.defaultLaneWidth));
//			trafficLight.paintObject(g);	
//		}
//		
//		else {
//			trafficLight.setOrigin(new Point (origin.x, origin.y));
//			trafficLight.setDimension(new Dimension (10,TrafficLightSimulator.defaultLaneWidth));
//			trafficLight.paintObject(g);					
//			}			
		}
		

	}
	
	@Override
	public void setOrigin (Point aPoint) {origin = aPoint;}
	
	@Override
	public Point getOrigin() {return origin;}

	@Override
	public void setDimension(Dimension dimension) {this.dimension = dimension;}

	@Override
	public Dimension getDimension() {return dimension;}

	public LinkedList<Vehicle> getVerhiclesOnLane() {
		return verhiclesOnLane;
	}

	public void setVerhiclesOnLane(LinkedList<Vehicle> verhiclesOnLane) {
		this.verhiclesOnLane = verhiclesOnLane;
	}

	public Street getStreet() {
		return street;
	}

	public void setStreet(Street street) {
		this.street = street;
	}

}
