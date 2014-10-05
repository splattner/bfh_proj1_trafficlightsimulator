package ch.bfh.proj1.trafficlightsimulator;

import java.awt.Graphics;
import java.awt.Point;

public class Junction implements DrawableObject{
	
	private Street topStreet;
	private Street bottomStreet;
	private Street leftStreet;
	private Street rightStreet;
	
	public Street getTopStreet() {
		return topStreet;
	}
	public void setTopStreet(Street topStreet) {
		this.topStreet = topStreet;
	}
	public Street getBottomStreet() {
		return bottomStreet;
	}
	public void setBottomStreet(Street bottomStreet) {
		this.bottomStreet = bottomStreet;
	}
	public Street getLeftStreet() {
		return leftStreet;
	}
	public void setLeftStreet(Street leftStreet) {
		this.leftStreet = leftStreet;
	}
	public Street getRightStreet() {
		return rightStreet;
	}
	public void setRightStreet(Street rightStreet) {
		this.rightStreet = rightStreet;
	}
	@Override
	public void paintObject(Graphics g) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setOrigin(Point origin) {
		// TODO Auto-generated method stub
		
	}

}
