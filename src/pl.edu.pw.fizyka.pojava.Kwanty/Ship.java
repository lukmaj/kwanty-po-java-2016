package pl.edu.pw.fizyka.pojava.Kwanty;

import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

public class Ship extends CelestialBody
{ //PJ
//	private Point2D.Double shipLocation;
//	private final double shipSize = 1e9;
//	private double shipRadius;
	
//	private double engineAcceleration;
	private double engineAccelerationX;
	private double engineAccelerationY;
	
	private final int c = 299792458;
	

	
	
	public Ship(double mass, double shipOrbitRadius, Point2D.Double shipLocation)
	{
		super(mass, shipOrbitRadius, shipOrbitRadius);
		//this.shipLocation = shipLocation;
		this.x = shipLocation.getX();
		this.y = shipLocation.getY();
		
		this.engineAccelerationX = 0;
		this.engineAccelerationY = 0;
		
	//	this.actualRadius = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		
		initValue();
		
		this.a = 0;
		this.ax = 0;
		this.ay = 0;
	}
	
	public Ship(Ship playerShip)
	{
		super(playerShip.bodyMass, playerShip.maxRadius, playerShip.maxRadius);
		this.x = playerShip.x;
		this.y = playerShip.y;
		
		
		
//		this.engineAccelerationX = playerShip.engineAccelerationX;
//		this.engineAccelerationY = playerShip.engineAccelerationY;
		initValue();
		
		this.vx = playerShip.vx;
		this.vy = playerShip.vy;
		
		this.a = playerShip.a;
		this.ax = playerShip.ax;
		this.ay = playerShip.ay;
		
		this.actualRadius = playerShip.actualRadius;
		this.maxRadius = playerShip.maxRadius;
		this.minRadius = playerShip.minRadius;
		
		this.actualAngle = playerShip.actualAngle;
		this.beginAngle = playerShip.beginAngle;
		this.halfOrbitAngle = playerShip.halfOrbitAngle;
		this.maxAngle = playerShip.maxAngle;
		this.minAngle = playerShip.minAngle;
		
		this.flag360 = playerShip.flag360;
		this.halfOrbit = playerShip.halfOrbit;
		
	}
	
	public void copyShip(Ship playerShip)
	{
		this.x = playerShip.x;
		this.y = playerShip.y;
		
		initValue();
		
		this.vx = playerShip.vx;
		this.vy = playerShip.vy;
		
//		this.engineAccelerationX = playerShip.engineAccelerationX;
//		this.engineAccelerationY = playerShip.engineAccelerationY;
		
		this.a = playerShip.a;
		this.ax = playerShip.ax;
		this.ay = playerShip.ay;
		
		this.actualRadius = playerShip.actualRadius;
		this.maxRadius = playerShip.maxRadius;
		this.minRadius = playerShip.minRadius;
		
		this.actualAngle = playerShip.actualAngle;
		this.beginAngle = playerShip.beginAngle;
		this.halfOrbitAngle = playerShip.halfOrbitAngle;
		this.maxAngle = playerShip.maxAngle;
		this.minAngle = playerShip.minAngle;
		
		this.flag360 = playerShip.flag360;
	//	this.halfOrbit = playerShip.halfOrbit;
	}
	
	public Ellipse2D.Double getShipShape(double shipSize)
	{
		return new Ellipse2D.Double(this.getX() - shipSize, this.getY() - shipSize, 2 * shipSize, 2 * shipSize);
	}
	
	/// acceleration superposition
	public void shipAcceleration(Planet[] planetModel, double dt)
	{
		double ax = 0;
		double ay = 0;
		
		
		for (int ii = 0; ii < planetModel.length; ++ii)
		{
			double planetToShipDistanceX = this.x - planetModel[ii].getX();
			double planetToShipDistanceY = this.y - planetModel[ii].getY();			
			double planetToShipDistance = Math.sqrt(Math.pow(planetToShipDistanceX, 2) + Math.pow(planetToShipDistanceY, 2));
			
			if (planetToShipDistance < 100 * planetModel[ii].getplanetaryRadius())
			{
				double shipAccelerationFromPlanet = planetModel[ii].getAccelerationAtPoint(planetToShipDistance);
				double shipAccelerationFromPlanetX = shipAccelerationFromPlanet * (planetToShipDistanceX/planetToShipDistance);
				double shipAccelerationFromPlanetY = shipAccelerationFromPlanet * (planetToShipDistanceY/planetToShipDistance);
			
				ax += shipAccelerationFromPlanetX;
				ay += shipAccelerationFromPlanetY;
			}
		}
		
		double solarRadius = 6.957e8 * 40;
		
		if (actualRadius < 1.5 * solarRadius)
		{
			double shipAccelerationFromSun = (-1) * Math.pow(40, 2) * GMs/Math.pow(this.actualRadius, 2); 
			double shipAccelerationFromSunX = shipAccelerationFromSun * (this.x/actualRadius);
			double shipAccelerationFromSunY = shipAccelerationFromSun * (this.y/actualRadius);
		
			ax += shipAccelerationFromSunX;
			ay += shipAccelerationFromSunY;
//			System.out.println("sun x" + Math.sqrt(Math.pow(ax, 2) + Math.pow(ay, 2)));
		}
		
	//	this.a += engineAcceleration;
		ax += engineAccelerationX;
		ay += engineAccelerationY;
		
	
		
		this.ax = ax;
		this.ay = ay;
		
		if (vx < c)
			this.vx += ax * dt; // just engine acceleration x???? to test
		else
			this.vx = c;
		
		if (vy < c)
			this.vy += ay * dt;
		else
			this.vy = c;
		
//		System.out.println("ax: "+ this.ax + " ay: " + this.ay);
//		System.out.println("vx: "+ this.vx + " vy: " + this.vy);
		
	}
	
	public double getA()
	{
		return a;
	}
	/*
	public void RK4(double dt)
	{ // Runge Kutta 4th order ODE simulation
		
		double cosalpha = x/actualRadius;
		
		double x1 = x;
		double vx1 = vx;
		double ax1 = (ax/cosalpha) * (x1/this.actualRadius);
			
		double x2 = x + 0.5 * vx1 * dt;
		double vx2 = vx + 0.5 * ax1 * dt;
		double ax2 = (ax/cosalpha) * (x2/this.actualRadius);
			
		double x3 = x + 0.5 * vx2 * dt;
		double vx3 = vx + 0.5 * ax2 * dt;
		double ax3 = (ax/cosalpha) * (x3/this.actualRadius);
			
		double x4 = x + vx3 * dt;
		double vx4 = vx + ax3 * dt;
		double ax4 = (ax/cosalpha) * (x4/this.actualRadius);
			
		double sinalpha = y/actualRadius;
		
		double y1 = y;
		double vy1 = vy;
		double ay1 = (ay/sinalpha) * (y1/this.actualRadius);
		
		double y2 = y + 0.5 * vy1 * dt;
		double vy2 = vy + 0.5 * ay1 * dt;
		double ay2 = (ay/sinalpha) * (y2/this.actualRadius);
			
		double y3 = y + 0.5 * vy2 * dt;
		double vy3 = vy + 0.5 * ay2 * dt;
		double ay3 = (ay/sinalpha) * (y3/this.actualRadius);
			
		double y4 = y + vy3 * dt;
		double vy4 = vy + ay3 * dt;
		double ay4 = (ay/sinalpha) * (y4/this.actualRadius);
			
		x = x + (dt/6) * (vx1 + 2 * vx2 + 2 * vx3 + vx4);
		y = y + (dt/6) * (vy1 + 2 * vy2 + 2 * vy3 + vy4);
			
			
		vx = vx + (dt/6) * (ax1 + 2 * ax2 + 2 * ax3 + ax4);
		vy = vy + (dt/6) * (ay1 + 2 * ay2 + 2 * ay3 + ay4);
			
		actualRadius = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		v = Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
//		a = (-1) * GMs/Math.pow(actualRadius, 2);
		
	//	ax = a * (x/this.actualRadius);
	//	ay = a * (y/this.actualRadius);
	}
*/
	public void engineAcceleration(double accelerationX, double accelerationY)
	{
		engineAccelerationX = accelerationX;
		engineAccelerationY = accelerationY;
	}
	
	
	public double getEngineAccelerationX()
	{
		return engineAccelerationX;
	}
	public double getEngineAccelerationY()
	{
		return engineAccelerationY;
	}
	
	public double shipKineticEnergy()
	{
		return (bodyMass * Math.pow(v, 2)/2);
	}
	
	public GeneralPath generateOneOrbit(double dt)
	{
		GeneralPath newOrbit = new GeneralPath();

		Point2D.Double pointEnd = null;
		newOrbit.moveTo(this.x, this.y);
		double lastAngle = actualAngle;
		while (!fullOrbit())
		{
			RK4(dt);
			if(Math.abs(lastAngle - actualAngle) >= 1)
			{
				lastAngle = actualAngle;
				newOrbit.lineTo(this.x, this.y);
				pointEnd = new Point2D.Double(this.x, this.y);
				newOrbit.lineTo(pointEnd.getX(), pointEnd.getY());
				pointEnd = null;
			}
		}
		
		return newOrbit;
	}

	/*
	public void run()
	{
		
		while(true)
		{
			GeneralPath newOrbit = new GeneralPath();
			
			Point2D.Double pointBegin = new Point2D.Double(this.x, this.y);
			Point2D.Double pointStart = pointBegin;
			Point2D.Double pointEnd = null;
		
			Point2D.Double centerPoint = new Point2D.Double(0, 0); 
			Line lineBegin = new Line(centerPoint, new Point2D.Double(this.x, this.y));
			Line lineStart = lineBegin;
			Line lineEnd = null;
			
			Point2D.Double controlPoint;
			
			orbitPath.moveTo(this.x, this.y);
			double lastAngle = actualAngle;
			
			while (!fullOrbit())
			{
				
				RK4(dt);
				
				
				if(Math.abs(lastAngle - actualAngle) >= 1)
				{
					
					lastAngle = actualAngle;
					pointEnd = new Point2D.Double(this.x, this.y);
					lineEnd = new Line(centerPoint, pointEnd);
					controlPoint = Line.intersectionPoint(lineBegin.perpendicularLine(pointBegin), lineEnd.perpendicularLine(pointEnd));
					orbitPath.quadTo(controlPoint.getX(), controlPoint.getY(), pointEnd.getX(), pointEnd.getY());
					lineBegin = lineEnd;
					pointBegin = pointEnd;
				}
				
			}
			controlPoint = Line.intersectionPoint(lineEnd.perpendicularLine(pointEnd), lineStart.perpendicularLine(pointStart));
			orbitPath.quadTo(pointEnd.getX(), pointEnd.getY(), pointStart.getX(), pointStart.getY());
			
		}
	}
*/
	
}
