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

package ch.bfh.proj1.trafficlightsimulator.vehicles;

import java.awt.Color;
import java.awt.Dimension;

public class Truck extends Vehicle {
	
	public Truck() {
		this.dimension = new Dimension(20,7);
		this.carColor = Color.ORANGE;
		
		this.accelerationMaxPositiv = 10;
		this.accelerationMaxNegativ = 15;
		this.accelerationStartPositiv = 3;
		this.accelerationStartNegativ = 7;
		this.accelerationStepPositiv = 1;
		this.accelerationStepNegativ = 1;
		
		this.maxSpeed = 300;
	}
}
