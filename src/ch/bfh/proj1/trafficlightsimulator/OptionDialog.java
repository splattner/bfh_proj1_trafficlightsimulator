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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import ch.bfh.proj1.trafficlightsimulator.Lane.marker;
import ch.bfh.proj1.trafficlightsimulator.vehicles.Car;
import ch.bfh.proj1.trafficlightsimulator.vehicles.Truck;
import ch.bfh.proj1.trafficlightsimulator.vehicles.Vehicle;


@SuppressWarnings("serial")
public class OptionDialog extends JDialog implements ActionListener{ 

	private TrafficLightSimulator simulator;

	private JButton btApply;
	private JButton btAddRoute;
	private JButton btDelRoute;

	/**
	 * Table with all routes
	 */
	private JTable routeTable;

	/**
	 * Table with all Vehicle Classes
	 */
	private JTable vehicleTable;

	public OptionDialog(TrafficLightSimulator simulator) {
		super();

		this.setSimulator(simulator);

		this.setPreferredSize(new Dimension(800,600));

		this.setLayout(new BorderLayout());

		JPanel panel  = new JPanel();
		BoxLayout bl = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(bl);

		// Routes Table
		this.routeTable = new JTable(new RouteTableModel(this.getSimulator()));
		JScrollPane scrollPaneRoutes = new JScrollPane(this.routeTable);

		this.routeTable.setPreferredScrollableViewportSize(new Dimension(400, 400));
		this.routeTable.setFillsViewportHeight(true);

		// Set Cell Renderer & Editor
		this.routeTable.getColumnModel().getColumn(1).setCellRenderer(new SliderRenderer(JSlider.HORIZONTAL,0,100,0));
		this.routeTable.getColumnModel().getColumn(1).setCellEditor(new SliderEditor(JSlider.HORIZONTAL, 0, 100, 0));

		this.routeTable.getColumnModel().getColumn(2).setCellRenderer(new AddVehicleRendererAndEditor(this.getSimulator()));
		this.routeTable.getColumnModel().getColumn(2).setCellEditor(new AddVehicleRendererAndEditor(this.getSimulator()));

		this.routeTable.setRowHeight(40);

		SelectionListener listener = new SelectionListener(this.routeTable, this.getSimulator().getCurrentSimulation().getRoutes());
		this.routeTable.getSelectionModel().addListSelectionListener(listener);
		
		// Disable Column reorder
		this.routeTable.getTableHeader().setReorderingAllowed(false);


		panel.add(scrollPaneRoutes);
		
		JPanel routeMode = new JPanel();
		
		btAddRoute = new JButton("Add Route");
		btAddRoute.addActionListener(this);
		
		btDelRoute = new JButton("Delete Route");
		btDelRoute.addActionListener(this);
		
		routeMode.add(btAddRoute);
		routeMode.add(btDelRoute);
		
		panel.add(routeMode);

		// Vehicle Table
		this.vehicleTable = new JTable(new VehicleTableModel(this.getSimulator()));
		JScrollPane scrollPaneVehicles = new JScrollPane(this.vehicleTable);

		this.vehicleTable.setPreferredScrollableViewportSize(new Dimension(400, 400));
		this.vehicleTable.setFillsViewportHeight(true);

		// Set Cell Renderer & Editor
		this.vehicleTable.getColumnModel().getColumn(1).setCellRenderer(new SliderRenderer(JSlider.HORIZONTAL,0,100,0));
		this.vehicleTable.getColumnModel().getColumn(1).setCellEditor(new SliderEditor(JSlider.HORIZONTAL, 0, 100, 0));

		this.vehicleTable.setRowHeight(40);
		
		// Disable Column reorder
		this.vehicleTable.getTableHeader().setReorderingAllowed(false);

		panel.add(scrollPaneVehicles);

		this.add(panel, BorderLayout.CENTER);

		this.btApply = new JButton("Close");
		this.btApply.addActionListener(this);


		this.add(btApply, BorderLayout.PAGE_END);

	}

	public TrafficLightSimulator getSimulator() { return simulator; }
	public void setSimulator(TrafficLightSimulator simulator) { this.simulator = simulator; }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		// Add a new route, only if simulation is not running
		if (e.getSource().equals(this.btAddRoute) && !this.getSimulator().getCurrentSimulation().isRunning()) {
			
			int idLastRoute = this.getSimulator().getCurrentSimulation().getRoutes().size();
			
			Route r = new Route(idLastRoute+1);
			
			this.getSimulator().getCurrentSimulation().getRoutes().add(r);
			
			((AbstractTableModel) this.routeTable.getModel()).fireTableDataChanged();
			
			
		}
		
		// Delete a selected route, only if simulation is not running
		if (e.getSource().equals(this.btDelRoute) && !this.getSimulator().getCurrentSimulation().isRunning()) {
			

			
			for (Route r : this.getSimulator().getCurrentSimulation().getRoutes()) {
				if (r.isVisible()) {
					r.highlightLanes(marker.none);
					this.getSimulator().getCurrentSimulation().getRoutes().remove(r);
					break;
				}
			}
			((AbstractTableModel) this.routeTable.getModel()).fireTableDataChanged();
			
		}
		
		if (e.getSource().equals(this.btApply)) {
			this.setVisible(false);
		}
		
	}

	class VehicleTableModel extends AbstractTableModel {
		private String[] columnNames = {"Vehicle","Distribution of Vehicle"};

		private TrafficLightSimulator simulator;

		public VehicleTableModel(TrafficLightSimulator simulator) {	this.simulator = simulator; }

		public Class<?> getColumnClass(int column) { return getValueAt(0, column).getClass(); }

		public int getColumnCount() {return columnNames.length;	}

		public int getRowCount() {return this.simulator.getVehicleRegistry().size();}

		public String getColumnName(int col) { return columnNames[col]; }


		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Object ret = null;

			switch (columnIndex) {
			case 0:
				ret = this.simulator.getVehicleRegistry().get(rowIndex).getVehicleName();

				break;
			case 1:
				ret = this.simulator.getVehicleRegistry().get(rowIndex).getDistribution();
				break;

			}

			return ret;
		}

		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {

			switch (columnIndex) {

			case 1:
				this.simulator.getVehicleRegistry().get(rowIndex).setDistribution((Integer) value);

				int sum = 0;
				int overRunBy = 0;

				for (VehicleRegistryEntry ve : this.simulator.getVehicleRegistry()) {
					sum += ve.getDistribution();
				}

				if (sum > 100) {
					overRunBy = sum - 100;
					this.simulator.getVehicleRegistry().get(rowIndex).setDistribution(this.simulator.getVehicleRegistry().get(rowIndex).getDistribution() - overRunBy);

				}

				break;
			}

			//Update table
			fireTableCellUpdated(rowIndex, columnIndex);
		}

		@Override
		public boolean isCellEditable(int row, int col) {
			if (col == 1) {
				return true;
			} else {
				return false;
			}
		}


	}

	class RouteTableModel extends AbstractTableModel {
		private String[] columnNames = {"Route","Distribution of Vehicles","Add Vehicle"};

		private TrafficLightSimulator simulator;
		private ArrayList<Route> routes;

		public RouteTableModel(TrafficLightSimulator simulator) {
			this.simulator = simulator;
			routes = (ArrayList<Route>) this.simulator.getCurrentSimulation().getRoutes();
		}

		public Class<?> getColumnClass(int column) { return getValueAt(0, column).getClass(); }


		public int getColumnCount() { return columnNames.length; }

		public int getRowCount() { return routes.size(); }

		public String getColumnName(int col) { return columnNames[col]; }


		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Object ret = null;
			


			switch (columnIndex) {
			case 0:
				ret =  "Route " + Integer.toString(rowIndex+1);
				break;
			case 1:
				ret = routes.get(rowIndex).getDistribution();
				break;
			case 2:
				ret = 0;
				break;
			}

			return ret;
		}

		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {

			switch (columnIndex) {
			case 1:

				this.routes.get(rowIndex).setDistribution((Integer) value);

				int sum = 0;
				int overRunBy = 0;
				for (Route r : this.routes) {
					sum += r.getDistribution();
				}

				if (sum > 100) {
					overRunBy = sum - 100;
					this.routes.get(rowIndex).setDistribution(this.routes.get(rowIndex).getDistribution() - overRunBy);
				}


				break;
			}

			//Update table
			fireTableCellUpdated(rowIndex, columnIndex);
		}

		@Override
		public boolean isCellEditable(int row, int col) {
			if (col == 1 || col == 2) {
				return true;
			} else {
				return false;
			}
		}
	}

	class AddVehicleRendererAndEditor extends AbstractCellEditor implements TableCellRenderer,TableCellEditor,ActionListener {

		private JButton btCar;
		private JButton btTruck;

		private int routeIndex;

		private TrafficLightSimulator simulator;

		public AddVehicleRendererAndEditor(TrafficLightSimulator simulator) {
			this.btCar = new JButton("Car");
			this.btTruck = new JButton("Truck");
			this.btCar.addActionListener(this);
			this.btTruck.addActionListener(this);

			this.setSimulator(simulator);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {

			this.routeIndex = row;


			JPanel panel = new JPanel();
			panel.add(this.btCar);
			panel.add(this.btTruck);

			if (isSelected) {
				panel.setForeground(table.getSelectionForeground());
				panel.setBackground(table.getSelectionBackground());
			} else {
				panel.setForeground(table.getForeground());
				panel.setBackground(table.getBackground());
			}


			return panel;
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource().equals(this.btCar)) {

				ArrayList<Vehicle> vehicles = this.getSimulator().getCurrentSimulation().getVerhicles();
				Vehicle v = new Car();
				//Vehicle v = new Truck();

				Route r = ((ArrayList<Route>) this.getSimulator().getCurrentSimulation().getRoutes()).get(this.routeIndex);

				Lane l = r.getLanes().get(0);

				v.setRoute(r);
				v.setCurrentLane(l);

				vehicles.add(v);

			}

			if (e.getSource().equals(this.btTruck)) {

				ArrayList<Vehicle> vehicles = this.getSimulator().getCurrentSimulation().getVerhicles();
				Vehicle v = new Truck();

				Route r = ((ArrayList<Route>) this.getSimulator().getCurrentSimulation().getRoutes()).get(this.routeIndex);

				Lane l = r.getLanes().get(0);

				v.setRoute(r);
				v.setCurrentLane(l);

				vehicles.add(v);

			}



		}

		public TrafficLightSimulator getSimulator() { return simulator; }

		public void setSimulator(TrafficLightSimulator simulator) { this.simulator = simulator;}

		@Override
		public boolean isCellEditable(EventObject anEvent) {
			return true;
		}


		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			this.routeIndex = row;


			JPanel panel = new JPanel();
			panel.add(this.btCar);
			panel.add(this.btTruck);

			if (isSelected) {
				panel.setForeground(table.getSelectionForeground());
				panel.setBackground(table.getSelectionBackground());
			} else {
				panel.setForeground(table.getForeground());
				panel.setBackground(table.getBackground());
			}


			return panel;
		}

		@Override
		public Object getCellEditorValue() { return null; }

	}

	class SliderRenderer extends JSlider implements TableCellRenderer
	{
		public SliderRenderer(int orientation, int min, int max, int value) {
			super(orientation, min, max, value);
		}

		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus,
				int row, int column)
		{
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(table.getBackground());
			}

			TableColumnModel columnModel = table.getColumnModel();     
			TableColumn selectedColumn = columnModel.getColumn(column);     
			int columnWidth = selectedColumn.getWidth();
			int columnHeight = table.getRowHeight();
			setSize(new Dimension(columnWidth, columnHeight));

			if (value != null){
				setValue(((Integer)value).intValue());
			}

			setMajorTickSpacing(10);
			setMinorTickSpacing(5);
			setPaintTicks(true);
			setLabelTable(createStandardLabels(10));
			setPaintLabels(true);

			return this;
		}
	}




	class SliderEditor extends DefaultCellEditor implements MouseListener {
		private JSlider slider;
		private int value;
		
		public SliderEditor(int orientation, int min, int max, int value) {
			super(new JCheckBox());
			this.slider = new JSlider(orientation, min, max, value);     
			this.slider.setOpaque(true);   
			this.slider.addMouseListener(this);


		}

		public Component getTableCellEditorComponent(JTable table, Object value,
				boolean isSelected, int row, int column) {

			if (isSelected) {
				this.slider.setForeground(table.getSelectionForeground());
				this.slider.setBackground(table.getSelectionBackground());
			} else {
				this.slider.setForeground(table.getForeground());
				this.slider.setBackground(table.getBackground());
			}
			this.slider.setValue(((Integer) value).intValue());

			this.slider.setMajorTickSpacing(10);
			this.slider.setMinorTickSpacing(5);
			this.slider.setPaintTicks(true);

			this.slider.setLabelTable(this.slider.createStandardLabels(10));
			this.slider.setPaintLabels(true);

			this.value = this.slider.getValue();
			return slider;
		}

		public Object getCellEditorValue() { return new Integer(slider.getValue()); }

		public boolean stopCellEditing() { return super.stopCellEditing(); }

		protected void fireEditingStopped() { super.fireEditingStopped(); }

		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) { this.fireEditingStopped(); }

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}


	}

	class SelectionListener implements ListSelectionListener {
		private JTable table;
		private ArrayList<Route> routes;

		SelectionListener(JTable table, Collection<Route> routes) {
			this.table = table;
			this.routes = (ArrayList<Route>)routes;
		}
		public void valueChanged(ListSelectionEvent e) {

			int[] selectedRow = this.table.getSelectedRows();

			for (Route r : this.routes) {
				r.setVisible(false);

				r.highlightLanes(marker.none);
			}

			for (int i = 0; i < selectedRow.length; i++) {
				Route r = routes.get(selectedRow[i]);
				r.setVisible(true);
				if (!getSimulator().getCurrentSimulation().isRunning()) {
					r.highlightLanes(marker.green);
				}
			}

			getSimulator().refreshWindow();
		}


	}
}


