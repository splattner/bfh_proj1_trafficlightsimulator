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

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import ch.bfh.proj1.trafficlightsimulator.TrafficLightSimulator.SimulationMode;
import ch.bfh.proj1.trafficlightsimulator.vehicles.Vehicle;

public class Simulation extends Thread {
	
	private boolean running = false;
	private boolean breakState = false;
	private boolean loaded = false;
	
	private TrafficLightSimulator simulator;
	
	private Random rand = new Random();
	
	// Speed for automatic Simulation mode
	private int simulationSpeed = 20;
	private int currentStep = 0;
	
	public Simulation(TrafficLightSimulator simulator) {this.setSimulator(simulator);}
		
	
	public Simulation (Simulation oldSimulation) {

		this.setSimulationSpeed(oldSimulation.getSimulationSpeed());
		this.setSimulator(oldSimulation.getSimulator());
		this.setJunctions(oldSimulation.getJunctions());
		this.setStreets(oldSimulation.getStreets());
		this.setRoutes(oldSimulation.getRoutes());
		this.setLoaded(oldSimulation.isLoaded());
		
		// Clear Vehicle List
		this.setVerhicles(new ArrayList<Vehicle>());
		
		// Reset Vehicles on Lane
		for (Street s : this.getStreets()) {
			for (Lane l : s.getLanes()) {
				l.getVerhiclesOnLane().clear();
			}
		}

		
		oldSimulation = null;
	}
	
	private Collection<Junction> junctions;
	public Collection<Junction> getJunctions() {return junctions;}
	public void setJunctions(Collection<Junction> junctions) {this.junctions = junctions;}
	
	private Collection<Street> streets;
	public void setStreets(Collection<Street> streets) {this.streets = streets;}
	public Collection<Street> getStreets() {return streets;}
	
	private Collection<Route> routes;
	public Collection<Route> getRoutes() {return routes;}
	public void setRoutes(Collection<Route> routes) {this.routes = routes;}
	
	private ArrayList<Vehicle> verhicles;
	public ArrayList<Vehicle> getVerhicles() {return verhicles;}
	public void setVerhicles(ArrayList<Vehicle> verhicles) {this.verhicles = verhicles;}
	
	public void run() {

		//System.out.println("Start SimLoop");
		
		synchronized (this) {
			while (this.isRunning()) {
				
				
				
				// Simulate all vehicles
				for (Vehicle v : this.getSimulator().getCurrentSimulation().getVerhicles()) {
					v.simulationStep();
				}
				
				
				
				// Simulate all Junctions (Traffic Lights)
				for (Junction j : this.getJunctions()) {
					j.simulationStep(this.getSimulator().getCurrentMode());
				}
				
				
				// Redraw Sim Panel
				this.getSimulator().refreshWindow();
				
				
				
				
				
				// Add Vehicles if in automatic mode
				// Only add them, when we have a simulation speed and current step is 0
				if (this.getSimulationSpeed() > 0 && this.currentStep == 0 && this.getSimulator().getSimulationMode() == SimulationMode.automatic) {
					
					//System.out.println("Automatic mode");
					
					// Get Randoms for Vehicle and Route (from 0 to 100)
					// If boundery is between a vehicle/routes lower and upper bounder, then add the car to the route.
					int randNumberVehicle = this.rand.nextInt(100);
					int randNumberRoute = this.rand.nextInt(100);
					
					int currentVehicleDist = 0;
					int nextVehicleDist = 0;
					
					int currentRouteDist = 0;
					int nextRouteDist = 0;
					
					// Loop trought all Vehicles
					for (VehicleRegistryEntry ve : this.getSimulator().getVehicleRegistry()) {
						// Set distribution for upper boundery
						nextVehicleDist += ve.getDistribution();
						
						// if random was for this vehicle
						if (randNumberVehicle > currentVehicleDist && randNumberVehicle <= nextVehicleDist) {

							// Loop trought all routes
							for (Route r : this.getSimulator().getCurrentSimulation().getRoutes()) {
								// Set distribution for upper boundery
								nextRouteDist += r.getDistribution();
								
								// Random was for this route
								if (randNumberRoute > currentRouteDist && randNumberRoute <= nextRouteDist) {
	
									Vehicle v = null;
									try {
										v = (Vehicle) ve.getVehicle().newInstance();
									} catch (InstantiationException e) {

									} catch (IllegalAccessException e) {

									}
									
									
									if (v != null) {
										v.setRoute(r);
										v.setCurrentLane(r.getLanes().get(0));
										
										this.getSimulator().getCurrentSimulation().getVerhicles().add(v);
									}
									
								}
								
								// Set Distribution for lower boundery (this upper is the next lower)
								currentRouteDist += r.getDistribution();
								
							}
							
						}
						
						// Set Distribution for lower boundery (this upper is the next lower)
						currentVehicleDist += ve.getDistribution();

					}	
					
				}
				
				// Increase current Simulation Step
				currentStep++;
				// Based on the Simulation speed, do we need to add new vehicles?
				if (this.getSimulationSpeed() > 0 && ((currentStep % ((101 - this.getSimulationSpeed())) * 10000)  == 0)) {
					currentStep = 0;
				}
				
				// Wait n ms after each loop
				try {
					this.wait(10);
				} catch (InterruptedException e1) {

				}
				

				// Check if we should break
				if (this.isBreaking()) {
					//System.out.println("Breaking");
					// Redraw Sim Panel
					this.getSimulator().refreshWindow();
					try {
						this.wait();
					} catch (InterruptedException e) {

					}
				}
			
			}
			
		}

		//System.out.println("Interrupted");
	}
	
	public void stopSimulation() {
		
		this.running = false;
		synchronized(this) {
			this.interrupt();
		}
	}
	
	public void startSimulation() {
		this.running = true;

		this.start();
	}

	public boolean isRunning() { return running;}

	public void setRunning(boolean running) { this.running = running;}
	
	public boolean isBreaking() {
		return breakState;
	}
	
	public void setBreaking(boolean breakState) {
		this.breakState = breakState;
		
		if (!this.isBreaking()) {
			synchronized(this) {
				this.notify();
			}
		}
	}



	public TrafficLightSimulator getSimulator() {return simulator;}
	public void setSimulator(TrafficLightSimulator simulator) {this.simulator = simulator;}

	public int getSimulationSpeed() {return simulationSpeed;}
	public void setSimulationSpeed(int simulationSpeed) {this.simulationSpeed = simulationSpeed;}


	public boolean isLoaded() { return loaded;}
	public void setLoaded(boolean loaded) { this.loaded = loaded; }
	
	/**
	 * This Method calculates all origins and all dimensions for the simulation elements (streets, lanes, junctions)
	 * The algorithm starts with the first street 
	 * So it is important that this street has a origin set
	 * the first street needs a End Junction
	 * and this street needs to be at top or at left of the connected end junction
	 * Otherwise the algorithm does not work!
	 */
	public void initOrigins() {

		// Start with first street (the one very left/top and should have no start junction
		Street s = ((List<Street>) this.getStreets()).get(0);
		
		Junction j;
		int numOfLanes = 0;
		Dimension d;
		Point newOrigin;
		
		// Check if this is a horizontal or vertical steet
		if (s.getEndJunction() != null) {
			
			j = s.getEndJunction();
			numOfLanes = s.getLanes().size();
			
			// Check if street is connected to left side
			if (j.getLeftStreet().equals(s)) {
				// Its a horizontal street

				
				d = new Dimension();
				d.setSize(TrafficLightSimulator.defaultStreetLenght, numOfLanes * TrafficLightSimulator.defaultLaneWidth);
				s.setDimension(d);

				// Set Origin for Junction
				newOrigin = new Point();
				newOrigin.setLocation(s.getOrigin().x + s.getDimension().width, s.getOrigin().y);
				j.setOrigin(newOrigin);
				j.setDimension(j.calcucateDimension());

				// Go to next Street (top, right, bottom)
				Street streets[] = {j.getTopStreet(), j.getRightStreet(), j.getBottomStreet()};
				for (Street nextStreet : streets) {
					if (nextStreet != null) {
						calcucalteOriginAndDimension(nextStreet, j);
					}
				}

				
			} else if ((j.getTopStreet().equals(s))) {
				// Its a vertical street
	
				d = new Dimension();
				d.setSize(numOfLanes * TrafficLightSimulator.defaultLaneWidth, TrafficLightSimulator.defaultStreetLenght);
				s.setDimension(d);

				// Set Origin for Junction
				newOrigin = new Point();
				newOrigin.setLocation(s.getOrigin().x , s.getOrigin().y + s.getDimension().height);
				j.setOrigin(newOrigin);
				j.setDimension(j.calcucateDimension());

				// Go to next Street (left, bottom, right)
				Street streets[] = {j.getLeftStreet(), j.getBottomStreet(), j.getRightStreet()};
				for (Street nextStreet : streets) {
					if (nextStreet != null) {
						calcucalteOriginAndDimension(nextStreet, j);
					}
				}

			}
			
		}
	}
	
	/**
	 * Recursive function to calculate Origin and Dimension of Streets, Junctions
	 * @param s The street for which we should calculate
	 * @param lastJunction the junction from which we came onto the street
	 */
	private void calcucalteOriginAndDimension(Street s, Junction lastJunction) {
		
		Junction nextJunction = null;
		Street nextStreet = null;
		Point newOrigin = new Point();
		Dimension newDimension = new Dimension();
		int numOfLanes;
		
		if (s.getOrigin() != null) {
			// Already calcucaled
			return;
		}
		
	
		numOfLanes = s.getLanes().size();
		
		// Check if Last Junction was start or End
		if (s.getStartJunction() != null && s.getStartJunction().equals(lastJunction)){
			// This Street starts at last one
			nextJunction = s.getEndJunction();
		} else {
			if (s.getEndJunction() != null && s.getEndJunction().equals(lastJunction)) {
				// This Street ends at last one
				nextJunction = s.getStartJunction();	
			}
		}

		
		// Check orientation for street
		if (lastJunction.getTopStreet() != null && lastJunction.getTopStreet().equals(s)) {
			// it was a top street

			// Set Dimension and origin for street
			newDimension.setSize(numOfLanes * TrafficLightSimulator.defaultLaneWidth, TrafficLightSimulator.defaultStreetLenght);
			s.setDimension(newDimension);
			newOrigin.setLocation(lastJunction.getOrigin().x, lastJunction.getOrigin().y - newDimension.height);
			s.setOrigin(newOrigin);
	
			if (nextJunction == null) {
				// No Next Junction. This is the end of the street
				return;
			}
			
			// Go to next Street (left, top, right)
			if ((nextStreet = nextJunction.getLeftStreet()) != null) {
				if (nextJunction.getOrigin() == null) {
					
					// Dimension and Origin for next Junction
					nextJunction.setDimension(nextJunction.calcucateDimension());
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x - s.getDimension().width - nextJunction.getDimension().width, s.getOrigin().y);
					nextJunction.setOrigin(newOrigin);
				}

				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			
			// Go to next Street
			if ((nextStreet = nextJunction.getTopStreet()) != null) {			
				if (nextJunction.getOrigin() == null) {
					
					// Dimension and Origin for next Junction
					nextJunction.setDimension(nextJunction.calcucateDimension());
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x + s.getDimension().width, s.getOrigin().y);
					nextJunction.setOrigin(newOrigin);

				}
				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			
			// Go to next Street
			if ((nextStreet = nextJunction.getRightStreet()) != null) {
				if (nextJunction.getOrigin() == null) {
					// Dimension and Origin for next Junction
					nextJunction.setDimension(nextJunction.calcucateDimension());
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x + s.getDimension().width, s.getOrigin().y);
					nextJunction.setOrigin(newOrigin);
				}
				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			
			//No more Streets for next junction. Return			
			return;
			
			
		}
		
		// Check orientation for street
		if (lastJunction.getBottomStreet() != null && lastJunction.getBottomStreet().equals(s)) {
			// it was a bottom street
			
			// Set Dimension and origin for street
			newDimension.setSize(numOfLanes * TrafficLightSimulator.defaultLaneWidth, TrafficLightSimulator.defaultStreetLenght);
			s.setDimension(newDimension);
			newOrigin.setLocation(lastJunction.getOrigin().x, lastJunction.getOrigin().y + lastJunction.getDimension().height);
			s.setOrigin(newOrigin);
			
			
			if (nextJunction == null) {
				// No Next Junction. This is the end of the street
				return;
			}
			
			// Go to next Street (left, bottom, right)
			if ((nextStreet = nextJunction.getLeftStreet()) != null) {
				if (nextJunction.getOrigin() == null) {
					// Dimension and Origin for next Junction
					nextJunction.setDimension(nextJunction.calcucateDimension());
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x, s.getOrigin().y + s.getDimension().height);
					nextJunction.setOrigin(newOrigin);
				}
				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			
			// Go to next Street
			if ((nextStreet = nextJunction.getBottomStreet()) != null) {
				if (nextJunction.getOrigin() == null) {
					// Dimension and Origin for next Junction
					nextJunction.setDimension(nextJunction.calcucateDimension());
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x + s.getDimension().width, s.getOrigin().y + s.getDimension().height);
					nextJunction.setOrigin(newOrigin);
				}
				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			
			// Go to next Street
			if ((nextStreet = nextJunction.getRightStreet()) != null) {
				if (nextJunction.getOrigin() == null) {
					// Dimension and Origin for next Junction
					nextJunction.setDimension(nextJunction.calcucateDimension());
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x + s.getDimension().width, s.getOrigin().y);
					nextJunction.setOrigin(newOrigin);
				}
				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			
			//No more Streets for next junction. Return	
			return;
		}
		
		// Check orientation for street
		if (lastJunction.getLeftStreet() != null && lastJunction.getLeftStreet().equals(s)) {
			// it was a left street

			// Set Dimension and origin for street
			newDimension.setSize(TrafficLightSimulator.defaultStreetLenght, numOfLanes * TrafficLightSimulator.defaultLaneWidth);
			s.setDimension(newDimension);
			newOrigin.setLocation(lastJunction.getOrigin().x  - newDimension.width, lastJunction.getOrigin().y);
			s.setOrigin(newOrigin);
			
			
			if (nextJunction == null) {
				// No Next Junction. This is the end of the street
				return;
			}
			
			// Go to next Street (top, left, bottom)
			if ((nextStreet = nextJunction.getTopStreet()) != null) {
				if (nextJunction.getOrigin() == null) {
					// Dimension and Origin for next Junction
					nextJunction.setDimension(nextJunction.calcucateDimension());
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x - nextJunction.getDimension().width, s.getOrigin().y);
					nextJunction.setOrigin(newOrigin);
				}
				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			
			// Go to next Street
			if ((nextStreet = nextJunction.getLeftStreet()) != null) {
				if (nextJunction.getOrigin() == null) {
					// Dimension and Origin for next Junction
					nextJunction.setDimension(nextJunction.calcucateDimension());
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x - s.getDimension().width - nextJunction.getDimension().width, s.getOrigin().y);
					nextJunction.setOrigin(newOrigin);
				}
				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			
			// Go to next Street
			if ((nextStreet = nextJunction.getBottomStreet()) != null) {			
				if (nextJunction.getOrigin() == null) {
					// Dimension and Origin for next Junction
					nextJunction.setDimension(nextJunction.calcucateDimension());
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x + s.getDimension().width, s.getOrigin().y + s.getDimension().height);
					nextJunction.setOrigin(newOrigin);
				}
				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			//No more Streets for next junction. Return	
			return;
		}
		
		// Check orientation for street
		if (lastJunction.getRightStreet() != null &&  lastJunction.getRightStreet().equals(s)) {
			// it was a right street
			
			// Set Dimension and origin for street
			newDimension.setSize(TrafficLightSimulator.defaultStreetLenght, numOfLanes * TrafficLightSimulator.defaultLaneWidth);
			s.setDimension(newDimension);
			newOrigin.setLocation(lastJunction.getOrigin().x + lastJunction.getDimension().width, lastJunction.getOrigin().y);
			s.setOrigin(newOrigin);

			
			if (nextJunction == null) {
				// No Next Junction. This is the end of the street
				return;
			}
			
			// Go to next Street (top, right, bottom)
			if ((nextStreet = nextJunction.getTopStreet()) != null) {
				if (nextJunction.getOrigin() == null) {
					// Dimension and Origin for next Junction
					nextJunction.setDimension(nextJunction.calcucateDimension());
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x + s.getDimension().width, s.getOrigin().y);
					nextJunction.setOrigin(newOrigin);
				}

				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			
			// Go to next Street
			if ((nextStreet = nextJunction.getRightStreet()) != null) {
				if (nextJunction.getOrigin() == null) {
					// Dimension and Origin for next Junction
					nextJunction.setDimension(nextJunction.calcucateDimension());
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x + s.getDimension().width, s.getOrigin().y);
					nextJunction.setOrigin(newOrigin);
				}
				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			
			// Go to next Street
			if ((nextStreet = nextJunction.getBottomStreet()) != null) {
				if (nextJunction.getOrigin() == null) {
					// Dimension and Origin for next Junction	
					nextJunction.setDimension(nextJunction.calcucateDimension());
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x + s.getDimension().width, s.getOrigin().y + s.getDimension().height);
					nextJunction.setOrigin(newOrigin);
				}
				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			//No more Streets for next junction. Return	
			return;
		}
		
		
	}
	
	/**
	 * Move all elements in the Simulation by x & y
	 * @param x
	 * @param y
	 */
	public void moveSimulation(int x, int y) {
		
		// Only if simualation is loaded, otherwise there are no elements to move
		if (this.isLoaded()) {
			
			// Move all Junctions
			for (Junction j : this.getJunctions()) {
				j.setOrigin(new Point(j.getOrigin().x + x, j.getOrigin().y + y));
			}
			
			// Move all streets (and lanes, and trafficlights)
			for (Street s : this.getStreets()) {
				s.setOrigin(new Point(s.getOrigin().x + x, s.getOrigin().y + y));
			}
			
			// Move all Vehicles
			for (Vehicle v : this.getVerhicles()) {
				v.setOrigin(new Point(v.getOrigin().x + x, v.getOrigin().y + y));
			}
		}
	}
}
