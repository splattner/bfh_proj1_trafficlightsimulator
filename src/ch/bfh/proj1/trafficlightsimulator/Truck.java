package ch.bfh.proj1.trafficlightsimulator;

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
