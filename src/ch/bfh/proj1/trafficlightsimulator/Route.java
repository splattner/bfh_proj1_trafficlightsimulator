package ch.bfh.proj1.trafficlightsimulator;

import java.util.LinkedList;

public class Route {
	
	private LinkedList<Lane> route;
	
	public Route() {
		this.route = new LinkedList<Lane>();
	}
	
	public void addLane (Lane l) {
		this.route.add(l);
	}
	
	public LinkedList<Lane> getRoute() {
		return this.route;
	}

}
