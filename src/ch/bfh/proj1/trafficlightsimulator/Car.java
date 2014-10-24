package ch.bfh.proj1.trafficlightsimulator;

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
