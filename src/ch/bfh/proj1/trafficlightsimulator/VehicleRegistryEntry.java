package ch.bfh.proj1.trafficlightsimulator;

public class VehicleRegistryEntry {
	
	private Class vehicle;
	
	private int distribution;
	
	private String vehicleName;
	
	public VehicleRegistryEntry(Class vehicle, String vehicleName, int distribution) {
		this.setVehicle(vehicle);
		this.setVehicleName(vehicleName);
		this.setDistribution(distribution);
	}

	public Class getVehicle() {
		return vehicle;
	}

	public void setVehicle(Class vehicle) {
		this.vehicle = vehicle;
	}

	public int getDistribution() {
		return distribution;
	}

	public void setDistribution(int distribution) {
		this.distribution = distribution;
	}

	public String getVehicleName() {
		return vehicleName;
	}

	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
	}

}
