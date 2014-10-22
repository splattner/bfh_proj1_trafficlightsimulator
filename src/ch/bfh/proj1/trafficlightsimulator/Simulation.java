package ch.bfh.proj1.trafficlightsimulator;

import java.util.ArrayList;

public class Simulation extends Thread {
	
	private boolean running = false;
	private boolean breakState = false;
	
	private ArrayList<Vehicle> verhicles;
	private ArrayList<Route> routes;
	private SimPanel simPanel;
	
	public Simulation(ArrayList<Vehicle> verhicles, ArrayList<Route> routes, SimPanel simPanel) {
		this.setVerhicles(verhicles);
		this.setRoutes(routes);
		this.setSimPanel(simPanel);
		
	}
	
	public Simulation (Simulation oldSimulation) {
		this.setVerhicles(oldSimulation.getVerhicles());
		this.setRoutes(oldSimulation.getRoutes());
		this.setSimPanel(oldSimulation.getSimPanel());
		
		oldSimulation = null;
		
	}
	
	public void run() {

		
		synchronized (this) {
			while (this.isRunning()) {
				
				System.out.println("SimLoop");
				
				for (Vehicle v : this.verhicles) {
					System.out.println("Simulate Car");
					v.simulationStep();
				}
				
				
				
				System.out.println("Redraw Sim Panel");
				this.simPanel.invalidate();
				this.simPanel.repaint();
				
				
				
				// Wait n ms after each loop
				try {
					this.wait(1000);
				} catch (InterruptedException e1) {

				}
				

				// Check if we should break
				if (this.isBreaking()) {
					System.out.println("Breaking");
					try {
						this.wait();
					} catch (InterruptedException e) {

					}
				}
			
			}
			
		}

		System.out.println("Interrupted");
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

	public ArrayList<Vehicle> getVerhicles() {
		return verhicles;
	}

	public void setVerhicles(ArrayList<Vehicle> verhicles) {
		this.verhicles = verhicles;
	}

	public ArrayList<Route> getRoutes() {
		return routes;
	}

	public void setRoutes(ArrayList<Route> routes) {
		this.routes = routes;
	}

	public SimPanel getSimPanel() {
		return simPanel;
	}

	public void setSimPanel(SimPanel simPanel) {
		this.simPanel = simPanel;
	}

}
