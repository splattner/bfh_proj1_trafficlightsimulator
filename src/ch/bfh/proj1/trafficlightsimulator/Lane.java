package ch.bfh.proj1.trafficlightsimulator;

import java.util.Collection;

public class Lane {
	
	public enum laneOrientations {
		startToEnd,
		endToStart
	}
	
	private laneOrientations laneOrientation;
	
	private Collection<Vehicle> verhiclesOnLane;
	
	private TrafficLight trafficLight;

}
