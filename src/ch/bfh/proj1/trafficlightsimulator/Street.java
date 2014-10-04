package ch.bfh.proj1.trafficlightsimulator;

import java.awt.Graphics;
import java.util.Collection;

public class Street implements DrawableObject{
	
	private int lenhgt;
	private Collection<Lane> lanes;
	
	private Junction startJunction;
	private Junction endJunction;

	@Override
	public void paintObject(Graphics g) {
		// TODO Auto-generated method stub
		
	}

}
