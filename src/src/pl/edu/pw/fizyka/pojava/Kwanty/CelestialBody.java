package src.pl.edu.pw.fizyka.pojava.Kwanty;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

public abstract class CelestialBody implements Runnable
{ // P.J./L.M
	static protected final double G = 6.667408e-11;
	static protected final double solarMass = 1.98855e30;
	static protected final double GMs = G*solarMass;
	static protected final double sunRadius = 6.957e8 * 40;
	static private final double UranusRadius = 4537.368e9; // Serves to define bounds of Solar System
	
	//Celestial Body parameters
	protected double bodyMass; // kg	
	protected double maxRadius; // m
	protected double minRadius; // m
	protected double actualRadius; // m
	protected double tempMinRadius;
	protected double tempMaxRadius;
	protected double a; // m/s^2
	protected double v; // m/s

	// Planar orbit parameters
	protected double ax;
	protected double ay;
	protected double vx;
	protected double vy;
	protected double x; 
	protected double y;
	
	// Angular orbit Parameters
	protected double beginAngle; // starting angle of orbit
	protected double halfOrbitAngle; // beginAngle + 180 degrees
	protected double actualAngle;
	protected double maxAngle;
	protected double minAngle;
	
	protected boolean flag360; // checks if actualAngle is over 360 deg
	protected boolean halfOrbit; // true if planet is currently at halfOrbitAngle
	
	// General orbit parameters
	protected double eccentricity;
	protected double majorOrbitSemiAxis;
	protected double minorOrbitSemiAxis;
	protected GeneralPath orbitPath;
	
	protected double dt;
	
	
	
	CelestialBody(double mass, double maxRadius, double minRadius)
	{
		this.bodyMass = mass;
		this.maxRadius = maxRadius;
		this.minRadius = minRadius;
		this.eccentricity = (this.maxRadius - this.minRadius)/(this.maxRadius + this.minRadius);
		
		this.majorOrbitSemiAxis = (this.maxRadius + this.minRadius)/2;
		this.minorOrbitSemiAxis = Math.sqrt(this.minRadius) * Math.sqrt(this.maxRadius);
		
		this.orbitPath = new GeneralPath();
	}
	
	public void initValue()
	{
		this.actualRadius = Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
		
		this.v = Math.sqrt((GMs + G*this.bodyMass) * ((2/(this.actualRadius)) - (1/(this.majorOrbitSemiAxis))));	
		this.vx = this.v * (this.y/(this.actualRadius));
		this.vy = this.v * (-this.x/(this.actualRadius));
		
		this.a = (-1) * GMs/Math.pow(this.actualRadius, 2);
		this.ax = this.a * (this.x/this.actualRadius);
		this.ay = this.a * (this.y/this.actualRadius);
		
		this.halfOrbit = false;
		this.beginAngle = Math.toDegrees(Math.atan2(-y, x));
		if (beginAngle<0)
			beginAngle += 360;
		
		this.halfOrbitAngle = (beginAngle + 180.0) % 360;
		this.actualAngle = Math.toDegrees(Math.atan2(-y, x));
		this.minAngle = this.beginAngle;
		this.maxAngle = this.beginAngle;
	}
		
	public void RK4(double dt)
	{ // Runge Kutta 4th order ODE simulation
		
		double x1 = this.x;
		double vx1 = this.vx;
		double ax1 = this.a * (x1/this.actualRadius);
			
		double x2 = this.x + 0.5 * vx1 * dt;
		double vx2 = this.vx + 0.5 * ax1 * dt;
		double ax2 = this.a * (x2/this.actualRadius);
			
		double x3 = this.x + 0.5 * vx2 * dt;
		double vx3 = this.vx + 0.5 * ax2 * dt;
		double ax3 = this.a * (x3/this.actualRadius);
			
		double x4 = this.x + vx3 * dt;
		double vx4 = this.vx + ax3 * dt;
		double ax4 = this.a * (x4/this.actualRadius);
			
		double y1 = this.y;
		double vy1 = this.vy;
		double ay1 = this.a * (y1/this.actualRadius);
			
		double y2 = this.y + 0.5 * vy1 * dt;
		double vy2 = this.vy + 0.5 * ay1 * dt;
		double ay2 = this.a * (y2/this.actualRadius);
			
		double y3 = this.y + 0.5 * vy2 * dt;
		double vy3 = this.vy + 0.5 * ay2 * dt;
		double ay3 = this.a * (y3/this.actualRadius);
			
		double y4 = this.y + vy3 * dt;
		double vy4 = this.vy + ay3 * dt;
		double ay4 = this.a * (y4/this.actualRadius);
			
		this.x = this.x + (dt/6) * (vx1 + 2 * vx2 + 2 * vx3 + vx4);
		this.y = this.y + (dt/6) * (vy1 + 2 * vy2 + 2 * vy3 + vy4);
			
			
		this.vx = this.vx + (dt/6) * (ax1 + 2 * ax2 + 2 * ax3 + ax4);
		this.vy = this.vy + (dt/6) * (ay1 + 2 * ay2 + 2 * ay3 + ay4);
			
		this.actualRadius = Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
		this.v = Math.sqrt(Math.pow(this.vx, 2) + Math.pow(this.vy, 2));
		this.a = (-1) * GMs/Math.pow(this.actualRadius, 2);
	}
	
	
	public boolean fullOrbit(double xCenter, double yCenter) 
	{// returns true if orbit is complete
		
		double oldAngle = this.actualAngle;
		this.actualAngle = Math.toDegrees(Math.atan2(-(this.y - yCenter), this.x - xCenter));
		if (this.actualAngle < 0)
			this.actualAngle += 360.0;
		
		if(oldAngle - this.actualAngle > 180)
			this.flag360 = true;
			
			
		if (this.actualRadius > this.tempMaxRadius)
		{
			this.tempMaxRadius = this.actualRadius;
			this.maxAngle = this.actualAngle;
		}
		else if(this.actualRadius < this.tempMinRadius)
		{
			this.tempMinRadius = this.actualRadius;
			this.minAngle = this.actualAngle;
		}
		
		if (this.halfOrbitAngle > this.beginAngle)
		{
			if(this.actualAngle >= this.halfOrbitAngle)
			{
				if (!this.halfOrbit)
				{
					this.halfOrbit = true;
				}
				
				return false;
			}
			
			if (this.halfOrbit && this.actualAngle >= this.beginAngle && this.actualAngle < this.halfOrbitAngle)
			{
				this.halfOrbit = false;
				this.maxRadius = this.tempMaxRadius;
				this.minRadius = this.tempMinRadius;
				
				return true;
				
			}
		}
		else //halfOrbitAngle < beginAngle
		{
			if(this.actualAngle >= this.halfOrbitAngle && this.actualAngle < this.beginAngle)
			{
				if (!halfOrbit)
				{
					halfOrbit = true;
				}
				
				return false;
			}
				
			if (this.halfOrbit && this.actualAngle >= this.beginAngle)
			{
				this.maxRadius = this.tempMaxRadius;
				this.minRadius = this.tempMinRadius;
				
				return true;
			}
		}
		
		return false;
	}
	
	public void run()
	{
		this.orbitPath = generateOneOrbit(this.dt);
	}
	
	
	public GeneralPath getOrbitShape()
	{
		return this.orbitPath;
	}
	
	public void setOrbitShape(GeneralPath newOrbitShape)
	{
		this.orbitPath = newOrbitShape;
	}
	
	public GeneralPath generateOneOrbit(double dt)
	{
		GeneralPath newOrbit = new GeneralPath();
		
		Point2D.Double pointBegin = new Point2D.Double(this.x, this.y);
		final Point2D.Double pointStart = pointBegin;
		Point2D.Double pointEnd = null;
		
		final Point2D.Double centerPoint = new Point2D.Double(0, 0); 
		Line lineBegin = new Line(centerPoint, new Point2D.Double(this.x, this.y));
		final Line lineStart = lineBegin;
		Line lineEnd = null;
		
		Point2D.Double controlPoint;
		
		newOrbit.moveTo(this.x, this.y);
		double lastAngle = this.actualAngle;
		while (!fullOrbit(0, 0))
		{
			RK4(dt);
			if(Math.abs(lastAngle - this.actualAngle) >= 1)
			{
				lastAngle = this.actualAngle;
				pointEnd = new Point2D.Double(this.x, this.y);
				lineEnd = new Line(centerPoint, pointEnd);
				controlPoint = Line.intersectionPoint(lineBegin.perpendicularLine(pointBegin), lineEnd.perpendicularLine(pointEnd));
				newOrbit.quadTo(controlPoint.getX(), controlPoint.getY(), pointEnd.getX(), pointEnd.getY());
				lineBegin = lineEnd;
				pointBegin = pointEnd;
			}
			
		}
		
		
		controlPoint = Line.intersectionPoint(lineEnd.perpendicularLine(pointEnd), lineStart.perpendicularLine(pointStart));
		newOrbit.quadTo(pointEnd.getX(), pointEnd.getY(), pointStart.getX(), pointStart.getY());
		
		return newOrbit;
	}
	
	public double distanceFromSun()
	{
		double radiusSquared = Math.pow(this.x, 2) + Math.pow(this.y, 2);
		
		return Math.sqrt(radiusSquared);
	}
	
	public boolean isInsideSun()
	{
		return distanceFromSun() < CelestialBody.sunRadius;
	}
	
	public boolean isInsideSolarSystem()
	{// Uranus was chosen because it's the last planet in solar system
		
		return distanceFromSun() < CelestialBody.UranusRadius * 2;
	}
	
	
	// FIX BOTH FUNCTIONS
	
	
	
	
	
	/*
	public boolean hasCrashedWithPlanet(Planet[] planetModel)
	{
		boolean isInsidePlanet;
		for (int ii = 0; ii < planetModel.length; ++ii)
		{
			isInsidePlanet = (this.getX() <= (planetModel[ii].getX() - 2 * planetModel[ii].getplanetaryRadius()) && this.getY() <= (planetModel[ii].getY() - 2 * planetModel[ii].getplanetaryRadius()));
			if (isInsidePlanet)
			{
				double planetVelocity = Math.sqrt(Math.pow(planetModel[ii].getVX(), 2) + Math.pow(planetModel[ii].getVY(), 2));
				double shipVelocity = Math.sqrt(Math.pow(this.getVX(), 2) + Math.pow(this.getVY(), 2));
				if(shipVelocity > 3 * planetVelocity)
					return true;
			}
		}
		
		return false;
	}
	
	public boolean hasLandedOnPlanet(Planet[] planetModel)
	{
		boolean isInsidePlanet;
		for (int ii = 0; ii < planetModel.length; ++ii)
		{
			isInsidePlanet = (this.getX() <= (planetModel[ii].getX() - 2 * planetModel[ii].getplanetaryRadius()) && this.getY() <= (planetModel[ii].getY() - 2 * planetModel[ii].getplanetaryRadius()));
			if (isInsidePlanet)
			{
				double planetVelocity = Math.sqrt(Math.pow(planetModel[ii].getVX(), 2) + Math.pow(planetModel[ii].getVY(), 2));
				double shipVelocity = Math.sqrt(Math.pow(this.getVX(), 2) + Math.pow(this.getVY(), 2));
				if(shipVelocity < 3 * planetVelocity)
					return true;
			}
		}
		
		return false;
	}
	
	*/
	
	// Planet parameters getters
	public double getbodyMass()
	{
		return this.bodyMass;
	}
	
	// General Orbit parameters getters
	
	
	public double getSemiMajorAxis()
	{
		return this.majorOrbitSemiAxis;
	}
		
	public double getSemiMinorAxis()
	{
		return this.minorOrbitSemiAxis;
	}
	
	public double getMinRadius()
	{
		return this.minRadius;
	}
		
	public double getMaxRadius()
	{
		return this.maxRadius;
	}
		
	public double getActualRadius()
	{
		return this.actualRadius;
	}
		
	public double getMaxAngle()
	{
		return this.maxAngle;
	}
		
	public double getMinAngle()
	{
		return this.minAngle;
	}
		
	public double getActualAngle()
	{
		return this.actualAngle;
	}
		
	public double getX()
	{
		return this.x;
	}
		
	public double getY()
	{
		return this.y;
	}
		
	public double getVX()
	{
		return this.vx;
	}
		
	public double getVY()
	{
		return this.vy;
	}
		
	public double getAX()
	{
		return this.ax;
	}
		
	public double getAY()
	{
		return this.ay;
	}

	public void setdt(double newdt)
	{
		this.dt = newdt;
	}
	

}
