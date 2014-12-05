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

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import ch.bfh.proj1.trafficlightsimulator.vehicles.Car;
import ch.bfh.proj1.trafficlightsimulator.vehicles.Truck;


/**
 * @author sebastianplattner
 *
 */
public class TrafficLightSimulator {
	

	/**
	 * The Application Title
	 */
	static public String applicationTitle = "TrafficLightSimulator v1.0";
	
	
	/**
	 * Application Width
	 */
	static public int applicationWidth = 1024;
	
	/**
	 * Application Height
	 */
	static public int applicationHeight = 600;
	
	/**
	 * Number of positions on a street. Each car is on one of these positions
	 */
	static public int defaultPositinOnStreet = 100000;
	
	/**
	 * Street Length in Pixel
	 */
	static public int defaultStreetLenght = 150;
	
	/**
	 * Street Width in Pixel
	 */
	static public int defaultLaneWidth = 15;
	
	/**
	 * Minimum Distance between two Vehicles at max (depening on vehicles speed)
	 */
	static public int minimumDistanceBetweenVehiclesMax = 20000;
	
	/**
	 * Minimum Distance between two Vehicles at min (depening on vehicles speed)
	 */
	static public int minimumDistanceBetweenVehiclesMin = 4000;
	
	/**
	 * Minimum Green Light Phase. Change light, if necessary, after this minimum green light phase
	 */
	static public int minimumGreenLightPhase = 400;
	
	
	/**
	 * The Application Main frame
	 */
	private JFrame mainFrame;
	
	
	/**
	 * An Array List with VehicleRegitry Entries. Each entry represents one type of vehicle
	 */
	private ArrayList<VehicleRegistryEntry> vehicleRegistry;
	

	/**
	 * Return the VehicleRegistry
	 * @return vehicleRegistry
	 */
	public ArrayList<VehicleRegistryEntry> getVehicleRegistry() {
		return vehicleRegistry;
	}


	/**
	 * Set the vehicle Registry
	 * @param vehicleRegistry
	 */
	public void setVehicleRegistry(ArrayList<VehicleRegistryEntry> vehicleRegistry) {
		this.vehicleRegistry = vehicleRegistry;
	}
	
	
	/**
	 * ENUM for all available Traffic Light Modes
	 * manuel = no automatic change, only by user
	 * sequenze = in a predefined sequenze
	 * smart = change traffic lights based on the situation (how many vehicles are waiting)
	 */
	public enum TrafficLightMode {
		manuel,
		sequenze,
		smart
	}
	
	/**
	 * ENUM for available Simulation Mode
	 * manuel = no automatic vehicle adding
	 * automatic = automatic vehicle adding based on speed and distribution
	 *
	 */
	public enum SimulationMode {
		manuel,
		automatic
	}
	
	
	public void refreshWindow() {
		this.mainFrame.invalidate();
		this.mainFrame.repaint();	
	}
	

	private OptionPanel optionPanel;
	private SimPanel simPanel;
	private JScrollPane scrollPanel;
	

	
	private Simulation currentSimulation;
	public Simulation getCurrentSimulation() {return currentSimulation;}
	public void setCurrentSimulation(Simulation currentSimulation) {this.currentSimulation = currentSimulation;}
	
	private TrafficLightMode currentMode = TrafficLightMode.smart;
	public TrafficLightMode getCurrentMode() { return currentMode; }
	public void setCurrentMode(TrafficLightMode currentMode) { this.currentMode = currentMode; }
	
	private SimulationMode simulationMode = SimulationMode.manuel;
	public SimulationMode getSimulationMode() { return simulationMode; }
	public void setSimulationMode(SimulationMode simulationMode) { this.simulationMode = simulationMode;}


	public static void main(String[] args) {

		TrafficLightSimulator app = new TrafficLightSimulator();
		app.init();
		

	}
	
	public TrafficLightSimulator() {
		
		
		// Create new Vehicle Registry and register the current available Vehicles
		// TODO: Dynamically scan available vehicle classess and add them to registry
		this.vehicleRegistry = new ArrayList<VehicleRegistryEntry>();

		this.vehicleRegistry.add(new VehicleRegistryEntry(Car.class, "Car",50));
		this.vehicleRegistry.add(new VehicleRegistryEntry(Truck.class, "Truck",50));
				
	}
	


	/**
	 * Configuration of GUI Elements
	 * Setup of new Simulation
	 * Show GUI
	 * 
	 */
	private void init() {
		mainFrame = new JFrame(TrafficLightSimulator.applicationTitle);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainFrame.setSize(TrafficLightSimulator.applicationWidth, TrafficLightSimulator.applicationHeight);
		
		// Create sim Panel
		simPanel = new SimPanel(this);
		
		// Show Sim Panel in a scroll Panel
		scrollPanel = new JScrollPane(simPanel);
		scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		//Create a new Simulation
		setCurrentSimulation(new Simulation(this));
		
		optionPanel = new OptionPanel(this);
		optionPanel.setSize(250, applicationHeight);
		
		mainFrame.add(optionPanel);
		mainFrame.add(scrollPanel);
		
		mainFrame.setVisible(true);
	}



}
