package ch.bfh.proj1.trafficlightsimulator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JPanel;


/**
 * @author Sebastian Plattner
 *
 */
public class SimPanel extends JPanel implements MouseListener{
	

	/*
	 * Reference to the Traffic Light Simulator
	 */
	private TrafficLightSimulator simulator;
	
	
	/**
	 * Return the Traffic Light Simulator
	 * 
	 * @return simulator
	 */
	public TrafficLightSimulator getSimulator() {
		return simulator;
	}

	/**
	 * Set Traffic Light Simulator
	 * @param simulator
	 */
	public void setSimulator(TrafficLightSimulator simulator) {
		this.simulator = simulator;
	}

	
	public SimPanel(TrafficLightSimulator simulator) {
		super();
		
		this.setSimulator(simulator);
		
		
		// Mouse Listener to react on mouse clickes in the Sim Panwl
		this.addMouseListener(this);
		
	}
	
	
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		
		// Change Color of Backgrund when Simulation is running
		if (this.getSimulator().getCurrentSimulation().isRunning()) {
			if (this.getSimulator().getCurrentSimulation().isBreaking()) {
				g.setColor(new Color(100,100,100));
			} else {
				g.setColor(new Color(100,0,0));
			}
			
		} else {
			g.setColor(Color.BLACK);
		}
		
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		
		if (!this.getSimulator().getCurrentSimulation().isLoaded()) {
			// If Simulation is not loaded, then don't paint anything
			return;
		}

		
		// Draw all Streets
		for (Street street : this.getSimulator().getStreets()) {

			street.paintObject(g);
			
			
			// Draw Vehicles on Lane
			for (Lane l : street.getLanes()) {
				for (Vehicle v : l.getVerhiclesOnLane()) {

					v.paintObject(g);
				}
			}
			
		}
		
		// Draw all Junctions
		for (Junction j : this.getSimulator().getJunctions()) {
			j.paintObject(g);
		}
		
		
		// Draw all visible Routes
		g.setColor(Color.RED);
		
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
     
		
		for (Route r : this.getSimulator().getRoutes()) {
			if (r.isVisible()) {
				for (Lane l : r.getLanes()) {
					if (l.getStreet().getOrientaion() == Street.orientation.horizontal) {
						g2d.drawLine(l.getOrigin().x, l.getOrigin().y + 7, l.getOrigin().x + l.getDimension().width, l.getOrigin().y + 7);
					} else {
						g2d.drawLine(l.getOrigin().x + 7, l.getOrigin().y, l.getOrigin().x + 7, l.getOrigin().y + l.getDimension().height);
					}
					
				}
			}
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		// Only allow interction with traffic lights in manuel mode
		if (this.getSimulator().getCurrentMode() == TrafficLightSimulator.TrafficLightMode.manuel) {
			for (Junction j : this.getSimulator().getJunctions()) {
				
				// Change a single light
				for (TrafficLight tf : j.getTrafficLights()) {
					if (e.getX() >= tf.getOrigin().x && e.getX() < tf.getOrigin().x + tf.getDimension().width &&
							e.getY() >= tf.getOrigin().y && e.getY() < tf.getOrigin().y + tf.getDimension().height) {
						if (tf.getCurrentStatus() == TrafficLight.trafficLightStatus.GREEN) {
							tf.setCurrentStatus(TrafficLight.trafficLightStatus.RED);
						} else {
							tf.setCurrentStatus(TrafficLight.trafficLightStatus.GREEN);
						}
						
						
					}
					
				}
				
				// Change all light of a Junction
				if (e.getX() >= j.getOrigin().x && e.getX() < j.getOrigin().x + j.getDimension().width &&
					e.getY() >= j.getOrigin().y && e.getY() < j.getOrigin().y + j.getDimension().height) {
					
					for (TrafficLight tf : j.getTrafficLights()) {
						if (tf.getCurrentStatus() == TrafficLight.trafficLightStatus.GREEN) {
							tf.setCurrentStatus(TrafficLight.trafficLightStatus.RED);
						} else {
							tf.setCurrentStatus(TrafficLight.trafficLightStatus.GREEN);
						}
					}
				}
			}
		}
		
		// Redraw
		this.getSimulator().getMainFrame().invalidate();
		this.getSimulator().getMainFrame().repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
	
	}

	@Override
	public void mouseExited(MouseEvent e) {
	
	}


}
