package pl.edu.pw.fizyka.pojava.Kwanty;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

@SuppressWarnings("serial")
public class EngineDialog extends JDialog
{ // L.M.
	
	private JLabel accelerationX;
	private JLabel accelerationY;
	private JSpinner accelerationXSpinner;
	private JSpinner accelerationYSpinner;
	private SpinnerModel spinnerXModel;
	private SpinnerModel spinnerYModel;

	private boolean enginesOnline;
	private JButton engineStatusButton;
	private JButton implementAccelerationsButton;
	
	EngineDialog(MainWindow mainWindow, Ship playerShip)
	{
		super(mainWindow, false);
		this.setLayout(new BorderLayout());
		
		JPanel northPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel southPanel = new JPanel();
		JPanel center1 = new JPanel();

		// NORTH PANEL
		this.accelerationX = new JLabel("Acceleration in X direction:");
		this.spinnerXModel = new SpinnerNumberModel(0, -5, 5, 0.1);
		this.spinnerYModel = new SpinnerNumberModel(0, -5, 5, 0.1);
		this.accelerationXSpinner = new JSpinner(this.spinnerXModel);
		northPanel.add(this.accelerationX);
		northPanel.add(this.accelerationXSpinner);
		this.add(northPanel, BorderLayout.NORTH);
	
		// CENTRAL PANEL
		this.accelerationY = new JLabel("Acceleration in Y direction:");
		this.accelerationYSpinner = new JSpinner(this.spinnerYModel);
		center1.add(this.accelerationY);
		center1.add(this.accelerationYSpinner);
		centerPanel.add(center1);
		this.add(centerPanel, BorderLayout.CENTER);
		
		// SOUTH PANEL
		this.enginesOnline = false;
		this.engineStatusButton = new JButton();
		this.engineStatusButton.addActionListener(new EngineStatusListener(playerShip));	
		this.implementAccelerationsButton = new JButton("Implement data");
		this.implementAccelerationsButton.addActionListener(new ImplementAccelerationsButtonListener(playerShip));
		southPanel.add(this.engineStatusButton);
		southPanel.add(this.implementAccelerationsButton);		
		this.add(southPanel, BorderLayout.SOUTH);
		
		
		setWindow(playerShip);
		this.setTitle("Ship Settings");
		this.pack();
		
		
	}
	
	public void setWindow(Ship playerShip)
	{
		if (!this.enginesOnline)
		{
			this.engineStatusButton.setBackground(Color.RED);
			this.engineStatusButton.setText("Engines Offline");
			this.accelerationXSpinner.setEnabled(false);
			this.accelerationYSpinner.setEnabled(false);
			this.accelerationXSpinner.setValue(0);
			this.accelerationYSpinner.setValue(0);
			playerShip.engineAcceleration(0, 0);
		}
		else
		{
			this.engineStatusButton.setBackground(Color.GREEN);
			this.engineStatusButton.setText("Engines Online");
			this.accelerationXSpinner.setEnabled(true);
			this.accelerationYSpinner.setEnabled(true);
		}
	}
	
	class ImplementAccelerationsButtonListener implements ActionListener
	{
		Ship playerShip;
		ImplementAccelerationsButtonListener(Ship playerShip)
		{
			this.playerShip = playerShip;
		}

		public void actionPerformed(ActionEvent arg0)
		{
			double engineAccelerationX;
			double engineAccelerationY;
			
			try
			{
				engineAccelerationX = (double) EngineDialog.this.accelerationXSpinner.getValue();
			}
			catch(Exception e)
			{
				engineAccelerationX = 0;
			}
			
			try
			{
				engineAccelerationY = (double) EngineDialog.this.accelerationYSpinner.getValue();
			}
			catch(Exception e)
			{
				engineAccelerationY = 0;
			}
			
			this.playerShip.engineAcceleration(engineAccelerationX, engineAccelerationY);
		}
	}
	
	class EngineStatusListener implements ActionListener
	{
		Ship playerShip;
		EngineStatusListener(Ship playerShip)
		{
			this.playerShip = playerShip;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			EngineDialog.this.enginesOnline = (!EngineDialog.this.enginesOnline);
			setWindow(this.playerShip);
			repaint();
		}
	}
}
