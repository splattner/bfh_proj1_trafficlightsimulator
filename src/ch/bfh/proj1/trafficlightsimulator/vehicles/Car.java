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


public class Car extends Vehicle {
	

	public Car() {
		this.dimension = new Dimension(10,5);
		this.carColor = Color.BLUE;
		
		this.accelerationMaxPositiv = 15;
		this.accelerationMaxNegativ = 25;
		this.accelerationStartPositiv = 5;
		this.accelerationStartNegativ = 10;
		this.accelerationStepPositiv = 2;
		this.accelerationStepNegativ = 3;
		
		this.maxSpeed = 500;
		
	}




}
