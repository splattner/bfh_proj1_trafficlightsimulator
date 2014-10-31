package ch.bfh.proj1.trafficlightsimulator;

import java.util.LinkedList;

public class Route {
	
	private LinkedList<Lane> route;
	
	/**
	 * Distribute vehicles based on this distribution on all available routes
	 * Value between 0 and 100
	 * Sum of all distribution of all routes should be 100
	 */
	private int distribution = 0;
	
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
