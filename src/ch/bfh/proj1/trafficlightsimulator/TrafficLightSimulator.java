package ch.bfh.proj1.trafficlightsimulator;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

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
		
			
//		junctions = new ArrayList<Junction>();
//		streets = new ArrayList<Street>();
//		routes = new ArrayList<Route>();
		verhicles = new ArrayList<Vehicle>();
		
		TrafficLightsXMLHandler txmlh = new TrafficLightsXMLHandler("C:\\Users\\HP\\Documents\\eclipse workspace\\bfh_proj1_trafficlightsimulator\\TrafficLightsConfig1.xml");//TODO:put string variable from filechoser instead of hardtext
		junctions = txmlh.getJunctions();
		streets = txmlh.getStreets();
		routes = txmlh.getRoutes();
		
//		((ArrayList<Street>)streets).get(0).setOrigin(new Point(300,200));
		
		((List<Street>) streets).get(0).setOrigin(new Point(300,200));
	
		
		/*
		 * Manually configure Street & Junctions
		 */		

	
//		Junction j1 = new Junction(1);
//		Junction j2 = new Junction(2);
//		Junction j3 = new Junction(3);
//		Junction j4 = new Junction(4);
//		Junction j5 = new Junction(5);
//		
//		junctions.add(j1);
//		junctions.add(j2);
//		junctions.add(j3);
//		junctions.add(j4);
//		junctions.add(j5);
//	
//		Street s1 = new Street(1);
//		s1.setOrientaion(Street.orientation.horizontal);
//		Street s2 = new Street(2);
//		s2.setOrientaion(Street.orientation.horizontal);
//		Street s3 = new Street(3);
//		s3.setOrientaion(Street.orientation.horizontal);
//		Street s4 = new Street(4);
//		s4.setOrientaion(Street.orientation.horizontal);
//		Street s5 = new Street(5);
//		s5.setOrientaion(Street.orientation.vertical);
//		Street s6 = new Street(6);
//		s6.setOrientaion(Street.orientation.vertical);
//		Street s7 = new Street(7);
//		s7.setOrientaion(Street.orientation.vertical);
//		Street s8 = new Street(8);
//		s8.setOrientaion(Street.orientation.vertical);
//		Street s9 = new Street(9);
//		s9.setOrientaion(Street.orientation.vertical);
//		Street s10 = new Street(10);
//		s10.setOrientaion(Street.orientation.vertical);
//		
//		Lane l1_s1 = new Lane(1,Lane.laneOrientations.endToStart);
//		Lane l2_s1 = new Lane(2,Lane.laneOrientations.startToEnd);
//
//		Lane l1_s2 = new Lane(3,Lane.laneOrientations.endToStart);
//		Lane l2_s2 = new Lane(4,Lane.laneOrientations.startToEnd);
//
//		Lane l1_s3 = new Lane(5,Lane.laneOrientations.endToStart);
//		Lane l2_s3 = new Lane(6,Lane.laneOrientations.startToEnd);
//
//		Lane l1_s4 = new Lane(7,Lane.laneOrientations.endToStart);
//		Lane l2_s4 = new Lane(8,Lane.laneOrientations.startToEnd);
//
//		Lane l1_s5 = new Lane(9,Lane.laneOrientations.startToEnd);
//		Lane l2_s5 = new Lane(10,Lane.laneOrientations.endToStart);
//
//		Lane l1_s6 = new Lane(11,Lane.laneOrientations.startToEnd);
//		Lane l2_s6 = new Lane(12,Lane.laneOrientations.endToStart);
//		
//		Lane l1_s7 = new Lane(13,Lane.laneOrientations.startToEnd);
//		Lane l2_s7 = new Lane(14,Lane.laneOrientations.endToStart);
//		
//		Lane l1_s8 = new Lane(15,Lane.laneOrientations.startToEnd);
//		Lane l2_s8 = new Lane(16,Lane.laneOrientations.endToStart);
//		
//		Lane l1_s9 = new Lane(17,Lane.laneOrientations.startToEnd);
//		Lane l2_s9 = new Lane(18,Lane.laneOrientations.endToStart);
//		
//		Lane l1_s10 = new Lane(19,Lane.laneOrientations.startToEnd);
//		Lane l2_s10 = new Lane(20,Lane.laneOrientations.endToStart);
//
//
//
//		s1.addLane(l1_s1);
//		s1.addLane(l2_s1);
//		
//		s2.addLane(l1_s2);
//		s2.addLane(l2_s2);
//		
//		s3.addLane(l1_s3);
//		s3.addLane(l2_s3);
//		
//		s4.addLane(l1_s4);
//		s4.addLane(l2_s4);
//		
//		s5.addLane(l1_s5);
//		s5.addLane(l2_s5);
//		
//		s6.addLane(l1_s6);
//		s6.addLane(l2_s6);
//		
//		s7.addLane(l1_s7);
//		s7.addLane(l2_s7);
//		
//		s8.addLane(l1_s8);
//		s8.addLane(l2_s8);
//		
//		s9.addLane(l1_s9);
//		s9.addLane(l2_s9);
//		
//		s10.addLane(l1_s10);
//		s10.addLane(l2_s10);
//	
//		streets.add(s1);
//		streets.add(s2);
//		streets.add(s3);
//		streets.add(s4);
//		streets.add(s5);
//		streets.add(s6);
//		streets.add(s7);
//		streets.add(s8);
//		streets.add(s9);
//		streets.add(s10);
//
//		j1.setLeftStreet(s1);
//		j1.setRightStreet(s2);
//		
//		s1.setEndJunction(j1);
//		s2.setStartJunction(j1);
//		
//		j2.setLeftStreet(s2);
//		j2.setRightStreet(s3);
//
//		s2.setEndJunction(j2);
//		s3.setStartJunction(j2);
//		
//		j3.setLeftStreet(s3);
//		j3.setRightStreet(s4);
//		
//		s3.setEndJunction(j3);
//		s4.setStartJunction(j3);
//		
//		j1.setTopStreet(s5);
//		j2.setTopStreet(s6);
//		j3.setTopStreet(s7);
//		
//		j1.setBottomStreet(s8);
//		j2.setBottomStreet(s9);
//		j3.setBottomStreet(s10);
//		
//		s5.setEndJunction(j1);
//		s6.setEndJunction(j2);
//		s7.setEndJunction(j3);
//		
//		s8.setStartJunction(j1);
//		s9.setStartJunction(j2);
//		s10.setStartJunction(j3);
//		
//		Street s11 = new Street(11);
//		s11.setOrientaion(Street.orientation.horizontal);
//		streets.add(s11);
//		
//		s8.setEndJunction(j4);
//		s9.setEndJunction(j5);
//		
//
//		Lane l1_s11 = new Lane(21,Lane.laneOrientations.endToStart);
//		Lane l2_s11 = new Lane(22,Lane.laneOrientations.startToEnd);
//
//		s11.addLane(l1_s11);
//		s11.addLane(l2_s11);
//		
//		j4.setTopStreet(s8);
//		j4.setRightStreet(s11);
//		j5.setTopStreet(s9);
//		j5.setLeftStreet(s11);
//		
//		s11.setStartJunction(j4);
//		s11.setEndJunction(j5);
//		
//		Route route1 = new Route(1);
//		route1.setDistribution(25);
//		route1.addLane(l2_s1);
//		route1.addLane(l2_s2);
//		route1.addLane(l2_s3);
//		route1.addLane(l2_s4);
//		
//		Route route2 = new Route(2);
//		route2.setDistribution(25);
//		route2.addLane(l1_s4);
//		route2.addLane(l1_s3);
//		route2.addLane(l1_s2);
//		route2.addLane(l1_s1);
//		
//		Route route3 = new Route(3);
//		route3.setDistribution(25);
//		route3.addLane(l1_s5);
//		route3.addLane(l2_s2);
//		route3.addLane(l1_s9);
//		
//		Route route4 = new Route(4);
//		route4.setDistribution(25);
//		route4.addLane(l2_s1);
//		route4.addLane(l1_s8);
//		route4.addLane(l2_s11);
//		route4.addLane(l2_s9);
//		route4.addLane(l2_s3);
//		route4.addLane(l1_s10);
//
//		
//		routes.add(route1);
//		routes.add(route2);
//		routes.add(route3);
//		routes.add(route4);
		
//		s1.setOrigin(origin_s1);
	
		
	}
	
	private void runGui() {
		mainFrame = new JFrame(TrafficLightSimulator.applicationTitle);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainFrame.setSize(TrafficLightSimulator.applicationWidth, TrafficLightSimulator.applicationHeight);
		
		simPanel = new SimPanel(this);
		
		scrollPanel = new JScrollPane(simPanel);
		//scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		//scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		
		setCurrentSimulation(new Simulation(verhicles, (ArrayList<Route>) routes, this));
		
		optionPanel = new OptionPanel(this);
		optionPanel.setSize(250, applicationHeight);
		
		mainFrame.add(optionPanel);
		mainFrame.add(scrollPanel);
		
		mainFrame.setVisible(true);
	}



}
