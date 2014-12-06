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

package ch.bfh.proj1.trafficlightsimulator.vehicles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import ch.bfh.proj1.trafficlightsimulator.DrawableObject;
import ch.bfh.proj1.trafficlightsimulator.Lane;
import ch.bfh.proj1.trafficlightsimulator.Route;
import ch.bfh.proj1.trafficlightsimulator.Street;
import ch.bfh.proj1.trafficlightsimulator.TrafficLight;
import ch.bfh.proj1.trafficlightsimulator.TrafficLightSimulator;


public abstract class Vehicle implements DrawableObject{

	protected int maxSpeed;
	protected int accelerationMaxPositiv;
	protected int accelerationMaxNegativ;
	protected int accelerationStartPositiv;
	protected int accelerationStartNegativ;
	protected int accelerationStepPositiv;
	protected int accelerationStepNegativ;
	protected int lenght;
	protected int currentSpeed;
	protected int currentAcceleration;
	protected int currentPosOnLane;
	
	protected Route route;
	protected Lane currentLane;
	protected int laneIndexOnRoute;

	protected Point origin;

	protected Dimension dimension;
	
	protected Color carColor;
	
	private boolean brakeStatus = false;
	
	public int getMaxSpeed() { return maxSpeed; }
	public void setMaxSpeed(int maxSpeed) { this.maxSpeed = maxSpeed;
	}
	public int getAccelerationMaxPositiv() { return accelerationMaxPositiv; }
	public void setAccelerationMaxPositiv(int accelerationMaxPositiv) {	this.accelerationMaxPositiv = accelerationMaxPositiv; }
	
	public int getAccelerationMaxNegativ() { return accelerationMaxNegativ; }
	public void setAccelerationMaxNegativ(int accelerationMaxNegativ) {	this.accelerationMaxNegativ = accelerationMaxNegativ;}
	
	public int getAccelerationStartPositiv() { return accelerationStartPositiv;	}
	public void setAccelerationStartPositiv(int accelerationStartPositiv) {	this.accelerationStartPositiv = accelerationStartPositiv;}
	
	public int getAccelerationStartNegativ() { return accelerationStartNegativ; }
	public void setAccelerationStartNegativ(int accelerationStartNegativ) { this.accelerationStartNegativ = accelerationStartNegativ;}
	
	public int getAccelerationStepPositiv() { return accelerationStepPositiv; }
	public void setAccelerationStepPositiv(int accelerationStepPositiv) { this.accelerationStepPositiv = accelerationStepPositiv; }
	
	public int getAccelerationStepNegativ() { return accelerationStepNegativ; }
	public void setAccelerationStepNegativ(int accelerationStepNegativ) { this.accelerationStepNegativ = accelerationStepNegativ; }
	
	public int getLenght() { return lenght; }
	public void setLenght(int lenght) { this.lenght = lenght; }
	
	public int getCurrentSpeed() { return currentSpeed; }
	public void setCurrentSpeed(int currentSpeed) { this.currentSpeed = currentSpeed; }
	
	
	public int getCurrentAcceleration() { return currentAcceleration; }
	public void setCurrentAcceleration(int currentAcceleration) { this.currentAcceleration = currentAcceleration; }
	
	public Route getRoute() { return route; }
	public void setRoute(Route myRoute) { this.route = myRoute; }
	
	public Lane getCurrentLane() { return currentLane; }
	public int getLaneIndexOnRoute() { return laneIndexOnRoute; }
	public void setLaneIndexOnRoute(int laneIndexOnRoute) { this.laneIndexOnRoute = laneIndexOnRoute; }
	
	
	
	/**
	 * Set the current Lane for this vehicle
	 * Also add this vehicle to the Vehicle Collection on the lane
	 * @param currentLane
	 */
	/**
	 * @param currentLane
	 */
	public void setCurrentLane(Lane currentLane) {
		this.currentLane = currentLane;
		this.currentLane.getVerhiclesOnLane().addFirst(this);
	}
	
	/**
	 * One Step of the simulation. This function is called by the Simulation Threat
	 * Move Vehicles
	 */
	public void simulationStep()
	{
		// Check if this Vehicle is on a Lane
		if (this.currentLane != null) {
		
			
			// Get Discance to next car
			int indexOfVehicleonLane = this.currentLane.getVerhiclesOnLane().indexOf(this);
			int indexOfNextVehicleonLane = indexOfVehicleonLane + 1;
			
			// Get all vehicles on this lane
			LinkedList<Vehicle> vehiclesOnThisLane = this.currentLane.getVerhiclesOnLane();
			
			// Get next vehicle, if any
			// next vehicle could be on next lane!
			Vehicle nextVehicle = null;
			Lane nextLane = null;
			try {
				// Try on same lane first
				nextVehicle = vehiclesOnThisLane.get(indexOfNextVehicleonLane);	
			} catch (IndexOutOfBoundsException e1) {
				// There is no next Vehicle on this Lane
				
				// Find next Lane on route
				for (int i = 0; i < route.getLanes().size(); i++) {
					if (route.getLanes().get(i) == this.currentLane) {
						
						try {
							nextLane = route.getLanes().get(i+1);
							// Next Vehicle is first on next lane
							nextVehicle = nextLane.getVerhiclesOnLane().getFirst();
						} catch (NoSuchElementException e2) {
							// No Vehicle on next lane
							nextVehicle = null;
						} catch (IndexOutOfBoundsException e3) {
							// No next Lane, so no next vehicle
							nextVehicle = null;
						}
						
						// We found it, skip the rest
						break;
					}
				}
			}
			
			int carLenghtinPositions = (int) ( ( (double)this.dimension.width / (double) TrafficLightSimulator.defaultStreetLenght) * TrafficLightSimulator.defaultPositinOnStreet);			
			//int trafficLigthLenghtInPosition = (int) ( ( (double)this.currentLane.getTrafficLight().getDimension().width / (double) TrafficLightSimulator.defaultStreetLenght) * TrafficLightSimulator.defaultPositinOnStreet);
			int trafficLigthLenghtInPosition = (int) ( ( (double) 10 / (double) TrafficLightSimulator.defaultStreetLenght) * TrafficLightSimulator.defaultPositinOnStreet);	
					
			
			// Get Minimum Distance between two vehicles based on this vehicles speed (when speed equal)
			int minDistance = ((TrafficLightSimulator.minimumDistanceBetweenVehiclesMax - TrafficLightSimulator.minimumDistanceBetweenVehiclesMin) / this.maxSpeed * this.currentSpeed) + TrafficLightSimulator.minimumDistanceBetweenVehiclesMin; 
			minDistance += carLenghtinPositions;
			
			// Force Max Speed
			if (this.currentSpeed > this.maxSpeed) this.currentSpeed = this.maxSpeed;
			
			// Calculate Distance for a full stop (for traffic light stop)
			int distFullStop = this.calcucateDistanceForBraking();
			distFullStop += carLenghtinPositions;
			//System.out.println("Distance full stop: " + distFullStop);
			
			// Did we already fired a brakeing command? if so, no second one!
			boolean alreadyBraked = false;
			
			// Check if traffic light is red
			// false if there is no traffic light
			boolean isRed = (this.currentLane.getTrafficLight() != null) && (this.currentLane.getTrafficLight().getCurrentStatus() == TrafficLight.trafficLightStatus.RED);
			
			// Check if distance to next vehicle is enought (if any)
			boolean isEnoughDistance = false;
			if (nextVehicle != null) {
				if ( this.getCurrentLane() != nextVehicle.getCurrentLane()) {
					// Next Vehicle is on next lane
					// Add current Street lenght to next Vehicle current Position
					//System.out.println("Next VehicleVirtual Pos: " + (this.getCurrentLane().getStreet().getPositionsOnStreet() + nextVehicle.getCurrentPosOnLane()));
					//System.out.println("This Vehicle Pos: " + this.getCurrentPosOnLane());
					isEnoughDistance = ((this.getCurrentLane().getStreet().getPositionsOnStreet() + nextVehicle.getCurrentPosOnLane()) - this.getCurrentPosOnLane()) > minDistance;
					//System.out.println("Is Enought: " + isEnoughDistance);
				} else {
					// They are on the same lane
					isEnoughDistance = nextVehicle.getCurrentPosOnLane() - this.getCurrentPosOnLane() > minDistance;
				}
			}
			
			// Check if distance for full brake to next vehicle is enought (if any)
			boolean isEnoughDistanceFullBrake = false;
			if (nextVehicle != null) {
				if ( this.getCurrentLane() != nextVehicle.getCurrentLane()) {
					// Next Vehicle is on next lane
					// Add current Street lenght to next Vehicle current Position
					isEnoughDistanceFullBrake = ((nextVehicle.getCurrentPosOnLane() + this.getCurrentLane().getStreet().getPositionsOnStreet()) - this.currentPosOnLane) > (distFullStop + TrafficLightSimulator.minimumDistanceBetweenVehiclesMin);
				} else {
					// They are on the same lane
					isEnoughDistanceFullBrake = (nextVehicle.getCurrentPosOnLane() - this.currentPosOnLane) > (distFullStop + TrafficLightSimulator.minimumDistanceBetweenVehiclesMin);
				}
			}			
			

	 
			
			// is there a next vehicle
			if (nextVehicle != null) {
				//System.out.println("There is a next car");
				if (nextVehicle.getCurrentSpeed() == 0) {
					//System.out.println("Next Car has speed 0");
					// Next Vehicle stop, brake if distance is not enought for full stop
					if (this.brakeStatus || !isEnoughDistanceFullBrake) {
						//System.out.println("Distance not enoght -> brake");
						this.brakeStatus = true;
						this.decreaseSpeed();
						alreadyBraked = true;
					} else {
						this.brakeStatus = false;
					}
				} else {
					// Next Vehicle is moving
					//System.out.println("Next Car is moving");
					// TODO: Verify factor for differentiation of big & small speed difference
					if (Math.abs(this.currentSpeed - nextVehicle.currentSpeed) > this.currentSpeed * 0.8) {
						//System.out.println("with big speed diference");
						// Speed of next vehicle has big difference
						// brake if distance is not enought for full stop
						if (!isEnoughDistanceFullBrake) {
							//System.out.println("Distance not enoght -> brake");
							this.brakeStatus = true;
							this.decreaseSpeed();
							alreadyBraked = true;
						} else {
							this.brakeStatus = false;
						}
					} else {
						// Speed difference is low
						// Maintain minimum distance (based on current Speed)
						//System.out.println("no big speed diference");
						//if ((nextVehicle.getCurrentPosOnLane() + this.getCurrentLane().getStreet().getPositionsOnStreet()) - this.getCurrentPosOnLane() < minDistance) {
						if (!isEnoughDistance) {
							//System.out.println("Distance not enoght -> bracke");
							this.brakeStatus = true;
							this.decreaseSpeed();
							alreadyBraked = true;
						} else {
							this.brakeStatus = false;
						}
					}
				}
			}
			
			if (isRed) {
				//System.out.println("Traffic Light is red");
				if (this.brakeStatus || this.currentLane.getStreet().getPositionsOnStreet() - this.currentPosOnLane <= (distFullStop + (2 * trafficLigthLenghtInPosition))) {
					//System.out.println("Distance not enoght");
					// Are we already breaking?
					if (!alreadyBraked) {
						//System.out.println("-> Brake, because its not yet breaking");
						this.brakeStatus = true;
						this.decreaseSpeed();
						alreadyBraked = true;

					}
				}
			} else {
				if (!alreadyBraked) {
					this.brakeStatus = false;
				}
			}
			
			// Are we currently braking?
			if (!this.brakeStatus ) {
				if (isRed && (this.currentLane.getStreet().getPositionsOnStreet() - this.currentPosOnLane > (distFullStop + (2 * trafficLigthLenghtInPosition)))) {
					//System.out.println("Not braking, Red but enought distance -> Increase Speed");
					this.increaseSpeed();
				} else if (isEnoughDistance){
					//System.out.println("Not braking, distance to next enought -> Increase Speed");
					this.increaseSpeed();
				} else {
					//System.out.println("Not braking, Increase Speed");
					this.increaseSpeed();
				}
					
			}
			
			

			
			// Move car to next position based on his speed
			this.currentPosOnLane += this.currentSpeed;
			
			// Move car on next lane if necessary
			if (this.currentPosOnLane > this.currentLane.getStreet().getPositionsOnStreet()) {
				this.toNextLaneOnRoute();
			}
		}
		
	}
	
	// Accelerate vehicle to max
	private void increaseSpeed() {
		if (this.currentAcceleration <= 0) {
			this.currentAcceleration = this.accelerationStartPositiv;
		} else {
			if (this.currentAcceleration < this.accelerationMaxPositiv) {
				this.currentAcceleration += this.accelerationStepPositiv;
			}
		}
		
		// Increase speed with current acceleration if below max speed
		if (this.currentSpeed < this.maxSpeed) {
			this.currentSpeed += this.currentAcceleration;
		}
		
		// Don't accelerate anymore when max speed reached
		if (currentSpeed >= this.maxSpeed) {
			this.currentSpeed = this.maxSpeed;
			this.currentAcceleration = 0;
		}
		
		//System.out.println("Increase. Current Speed: " + this.currentSpeed + " Current Acc: " + this.currentAcceleration);
	
	}
	
	private void decreaseSpeed() {
		
		if (this.currentAcceleration >= 0 ) {
			this.currentAcceleration = -this.accelerationStartNegativ;
		} else {
			if (this.currentAcceleration > - this.accelerationMaxNegativ) {
				this.currentAcceleration -= this.accelerationStepNegativ;
			}
		}
		
		// Increase speed with current acceleration if below max speed
		if (this.currentSpeed > 0) {
			this.currentSpeed += this.currentAcceleration;
		}
		
		// Don't accelerate anymore when min speed reached
		if (currentSpeed <= 0) {
			this.currentSpeed = 0;
			this.currentAcceleration = 0;
		}
		
		//System.out.println("Decrease. Current Speed: " + this.currentSpeed + " Current Acc: " + this.currentAcceleration);
		
	}
	
	/**
	 * Put this vehicle on the next lane of the route
	 */
	public void toNextLaneOnRoute() {
		// Do we have a current Lane? Other we are on the first lane of our route;
		if (this.getCurrentLane() == null) {
			this.setCurrentLane(route.getLanes().get(0));
			this.setLaneIndexOnRoute(0);
			currentLane.getVerhiclesOnLane().addFirst(this);
			
		} else {
			// Goto next Lane on this route
			
			// Remove vehicle for current Lane
			currentLane.getVerhiclesOnLane().remove(this);
			
			if (this.laneIndexOnRoute +1 < route.getLanes().size()) {
				currentLane = route.getLanes().get(laneIndexOnRoute+1);
				currentLane.getVerhiclesOnLane().addFirst(this);
				this.laneIndexOnRoute++;
			} else {
				currentLane = null;
			}
			
		}
		
		this.currentPosOnLane = 0;
	}
	
	/**
	 * Calculate the distance for a full stop based on this vehicles properties
	 * @return
	 */
	private int calcucateDistanceForBraking() {
		int speed = this.currentSpeed;
		int acc = -this.accelerationStartNegativ;
		int dist = 0;
		
		//System.out.println("Start Calc Dist For Full Break. Speed = " + speed);
		//System.out.println("Acc = " + acc);
		
		while (speed > 0) {
			

			speed += acc;
			//System.out.println("Speed = " + speed);
			
			dist += speed;
			
			//System.out.println("Dist: " + dist);
			
			if (acc > - this.accelerationMaxNegativ) {
				acc -= this.accelerationStepNegativ;
				//System.out.println("Acc = " + acc);
			}
			
		}
		
		return dist;
	}
	
	public int getCurrentPosOnLane() {
		return currentPosOnLane;
	}
	public void setCurrentPosOnLane(int currentPosOnLane) {
		this.currentPosOnLane = currentPosOnLane;
	}
	
	@Override
	public void paintObject(Graphics g) {
		
		Lane l = this.getCurrentLane();
		Street s = l.getStreet();
		
		g.setColor(carColor);
		
		Double convertedPos =  ( (double) this.getCurrentPosOnLane() / (double) s.getPositionsOnStreet() * (double) s.getLenght());
		
		
		int currentRouteIndex = this.getRoute().getLanes().indexOf(l);
		
		
		
		
		if (s.getOrientaion() == Street.orientation.horizontal) {
			
			
			// Car is on a horizontal Street
			if (currentLane.getLaneOrientation() == Lane.laneOrientations.startToEnd) {
				
				
				
				// Drive from left to right
				this.setOrigin(new Point(l.getOrigin().x + convertedPos.intValue(), l.getOrigin().y + 5));
			} else {
				// Drive from right to left
				this.setOrigin(new Point(l.getOrigin().x + l.getDimension().width- convertedPos.intValue() , l.getOrigin().y + 5));
			}
			
			g.fillRect(origin.x, origin.y, dimension.width, dimension.height);
			
			
		} else {
			// Car is on a vertical Street
			if (currentLane.getLaneOrientation() == Lane.laneOrientations.startToEnd) {
				// Drive from left to right
				this.setOrigin(new Point(l.getOrigin().x + 5, l.getOrigin().y + convertedPos.intValue()));
			} else {
				// Drive from right to left
				this.setOrigin(new Point(l.getOrigin().x + 5, l.getOrigin().y + l.getDimension().height- convertedPos.intValue()));
			}
			
			g.fillRect(origin.x, origin.y, dimension.height, dimension.width);
		}
		
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
