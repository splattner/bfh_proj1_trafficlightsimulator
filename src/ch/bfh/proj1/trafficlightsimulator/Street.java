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
	
	public enum orientation {
		horizontal,
		vertical
	}
	
	private orientation orientaion;
	
	public static final Color laneColor = Color.YELLOW;
	public static final Color streetColor = Color.GRAY;
	
	public Street() {
		this.lanes = new LinkedList<Lane>();
		this.length = TrafficLightSimulator.defaultStreetLenght;
		this.positionsOnStreet = TrafficLightSimulator.defaultPositinOnStreet;
	}

	public Street(int length) {
		this.lanes = new LinkedList<Lane>();
		this.positionsOnStreet = TrafficLightSimulator.defaultPositinOnStreet;
		this.length = length;
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
				if (dimension.width > dimension.height) {
					// Vertical street
					l.setOrigin(new Point (origin.x, origin.y+TrafficLightSimulator.defaultLaneWidth*i));
					l.setDimension(new Dimension (TrafficLightSimulator.defaultStreetLenght, TrafficLightSimulator.defaultLaneWidth));
				} else {
					// Horizontal
					l.setOrigin(new Point (origin.x +TrafficLightSimulator.defaultLaneWidth*i, origin.y));
					l.setDimension(new Dimension (TrafficLightSimulator.defaultLaneWidth, TrafficLightSimulator.defaultStreetLenght));
				}
				
				i++;
			}
			
			l.paintObject(g);

		}
		
		// Draw the separation line
		if (numOfLanes > 1) {
			for (int j = 1 ; j < numOfLanes ; j++) {
				if (dimension.width > dimension.height) {
					g.setColor(laneColor);
					g.drawLine(origin.x, origin.y + (TrafficLightSimulator.defaultLaneWidth * j), origin.x + dimension.width, origin.y + (TrafficLightSimulator.defaultLaneWidth * j));
					
				} else {
					g.setColor(laneColor);
					g.drawLine(origin.x + (TrafficLightSimulator.defaultLaneWidth * j), origin.y , origin.x + (TrafficLightSimulator.defaultLaneWidth * j), origin.y + dimension.height);

				}
			}
		}
	}

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

	public orientation getOrientaion() {
		return orientaion;
	}

	public void setOrientaion(orientation orientaion) {
		this.orientaion = orientaion;
	}

	public int getPositionsOnStreet() {
		return positionsOnStreet;
	}

	public void setPositionsOnStreet(int positionsOnStreet) {
		this.positionsOnStreet = positionsOnStreet;
	}

}
