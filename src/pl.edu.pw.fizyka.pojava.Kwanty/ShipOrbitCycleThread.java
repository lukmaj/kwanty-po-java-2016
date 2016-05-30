package pl.edu.pw.fizyka.pojava.Kwanty;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Thread.State;

import javax.swing.Timer;

public class ShipOrbitCycleThread implements Runnable
{
	Ship playerShip;
	Ship daemonPlayerShip;
	Timer orbitCyclicalTimer;
	Thread orbitThread;
	
	public ShipOrbitCycleThread(Ship playerShip)
	{
		this.playerShip = playerShip;
		this.daemonPlayerShip = new Ship(playerShip);
		
		TimerListener timeListener = new TimerListener();
		
		orbitCyclicalTimer = new Timer(1, timeListener);
	}
	
	
	public void run()
	{
		System.out.println("Thread running");
		
		daemonPlayerShip.copyShip(playerShip);
		System.out.println("ship copied");
//		GeneralPath tempPath = daemonPlayerShip.generateOneOrbit(60*60*24 * 1000);
//		System.out.println("tempath finished");
		playerShip.setOrbitShape(daemonPlayerShip.generateOneOrbit(60*60*24));
		System.out.println("tempath set as orbit");
//		if (tempPath.equals(playerShip.orbitPath))
//			System.out.println("rowne");
//		else
//			System.out.println("nierowne");
	//		Thread.sleep(3000);
		
		
	}
	
	public void startTimer()
	{
		System.out.println("starttimer");
		orbitThread = new Thread(this);
		orbitThread.start();
		orbitCyclicalTimer.start();
	}
	
	public void stopTimer()
	{
		if (orbitCyclicalTimer.isRunning())
			orbitCyclicalTimer.stop();
	}
	
	private class TimerListener implements ActionListener
	{
		
		public void actionPerformed(ActionEvent e)
		{	
		
			if (orbitThread.getState() == State.TERMINATED)
			{
				System.out.println("orbit thd terminated");
				orbitThread = new Thread(ShipOrbitCycleThread.this);
				orbitThread.start();
			}
				
			
		}
	}
	
	public void getActualShip(Ship newPlayerShip)
	{
		playerShip = newPlayerShip;
	}
	
}
