package ch.bfh.proj1.trafficlightsimulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public class Car extends Vehicle {
	
	
	private Point origin;
	private Dimension dimension;
	
	private Color carColor = Color.BLUE;
	
	
	public Car() {
		this.dimension = new Dimension(5,5);
	}

	@Override
	public void paintObject(Graphics g) {
		
		if (this.origin == null) {
			this.setOrigin(new Point(this.getCurrentLane().getOrigin().x + this.getCurrentPosOnLane(), this.getCurrentLane().getOrigin().y + 5));
		}
		
		g.setColor(carColor);
		g.fillRect(origin.x, origin.y, dimension.width, dimension.height);
		
	}

	@Override
	public void setOrigin(Point origin) {
		this.origin = origin;
		
	}

	@Override
	public void setDimension(Dimension dimension) {
		// You cannot set the dimension of a car.
		
	}

	@Override
	public Point getOrigin() {
		return this.origin;
	}

	@Override
	public Dimension getDimension() {
		return this.dimension;
	}

}
