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


import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import ch.bfh.proj1.trafficlightsimulator.TrafficLightSimulator.SimulationMode;
import ch.bfh.proj1.trafficlightsimulator.TrafficLightSimulator.TrafficLightMode;
import ch.bfh.proj1.trafficlightsimulator.vehicles.Vehicle;
import ch.bfh.proj1.trafficlightsimulator.xmlLoader.TrafficLightsXMLHandler;
import ch.bfh.proj1.trafficlightsimulator.xmlLoader.TrafficLightsXMLWriter;

@SuppressWarnings("serial")
public class OptionPanel extends JPanel implements ActionListener, ChangeListener{
	
	private JButton btRunSimulation;
	private JButton btBreak;
	private JButton btShowConfig;
	private JButton btloadXML;
	private JButton btsaveXML;
	
	private JSlider simulationFrequency;
	
	private JComboBox<String> LightModeSelector;
	private JComboBox<String> SimulationModeSelector;
	
	private OptionDialog optionDialog;
	
	private JFileChooser fileChooser;
	private File simulationFile;
	

	private TrafficLightSimulator simulator;
	
	public OptionPanel(TrafficLightSimulator simulator) {
		this.setSimulator(simulator);

		
		if (this.getSimulator().getCurrentSimulation().isLoaded()) {
			optionDialog = new OptionDialog(this.getSimulator());
			optionDialog.pack();
		}
		
		
		this.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		
		c.anchor = GridBagConstraints.NORTH;

		//c.weighty = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		
		


		JLabel lb1 = new JLabel("Simulation Status");
		Font font = lb1.getFont();
		Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
		lb1.setFont(boldFont);
	
		c.gridx = 0;
		c.gridy = 0;


		this.add(lb1, c);
		
		
			
		this.btRunSimulation = new JButton("Start");
		this.btRunSimulation.addActionListener(this);
		
		c.gridx = 0;
		c.gridy = 1;
		
		if (!this.getSimulator().getCurrentSimulation().isLoaded()) {
			this.btRunSimulation.setEnabled(false);
			this.btRunSimulation.setToolTipText("No Simulation loaded");
		}
		
		this.add(btRunSimulation, c);
		
		this.btBreak = new JButton("Break");
		this.btBreak.addActionListener(this);
		this.btBreak.setEnabled(false);
		
		c.gridx = 1;
		c.gridy = 1;
		this.add(btBreak, c);
				
		
		
		JLabel lb2 = new JLabel("Traffic Light Mode");

	
		c.gridx = 0;
		c.gridy = 2;

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
		c.gridy = 2;
		this.add(LightModeSelector, c);
		
		JLabel lb3 = new JLabel("Simulation Mode");

		
		c.gridx = 0;
		c.gridy = 3;

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
		c.gridy = 3;
		this.add(SimulationModeSelector, c);
		
		this.btShowConfig = new JButton("Simulation Control");
		this.btShowConfig.addActionListener(this);
		
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		
		if (!this.getSimulator().getCurrentSimulation().isLoaded()) {
			this.btShowConfig.setEnabled(false);
			this.btShowConfig.setToolTipText("No Simulation loaded");
		}
		this.add(this.btShowConfig,c);
		
		JLabel lb4 = new JLabel("Vehicle Injection Speed");

		
		c.gridx = 0;
		c.gridy = 5;

		this.add(lb4, c);
		
		this.simulationFrequency = new JSlider(0,100,20);
		this.simulationFrequency.setMajorTickSpacing(10);
		this.simulationFrequency.setMinorTickSpacing(5);
		this.simulationFrequency.setPaintTicks(true);
		this.simulationFrequency.setLabelTable(this.simulationFrequency.createStandardLabels(10));
		this.simulationFrequency.setPaintLabels(true);
		this.simulationFrequency.addChangeListener(this);
		
		c.gridx = 0;
		c.gridy = 6;
		

		this.add(this.simulationFrequency, c);
		
		this.btloadXML = new JButton("Load Scenario");
		this.btloadXML.addActionListener(this);
		
		c.gridx = 0;
		c.gridy = 7;
		c.gridwidth = 1;
		this.add(btloadXML, c);
		
		this.btsaveXML = new JButton("Save Scenario");
		this.btsaveXML.addActionListener(this);
		
		c.gridx = 1;
		c.gridy = 7;
		this.add(btsaveXML, c);

		
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// Save a XML File
		if (e.getSource().equals(this.btsaveXML))
		{

			
			this.fileChooser = new JFileChooser();
			
			if (this.simulationFile != null) {
				this.fileChooser.setCurrentDirectory(this.simulationFile);
			}
			
			FileNameExtensionFilter filter = new FileNameExtensionFilter("XML file .xml", "xml");
			fileChooser.setFileFilter(filter);
		    int returnVal = fileChooser.showSaveDialog(this);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	this.simulationFile = this.fileChooser.getSelectedFile();
		    	
				TrafficLightsXMLWriter txmlw = new TrafficLightsXMLWriter(
						this.simulationFile.getAbsolutePath(),
						this.getSimulator().getCurrentSimulation().getJunctions(),
						this.getSimulator().getCurrentSimulation().getStreets(),
						this.getSimulator().getCurrentSimulation().getRoutes());
		    }
		}
		    
		// Load a XML File and configure Simulation
		if (e.getSource().equals(this.btloadXML))
		{
			this.fileChooser = new JFileChooser();
			
			if (this.simulationFile != null) {
				this.fileChooser.setCurrentDirectory(this.simulationFile);
			}
			
			FileNameExtensionFilter filter = new FileNameExtensionFilter("XML file .xml", "xml");
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setFileFilter(filter);
		    int returnVal = fileChooser.showOpenDialog(this);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	this.simulationFile = this.fileChooser.getSelectedFile();
		    	
				TrafficLightsXMLHandler txmlh = new TrafficLightsXMLHandler(this.simulationFile.getAbsolutePath());

				
				this.getSimulator().getCurrentSimulation().setJunctions(txmlh.getJunctions());
				this.getSimulator().getCurrentSimulation().setStreets(txmlh.getStreets());
				this.getSimulator().getCurrentSimulation().setRoutes(txmlh.getRoutes());
				
				this.getSimulator().getCurrentSimulation().setVerhicles(new ArrayList<Vehicle>());
				
				((LinkedList<Street>)this.getSimulator().getCurrentSimulation().getStreets()).get(0).setOrigin(new Point(300,200));
				
				this.getSimulator().getCurrentSimulation().initOrigins();
				
				this.optionDialog = new OptionDialog(this.getSimulator());
				this.optionDialog.pack();
				
				this.getSimulator().getCurrentSimulation().setLoaded(true);
				
				this.btShowConfig.setEnabled(true);
				this.btShowConfig.setToolTipText("");
				this.btRunSimulation.setEnabled(true);
				this.btRunSimulation.setToolTipText("");
	
				// Redraw Sim Panel
				this.getSimulator().refreshWindow();

		    	
		    }
		}
		
		if (e.getSource().equals(this.btShowConfig)) {
			if (this.optionDialog != null) {
				this.optionDialog.setVisible(true);
			}
			
		}
		
		if (e.getSource().equals(SimulationModeSelector)) {

			switch (SimulationModeSelector.getSelectedIndex()) {
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

				// Create a new Simulation (based on the old one)
				this.simulator.setCurrentSimulation(new Simulation(this.simulator.getCurrentSimulation()));
				
				// Redraw Sim Panel
				this.getSimulator().refreshWindow();

			} else {
				if (this.getSimulator().getCurrentSimulation().isLoaded()) {
					btRunSimulation.setText("Stop");
					this.simulator.getCurrentSimulation().startSimulation();
					btBreak.setEnabled(true);
				}
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

	public TrafficLightSimulator getSimulator() { return simulator; }

	public void setSimulator(TrafficLightSimulator simulator) {	this.simulator = simulator; }

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource().equals(this.simulationFrequency)) {
			this.getSimulator().getCurrentSimulation().setSimulationSpeed(this.simulationFrequency.getValue());
		}
		
	}

}
