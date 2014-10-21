package ch.bfh.proj1.trafficlightsimulator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class OptionPanel extends JPanel implements ActionListener{
	
	JButton btRunSimulation;
	JButton btBreak;
	
	Simulation currentSImulation;
	
	public OptionPanel(Simulation currentSimulation) {
		this.currentSImulation = currentSimulation;
		
		btRunSimulation = new JButton("Start");
		btRunSimulation.addActionListener(this);
		
		btBreak = new JButton("Break");
		btBreak.addActionListener(this);
		btBreak.setEnabled(false);
		
		
		
		this.add(btRunSimulation);
		this.add(btBreak);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		

		if (e.getSource().equals(btRunSimulation)) {

			if (currentSImulation.isRunning()) {
				btRunSimulation.setText("Start");
				currentSImulation.stopSimulation();
				btBreak.setEnabled(false);
				
				this.currentSImulation = null;
				this.currentSImulation = new Simulation();
			} else {
				btRunSimulation.setText("Stop");
				currentSImulation.startSimulation();
				btBreak.setEnabled(true);
			}
	
		}
		
		if (e.getSource().equals(btBreak)) {
			if (currentSImulation.isBreaking()) {
				currentSImulation.setBreaking(false);
				btBreak.setText("Break");
			} else {
				currentSImulation.setBreaking(true);
				btBreak.setText("Continue");
			}
			
		}
		
	}

}
