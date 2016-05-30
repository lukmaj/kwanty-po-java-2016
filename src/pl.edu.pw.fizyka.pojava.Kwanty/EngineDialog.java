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
{ // LM
	private boolean enginesOnline;
	
	private JLabel accelerationX;
	private JLabel accelerationY;
	private JSpinner accelerationXSpinner;
	private JSpinner accelerationYSpinner;
	private SpinnerModel spinnerXModel;
	private SpinnerModel spinnerYModel;

	
	JButton engineStatusButton;
	JButton implementAccelerationsButton;
	
	EngineDialog(MainWindow mainWindow, Ship playerShip)
	{
		super(mainWindow, false);
		
		this.setLayout(new BorderLayout());
		
		JPanel northPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel southPanel = new JPanel();
		JPanel center1 = new JPanel();

		
		// NORTH PANEL
		accelerationX = new JLabel("Acceleration in X direction:");
		spinnerXModel = new SpinnerNumberModel(0, -30, 30, 0.25);
		spinnerYModel = new SpinnerNumberModel(0, -30, 30, 0.25);
		accelerationXSpinner = new JSpinner(spinnerXModel);

		
		northPanel.add(accelerationX);
		northPanel.add(accelerationXSpinner);

		this.add(northPanel, BorderLayout.NORTH);
	
		// CENTRAL PANEL
		accelerationY = new JLabel("Acceleration in Y direction:");
		accelerationYSpinner = new JSpinner(spinnerYModel);

	
		center1.add(accelerationY);
		center1.add(accelerationYSpinner);
		centerPanel.add(center1);
		
		this.add(centerPanel, BorderLayout.CENTER);
		
		// SOUTH PANEL
		engineStatusButton = new JButton();
		southPanel.add(engineStatusButton);
		
		implementAccelerationsButton = new JButton("Implement data");
		southPanel.add(implementAccelerationsButton);
		
		this.add(southPanel, BorderLayout.SOUTH);
		
		this.enginesOnline = false;
		setWindow(playerShip);
		
		
		this.setTitle("Ship Settings");
		this.pack();
		
		engineStatusButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				enginesOnline = (!enginesOnline);
				setWindow(playerShip);
				repaint();
			}
		});	
		
		
		ImplementAccelerationsButtonListener accButtonListener = new ImplementAccelerationsButtonListener(playerShip);
		implementAccelerationsButton.addActionListener(accButtonListener);
	}
	
	public void setWindow(Ship playerShip)
	{
		if (!enginesOnline)
		{
			engineStatusButton.setBackground(Color.RED);
			engineStatusButton.setText("Engines Offline");
			
			accelerationXSpinner.setEnabled(false);
			accelerationYSpinner.setEnabled(false);
			accelerationXSpinner.setValue(0);
			accelerationYSpinner.setValue(0);
			
			playerShip.engineAcceleration(0, 0);
		}
		else
		{
			engineStatusButton.setBackground(Color.GREEN);
			engineStatusButton.setText("Engines Online");
			
			accelerationXSpinner.setEnabled(true);
			accelerationYSpinner.setEnabled(true);
			
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
				engineAccelerationX = (double) accelerationXSpinner.getValue();
			}
			catch(Exception e)
			{
				engineAccelerationX = 0;
			}
			
			try
			{
				engineAccelerationY = (double) accelerationYSpinner.getValue();
			}
			catch(Exception e)
			{
				engineAccelerationY = 0;

			}
			
			playerShip.engineAcceleration(engineAccelerationX, engineAccelerationY);
		}
	}
}
