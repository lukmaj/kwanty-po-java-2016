package pl.edu.pw.fizyka.pojava.Kwanty;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class LoadOrbits extends JDialog
{ // PJ + LM
	JProgressBar loadBar;
	JLabel loadLabel;
	MainWindow mainWindow;
	
	Timer progressTimer;
	ThreadGroup group;
	
	public LoadOrbits(MainWindow mainWindow)
	{ // LM, PJ
		super(mainWindow);
		this.mainWindow = mainWindow;
		
		
		loadBar = new JProgressBar(0, 100);
		loadLabel = new JLabel("Generating Orbits: ");
		JPanel panel = new JPanel();
		panel.add(loadLabel);
		panel.add(loadBar);
		
		this.add(panel);
		
		
		group = new ThreadGroup("planet Threads");
		
		progressTimer = new Timer(500, new ActionListener()
		{
			int active;
			public void actionPerformed(ActionEvent arg0)
			{
				active = group.activeCount();
				setProgress(group.activeCount());
				repaint();
				if (active == 0)
				{
					progressTimer.stop();
					LoadOrbits.this.setVisible(false);
					mainWindow.setVisible(true);
				}
				
			}
			
		});
		
		
		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.pack();
		this.setVisible(true);
		
	}
	
	public void setProgress(int activeThreads)
	{
		float progress = ((8.0f - activeThreads)/8.0f) * 100.0f + 0.5f;
		loadBar.setValue((int) progress);
		
	}
	
	public void generatePlanetaryOrbits(Planet[] planetModel)
	{
		int secondsToMinute = 60;
		
		Thread[] planetThread = new Thread[8];
		
		for (int ii = 0; ii < planetModel.length; ++ii)
		{
			planetThread[ii] = new Thread(group, planetModel[ii]);
			planetModel[ii].setdt(secondsToMinute);
			planetThread[ii].start();
		}
		
		progressTimer.start();
		
	//	this.setVisible(false);
		
		
		
		repaint();
	}
}
