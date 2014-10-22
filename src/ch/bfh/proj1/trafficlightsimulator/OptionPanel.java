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
	
	Simulation currentSImulation;
	
	public OptionPanel(Simulation currentSimulation) {
		this.currentSImulation = currentSimulation;
		
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
			ArrayList<Vehicle> vehicles = this.currentSImulation.getVerhicles();
			Vehicle car = new Car();
			
			Route r = this.currentSImulation.getRoutes().get(0);
			Lane l = r.getRoute().getFirst();
			
			car.setRoute(r);
			car.setCurrentLane(l);
			
			vehicles.add(car);
			
			System.out.println("Added car");
			
			
		}

		if (e.getSource().equals(btRunSimulation)) {

			if (currentSImulation.isRunning()) {
				btRunSimulation.setText("Start");
				currentSImulation.stopSimulation();
				btBreak.setEnabled(false);
				

				this.currentSImulation = new Simulation(this.currentSImulation);
				
				
				
			} else {
				btRunSimulation.setText("Stop");
				currentSImulation.startSimulation();
				btBreak.setEnabled(true);
			}
	
		}
		
		if (e.getSource().equals(btBreak)) {
			if (currentSImulation.isBreaking()) {
				currentSImulation.setBreaking(false);
				btBreak.setText("Break");
			} else {
				currentSImulation.setBreaking(true);
				btBreak.setText("Continue");
			}
			
		}
		
	}

}
