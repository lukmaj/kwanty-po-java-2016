package src.pl.edu.pw.fizyka.pojava.Kwanty;

import java.awt.geom.Point2D;

public class Line
{ // P.J./L.M. 
	private double A;
	private double B;
	private double C;
	
	public Line()
	{
		this.A = 0;
		this.B = 0;
		this.C = 0;
	}
	
	public Line(Point2D.Double startPoint, Point2D.Double endPoint)
	{
		if (startPoint.getX() == endPoint.getX())
		{
			this.A = 1;
			this.B = 0;
			this.C = (-1) * startPoint.getX();
		}
		else
		{
			this.B = 1;
			this.A = (-1) * Math.tan((endPoint.getY() - startPoint.getY())/(endPoint.getX() - startPoint.getX()));
			this.C = (-1) * this.A * startPoint.getX() - startPoint.getY();
		}
	}
	
	public Line perpendicularLine(Point2D.Double point)
	{
		Line perpendicularLine = new Line();
		if (this.B == 0)
		{
			perpendicularLine.A = 0;
			perpendicularLine.B = 1;
			perpendicularLine.C = (-1) * point.getY();
		}
		else
		{
			perpendicularLine.B = 1;
			perpendicularLine.A = point.getX()/point.getY() + this.C/this.A * 1/point.getY();
			
//			perpendicularLine.A = (-1) * this.B/this.A;
			perpendicularLine.C = (-1) * perpendicularLine.A * point.getX() - point.getY();
		}
		
		return perpendicularLine;
	}
	
	
	static Point2D.Double intersectionPoint(Line line1, Line line2)
	{
		double w = line1.A * line2.B - line1.B * line2.A;
		double wx = (-1) * line1.C * line2.B + line1.B * line2.C;
		double wy = (-1) * line1.A * line2.C + line1.C * line2.A;
		
		double x = wx/w;
		double y = wy/w;
		
		return new Point2D.Double(x, y);
	}
}
