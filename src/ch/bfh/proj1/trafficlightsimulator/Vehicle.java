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
	
	protected Point origin;
	protected Dimension dimension;
	
	protected Color carColor;
	
	private boolean breakStatus = false;
	
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
	public int getAccelerationStartPositiv() {
		return accelerationStartPositiv;
	}
	public void setAccelerationStartPositiv(int accelerationStartPositiv) {
		this.accelerationStartPositiv = accelerationStartPositiv;
	}
	public int getAccelerationStartNegativ() {
		return accelerationStartNegativ;
	}
	public void setAccelerationStartNegativ(int accelerationStartNegativ) {
		this.accelerationStartNegativ = accelerationStartNegativ;
	}
	public int getAccelerationStepPositiv() {
		return accelerationStepPositiv;
	}
	public void setAccelerationStepPositiv(int accelerationStepPositiv) {
		this.accelerationStepPositiv = accelerationStepPositiv;
	}
	public int getAccelerationStepNegativ() {
		return accelerationStepNegativ;
	}
	public void setAccelerationStepNegativ(int accelerationStepNegativ) {
		this.accelerationStepNegativ = accelerationStepNegativ;
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
			
			// Get all vehicles on this lane
			LinkedList<Vehicle> vehiclesOnThisLane = this.currentLane.getVerhiclesOnLane();
			
			// Get next vehicle, if any
			Vehicle nextVehicle;
			try {
				nextVehicle = vehiclesOnThisLane.get(indexOfNextVehicleonLane);	
			} catch (IndexOutOfBoundsException e) {
				nextVehicle = null;
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
			int distFullStop = this.calcucateDistanceForBreaking();
			distFullStop += carLenghtinPositions;
			//System.out.println("Distance full stop: " + distFullStop);
			
			// Did we already fired a breaking command? if so, no second one!
			boolean alreadyBreaked = false;
			
			// Check if traffic light is red
			// false if there is no traffic light
			boolean isRed = (this.currentLane.getTrafficLight() != null) && (this.currentLane.getTrafficLight().getCurrentStatus() == TrafficLight.trafficLightStatus.RED);
			
			// Check if distance to next vehicle is enought (if any)
			boolean isEnoughDistance = nextVehicle != null && nextVehicle.getCurrentPosOnLane() - this.getCurrentPosOnLane() > minDistance;
	 
			
			// is there a next vehicle
			if (nextVehicle != null) {
				//System.out.println("There is a next car");
				if (nextVehicle.getCurrentSpeed() == 0) {
					//System.out.println("Next Car has speed 0");
					// Next Vehicle stop, break if distance is not enought for full stop
					if (this.breakStatus || nextVehicle.getCurrentPosOnLane() - this.currentPosOnLane <= (distFullStop + TrafficLightSimulator.minimumDistanceBetweenVehiclesMin)) {
						//System.out.println("Distance not enoght -> break");
						this.breakStatus = true;
						this.decreaseSpeed();
						alreadyBreaked = true;
					} else {
						this.breakStatus = false;
					}
				} else {
					// Next Vehicle is moving
					//System.out.println("Next Car is moving");
					// TODO: Verify factor for differentiation of big & small speed difference
					if (Math.abs(this.currentSpeed - nextVehicle.currentSpeed) > this.currentSpeed * 0.8) {
						//System.out.println("with big speed diference");
						// Speed of next vehicle has big difference
						// break if distance is not enought for full stop
						if (nextVehicle.getCurrentPosOnLane() - this.currentPosOnLane <= ( distFullStop + TrafficLightSimulator.minimumDistanceBetweenVehiclesMin)) {
							//System.out.println("Distance not enoght -> break");
							this.breakStatus = true;
							this.decreaseSpeed();
							alreadyBreaked = true;
						} else {
							this.breakStatus = false;
						}
					} else {
						// Speed difference is low
						// Maintain minimum distance (based on current Speed)
						//System.out.println("no big speed diference");
						if (nextVehicle.getCurrentPosOnLane() - this.getCurrentPosOnLane() < minDistance) {
							//System.out.println("Distance not enoght -> break");
							this.breakStatus = true;
							this.decreaseSpeed();
							alreadyBreaked = true;
						} else {
							this.breakStatus = false;
						}
					}
				}
			}
			
			if (isRed) {
				//System.out.println("Traffic Light is red");
				if (this.breakStatus || this.currentLane.getStreet().getPositionsOnStreet() - this.currentPosOnLane <= (distFullStop + (2 * trafficLigthLenghtInPosition))) {
					//System.out.println("Distance not enoght");
					this.breakStatus = true;
					// Are we already breaking?
					if (!alreadyBreaked) {
						//System.out.println("-> Break, because its not yet breaking");
						this.decreaseSpeed();
						alreadyBreaked = true;
					}
				} else {
					this.breakStatus = false;
				}
			}
			
			// Are we currently breaking?
			if (!this.breakStatus ) {
				if (isRed && (this.currentLane.getStreet().getPositionsOnStreet() - this.currentPosOnLane > (distFullStop + (3 * trafficLigthLenghtInPosition)))) {
					//System.out.println("Not Breakning, Red but enought distance -> Increase Speed");
					this.increaseSpeed();
				} else if (isEnoughDistance){
					//System.out.println("Not Breakning, distance to next enought -> Increase Speed");
					this.increaseSpeed();
				} else {
					//System.out.println("Not Breakning, Increase Speed");
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
		if (this.currentAcceleration >= 0) {
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
		
		// Don't accelerate anymore when max speed reached
		if (currentSpeed <= 0) {
			this.currentSpeed = 0;
			this.currentAcceleration = 0;
			this.breakStatus = false; // You do not need to break, if already at zero
		}
		
		//System.out.println("Decrease. Current Speed: " + this.currentSpeed + " Current Acc: " + this.currentAcceleration);
		
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
	
	private int calcucateDistanceForBreaking() {
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
