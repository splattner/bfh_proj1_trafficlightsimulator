package ch.bfh.proj1.trafficlightsimulator;

import java.util.ArrayList;

public class Simulation extends Thread {
	
	private boolean running = false;
	private boolean breakState = false;
	
	private TrafficLightSimulator simulator;
	
	public Simulation(ArrayList<Vehicle> verhicles, ArrayList<Route> routes, TrafficLightSimulator simulator) {

		this.setSimulator(simulator);
		
	}
	
	public Simulation (Simulation oldSimulation) {

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
				
				
				// Wait n ms after each loop
				try {
					this.wait(10);
				} catch (InterruptedException e1) {

				}
				

				// Check if we should break
				if (this.isBreaking()) {
					//System.out.println("Breaking");
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

}
