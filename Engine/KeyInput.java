package Engine;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class KeyInput extends Engine implements KeyListener,
MouseMotionListener, MouseListener, MouseWheelListener
{
	Engine game;
	
	public KeyInput(Engine game)
	{
		this.game = game;
		
	}
	
	public void keyPressed(KeyEvent e)
	{
		game.keyPressed(e);	
	}
	
	public void keyReleased(KeyEvent e)
	{
		game.keyReleased(e);
	}
	
	public void mousePressed()
	{
	}
	
	public void mouseMoved(MouseEvent e)
	{
		Point p = e.getPoint();
		double mouseY = p.getY();
		double mouseX = p.getX();
		
		game.mouseMoved(mouseX, mouseY);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		game.mouseReleased();
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
