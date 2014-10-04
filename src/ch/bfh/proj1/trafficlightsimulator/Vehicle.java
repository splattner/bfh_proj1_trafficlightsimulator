package ch.bfh.proj1.trafficlightsimulator;

public abstract class Vehicle implements DrawableObject{

	private int maxSpeed;
	private int accelerationMaxPositiv;
	private int accelerationMaxNegativ;
	private int lenght;
	private int currentSpeed;
	private int currentAcceleration;
	
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
}
