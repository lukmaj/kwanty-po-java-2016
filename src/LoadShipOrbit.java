package pl.edu.pw.fizyka.pojava.Kwanty;

public class LoadShipOrbit 
{
	Ship playerShip;
	public LoadShipOrbit(Ship playerShip)
	{
		this.playerShip = playerShip;
	}
	
	public void generateShipOrbit()
	{
		int secondsToDay = 60 * 60 * 24;
		
		Thread shipThread = new Thread(playerShip);
		playerShip.setdt(secondsToDay);
		
//		playerShip.setdt(100);
		shipThread.start();

	}
}
