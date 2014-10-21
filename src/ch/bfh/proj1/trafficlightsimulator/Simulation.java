package ch.bfh.proj1.trafficlightsimulator;

public class Simulation extends Thread {
	
	private boolean running = false;
	private boolean breakState = false;
	
	public Simulation() {
		
	}
	
	public void run() {

		
		synchronized (this) {
			while (this.isRunning()) {
				
				System.out.println("SimLoop");
				
				try {
					this.wait(1000);
				} catch (InterruptedException e) {
					
				}
				
				if (this.isBreaking()) {
					System.out.println("Breaking");
					try {
						this.wait();
					} catch (InterruptedException e) {

					}
				}
				
			}
			
		}
		


		
		System.out.println("Interrupted");
	}
	
	public void stopSimulation() {
		
		this.running = false;
		
		this.interrupt();
	}
	
	public void startSimulation() {
		this.running = true;

		this.start();
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
	public boolean isBreaking() {
		return breakState;
	}
	
	public void setBreaking(boolean breakState) {
		this.breakState = breakState;
	}

}
