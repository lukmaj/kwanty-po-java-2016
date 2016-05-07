package pl.edu.pw.fizyka.pojava.Kwanty;

import java.awt.geom.GeneralPath;

// Klasa P.J.

public class Planet 
{
	static private final double G = 6.667408e-11;
	static private final double solarMass=1.98855e30;
	static private final double GMs=G*solarMass;
	private final double planetMass; // kg
	
	
	private double maxRadius; // km
	private double minRadius; // km
	private double actualRadius;
	
	private double a;
	private double v;

	private double ax;
	private double ay;
	private double vx;
	private double vy;
	private double x; 
	private double y;
	
	private double beginAngle; // starting angle of orbit
	private final double halfOrbitAngle; // beginAngle + 180 degrees
	private double actualAngle;
	private double maxAngle;
	private double minAngle;
	
	private boolean flag360; // checks if actualAngle is over 360 deg
	private boolean halfOrbit; // true if planet is currently at halfOrbitAngle
	
	private double tempMaxRadius;
	private double tempMinRadius;
	
	private double eccentricity;
	private double majorOrbitSemiAxis;
	private double minorOrbitSemiAxis;
	
//	GeneralPath orbitPoints;
	
	
	public Planet(double mass, double maxRadius, double minRadius)
	{
		this.planetMass = mass;
		this.maxRadius = maxRadius*1000; // switch to meters
		this.minRadius = minRadius*1000; // switch to meters
		
		this.eccentricity=(this.maxRadius-this.minRadius)/(this.maxRadius+this.minRadius);
		System.out.println("Eccentrycznosc: "+ this.eccentricity);
		
		this.majorOrbitSemiAxis=(this.maxRadius+this.minRadius)/2;
		this.minorOrbitSemiAxis=Math.sqrt(this.minRadius) * Math.sqrt(this.maxRadius);
		
		double c = this.eccentricity * majorOrbitSemiAxis;
		
		System.out.println("a = " + this.majorOrbitSemiAxis + " b = "+ this.minorOrbitSemiAxis+ " c = " + c);
		
		this.x = Math.random() * (this.maxRadius+this.minRadius) - this.minRadius;
		this.y = minorOrbitSemiAxis * Math.sqrt(1 - Math.pow((x-c)/majorOrbitSemiAxis, 2));
		if (Math.random() < 0.5) // 50% chance of being on upper half of orbit
			this.y = -y;
		
		/*
		this.x = (Math.random() * 2 * this.maxRadius - this.maxRadius);
		this.y = this.minRadius * Math.sqrt(1 - Math.pow(x/this.maxRadius, 2));
		if (Math.random() < 0.5) // 50% chance of being on upper half of orbit
			this.y = -y;
		*/
	//	this.orbitPoints=new GeneralPath();
	//	orbitPoints.moveTo(this.x, this.y);
		
		this.halfOrbit = false;
		this.beginAngle = Math.toDegrees(Math.atan2(-y, x));
		if (beginAngle<0)
			beginAngle+=360;
		
		this.halfOrbitAngle =(beginAngle + 180.0) % 360;
		
		actualAngle=Math.toDegrees(Math.atan2(-y, x));
		this.minAngle=this.beginAngle;
		this.maxAngle=this.beginAngle;
		
	//	System.out.println("beginAngle "+ beginAngle + " halfOrbitAngle "+ halfOrbitAngle);
		
	//	this.x=this.maxRadius;
	//	this.y=-this.minRadius*Math.sqrt(1-Math.pow(x/this.maxRadius, 2));
		this.actualRadius = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		
		this.tempMaxRadius=this.tempMinRadius=this.actualRadius;
		
		v=Math.sqrt((GMs + G*this.planetMass) * ((2/(this.actualRadius)) - (1/(this.majorOrbitSemiAxis))));	
		vx = v * (y/(this.actualRadius));
		vy = v * (-x/(this.actualRadius));
		
		a=-GMs/Math.pow(actualRadius, 2);
		ax=a * (x/this.actualRadius);
		ay=a * (y/this.actualRadius);
		
	}
	
	
	public void RK4(double dt)
	{
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
		//v=Math.sqrt(GMs*((2/(this.actualRadius))-(1/(this.maxRadius))));
		v = Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
		a = -GMs/Math.pow(actualRadius, 2);
		
		
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

	public double getSemiMajorAxis()
	{
		return this.majorOrbitSemiAxis;
	}
	
	public double getSemiMinorAxis()
	{
		return this.minorOrbitSemiAxis;
	}
	
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
}