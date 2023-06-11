package net.ddns.x444556;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;

public class Window extends JPanel implements Comparable<Window> {
	private static final long serialVersionUID = -5605184804318096421L;
	
	public String Title;
	public boolean isFocused = false;
	public boolean isClosing = false;
	public boolean isAdded = false;
	
	public Application Owner;
	public JLayeredPane Parent;
	
	public final int headerHeight = 32;
	public final int borderWidth = 1;

	public Window(int width, int height, int posX, int posY, String title) {
		super();
		
		this.setLayout(null);
		this.setBackground(new Color(128, 128, 128));
		this.setForeground(new Color(0, 0, 0));
		this.setSize(width, height + headerHeight);
		this.setLocation(posX, posY);

		this.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), borderWidth, false));
		
		Title = title;
	}
	
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if(Owner != null) Owner.paintWindow(this, g.create(borderWidth, headerHeight, getWidth() - borderWidth, getHeight() - headerHeight));

        for(int y=0; y<headerHeight; y++) {
			g.setColor(new Color(80+2*y, 80+2*y, 80+2*y));
			g.drawLine(0, y, this.getWidth(), y);
		}
		
		g.setColor(new Color(70, 70, 70));
		g.drawLine(0, headerHeight-1, getWidth(), headerHeight-1);
		
		g.setColor(new Color(40, 40, 40));
		g.drawString(Title, 10, 20);
		
		g.setColor(new Color(200, 0, 0));
		g.fillRect(getWidth() - 30, 6, 22, 21);
		g.setColor(new Color(140, 0, 0));
		g.drawRect(getWidth() - 30, 6, 22, 21);
		g.setColor(new Color(200, 200, 200));
		g.drawLine(getWidth() - 27, 9, getWidth() - 12, 24);
		g.drawLine(getWidth() - 12, 9, getWidth() - 27, 24);
		
		if(isClosing) {
			g.setColor(new Color(255, 255, 255, 127));
			g.fillRect(0, 0, getWidth(), getHeight());
		}
    } 
	
	@Override
	public boolean contains(int x, int y) {
		return x >= getX() && x < getX() + getWidth() && y >= getY() && y < getY() + getHeight();
	}

	public void Focus() {
		isFocused = true;
		Parent.setLayer(this, 1);
		Parent.moveToFront(this);
		this.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), borderWidth, false));
	}
	public void UnFocus() {
		isFocused = false;
		Parent.setLayer(this, 0);
		Parent.moveToFront(this);
		this.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), borderWidth, false));
	}
	
	private boolean mouseIsDragging = false;
	public void mouseClicked(MouseEvent e) {
		if(Owner != null && e.getX() >= getX() + getWidth() - 30 && e.getX() < getX() + getWidth() - 8 && e.getY() >= getY() + 6 && 
				e.getY() < getY() + 27) {
			Owner.Close(this);
			return;
		}
		
		if(SwingUtilities.isMiddleMouseButton(e) && e.getY() < getY() + headerHeight) {
			mouseIsDragging = !mouseIsDragging;
		}
		
		if(e.getY() > getY() + headerHeight) {
			Owner.mouseClicked(e, this);
		}
	}
	public void mousePressed(MouseEvent e) {
		if(e.getY() > getY() + headerHeight) {
			Owner.mousePressed(e, this);
		}
	}
	public void mouseReleased(MouseEvent e) {
		if(e.getY() > getY() + headerHeight) {
			Owner.mouseReleased(e, this);
		}
	}
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	private int lastMouseX=0, lastMouseY=0;
	public void mouseDragged(MouseEvent e) {
		if(e.getY() < getY() + headerHeight || lastMouseY < headerHeight) {
			int movedX = e.getX() - lastMouseX;
			int movedY = e.getY() - lastMouseY;
			lastMouseX = e.getX();
			lastMouseY = e.getY();
			
			this.setLocation(getX() + movedX, getY() + movedY);
		}
		else {
			Owner.mouseMoved(e, this);
		}
	}
	public void mouseMoved(MouseEvent e) {
		lastMouseX = e.getX();
		lastMouseY = e.getY();
		
		if(e.getY() > getY() + headerHeight) {
			Owner.mouseMoved(e, this);
		}
	}
	public void keyTyped(KeyEvent e) {
		Owner.keyTyped(e, this);
	}
	public void keyPressed(KeyEvent e) {
		Owner.keyPressed(e, this);
	}
	public void keyReleased(KeyEvent e) {
		Owner.keyReleased(e, this);
	}

	@Override
	public int compareTo(Window o) {
		if(JLayeredPane.getLayer(o) == JLayeredPane.getLayer(this)) {
			return 0;
		}
		else if(JLayeredPane.getLayer(o) >= JLayeredPane.getLayer(this)) {
			return 1;
		}
		else {
			return -1;
		}
	}
}
