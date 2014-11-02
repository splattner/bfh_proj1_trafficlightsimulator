package ch.bfh.proj1.trafficlightsimulator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.LinkedList;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


public class OptionDialog extends JDialog implements ActionListener{ 

	private TrafficLightSimulator simulator;

	private JButton btApply;

	private JTable routeTable;
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

		this.routeTable.getColumnModel().getColumn(1).setCellRenderer(new SliderRenderer(JSlider.HORIZONTAL,0,100,0));
		this.routeTable.getColumnModel().getColumn(1).setCellEditor(new SliderEditor(JSlider.HORIZONTAL, 0, 100, 0));
		
		
		this.routeTable.getColumnModel().getColumn(2).setCellRenderer(new AddVehicleRendererAndEditor(this.getSimulator()));
		this.routeTable.getColumnModel().getColumn(2).setCellEditor(new AddVehicleRendererAndEditor(this.getSimulator()));

		this.routeTable.setRowHeight(40);
		
		SelectionListener listener = new SelectionListener(this.routeTable, this.getSimulator().getRoutes());
		this.routeTable.getSelectionModel().addListSelectionListener(listener);
		

		panel.add(scrollPaneRoutes);
		
		// Vehicle Table
		this.vehicleTable = new JTable(new VehicleTableModel(this.getSimulator()));
		JScrollPane scrollPaneVehicles = new JScrollPane(this.vehicleTable);

		this.vehicleTable.setPreferredScrollableViewportSize(new Dimension(400, 400));
		this.vehicleTable.setFillsViewportHeight(true);

		this.vehicleTable.getColumnModel().getColumn(1).setCellRenderer(new SliderRenderer(JSlider.HORIZONTAL,0,100,0));
		this.vehicleTable.getColumnModel().getColumn(1).setCellEditor(new SliderEditor(JSlider.HORIZONTAL, 0, 100, 0));
		
		this.vehicleTable.setRowHeight(40);
		
		panel.add(scrollPaneVehicles);

		this.add(panel, BorderLayout.CENTER);

		this.btApply = new JButton("Apply");
		this.btApply.addActionListener(this);


		this.add(btApply, BorderLayout.PAGE_END);



	}

	public TrafficLightSimulator getSimulator() {
		return simulator;
	}

	public void setSimulator(TrafficLightSimulator simulator) {
		this.simulator = simulator;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
	
	class VehicleTableModel extends AbstractTableModel {
		private String[] columnNames = {"Vehicle","Distribution of Vehicle"};

		private TrafficLightSimulator simulator;

		public VehicleTableModel(TrafficLightSimulator simulator) {
			this.simulator = simulator;
		}

		public Class getColumnClass(int column) {
			return getValueAt(0, column).getClass();
		}


		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return this.simulator.getVehicleRegistry().size();
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}


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
				break;
			}
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
			routes = this.simulator.getRoutes();
		}

		public Class getColumnClass(int column) {
			return getValueAt(0, column).getClass();
		}


		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return routes.size();
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}


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
			/*case 0:
				ret =  "Route " + Integer.toString(rowIndex);
				break;*/
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
				/*case 2:
				ret = 0;
				break;*/
			}
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
				
				ArrayList<Vehicle> vehicles = this.getSimulator().getVerhicles();
				Vehicle v = new Car();
				//Vehicle v = new Truck();

				Route r = this.getSimulator().getRoutes().get(this.routeIndex);

				Lane l = r.getRoute().getFirst();
				
				v.setRoute(r);
				v.setCurrentLane(l);
				
				vehicles.add(v);
				
			}
			
			if (e.getSource().equals(this.btTruck)) {
				
				ArrayList<Vehicle> vehicles = this.getSimulator().getVerhicles();
				Vehicle v = new Truck();

				Route r = this.getSimulator().getRoutes().get(this.routeIndex);

				Lane l = r.getRoute().getFirst();
				
				v.setRoute(r);
				v.setCurrentLane(l);
				
				vehicles.add(v);
				
			}
			
			
			
		}

		public TrafficLightSimulator getSimulator() {
			return simulator;
		}

		public void setSimulator(TrafficLightSimulator simulator) {
			this.simulator = simulator;
		}



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
		public Object getCellEditorValue() {
			// TODO Auto-generated method stub
			return null;
		}

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

		public Object getCellEditorValue() {
			return new Integer(slider.getValue());
		}

		public boolean stopCellEditing() {
			return super.stopCellEditing();
		}

		protected void fireEditingStopped() {
			super.fireEditingStopped();
		}




		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			this.fireEditingStopped();
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}


	}
	
	class SelectionListener implements ListSelectionListener {
		  private JTable table;
		  private ArrayList<Route> routes;

		  SelectionListener(JTable table, ArrayList<Route> routes) {
		    this.table = table;
		    this.routes = routes;
		  }
		  public void valueChanged(ListSelectionEvent e) {
			  
			  int[] selectedRow = this.table.getSelectedRows();
			  
			  
		      
		      for (Route r : this.routes) {
		    	  r.setVisible(false);
		      }
		      
		      for (int i = 0; i < selectedRow.length; i++) {
		    	  routes.get(selectedRow[i]).setVisible(true);
		      }
			  
			  
		  }
		}



}


