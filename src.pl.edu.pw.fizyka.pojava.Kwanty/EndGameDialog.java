package src.pl.edu.pw.fizyka.pojava.Kwanty;

import javax.swing.JOptionPane;

public class EndGameDialog
{
	
	public static void shipOutsideSolarSystem(MainWindow mainWindow)
	{
		JOptionPane.showMessageDialog(mainWindow, "Ship is outside solar system bounds.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void shipInsideSun(MainWindow mainWindow)
	{
		JOptionPane.showMessageDialog(mainWindow, "Ship flew inside the Sun.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void shipLandedOnPlanet(MainWindow mainWindow)
	{
		JOptionPane.showMessageDialog(mainWindow, "Ship landed safely on the planet.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void shipCrashedWithPlanet(MainWindow mainWindow)
	{
		JOptionPane.showMessageDialog(mainWindow, "Ship has crashed.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
	}
}
