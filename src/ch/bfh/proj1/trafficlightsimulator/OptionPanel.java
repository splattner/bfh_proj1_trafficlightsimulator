package ch.bfh.proj1.trafficlightsimulator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

public class OptionPanel extends JPanel implements ActionListener{
	
	JButton btRunSimulation;
	JButton btBreak;
	
	
	// For temp purpose only
	JButton btAddCar;
	

	private TrafficLightSimulator simulator;
	
	public OptionPanel(TrafficLightSimulator simulator) {
		this.setSimulator(simulator);
		
		btRunSimulation = new JButton("Start");
		btRunSimulation.addActionListener(this);
		
		btBreak = new JButton("Break");
		btBreak.addActionListener(this);
		btBreak.setEnabled(false);
		
		btAddCar = new JButton("Add Car");
		btAddCar.addActionListener(this);
		
		
		
		this.add(btRunSimulation);
		this.add(btBreak);
		this.add(btAddCar);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		if (e.getSource().equals(btAddCar)) {
			ArrayList<Vehicle> vehicles = this.simulator.getCurrentSimulation().getVerhicles();
			Vehicle v = new Car();
			//Vehicle v = new Truck();
			
			Route r = this.simulator.getCurrentSimulation().getRoutes().get(2);
			Lane l = r.getRoute().getFirst();
			
			v.setRoute(r);
			v.setCurrentLane(l);
			
			vehicles.add(v);
			
		}

		if (e.getSource().equals(btRunSimulation)) {

			if (this.simulator.getCurrentSimulation().isRunning()) {
				btRunSimulation.setText("Start");
				this.simulator.getCurrentSimulation().stopSimulation();
				btBreak.setEnabled(false);
				
				// Remove all cars from Lanes (if they are still on a lane)
				for (Vehicle v : this.simulator.getCurrentSimulation().getVerhicles()) {
					
					if (v.getCurrentLane() != null)
						v.getCurrentLane().getVerhiclesOnLane().remove(v);
					
					// Dereference, so Garbage Collector can remove the object
					v = null;
					
				}
				
				// Remove all Vehicles
				this.simulator.getCurrentSimulation().getVerhicles().clear();

				// Create a new Simulation (based on the old one)
				this.simulator.setCurrentSimulation(new Simulation(this.simulator.getCurrentSimulation()));
				
				
				
			} else {
				btRunSimulation.setText("Stop");
				this.simulator.getCurrentSimulation().startSimulation();
				btBreak.setEnabled(true);
			}
	
		}
		
		if (e.getSource().equals(btBreak)) {
			if (this.simulator.getCurrentSimulation().isBreaking()) {
				this.simulator.getCurrentSimulation().setBreaking(false);
				btBreak.setText("Break");
			} else {
				this.simulator.getCurrentSimulation().setBreaking(true);
				btBreak.setText("Continue");
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
