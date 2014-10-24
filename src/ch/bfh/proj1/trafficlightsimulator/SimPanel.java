package ch.bfh.proj1.trafficlightsimulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

public class SimPanel extends JPanel{
	
	private ArrayList<Junction> junctions;
	
	private ArrayList<Street> streets;
	
	public SimPanel(ArrayList<Junction> junctions, ArrayList<Street> streets) {
		super();
		
		this.junctions = junctions;
		this.streets = streets;
		
		setBackground(Color.BLACK);
		
		initOrigins();
		
	}
	
	private void initOrigins() {

		// Start with first street (the one very left/top and should have no start junction
		Street s = streets.get(0);
		
		System.out.println("Startin with Street " + s.getOrigin().x + " / " + s.getOrigin().y);
		Junction j;
		int numOfLanes = 0;
		Dimension d;
		Point newOrigin;
		Street nextStreet = null;
		
		// Check if this is a horizontal or vertical steet
		if (s.getEndJunction() != null) {
			
			j = s.getEndJunction();
			numOfLanes = s.getLanes().size();
			
			// Check if street is connected to left side
			if (j.getLeftStreet().equals(s)) {
				// Its a horizontal street
				System.out.println("Horizontal Street");
				
				
				d = new Dimension();
				d.setSize(TrafficLightSimulator.defaultStreetLenght, numOfLanes * TrafficLightSimulator.defaultLaneWidth);
				s.setDimension(d);
				System.out.println("Calculated Dimension for Street " + d.getWidth() + " * " + d.getHeight());
				
				
				// Set Origin for Junction
				newOrigin = new Point();
				newOrigin.setLocation(s.getOrigin().x + s.getDimension().width, s.getOrigin().y);
				j.setOrigin(newOrigin);
				System.out.println("Calculated Origin of Junction " + newOrigin.x + " / " + newOrigin.y);
				
				
				j.setDimension(j.calcucateDimension());
				System.out.println("Calculated Dimension of Junction " + j.getDimension().getWidth() + " * " + j.getDimension().getHeight());
				
				// Go to next Street (top, right, bottom)
				if ((nextStreet = j.getTopStreet()) != null) {
					System.out.println("Next Street is top");
					calcucalteOriginAndDimension(nextStreet, j);
				}
				
				// Go to next Street
				if ((nextStreet = j.getRightStreet()) != null) {
					System.out.println("Next street is right");
					calcucalteOriginAndDimension(nextStreet, j);
				}
				
				// Go to next Street
				if ((nextStreet = j.getBottomStreet()) != null) {
					System.out.println("Next street is bottom");
					calcucalteOriginAndDimension(nextStreet, j);
				}
				
			} else if ((j.getTopStreet().equals(s))) {
				// Its a vertical street
				System.out.println("Vertical Street");
				
				d = new Dimension();
				d.setSize(numOfLanes * TrafficLightSimulator.defaultLaneWidth, TrafficLightSimulator.defaultStreetLenght);
				s.setDimension(d);
				System.out.println("Calculated Dimension for Street " + d.getWidth() + " * " + d.getHeight());
				
				// Set Origin for Junction
				newOrigin = new Point();
				newOrigin.setLocation(s.getOrigin().x , s.getOrigin().y + s.getDimension().height);
				j.setOrigin(newOrigin);
				System.out.println("Calculated Origin of Junction " + newOrigin.x + " / " + newOrigin.y);
				
				
				j.setDimension(j.calcucateDimension());
				System.out.println("Calculated Dimension of Junction " + j.getDimension().getWidth() + " * " + j.getDimension().getHeight());
				
				// Go to next Street (left, bottom, right)
				if ((nextStreet = j.getLeftStreet()) != null) {
					System.out.println("Next Street is left");
					calcucalteOriginAndDimension(nextStreet, j);
				}
				
				// Go to next Street
				if ((nextStreet = j.getBottomStreet()) != null) {
					System.out.println("Next Street is bottom");
					calcucalteOriginAndDimension(nextStreet, j);
				}
				
				// Go to next Street
				if ((nextStreet = j.getRightStreet()) != null) {
					System.out.println("Next Street is right");
					calcucalteOriginAndDimension(nextStreet, j);
				}
			
			}
			
		}
	}
	
	private void calcucalteOriginAndDimension(Street s, Junction lastJunction) {
		
		Junction nextJunction = null;
		Street nextStreet = null;
		Point newOrigin = new Point();
		Dimension newDimension = new Dimension();
		int numOfLanes;
		
		if (s.getOrigin() != null) {
			System.out.println("Street already calcucaled");
			// Already calcucaled
			return;
		}
		
	
		numOfLanes = s.getLanes().size();
		
		// Check if Last Junction was start or End
		if (s.getStartJunction() != null && s.getStartJunction().equals(lastJunction)){
			System.out.println("Last Junction was start for this street");
			// This Street starts at last one
			nextJunction = s.getEndJunction();
			
			
		} else {
			if (s.getEndJunction() != null && s.getEndJunction().equals(lastJunction)) {
				System.out.println("Last Junction was end for this street");
				// This Street ends at last one
				nextJunction = s.getStartJunction();	
			}
		}

		
		// Check orientation for street
		if (lastJunction.getTopStreet() != null && lastJunction.getTopStreet().equals(s)) {
			// it was a top street
			System.out.println("This Street is top of last Junction");

			newDimension.setSize(numOfLanes * TrafficLightSimulator.defaultLaneWidth, TrafficLightSimulator.defaultStreetLenght);
			s.setDimension(newDimension);
			System.out.println("Set Dimension for this street " + newDimension.width + " * " + newDimension.height);
			
			newOrigin.setLocation(lastJunction.getOrigin().x, lastJunction.getOrigin().y - newDimension.height);
			s.setOrigin(newOrigin);
			System.out.println("Set origin for this street " + newOrigin.x + " / " + newOrigin.y);
			
			if (nextJunction == null) {
				System.out.println("no next Junction, this is the end of this street");
				return;
			}
			
			// Go to next Street (left, top, right)
			if ((nextStreet = nextJunction.getLeftStreet()) != null) {
				System.out.println("Next is left Street of Junction");
				
				if (nextJunction.getOrigin() == null) {
					System.out.println("Junction has no origin");
					
					nextJunction.setDimension(nextJunction.calcucateDimension());
					
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x - s.getDimension().width - nextJunction.getDimension().width, s.getOrigin().y);
					nextJunction.setOrigin(newOrigin);
					System.out.println("Calculated Origin of Junction " + newOrigin.x + " / " + newOrigin.y);
				}

				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			
			// Go to next Street
			if ((nextStreet = nextJunction.getTopStreet()) != null) {
				System.out.println("Next is top Street of Junction");
				
				
				if (nextJunction.getOrigin() == null) {
					System.out.println("Junction has no origin");
					
					nextJunction.setDimension(nextJunction.calcucateDimension());
					
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x + s.getDimension().width, s.getOrigin().y);
					nextJunction.setOrigin(newOrigin);
					System.out.println("Calculated Origin of Junction " + newOrigin.x + " / " + newOrigin.y);
				}
				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			
			// Go to next Street
			if ((nextStreet = nextJunction.getRightStreet()) != null) {
				System.out.println("Next is right Street of Junction");
				
				if (nextJunction.getOrigin() == null) {
					System.out.println("Junction has no origin");
					
					nextJunction.setDimension(nextJunction.calcucateDimension());
					
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x + s.getDimension().width, s.getOrigin().y);
					nextJunction.setOrigin(newOrigin);
					System.out.println("Calculated Origin of Junction " + newOrigin.x + " / " + newOrigin.y);
				}
				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			
			System.out.println("No more Streets for next junction. Return");
			
			return;
			
			
		}
		
		// Check orientation for street
		if (lastJunction.getBottomStreet() != null && lastJunction.getBottomStreet().equals(s)) {
			// it was a bottom street
			System.out.println("This Street is bottom of last Junction");
			
			newDimension.setSize(numOfLanes * TrafficLightSimulator.defaultLaneWidth, TrafficLightSimulator.defaultStreetLenght);
			s.setDimension(newDimension);
			System.out.println("Set Dimension for this street " + newDimension.width + " * " + newDimension.height);
			newOrigin.setLocation(lastJunction.getOrigin().x, lastJunction.getOrigin().y + lastJunction.getDimension().height);
			s.setOrigin(newOrigin);
			System.out.println("Set origin for this street " + newOrigin.x + " / " + newOrigin.y);
			
			
			if (nextJunction == null) {
				System.out.println("no next Junction, this is the end of this street");
				return;
			}
			
			// Go to next Street (left, bottom, right)
			if ((nextStreet = nextJunction.getLeftStreet()) != null) {
				System.out.println("Next is left Street of Junction");
				
				if (nextJunction.getOrigin() == null) {
					System.out.println("Junction has no origin");
					
					nextJunction.setDimension(nextJunction.calcucateDimension());
					
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x, s.getOrigin().y + s.getDimension().height);
					nextJunction.setOrigin(newOrigin);
					System.out.println("Calculated Origin of Junction " + newOrigin.x + " / " + newOrigin.y);
				}
				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			
			// Go to next Street
			if ((nextStreet = nextJunction.getBottomStreet()) != null) {
				System.out.println("Next is bottom Street of Junction");
				
				if (nextJunction.getOrigin() == null) {
					System.out.println("Junction has no origin");
					
					nextJunction.setDimension(nextJunction.calcucateDimension());
					
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x + s.getDimension().width, s.getOrigin().y + s.getDimension().height);
					nextJunction.setOrigin(newOrigin);
					System.out.println("Calculated Origin of Junction " + newOrigin.x + " / " + newOrigin.y);
				}
				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			
			// Go to next Street
			if ((nextStreet = nextJunction.getRightStreet()) != null) {
				System.out.println("Next is right Street of Junction");
				
				if (nextJunction.getOrigin() == null) {
					System.out.println("Junction has no origin");
					
					nextJunction.setDimension(nextJunction.calcucateDimension());
					
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x + s.getDimension().width, s.getOrigin().y);
					nextJunction.setOrigin(newOrigin);
					System.out.println("Calculated Origin of Junction " + newOrigin.x + " / " + newOrigin.y);
				}
				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			
			System.out.println("No more Streets for next junction. Return");
			return;
		}
		
		// Check orientation for street
		if (lastJunction.getLeftStreet() != null && lastJunction.getLeftStreet().equals(s)) {
			// it was a left street
			System.out.println("This Street is left of last Junction");
			
			newDimension.setSize(TrafficLightSimulator.defaultStreetLenght, numOfLanes * TrafficLightSimulator.defaultLaneWidth);
			s.setDimension(newDimension);
			System.out.println("Set Dimension for this street " + newDimension.width + " * " + newDimension.height);
			newOrigin.setLocation(lastJunction.getOrigin().x  - newDimension.width, lastJunction.getOrigin().y);
			s.setOrigin(newOrigin);
			System.out.println("Set origin for this street " + newOrigin.x + " / " + newOrigin.y);
			
			
			if (nextJunction == null) {
				System.out.println("no next Junction, this is the end of this street");
				return;
			}
			
			// Go to next Street (top, left, bottom)
			if ((nextStreet = nextJunction.getTopStreet()) != null) {
				System.out.println("Next is top Street of Junction");
				
				if (nextJunction.getOrigin() == null) {
					System.out.println("Junction has no origin");
					
					nextJunction.setDimension(nextJunction.calcucateDimension());
					
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x - nextJunction.getDimension().width, s.getOrigin().y);
					nextJunction.setOrigin(newOrigin);
					System.out.println("Calculated Origin of Junction " + newOrigin.x + " / " + newOrigin.y);
				}
				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			
			// Go to next Street
			if ((nextStreet = nextJunction.getLeftStreet()) != null) {
				System.out.println("Next is left Street of Junction");
				
				if (nextJunction.getOrigin() == null) {
					System.out.println("Junction has no origin");
					
					nextJunction.setDimension(nextJunction.calcucateDimension());
					
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x - s.getDimension().width - nextJunction.getDimension().width, s.getOrigin().y);
					nextJunction.setOrigin(newOrigin);
					System.out.println("Calculated Origin of Junction " + newOrigin.x + " / " + newOrigin.y);
				}
				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			
			// Go to next Street
			if ((nextStreet = nextJunction.getBottomStreet()) != null) {
				System.out.println("Next is bottom Street of Junction");
				
				if (nextJunction.getOrigin() == null) {
					System.out.println("Junction has no origin");
					
					nextJunction.setDimension(nextJunction.calcucateDimension());
					
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x + s.getDimension().width, s.getOrigin().y + s.getDimension().height);
					nextJunction.setOrigin(newOrigin);
					System.out.println("Calculated Origin of Junction " + newOrigin.x + " / " + newOrigin.y);
				}
				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			System.out.println("No more Streets for next junction. Return");
			return;
		}
		
		// Check orientation for street
		if (lastJunction.getRightStreet() != null &&  lastJunction.getRightStreet().equals(s)) {
			// it was a right street
			System.out.println("This Street is right of last Junction");
			
			newDimension.setSize(TrafficLightSimulator.defaultStreetLenght, numOfLanes * TrafficLightSimulator.defaultLaneWidth);
			s.setDimension(newDimension);
			System.out.println("Set Dimension for this street " + newDimension.width + " * " + newDimension.height);
			newOrigin.setLocation(lastJunction.getOrigin().x + lastJunction.getDimension().width, lastJunction.getOrigin().y);
			s.setOrigin(newOrigin);
			
			System.out.println("Set origin for this street " + newOrigin.x + " / " + newOrigin.y);
			
			if (nextJunction == null) {
				System.out.println("no next Junction, this is the end of this street");
				return;
			}
			
			// Go to next Street (top, right, bottom)
			if ((nextStreet = nextJunction.getTopStreet()) != null) {
				System.out.println("Next is top Street of Junction");
				
				if (nextJunction.getOrigin() == null) {
					System.out.println("Junction has no origin");
					
					nextJunction.setDimension(nextJunction.calcucateDimension());
					
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x + s.getDimension().width, s.getOrigin().y);
					nextJunction.setOrigin(newOrigin);
					System.out.println("Calculated Origin of Junction " + newOrigin.x + " / " + newOrigin.y);
				}

				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			
			// Go to next Street
			if ((nextStreet = nextJunction.getRightStreet()) != null) {
				System.out.println("Next is right Street of Junction");
				
				if (nextJunction.getOrigin() == null) {
					System.out.println("Junction has no origin");
					
					nextJunction.setDimension(nextJunction.calcucateDimension());
					
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x + s.getDimension().width, s.getOrigin().y);
					nextJunction.setOrigin(newOrigin);
					System.out.println("Calculated Origin of Junction " + newOrigin.x + " / " + newOrigin.y);
				}
				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			
			// Go to next Street
			if ((nextStreet = nextJunction.getBottomStreet()) != null) {
				System.out.println("Next is bottom Street of Junction");
				
				if (nextJunction.getOrigin() == null) {
					System.out.println("Junction has no origin");
					
					nextJunction.setDimension(nextJunction.calcucateDimension());
					
					newOrigin = new Point();
					newOrigin.setLocation(s.getOrigin().x + s.getDimension().width, s.getOrigin().y + s.getDimension().height);
					nextJunction.setOrigin(newOrigin);
					System.out.println("Calculated Origin of Junction " + newOrigin.x + " / " + newOrigin.y);
				}
				
				calcucalteOriginAndDimension(nextStreet, nextJunction);
			}
			System.out.println("No more Streets for next junction. Return");
			return;
		}
		
		
	}
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		// Draw all Streets
		for (Street street : streets) {

			street.paintObject(g);
			
			
			// Draw Vehicles on Lane
			for (Lane l : street.getLanes()) {
				for (Vehicle v : l.getVerhiclesOnLane()) {

					v.paintObject(g);
				}
			}
			
		}
		
		for (Junction j : junctions) {
			j.paintObject(g);
		}
	}


}
