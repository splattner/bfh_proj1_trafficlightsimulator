package ch.bfh.proj1.trafficlightsimulator;

import java.util.Collection;
import java.util.LinkedList;

public class Lane {
	
	public enum laneOrientations {
		startToEnd,
		endToStart
	}
	

	private laneOrientations laneOrientation;
	
	private Collection<Vehicle> verhiclesOnLane;
	
	private TrafficLight trafficLight;
	
	public Lane() {
		this.verhiclesOnLane = new LinkedList<Vehicle>();
	}

	public laneOrientations getLaneOrientation() {
		return laneOrientation;
	}

	public void setLaneOrientation(laneOrientations laneOrientation) {
		this.laneOrientation = laneOrientation;
	}

}
