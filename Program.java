package net.ddns.x444556;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TimerTask;

import javax.swing.JFrame;
import java.util.Timer;
import javax.swing.event.MouseInputListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ComponentListener;

public class Program extends JFrame implements Runnable, MouseInputListener, KeyListener, ComponentListener {
	private static final long serialVersionUID = 1L;
	private Thread thread;
	public int windowX = 1000, windowY = 600;
	
	public ArrayList<Window> Windows = new ArrayList<Window>();
	private ArrayList<Window> windowsToRemove = new ArrayList<Window>();
	private ArrayList<Window> windowsToAdd = new ArrayList<Window>();
	public ArrayList<AppInfo> DesktopApps = new ArrayList<AppInfo>();
	public ArrayList<Application> RunningApps = new ArrayList<Application>();
	private ArrayList<Application> appsToQuit = new ArrayList<Application>();
	private ArrayList<Application> appsToAdd = new ArrayList<Application>();
	public DesktopRenderer Desktop;
	
	private long nextPID = 1;
	
	public Program() {
		thread = new Thread(this);
		
		setSize(windowX, windowY);
		setResizable(true);
		setTitle("JavaOS");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setPreferredSize(new Dimension(windowX, windowY));
		getContentPane().setBackground(Color.lightGray);
		setLocationRelativeTo(null);
		setLayout(null);
		
		pack();
		setVisible(true);
		
		Desktop = new DesktopRenderer(this, Windows, DesktopApps);
		Desktop.setSize(windowX, windowY);
		Desktop.setBackground(Color.lightGray);
		
		getContentPane().add(Desktop);
		
		Desktop.addMouseListener(this);
		Desktop.addMouseMotionListener(this);
		addKeyListener(this);
		addComponentListener(this);
	}
	public void AddElement(Window w) {
		windowsToAdd.add(w);
	}
	public void RemoveWindow(Window w) {
		Desktop.remove(w);
		windowsToRemove.add(w);
	}
	public void ExitApplication(Application app) {
		appsToQuit.add(app);
		app.SysExit();
	}
	private synchronized void start() {
		thread.start();
		
		new Timer().scheduleAtFixedRate(new TimerTask() {
	        @Override
	        public void run() {
	            for(int i=0; i<windowsToAdd.size(); i++) {
	            	Window w = windowsToAdd.get(i);
	            	Desktop.add(w, 0);
	        		w.Parent = Desktop;
	        		Windows.add(w);
	        		w.isAdded = true;
	        		sortWindows();
	            }
	            windowsToAdd.clear();
	            
	            for(Window w : windowsToRemove) {
	            	Windows.remove(w);
	            }
	            windowsToRemove.clear();
	            
	            for(Application a : appsToAdd) {
	            	RunningApps.add(a);
	            }
	            appsToAdd.clear();
	            
	            for(Application a : appsToQuit) {
	            	RunningApps.remove(a);
	            }
	            appsToQuit.clear();
	            
	            repaint();
	        }
	    }, 0, 250);
	}
	public void RunApplication(Application app) {
		app.PID = nextPID;
		nextPID++;
		appsToAdd.add(app);
		new Thread(() -> {
			app.Start();
		}).start();
	}
	public synchronized void stop() {
		try {
			thread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	public int randomInt(int min, int max) {
		return (int)(Math.random() * (max - min) + min);
	}
	public void sortWindows() {
		Collections.sort(Windows);
	}
	
	public static Program desktop;
	public static void main(String [] args) {
		desktop = new Program();
		
		//desktop.AddElement(new Window(200, 200, 10, 50, "Test-Window #0"));
		//desktop.AddElement(new Window(400, 200, 10, 50, "Test-Window #1"));
		
		AppInfo app0 = new AppInfo();
		app0.Title = "Terminal";
		app0.X = 0;
		app0.Y = 0;
		app0.Color = Color.black;
		app0.App = new TerminalApp(desktop);
		desktop.DesktopApps.add(app0);
		
		AppInfo app1 = new AppInfo();
		app1.Title = "Terminal";
		app1.X = 2;
		app1.Y = 1;
		app1.Color = Color.black;
		app1.App = new TerminalApp(desktop);
		desktop.DesktopApps.add(app1);
		
		AppInfo app2 = new AppInfo();
		app2.Title = "Settings";
		app2.X = 1;
		app2.Y = 0;
		app2.Color = new Color(0, 0, 255);
		app2.App = new DesktopSettingsApp(desktop);
		desktop.DesktopApps.add(app2);
		
		AppInfo app3 = new AppInfo();
		app3.Title = "Processes";
		app3.X = 1;
		app3.Y = 1;
		app3.Color = new Color(255, 0, 0);
		app3.App = new ProcessExplorerApp(desktop);
		desktop.DesktopApps.add(app3);
		
		desktop.start();
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		boolean ea = false;
		sortWindows();
		for(Window w : Windows) {
			if(!ea && w.contains(e.getX(), e.getY())) {
				if(!w.isFocused) w.Focus();
				w.mousePressed(e);
				ea = true;
			}
			else if(w.isFocused) w.UnFocus();
		}
		if(!ea) Desktop.mousePressed(e);
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		boolean ea = false;
		sortWindows();
		for(Window w : Windows) {
			if(!ea && w.contains(e.getX(), e.getY())) {
				if(!w.isFocused) w.Focus();
				w.mouseReleased(e);
				ea = true;
			}
			else if(w.isFocused) w.UnFocus();
		}
		if(!ea) Desktop.mouseReleased(e);
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		boolean ea = false;
		sortWindows();
		for(Window w : Windows) {
			if(!ea && w.contains(e.getX(), e.getY())) {
				if(!w.isFocused) w.Focus();
				w.mouseClicked(e);
				ea = true;
			}
			else if(w.isFocused) w.UnFocus();
		}
		if(!ea) Desktop.mouseClicked(e);
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		if(e.getX() >= 0 && e.getY() >= 0 && e.getX() < getWidth() && e.getY() < getHeight()) {
			boolean ea = false;
			for(Window w : Windows) {
				if(w.isFocused && w.contains(e.getX(), e.getY())) {
					w.mouseDragged(e);
					ea = true;
					break;
				}
			}
			if(!ea) Desktop.mouseDragged(e);
		}
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		boolean ea = false;
		for(Window w : Windows) {
			if(!ea && w.contains(e.getX(), e.getY())) {
				w.mouseMoved(e);
				ea = true;
			}
		}
		if(!ea) Desktop.mouseMoved(e);
	}
	@Override
	public void keyTyped(KeyEvent e) {
		boolean ea = false;
		for(Window w : Windows) {
			if(!ea && w.isFocused) {
				w.keyTyped(e);
				ea = true;
			}
		}
	}
	@Override
	public void keyPressed(KeyEvent e) {
		boolean ea = false;
		for(Window w : Windows) {
			if(!ea && w.isFocused) {
				w.keyPressed(e);
				ea = true;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			
		}
		else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			
		}
		else if(e.getKeyCode() == KeyEvent.VK_DELETE) {
			
		}

		else if(e.getKeyCode() == KeyEvent.VK_UP) {

		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN) {

		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		for(Window w : Windows) {
			if(w.isFocused) {
				w.keyReleased(e);
				break;
			}
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
}
