package net.ddns.x444556;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.util.ArrayList;

public class TerminalApp extends Application {

	private Window cli;
	private Program javaos;
	
	private String Screen="JavaOS Terminal - by x444556\n\n", Input="", Prompt=">> ";
	
	public TerminalApp(Program javaos) {
		super(javaos);
		this.javaos = javaos;
		Name = "Terminal";
	}
	
	public void Execute(String cmd) {
		System.out.println("[Terminal] Executing command: \"" + cmd + "\"");
		
		String[] splits = cmd.split(" ");
		
		if(splits[0].equalsIgnoreCase("window") && splits.length >= 2) {
			if(splits[1].equalsIgnoreCase("new")) {
				AddWindow(new Window(256, 64, javaos.randomInt(0, javaos.getWidth() - 272), 
						javaos.randomInt(0, javaos.getHeight() - cli.headerHeight - 64), "Terminal - Child"));
				Screen += "OK\n";
			}
			else if(splits[1].equalsIgnoreCase("attach")) {
				Application a = this;
				long pid = Long.parseLong(splits[2]);
				for(Application a2 : javaos.RunningApps) {
					if(a2.PID == pid) {
						a = a2;
						break;
					}
				}
				
				a.AddWindow(new Window(256, 64, javaos.randomInt(0, javaos.getWidth() - 272), 
						javaos.randomInt(0, javaos.getHeight() - cli.headerHeight - 64), "Terminal - Child (Remote Attached)"));
				Screen += "OK\n";
			}
			else if(splits[1].equalsIgnoreCase("wipe")) {
				Application a = this;
				long pid = Long.parseLong(splits[2]);
				for(Application a2 : javaos.RunningApps) {
					if(a2.PID == pid) {
						a = a2;
						break;
					}
				}
				for(Window w : a.Windows) {
					javaos.RemoveWindow(w);
				}
				a.Windows.clear();
				Screen += "OK\n";
			}
			else {
				Screen += "ERROR: unknown subcommand \"" + splits[1] + "\"\n";
			}
		}
		else if(splits[0].equalsIgnoreCase("app")) {
			if(splits[1].equalsIgnoreCase("kill")) {
				Application a = this;
				long pid = Long.parseLong(splits[2]);
				for(Application a2 : javaos.RunningApps) {
					if(a2.PID == pid) {
						a = a2;
						break;
					}
				}
				javaos.ExitApplication(a);
				Screen += "OK\n";
			}
			else {
				Screen += "ERROR: unknown subcommand \"" + splits[1] + "\"\n";
			}
		}
		else {
			Screen += "ERROR: unknown command \"" + splits[0] + "\" or invalid Syntax\n";
		}
	}

	@Override
	public void paintWindow(Window w, Graphics g) {
		if(w != cli) return;
		
		g.setColor(new Color(40, 40, 40));
		g.fillRect(0, 0, cli.getWidth(), cli.getHeight());
		
		int charWidth = (int) g.getFont().getStringBounds("-", new FontRenderContext(null, true, true)).getWidth();
		int charHeight = (int) g.getFont().getStringBounds("-", new FontRenderContext(null, true, true)).getHeight();
		int charsPerLine = (cli.getWidth() - 16) / charWidth;
		int linesPerScreen = (cli.getHeight() - cli.headerHeight - charHeight) / charHeight;
		
		String[] lines = Screen.split("\n");
		ArrayList<String> linesFit = new ArrayList<String>();
		for(String l : lines) {
			if(l.length() < charsPerLine) linesFit.add(l);
			else {
				String t = "";
				for(int i=0; i<l.length(); i++) {
					char c = l.charAt(i);
					t += c;
					if(i == charsPerLine) {
						linesFit.add(t);
						t = "";
					}
				}
			}
		}
		
		g.setColor(new Color(200, 200, 200));
		int y=charHeight;
		for(int i=(lines.length <= linesPerScreen ? 0 : lines.length - linesPerScreen); i<lines.length; i++) {
			g.drawString(lines[i], 0, y);
			y += charHeight;
		}
		g.drawString(Prompt + Input, 0, linesPerScreen * charHeight + charHeight);
	}
	@Override 
	public Application CreateNew() {
		return new TerminalApp(javaos);
	}
	@Override
	public void Start() {
		cli = new Window(javaos.getWidth() / 2, javaos.getHeight() / 2, javaos.randomInt(0, javaos.getWidth() / 2 - 16), 
				javaos.randomInt(0, javaos.getHeight() / 2 - 64), "Terminal");
		AddWindow(cli);
		while(!cli.isAdded)
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		System.out.println("asdad");
		javaos.sortWindows();
		cli.Focus();
		javaos.sortWindows();
		cli.UnFocus();
		javaos.sortWindows();
	}
	@Override
	public void Close(Window w) {		
		if(w == cli) this.Exit();
		else {
			javaos.RemoveWindow(w);
			Windows.remove(w);
		}
	}
	@Override
	public void keyReleased(KeyEvent e, Window w) {
		if(w != cli) return;
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			Screen += Prompt + Input + "\n";
			Execute(Input);
			Input = "";
		}
		else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			if(Input.length() > 0) Input = Input.substring(0, Input.length() - 1);
		}
		else if(e.getKeyChar() != '\n') {
			Input += e.getKeyChar();
		}
		cli.repaint();
	}
}
