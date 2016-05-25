package pl.edu.pw.fizyka.pojava.Kwanty;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class Ship extends CelestialBody
{ //PJ
//	private Point2D.Double shipLocation;
//	private final double shipSize = 1e9;
//	private double shipRadius;
	
//	private double engineAcceleration;
	private double engineAccelerationX;
	private double engineAccelerationY;
	

	
	
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
	
	public Ellipse2D.Double getShipShape(double shipSize)
	{
		return new Ellipse2D.Double(this.getX() - shipSize, this.getY() - shipSize, 2 * shipSize, 2 * shipSize);
	}
	
	/// acceleration superposition
	public void shipAcceleration(Planet[] planetModel)
	{
		this.a = 0;
		this.ax = 0;
		this.ay = 0;
		
		
		for (int ii = 0; ii < planetModel.length; ++ii)
		{
			double planetToShipDistanceX = this.x - planetModel[ii].getX();
			double planetToShipDistanceY = this.y - planetModel[ii].getY();			
			double planetToShipDistance = Math.sqrt(Math.pow(planetToShipDistanceX, 2) + Math.pow(planetToShipDistanceY, 2));
			
//			if (planetToShipDistance < 100 * planetModel[ii].getplanetaryRadius())
			{
				double shipAccelerationFromPlanet = planetModel[ii].getAccelerationAtPoint(planetToShipDistance);
				double shipAccelerationFromPlanetX = shipAccelerationFromPlanet * (planetToShipDistanceX/planetToShipDistance);
				double shipAccelerationFromPlanetY = shipAccelerationFromPlanet * (planetToShipDistanceY/planetToShipDistance);
			
				this.ax += shipAccelerationFromPlanetX;
				this.ay += shipAccelerationFromPlanetY;
			}
		}
		
//		double solarRadius = 6.957e8 * 40;
		
//		if (actualRadius < 1.5 * solarRadius)
		{
			double shipAccelerationFromSun = (-1) * Math.pow(40, 2) * GMs/Math.pow(actualRadius, 2); 
			double shipAccelerationFromSunX = shipAccelerationFromSun * (this.x/actualRadius);
			double shipAccelerationFromSunY = shipAccelerationFromSun * (this.y/actualRadius);
		
			this.ax += shipAccelerationFromSunX;
			this.ay += shipAccelerationFromSunY;
//			System.out.println("sun x" + Math.sqrt(Math.pow(ax, 2) + Math.pow(ay, 2)));
		}
		
	//	this.a += engineAcceleration;
		this.ax += engineAccelerationX;
		this.ay += engineAccelerationY;
		
	
	//	this.a=Math.sqrt(Math.pow(this.ax, 2) + Math.pow(this.ay, 2));
		
		System.out.println("ax: "+ this.ax + " ay: " + this.ay);
		
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
	
	public double shipKineticEnergy()
	{
		return (bodyMass * Math.pow(v, 2)/2);
	}


	
}
