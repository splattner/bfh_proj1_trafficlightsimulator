package ch.bfh.proj1.trafficlightsimulator;

import java.util.Collection;
import java.util.LinkedList;

public class Lane {
	
	public enum laneOrientations {
		startToEnd,
		endToStart
	}
	

	private laneOrientations laneOrientation;
	
	private LinkedList<Vehicle> verhiclesOnLane;
	
	private TrafficLight trafficLight;
	
	private Street street;
	
	public Lane() {
		this.setVerhiclesOnLane(new LinkedList<Vehicle>());
	}
	
	public Lane(laneOrientations laneOrientation) {
		this.setVerhiclesOnLane(new LinkedList<Vehicle>());
		this.setLaneOrientation(laneOrientation);
	}

	public laneOrientations getLaneOrientation() {
		return laneOrientation;
	}

	public void setLaneOrientation(laneOrientations laneOrientation) {
		this.laneOrientation = laneOrientation;
	}

	public LinkedList<Vehicle> getVerhiclesOnLane() {
		return verhiclesOnLane;
	}

	public void setVerhiclesOnLane(LinkedList<Vehicle> verhiclesOnLane) {
		this.verhiclesOnLane = verhiclesOnLane;
	}

	public Street getStreet() {
		return street;
	}

	public void setStreet(Street street) {
		this.street = street;
	}

}
