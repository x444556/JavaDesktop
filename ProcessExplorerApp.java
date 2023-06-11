package net.ddns.x444556;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class ProcessExplorerApp extends Application {
	private Window ui;
	private Program javaos;
	
	private int scrollPosition = 0;
	
	public ProcessExplorerApp(Program javaos) {
		super(javaos);
		this.javaos = javaos;
		Name = "ProcessExplorer";
	}
	
	@Override
	public void paintWindow(Window w, Graphics g) {
		if(w != ui) return;
		
		g.setColor(new Color(90, 90, 90));
		g.fillRect(0, 0, ui.getWidth(), ui.getHeight());
		
		g.setColor(new Color(30, 30, 30));
		g.drawString("Total Applications", 10, 14);
		g.drawString("Total Windows", 10, 26);
		g.drawString(Integer.toString(javaos.RunningApps.size()), 128, 14);
		g.drawString(Integer.toString(javaos.Windows.size()), 128, 26);
		
		for(int i=0; i+scrollPosition<javaos.RunningApps.size() && i<(ui.getHeight() - 64)/12; i++) {
			Application a = javaos.RunningApps.get(i+scrollPosition);
			g.drawString(a.Name, 10, 64 + i*12);
			g.drawString(Long.toString(a.PID), 180, 64 + i*12);
			g.drawString(Integer.toString(a.Windows.size()), 200, 64 + i*12);
		}
	}
	@Override 
	public Application CreateNew() {
		return new ProcessExplorerApp(javaos);
	}
	@Override
	public void Start() {
		ui = new Window(230, 300, javaos.randomInt(0, javaos.getWidth() - 230), 
				javaos.randomInt(0, javaos.getHeight() - 490), "Process Explorer");
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
	public void keyReleased(KeyEvent e, Window w) {
		if(w != ui) return;
		
		if(e.getKeyCode() == KeyEvent.VK_DOWN && scrollPosition < javaos.RunningApps.size() - 1) {
			scrollPosition++;
		}
		else if(e.getKeyCode() == KeyEvent.VK_UP && scrollPosition > 0) {
			scrollPosition--;
		}
	}
}
