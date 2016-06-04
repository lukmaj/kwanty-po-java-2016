package src.pl.edu.pw.fizyka.pojava.Kwanty;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import javax.swing.JComponent;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class GUI extends JComponent
{// L.M.
	
	private final double sunRadius = 6.957e8 * 40;
	private final int dt = 1; // time interval in seconds (s)
	
	private MainWindow mainWindow;
	
	private Planet[] planetModel;
	private Ellipse2D.Double[] planets;
	private Ellipse2D.Double[] potentialFields;
	private Ellipse2D.Double sun;
	
	private Timer planetsTimer;
	private Timer shipTimer;
	private ShipOrbitCycleThread refreshOrbits;
	
//	private Ellipse2D.Double playerShipOrbit;
	private double shipSize;
	private double scaleFactor;
	private double timeScale;
	private double SystemCenterY;
	private double SystemCenterX;
	private double distanceToPixel; // Translates a pixel into an amount of meters
	
	public void startTimer(Ship playerShip)
	{
		this.planetsTimer.start();
		this.shipTimer.start();
		if (this.refreshOrbits != null)
		{
			this.refreshOrbits.stopTimer();
		}
		
		this.refreshOrbits = new ShipOrbitCycleThread(playerShip, this.planetModel, this.sunRadius);
		this.refreshOrbits.startTimer();
		
	}
	public void stopTimer()
	{
		this.planetsTimer.stop();
		this.shipTimer.stop();
		this.refreshOrbits.stopTimer();
	}
	
	public boolean isSimulationRunning()
	{
		// Checking one timer is enough, because they either work simultaneously or not at all
		if (this.planetsTimer.isRunning())
			return true;
		else
			return false;
	}
	public GUI(Planet[] planetModel, MainWindow mainWindow)
	{
		this.mainWindow = mainWindow;
		
		this.scaleFactor = 1.0;
		this.planetModel = planetModel;
		this.planets = new Ellipse2D.Double[8];
		this.sun = new Ellipse2D.Double((-1) * this.sunRadius, (-1) * this.sunRadius, 2 * this.sunRadius, 2 * this.sunRadius);
		this.potentialFields = new Ellipse2D.Double[9]; // includes the sun
		
		this.shipSize = 5000 * 1e6;
		
		this.addMouseListener(new GUIMouseListener());
		this.addMouseMotionListener(new MouseDragListener());
		this.addMouseWheelListener(new MouseScaleListener());
		
		for (int ii = 0; ii < 8; ii++)
		{
			this.planets[ii] = new Ellipse2D.Double();
		}
		for (int ii = 0; ii < 9; ii++)
		{
			this.potentialFields[ii] = new Ellipse2D.Double();
		}
		

		this.timeScale = 1.0;
		this.planetsTimer= new Timer(1, new PlanetTimerListener());
		this.shipTimer = new Timer(1, new ShipTimerListener());

		LoadOrbitsDialog orbitPaths = new LoadOrbitsDialog(this.mainWindow);
		orbitPaths.generatePlanetaryOrbits(this.planetModel);
	}
	
	
	
	
	
	
	
	public void orbitRender() // P.J.
	{// P.J.
		
		double planetaryRadius = 0;
		potentialFields[0].setFrame((-1.5) * this.sunRadius, (-1.5) * this.sunRadius, 3 * this.sunRadius, 3 * this.sunRadius);
		
		for (int ii = 0; ii < this.planetModel.length; ++ii)
		{
			planetaryRadius = this.planetModel[ii].getplanetaryRadius();
			this.planets[ii].setFrame(this.planetModel[ii].getX() - planetaryRadius, this.planetModel[ii].getY() - planetaryRadius, 2 * planetaryRadius, 2 * planetaryRadius);
			this.potentialFields[ii + 1].setFrame(this.planetModel[ii].getX() - 50 * planetaryRadius, this.planetModel[ii].getY() - 50 * planetaryRadius, 100 * planetaryRadius, 100 * planetaryRadius);
		}
		
	}

	public void paintComponent(Graphics g)
	{ // P.J.
		
		double screenMaxWidth = 2 * this.planetModel[7].getMaxRadius();
		this.distanceToPixel = this.scaleFactor * (double)this.getWidth()/screenMaxWidth;
		
		Graphics2D g2 = (Graphics2D) g;
		g2.translate(this.SystemCenterX, this.SystemCenterY);
		g2.scale(this.distanceToPixel, this.distanceToPixel);
		
		
		g2.setPaint(new Color(18, 55, 34));
		for (int ii = 0; ii < this.potentialFields.length; ++ii)
			g2.fill(this.potentialFields[ii]);
		
		
		g2.setPaint(Color.YELLOW);
		g2.fill(this.sun);
		
		orbitRender();
		
		
		g2.setPaint(Color.RED);
		for (int ii = 0; ii < this.planets.length; ++ii)
			g2.draw(this.planetModel[ii].getorbitPath());
		
		
		g2.setPaint(Color.BLACK);
		for (int ii = 0; ii < this.planetModel.length; ++ii)
		{
			g2.draw(this.planets[ii]);
			g2.fill(this.planets[ii]);
		}
		
		Ship playerShip = this.mainWindow.getActualShip();
		
		if (playerShip!=null)
		{
			g2.setPaint(Color.BLUE);
			g2.fill(playerShip.getShipShape(this.shipSize));
			g2.draw(playerShip.getOrbitShape());
			
			g2.setPaint(Color.GREEN);
			g2.draw(new Ellipse2D.Double(playerShip.getX(), playerShip.getY(), 1, 1));
			
		}
		
		g2.setPaint(Color.GREEN);
		for (int ii = 0; ii < this.planets.length; ++ii)
			g2.draw(new Ellipse2D.Double(this.planetModel[ii].getX(), this.planetModel[ii].getY(), 1, 1));
		
		
		
	}
	
	public double getScale()
	{
		return this.scaleFactor;
	}
	
	public void setScale(double newScale)
	{
		if (newScale > 0)
			this.scaleFactor = newScale;
	}
	
	public double getTimeScale()
	{
		return this.timeScale;
	}
	
	public void setTimeScale(double newTimeModifier)
	{
		if (newTimeModifier > 0)
			this.timeScale = newTimeModifier;
	}
	
	public Point2D.Double translateFromPixeltoMeters(Point2D mousePoint)
	{
		double newPointX = (mousePoint.getX() - this.SystemCenterX)/this.distanceToPixel;
		double newPointY = (mousePoint.getY() - this.SystemCenterY)/this.distanceToPixel;
		
		return new Point2D.Double(newPointX, newPointY);
	}
	
	public Point2D.Double getCenterInPixels()
	{
		return new Point2D.Double(this.SystemCenterX, this.SystemCenterY);
	}
	
	public void setCenterInPixels(Point2D.Double newSystemCenter)
	{
		this.SystemCenterX = newSystemCenter.getX();
		this.SystemCenterY = newSystemCenter.getY();
	}
	
	public void printSize()
	{
		System.out.println("Height & width" + this.getHeight() + " " + this.getWidth());
	}
	
	public void setDefaultCenter()
	{
		this.SystemCenterY=(double)this.getHeight()/2;
		this.SystemCenterX=(double)this.getWidth()/2;
	}
	
	class GUIMouseListener implements MouseListener
	{
		public void mouseClicked(MouseEvent arg0)
		{
			
			if (arg0.getClickCount() > 1 && GUI.this.mainWindow.addShip)
			{
				Point2D.Double shipLocation = GUI.this.translateFromPixeltoMeters(new Point2D.Double(arg0.getX(), arg0.getY()));
				double shipRadius = Math.sqrt(Math.pow(shipLocation.getX(), 2) + Math.pow(shipLocation.getY(), 2));
				GUI.this.mainWindow.playerShip = new Ship(30e3, shipRadius, shipLocation);
				LoadShipOrbit shipOrbit = new LoadShipOrbit(GUI.this.mainWindow.playerShip);
				shipOrbit.generateShipOrbit();
				GUI.this.mainWindow.addShip = false;

				
				GUI.this.repaint();
			}
		}

		public void mouseEntered(MouseEvent arg0)
		{}

		public void mouseExited(MouseEvent arg0) 
		{}

		public void mousePressed(MouseEvent arg0)
		{
			GUI.this.mainWindow.startPoint = arg0.getPoint();
			GUI.this.mainWindow.oldCenter = GUI.this.getCenterInPixels();
		}

		public void mouseReleased(MouseEvent arg0)
		{
			double newCenterX = GUI.this.mainWindow.oldCenter.getX() + arg0.getPoint().getX() - GUI.this.mainWindow.startPoint.getX();
			double newCenterY = GUI.this.mainWindow.oldCenter.getY() + arg0.getPoint().getY() - GUI.this.mainWindow.startPoint.getY();
			
			GUI.this.setCenterInPixels(new Point2D.Double(newCenterX, newCenterY));
			
			GUI.this.repaint();
		}
	}
	
	class MouseDragListener extends MouseMotionAdapter
	{
		public void mouseDragged(MouseEvent e)
		{
			{
				double newCenterX = GUI.this.mainWindow.oldCenter.getX() + (e.getPoint().getX() - GUI.this.mainWindow.startPoint.getX());
				double newCenterY = GUI.this.mainWindow.oldCenter.getY() + (e.getPoint().getY() - GUI.this.mainWindow.startPoint.getY());
				
				GUI.this.setCenterInPixels(new Point2D.Double(newCenterX, newCenterY));
				GUI.this.repaint();
			}
		}	
	}
	
	class MouseScaleListener implements MouseWheelListener
	{
		public void mouseWheelMoved(MouseWheelEvent arg0)
		{
			if(arg0.getPreciseWheelRotation() < 0)
			{
				GUI.this.setScale((GUI.this.getScale() - arg0.getPreciseWheelRotation() * Math.sqrt(GUI.this.getScale())));
			}
			else if (arg0.getPreciseWheelRotation() > 0)
			{
				GUI.this.setScale((GUI.this.getScale() - arg0.getPreciseWheelRotation() * Math.sqrt(GUI.this.getScale())));
			}
			GUI.this.repaint();
		}
	}
	
	class ShipTimerListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			Ship playerShip = GUI.this.mainWindow.getActualShip();
			playerShip.shipAcceleration(GUI.this.dt, GUI.this.planetModel);

			if (!playerShip.isInsidePotentialField)
				playerShip.RK4AroundSun(GUI.this.dt * GUI.this.timeScale, GUI.this.planetModel);
			
			else
				playerShip.RK4AroundPlanet(GUI.this.dt * GUI.this.timeScale, GUI.this.planetModel);

			
			GUI.this.refreshOrbits.getActualShip(playerShip);

			repaint();
			
			if (playerShip.isInsideSun())
			{
				GUI.this.stopTimer();
				EndGameDialog.shipInsideSun(GUI.this.mainWindow);
			}
			else if (!playerShip.isInsideSolarSystem())
			{
				GUI.this.stopTimer();
				EndGameDialog.shipOutsideSolarSystem(GUI.this.mainWindow);
			}
			
			
			
			
			// TO DO
			
			/*
			else if (playerShip.hasLandedOnPlanet(this.planetModel))
			{
				GUI.this.stopTimer();
				EndGameDialog.shipLandedOnPlanet(GUI.this.mainWindow);
			}
			else if (playerShip.hasCrashedWithPlanet(this.planetModel))
			{
				GUI.this.stopTimer();
				EndGameDialog.shipCrashedWithPlanet(GUI.this.mainWindow);
			}
			*/
		}
	}
	
	class PlanetTimerListener implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0)
		{
			for (int ii = 0; ii < GUI.this.planetModel.length; ++ii)
			{
				GUI.this.planetModel[ii].RK4(GUI.this.dt * GUI.this.timeScale);
			}

			
			repaint();
		}
	}
}
	
	
