package src.pl.edu.pw.fizyka.pojava.Kwanty;

import java.awt.geom.GeneralPath;



public class Planet extends CelestialBody
{	// L.M.
	public final static double enlargementFactor = 40;
	public boolean shipInsidePotentialField;
	private double planetRadius;
	
	
	public Planet(double mass, double maxRadius, double minRadius, double planetRadius)
	{
		super(mass, maxRadius, minRadius);
		this.planetRadius = planetRadius * enlargementFactor;
		double c = this.eccentricity * majorOrbitSemiAxis;
		
		this.x = Math.random() * (this.maxRadius + this.minRadius) - this.minRadius;
		this.y = minorOrbitSemiAxis * Math.sqrt(1 - Math.pow((x - c)/majorOrbitSemiAxis, 2));
		if (Math.random() < 0.5) // 50% chance of being on upper half of orbit
			this.y = -y;
		
		initValue();
	}
	
	public Planet(Planet planet)
	{
		super(planet.bodyMass, planet.maxRadius, planet.minRadius);
		this.x = planet.x;
		this.y = planet.y;

		this.v = planet.v;
		this.vx = planet.vx;
		this.vy = planet.vy;
		
		this.a = planet.a;
		this.ax = planet.ax;
		this.ay = planet.ay;
		
		this.actualRadius = planet.actualRadius;
		this.actualAngle = planet.actualAngle;
		this.beginAngle = planet.beginAngle;
		this.halfOrbitAngle = planet.halfOrbitAngle;
		this.maxAngle = planet.maxAngle;
		this.minAngle = planet.minAngle;
	}
	
	public void copyPlanet(Planet planet)
	{
		this.bodyMass = planet.bodyMass;
		
		this.x = planet.x;
		this.y = planet.y;
		
		this.v = planet.v;
		this.vx = planet.vx;
		this.vy = planet.vy;
		

		this.a = planet.a;
		this.ax = planet.ax;
		this.ay = planet.ay;
		
		this.actualRadius = planet.actualRadius;
		this.maxRadius = planet.maxRadius;
		this.minRadius = planet.minRadius;
		this.actualAngle = planet.actualAngle;
		this.beginAngle = planet.beginAngle;
		this.halfOrbitAngle = planet.halfOrbitAngle;
		this.maxAngle = planet.maxAngle;
		this.minAngle = planet.minAngle;
	}
	
	
	public double getplanetaryRadius()
	{
		return this.planetRadius;
	}
	
	public GeneralPath getorbitPath()
	{
		return this.orbitPath;
	}
	
}