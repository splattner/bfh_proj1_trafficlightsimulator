package ch.bfh.proj1.trafficlightsimulator;

import java.util.ArrayList;
import java.util.Random;

import ch.bfh.proj1.trafficlightsimulator.TrafficLightSimulator.SimulationMode;

public class Simulation extends Thread {
	
	private boolean running = false;
	private boolean breakState = false;
	
	private TrafficLightSimulator simulator;
	
	private Random rand = new Random();
	
	// Speed for autimatic Simulation mode
	private int simulationSpeed = 20;
	private int currentStep = 0;
	
	public Simulation(ArrayList<Vehicle> verhicles, ArrayList<Route> routes, TrafficLightSimulator simulator) {

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
				
				
				
				
				for (Vehicle v : this.getSimulator().getVerhicles()) {
					v.simulationStep();
				}
				
				
				
				for (Junction j : this.getSimulator().getJunctions()) {
					j.simulationStep(this.getSimulator().getCurrentMode());
				}
				
				
				//System.out.println("Redraw Sim Panel");
				this.getSimulator().getMainFrame().invalidate();
				this.getSimulator().getMainFrame().repaint();
				
				
				
				
				
				// Add Vehicles if in automatic mode
				if (this.getSimulationSpeed() > 0 && this.currentStep == 0 && this.getSimulator().getSimulationMode() == SimulationMode.automatic) {
					
					// TODO: Frequency of adding Vehicles!!
					
					//System.out.println("Automatic mode");
					
					int randNumberVehicle = this.rand.nextInt(100);
					int randNumberRoute = this.rand.nextInt(100);
					int currentVehicleDist = 0;
					int nextVehicleDist = 0;
					
					int currentRouteDist = 0;
					int nextRouteDist = 0;
					
					// Loop trought all Vehicles
					for (VehicleRegistryEntry ve : this.getSimulator().getVehicleRegistry()) {
						nextVehicleDist += ve.getDistribution();
						
						// if random was for this vehicle
						if (randNumberVehicle > currentVehicleDist && randNumberVehicle <= nextVehicleDist) {
							
							int i = 0;
							// Loop trought all routes
							for (Route r : this.getSimulator().getRoutes()) {
								nextRouteDist += r.getDistribution();
								
								// Random was for this route
								if (randNumberRoute > currentRouteDist && randNumberRoute <= nextRouteDist) {
									System.out.println("Add a : " + ve.getVehicleName() + " to Route " + (i+1));
									
									
									Vehicle v = null;
									try {
										v = (Vehicle) ve.getVehicle().newInstance();
									} catch (InstantiationException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IllegalAccessException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									if (v != null) {
										v.setRoute(r);
										v.setCurrentLane(r.getRoute().getFirst());
										
										this.getSimulator().getVerhicles().add(v);
									}
									
								}
								
								i++;
								
								currentRouteDist += r.getDistribution();
								
							}
							
						}
						
						currentVehicleDist += ve.getDistribution();
						

						
						
					}	
					
				}
				
				currentStep++;
				
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

}
