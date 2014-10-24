package ch.bfh.proj1.trafficlightsimulator;

import java.awt.Color;
import java.awt.Dimension;


public class Car extends Vehicle {
	

	public Car() {
		this.dimension = new Dimension(10,5);
		this.carColor = Color.BLUE;
		
		this.accelerationMaxPositiv = 15;
		this.accelerationMaxNegativ = 15;
		this.maxSpeed = 500;
		
	}




}
