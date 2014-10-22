package ch.bfh.proj1.trafficlightsimulator;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public class Junction implements DrawableObject{
	
	private Street topStreet;
	private Street bottomStreet;
	private Street leftStreet;
	private Street rightStreet;
	
	private Point origin;
	private Dimension dimension;
	
	public Street getTopStreet() {return topStreet;}
	
	public void setTopStreet(Street topStreet) {this.topStreet = topStreet;}
	
	public Street getBottomStreet() {return bottomStreet;}
	
	public void setBottomStreet(Street bottomStreet) {this.bottomStreet = bottomStreet;}
	
	public Street getLeftStreet() {return leftStreet;}
	
	public void setLeftStreet(Street leftStreet) {this.leftStreet = leftStreet;}
	
	public Street getRightStreet() {return rightStreet;}
	
	public void setRightStreet(Street rightStreet) {this.rightStreet = rightStreet;}
	
	@Override
	public void paintObject(Graphics g) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setOrigin(Point origin) {this.origin = origin;}
	
	@Override
	public void setDimension(Dimension dimension) {this.dimension = dimension;}
	
	@Override
	public Point getOrigin() {return this.origin;}
	
	@Override
	public Dimension getDimension() {return this.dimension;}
	
	public Dimension calcucateDimension() {
		Dimension d = new Dimension();
		int numOfLanesVertical = 0;
		int numOfLanesHorizontal = 0;
		
		if (this.getTopStreet() != null) {
			numOfLanesVertical = this.getTopStreet().getLanes().size();
		} else {
			if (this.getBottomStreet() != null) {
				numOfLanesVertical = this.getBottomStreet().getLanes().size();
			}
		}
		
		if (this.getLeftStreet() != null) {
			numOfLanesVertical = this.getLeftStreet().getLanes().size();
		} else {
			if (this.getRightStreet() != null) {
				numOfLanesVertical = this.getRightStreet().getLanes().size();
			} 
		}
		
		if (numOfLanesHorizontal == 0) {
			numOfLanesHorizontal = numOfLanesVertical;
		}
		
		if (numOfLanesVertical == 0) {
			numOfLanesVertical = numOfLanesHorizontal;
		}
		
		d.setSize(numOfLanesHorizontal * TrafficLightSimulator.defaultLaneWidth, numOfLanesVertical * TrafficLightSimulator.defaultLaneWidth);
		
		return d;
	}

}
