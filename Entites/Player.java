package Entites;


import java.awt.AWTException;
import java.awt.Graphics2D;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import Engine.BulletHandler;
import Engine.CollisionDetector;
import Engine.Engine;
import Graphics.Animation;
import Graphics.RenderUnit;



public class Player extends Creature {
	Sprite wound;
	Animation woundAni, magazine, healthAnimation;
	int woundCurrentDuration, health;
	Robot r;
	private BulletHandler mBulletHandler;
	private boolean alive;
	private Engine e;
	private BufferedImage deadPlayer;
	
	public Player(Animation upperBody, Animation attacking, Animation wound, Animation lowerBody,
			Animation magazine, Animation healthAnimation, Engine e, BufferedImage deadPlayer)
	{
		super(upperBody,attacking,lowerBody);
		try {
			r = new Robot();
		} catch (AWTException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		woundAni = wound;
		this.healthAnimation = healthAnimation;
		health = 38;
		woundCurrentDuration = 0;
		this.magazine = magazine;
		setX(200);
		setY(20);
		isPlayer = true;
		alive = true;
		this.e = e;
		this.deadPlayer = deadPlayer;
	}
	
	public void loadBulletHandler(ZombieHandler zh, RenderUnit ru)
	{
		mBulletHandler = new BulletHandler(zh,this,ru);
	}
	
	
	
	public void shoot(int mouseX, int mouseY, int offsetX, int offsetY)
	{
		mBulletHandler.firePistolBullet(mouseX,mouseY,offsetX, offsetY);
		magazine.update(1);
	}
	
	
	public void update(long elapsedTime,BufferedImage currentScreen)
	{
		if(wound != null)
		{
			wound.getAni().update(elapsedTime);
			wound.update(elapsedTime);
			woundCurrentDuration+=elapsedTime;
			if(woundCurrentDuration >= wound.getAni().getDuration())
			{
				woundCurrentDuration = 0;
				wound = null;
			}
		}
		
		//mBulletHandler.update(currentScreen);
	}
	
	public void updateBulletHandler(BufferedImage currentScreen,int offsetX,int offsetY)
	{
		mBulletHandler.update(currentScreen, offsetX,offsetY);
	}
	public Sprite getWound()
	{
		return wound;
	}

	public void render(Graphics2D g, int offsetX,int offsetY, int mouseX, int mouseY)
	{
		if(alive)
		{
		int centerX = (int) (getCenterX()+offsetX);
		int centerY = (int) getCenterY()+offsetY;
		double angle = Math.atan2(centerY - mouseY, centerX - mouseX) - Math.PI / 2;
		
		// rotate player lowerBody according to movement direction
		int playerDeltaX = (int)getVelocityX();
		int playerDeltaY = (int)getVelocityY();
		int degreesToRotate = 0;
		if(playerDeltaX > 0 && playerDeltaY < 0) degreesToRotate = 45;
		else if(playerDeltaX > 0 && playerDeltaY == 0) degreesToRotate = 45*2;
		else if(playerDeltaX > 0 && playerDeltaY > 0) degreesToRotate = 45*3;
		else if(playerDeltaX == 0 && playerDeltaY > 0) degreesToRotate = 45*4;
		else if(playerDeltaX < 0 && playerDeltaY > 0) degreesToRotate = 45*5;
		else if(playerDeltaX < 0 && playerDeltaY == 0) degreesToRotate = 45*6;
		else if(playerDeltaX < 0 && playerDeltaY < 0) degreesToRotate = 45*7;
		


		
		g.rotate(Math.toRadians(degreesToRotate), centerX, centerY);
		g.drawImage(getLowerBodyImage(),(int)getX()+offsetX,(int)getY()+offsetY,null);
		g.rotate(Math.toRadians(-degreesToRotate), centerX, centerY);

		
		// rotate player upperBody toward mouse and draw
		g.rotate(angle, centerX, centerY);
		g.drawImage(getUpperBodyImage(),(int)getX()+offsetX,(int)getY()-15+offsetY,null);
		g.rotate(-angle, centerX,centerY);
		}
		else
		{
			g.drawImage(deadPlayer,(int)getX(),(int)getY(),null);
		}
		
		
		
		mBulletHandler.render(g,offsetX,offsetY);
		g.drawImage(magazine.getImage(), 135, 580,null);
		g.drawImage(healthAnimation.getImage(),220,580,null);
	}
	
	public void takeHit(int damage)
	{
		health -= damage;
		if(health >= 0)healthAnimation.update(damage);
		else 
			{
			alive = false;
			e.setInGame(false);
			}
		//else healthAnimation.
	}
	
	public boolean isAlive()
	{
		return alive;
	}
}
