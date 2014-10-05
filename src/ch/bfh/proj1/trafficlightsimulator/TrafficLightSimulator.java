package ch.bfh.proj1.trafficlightsimulator;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import ch.bfh.proj1.trafficlightsimulator.Lane.laneOrientations;

public class TrafficLightSimulator {
	
	static public String applicationTitle = "TrafficLightSimulator v0.1";
	static public int applicationWidth = 1024;
	static public int applicationHeight = 600;
	static public int defaultStreetLenght = 150;
	static public int defaultLaneWidth = 15;
	
	private JFrame mainFrame;
	private JPanel optionPanel;
	private SimPanel simPanel;
	JScrollPane scrollPanel;
	
	private ArrayList<Junction> junctions;
	private ArrayList<Street> streets;
	
	private boolean simState;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		TrafficLightSimulator app = new TrafficLightSimulator();
		app.init();
		app.runGui();
		

	}
	
	private void init() {
		
		simState = false;
		
		junctions = new ArrayList<Junction>();
		streets = new ArrayList<Street>();
		
		/*
		 * Manually configure Street & Junctions
		 */
		
		Point origin_s1 = new Point(250,200);
		
		/*
		Point origin_s2 = new Point(430,200);
		Point origin_s3 = new Point(610,200);
		Point origin_s4 = new Point(790,200);
		
		Point origin_s5 = new Point(400,50);
		Point origin_s6 = new Point(580,50);
		Point origin_s7 = new Point(760,50);
		
		Point origin_s8 = new Point(400,230);
		Point origin_s9 = new Point(580,230);
		Point origin_s10 = new Point(760,230);
		*/
		
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
		
		Lane l1_s1 = new Lane();
		Lane l2_s1 = new Lane();
		
		l1_s1.setLaneOrientation(laneOrientations.startToEnd);
		l2_s1.setLaneOrientation(laneOrientations.endToStart);
		
		Lane l1_s2 = new Lane();
		Lane l2_s2 = new Lane();
		
		l1_s2.setLaneOrientation(laneOrientations.startToEnd);
		l2_s2.setLaneOrientation(laneOrientations.endToStart);
		
		Lane l1_s3 = new Lane();
		Lane l2_s3 = new Lane();
		
		l1_s2.setLaneOrientation(laneOrientations.startToEnd);
		l2_s2.setLaneOrientation(laneOrientations.endToStart);
		
		Lane l1_s4 = new Lane();
		Lane l2_s4 = new Lane();
		
		l1_s4.setLaneOrientation(laneOrientations.startToEnd);
		l2_s4.setLaneOrientation(laneOrientations.endToStart);
		
		Lane l1_s5 = new Lane();
		Lane l2_s5 = new Lane();
		
		l1_s5.setLaneOrientation(laneOrientations.startToEnd);
		l2_s5.setLaneOrientation(laneOrientations.endToStart);
		
		Lane l1_s6 = new Lane();
		Lane l2_s6 = new Lane();
		
		l1_s6.setLaneOrientation(laneOrientations.startToEnd);
		l2_s6.setLaneOrientation(laneOrientations.endToStart);
		
		Lane l1_s7 = new Lane();
		Lane l2_s7 = new Lane();
		
		l1_s4.setLaneOrientation(laneOrientations.startToEnd);
		l2_s4.setLaneOrientation(laneOrientations.endToStart);
		
		Lane l1_s8 = new Lane();
		Lane l2_s8 = new Lane();
		
		l1_s8.setLaneOrientation(laneOrientations.startToEnd);
		l2_s8.setLaneOrientation(laneOrientations.endToStart);
		
		Lane l1_s9 = new Lane();
		Lane l2_s9 = new Lane();
		
		l1_s9.setLaneOrientation(laneOrientations.startToEnd);
		l2_s9.setLaneOrientation(laneOrientations.endToStart);
		
		Lane l1_s10 = new Lane();
		Lane l2_s10 = new Lane();
		
		l1_s10.setLaneOrientation(laneOrientations.startToEnd);
		l2_s10.setLaneOrientation(laneOrientations.endToStart);
		
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
		/*s2.setOrigin(origin_s2);
		s3.setOrigin(origin_s3);
		s4.setOrigin(origin_s4);
		s5.setOrigin(origin_s5);
		s6.setOrigin(origin_s6);
		s7.setOrigin(origin_s7);
		s8.setOrigin(origin_s8);
		s9.setOrigin(origin_s9);
		s10.setOrigin(origin_s10);*/
		
		
		
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
	}
	
	private void runGui() {
		mainFrame = new JFrame(TrafficLightSimulator.applicationTitle);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainFrame.setSize(TrafficLightSimulator.applicationWidth, TrafficLightSimulator.applicationHeight);
		
		optionPanel = new JPanel();
		optionPanel.setSize(200, applicationHeight);
		
		
		simPanel = new SimPanel(junctions, streets);
		
		scrollPanel = new JScrollPane(simPanel);
		//scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		//scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		
		mainFrame.add(optionPanel);
		mainFrame.add(scrollPanel);
		
		mainFrame.setVisible(true);
	}
	
	private void simLoop() {
		
		
	}

}
