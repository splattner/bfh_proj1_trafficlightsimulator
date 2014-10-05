package ch.bfh.proj1.trafficlightsimulator;

import java.awt.Color;
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
		
		int streetHeight;
		int streetLenght;
		
		if (!drawComplete && startJunction != null && startJunction.getRightStreet().equals(this)) {
			
			System.out.println("Horizontal Street with a Start Junction");
			
			g.setColor(streetColor);
			
			// This Street is connected to right of the Junction
			streetHeight = numOfLanes * TrafficLightSimulator.defaultLaneWidth;
			streetLenght =  TrafficLightSimulator.defaultStreetLenght;
			
			g.fillRect(origin.x , origin.y, streetLenght, streetHeight);
			
			g.setColor(laneColor);
			if (numOfLanes > 1) {
				for (int i = 1 ; i < numOfLanes ; i++) {
					g.drawLine(origin.x, origin.y + (TrafficLightSimulator.defaultLaneWidth * i), origin.x + streetLenght, origin.y + (TrafficLightSimulator.defaultLaneWidth * i));
				}
			}
			
			drawComplete = true;
		}
		
		if (!drawComplete &&  endJunction != null && endJunction.getLeftStreet().equals(this)) {
			// This Street is connected to left of the Junction
			System.out.println("Horizontal Street without a Start Junction");
			
			g.setColor(streetColor);
			
			streetHeight = numOfLanes * TrafficLightSimulator.defaultLaneWidth;
			streetLenght =  TrafficLightSimulator.defaultStreetLenght;
			
			g.fillRect(origin.x , origin.y, streetLenght, streetHeight);
			
			g.setColor(laneColor);
			if (numOfLanes > 1) {
				for (int i = 1 ; i < numOfLanes ; i++) {
					g.drawLine(origin.x, origin.y + (TrafficLightSimulator.defaultLaneWidth * i), origin.x + streetLenght, origin.y + (TrafficLightSimulator.defaultLaneWidth * i));
				}
			}
			
			drawComplete = true;
			
		}
		
		if (!drawComplete &&  endJunction != null && endJunction.getTopStreet().equals(this)) {
			// This Street is connected to left of the Junction
			System.out.println("Vertical Street without a Start Junction");
			
			g.setColor(streetColor);
			
			streetHeight = TrafficLightSimulator.defaultStreetLenght;
			streetLenght =  numOfLanes * TrafficLightSimulator.defaultLaneWidth;
			
			g.fillRect(origin.x , origin.y, streetLenght, streetHeight);
			
			g.setColor(laneColor);
			if (numOfLanes > 1) {
				for (int i = 1 ; i < numOfLanes ; i++) {
					g.drawLine(origin.x + (TrafficLightSimulator.defaultLaneWidth * i), origin.y , origin.x + (TrafficLightSimulator.defaultLaneWidth * i), origin.y + streetHeight);
				}
			}
			
			drawComplete = true;
			
		}
		
		if (!drawComplete &&  startJunction != null && startJunction.getBottomStreet().equals(this)) {
			// This Street is connected to left of the Junction
			System.out.println("Vertical Street with a Start Junction");
			
			g.setColor(streetColor);
			
			streetHeight = TrafficLightSimulator.defaultStreetLenght;
			streetLenght =  numOfLanes * TrafficLightSimulator.defaultLaneWidth;
			
			g.fillRect(origin.x , origin.y, streetLenght, streetHeight);
			
			g.setColor(laneColor);
			if (numOfLanes > 1) {
				for (int i = 1 ; i < numOfLanes ; i++) {
					g.drawLine(origin.x + (TrafficLightSimulator.defaultLaneWidth * i), origin.y , origin.x + (TrafficLightSimulator.defaultLaneWidth * i), origin.y + streetHeight);
				}
			}
			
			drawComplete = true;
			
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

}
