package ch.bfh.proj1.trafficlightsimulator;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JPanel;

public class SimPanel extends JPanel {
	
	private Collection<Junction> junctions;
	
	private Collection<Street> streets;
	
	public SimPanel(Collection<Junction> junctions, Collection<Street> streets) {
		super();
		
		this.junctions = junctions;
		this.streets = streets;
		
		setBackground(Color.BLACK);
		
	}
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		/*for (DrawableObject junction : junctions) {
			junction.paintObject(g);
		}*/
		
		for (DrawableObject street : streets) {
			street.paintObject(g);
		}
		
		//ArrayList<Street> myStreets = (ArrayList<Street>) streets;
		//myStreets.get(0).paintObject(g);
		

	}

}
