package ch.bfh.proj1.trafficlightsimulator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class TrafficLightSimulator {
	
	
	static String applicationTitle = "TrafficLightSimulator v0.1";
	static int applicationWidth = 1024;
	static int applicationHeight = 600;
	
	private JFrame mainFrame;
	private JPanel optionPanel;
	private SimPanel simPanel;
	JScrollPane scrollPanel;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		TrafficLightSimulator app = new TrafficLightSimulator();
		app.runGui();
		

	}
	
	private void runGui() {
		mainFrame = new JFrame(TrafficLightSimulator.applicationTitle);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainFrame.setSize(TrafficLightSimulator.applicationWidth, TrafficLightSimulator.applicationHeight);
		
		optionPanel = new JPanel();
		optionPanel.setSize(200, applicationHeight);
		
		
		simPanel = new SimPanel();
		
		scrollPanel = new JScrollPane(simPanel);
		//scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		//scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		
		
		
		
		mainFrame.add(optionPanel);
		mainFrame.add(scrollPanel);
		
		mainFrame.setVisible(true);
	}

}
