/*
 * Copyright 2014
 * Sebastian Plattner, Donatello Gallucci
 * Bern University of applied Science
 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ch.bfh.proj1.trafficlightsimulator;

import java.util.LinkedList;

import ch.bfh.proj1.trafficlightsimulator.Lane.LaneMarker;

public class Route {
	
	private int id;
	
	private LinkedList<Lane> route;
	
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
		this.route = new LinkedList<Lane>();
		this.id = id;
	}
	
	public void addLane (Lane l) {
		this.route.add(l);
	}
	
	/**
	 * Return all Lanes of this Route
	 */
	public LinkedList<Lane> getLanes() {
		return this.route;
	}

	public int getDistribution() { return distribution;	}
	public void setDistribution(int distribution) {	this.distribution = distribution;}

	public boolean isVisible() { return visible; }
	public void setVisible(boolean visible) { this.visible = visible; }
	
	public int getId() {return id;}
	
	
	/**
	 * Highlight (or de highlight) this lane
	 * @param doHighlight
	 */
	public void hightLight(boolean doHighlight) {
		if (doHighlight) {
			this.highlightLanes(LaneMarker.GREEN);
		} else {
			this.highlightLanes(LaneMarker.NONE);
		}
	}
	
	/**
	 * Highlight all possible lanes you can add to the route
	 * Highlight the lane you can remove from the route (lastOne)
	 * @param highLight
	 */
	private void highlightLanes(LaneMarker newMarker) {
		try {
		  	  Lane lastLane = this.getLanes().get(this.getLanes().size()-1);
		  	  Junction nextJunction = lastLane.getNextJunction();
		  	  
		  	  if (nextJunction != null) {
			    	  for (Lane l : nextJunction.getOutgoingLanes()) {
			    		  l.setCurrentMarker(newMarker);
			    	  }
		  	  }
		  	  
		  	  // Remove marker on last Lane when we dehighlight the lanes
		  	  // Otherwise mark last lane with red
		  	  if (newMarker == LaneMarker.NONE) {
		  		  lastLane.setCurrentMarker(LaneMarker.NONE);
		  	  } else {
		  		lastLane.setCurrentMarker(LaneMarker.RED);
		  	  }
		  	  
		  	  
		} catch (Exception e) {}

	}


}
