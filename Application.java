package net.ddns.x444556;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.Graphics;

public class Application {

	private Program javaos;
	
	public ArrayList<Window> Windows = new ArrayList<Window>();
	
	public String Name = "App";
	public long PID = -1;
	
	public Application(Program javaos) {
		this.javaos = javaos;
	}
	
	protected boolean isWithinArea(int x, int y, int startX, int startY, int width, int height) {
		return x >= startX && y >= startY && x < startX + width && y < startY + height;
	}
	
	public void Start() {

	}
	public void Exit() {
		for(Window w : Windows) javaos.RemoveWindow(w);
		javaos.ExitApplication(this);
	}
	public final void SysExit() {
		// Called by Program when app exited using ExitApplication()
		for(Window w : Windows) javaos.RemoveWindow(w);
	}
	public void Close(Window w) {
		w.isClosing = true;
		javaos.RemoveWindow(w);
		Windows.remove(w);
	}
	public void AddWindow(Window w) {
		Windows.add(w);
		javaos.AddElement(w);
		w.Owner = this;
	}
	public Application CreateNew() {
		Application a = new Application(javaos);
		return a;
	}
	public void paintWindow(Window w, Graphics g) {
		
	}
	public void keyTyped(KeyEvent e, Window w) {
		
	}
	public void keyPressed(KeyEvent e, Window w) {
		
	}
	public void keyReleased(KeyEvent e, Window w) {
		
	}
	public void mouseClicked(MouseEvent e, Window w) {
		
	}
	public void mousePressed(MouseEvent e, Window w) {
		
	}
	public void mouseReleased(MouseEvent e, Window w) {
		
	}
	public void mouseMoved(MouseEvent e, Window w) {
		
	}
}
