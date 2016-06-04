package src.pl.edu.pw.fizyka.pojava.Kwanty;

import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

public class Ship extends CelestialBody
{ //P.J.

	private double engineAccelerationX;
	private double engineAccelerationY;
	
	private final int c = 299792458;
	
	public boolean isCaughtByPotentialField;
	public boolean isInsidePotentialField;
	public int shipCaughtByPlanetNumber;
	
	
	
	public Ship(double mass, double shipOrbitRadius, Point2D.Double shipLocation)
	{
		super(mass, shipOrbitRadius, shipOrbitRadius);
		
		this.x = shipLocation.getX();
		this.y = shipLocation.getY();

		this.engineAccelerationX = 0;
		this.engineAccelerationY = 0;
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
		
		this.v = playerShip.v;
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
		
		this.isInsidePotentialField = playerShip.isInsidePotentialField;
		this.shipCaughtByPlanetNumber = playerShip.shipCaughtByPlanetNumber;
		this.flag360 = playerShip.flag360;
		
	}
	
	public void copyShip(Ship playerShip)
	{
		this.bodyMass = playerShip.bodyMass;
		
		this.x = playerShip.x;
		this.y = playerShip.y;
		
		this.v = playerShip.v;
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
		
		this.isInsidePotentialField = playerShip.isInsidePotentialField;
		this.shipCaughtByPlanetNumber = playerShip.shipCaughtByPlanetNumber;
		this.flag360 = playerShip.flag360;
	
	}
	
	public Ellipse2D.Double getShipShape(double shipSize)
	{
		return new Ellipse2D.Double(this.getX() - shipSize, this.getY() - shipSize, 2 * shipSize, 2 * shipSize);
	}
	

	public void shipAcceleration(double dt, Planet[] planetModel)
	{// acceleration superposition
		
		double ax = 0;
		double ay = 0;
		
		this.isInsidePotentialField = false;

		for (int ii = 0; ii < planetModel.length; ++ii)
		{
			
			double planetToShipDistanceX = this.x - planetModel[ii].getX();
			double planetToShipDistanceY = this.y - planetModel[ii].getY();	
			double planetToShipDistance = Math.sqrt(Math.pow(planetToShipDistanceX, 2) + Math.pow(planetToShipDistanceY, 2));
			planetModel[ii].shipInsidePotentialField = planetToShipDistance < 50 * planetModel[ii].getplanetaryRadius();
			if (planetModel[ii].shipInsidePotentialField)
			{
				this.isInsidePotentialField = true;
				this.shipCaughtByPlanetNumber = ii;
				
				ax += threeBodyProblemAX(planetModel[this.shipCaughtByPlanetNumber]);
				ay += threeBodyProblemAY(planetModel[this.shipCaughtByPlanetNumber]);
			}
		}	
		
		sublightVelocityCheck();
		
		this.ax = ay;
		this.ay = ax;
		this.a = (-1) * Math.sqrt(Math.pow(this.ax, 2) + Math.pow(this.ay, 2));	
	}
	
	public double getA()
	{
		return a;
	}
	
	public void sublightVelocityCheck()
	{
		if (v <= c)
		{
			this.vx += this.engineAccelerationX * this.dt;
			this.vy += this.engineAccelerationY * this.dt;
		}
		else
			this.v = this.c;
		
	}
	
	public void RK4Base(double dt, double x, double y, double vx, double vy, double a)
	{// Serves as calculations Base for other RK4 methods
		
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
			
		this.x = this.x + (dt/6) * (vx1 + 2 * vx2 + 2 * vx3 + vx4);
		this.y = this.y + (dt/6) * (vy1 + 2 * vy2 + 2 * vy3 + vy4);
			
		this.vx = this.vx + (dt/6) * (ax1 + 2 * ax2 + 2 * ax3 + ax4);
		this.vy = this.vy + (dt/6) * (ay1 + 2 * ay2 + 2 * ay3 + ay4);
			
		this.v = Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
	}
	
	
	public void RK4AroundSun(double dt, Planet[] planetModel)
	{
		
		this.actualRadius = Math.sqrt(Math.pow((this.x), 2) + Math.pow(this.y, 2));
		this.a = (-1) * GMs/Math.pow(this.actualRadius, 2);
		
		RK4Base(dt, this.x, this.y, this.vx, this.vy, this.a);
	}
	
	public void RK4AroundPlanet(double dt, Planet[] planetModel)
	{
		double newCenterX = planetModel[this.shipCaughtByPlanetNumber].getX();
		double newCenterY = planetModel[this.shipCaughtByPlanetNumber].getY();
		
		final double shipPlanetaryX = this.x - newCenterX;
		final double shipPlanetaryY = this.y - newCenterY;
		
		this.actualRadius = Math.sqrt(Math.pow((shipPlanetaryX), 2) + Math.pow(shipPlanetaryY, 2));
		
		RK4Base(dt, shipPlanetaryX, shipPlanetaryY, this.vx, this.vy, this.a);
	}
	
	public void DaemonRK4AroundPlanet(double dt, Planet planet, double newCenterX, double newCenterY)
	{
		planet.RK4(dt);

		final double shipPlanetaryX = this.x - newCenterX;
		final double shipPlanetaryY = this.y - newCenterY;
		this.actualRadius = Math.sqrt(Math.pow((shipPlanetaryX), 2) + Math.pow(shipPlanetaryY, 2));
		
		RK4Base(dt, shipPlanetaryX, shipPlanetaryY, this.vx, this.vy, this.a);
	}
	
	public double threeBodyProblemAX(Planet planet)
	{// 1 - sun, 2 - planet, 3 - ship
		double x1 = 0;
		double x2 = planet.getX();
		double x3 = this.x;
		
		double y1 = 0;
		double y2 = planet.getY();
		double y3 = this.y;
		
		double r1 = Math.sqrt(Math.pow(x3 - x1, 2) + Math.pow(y3 - y1, 2));
		double r3 = Math.sqrt(Math.pow(x3 - x2, 2) + Math.pow(y3 - y2, 2));
		
		double m1 = solarMass;
		double m2 = planet.getbodyMass();
		
		int gameFactor = 20; // magic number which increases forces, so that force effects are more visible
		
		double ax = (-1) * gameFactor * G * m1 * (x3 - x1)/Math.pow(r1, 3) - 2 * gameFactor * G * m2 * (x3 - x2)/Math.pow(r3, 3);
		return ax;
	}
	
	public double threeBodyProblemAY(Planet planet)
	{// 1 - sun, 2 - planet, 3 - ship
		double x1 = 0;
		double x2 = planet.getX();
		double x3 = this.x;
		
		double y1 = 0;
		double y2 = planet.getY();
		double y3 = this.y;
		
		double r1 = Math.sqrt(Math.pow(x3 - x1, 2) + Math.pow(y3 - y1, 2));
		double r3 = Math.sqrt(Math.pow(x3 - x2, 2) + Math.pow(y3 - y2, 2));
		
		double m1 = solarMass;
		double m2 = planet.getbodyMass();
		
		int gameFactor = 20; // magic number which increases forces, so that force effects are more visible
		
		double ay = (-1) * gameFactor * G * m1 * (y3 - y1)/Math.pow(r1, 3) - 2 * gameFactor * G * m2 * (y3 - y2)/Math.pow(r3, 3);
		return ay;
	}
	
	
	public void engineAcceleration(double accelerationX, double accelerationY)
	{
		this.engineAccelerationX = accelerationX;
		this.engineAccelerationY = accelerationY;
	}
	
	
	public double getEngineAccelerationX()
	{
		return this.engineAccelerationX;
	}
	
	public double getEngineAccelerationY()
	{
		return this.engineAccelerationY;
	}
	
	public double shipKineticEnergy()
	{
		return (this.bodyMass * Math.pow(this.v, 2)/2);
	}
	
	public GeneralPath generateOneOrbit(double dt, Planet[] planetModel)
	{
		
		if (!this.isInsidePotentialField)
		{
			return generateOneOrbitAroundSun(dt, planetModel);
		}
		
		else
		{
			return generateOneOrbitAroundPlanet(dt, planetModel);
		}
	}
	
	private GeneralPath generateOneOrbitAroundSun(double dt, Planet[] planetModel)
	{
		GeneralPath newOrbit = new GeneralPath();

		Point2D.Double pointEnd = null;
		newOrbit.moveTo(this.x, this.y);
		double lastAngle = this.actualAngle; // actualAngle trzeba poprawic
		while (!fullOrbit(0, 0) && this.isInsideSolarSystem())
		{
			
			RK4AroundSun(dt, planetModel);
			
			if(Math.abs(lastAngle - this.actualAngle) >= 0.5)
			{
				lastAngle = this.actualAngle;
				newOrbit.lineTo(this.x, this.y);
//				pointEnd = new Point2D.Double(this.x, this.y);
//				newOrbit.lineTo(pointEnd.getX(), pointEnd.getY());
				pointEnd = null;
			}
		}
		
		return newOrbit;
	}
	
	private GeneralPath generateOneOrbitAroundPlanet(double dt, Planet[] planetModel)
	{
		GeneralPath newOrbit = new GeneralPath();

		Planet daemonPlanet = new Planet(planetModel[this.shipCaughtByPlanetNumber]);
		
		Point2D.Double pointEnd = null;
		newOrbit.moveTo(this.x, this.y);
		double lastAngle = this.actualAngle;
//		while (!fullOrbit(daemonPlanet.getX(), daemonPlanet.getY()) && this.isInsidePotentialField)
		while (!fullOrbit(daemonPlanet.getX(), daemonPlanet.getY()))
		{
			
		//	RK4(dt);
//			daemonPlanet.RK4(dt);
//			RK4AroundPlanet(dt, planetModel);
			DaemonRK4AroundPlanet(dt, daemonPlanet, daemonPlanet.getX(), daemonPlanet.getY());
			if(Math.abs(lastAngle - this.actualAngle) >= 0.5)
			{
				lastAngle = this.actualAngle;
				newOrbit.lineTo(this.x , this.y);
				//pointEnd = new Point2D.Double(this.x, this.y);
				//newOrbit.lineTo(pointEnd.getX(), pointEnd.getY());
				pointEnd = null;
			}
		}
		return newOrbit;
	}
}
