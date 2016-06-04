package src.pl.edu.pw.fizyka.pojava.Kwanty;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class LoadOrbitsDialog extends JDialog
{ // P.J
	private JProgressBar loadBar;
	private JLabel loadLabel;
	private Timer progressTimer;
	private ThreadGroup group;
	public MainWindow mainWindow;
	
	public LoadOrbitsDialog(MainWindow mainWindow)
	{ // LM, PJ
		super(mainWindow);
		
		this.mainWindow = mainWindow;
		this.loadBar = new JProgressBar(0, 100);
		this.loadLabel = new JLabel("Generating Orbits: ");
		JPanel panel = new JPanel();
		panel.add(this.loadLabel);
		panel.add(this.loadBar);
		this.add(panel);

		this.group = new ThreadGroup("planet Threads");
		this.progressTimer = new Timer(500, new ProgressTimerListener());
		
		
		int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width/2;
		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height/2;
		this.setLocation(screenWidth - this.getWidth()/2, screenHeight - this.getHeight()/2);
		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.pack();
		this.setVisible(true);
		
	}
	
	public void setProgress(int activeThreads)
	{
		float progress = ((8.0f - activeThreads)/8.0f) * 100.0f + 0.5f;
		this.loadBar.setValue((int) progress);
		
	}
	
	public void generatePlanetaryOrbits(Planet[] planetModel)
	{
		int secondsToMinute = 60;
		
		Thread[] planetThread = new Thread[8];
		
		for (int ii = 0; ii < planetModel.length; ++ii)
		{
			planetThread[ii] = new Thread(this.group, planetModel[ii]);
			planetModel[ii].setdt(secondsToMinute);
			planetThread[ii].start();
		}
		
		this.progressTimer.start();
		
	//	this.setVisible(false);
		
		
		
		repaint();
	}
	
	class ProgressTimerListener implements ActionListener
	{
		int active;
		public void actionPerformed(ActionEvent arg0)
		{
			this.active = LoadOrbitsDialog.this.group.activeCount();
			setProgress(LoadOrbitsDialog.this.group.activeCount());
			repaint();
			if (this.active == 0)
			{
				LoadOrbitsDialog.this.progressTimer.stop();
				LoadOrbitsDialog.this.setVisible(false);
				LoadOrbitsDialog.this.mainWindow.setVisible(true);
			}
		}
	}
}
