/*
 * Copyright 2014
 * Sebastian Plattner, Donatello Gallucci
 * Bern University of applied Science

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ch.bfh.proj1.trafficlightsimulator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

import javax.swing.JPanel;
import ch.bfh.proj1.trafficlightsimulator.vehicles.Vehicle;



@SuppressWarnings("serial")
public class SimPanel extends JPanel implements MouseListener, MouseMotionListener{


	/**
	 * Reference to the Traffic Light Simulator
	 */
	private TrafficLightSimulator simulator;


	/**
	 * Return the Traffic Light Simulator
	 * 
	 * @return simulator
	 */
	public TrafficLightSimulator getSimulator() { return simulator; }

	/**
	 * Set Traffic Light Simulator
	 * @param simulator
	 */
	public void setSimulator(TrafficLightSimulator simulator) { this.simulator = simulator; }


	public SimPanel(TrafficLightSimulator simulator) {
		super();

		this.setSimulator(simulator);


		// Mouse Listener to react on mouse clickes in the Sim Panwl
		this.addMouseListener(this);
		this.addMouseMotionListener(this);

	}


	/**
	 * Paint all elements on the Sim Panel (Street, Lanes, Junctions, Traffic Lights, Vehicles)
	 */
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		// Change Color of Background when Simulation is running
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



		// Change Color of Background when in drag mode
		if (this.mouse_pressed) {
			g.setColor(new Color(50,50,50));
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}


		g.setColor(Color.white);
		g.drawString("Drag & Drop for moving around the simulation", 270, 30);



		// Draw all Streets
		for (Street street : this.getSimulator().getCurrentSimulation().getStreets()) {
			
			// Paint the Steet
			street.paintObject(g);

			// Draw Vehicles on Lane
			for (Lane l : street.getLanes()) {
				for (Vehicle v : l.getVerhiclesOnLane()) {
					// Paint the vehicle
					v.paintObject(g);
				}
			}

		}

		// Draw all Junctions
		for (Junction j : this.getSimulator().getCurrentSimulation().getJunctions()) {
			j.paintObject(g);
		}

		// Draw all visible Routes if simulation is not running
		g.setColor(Color.RED);

		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));


		for (Route r : this.getSimulator().getCurrentSimulation().getRoutes()) {
			if (r.isVisible()) {
				for (Lane l : r.getLanes()) {
					if (l.getStreet().isHorizontal()) {
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
			for (Junction j : this.getSimulator().getCurrentSimulation().getJunctions()) {

				// Change a single light
				for (TrafficLight tf : j.getTrafficLights()) {
					if (e.getX() >= tf.getOrigin().x && e.getX() < tf.getOrigin().x + tf.getDimension().width &&
							e.getY() >= tf.getOrigin().y && e.getY() < tf.getOrigin().y + tf.getDimension().height) {
						if (tf.isGreen()) {
							tf.setRed();
						} else {
							tf.setGreen();
						}


					}

				}

				// Change all light of a Junction
				if (e.getX() >= j.getOrigin().x && e.getX() < j.getOrigin().x + j.getDimension().width &&
						e.getY() >= j.getOrigin().y && e.getY() < j.getOrigin().y + j.getDimension().height) {

					for (TrafficLight tf : j.getTrafficLights()) {
						if (tf.isGreen()) {
							tf.setRed();
						} else {
							tf.setGreen();
						}
					}
				}
			}
		}

		/*
		 * Check if we clicked on a lane
		 * Only if simulation is not running
		 */
		if (!this.getSimulator().getCurrentSimulation().isRunning() && this.getSimulator().getCurrentSimulation().isLoaded()) {
			for (Street s : this.getSimulator().getCurrentSimulation().getStreets()) {
				for (Lane l : s.getLanes()) {
					if (e.getX() >= l.getOrigin().x && e.getX() < l.getOrigin().x + l.getDimension().width &&
							e.getY() >= l.getOrigin().y && e.getY() < l.getOrigin().y + l.getDimension().height) {

						// Find the route wich the lane belongs to
						for (Route r : this.getSimulator().getCurrentSimulation().getRoutes()) {
							if (r.isVisible()) {

								LinkedList<Lane> lanes = r.getLanes();
								Lane lastLane;
								try {
									lastLane = lanes.get(lanes.size()-1);
								} catch (Exception outofBound) {
									lastLane = null;
								}

								// Check if this route has the clicked lane

								// Remove lane if it was the lastLane in route
								if (lastLane != null && lanes.contains(l) && l == lastLane) {
									// You can only remove the last one

									r.hightLight(false);
									lanes.removeLastOccurrence(l);

									// Highlight next available Lanes
									r.hightLight(true);


								} else {
									// Add Lane
									if (lastLane != null) {	
										Junction nextJunction = lastLane.getNextJunction();
										if (nextJunction != null && nextJunction.getOutgoingLanes().contains(l)) {
											r.hightLight(false);
											lanes.add(l);
											// Highlight next available Lanes
											r.hightLight(true);
										}
									} else {
										lanes.add(l);
										r.hightLight(true);
									}
								}
							}
						}
					}
				}
			}
		}

		// Redraw Sim Panel
		this.getSimulator().refreshWindow();
	}


	private int mouse_press_x = 0;
	private int mouse_press_y = 0;
	private boolean mouse_pressed = false;

	@Override
	public void mousePressed(MouseEvent e) {

		mouse_press_x = e.getX();
		mouse_press_y = e.getY();
		mouse_pressed = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		mouse_pressed = false;

		this.getSimulator().refreshWindow();

	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {

		if (mouse_pressed) {
			int mouse_move_x = e.getX() - mouse_press_x;
			int mouse_move_y = e.getY() - mouse_press_y;

			// Don't Move at everystep
			if (Math.abs(mouse_move_x) > 5 || Math.abs(mouse_move_y) > 5) {

				this.getSimulator().getCurrentSimulation().moveSimulation(mouse_move_x, mouse_move_y);

				mouse_press_x = e.getX();
				mouse_press_y = e.getY();

				this.getSimulator().refreshWindow();
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {}


}
