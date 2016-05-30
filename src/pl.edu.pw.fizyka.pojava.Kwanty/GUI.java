package pl.edu.pw.fizyka.pojava.Kwanty;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import javax.swing.JComponent;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class GUI extends JComponent
{// P.J.
	
	private final double sunRadius = 6.957e8 * 40;
	
	private Planet[] planetModel;
//	private Ship playerShip;
	double shipSize;
	
	MainWindow mainWindow;
	
	private Ellipse2D.Double[] orbits;
	private Ellipse2D.Double[] planets;
	private Ellipse2D.Double[] potentialFields;
	private Ellipse2D.Double sun;
	
//	private Ellipse2D.Double playerShipOrbit;
	
	private double scaleFactor;
	
	private Timer timeSimulator;
	private Timer shipTimer;
	private ShipOrbitCycleThread refreshOrbits;
	
	private double timeScale;
	private final int dt = 1; // time interval in seconds (s)
	
	private double SystemCenterY;
	private double SystemCenterX;

	double distanceToPixel; // Translates a pixel into an amount of meters
	
	public void startTimer(Ship playerShip)
	{// L.M.
		timeSimulator.start();
		shipTimer.start();
		if (refreshOrbits != null)
		{
			refreshOrbits.stopTimer();
		}
		
		refreshOrbits = new ShipOrbitCycleThread(playerShip);
		refreshOrbits.startTimer();
		
	}
	public void stopTimer()
	{// L.M.
		timeSimulator.stop();
		shipTimer.stop();
		refreshOrbits.stopTimer();
	}
	
	public GUI(Planet[] planetModel, MainWindow mainWindow)
	{
		this.mainWindow = mainWindow;
		
		this.scaleFactor = 1.0;
		this.planetModel = planetModel;
		this.orbits = new Ellipse2D.Double[8];
		this.planets = new Ellipse2D.Double[8];
		this.sun = new Ellipse2D.Double((-1) * sunRadius, (-1) * sunRadius, 2 * sunRadius, 2 * sunRadius);
		this.potentialFields = new Ellipse2D.Double[9]; // includes the sun
		
		this.shipSize = 5000 * 1e6;
		
		
		
		for (int ii = 0; ii < 8; ii++)
		{
			this.orbits[ii] = new Ellipse2D.Double();
			this.planets[ii] = new Ellipse2D.Double();
		}
		for (int ii = 0; ii < 9; ii++)
		{
			this.potentialFields[ii] = new Ellipse2D.Double();
		}
		
		
		
		this.timeScale = 1.0;
		this.timeSimulator= new Timer(1, new ActionListener()
		{// L.M. (timer) + P.J(RK4)
			
			public void actionPerformed(ActionEvent arg0)
			{
//				Ship playerShip = mainWindow.getActualShip();
				for (int ii = 0; ii < planetModel.length; ++ii)
				{
					planetModel[ii].RK4(dt * timeScale/1000);
					
				}
//				playerShip.shipAcceleration(planetModel, dt);
//				playerShip.RK4(dt * timeScale/1000);
				
				repaint();
			}
		});
		
		
		this.shipTimer = new Timer(1, new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				Ship playerShip = mainWindow.getActualShip();
				playerShip.shipAcceleration(planetModel, dt);
				playerShip.RK4(dt * timeScale/1000);
				refreshOrbits.getActualShip(mainWindow.getActualShip());
	//			refreshOrbits.updateDaemonShip();
				
				repaint();
				
						
			}
		});
		
//		generatePlanetaryOrbits();
		LoadOrbits orbitPaths = new LoadOrbits(this.mainWindow);
		orbitPaths.generatePlanetaryOrbits(planetModel);
		
//		repaint();
	}
	
	
	
	
	
	
	
	public void orbitRender() // P.J.
	{// P.J.
		
		double planetaryRadius = 0;
		potentialFields[0].setFrame((-1.5) * sunRadius, (-1.5) * sunRadius, 3 * sunRadius, 3 * sunRadius);
		
		for (int ii = 0; ii < planetModel.length; ++ii)
		{
//			planetaryRadius = planetModel[ii].getplanetaryRadius() * getScale() * 30;
			planetaryRadius = planetModel[ii].getplanetaryRadius();
//			orbits[ii].setFrame((-1) * planetModel[ii].getMinRadius(), (-1) * planetModel[ii].getSemiMinorAxis(), 2 * planetModel[ii].getSemiMajorAxis(), 2 * planetModel[ii].getSemiMinorAxis());
			planets[ii].setFrame(planetModel[ii].getX() - planetaryRadius, planetModel[ii].getY() - planetaryRadius, 2 * planetaryRadius, 2 * planetaryRadius);
			potentialFields[ii + 1].setFrame(planetModel[ii].getX() - 50 * planetaryRadius, planetModel[ii].getY() - 50 * planetaryRadius, 100 * planetaryRadius, 100 * planetaryRadius);
		}
		
	}

	public void paintComponent(Graphics g)
	{ // P.J.
		
		double screenMaxWidth = 2 * planetModel[7].getMaxRadius();
		
		distanceToPixel = scaleFactor * (double)this.getWidth()/screenMaxWidth;
		
		Graphics2D g2 = (Graphics2D) g;
		
		
		
		
		g2.translate(SystemCenterX, SystemCenterY);
		g2.scale(distanceToPixel, distanceToPixel);
		
		g2.setPaint(new Color(18, 55, 34));
		for (int ii = 0; ii < potentialFields.length; ++ii)
		{
			g2.fill(potentialFields[ii]);
		}
		g2.setPaint(Color.YELLOW);
		g2.fill(sun);
		
		orbitRender();
		
		
		g2.setPaint(Color.RED);
		for (int ii = 0; ii < orbits.length; ++ii)
		{
			
			g2.draw(planetModel[ii].getorbitPath());
//			g2.draw(orbits[ii]);
		}
		
		
		
		
		g2.setPaint(Color.BLACK);
		for (int ii = 0; ii < planetModel.length; ++ii)
		{
			g2.draw(planets[ii]);
			g2.fill(planets[ii]);
		//	System.out.println(" "+ planets[ii].getBounds2D().getHeight());
		}
		
		Ship playerShip = mainWindow.getActualShip();
		
		if (playerShip!=null)
		{
			g2.setPaint(Color.BLUE);
			g2.fill(playerShip.getShipShape(shipSize));
			g2.draw(playerShip.getOrbitShape());
			
			g2.setPaint(Color.GREEN);
			g2.draw(new Ellipse2D.Double(playerShip.getX(), playerShip.getY(), 1, 1));
			
		}
		
		g2.setPaint(Color.GREEN);
		for (int ii = 0; ii < orbits.length; ++ii)
			g2.draw(new Ellipse2D.Double(planetModel[ii].getX(), planetModel[ii].getY(), 1, 1));
		
		
		
	}
	
	public double getScale()
	{ // L.M.
		return scaleFactor;
	}
	
	public void setScale(double newScale)
	{// L.M.
		if (newScale > 0)
			scaleFactor = newScale;
	}
	
	public double getTimeScale()
	{// L.M.
		return timeScale;
	}
	
	public void setTimeScale(double newTimeModifier)
	{// L.M.
		if (newTimeModifier > 0)
			timeScale = newTimeModifier;
	}
	
	public Point2D.Double translateFromPixeltoMeters(Point2D mousePoint)
	{
		double newPointX = (mousePoint.getX() - SystemCenterX)/distanceToPixel;
		double newPointY = (mousePoint.getY() - SystemCenterY)/distanceToPixel;
		
		return new Point2D.Double(newPointX, newPointY);
	}
	
	public Point2D.Double getCenterInPixels()
	{
		return new Point2D.Double(SystemCenterX, SystemCenterY);
	}
	
	public void setCenterInPixels(Point2D.Double newSystemCenter)
	{
		SystemCenterX = newSystemCenter.getX();
		SystemCenterY = newSystemCenter.getY();
	}
	
	public void printSize()
	{// L.M.
		System.out.println("Height i width" + this.getHeight() + " " + this.getWidth());
	}
	
	public void setDefaultCenter()
	{
		SystemCenterY=(double)this.getHeight()/2;
		SystemCenterX=(double)this.getWidth()/2;
	}
	
	
}