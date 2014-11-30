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
	
	/*
	 * Possible orientation of the street
	 */
	public enum orientation {
		horizontal,
		vertical
	}
	
	/*
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

	@Override
	public void setOrigin(Point origin) {this.origin = origin;}

	public int getLenght() {return length;}

	public void setLenght(int lenght) {this.length = lenght;}

	@Override
	public void setDimension(Dimension dimension) {this.dimension = dimension;}
	
	public Collection<Lane> getLanes() {return this.lanes;}

	@Override
	public Point getOrigin() {return this.origin;}

	@Override
	public Dimension getDimension() {return this.dimension;}

	public orientation getOrientaion() {return orientaion;}

	public void setOrientaion(orientation orientaion) {this.orientaion = orientaion;}

	public int getPositionsOnStreet() {return positionsOnStreet;}

	public void setPositionsOnStreet(int positionsOnStreet) {this.positionsOnStreet = positionsOnStreet;}
	
	public int getId() {return id;}

}
