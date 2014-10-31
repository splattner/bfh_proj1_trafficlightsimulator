package ch.bfh.proj1.trafficlightsimulator;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ch.bfh.proj1.trafficlightsimulator.TrafficLightSimulator.SimulationMode;
import ch.bfh.proj1.trafficlightsimulator.TrafficLightSimulator.TrafficLightMode;

public class OptionPanel extends JPanel implements ActionListener{
	
	JButton btRunSimulation;
	JButton btBreak;
	JButton btShowConfig;
	
	
	JComboBox<String> LightModeSelector;
	JComboBox<String> SimulationModeSelector;
	
	OptionDialog optionDialog;
	

	private TrafficLightSimulator simulator;
	
	public OptionPanel(TrafficLightSimulator simulator) {
		this.setSimulator(simulator);
		
		
		optionDialog = new OptionDialog(this.getSimulator());
		optionDialog.pack();
		
		
		this.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		
		//c.anchor = GridBagConstraints.PAGE_START;
		//c.gridwidth = 2;
		//c.gridheight = 5;
		
		


		JLabel lb1 = new JLabel("Simulation Status");
		Font font = lb1.getFont();
		Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
		lb1.setFont(boldFont);
	
		c.gridx = 0;
		c.gridy = 0;

		c.fill = GridBagConstraints.HORIZONTAL;

		this.add(lb1, c);
		
		
			
		btRunSimulation = new JButton("Start");
		btRunSimulation.addActionListener(this);
		
		c.gridx = 0;
		c.gridy = 1;
		this.add(btRunSimulation, c);
		
		btBreak = new JButton("Break");
		btBreak.addActionListener(this);
		btBreak.setEnabled(false);
		
		c.gridx = 1;
		c.gridy = 1;
		this.add(btBreak, c);
				
		
		
		JLabel lb2 = new JLabel("Traffic Light Mode");

	
		c.gridx = 0;
		c.gridy = 4;

		this.add(lb2, c);
		
		String[] lightModeOptions = {"manual","sequence","smart"};
		
		LightModeSelector = new JComboBox<String>(lightModeOptions);
		
		if (this.getSimulator().getCurrentMode() == TrafficLightMode.manuel) {
			LightModeSelector.setSelectedIndex(0);
		}
		if (this.getSimulator().getCurrentMode() == TrafficLightMode.sequenze) {
			LightModeSelector.setSelectedIndex(1);
		}
		if (this.getSimulator().getCurrentMode() == TrafficLightMode.smart) {
			LightModeSelector.setSelectedIndex(2);
		}
		
		LightModeSelector.addActionListener(this);
		
		c.gridx = 1;
		c.gridy = 4;
		this.add(LightModeSelector, c);
		
		JLabel lb3 = new JLabel("Simulation Mode");

		
		c.gridx = 0;
		c.gridy = 5;

		this.add(lb3, c);
		
		String[] simulationModeOptions = {"manual","automatic"};
		
		SimulationModeSelector = new JComboBox<String>(simulationModeOptions);
		
		if (this.getSimulator().getSimulationMode() == SimulationMode.manuel) {
			SimulationModeSelector.setSelectedIndex(0);
		}
		if (this.getSimulator().getSimulationMode() == SimulationMode.automatic) {
			SimulationModeSelector.setSelectedIndex(1);
		}

		
		SimulationModeSelector.addActionListener(this);
		
		c.gridx = 1;
		c.gridy = 5;
		this.add(SimulationModeSelector, c);
		
		this.btShowConfig = new JButton("Simulation Control");
		this.btShowConfig.addActionListener(this);
		
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 2;
		this.add(this.btShowConfig,c);

		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource().equals(this.btShowConfig)) {
			this.optionDialog.setVisible(true);
			
		}
		
		if (e.getSource().equals(SimulationModeSelector)) {
			switch (LightModeSelector.getSelectedIndex()) {
			case 0:
				this.getSimulator().setSimulationMode(SimulationMode.manuel);
				break;
			case 1:
				this.getSimulator().setSimulationMode(SimulationMode.automatic);
				break;

			}
			
		}
		
		if (e.getSource().equals(LightModeSelector)) {
			switch (LightModeSelector.getSelectedIndex()) {
			case 0:
				this.getSimulator().setCurrentMode(TrafficLightMode.manuel);
				break;
			case 1:
				this.getSimulator().setCurrentMode(TrafficLightMode.sequenze);
				break;
			case 2:
				this.getSimulator().setCurrentMode(TrafficLightMode.smart);
				break;
			}
			
		}
		
		

		if (e.getSource().equals(btRunSimulation)) {

			if (this.simulator.getCurrentSimulation().isRunning()) {
				btRunSimulation.setText("Start");
				this.simulator.getCurrentSimulation().stopSimulation();
				btBreak.setEnabled(false);
				
				// Remove all cars from Lanes (if they are still on a lane)
				for (Vehicle v : this.getSimulator().getVerhicles()) {
					
					if (v.getCurrentLane() != null)
						v.getCurrentLane().getVerhiclesOnLane().remove(v);
					
					// Dereference, so Garbage Collector can remove the object
					v = null;
					
				}
				
				// Remove all Vehicles
				this.getSimulator().getVerhicles().clear();

				// Create a new Simulation (based on the old one)
				this.simulator.setCurrentSimulation(new Simulation(this.simulator.getCurrentSimulation()));
				
				// Redraw
				this.getSimulator().getMainFrame().invalidate();
				this.getSimulator().getMainFrame().repaint();
				
				
				
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
