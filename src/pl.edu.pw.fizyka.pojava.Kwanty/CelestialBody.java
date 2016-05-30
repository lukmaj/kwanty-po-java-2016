package pl.edu.pw.fizyka.pojava.Kwanty;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

public abstract class CelestialBody implements Runnable
{ // P.J./L.M
	static protected final double G = 6.667408e-11;
	static protected final double solarMass = 1.98855e30;
	static protected final double GMs = G*solarMass;
	
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
	
	// Angular orbit Parameters (necessary?)
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
		
		orbitPath = new GeneralPath();
	}
	
	public void initValue()
	{
		this.actualRadius = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		
		v = Math.sqrt((GMs + G*this.bodyMass) * ((2/(this.actualRadius)) - (1/(this.majorOrbitSemiAxis))));	
		vx = v * (y/(this.actualRadius));
		vy = v * (-x/(this.actualRadius));
		
		a = (-1) * GMs/Math.pow(actualRadius, 2);
		ax = a * (x/this.actualRadius);
		ay = a * (y/this.actualRadius);
		
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
		
		double x1 = x;
		double vx1 = vx;
		double ax1 = a * (x1/this.actualRadius);
			
		double x2 = x + 0.5 * vx1 * dt;
		double vx2 = vx + 0.5 * ax1 * dt;
		double ax2 = a * (x2/this.actualRadius);
			
		double x3 = x + 0.5 * vx2 * dt;
		double vx3 = vx + 0.5 * ax2 * dt;
		double ax3 = a * (x3/this.actualRadius);
			
		double x4 = x + vx3 * dt;
		double vx4 = vx + ax3 * dt;
		double ax4 = a * (x4/this.actualRadius);
			
		double y1 = y;
		double vy1 = vy;
		double ay1 = a * (y1/this.actualRadius);
			
		double y2 = y + 0.5 * vy1 * dt;
		double vy2 = vy + 0.5 * ay1 * dt;
		double ay2 = a * (y2/this.actualRadius);
			
		double y3 = y + 0.5 * vy2 * dt;
		double vy3 = vy + 0.5 * ay2 * dt;
		double ay3 = a * (y3/this.actualRadius);
			
		double y4 = y + vy3 * dt;
		double vy4 = vy + ay3 * dt;
		double ay4 = a * (y4/this.actualRadius);
			
		x = x + (dt/6) * (vx1 + 2 * vx2 + 2 * vx3 + vx4);
		y = y + (dt/6) * (vy1 + 2 * vy2 + 2 * vy3 + vy4);
			
			
		vx = vx + (dt/6) * (ax1 + 2 * ax2 + 2 * ax3 + ax4);
		vy = vy + (dt/6) * (ay1 + 2 * ay2 + 2 * ay3 + ay4);
			
		actualRadius = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		v = Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
		a = (-1) * GMs/Math.pow(actualRadius, 2);
		
		
	//	ax = a * (x/this.actualRadius);
	//	ay = a * (y/this.actualRadius);
	}
	
	public boolean fullOrbit() // returns true if orbit is complete
	{
		double oldAngle = actualAngle;
		actualAngle = Math.toDegrees(Math.atan2(-y, x));
		if (actualAngle < 0)
			actualAngle += 360.0;
		
		if(oldAngle - actualAngle > 180)
			flag360 = true;
			
			
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
		
		if (halfOrbitAngle > beginAngle)
		{
			if(actualAngle >= halfOrbitAngle)
			{
				if (!halfOrbit)
				{
//					System.out.println("Polowa orbity");
					halfOrbit = true;
				}
				
				return false;
			}
			
			if (halfOrbit && actualAngle >= beginAngle && actualAngle < halfOrbitAngle)
			{
				halfOrbit = false;
//				System.out.println("Aktualizacja orbity");
				
				this.maxRadius = this.tempMaxRadius;
				this.minRadius = this.tempMinRadius;
//				System.out.println("Max angle: "+ maxAngle);
//				System.out.println("Min angle: "+ minAngle);
				return true;
				// aktualizacja elipsy
			}
		}
		else //halfOrbitAngle<beginAngle
		{
			if(actualAngle >= halfOrbitAngle && actualAngle < beginAngle)
			{
				if (!halfOrbit)
				{
//					System.out.println("Polowa orbity");
					halfOrbit = true;
				}
				
				return false;
			}
				
			if (halfOrbit && actualAngle >= beginAngle)
			{
				halfOrbit = false;
//				System.out.println("Aktualizacja orbity");
				
				this.maxRadius = this.tempMaxRadius;
				this.minRadius = this.tempMinRadius;
//				System.out.println("Max angle: "+ maxAngle);
//				System.out.println("Min angle: "+ minAngle);
				return true;
				// aktualizacja elipsy
			}
		}
		
		return false;
	}
	
	
	
	public void run()
	{
		
		orbitPath = generateOneOrbit(dt);
		
		
		/*
		Point2D.Double pointBegin = new Point2D.Double(this.x, this.y);
		final Point2D.Double pointStart = pointBegin;
		Point2D.Double pointEnd = null;
		
		final Point2D.Double centerPoint = new Point2D.Double(0, 0); 
		Line lineBegin = new Line(centerPoint, new Point2D.Double(this.x, this.y));
		final Line lineStart = lineBegin;
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
//				orbitPath.lineTo(this.x, this.y);
				pointEnd = new Point2D.Double(this.x, this.y);
				lineEnd = new Line(centerPoint, pointEnd);
				controlPoint = Line.intersectionPoint(lineBegin.perpendicularLine(pointBegin), lineEnd.perpendicularLine(pointEnd));
//				orbitPath.lineTo(pointEnd.getX(), pointEnd.getY());
				orbitPath.quadTo(controlPoint.getX(), controlPoint.getY(), pointEnd.getX(), pointEnd.getY());
				lineBegin = lineEnd;
//				lineEnd = null;
				pointBegin = pointEnd;
//				pointEnd = null;
			}
			
		}
		
		
		controlPoint = Line.intersectionPoint(lineEnd.perpendicularLine(pointEnd), lineStart.perpendicularLine(pointStart));
		orbitPath.quadTo(pointEnd.getX(), pointEnd.getY(), pointStart.getX(), pointStart.getY());
		
		*/
	}
	
	
	public GeneralPath getOrbitShape()
	{
		return orbitPath;
	}
	
	public void setOrbitShape(GeneralPath newOrbitShape)
	{
		orbitPath = newOrbitShape;
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
		double lastAngle = actualAngle;
		while (!fullOrbit())
		{
			RK4(dt);
			if(Math.abs(lastAngle - actualAngle) >= 1)
			{
				lastAngle = actualAngle;
//				orbitPath.lineTo(this.x, this.y);
				pointEnd = new Point2D.Double(this.x, this.y);
				lineEnd = new Line(centerPoint, pointEnd);
				controlPoint = Line.intersectionPoint(lineBegin.perpendicularLine(pointBegin), lineEnd.perpendicularLine(pointEnd));
//				orbitPath.lineTo(pointEnd.getX(), pointEnd.getY());
				newOrbit.quadTo(controlPoint.getX(), controlPoint.getY(), pointEnd.getX(), pointEnd.getY());
				lineBegin = lineEnd;
//				lineEnd = null;
				pointBegin = pointEnd;
//				pointEnd = null;
			}
			
		}
		
		
		controlPoint = Line.intersectionPoint(lineEnd.perpendicularLine(pointEnd), lineStart.perpendicularLine(pointStart));
		newOrbit.quadTo(pointEnd.getX(), pointEnd.getY(), pointStart.getX(), pointStart.getY());
		
		return newOrbit;
	}
	
	
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
		return actualRadius;
	}
		
	public double getMaxAngle()
	{
		return maxAngle;
	}
		
	public double getMinAngle()
	{
		return minAngle;
	}
		
	public double getActualAngle()
	{
		return actualAngle;
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
		dt = newdt;
	}
	

}
