package pl.edu.pw.fizyka.pojava.Kwanty;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;

import javax.swing.JComponent;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class GUI extends JComponent
{// P.J.
	
	private final double AU = 1.4960e8; // astronomical units in km
	
	private Planet[] planetModel;
	
	private Ellipse2D.Double[] orbits;
	private Ellipse2D.Double[] planets;
	
	private double scaleFactor=1;
	
	private Timer timeSimulator;
	private double timeScale=1;
	private final int dt=1; // time interval in seconds (s)
	
	private double screenCenterHeight;
	private double screenCenterWidth;
	
	
	public void startTimer()
	{// L.M.
		timeSimulator.start();
	}
	public void stopTimer()
	{// L.M.
		timeSimulator.stop();
	}
	
	public GUI(Planet[] planetModel)
	{
		this.scaleFactor=1.0;
		this.planetModel=planetModel;
		orbits=new Ellipse2D.Double[8];
		planets=new Ellipse2D.Double[8];

		

		
		for (int ii=0; ii<8; ii++)
		{
			/*
			while (!planetModel[ii].fullOrbit())
			{
				planetModel[ii].RK4(dt*60*60*24);
				System.out.println("Actual radius: "+planetModel[3].getActualRadius());
//					if (ii==3)
//						System.out.println("Actual radius: "+planetModel[3].getActualRadius() +" actualAngle: " + planetModel[3].getActualAngle());
			}
//			System.out.println("Max radius: "+planetModel[3].getMaxRadius() +" MinRadius: " +planetModel[3].getMinRadius() + " minAngle: " + planetModel[3].getMinAngle() + " MaxAngle: " + planetModel[3].getMaxAngle());
 */
			orbits[ii]=new Ellipse2D.Double();
			planets[ii]=new Ellipse2D.Double();
		}
		
		this.timeScale=1;
		this.timeSimulator=new Timer(1, new ActionListener()
		{// L.M. (timer) + P.J(RK4)
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				for (int ii=0;ii<planetModel.length;++ii)
				{
					planetModel[ii].RK4(dt*timeScale/1000);
					//System.out.println(Math.sqrt(Math.pow(planetModel[0].getVX(), 2)+Math.pow(planetModel[0].getVY(), 2)));
					//planetModel[0].fullOrbit();
					
				}
				repaint();
			}
		});
	}
	
	
	public void orbitRender() // P.J.
	{// P.J.
		
		
		
//		final int screenMargin=2;
//		double screenMaxWidth=2*planetModel[7].getMaxRadius()/AU + screenMargin; // 61 AU on x axis
//		double screenMaxHeight=2*planetModel[7].getMinRadius()/AU + screenMargin;
		
		
//		double distanceToPixel = (double)this.getWidth()/screenMaxWidth; // unit: AU
		
		/*
		for (int ii=0; ii< planetModel.length; ++ii)
		{
 			double pixelMaxRadius = (planetModel[ii].getMaxRadius()/AU)*distanceToPixel*scaleFactor;
			double pixelMinRadius = (planetModel[ii].getMinRadius()/AU)*distanceToPixel*scaleFactor;
			
			double pixelX=(planetModel[ii].getX()/AU)*distanceToPixel*scaleFactor+screenCenterWidth;
			double pixelY=(planetModel[ii].getY()/AU)*distanceToPixel*scaleFactor+screenCenterHeight;
			
			
			orbits[ii].setFrameFromCenter(screenCenterWidth, screenCenterHeight, screenCenterWidth-pixelMaxRadius, screenCenterHeight-pixelMinRadius);
			planets[ii].setFrameFromCenter(pixelX, pixelY, pixelX-2, pixelY-2);
			
			
		}
		*/
		double planetaryRadius=1e11/8;
		for (int ii=0; ii < planetModel.length; ++ii)
		{
			
			orbits[ii].setFrame((-1)*planetModel[ii].getMinRadius(), (-1)*planetModel[ii].getSemiMinorAxis(), 2*planetModel[ii].getSemiMajorAxis(), 2*planetModel[ii].getSemiMinorAxis());
			planets[ii].setFrame(planetModel[ii].getX()-planetaryRadius, planetModel[ii].getY()-planetaryRadius, 2*planetaryRadius, 2*planetaryRadius);
		}
	}
	
	public void paintComponent2(Graphics g)
	{ // P.J.
		Graphics2D g2 = (Graphics2D) g;
		g2.setPaint(Color.RED);
		orbitRender();
		for (int ii=0;ii< orbits.length;++ii)
		{
			g2.draw(orbits[ii]);
			
		}
		
		g2.setPaint(Color.BLACK);
		for (int ii=0;ii<planetModel.length;++ii)
		{
			g2.draw(planets[ii]);
			g2.fill(planets[ii]);
		}
		
	}
	
	public void paintComponent(Graphics g)
	{ // P.J.
		
		screenCenterHeight=(double)this.getHeight()/2;
		screenCenterWidth=(double)this.getWidth()/2;
		
		final int screenMargin=2;
		double screenMaxWidth=2*planetModel[7].getMaxRadius() + screenMargin;
		double screenMaxHeight=2*planetModel[7].getMinRadius() + screenMargin;
		
		
		double distanceToPixel = scaleFactor*(double)this.getWidth()/screenMaxWidth;// unit: AU
		
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setPaint(Color.RED);
	//	System.out.println("X: "+screenCenterWidth + " Y: " +screenCenterHeight);
		g2.translate(screenCenterWidth, screenCenterHeight);
		g2.draw(new Ellipse2D.Double(-2,-2,4,4));
		g2.scale(distanceToPixel, distanceToPixel);
		orbitRender();
		for (int ii=0;ii< orbits.length;++ii)
		{
			g2.draw(orbits[ii]);
			
		}
		
		g2.setPaint(Color.BLACK);
		for (int ii=0;ii<planetModel.length;++ii)
		{
			g2.draw(planets[ii]);
			g2.fill(planets[ii]);
		//	System.out.println(" "+ planets[ii].getBounds2D().getHeight());
		}
		
		
		
	}
	
	public double getScale()
	{ // L.M.
		return scaleFactor;
	}
	
	public void setScale(double newScale)
	{// L.M.
		scaleFactor=newScale;
	}
	
	public double getTimeScale()
	{// L.M.
		return timeScale;
	}
	
	public void setTimeScale(double newTimeModifier)
	{// L.M.
		timeScale=newTimeModifier;
	}
	
	public void printSize()
	{// L.M.
		System.out.println("Height i width" + this.getHeight()+" "+this.getWidth());
	}
	
	
}