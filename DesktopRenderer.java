package net.ddns.x444556;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.awt.Color;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

public class DesktopRenderer extends JLayeredPane {
	private static final long serialVersionUID = -1530371300000638095L;
	
	public ArrayList<Window> Windows;
	public ArrayList<AppInfo> Apps;
	public DateTimePosition PreferredDateTimePosition = DateTimePosition.CENTER;
	public DateTimeFormat PreferredDateTimeFormat = DateTimeFormat.DATETIME;
	public boolean DisplayDayOfWeek = true;
	
	private Program javaos;
	
	public enum DateTimePosition{
		LEFT,
		RIGHT,
		CENTER
	}
	public enum DateTimeFormat{
		DATETIME,
		DATE,
		TIME,
		TIME_SHORT,
		DATETIME_SHORT
	}

	public DesktopRenderer(Program javaos, ArrayList<Window> windows, ArrayList<AppInfo> apps) {
		super();
		
		Windows = windows;
		Apps = apps;
		
		this.javaos = javaos;
	}

	@Override
	public void paintComponent(Graphics g) {
		double f = 1.0 / getHeight() * 50.0;
		for(int y=20; y<getHeight(); y++) {
			g.setColor(new Color(100 - (int)(y*f), 100 - (int)(y*f), 100 - (int)(y*f)));
			g.drawLine(0, y, 80, y);
			
			g.setColor(new Color(130 - (int)(y*f), 130 - (int)(y*f), 130 - (int)(y*f)));
			g.drawLine(80, y, getWidth(), y);
		}

		for(int y=0; y<getHeight() && y<20; y++) {
			g.setColor(new Color(80 - y, 80 - y, 80 - y));
			g.drawLine(0, y, getWidth(), y);
		}
		
		for(AppInfo a : Apps) {
			if(a == dragged) continue;
			
			g.setColor(a.Color);
			g.fillRect(88 + a.X * 72, 28 + a.Y * 88, 64, 64);
			
			g.setColor(new Color(255, 255, 255));
			g.drawString(a.Title, 120 + a.X * 72 - 
					(int) (g.getFont().getStringBounds(a.Title, new FontRenderContext(null, true, true)).getWidth() / 2), 104 + a.Y * 88);
		}
		if(dragged != null) {
			g.setColor(dragged.Color);
			g.fillRect(mouseX - mouseDragOffX, mouseY - mouseDragOffY, 64, 64);
			
			g.setColor(new Color(255, 255, 255));
			g.drawString(dragged.Title, 32 + mouseX - 
					(int) (g.getFont().getStringBounds(dragged.Title, new FontRenderContext(null, true, true)).getWidth() / 2) - 
					mouseDragOffX, 76 + mouseY - mouseDragOffY);
		}

		String dtpattern = (DisplayDayOfWeek ? "EEE " : "");
		if(PreferredDateTimeFormat == DateTimeFormat.DATETIME) dtpattern += "d MMM uuuu - HH:mm:ss";
		else if(PreferredDateTimeFormat == DateTimeFormat.DATE) dtpattern += "d MMM uuuu";
		else if(PreferredDateTimeFormat == DateTimeFormat.TIME) dtpattern += "HH:mm:ss";
		else if(PreferredDateTimeFormat == DateTimeFormat.TIME_SHORT) dtpattern += "HH:mm";
		else if(PreferredDateTimeFormat == DateTimeFormat.DATETIME_SHORT) dtpattern += "dd.MM.uu - HH:mm";
		
		String dateTimeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern(dtpattern));
		if(PreferredDateTimePosition == DateTimePosition.CENTER) {
			g.drawString(dateTimeStr, getWidth() / 2 - 
					(int) (g.getFont().getStringBounds(dateTimeStr, new FontRenderContext(null, true, true)).getWidth() / 2), 16);
		}
		else if(PreferredDateTimePosition == DateTimePosition.LEFT) {
			g.drawString(dateTimeStr, 8, 16);
		}
		else if(PreferredDateTimePosition == DateTimePosition.RIGHT) {
			g.drawString(dateTimeStr, getWidth() - 
					(int) (g.getFont().getStringBounds(dateTimeStr, new FontRenderContext(null, true, true)).getWidth()) - 8, 16);
		}
	}
	private long lastMouseClickMillis = 0;
	private long millisBetweenDoubleClick = 300;
	private int maxDistanceBetweenDoubleClick = 16;
	private int lastMouseX = 0;
	private int lastMouseY = 0;
	private int mouseX = 0, mouseY = 0;
	private int mouseDragOffX = 0, mouseDragOffY = 0;
	public void mouseClicked(MouseEvent e) {
		int mouseX = e.getX();
		int mouseY = e.getY();
		
		boolean isDoubleClick = System.currentTimeMillis() - lastMouseClickMillis <= millisBetweenDoubleClick && 
				Math.sqrt((lastMouseX - mouseX) * (lastMouseX - mouseX) + (lastMouseY - mouseY) * (lastMouseY - mouseY)) <= 
				maxDistanceBetweenDoubleClick;
		lastMouseClickMillis = System.currentTimeMillis();

		if(SwingUtilities.isLeftMouseButton(e) && isDoubleClick) {
			if(mouseY > 20) {
				if(mouseX < 80) {
					// Left Bar (favourites, ...)
				}
				else {
					int appX = (mouseX - 88) / 72;
					int appY = (mouseY - 28) / 88;
					//System.out.println("click x="+appX+"; y="+appY);
					for(AppInfo a : Apps) {
						if(a.X == appX && a.Y == appY) {
							//System.out.println("start app");
							javaos.RunApplication(a.App.CreateNew());
							break;
						}
					}
				}
			}
			else {
				// Top Bar (Clock, Date, ...)
			}
		}
		
		lastMouseX = mouseX;
		lastMouseY = mouseY;
	}
	private AppInfo dragged;
	public void mousePressed(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();

		boolean isDoubleClick = System.currentTimeMillis() - lastMouseClickMillis <= millisBetweenDoubleClick && 
				Math.sqrt((lastMouseX - mouseX) * (lastMouseX - mouseX) + (lastMouseY - mouseY) * (lastMouseY - mouseY)) <= 
				maxDistanceBetweenDoubleClick;

		if(SwingUtilities.isLeftMouseButton(e) && !isDoubleClick) {
			if(mouseY > 20) {
				if(mouseX < 80) {
					// Left Bar (favourites, ...)
				}
				else {
					int appX = (mouseX - 88) / 72;
					int appY = (mouseY - 28) / 88;
					mouseDragOffX = mouseX - (appX * 72 + 88);
					mouseDragOffY = mouseY - (appY * 88 + 28);
					for(AppInfo a : Apps) {
						if(a.X == appX && a.Y == appY) {
							dragged = a;
							break;
						}
					}
				}
			}
			else {
				// Top Bar (Clock, Date, ...)
			}
		}
	}
	public void mouseReleased(MouseEvent e) {
		if(dragged != null) {
			dragged.X = (e.getX() - 88) / 72;
			dragged.Y = (e.getY() - 28) / 88;
		}
		
		dragged = null;
	}
	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}
	public void mouseMoved(MouseEvent e) {
		
	}
}
