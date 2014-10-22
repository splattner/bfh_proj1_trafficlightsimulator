package ch.bfh.proj1.trafficlightsimulator;

import java.awt.Point;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import ch.bfh.proj1.trafficlightsimulator.Lane.laneOrientations;

public class TrafficLightSimulator {
	
	static public String applicationTitle = "TrafficLightSimulator v0.1";
	static public int applicationWidth = 1024;
	static public int applicationHeight = 600;
	static public int defaultStreetLenght = 150;
	static public int defaultLaneWidth = 15;
	
	private JFrame mainFrame;
	private OptionPanel optionPanel;
	private SimPanel simPanel;
	JScrollPane scrollPanel;
	
	private ArrayList<Junction> junctions;
	private ArrayList<Street> streets;
	private ArrayList<Route> routes;
	private ArrayList<Vehicle> verhicles;
	
	Simulation currentSimulation;


	public static void main(String[] args) {

		
		TrafficLightSimulator app = new TrafficLightSimulator();
		app.init();
		app.runGui();
		

	}
	
	private void init() {
		
			
		junctions = new ArrayList<Junction>();
		streets = new ArrayList<Street>();
		routes = new ArrayList<Route>();
		verhicles = new ArrayList<Vehicle>();
		
		/*
		 * Manually configure Street & Junctions
		 */
		
		Point origin_s1 = new Point(250,200);
		
		
		Junction j1 = new Junction();
		Junction j2 = new Junction();
		Junction j3 = new Junction();
		Street s1 = new Street();
		Street s2 = new Street();
		Street s3 = new Street();
		Street s4 = new Street();
		
		Street s5 = new Street();
		Street s6 = new Street();
		Street s7 = new Street();
		
		Street s8 = new Street();
		Street s9 = new Street();
		Street s10 = new Street();
		
		Lane l1_s1 = new Lane(laneOrientations.startToEnd);
		Lane l2_s1 = new Lane(laneOrientations.endToStart);

		Lane l1_s2 = new Lane(laneOrientations.startToEnd);
		Lane l2_s2 = new Lane(laneOrientations.endToStart);
		
		Lane l1_s3 = new Lane(laneOrientations.startToEnd);
		Lane l2_s3 = new Lane(laneOrientations.endToStart);
		
		Lane l1_s4 = new Lane(laneOrientations.startToEnd);
		Lane l2_s4 = new Lane(laneOrientations.endToStart);
		
		Lane l1_s5 = new Lane(laneOrientations.startToEnd);
		Lane l2_s5 = new Lane(laneOrientations.endToStart);
	
		Lane l1_s6 = new Lane(laneOrientations.startToEnd);
		Lane l2_s6 = new Lane(laneOrientations.endToStart);
		
		Lane l1_s7 = new Lane(laneOrientations.startToEnd);
		Lane l2_s7 = new Lane(laneOrientations.endToStart);
		
		Lane l1_s8 = new Lane(laneOrientations.startToEnd);
		Lane l2_s8 = new Lane(laneOrientations.endToStart);
	
		Lane l1_s9 = new Lane(laneOrientations.startToEnd);
		Lane l2_s9 = new Lane(laneOrientations.endToStart);
	
		Lane l1_s10 = new Lane(laneOrientations.startToEnd);
		Lane l2_s10 = new Lane(laneOrientations.endToStart);
		
		s1.addLane(l1_s1);
		s1.addLane(l2_s1);
		
		s2.addLane(l1_s2);
		s2.addLane(l2_s2);
		
		s3.addLane(l1_s3);
		s3.addLane(l2_s3);
		
		s4.addLane(l1_s4);
		s4.addLane(l2_s4);
		
		s5.addLane(l1_s5);
		s5.addLane(l2_s5);
		
		s6.addLane(l1_s6);
		s6.addLane(l2_s6);
		
		s7.addLane(l1_s7);
		s7.addLane(l2_s7);
		
		s8.addLane(l1_s8);
		s8.addLane(l2_s8);
		
		s9.addLane(l1_s9);
		s9.addLane(l2_s9);
		
		s10.addLane(l1_s10);
		s10.addLane(l2_s10);
		
		s1.setOrigin(origin_s1);

	
		junctions.add(j1);
		junctions.add(j2);
		junctions.add(j3);
		
		streets.add(s1);
		streets.add(s2);
		streets.add(s3);
		streets.add(s4);
		streets.add(s5);
		streets.add(s6);
		streets.add(s7);
		streets.add(s8);
		streets.add(s9);
		streets.add(s10);
		
		j1.setLeftStreet(s1);
		j1.setRightStreet(s2);
		
		s1.setEndJunction(j1);
		s2.setStartJunction(j1);
		
		j2.setLeftStreet(s2);
		j2.setRightStreet(s3);

		s2.setEndJunction(j2);
		s3.setStartJunction(j2);
		
		j3.setLeftStreet(s3);
		j3.setRightStreet(s4);
		
		s3.setEndJunction(j3);
		s4.setStartJunction(j3);
		
		j1.setTopStreet(s5);
		j2.setTopStreet(s6);
		j3.setTopStreet(s7);
		
		j1.setBottomStreet(s8);
		j2.setBottomStreet(s9);
		j3.setBottomStreet(s10);
		
		s5.setEndJunction(j1);
		s6.setEndJunction(j2);
		s7.setEndJunction(j3);
		
		s8.setStartJunction(j1);
		s9.setStartJunction(j2);
		s10.setStartJunction(j3);
		
		
		Junction j4 = new Junction();
		Junction j5 = new Junction();
		Street s11 = new Street();
		
		s8.setEndJunction(j4);
		s9.setEndJunction(j5);
		
		Lane l1_s11 = new Lane(laneOrientations.startToEnd);
		Lane l2_s11 = new Lane(laneOrientations.endToStart);

		
		s11.addLane(l1_s11);
		s11.addLane(l2_s11);
		
		j4.setTopStreet(s8);
		j4.setRightStreet(s11);
		j5.setTopStreet(s9);
		j5.setLeftStreet(s11);
		
		s11.setStartJunction(j4);
		s11.setEndJunction(j5);
		
		streets.add(s11);
		junctions.add(j4);
		junctions.add(j5);
		
		Route route1 = new Route();
		route1.addLane(l1_s1);
		route1.addLane(l1_s2);
		route1.addLane(l1_s3);
		route1.addLane(l1_s4);
		
		routes.add(route1);
	
		
		
		
		
		
		
	}
	
	private void runGui() {
		mainFrame = new JFrame(TrafficLightSimulator.applicationTitle);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainFrame.setSize(TrafficLightSimulator.applicationWidth, TrafficLightSimulator.applicationHeight);
		

		
		
		
		
		simPanel = new SimPanel(junctions, streets);
		
		scrollPanel = new JScrollPane(simPanel);
		//scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		//scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		
		currentSimulation = new Simulation(verhicles, routes, simPanel);
		
		optionPanel = new OptionPanel(currentSimulation);
		optionPanel.setSize(200, applicationHeight);
		
		mainFrame.add(optionPanel);
		mainFrame.add(scrollPanel);
		
		mainFrame.setVisible(true);
	}
	


}
