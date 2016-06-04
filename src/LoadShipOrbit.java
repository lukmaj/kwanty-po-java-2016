package pl.edu.pw.fizyka.pojava.Kwanty;

public class LoadShipOrbit 
{
	private Ship playerShip;
	
	public LoadShipOrbit(Ship playerShip)
	{
		this.playerShip = playerShip;
	}
	
	public void generateShipOrbit()
	{
		
		int secondsToDay = 60 * 60 * 24;
		
		Thread shipThread = new Thread(this.playerShip);
		this.playerShip.setdt(secondsToDay);
		shipThread.start();
	}
}
