package ch.bfh.proj1.trafficlightsimulator;

public abstract class Vehicle implements DrawableObject{

	private int maxSpeed;
	private int accelerationMaxPositiv;
	private int accelerationMaxNegativ;
	private int lenght;
	private int currentSpeed;
	private int currentAcceleration;
	private int currentPosOnLane;
	
	private Route route;
	private Lane currentLane;
	
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
		
			this.currentPosOnLane++;
			
			/*
			// Incease Speed if we are not at max speed
			if (this.currentSpeed < this.maxSpeed) {
				// Increase Acceleration if we are not at max acceleration positiv
				if (this.currentAcceleration < this.accelerationMaxPositiv)
					this.currentAcceleration++;
				this.currentSpeed += this.currentAcceleration;
			} else {
				this.currentAcceleration = 0;
			}
			*/
			
			if (this.currentPosOnLane > this.currentLane.getStreet().getLenght()) {
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
}
