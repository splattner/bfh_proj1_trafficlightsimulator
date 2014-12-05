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

/**
 * This is a Vehicle Registry Entry.
 * Each entry represents one type of vehicle
 *
 */
public class VehicleRegistryEntry {
	
	private Class<?> vehicle;
	
	private int distribution;
	
	private String vehicleName;
	
	public VehicleRegistryEntry(Class<?> vehicle, String vehicleName, int distribution) {
		this.setVehicle(vehicle);
		this.setVehicleName(vehicleName);
		this.setDistribution(distribution);
	}

	public Class<?> getVehicle() {
		return vehicle;
	}

	public void setVehicle(Class<?> vehicle) {
		this.vehicle = vehicle;
	}

	public int getDistribution() {
		return distribution;
	}

	public void setDistribution(int distribution) {
		this.distribution = distribution;
	}

	public String getVehicleName() {
		return vehicleName;
	}

	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
	}

}
