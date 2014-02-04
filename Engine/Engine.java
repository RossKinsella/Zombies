package Engine;

import java.awt.AWTException;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Menu;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import Entites.Player;
import Entites.Zombie;
import Entites.ZombieHandler;
import Graphics.CustomMenu;
import Graphics.RenderUnit;

public class Engine extends Canvas implements Runnable
{
	// canvas stuff
	static JFrame frame;
	public static int WIDTH = 880;
	public static int HEIGHT = WIDTH/12*9;
	public static final int SCALE = 1;
	public final String TITLE = "Zombies";
	
	// thread stuff
	private boolean running = false;
	private Thread thread;
	double delta = 0;
	double bulletDelta = 0;
	
	// modules
	private RenderUnit renderer;
	private static Robot robot;
	CollisionDetector cd;
	
	// entities
	private Player p;
	private ZombieHandler z;
	
	// TEMP
    long startTime;
    long currTime;
    long elapsedTime;
    boolean inGame;
    
    static int robotLimiter, currentCount;
	static Point screenLocation;
	Rectangle searchRect;		
	BufferedImage searchBox;
    
    double mouseX;
    double mouseY;
    
    static GraphicsDevice gd;
    static BufferStrategy bs;
    
    BufferedImage backBuffer;
    CustomMenu menu;
    
    static Component c;
	
	public static void main(String[] args)
	{
		Engine game = new Engine();
		int j = Math.max(2, 5);
		game.setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		game.setMaximumSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		game.setMinimumSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		
		frame = new JFrame(game.TITLE);
		frame.add(game);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		//temp

		
		 GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
		 gd=ge.getDefaultScreenDevice();
		

		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		robotLimiter = 20;
		currentCount = 0;
		

		
		game.start();
	}
	
	private synchronized void start()
	{
		
		if(running)
			return;
		
		running = true;
		thread = new Thread(this);
		thread.start();
		
		screenLocation = getLocationOnScreen();
		searchRect = new Rectangle((int)screenLocation.getX()-1,
				(int)screenLocation.getY()-1, getWidth(), getHeight());
		searchBox = robot.createScreenCapture(searchRect);
	}
	
	private synchronized void stop()
	{
		if(!running)
			return;
		
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(1);	
	}
	
	public void run()
	{
		init();
		long lastTime = System.nanoTime();
		final double ammountOfTicks = 60.0;
		double ns = 1000000000 / ammountOfTicks;

		int updates =0;
		int cycles = 0;
		long timer = System.currentTimeMillis();
		
		
		
		while(running) // THE MAIN LOOP
		{
           
           //System.out.println(currTime);
		  // System.out.println(elapsedTime);
			long now = System.nanoTime();
			delta += ( now - lastTime ) /ns;
			bulletDelta += ( now - lastTime ) /ns;
			lastTime = now;
			if(delta >= 1)
			{
				tick();
				render();
				updates++;
				delta--;
			}
			if(bulletDelta >= .06) 
				{
				int offsetX = getWidth()/ 2 -
					    Math.round(p.getX()+30);
				offsetX = Math.min(offsetX, 0);
				offsetX = Math.max(offsetX, getWidth() - renderer.getMap().getWidth());
				
				int offsetY = getHeight()/ 2 -
					    Math.round(p.getY()+30);
				offsetY = Math.min(offsetY, 0);
				offsetY = Math.max(offsetY, getHeight() - renderer.getMap().getHeight());
				
				p.updateBulletHandler(searchBox,offsetX,offsetY);
				bulletDelta = 0;
				}
			cycles++;
			
			if(System.currentTimeMillis() - timer > 1000)
			{
				timer += 1000;
				//System.out.println(updates + " Ticks, Cycles " + cycles);
				//System.out.println((long)delta);
				updates = 0;
				cycles = 0;
			}
		}
		stop();
	}
	
	private void init()
	{
		requestFocus();
		addKeyListener(new KeyInput (this));
		addMouseListener(new KeyInput (this));
	    addMouseMotionListener(new KeyInput (this));

		
		renderer = new RenderUnit(this);
		
		p = new Player(renderer.getPlayerUpperBody(),renderer.getPistolShot()
				,renderer.getBulletWound(),renderer.getPlayerLowerBody()
				,renderer.getNineMillMag(),renderer.getHealth(),this,renderer.getDeadPlayer());
		z = new ZombieHandler(renderer, p);
		
		renderer.importZombieHandler(z);	
		
		p.loadBulletHandler(z, renderer);
		
		cd = new CollisionDetector(renderer.getMap(),z);
		
		inGame = true;
		//System.out.println(renderer.getMap().get)
		
		Cursor target =
			    Toolkit.getDefaultToolkit().createCustomCursor(
			    Toolkit.getDefaultToolkit().getImage(""),
			    new Point(0,0),
			    "invisible");
	
		frame.setCursor(target);
		
		
		//menu = new CustomMenu(getWidth()/4, getHeight()/4, getWidth()/2, getHeight()/2, 5,
				//Color.WHITE, new Color(200,100,100,100));
		//menu.createButton(x, y, width, height, borderThickness, borderColor, bodyColor, text)
		
		/// temp
		//backBuffer = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
	}
	
	private void tick()
	{
		if(inGame)
		{
			if(currentCount == 0)
			{
				screenLocation = getLocationOnScreen();
				searchRect = new Rectangle((int)screenLocation.getX()-1,
						(int)screenLocation.getY()-1, getWidth(), getHeight());
				searchBox = robot.createScreenCapture(searchRect);
			}
			else if (currentCount == robotLimiter)
				currentCount = -1;
			p.update(1,searchBox);
			p.updateAnimation(1,cd);
			z.update(1,cd);
			currentCount++;
		}
		else
		{
			
		}
	}
	
	private void render()
	{
		bs = this.getBufferStrategy();
		
		if(bs == null)
		{
			createBufferStrategy(2);
			return;
		}
		
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		////////////////////////////////////////
		// all rendering takes place here   ////
		//									////
		renderer.render(g,1,mouseX,mouseY);
		//menu.render(g);
		if(!inGame)
		{
			g.setColor(Color.CYAN.darker().darker());
			g.fillRect((getWidth()/4)-8, (getHeight()/4)-8,
					8, (getHeight()/2)+16);
			g.fillRect((getWidth()/4)-8, (getHeight()/4)-8,
					(getWidth()/2)+16, 8);
			g.fillRect((getWidth()/4)-8, (getHeight()/4*3),
					(getWidth()/2)+16, 8);
			g.fillRect(((getWidth()/4)*3), (getHeight()/4)-8,
					8, (getHeight()/2)+16);
			
			
			g.setColor(new Color(150,150,150,180).darker().darker().darker());
			g.fillRect(getWidth()/4, getHeight()/4,
					getWidth()/2, getHeight()/2);

			
			g.setColor(Color.WHITE);
			g.fillRect(getWidth()/3-4, getHeight()/3-4, getWidth()/3+8, getHeight()/9+8);
			g.setColor(Color.DARK_GRAY);
			g.fillRect(getWidth()/3, getHeight()/3, getWidth()/3, getHeight()/9);
		}
		g.dispose();
		bs.show();	
		//									////
		//									////
		////////////////////////////////////////
		
	}
	
	
	public static Graphics2D getGraphicsS()
	{
		return (Graphics2D) bs.getDrawGraphics();
	}
	
	public void keyPressed(KeyEvent e)
	{
		int key = e.getKeyCode();
	if(key == KeyEvent.VK_UP  || key == KeyEvent.VK_W)
	{
		p.setVelocityY(-2);
	}
	else if(key == KeyEvent.VK_DOWN  || key == KeyEvent.VK_S)
	{
		p.setVelocityY(2);
	}
	else if(key == KeyEvent.VK_LEFT  || key == KeyEvent.VK_A)
	{
		p.setVelocityX(-2);
	}
	else if(key == KeyEvent.VK_RIGHT  || key == KeyEvent.VK_D)
	{
		p.setVelocityX(2);
	}}
	
	public void keyReleased(KeyEvent e)
	{
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_UP || key == KeyEvent.VK_W )
		{
			p.setVelocityY(0);
			p.resetAnimation();
		}
		else if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S)
		{
			p.setVelocityY(0);
			p.resetAnimation();
		}
		else if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A)
		{
			p.setVelocityX(0);
			p.resetAnimation();
		}
		else if(key == KeyEvent.VK_RIGHT|| key == KeyEvent.VK_D)
		{
			p.setVelocityX(0);
			p.resetAnimation();
		}
		else if(key == KeyEvent.VK_R)
		{
			spawnZombie();
			//setInGame(false);
		}
			
	}
	
	public void mouseMoved(double mouseX, double mouseY)
	{
		this.mouseX = mouseX;
		this.mouseY = mouseY;
	}
	
	public void mouseReleased()
	{
		int offsetX = getWidth()/ 2 -
			    Math.round(p.getX()+30);
		offsetX = Math.min(offsetX, 0);
		offsetX = Math.max(offsetX, getWidth() - renderer.getMap().getWidth());
		
		int offsetY = getHeight()/ 2 -
			    Math.round(p.getY()+30);
		offsetY = Math.min(offsetY, 0);
		offsetY = Math.max(offsetY, getHeight() - renderer.getMap().getHeight());
		
		p.setIsAttacking();
		p.shoot((int)mouseX, (int)mouseY, offsetX, offsetY);
	}
	
	public Player getPlayer()
	{
		return p;
	}
	
	public ZombieHandler getZombieHandler()
	{
		return z;
	}
	
	public void spawnZombie()
	{
		z.spawnZombie();
	}


	public void setInGame(boolean status)
	{
		inGame = status;
		if(status == false) 
			frame.setCursor(Cursor.getDefaultCursor());
	}



}
