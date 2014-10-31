package ch.bfh.proj1.trafficlightsimulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;

import ch.bfh.proj1.trafficlightsimulator.Lane.laneOrientations;
import ch.bfh.proj1.trafficlightsimulator.TrafficLight.trafficLightStatus;
import ch.bfh.proj1.trafficlightsimulator.TrafficLightSimulator.TrafficLightMode;

public class Junction implements DrawableObject{
	
	private Street topStreet;
	private Street bottomStreet;
	private Street leftStreet;
	private Street rightStreet;
	
	private Point origin;
	private Dimension dimension;
	
	private Color junctionColor = Color.DARK_GRAY;
	
	
	private LinkedList<TrafficLight> trafficLights;
	
	public Street getTopStreet() {return topStreet;}
	
	public void setTopStreet(Street topStreet) {this.topStreet = topStreet;}
	
	public Street getBottomStreet() {return bottomStreet;}
	
	public void setBottomStreet(Street bottomStreet) {this.bottomStreet = bottomStreet;}
	
	public Street getLeftStreet() {return leftStreet;}
	
	public void setLeftStreet(Street leftStreet) {this.leftStreet = leftStreet;}
	
	public Street getRightStreet() {return rightStreet;}
	
	public void setRightStreet(Street rightStreet) {this.rightStreet = rightStreet;}
	

	
	@Override
	public void paintObject(Graphics g) {
		g.setColor(junctionColor);
		g.fillRect(origin.x, origin.y, dimension.width, dimension.height);
		
	}
	
	@Override
	public void setOrigin(Point origin) {this.origin = origin;}
	
	@Override
	public void setDimension(Dimension dimension) {this.dimension = dimension;}
	
	@Override
	public Point getOrigin() {return this.origin;}
	
	@Override
	public Dimension getDimension() {return this.dimension;}
	
	public Dimension calcucateDimension() {
		Dimension d = new Dimension();
		int numOfLanesVertical = 0;
		int numOfLanesHorizontal = 0;
		
		if (this.getTopStreet() != null) {
			numOfLanesVertical = this.getTopStreet().getLanes().size();
		} else {
			if (this.getBottomStreet() != null) {
				numOfLanesVertical = this.getBottomStreet().getLanes().size();
			}
		}
		
		if (this.getLeftStreet() != null) {
			numOfLanesVertical = this.getLeftStreet().getLanes().size();
		} else {
			if (this.getRightStreet() != null) {
				numOfLanesVertical = this.getRightStreet().getLanes().size();
			} 
		}
		
		if (numOfLanesHorizontal == 0) {
			numOfLanesHorizontal = numOfLanesVertical;
		}
		
		if (numOfLanesVertical == 0) {
			numOfLanesVertical = numOfLanesHorizontal;
		}
		
		d.setSize(numOfLanesHorizontal * TrafficLightSimulator.defaultLaneWidth, numOfLanesVertical * TrafficLightSimulator.defaultLaneWidth);
		
		return d;
	}
	
	public LinkedList<TrafficLight> getTrafficLights() {
		if (this.trafficLights == null) {
			
			this.trafficLights = new LinkedList<TrafficLight>();
			
			// Traffic Lights on Top Street
			if (this.getTopStreet() != null) {
				for (Lane l : this.getTopStreet().getLanes()) {
					if (this.getTopStreet().getEndJunction().equals(this) && l.getLaneOrientation() == laneOrientations.startToEnd) {
						if (l.getTrafficLight() != null) {
							this.trafficLights.add(l.getTrafficLight());
						}
					}
				}
			}
			
			// Traffic Lights on Bottom Street
			if (this.getBottomStreet() != null) {
				for (Lane l : this.getBottomStreet().getLanes()) {
					if (this.getTopStreet().getEndJunction().equals(this) && l.getLaneOrientation() == laneOrientations.endToStart) {
						if (l.getTrafficLight() != null) {
							this.trafficLights.add(l.getTrafficLight());
						}
					}
				}
			}
			
			// Traffic Lights on Left Street
			if (this.getLeftStreet() != null) {
				for (Lane l : this.getLeftStreet().getLanes()) {
					if (this.getTopStreet().getEndJunction().equals(this) && l.getLaneOrientation() == laneOrientations.startToEnd) {
						if (l.getTrafficLight() != null) {
							this.trafficLights.add(l.getTrafficLight());
						}
					}
				}
			}
			
			// Traffic Lights on Right Street
			if (this.getRightStreet() != null) {
				for (Lane l : this.getRightStreet().getLanes()) {
					if (this.getTopStreet().getEndJunction().equals(this) && l.getLaneOrientation() == laneOrientations.endToStart) {
						if (l.getTrafficLight() != null) {
							this.trafficLights.add(l.getTrafficLight());
						}
					}
				}
			}
			
		}
		
		
		return this.trafficLights;
		
	}
	
	public void simulationStep(TrafficLightSimulator.TrafficLightMode mode) {
		

		if (mode == TrafficLightMode.smart) {
		
			TrafficLight lightToChange = null;
			TrafficLight currentGreenLight = null;
			int numberOnLane = Integer.MIN_VALUE;
			boolean hasGreenLight = false;
			
			// Check if there is a green light
			for (TrafficLight l : this.getTrafficLights()) {
				if (l.getCurrentStatus() == trafficLightStatus.GREEN) {
					currentGreenLight = l;
				}
			}
			
			// If we have a green light
			// Check if it was green enought
			if (currentGreenLight != null) {
				if (currentGreenLight.getTimeLastChange() > TrafficLightSimulator.minimumGreenLightPhase || currentGreenLight.getNumOfVehiclesNearLight() == 0) {
					currentGreenLight.setCurrentStatus(trafficLightStatus.RED);
					hasGreenLight = false;
				} else {
					currentGreenLight.setTimeLastChange(currentGreenLight.getTimeLastChange()+1);
					hasGreenLight = true;
				}
			}
			
			// If we don't yet have a green light
			if (!hasGreenLight) {
				// Get traffic light with most vehicles
				for (TrafficLight l : this.getTrafficLights()) {
					if (l.getNumOfVehiclesNearLight() > numberOnLane ) {
						numberOnLane = l.getNumOfVehiclesNearLight();
						lightToChange = l;
					}
				}
				
				
			}
				
			if (lightToChange != null && numberOnLane != 0) {
				lightToChange.setCurrentStatus(trafficLightStatus.GREEN);
				lightToChange.setTimeLastChange(0);
				
				// If new light was not old one, then reset Time
				if (lightToChange != currentGreenLight && currentGreenLight != null) {
					currentGreenLight.setTimeLastChange(0);
					
				}
			}

		}
		
	}

}
