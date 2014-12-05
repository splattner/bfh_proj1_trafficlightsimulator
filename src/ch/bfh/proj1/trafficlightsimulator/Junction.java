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
	private int id;
	
	private Color junctionColor = Color.DARK_GRAY;
	
	private LinkedList<TrafficLight> trafficLights;
	
	public Junction (int id){this.id=id;}
	
	public int getId() {return id;}
	
	public Street getTopStreet() {return topStreet;}
	public void setTopStreet(Street topStreet) {this.topStreet = topStreet;}
	
	public Street getBottomStreet() {return bottomStreet;}
	public void setBottomStreet(Street bottomStreet) {this.bottomStreet = bottomStreet;}
	
	public Street getLeftStreet() {return leftStreet;}
	public void setLeftStreet(Street leftStreet) {this.leftStreet = leftStreet;}
	
	public Street getRightStreet() {return rightStreet;}
	public void setRightStreet(Street rightStreet) {this.rightStreet = rightStreet;}
	

	public void paintObject(Graphics g) {
		g.setColor(junctionColor);
		g.fillRect(origin.x, origin.y, dimension.width, dimension.height);
		
	}
	

	public Point getOrigin() {return this.origin;}
	public void setOrigin(Point origin) {this.origin = origin;}
	
	public Dimension getDimension() {return this.dimension;}
	public void setDimension(Dimension dimension) {this.dimension = dimension;}

	
	/**
	 * Calculate Dimension of this Junction based on the connected streets / lanes
	 * @return Dimension of this junction
	 */
	public Dimension calcucateDimension() {
		Dimension d = new Dimension();
		int numOfLanesVertical = 0;
		int numOfLanesHorizontal = 0;
		
		// Get number of lanes on top and bottom streets
		if (this.getTopStreet() != null) {
			numOfLanesVertical = this.getTopStreet().getLanes().size();
		} else {
			if (this.getBottomStreet() != null) {
				numOfLanesVertical = this.getBottomStreet().getLanes().size();
			}
		}
		
		// Get number of lanes on left and right streets
		if (this.getLeftStreet() != null) {
			numOfLanesVertical = this.getLeftStreet().getLanes().size();
		} else {
			if (this.getRightStreet() != null) {
				numOfLanesVertical = this.getRightStreet().getLanes().size();
			} 
		}
		
		// if no Horizontal lanes, use num of Vertical Lanes
		if (numOfLanesHorizontal == 0) {
			numOfLanesHorizontal = numOfLanesVertical;
		}
		
		// if no Vertical lanes, use num of Horizontal Lanes
		if (numOfLanesVertical == 0) {
			numOfLanesVertical = numOfLanesHorizontal;
		}
		
		d.setSize(numOfLanesHorizontal * TrafficLightSimulator.defaultLaneWidth, numOfLanesVertical * TrafficLightSimulator.defaultLaneWidth);
		
		return d;
	}
	
	/**
	 * Get all traffic lights of the connected streets / lanes
	 * @return LinkedList with all connected Traffic Lights
	 */
	public LinkedList<TrafficLight> getTrafficLights() {
		// We need to do this only once. After that, reuse the List
		if (this.trafficLights == null) {
			
			this.trafficLights = new LinkedList<TrafficLight>();
			
			// Go trought all attached streets and add traffic lights
			LinkedList<Street> streets = new LinkedList<Street>();
			streets.add(this.getTopStreet());
			streets.add(this.getBottomStreet());
			streets.add(this.getLeftStreet());
			streets.add(this.getRightStreet());
			
			for (Street s : streets) {
				if (s != null) {
					for (Lane l : s.getLanes()) {
						if (s.getEndJunction()!=null){
							if (s.getEndJunction().equals(this) && l.getLaneOrientation() == laneOrientations.startToEnd) {
								if (l.getTrafficLight() != null) {
									this.trafficLights.add(l.getTrafficLight());
								}
							}
						}
						if (s.getStartJunction() != null) {
							if (s.getStartJunction().equals(this) && l.getLaneOrientation() == laneOrientations.endToStart) {
								if (l.getTrafficLight() != null) {
									this.trafficLights.add(l.getTrafficLight());
								}
							}
						}
					}
				}
			}

		}
		

		return this.trafficLights;
		
	}
	
	/**
	 * One Step of the simulation. This function is called by the Simulation Threat
	 * Depending of the Traffic Light Mode, compute the traffic light status
	 * @param mode The current Traffic Light Mode
	 */
	public void simulationStep(TrafficLightSimulator.TrafficLightMode mode) {
		
		
		if (mode == TrafficLightMode.sequenze) {
			
			TrafficLight currentGreenLight = null;
			int currentTrafficLightIndex = -1;
			
			// Check if there is a green light
			for (TrafficLight l : this.getTrafficLights()) {
				if (l.getCurrentStatus() == trafficLightStatus.GREEN) {
					currentGreenLight = l;
					break;
				}
			}
			
			// None is green, set the first one
			if (currentGreenLight == null){
				currentGreenLight = this.getTrafficLights().getFirst();
			}
			
			// Check if we have a light at all?
			if (currentGreenLight != null) {
				
				if (currentGreenLight.getTimeLastChange() > (1 * TrafficLightSimulator.minimumGreenLightPhase)) {
					currentGreenLight.setCurrentStatus(trafficLightStatus.RED);
					currentGreenLight.setTimeLastChange(0);
					
					// Change Light to next one
					currentTrafficLightIndex = this.getTrafficLights().indexOf(currentGreenLight);
					
					try {
						currentGreenLight = this.getTrafficLights().get(currentTrafficLightIndex+1);
					} catch (IndexOutOfBoundsException e) {
						currentGreenLight = this.getTrafficLights().getFirst();
					}
					
					currentGreenLight.setCurrentStatus(trafficLightStatus.GREEN);
	
				} else {
					// Increase Light Time
					currentGreenLight.setTimeLastChange(currentGreenLight.getTimeLastChange()+1);
				}
			}
			
			
		}

		// Smart Traffic Light Mode
		if (mode == TrafficLightMode.smart) {
		
			TrafficLight lightToChange = null;
			TrafficLight backupLightToChange = null;
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
						// Don't change if its the old one
						if (currentGreenLight != l) {
							numberOnLane = l.getNumOfVehiclesNearLight();
							lightToChange = l;
						} else {
							// if no other light meets the criteria, we still have one to change
							backupLightToChange = l;
						}
					}
				}
				
				
			}
			
			if (lightToChange == null && backupLightToChange != null) {
				lightToChange = backupLightToChange;
			}
			
			// if there is a light to change, do it
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
