package ch.bfh.proj1.trafficlightsimulator;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import xmlLoader.TrafficLightsXMLHandler;


public class TrafficLightSimulator {
	
	static public String applicationTitle = "TrafficLightSimulator v0.1";
	static public int applicationWidth = 1024;
	static public int applicationHeight = 600;
	
	static public int defaultPositinOnStreet = 100000;
	static public int defaultStreetLenght = 150;
	static public int defaultLaneWidth = 15;
	
	static public int minimumDistanceBetweenVehiclesMax = 20000;
	static public int minimumDistanceBetweenVehiclesMin = 4000;
	
	static public int minimumGreenLightPhase = 400;
	
	private JFrame mainFrame;
	
	private ArrayList<VehicleRegistryEntry> vehicleRegistry;
	
	public ArrayList<VehicleRegistryEntry> getVehicleRegistry() {
		return vehicleRegistry;
	}

	public void setVehicleRegistry(ArrayList<VehicleRegistryEntry> vehicleRegistry) {
		this.vehicleRegistry = vehicleRegistry;
	}
	
	public enum TrafficLightMode {
		manuel,
		sequenze,
		smart
	}
	
	public enum SimulationMode {
		manuel,
		automatic
	}
	
	public JFrame getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(JFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	private OptionPanel optionPanel;
	private SimPanel simPanel;
	private JScrollPane scrollPanel;
	
	private Collection<Junction> junctions;

	
	public Collection<Junction> getJunctions() {return junctions;}

	public void setJunctions(Collection<Junction> junctions) {this.junctions = junctions;}

	private Collection<Street> streets;

	public void setStreets(Collection<Street> streets) {this.streets = streets;}

	public Collection<Street> getStreets() {return streets;}

	private Collection<Route> routes;
	
	public Collection<Route> getRoutes() {return routes;}

	public void setRoutes(Collection<Route> routes) {this.routes = routes;}
	
	private ArrayList<Vehicle> verhicles;
	
	public ArrayList<Vehicle> getVerhicles() {return verhicles;}

	public void setVerhicles(ArrayList<Vehicle> verhicles) {this.verhicles = verhicles;}
	
	private Simulation currentSimulation;
	
	public Simulation getCurrentSimulation() {
		return currentSimulation;
	}

	public void setCurrentSimulation(Simulation currentSimulation) {
		this.currentSimulation = currentSimulation;
	}
	
	private TrafficLightMode currentMode = TrafficLightMode.smart;
	
	public TrafficLightMode getCurrentMode() {
		return currentMode;
	}

	public void setCurrentMode(TrafficLightMode currentMode) {
		this.currentMode = currentMode;
	}
	
	private SimulationMode simulationMode = SimulationMode.manuel;
	
	public SimulationMode getSimulationMode() {
		return simulationMode;
	}

	public void setSimulationMode(SimulationMode simulationMode) {
		this.simulationMode = simulationMode;
	}


	public static void main(String[] args) {

		
		TrafficLightSimulator app = new TrafficLightSimulator();
		app.init();
		app.runGui();
		

	}
	
	public TrafficLightSimulator() {
		
		this.vehicleRegistry = new ArrayList<VehicleRegistryEntry>();
		
		this.vehicleRegistry.add(new VehicleRegistryEntry(Car.class, "Car",50));
		this.vehicleRegistry.add(new VehicleRegistryEntry(Truck.class, "Truck",50));
		
		
		
	}
	


	private void init() {
		
		/*
		 * Some Remarks:
		 * - Start of a street is always on left/top side
		 * - End of a street is always on right/bottom side
		 * - Very first street need to start with start == null and end != null and with a origin
		 *   and has to be on left or top side of a junction
		 *   otherwise calculation of origin for the other streets does not work!
		 */
		

		/*
		TrafficLightsXMLHandler txmlh = new TrafficLightsXMLHandler("TrafficLightsConfig1.xml");

		
		junctions = txmlh.getJunctions();
		streets = txmlh.getStreets();
		routes = txmlh.getRoutes();
		
		verhicles = new ArrayList<Vehicle>();
		
		((List<Street>)streets).get(0).setOrigin(new Point(300,200));
		
		*/
		
//		junctions = new ArrayList<Junction>();
//		streets = new ArrayList<Street>();
//		routes = new ArrayList<Route>();			
		
	
		
	}
	
	private void runGui() {
		mainFrame = new JFrame(TrafficLightSimulator.applicationTitle);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainFrame.setSize(TrafficLightSimulator.applicationWidth, TrafficLightSimulator.applicationHeight);
		
		simPanel = new SimPanel(this);
		
		scrollPanel = new JScrollPane(simPanel);
		//scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		//scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		
		setCurrentSimulation(new Simulation(this));
		
		optionPanel = new OptionPanel(this);
		optionPanel.setSize(250, applicationHeight);
		
		mainFrame.add(optionPanel);
		mainFrame.add(scrollPanel);
		
		mainFrame.setVisible(true);
	}



}
