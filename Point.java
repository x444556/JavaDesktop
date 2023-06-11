package net.ddns.x444556;

public class Point {
	public double X, Y;

	public Point(double x, double y) {
		X = x;
		Y = y;
	}
	
	public Point Clone() {
		return new Point(X, Y);
	}
	public double DistanceTo(Point p) {
		double a = p.X - X;
		double b = p.Y - Y;
		return Math.sqrt(a*a + b*b);
	}
}
