package ch.bfh.proj1.trafficlightsimulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Collection;
import java.util.LinkedList;

public class Street implements DrawableObject{
	
	private int lenght;
	private Collection<Lane> lanes;
	
	private Junction startJunction;
	private Junction endJunction;
	
	private Point origin;
	private Dimension dimension;
	
	public Street() {
		this.lanes = new LinkedList<Lane>();
		this.lenght = TrafficLightSimulator.defaultStreetLenght;
	}

	public Street(int lenght) {
		this.lanes = new LinkedList<Lane>();
		this.lenght = lenght;
	}
	
	@Override
	public void paintObject(Graphics g) {
		// TODO Auto-generated method stub
		
		boolean drawComplete = false;
		
		Color streetColor = Color.GRAY;
		Color laneColor = Color.YELLOW;
		
		
		// Get Number of lanes
		int numOfLanes = lanes.size();
		

		
		g.setColor(streetColor);

		
		g.fillRect(origin.x , origin.y, dimension.width, dimension.height);
		
		g.setColor(laneColor);
		if (numOfLanes > 1) {
			for (int i = 1 ; i < numOfLanes ; i++) {
				if (dimension.width > dimension.height) {
					g.drawLine(origin.x, origin.y + (TrafficLightSimulator.defaultLaneWidth * i), origin.x + dimension.width, origin.y + (TrafficLightSimulator.defaultLaneWidth * i));
				} else {
					g.drawLine(origin.x + (TrafficLightSimulator.defaultLaneWidth * i), origin.y , origin.x + (TrafficLightSimulator.defaultLaneWidth * i), origin.y + dimension.height);
				}
				
				
			}
		}
		
		
		
	}
	
	public void addLane(Lane lane) {
		lanes.add(lane);
	}

	public Junction getStartJunction() {
		return startJunction;
	}

	public void setStartJunction(Junction startJunction) {
		this.startJunction = startJunction;
	}

	public Junction getEndJunction() {
		return endJunction;
	}

	public void setEndJunction(Junction endJunction) {
		this.endJunction = endJunction;
	}

	@Override
	public void setOrigin(Point origin) {
		this.origin = origin;
		
	}

	public int getLenght() {
		return lenght;
	}

	public void setLenght(int lenght) {
		this.lenght = lenght;
	}

	@Override
	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
		
	}
	
	public Collection<Lane> getLanes() {
		return this.lanes;
	}

	@Override
	public Point getOrigin() {
		return this.origin;
	}

	@Override
	public Dimension getDimension() {
		return this.dimension;
	}

}
