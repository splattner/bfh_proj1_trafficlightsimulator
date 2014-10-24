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
	JButton btAddCar1;
	JButton btAddCar2;
	JButton btLight;
	

	private TrafficLightSimulator simulator;
	
	public OptionPanel(TrafficLightSimulator simulator) {
		this.setSimulator(simulator);
		
		btRunSimulation = new JButton("Start");
		btRunSimulation.addActionListener(this);
		
		btBreak = new JButton("Break");
		btBreak.addActionListener(this);
		btBreak.setEnabled(false);
		
		btAddCar1 = new JButton("Add Car");
		btAddCar1.addActionListener(this);
		btAddCar2 = new JButton("Add Truck");
		btAddCar2.addActionListener(this);
		
		btLight = new JButton("Set Green");
		btLight.addActionListener(this);
		
		
		
		this.add(btRunSimulation);
		this.add(btBreak);
		this.add(btAddCar1);
		this.add(btAddCar2);
		this.add(btLight);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		if (e.getSource().equals(btAddCar1)) {
			ArrayList<Vehicle> vehicles = this.simulator.getCurrentSimulation().getVerhicles();
			Vehicle v = new Car();
			//Vehicle v = new Truck();
			
			Route r = this.simulator.getCurrentSimulation().getRoutes().get(2);
			Lane l = r.getRoute().getFirst();
			
			v.setRoute(r);
			v.setCurrentLane(l);
			
			vehicles.add(v);
			
		}
		
		if (e.getSource().equals(btAddCar2)) {
			ArrayList<Vehicle> vehicles = this.simulator.getCurrentSimulation().getVerhicles();
			Vehicle v = new Truck();
			//Vehicle v = new Truck();
			
			Route r = this.simulator.getCurrentSimulation().getRoutes().get(2);
			Lane l = r.getRoute().getFirst();
			
			v.setRoute(r);
			v.setCurrentLane(l);
			
			vehicles.add(v);
			
		}
		
		if (e.getSource().equals(btLight)) {
			if(btLight.getText().equals("Set Green")) {
				btLight.setText("Set Red");
				
				for (Street s : this.simulator.getStreets()) {
					for (Lane l : s.getLanes()) {
						l.getTrafficLight().setCurrentStatus(TrafficLight.trafficLightStatus.GREEN);
					}
				}
				
			} else {
				btLight.setText("Set Green");

				for (Street s : this.simulator.getStreets()) {
					for (Lane l : s.getLanes()) {
						l.getTrafficLight().setCurrentStatus(TrafficLight.trafficLightStatus.RED);
					}
				}
			}
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
