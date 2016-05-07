package pl.edu.pw.fizyka.pojava.Kwanty;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

// Klasa L.M.

@SuppressWarnings("serial")
public class MainWindow extends JFrame
{
	Planet[] solarSystem;
	GUI illustrate;
	
	JMenuBar menubar;
	JMenu menufile;
	JMenuItem exitOption;
	JMenuItem saveOption;
	JMenuItem loadOption;

	
	JMenuItem setTimeScale;
	JMenuItem setScaleFactor;
	JMenuItem startSim;
	JMenuItem stopSim;
	
	public MainWindow()
	{
		solarSystem = new Planet[8];
		solarSystem[0]=new Planet(3.3011e23, 69.816900e6, 46.001200e6);
		solarSystem[1]=new Planet(4.8675e24,108.939e6, 107.477e6);
		solarSystem[2]=new Planet(5.97237e24, 152.1e6, 147e6);
		solarSystem[3]=new Planet(6.4171e23, 249.2336e6, 206.65744e6);
		solarSystem[4]=new Planet(1.8986e27, 816.056032e6, 778.30896e6);
		solarSystem[5]=new Planet(5.6836e26, 1508.8656e6, 1349.9904e6);
		solarSystem[6]=new Planet(8.6810e25, 3008.456e6, 2742.168e6);
		solarSystem[7]=new Planet(1.0243e26,4537.368e6,4459.576e6);
		illustrate=new GUI(solarSystem);
		this.add(illustrate);
		
		Toolkit tools=Toolkit.getDefaultToolkit();
		Dimension dim=tools.getScreenSize();
		
		
		setMenu();
		this.setSize(1200, 900);
		this.setLocation(dim.width/4, dim.height/4);
		illustrate.repaint();
		
	}
	
	private void setMenu()
	{
		menubar= new JMenuBar();
		this.setJMenuBar(menubar);
		menufile=new JMenu("Plik");
		menubar.add(menufile);
		
		exitOption=new JMenuItem("Exit");
		saveOption=new JMenuItem("Save");
		loadOption=new JMenuItem("Load");
		
		startSim=new JMenuItem("Start Simulation");
		stopSim=new JMenuItem("Stop Simulation");
		
		setTimeScale=new JMenuItem("Set simulation speed");
		setScaleFactor=new JMenuItem("Set distance scale");
		
		menufile.add(startSim);
		menufile.add(stopSim);
		menufile.add(setScaleFactor);
		menufile.add(setTimeScale);
		menufile.add(saveOption);
		menufile.add(loadOption);
		menufile.add(exitOption);
		
		
		
		exitOption.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		
		startSim.setEnabled(true);
		stopSim.setEnabled(false);
		
		startSim.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				illustrate.startTimer();
				startSim.setEnabled(false);
				stopSim.setEnabled(true);
			}
		});
		
		stopSim.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				illustrate.stopTimer();
				startSim.setEnabled(true);
				stopSim.setEnabled(false);				
			}
		});
		
		setTimeScale.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				try
				{
					String input=JOptionPane.showInputDialog("Simulation speed: " + illustrate.getTimeScale()/3600 + " hours/second");
					double newTimeScale=Double.parseDouble(input)*3600;
					illustrate.setTimeScale(newTimeScale);
				}
				catch(Exception e)
				{}
			}
		});
		
		setScaleFactor.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				try
				{
					String input=JOptionPane.showInputDialog("Enlargement: " + illustrate.getScale());
					double newScale=Double.parseDouble(input);
					illustrate.setScale(newScale);
					illustrate.repaint();
				}
				catch(Exception e)
				{}
			}
		});
	}
}