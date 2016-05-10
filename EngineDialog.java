package pl.edu.pw.fizyka.pojava.Kwanty;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class EngineDialog extends JDialog
{
	private boolean enginesOnline;
	
	private JLabel accelerationX;
	private JLabel accelerationY;
	private JTextField accelerationXField;
	private JTextField accelerationYField;
	private JLabel fuelLabel;
	private JLabel fuelStatus;
	
	JButton engineStatus;
	JButton implementAccelerations;
	
	EngineDialog(MainWindow mainWindow, Ship playerShip)
	{
		super(mainWindow, false);
		
		this.setLayout(new BorderLayout());
		
		JPanel northPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel southPanel = new JPanel();
		JPanel center1 = new JPanel();
		JPanel center2 = new JPanel();
		JPanel center3 = new JPanel();
		
		// NORTH PANEL
		accelerationX = new JLabel("Acceleration in X direction:");
		accelerationXField = new JTextField("0.0", 10);
		
		northPanel.add(accelerationX);
		northPanel.add(accelerationXField);
		this.add(northPanel, BorderLayout.NORTH);
	
		// CENTRAL PANEL
		accelerationY = new JLabel("Acceleration in Y direction:");
		accelerationYField = new JTextField("0.0", 10);
	
		center1.add(accelerationY);
		center1.add(accelerationYField);
		
		implementAccelerations = new JButton("Implement data");
//		implementAccelerations.setSize(d);
		center2.add(implementAccelerations);
		
		fuelLabel = new JLabel("Fuel remaining: ");
		fuelStatus = new JLabel("100" + " %");
		
		center3.add(fuelLabel);
		center3.add(fuelStatus);
		
		centerPanel.add(center1);
		centerPanel.add(center2);
		centerPanel.add(center3);
		
		this.add(centerPanel, BorderLayout.CENTER);
		
		// SOUTH PANEL
		engineStatus = new JButton();
		
		southPanel.add(engineStatus);
		this.add(southPanel, BorderLayout.SOUTH);
		
		this.enginesOnline = false;
		setWindow(playerShip);
		
		
		this.setTitle("Ship Settings");
		this.setSize(300, 300);
		
		engineStatus.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				enginesOnline = (!enginesOnline);
				setWindow(playerShip);
				repaint();
			}
		});	
		
		implementAccelerations.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				double engineAccelerationX;
				double engineAccelerationY;
				
				try
				{
					engineAccelerationX = Double.parseDouble(accelerationXField.getText());
				}
				catch(Exception e)
				{
					engineAccelerationX = 0;
					accelerationXField.setText("0.0");
				}
				
				try
				{
					engineAccelerationY = Double.parseDouble(accelerationYField.getText());
				}
				catch(Exception e)
				{
					engineAccelerationY = 0;
					accelerationYField.setText("0.0");
				}
				
				playerShip.engineAcceleration(engineAccelerationX, engineAccelerationY);
			}
		});
	}
	
	public void setWindow(Ship playerShip)
	{
		if (!enginesOnline)
		{
			engineStatus.setBackground(Color.RED);
			engineStatus.setText("Engines Offline");
			
			accelerationXField.setEditable(false);
			accelerationYField.setEditable(false);
			accelerationXField.setText("0.0");
			accelerationYField.setText("0.0");
		}
		else
		{
			engineStatus.setBackground(Color.GREEN);
			engineStatus.setText("Engines Online");
			accelerationXField.setEditable(true);
			accelerationYField.setEditable(true);
		}
		
		
	}
}
