package pl.edu.pw.fizyka.pojava.Kwanty;


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