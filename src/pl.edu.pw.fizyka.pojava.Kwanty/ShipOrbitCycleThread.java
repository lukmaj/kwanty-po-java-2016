package pl.edu.pw.fizyka.pojava.Kwanty;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Thread.State;

import javax.swing.Timer;

public class ShipOrbitCycleThread implements Runnable
{ // orbitCyclicalTimer checks whether the thread, which refreshes the orbit, is active. If not, then it's brought to life.
	// If the timer is stopped, then the thread has to be killed if active.

	public final double solarRadius;
	private Ship playerShip;
	private Ship daemonPlayerShip;
	private Timer orbitCyclicalTimer;
	private Thread orbitThread;
	private Planet[] planetModel;
	
	public ShipOrbitCycleThread(Ship playerShip, Planet[] planetModel, double sunRadius)
	{
		this.playerShip = playerShip;
		this.daemonPlayerShip = new Ship(playerShip);
		this.planetModel = planetModel;
		this.solarRadius = sunRadius;
		this.orbitCyclicalTimer = new Timer(1, new TimerListener());
	}
	
	
	public void run()
	{
		this.daemonPlayerShip.copyShip(this.playerShip);
		this.playerShip.setOrbitShape(this.daemonPlayerShip.generateOneOrbit(60*60*24, this.planetModel));
	}
	
	public void startTimer()
	{
		this.orbitThread = new Thread(this);
		this.orbitThread.start();
		this.orbitCyclicalTimer.start();
	}
	
	public void stopTimer()
	{
		if (this.orbitCyclicalTimer.isRunning())
		{
			this.orbitCyclicalTimer.stop();
			if (this.orbitThread.isAlive())
				this.orbitThread.interrupt();
		}
	}
	
	public void getActualShip(Ship newPlayerShip)
	{
		this.playerShip = newPlayerShip;
	}
	
	private class TimerListener implements ActionListener
	{
		
		public void actionPerformed(ActionEvent e)
		{	
			if (ShipOrbitCycleThread.this.orbitThread.getState() == State.TERMINATED)
			{
				ShipOrbitCycleThread.this.orbitThread = new Thread(ShipOrbitCycleThread.this);
				ShipOrbitCycleThread.this.orbitThread.start();
			}
				
			
		}
	}
}
