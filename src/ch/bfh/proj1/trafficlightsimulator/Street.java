package ch.bfh.proj1.trafficlightsimulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Collection;
import java.util.LinkedList;

public class Street implements DrawableObject{
	
	private int length;
	private Collection<Lane> lanes;
	
	private Junction startJunction;
	private Junction endJunction;
	
	private Point origin;
	private Dimension dimension;
	
	public static final Color laneColor = Color.YELLOW;
	public static final Color streetColor = Color.GRAY;
	
	public Street() {
		this.lanes = new LinkedList<Lane>();
		this.length = TrafficLightSimulator.defaultStreetLenght;
	}

	public Street(int length) {
		this.lanes = new LinkedList<Lane>();
		this.length = length;
	}
	
	@Override
	public void paintObject(Graphics g) {
		
		// get number of lanes
		int numOfLanes = lanes.size();
				
		// Paint the lanes and give origin point to the latter
		int i=0;
		
		// ask the lanes to draw themselves
		for (Lane l : lanes) {
			
			if(l.getOrigin() == null) // if the origins have been calculated already, skip and paint
			{
			
				if (dimension.width > dimension.height) {
					// vertical street
					l.setOrigin(new Point (origin.x, origin.y+TrafficLightSimulator.defaultLaneWidth*i));
					l.setDimension(new Dimension (TrafficLightSimulator.defaultStreetLenght, TrafficLightSimulator.defaultLaneWidth));
				} else {
					// horizontal
					l.setOrigin(new Point (origin.x +TrafficLightSimulator.defaultLaneWidth*i, origin.y));
					l.setDimension(new Dimension (TrafficLightSimulator.defaultLaneWidth, TrafficLightSimulator.defaultStreetLenght));
				}
				i++;
			}
			l.paintObject(g);
			// TODO : somehow indicate the direction of the lane (needed for trafficlight later) 
		}
		
		// draws the separation line (that belongs to the street)
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
	
	public void addLane(Lane lane) {lanes.add(lane);}

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

}
