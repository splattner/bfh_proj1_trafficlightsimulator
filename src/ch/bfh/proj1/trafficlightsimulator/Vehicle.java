package ch.bfh.proj1.trafficlightsimulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;

public abstract class Vehicle implements DrawableObject{

	protected int maxSpeed;
	protected int accelerationMaxPositiv;
	protected int accelerationMaxNegativ;
	protected int lenght;
	protected int currentSpeed;
	protected int currentAcceleration;
	protected int currentPosOnLane;
	
	protected Route route;
	protected Lane currentLane;
	
	protected Point origin;
	protected Dimension dimension;
	
	protected Color carColor;
	
	public int getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	public int getAccelerationMaxPositiv() {
		return accelerationMaxPositiv;
	}
	public void setAccelerationMaxPositiv(int accelerationMaxPositiv) {
		this.accelerationMaxPositiv = accelerationMaxPositiv;
	}
	public int getAccelerationMaxNegativ() {
		return accelerationMaxNegativ;
	}
	public void setAccelerationMaxNegativ(int accelerationMaxNegativ) {
		this.accelerationMaxNegativ = accelerationMaxNegativ;
	}
	public int getLenght() {
		return lenght;
	}
	public void setLenght(int lenght) {
		this.lenght = lenght;
	}
	public int getCurrentSpeed() {
		return currentSpeed;
	}
	public void setCurrentSpeed(int currentSpeed) {
		this.currentSpeed = currentSpeed;
	}
	public int getCurrentAcceleration() {
		return currentAcceleration;
	}
	public void setCurrentAcceleration(int currentAcceleration) {
		this.currentAcceleration = currentAcceleration;
	}
	public Route getRoute() {
		return route;
	}
	public void setRoute(Route myRoute) {
		this.route = myRoute;
	}
	public Lane getCurrentLane() {
		return currentLane;
	}
	public void setCurrentLane(Lane currentLane) {
		this.currentLane = currentLane;
		this.currentLane.getVerhiclesOnLane().addFirst(this);
	}
	
	public void simulationStep()
	{
		// Check if this Vehicle is on a Lane
		if (this.currentLane != null) {
			
			
			// Get Discance to next car
			int indexOfVehicleonLane = this.currentLane.getVerhiclesOnLane().indexOf(this);
			int indexOfNextVehicleonLane = indexOfVehicleonLane + 1;
			
			LinkedList<Vehicle> vehiclesOnThisLane = this.currentLane.getVerhiclesOnLane();
			
			/*
			System.out.println("Index of this: " + indexOfVehicleonLane);
			System.out.println("NUmber of Vehicles on Lane: " + vehiclesOnThisLane.size());
			System.out.println("get Next: " + indexOfNextVehicleonLane);
			*/
			
			Vehicle v;
			int posOfNextVehicle;
			try {
				v = vehiclesOnThisLane.get(indexOfNextVehicleonLane);
				posOfNextVehicle = v.getCurrentPosOnLane();
				
			} catch (IndexOutOfBoundsException e) {
				v = null;
			}
		
			this.currentPosOnLane += this.currentSpeed;
			
			// Get Minimum Distance between two vehicles based on this vehicles speed
			int minDistance = ((TrafficLightSimulator.minimumDistanceBetweenVehiclesMax - TrafficLightSimulator.minimumDistanceBetweenVehiclesMin) / this.maxSpeed * this.currentSpeed) + TrafficLightSimulator.minimumDistanceBetweenVehiclesMin; 
			
			// Force Max Speed
			if (this.currentSpeed > this.maxSpeed) this.currentSpeed = this.maxSpeed;
			
			// Incease Speed if we are not at max speed
			if (this.currentSpeed < this.maxSpeed) {
				
				// Only Accelerate if minimum distance between vehicles is given
				if (v == null || (v.getCurrentPosOnLane() - this.getCurrentPosOnLane() > minDistance)) {
					if (currentAcceleration < 0) {
						// If still on neg acceleration, stop this
						this.currentAcceleration = 0;
					} else {
						// Increase Acceleration if we are not at max acceleration positiv
						if (this.currentAcceleration < this.accelerationMaxPositiv) {
							this.currentAcceleration++;
							//System.out.println("Current Acceleration: " + currentAcceleration);
						}
					}
				} else {
					// Break, we are to close
					if (currentAcceleration > 0) {
						// If still on pos acceleration, stop this
						this.currentAcceleration = 0;
					} else {
						// Break until max neg. Acceleration
						if (currentAcceleration > - this.accelerationMaxNegativ) {
							this.currentAcceleration--;
							//System.out.println("Current Acceleration: " + currentAcceleration);
						}
					}
					
				}

				this.currentSpeed += this.currentAcceleration;
			} else {
				this.currentAcceleration = 0;
			}
			
			//System.out.println("Current Speed: " + currentSpeed + " Current Acceleration: " + currentAcceleration);
			
			
			if (this.currentPosOnLane > this.currentLane.getStreet().getPositionsOnStreet()) {
				this.toNextLaneOnRoute();
			}
		}
		
	}
	
	public void toNextLaneOnRoute() {
		// Do we have a current Lane? Other we are on the first lane of our route;
		if (currentLane == null) {
			currentLane = route.getRoute().getFirst();
			currentLane.getVerhiclesOnLane().addFirst(this);
			
		} else {
			// Goto next Lane on this route
			for (int i = 0; i < route.getRoute().size(); i++) {
	            // Search position of current Lane and then set next
				if (route.getRoute().get(i) == currentLane) {
	            	// if next == null, car has left the route
					currentLane.getVerhiclesOnLane().remove(this);
					if (i+1 < route.getRoute().size()) {
						currentLane = route.getRoute().get(i+1);
						currentLane.getVerhiclesOnLane().addFirst(this);
					} else {
						currentLane = null;
					}
					
	            	break;
				}
			}
		}
		
		this.currentPosOnLane = 0;
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
				this.setOrigin(new Point(l.getOrigin().x + 5, l.getOrigin().y + l.getDimension().width- convertedPos.intValue()));
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
