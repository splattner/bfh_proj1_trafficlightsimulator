package ch.bfh.proj1.trafficlightsimulator;

import java.util.LinkedList;

public class Route {
	
	private LinkedList<Lane> route;
	
	private int distribution = 10;
	
	public Route() {
		this.route = new LinkedList<Lane>();
	}
	
	public void addLane (Lane l) {
		this.route.add(l);
	}
	
	public LinkedList<Lane> getRoute() {
		return this.route;
	}

	public int getDistribution() {
		return distribution;
	}

	public void setDistribution(int distribution) {
		this.distribution = distribution;
	}

}
