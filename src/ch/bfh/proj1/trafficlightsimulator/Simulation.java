package ch.bfh.proj1.trafficlightsimulator;

import java.awt.Dimension;
import java.awt.Point;
import java.util.List;
import java.util.Random;

import ch.bfh.proj1.trafficlightsimulator.TrafficLightSimulator.SimulationMode;

public class Simulation extends Thread {
	
	private boolean running = false;
	private boolean breakState = false;
	private boolean loaded = false;
	
	private TrafficLightSimulator simulator;
	
	private Random rand = new Random();
	
	// Speed for automatic Simulation mode
	private int simulationSpeed = 20;
	private int currentStep = 0;
	
	public Simulation(TrafficLightSimulator simulator) {

		this.setSimulator(simulator);
		
	}
		
	
	public Simulation (Simulation oldSimulation) {

		this.setSimulationSpeed(oldSimulation.getSimulationSpeed());
		this.setSimulator(oldSimulation.getSimulator());

		
		oldSimulation = null;

		
	}
	
	public void run() {

		//System.out.println("Start SimLoop");
		
		synchronized (this) {
			while (this.isRunning()) {
				
				
				
				// Simulate all vehicles
				for (Vehicle v : this.getSimulator().getVerhicles()) {
					v.simulationStep();
				}
				
				
				
				// Simulate all Junctions (Traffic Lights)
				for (Junction j : this.getSimulator().getJunctions()) {
					j.simulationStep(this.getSimulator().getCurrentMode());
				}
				
				
				// Redraw Sim Panel
				//System.out.println("Redraw Sim Panel");
				this.getSimulator().getMainFrame().invalidate();
				this.getSimulator().getMainFrame().repaint();
				
				
				
				
				
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
							for (Route r : this.getSimulator().getRoutes()) {
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
										
										this.getSimulator().getVerhicles().add(v);
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
					//System.out.println("Redraw Sim Panel");
					this.getSimulator().getMainFrame().invalidate();
					this.getSimulator().getMainFrame().repaint();
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

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
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



	public TrafficLightSimulator getSimulator() {
		return simulator;
	}

	public void setSimulator(TrafficLightSimulator simulator) {
		this.simulator = simulator;
	}

	public int getSimulationSpeed() {
		return simulationSpeed;
	}

	public void setSimulationSpeed(int simulationSpeed) {
		this.simulationSpeed = simulationSpeed;
	}


	public boolean isLoaded() {
		return loaded;
	}


	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}
	
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
		Street s = ((List<Street>) this.getSimulator().getStreets()).get(0);
		
		//System.out.println("Startin with Street " + s.getOrigin().x + " / " + s.getOrigin().y);
		Junction j;
		int numOfLanes = 0;
		Dimension d;
		Point newOrigin;
		Street nextStreet = null;
		
		// Check if this is a horizontal or vertical steet
		if (s.getEndJunction() != null) {
			
			j = s.getEndJunction();
			numOfLanes = s.getLanes().size();
			
			// Check if street is connected to left side
			if (j.getLeftStreet().equals(s)) {
				// Its a horizontal street
				//System.out.println("Horizontal Street");
				
				
				d = new Dimension();
				d.setSize(TrafficLightSimulator.defaultStreetLenght, numOfLanes * TrafficLightSimulator.defaultLaneWidth);
				s.setDimension(d);
				//System.out.println("Calculated Dimension for Street " + d.getWidth() + " * " + d.getHeight());
				
				
				// Set Origin for Junction
				newOrigin = new Point();
				newOrigin.setLocation(s.getOrigin().x + s.getDimension().width, s.getOrigin().y);
				j.setOrigin(newOrigin);
				//System.out.println("Calculated Origin of Junction " + newOrigin.x + " / " + newOrigin.y);
				
				
				j.setDimension(j.calcucateDimension());
				//System.out.println("Calculated Dimension of Junction " + j.getDimension().getWidth() + " * " + j.getDimension().getHeight());
				
				// Go to next Street (top, right, bottom)
				if ((nextStreet = j.getTopStreet()) != null) {
					//System.out.println("Next Street is top");
					calcucalteOriginAndDimension(nextStreet, j);
				}
				
				// Go to next Street
				if ((nextStreet = j.getRightStreet()) != null) {
					//System.out.println("Next street is right");
					calcucalteOriginAndDimension(nextStreet, j);
				}
				
				// Go to next Street
				if ((nextStreet = j.getBottomStreet()) != null) {
					//System.out.println("Next street is bottom");
					calcucalteOriginAndDimension(nextStreet, j);
				}
				
			} else if ((j.getTopStreet().equals(s))) {
				// Its a vertical street
				//System.out.println("Vertical Street");
				
				d = new Dimension();
				d.setSize(numOfLanes * TrafficLightSimulator.defaultLaneWidth, TrafficLightSimulator.defaultStreetLenght);
				s.setDimension(d);
				//System.out.println("Calculated Dimension for Street " + d.getWidth() + " * " + d.getHeight());
				
				// Set Origin for Junction
				newOrigin = new Point();
				newOrigin.setLocation(s.getOrigin().x , s.getOrigin().y + s.getDimension().height);
				j.setOrigin(newOrigin);
				//System.out.println("Calculated Origin of Junction " + newOrigin.x + " / " + newOrigin.y);
				
				
				j.setDimension(j.calcucateDimension());
				//System.out.println("Calculated Dimension of Junction " + j.getDimension().getWidth() + " * " + j.getDimension().getHeight());
				
				// Go to next Street (left, bottom, right)
				if ((nextStreet = j.getLeftStreet()) != null) {
					//System.out.println("Next Street is left");
					calcucalteOriginAndDimension(nextStreet, j);
				}
				
				// Go to next Street
				if ((nextStreet = j.getBottomStreet()) != null) {
					//System.out.println("Next Street is bottom");
					calcucalteOriginAndDimension(nextStreet, j);
				}
				
				// Go to next Street
				if ((nextStreet = j.getRightStreet()) != null) {
					//System.out.println("Next Street is right");
					calcucalteOriginAndDimension(nextStreet, j);
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
			//System.out.println("Street already calcucaled");
			// Already calcucaled
			return;
		}
		
	
		numOfLanes = s.getLanes().size();
		
		// Check if Last Junction was start or End
		if (s.getStartJunction() != null && s.getStartJunction().equals(lastJunction)){
			//System.out.println("Last Junction was start for this street");
			// This Street starts at last one
			nextJunction = s.getEndJunction();
			
			
		} else {
			if (s.getEndJunction() != null && s.getEndJunction().equals(lastJunction)) {
				//System.out.println("Last Junction was end for this street");
				// This Street ends at last one
				nextJunction = s.getStartJunction();	
			}
		}

		
		// Check orientation for street
		if (lastJunction.getTopStreet() != null && lastJunction.getTopStreet().equals(s)) {
			// it was a top street
			//System.out.println("This Street is top of last Junction");

			newDimension.setSize(numOfLanes * TrafficLightSimulator.defaultLaneWidth, TrafficLightSimulator.defaultStreetLenght);
			s.setDimension(newDimension);
			//System.out.println("Set Dimension for this street " + newDimension.width + " * " + newDimension.height);
			
			newOrigin.setLocation(lastJunction.getOrigin().x, lastJunction.getOrigin().y - newDimension.height);
			s.setOrigin(newOrigin);
			//System.out.println("Set origin for this street " + newOrigin.x + " / " + newOrigin.y);
			
			if (nextJunction == null) {
				//System.out.println("no next Junction, this is the end of this street");
				return;
			}
			
			// Go to next Street (left, top, right)
			if ((nextStreet = nextJunction.getLeftStreet()) != null) {
				//System.out.println("Next is left Street of Junction");
				
				if (nextJunction.getOrigin() == null) {
					//System.out.println("Junction has no origin");
					
					nextJunction.setDimension(nextJunction.calcucateDimension());
					
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x - s.getDimension().width - nextJunction.getDimension().width, s.getOrigin().y);
					nextJunction.setOrigin(newOrigin);
					//System.out.println("Calculated Origin of Junction " + newOrigin.x + " / " + newOrigin.y);
				}

				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			
			// Go to next Street
			if ((nextStreet = nextJunction.getTopStreet()) != null) {
				//System.out.println("Next is top Street of Junction");
				
				
				if (nextJunction.getOrigin() == null) {
					//System.out.println("Junction has no origin");
					
					nextJunction.setDimension(nextJunction.calcucateDimension());
					
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x + s.getDimension().width, s.getOrigin().y);
					nextJunction.setOrigin(newOrigin);
					//System.out.println("Calculated Origin of Junction " + newOrigin.x + " / " + newOrigin.y);
				}
				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			
			// Go to next Street
			if ((nextStreet = nextJunction.getRightStreet()) != null) {
				//System.out.println("Next is right Street of Junction");
				
				if (nextJunction.getOrigin() == null) {
					//System.out.println("Junction has no origin");
					
					nextJunction.setDimension(nextJunction.calcucateDimension());
					
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x + s.getDimension().width, s.getOrigin().y);
					nextJunction.setOrigin(newOrigin);
					//System.out.println("Calculated Origin of Junction " + newOrigin.x + " / " + newOrigin.y);
				}
				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			
			//System.out.println("No more Streets for next junction. Return");
			
			return;
			
			
		}
		
		// Check orientation for street
		if (lastJunction.getBottomStreet() != null && lastJunction.getBottomStreet().equals(s)) {
			// it was a bottom street
			//System.out.println("This Street is bottom of last Junction");
			
			newDimension.setSize(numOfLanes * TrafficLightSimulator.defaultLaneWidth, TrafficLightSimulator.defaultStreetLenght);
			s.setDimension(newDimension);
			//System.out.println("Set Dimension for this street " + newDimension.width + " * " + newDimension.height);
			newOrigin.setLocation(lastJunction.getOrigin().x, lastJunction.getOrigin().y + lastJunction.getDimension().height);
			s.setOrigin(newOrigin);
			//System.out.println("Set origin for this street " + newOrigin.x + " / " + newOrigin.y);
			
			
			if (nextJunction == null) {
				//System.out.println("no next Junction, this is the end of this street");
				return;
			}
			
			// Go to next Street (left, bottom, right)
			if ((nextStreet = nextJunction.getLeftStreet()) != null) {
				//System.out.println("Next is left Street of Junction");
				
				if (nextJunction.getOrigin() == null) {
					//System.out.println("Junction has no origin");
					
					nextJunction.setDimension(nextJunction.calcucateDimension());
					
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x, s.getOrigin().y + s.getDimension().height);
					nextJunction.setOrigin(newOrigin);
					//System.out.println("Calculated Origin of Junction " + newOrigin.x + " / " + newOrigin.y);
				}
				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			
			// Go to next Street
			if ((nextStreet = nextJunction.getBottomStreet()) != null) {
				//System.out.println("Next is bottom Street of Junction");
				
				if (nextJunction.getOrigin() == null) {
					//System.out.println("Junction has no origin");
					
					nextJunction.setDimension(nextJunction.calcucateDimension());
					
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x + s.getDimension().width, s.getOrigin().y + s.getDimension().height);
					nextJunction.setOrigin(newOrigin);
					//System.out.println("Calculated Origin of Junction " + newOrigin.x + " / " + newOrigin.y);
				}
				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			
			// Go to next Street
			if ((nextStreet = nextJunction.getRightStreet()) != null) {
				//System.out.println("Next is right Street of Junction");
				
				if (nextJunction.getOrigin() == null) {
					//System.out.println("Junction has no origin");
					
					nextJunction.setDimension(nextJunction.calcucateDimension());
					
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x + s.getDimension().width, s.getOrigin().y);
					nextJunction.setOrigin(newOrigin);
					//System.out.println("Calculated Origin of Junction " + newOrigin.x + " / " + newOrigin.y);
				}
				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			
			//System.out.println("No more Streets for next junction. Return");
			return;
		}
		
		// Check orientation for street
		if (lastJunction.getLeftStreet() != null && lastJunction.getLeftStreet().equals(s)) {
			// it was a left street
			//System.out.println("This Street is left of last Junction");
			
			newDimension.setSize(TrafficLightSimulator.defaultStreetLenght, numOfLanes * TrafficLightSimulator.defaultLaneWidth);
			s.setDimension(newDimension);
			//System.out.println("Set Dimension for this street " + newDimension.width + " * " + newDimension.height);
			newOrigin.setLocation(lastJunction.getOrigin().x  - newDimension.width, lastJunction.getOrigin().y);
			s.setOrigin(newOrigin);
			//System.out.println("Set origin for this street " + newOrigin.x + " / " + newOrigin.y);
			
			
			if (nextJunction == null) {
				//System.out.println("no next Junction, this is the end of this street");
				return;
			}
			
			// Go to next Street (top, left, bottom)
			if ((nextStreet = nextJunction.getTopStreet()) != null) {
				//System.out.println("Next is top Street of Junction");
				
				if (nextJunction.getOrigin() == null) {
					//System.out.println("Junction has no origin");
					
					nextJunction.setDimension(nextJunction.calcucateDimension());
					
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x - nextJunction.getDimension().width, s.getOrigin().y);
					nextJunction.setOrigin(newOrigin);
					//System.out.println("Calculated Origin of Junction " + newOrigin.x + " / " + newOrigin.y);
				}
				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			
			// Go to next Street
			if ((nextStreet = nextJunction.getLeftStreet()) != null) {
				//System.out.println("Next is left Street of Junction");
				
				if (nextJunction.getOrigin() == null) {
					//System.out.println("Junction has no origin");
					
					nextJunction.setDimension(nextJunction.calcucateDimension());
					
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x - s.getDimension().width - nextJunction.getDimension().width, s.getOrigin().y);
					nextJunction.setOrigin(newOrigin);
					//System.out.println("Calculated Origin of Junction " + newOrigin.x + " / " + newOrigin.y);
				}
				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			
			// Go to next Street
			if ((nextStreet = nextJunction.getBottomStreet()) != null) {
				//System.out.println("Next is bottom Street of Junction");
				
				if (nextJunction.getOrigin() == null) {
					//System.out.println("Junction has no origin");
					
					nextJunction.setDimension(nextJunction.calcucateDimension());
					
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x + s.getDimension().width, s.getOrigin().y + s.getDimension().height);
					nextJunction.setOrigin(newOrigin);
					//System.out.println("Calculated Origin of Junction " + newOrigin.x + " / " + newOrigin.y);
				}
				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			//System.out.println("No more Streets for next junction. Return");
			return;
		}
		
		// Check orientation for street
		if (lastJunction.getRightStreet() != null &&  lastJunction.getRightStreet().equals(s)) {
			// it was a right street
			//System.out.println("This Street is right of last Junction");
			
			newDimension.setSize(TrafficLightSimulator.defaultStreetLenght, numOfLanes * TrafficLightSimulator.defaultLaneWidth);
			s.setDimension(newDimension);
			//System.out.println("Set Dimension for this street " + newDimension.width + " * " + newDimension.height);
			newOrigin.setLocation(lastJunction.getOrigin().x + lastJunction.getDimension().width, lastJunction.getOrigin().y);
			s.setOrigin(newOrigin);
			
			//System.out.println("Set origin for this street " + newOrigin.x + " / " + newOrigin.y);
			
			if (nextJunction == null) {
				//System.out.println("no next Junction, this is the end of this street");
				return;
			}
			
			// Go to next Street (top, right, bottom)
			if ((nextStreet = nextJunction.getTopStreet()) != null) {
				//System.out.println("Next is top Street of Junction");
				
				if (nextJunction.getOrigin() == null) {
					//System.out.println("Junction has no origin");
					
					nextJunction.setDimension(nextJunction.calcucateDimension());
					
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x + s.getDimension().width, s.getOrigin().y);
					nextJunction.setOrigin(newOrigin);
					//System.out.println("Calculated Origin of Junction " + newOrigin.x + " / " + newOrigin.y);
				}

				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			
			// Go to next Street
			if ((nextStreet = nextJunction.getRightStreet()) != null) {
				//System.out.println("Next is right Street of Junction");
				
				if (nextJunction.getOrigin() == null) {
					//System.out.println("Junction has no origin");
					
					nextJunction.setDimension(nextJunction.calcucateDimension());
					
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x + s.getDimension().width, s.getOrigin().y);
					nextJunction.setOrigin(newOrigin);
					//System.out.println("Calculated Origin of Junction " + newOrigin.x + " / " + newOrigin.y);
				}
				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			
			// Go to next Street
			if ((nextStreet = nextJunction.getBottomStreet()) != null) {
				//System.out.println("Next is bottom Street of Junction");
				
				if (nextJunction.getOrigin() == null) {
					//System.out.println("Junction has no origin");
					
					nextJunction.setDimension(nextJunction.calcucateDimension());
					
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x + s.getDimension().width, s.getOrigin().y + s.getDimension().height);
					nextJunction.setOrigin(newOrigin);
					//System.out.println("Calculated Origin of Junction " + newOrigin.x + " / " + newOrigin.y);
				}
				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			//System.out.println("No more Streets for next junction. Return");
			return;
		}
		
		
	}

}
