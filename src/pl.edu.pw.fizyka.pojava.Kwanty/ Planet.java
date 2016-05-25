package pl.edu.pw.fizyka.pojava.Kwanty;

import java.awt.geom.GeneralPath;

// Klasa P.J.

public class Planet extends CelestialBody
{	
	public static double scaleRadius = 40;
	
	private double firstCosmicVelocity;
	private double secondCosmicVelocity;
	
	private double planetRadius;
	
	
	
	
	public Planet(double mass, double maxRadius, double minRadius, double planetRadius)
	{
		super(mass, maxRadius, minRadius);
		
		
		
		this.planetRadius = planetRadius * scaleRadius;
		
		double c = this.eccentricity * majorOrbitSemiAxis;
		
		this.x = Math.random() * (this.maxRadius + this.minRadius) - this.minRadius;
		this.y = minorOrbitSemiAxis * Math.sqrt(1 - Math.pow((x - c)/majorOrbitSemiAxis, 2));
		if (Math.random() < 0.5) // 50% chance of being on upper half of orbit
			this.y = -y;
		
		initValue();
		
		firstCosmicVelocity = Math.sqrt(G * this.bodyMass/this.planetRadius);
		secondCosmicVelocity = Math.sqrt(2) * firstCosmicVelocity;
	}
	
	public double getAccelerationAtPoint(double distance)
	{
		double a = (-1) * G * Math.pow(scaleRadius, 2) * bodyMass/Math.pow(distance, 2);
		//System.out.println("a: "  + a);
		return a;
	}
	
	public double getPotentialFieldAtPoint(double shipMass, double distance)
	{
		return (G * bodyMass * shipMass/distance);
	}
	
	public double getfirstCosmicVelocity()
	{
		return firstCosmicVelocity;
	}
	
	public double getsecondCosmicVelocity()
	{
		return secondCosmicVelocity;
	}
	
	public double getplanetaryRadius()
	{
		return planetRadius;
	}
	
	public GeneralPath getorbitPath()
	{
		return orbitPath;
	}
	


	/*
	public void run()
	{
		
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
				orbitPath.quadTo(controlPoint.getX(), controlPoint.getY(), pointEnd.getX(), pointEnd.getY());
				lineBegin = lineEnd;
//				lineEnd = null;
				pointBegin = pointEnd;
//				pointEnd = null;
			}
			
		}
		
		
		controlPoint = Line.intersectionPoint(lineEnd.perpendicularLine(pointEnd), lineStart.perpendicularLine(pointStart));
		orbitPath.quadTo(pointEnd.getX(), pointEnd.getY(), pointStart.getX(), pointStart.getY());
		
		
	
		
	}
	*/
	
	
	
	/*
	public boolean fullOrbit() // returns true if orbit is complete
	{
		double oldAngle=actualAngle;
		actualAngle=Math.toDegrees(Math.atan2(-y, x));
		if (actualAngle<0)
			actualAngle+=360.0;
		
		if(oldAngle-actualAngle>180)
			flag360=true;
		
		
		if (this.actualRadius>this.tempMaxRadius)
		{
			this.tempMaxRadius=this.actualRadius;
			this.maxAngle=this.actualAngle;
		}
		else if(this.actualRadius<this.tempMinRadius)
		{
			this.tempMinRadius=this.actualRadius;
			this.minAngle=this.actualAngle;
		}
		//System.out.println(actualAngle);
		if (halfOrbitAngle>beginAngle)
		{
			if(actualAngle>=halfOrbitAngle)
			{
				if (!halfOrbit)
				{
					System.out.println("Polowa orbity");
					halfOrbit=true;
				}
				
				return false;
			}
			
			if (halfOrbit && actualAngle>=beginAngle && actualAngle<halfOrbitAngle)
			{
				halfOrbit=false;
				System.out.println("Aktualizacja orbity");
				
				this.maxRadius=this.tempMaxRadius;
				this.minRadius=this.tempMinRadius;
				System.out.println("Max angle: "+ maxAngle);
				System.out.println("Min angle: "+ minAngle);
				return true;
				// aktualizacja elipsy
			}
		}
		else //halfOrbitAngle<beginAngle
		{
			if(actualAngle>=halfOrbitAngle && actualAngle<beginAngle)
			{
				if (!halfOrbit)
				{
					System.out.println("Polowa orbity");
					halfOrbit=true;
				}
				
				return false;
			}
			
			if (halfOrbit && actualAngle>=beginAngle)
			{
				halfOrbit=false;
				System.out.println("Aktualizacja orbity");
				
				this.maxRadius=this.tempMaxRadius;
				this.minRadius=this.tempMinRadius;
				System.out.println("Max angle: "+ maxAngle);
				System.out.println("Min angle: "+ minAngle);
				return true;
				// aktualizacja elipsy
			}
		}
		
		
		
		return false;
	}
	*/
}
