package ch.bfh.proj1.trafficlightsimulator;

import java.util.ArrayList;

public class Route {
	
	private int id;
	
	private ArrayList<Lane> route;
	
	/**
	 * Distribute vehicles based on this distribution on all available routes
	 * Value between 0 and 100
	 * Sum of all distribution of all routes should be 100
	 */
	private int distribution = 0;
	
	/**
	 * Indicate if a route is visible / drawn on the Sim Panel
	 */
	private boolean visible = false;
	
	public Route(int id) {
		this.route = new ArrayList<Lane>();
		this.id = id;
	}
	
	public void addLane (Lane l) {
		this.route.add(l);
	}
	
	/*
	 * Return all Lanes of this Route
	 */
	public ArrayList<Lane> getLanes() {
		return this.route;
	}

	public int getDistribution() {
		return distribution;
	}

	public void setDistribution(int distribution) {
		this.distribution = distribution;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public int getId() {return id;}

}
