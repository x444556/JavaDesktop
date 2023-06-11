package net.ddns.x444556;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DesktopSettingsApp extends Application {
	private Window ui;
	private Program javaos;
	
	public DesktopSettingsApp(Program javaos) {
		super(javaos);
		this.javaos = javaos;
		Name = "DesktopSettings";
	}
	
	@Override
	public void paintWindow(Window w, Graphics g) {
		if(w != ui) return;
		
		g.setColor(new Color(90, 90, 90));
		g.fillRect(0, 0, ui.getWidth(), ui.getHeight());
		
		g.setColor(new Color(30, 30, 30));
		g.drawRect(5, 10, 285, 45);
		g.drawRect(5, 65, 285, 75);
		g.drawRect(5, 150, 285, 45);
		g.setColor(new Color(90, 90, 90));
		g.drawLine(8, 10, 115, 10);
		g.drawLine(8, 65, 110, 65);
		g.drawLine(8, 150, 135, 150);
		g.setColor(new Color(30, 30, 30));
		g.drawString("DateTime Position", 10, 14);
		g.drawString("DateTime Format", 10, 69);
		g.drawString("DateTime DayOfWeek", 10, 155);
		
		g.setColor(new Color(110, 110, 110));
		g.fillRect(15, 20, 86, 25);
		g.fillRect(106, 20, 86, 25);
		g.fillRect(197, 20, 86, 25);
		
		g.fillRect(15, 75, 86, 25);
		g.fillRect(106, 75, 86, 25);
		g.fillRect(197, 75, 86, 25);
		g.fillRect(15, 105, 131, 25);
		g.fillRect(152, 105, 131, 25);

		g.fillRect(15, 160, 86, 25);
		g.fillRect(106, 160, 86, 25);
		
		g.setColor(new Color(30, 30, 30));
		g.drawString("Left", 48, 36);
		g.drawString("Center", 131, 36);
		g.drawString("Right", 224, 36);
		
		g.drawString(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")), 44, 91);
		g.drawString(LocalDateTime.now().format(DateTimeFormatter.ofPattern("d MMM uuuu")), 116, 91);
		g.drawString(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")), 214, 91);
		g.drawString(LocalDateTime.now().format(DateTimeFormatter.ofPattern("d MMM uuuu - HH:mm:ss")), 20, 121);
		g.drawString(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.uu - HH:mm")), 170, 121);
		
		g.drawString("Hide", 48, 176);
		g.drawString("Show", 131, 176);
		
		g.setColor(new Color(140, 140, 140));
		if(javaos.Desktop.PreferredDateTimePosition == DesktopRenderer.DateTimePosition.LEFT) g.drawRect(15, 20, 86, 24);
		else if(javaos.Desktop.PreferredDateTimePosition == DesktopRenderer.DateTimePosition.CENTER) g.drawRect(106, 20, 86, 24);
		else if(javaos.Desktop.PreferredDateTimePosition == DesktopRenderer.DateTimePosition.RIGHT) g.drawRect(197, 20, 86, 24);

		if(javaos.Desktop.PreferredDateTimeFormat == DesktopRenderer.DateTimeFormat.TIME_SHORT) g.drawRect(15, 75, 86, 24);
		else if(javaos.Desktop.PreferredDateTimeFormat == DesktopRenderer.DateTimeFormat.DATE) g.drawRect(106, 75, 86, 24);
		else if(javaos.Desktop.PreferredDateTimeFormat == DesktopRenderer.DateTimeFormat.TIME) g.drawRect(197, 75, 86, 24);
		else if(javaos.Desktop.PreferredDateTimeFormat == DesktopRenderer.DateTimeFormat.DATETIME) g.drawRect(15, 105, 131, 24);
		else if(javaos.Desktop.PreferredDateTimeFormat == DesktopRenderer.DateTimeFormat.DATETIME_SHORT) g.drawRect(152, 105, 131, 24);

		if(!javaos.Desktop.DisplayDayOfWeek) g.drawRect(15, 160, 86, 24);
		else g.drawRect(106, 160, 86, 24);
	}
	@Override 
	public Application CreateNew() {
		return new DesktopSettingsApp(javaos);
	}
	@Override
	public void Start() {
		ui = new Window(300, 203, javaos.randomInt(0, javaos.getWidth() - 300), 
				javaos.randomInt(0, javaos.getHeight() - 280), "Desktop Settings");
		AddWindow(ui);
		while(!ui.isAdded)
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		ui.Focus();
		ui.UnFocus();
		javaos.sortWindows();
	}
	@Override
	public void Close(Window w) {
		if(w == ui) this.Exit();
		else {
			javaos.RemoveWindow(w);
			Windows.remove(w);
		}
	}
	@Override
	public void mouseClicked(MouseEvent e, Window w) {
		if(w != ui) return;
		
		int x = e.getX() - ui.getX();
		int y = e.getY() - ui.getY() - ui.headerHeight;
		
		if(isWithinArea(x, y, 15, 20, 86, 25)) {
			javaos.Desktop.PreferredDateTimePosition = DesktopRenderer.DateTimePosition.LEFT;
		}
		else if(isWithinArea(x, y, 106, 20, 86, 25)) {
			javaos.Desktop.PreferredDateTimePosition = DesktopRenderer.DateTimePosition.CENTER;
		}
		else if(isWithinArea(x, y, 197, 20, 86, 25)) {
			javaos.Desktop.PreferredDateTimePosition = DesktopRenderer.DateTimePosition.RIGHT;
		}
		
		if(isWithinArea(x, y, 15, 75, 86, 25)) {
			javaos.Desktop.PreferredDateTimeFormat = DesktopRenderer.DateTimeFormat.TIME_SHORT;
		}
		else if(isWithinArea(x, y, 106, 75, 86, 25)) {
			javaos.Desktop.PreferredDateTimeFormat = DesktopRenderer.DateTimeFormat.DATE;
		}
		else if(isWithinArea(x, y, 197, 75, 86, 25)) {
			javaos.Desktop.PreferredDateTimeFormat = DesktopRenderer.DateTimeFormat.TIME;
		}
		else if(isWithinArea(x, y, 15, 105, 131, 25)) {
			javaos.Desktop.PreferredDateTimeFormat = DesktopRenderer.DateTimeFormat.DATETIME;
		}
		else if(isWithinArea(x, y, 152, 105, 131, 25)) {
			javaos.Desktop.PreferredDateTimeFormat = DesktopRenderer.DateTimeFormat.DATETIME_SHORT;
		}
		
		if(isWithinArea(x, y, 15, 160, 86, 25)) {
			javaos.Desktop.DisplayDayOfWeek = false;
		}
		else if(isWithinArea(x, y, 106, 160, 86, 25)) {
			javaos.Desktop.DisplayDayOfWeek = true;
		}
	}

}
