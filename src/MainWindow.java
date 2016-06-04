package pl.edu.pw.fizyka.pojava.Kwanty;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

// P.J./L.M.


@SuppressWarnings("serial")
public class MainWindow extends JFrame
{
	private Planet[] solarSystem;
	private GUI illustrate;
	public Ship playerShip;
	
	private JMenuBar menuBar;
	private JMenu menuFile;
	private JMenuItem exitOption;
	private JMenuItem saveOption;
	private JMenuItem loadOption;
	private JMenuItem addShipOption;
	private JMenuItem setTimeScale;
	private JMenuItem setScaleFactor;
	private JMenuItem startSim;
	private JMenuItem stopSim;
	private JMenu shipSettings;
	private JMenuItem engineSettings;
	private JMenu aboutFile;
	private JMenuItem aboutButton;
	
	public boolean addShip;
	public Point startPoint;
	public Point2D.Double oldCenter;
	
	public MainWindow()
	{
		this.solarSystem = new Planet[8];
		this.solarSystem[0] = new Planet(3.3011e23, 69.816900e9, 46.001200e9, 2439.7e3);
		this.solarSystem[1] = new Planet(4.8675e24, 108.939e9, 107.477e9, 6051.8e3);
		this.solarSystem[2] = new Planet(5.97237e24, 152.1e9, 147e9, 6371e3);
		this.solarSystem[3] = new Planet(6.4171e23, 249.2336e9, 206.65744e9, 3389.5e3);
		this.solarSystem[4] = new Planet(1.8986e27, 816.056032e9, 778.30896e9, 69911e3);
		this.solarSystem[5] = new Planet(5.6836e26, 1508.8656e9, 1349.9904e9, 58232e3);
		this.solarSystem[6] = new Planet(8.6810e25, 3008.456e9, 2742.168e9, 25362e3);
		this.solarSystem[7] = new Planet(1.0243e26, 4537.368e9, 4459.576e9, 24622e3);
		
		this.illustrate = new GUI(this.solarSystem, MainWindow.this);
		this.add(this.illustrate);
		
		Toolkit tools = Toolkit.getDefaultToolkit();
		Dimension dim = tools.getScreenSize();
		
		this.addShip = false;
		this.playerShip = null;
		
		setMenu();
		
		
		this.addKeyListener(new ArrowControlsListener());
		
		this.setSize(1200, 900);
		this.setLocation(dim.width/4, dim.height/4);
		this.illustrate.setSize(1200, 900);
		this.illustrate.setDefaultCenter();
		this.illustrate.repaint();
		
		
	}
	
	private void setMenu()
	{
		this.menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		this.menuFile = new JMenu("File");
		this.menuBar.add(menuFile);
		
		// File items
		this.exitOption = new JMenuItem("Exit");
		this.saveOption = new JMenuItem("Save");
		this.loadOption = new JMenuItem("Load");
		this.addShipOption = new JMenuItem ("Add Ship");
		this.startSim = new JMenuItem("Start Simulation");
		this.stopSim = new JMenuItem("Stop Simulation");
		this.setTimeScale = new JMenuItem("Set simulation speed");
		this.setScaleFactor = new JMenuItem("Set distance scale");
		this.menuFile.add(this.startSim);
		this.menuFile.add(this.stopSim);
		this.menuFile.add(this.setScaleFactor);
		this.menuFile.add(this.setTimeScale);
		this.menuFile.add(this.saveOption);
		this.menuFile.add(this.loadOption);
		this.menuFile.add(this.addShipOption);
		this.menuFile.add(this.exitOption);
		this.startSim.setEnabled(false);
		this.stopSim.setEnabled(false);
		this.saveOption.setEnabled(false);
		this.loadOption.setEnabled(false);
		// Engine Settings Items
		this.shipSettings = new JMenu("Ship");
		this.menuBar.add(this.shipSettings);
		this.shipSettings.setEnabled(false);
		this.engineSettings = new JMenuItem("Ship engines");
		this.shipSettings.add(this.engineSettings);
		// About Items
		this.aboutFile = new JMenu("About");
		this.menuBar.add(aboutFile);
		this.aboutButton = new JMenuItem("About");
		this.aboutButton.addActionListener(new AboutListener());
		// File ActionListeners
		this.exitOption.addActionListener(new ExitOptionListener());
		this.addShipOption.addActionListener(new AddShipListener());
		this.startSim.addActionListener(new StartSimListener());
		this.stopSim.addActionListener(new StopSimListener());
		this.setTimeScale.addActionListener(new SetTimeListener());
		this.setScaleFactor.addActionListener(new SetScaleListener());
		// Engine Settings ActionListener
		this.engineSettings.addActionListener(new EngineSettingsListener());
		// About ActionListener
		this.aboutFile.add(aboutButton);
	}
	
	public Ship getActualShip()
	{
		return this.playerShip;
	}

	class ArrowControlsListener implements KeyListener
	{
		public void keyPressed(KeyEvent arg0)
		{
			int keyPressed = arg0.getKeyCode();
			
			if (keyPressed == KeyEvent.VK_LEFT)
			{
				MainWindow.this.playerShip.engineAcceleration(MainWindow.this.playerShip.getEngineAccelerationX() - 0.5e-4, MainWindow.this.playerShip.getEngineAccelerationY());
			}
			else if (keyPressed == KeyEvent.VK_RIGHT)
			{
				MainWindow.this.playerShip.engineAcceleration(MainWindow.this.playerShip.getEngineAccelerationX() + 0.5e-4, MainWindow.this.playerShip.getEngineAccelerationY());
			}
			else if (keyPressed == KeyEvent.VK_UP)
			{
				MainWindow.this.playerShip.engineAcceleration(MainWindow.this.playerShip.getEngineAccelerationX(), MainWindow.this.playerShip.getEngineAccelerationY() - 0.5e-4);
			}
			else if (keyPressed == KeyEvent.VK_DOWN)
			{
				MainWindow.this.playerShip.engineAcceleration(MainWindow.this.playerShip.getEngineAccelerationX(), MainWindow.this.playerShip.getEngineAccelerationY() + 0.5e-4);
			}
		}

		public void keyReleased(KeyEvent arg0)
		{
			MainWindow.this.playerShip.engineAcceleration(0, 0);
		}
		public void keyTyped(KeyEvent arg0)
		{}
	}
	
	class AddShipListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			MainWindow.this.addShip=true;
			JOptionPane.showMessageDialog(MainWindow.this, "Place the ship on the map with Left Mouse Button");
				
			if (MainWindow.this.illustrate.isSimulationRunning())
				MainWindow.this.illustrate.stopTimer();
				
			MainWindow.this.startSim.setEnabled(true);
			MainWindow.this.stopSim.setEnabled(false);
			MainWindow.this.shipSettings.setEnabled(true);
		}
	}
	
	class StartSimListener implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0)
		{
			MainWindow.this.illustrate.startTimer(MainWindow.this.playerShip);
			MainWindow.this.startSim.setEnabled(false);
			MainWindow.this.stopSim.setEnabled(true);
		}
	}
	
	class StopSimListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			MainWindow.this.illustrate.stopTimer();
			MainWindow.this.startSim.setEnabled(true);
			MainWindow.this.stopSim.setEnabled(false);				
		}
	}
	
	class SetTimeListener implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0) 
		{
			try
			{
				int secondsToHours = 3600; // is not indicative of actual in-simulation time, because timers run at ~1 ms, while calculations run at dt=1s interval
				String input = JOptionPane.showInputDialog("Simulation speed: " + MainWindow.this.illustrate.getTimeScale()/secondsToHours + "x normal speed");
				double newTimeScale = Double.parseDouble(input) * secondsToHours;
				MainWindow.this.illustrate.setTimeScale(newTimeScale);
			}
			catch(Exception e)
			{}
		}
	}
	
	class SetScaleListener implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0) 
		{
			try
			{
				String input = JOptionPane.showInputDialog("Enlargement: " + MainWindow.this.illustrate.getScale());
				double newScale = Double.parseDouble(input);
				MainWindow.this.illustrate.setScale(newScale);
				MainWindow.this.illustrate.repaint();
			}
			catch(Exception e)
			{}
		}
	}
	
	class EngineSettingsListener implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0) 
		{
			EngineDialog enginePanel = new EngineDialog(MainWindow.this, MainWindow.this.playerShip);
			enginePanel.setVisible(true);
		}
	}
	
	class ExitOptionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			System.exit(0);
		}
	}
	
	class AboutListener implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0)
		{
			JOptionPane.showMessageDialog(MainWindow.this, "Open source project created by Piotr J. and Lukasz M. for Java Programming class project. Finished in June, 2016.");
		}
		
	}
}

	