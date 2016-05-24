package pl.edu.pw.fizyka.pojava.Kwanty;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

// Klasa L.M.


@SuppressWarnings("serial")
public class MainWindow extends JFrame
{
	Planet[] solarSystem;
	GUI illustrate;
	Ship playerShip;
	
	JMenuBar menuBar;

	JMenu menuFile;
	JMenuItem exitOption;
	JMenuItem saveOption;
	JMenuItem loadOption;
	JMenuItem addShipOption;
	JMenuItem setTimeScale;
	JMenuItem setScaleFactor;
	JMenuItem startSim;
	JMenuItem stopSim;

	JMenu shipSettings;
	JMenuItem engineSettings;
	
	boolean addShip;
	
	
	Point startPoint;
	Point2D.Double oldCenter;
	
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
		
		this.illustrate = new GUI(solarSystem, MainWindow.this);
		this.add(illustrate);
		
		Toolkit tools = Toolkit.getDefaultToolkit();
		Dimension dim = tools.getScreenSize();
		
		this.addShip = false;
		this.playerShip = null;
		
		setMenu();
		this.addMouseListener(new MouseListener()
		{
			
			public void mouseClicked(MouseEvent arg0)
			{
				
				if (arg0.getClickCount() > 1 && addShip)
				{
					Point2D.Double shipLocation = illustrate.translateFromPixeltoMeters(new Point2D.Double(arg0.getX(), arg0.getY()));

//					int x = 3; // debug for planets
//					Point2D.Double shipLocation = new Point2D.Double(solarSystem[x].getX() + solarSystem[x].getplanetaryRadius(), solarSystem[x].getY()+solarSystem[x].getplanetaryRadius());
					
//					Point2D.Double shipLocation = new Point2D.Double(0, -6.957e8 * 40); // debug for sun
					
					double shipRadius = Math.sqrt(Math.pow(shipLocation.getX(), 2) + Math.pow(shipLocation.getY(), 2));
					playerShip = new Ship(30e3, shipRadius, shipLocation);
					LoadShipOrbit shipOrbit = new LoadShipOrbit(playerShip);
					shipOrbit.generateShipOrbit();
					addShip = false;
					
					
					
					illustrate.repaint();
					
			//		System.out.println("a from sun on Earth" + (solarSystem[2].GMs/Math.pow(solarSystem[2].getActualRadius(), 2)) );
			//		System.out.println(playerShip.getA());
					
			//		System.out.println("ShipXLocation: " + shipLocation.getX() + " shipLocationY: " + shipLocation.getY());
				//	System.out.println("ShipX: " + playerShip.getX() + " shipY: " + playerShip.getY() +
				//	System.out.println(" shipRadius: " + playerShip.getActualRadius() + " sqrt(x^2+y^2): " + Math.sqrt(Math.pow(playerShip.getX(), 2) + Math.pow(playerShip.getY(), 2)));
					
				}
			}

			public void mouseEntered(MouseEvent arg0)
			{}

			public void mouseExited(MouseEvent arg0) 
			{}

			public void mousePressed(MouseEvent arg0)
			{
				startPoint = arg0.getPoint();
				oldCenter = illustrate.getCenterInPixels();
			}

			public void mouseReleased(MouseEvent arg0)
			{
				//System.out.println("arg: " + arg0.getModifiersEx());
				double newCenterX = oldCenter.getX() + arg0.getPoint().getX() - startPoint.getX();
				double newCenterY = oldCenter.getY() + arg0.getPoint().getY() - startPoint.getY();
				
				illustrate.setCenterInPixels(new Point2D.Double(newCenterX, newCenterY));
				
				illustrate.repaint();
			}
		});
		
		this.addMouseMotionListener(new MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{
				{
					double newCenterX = oldCenter.getX() + (e.getPoint().getX() - startPoint.getX());
					double newCenterY = oldCenter.getY() + (e.getPoint().getY() - startPoint.getY());
					
//					System.out.println("x " + newCenterX + " y " + newCenterY);
					illustrate.setCenterInPixels(new Point2D.Double(newCenterX, newCenterY));
					
					illustrate.repaint();
				}
			}	
		});
		
		this.addMouseWheelListener(new MouseWheelListener()
		{
			public void mouseWheelMoved(MouseWheelEvent arg0)
			{
				if(arg0.getPreciseWheelRotation() < 0)
				{
					illustrate.setScale((illustrate.getScale() - arg0.getPreciseWheelRotation() * Math.sqrt(illustrate.getScale())));
				}
				else if (arg0.getPreciseWheelRotation() > 0)
				{
					illustrate.setScale((illustrate.getScale() - arg0.getPreciseWheelRotation() * Math.sqrt(illustrate.getScale())));
				}
				illustrate.repaint();
			}
		});
		
		this.setSize(1200, 900);
		this.setLocation(dim.width/4, dim.height/4);
		illustrate.setSize(1200, 900);
		illustrate.setDefaultCenter();
		illustrate.repaint();
		
		
	}
	
	private void setMenu()
	{
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuFile = new JMenu("File");
		menuBar.add(menuFile);
		
		exitOption = new JMenuItem("Exit");
		saveOption = new JMenuItem("Save");
		loadOption = new JMenuItem("Load");
		addShipOption = new JMenuItem ("Add Ship");
		
		startSim = new JMenuItem("Start Simulation");
		stopSim = new JMenuItem("Stop Simulation");
		
		setTimeScale = new JMenuItem("Set simulation speed");
		setScaleFactor = new JMenuItem("Set distance scale");
		
		menuFile.add(startSim);
		menuFile.add(stopSim);
		menuFile.add(setScaleFactor);
		menuFile.add(setTimeScale);
		menuFile.add(saveOption);
		menuFile.add(loadOption);
		menuFile.add(addShipOption);
		menuFile.add(exitOption);
		
		startSim.setEnabled(false);
		stopSim.setEnabled(false);
		saveOption.setEnabled(false);
		loadOption.setEnabled(false);
		
		shipSettings = new JMenu("Ship");
		menuBar.add(shipSettings);
		
		engineSettings = new JMenuItem("Ship engines");
		shipSettings.add(engineSettings);
		
		exitOption.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		
		addShipOption.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				addShip=true;
				JOptionPane.showMessageDialog(MainWindow.this, "Place the ship on the map with Left Mouse Button");
				
				startSim.setEnabled(true);
				stopSim.setEnabled(true);
				saveOption.setEnabled(true);
				loadOption.setEnabled(true);	
			}
		});
		
		
		
		startSim.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				illustrate.startTimer();
				startSim.setEnabled(false);
				stopSim.setEnabled(true);
			}
		});
		
		stopSim.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				illustrate.stopTimer();
				startSim.setEnabled(true);
				stopSim.setEnabled(false);				
			}
		});
		
		setTimeScale.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				try
				{
					int secondsToHours = 3600;
					String input = JOptionPane.showInputDialog("Simulation speed: " + illustrate.getTimeScale()/secondsToHours + " hours/second");
					double newTimeScale = Double.parseDouble(input) * secondsToHours;
					illustrate.setTimeScale(newTimeScale);
				}
				catch(Exception e)
				{}
			}
		});
		
		setScaleFactor.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				try
				{
					String input = JOptionPane.showInputDialog("Enlargement: " + illustrate.getScale());
					double newScale = Double.parseDouble(input);
					illustrate.setScale(newScale);
					illustrate.repaint();
				}
				catch(Exception e)
				{}
			}
		});
		
		
		
		
		engineSettings.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				EngineDialog enginePanel = new EngineDialog(MainWindow.this, playerShip);
				enginePanel.setVisible(true);
			}
		});
		
		
	}
	
	public Ship getActualShip()
	{
		return playerShip;
	}

	
}