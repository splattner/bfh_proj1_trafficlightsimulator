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

import java.util.Collections;

import ch.bfh.proj1.trafficlightsimulator.Lane.LaneOrientations;
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


	private LinkedList<TrafficLight> lightToChange;


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

		d.setSize(numOfLanesHorizontal * TrafficLightSimulator.defaultLaneWidth, 
				numOfLanesVertical * TrafficLightSimulator.defaultLaneWidth);

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

			// Go trough all attached streets and add traffic lights
			Street streets[] = { this.getTopStreet(), this.getBottomStreet(),this.getLeftStreet(), this.getRightStreet() };

			for (Street s : streets) {
				if (s != null) {
					for (Lane l : s.getLanes()) {
						if (s.getEndJunction()!=null){
							if (s.getEndJunction().equals(this) && l.getLaneOrientation() == LaneOrientations.START_TO_END) {
								if (l.getTrafficLight() != null) {
									this.trafficLights.add(l.getTrafficLight());
								}
							}
						}
						if (s.getStartJunction() != null) {
							if (s.getStartJunction().equals(this) && l.getLaneOrientation() == LaneOrientations.END_TO_START) {
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
	 * Return all outgoing lanes of this Junction
	 * @return
	 */
	public LinkedList<Lane> getOutgoingLanes() {
		LinkedList<Lane> lanes = new LinkedList<Lane>();

		if (this.topStreet != null) {
			for (Lane l : this.topStreet.getLanes()) {
				if (l.isBottomToTop()) {
					lanes.add(l);
				}
			}
		}

		if (this.rightStreet != null) {
			for (Lane l : this.rightStreet.getLanes()) {
				if (l.isLeftToRight()) {
					lanes.add(l);
				}
			}
		}

		if (this.bottomStreet != null) {
			for (Lane l : this.bottomStreet.getLanes()) {
				if (l.isTopToBottom()) {
					lanes.add(l);
				}
			}
		}

		if (this.leftStreet != null) {
			for (Lane l : this.leftStreet.getLanes()) {
				if (l.isRightToLeft()) {
					lanes.add(l);
				}
			}
		}

		return lanes;

	}

	/**
	 * One Step of the simulation. This function is called by the Simulation Threat
	 * Depending of the Traffic Light Mode, compute the traffic light status
	 * @param mode The current Traffic Light Mode
	 */
	public void simulationStep(TrafficLightSimulator.TrafficLightMode mode) {


		/*
		 * Loop trough all lights of this junction
		 */
		if (mode == TrafficLightMode.sequenze) {

			TrafficLight currentGreenLight = null;
			int currentTrafficLightIndex = -1;

			// Check if there is a green light
			for (TrafficLight l : this.getTrafficLights()) {
				if (l.isGreen()) {
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

				// Is it time to change the light?
				if (currentGreenLight.getTimeLastChange() > (1 * TrafficLightSimulator.minimumGreenLightPhase)) {
					currentGreenLight.setRed();

					// Change Light to next one
					currentTrafficLightIndex = this.getTrafficLights().indexOf(currentGreenLight);

					try {
						currentGreenLight = this.getTrafficLights().get(currentTrafficLightIndex+1);
					} catch (IndexOutOfBoundsException e) {
						// no next light, so reset to first
						currentGreenLight = this.getTrafficLights().getFirst();
					}

					currentGreenLight.setGreen();

				} else {
					// Increase Light Time
					currentGreenLight.setGreen();
				}
			}


		}

		// Smart Traffic Light Mode
		if (mode == TrafficLightMode.smart) {


			TrafficLight currentGreenLight = null;
			boolean hasGreenLight = false;

			if (lightToChange == null) {
				lightToChange = new LinkedList<TrafficLight>();
			}

			// Check if there is a green light
			for (TrafficLight l : this.getTrafficLights()) {
				if (l.isGreen()) {
					currentGreenLight = l;
				}
			}

			// If we have a green light
			// Check if it was green long enough
			if (currentGreenLight != null) {
				if (currentGreenLight.getTimeLastChange() > (1 * TrafficLightSimulator.minimumGreenLightPhase) 
						|| currentGreenLight.getNumOfVehiclesNearLight() == 0) {
					currentGreenLight.setRed();
					hasGreenLight = false;
				} else {
					currentGreenLight.setGreen();
					hasGreenLight = true;
				}
			}

			// List with best light to change. Light with more vehicles are at beginning
			LinkedList<TrafficLight> bestLight = new LinkedList<TrafficLight>();

			// If we don't yet have a green light
			if (!hasGreenLight) {
				// Get traffic light with most vehicles
				//System.out.println("Number of TF's: " + this.getTrafficLights().size());

				for (TrafficLight l : this.getTrafficLights()) {

					if (l.getNumOfVehiclesNearLight() > 0) {
						bestLight.add(l);
					}
				}
				// Sort best Light, so the one with most vehicles are at beginning (TrafficLight is Comparable)
				Collections.sort(bestLight);
				
				//System.out.println("#############: Size of BestLight :" + bestLight.size());
				for (TrafficLight tf : bestLight) {
					//System.out.println("NumberonLane: " + tf.getNumOfVehiclesNearLight());

					//If these bestlights are not yet queued for change, add them (the one with most vehicles first)
					if (!lightToChange.contains(tf)) {
						lightToChange.add(tf);
					}
				}
				
				// if there is a light to change, do it. Take the first one of the queued list and remove it
				if (lightToChange.size() > 0) {

					lightToChange.getFirst().setGreen();
					lightToChange.removeFirst();


				}
				
				
			}




		}

	}

}
