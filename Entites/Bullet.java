package Entites;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Engine.Engine;
import Engine.LineIterator;

public class Bullet 
{
	private int mMouseX,mMouseY;
	private ZombieHandler zh;
	private Point mTargetPoint, mBarrelPoint;
	
	private int mCurrentX,mCurrentY;
	
	private Line2D mTrajectory;
	private List<Point2D> mTrajectoryPixels;
	private int mCurrentTrajectoryIndex, mHitZombie;
	Zombie z;
	private BufferedImage bulletImage;
	
	boolean mBulletEntered;
	
	public Bullet(Player p, ZombieHandler zh, int mouseX, int mouseY, BufferedImage bulletImage,int offsetX
			,int offsetY)
	{
		mMouseX = mouseX;
		mMouseY = mouseY;
		this.zh = zh;
		this.bulletImage = bulletImage;
		
		mTargetPoint = new Point(mMouseX , mMouseY);
		if(offsetX <= 0 && offsetY <= 0)
			mBarrelPoint = new Point((int)p.getX()+32+offsetX,(int)p.getY()+32+offsetY);
		else if(offsetX > 0 && offsetY <= 0)
			mBarrelPoint = new Point((int)p.getX()+offsetX,(int)p.getY()+32+offsetY);
		else if(offsetX <= 0 && offsetY > 0)
			mBarrelPoint = new Point((int)p.getX()+32+offsetX,(int)p.getY()+offsetY);
		else
			mBarrelPoint = new Point((int)p.getX()+offsetX,(int)p.getY()+offsetY);
			
			
		mTrajectory = new Line2D.Float(mBarrelPoint,mTargetPoint);
		
		mTrajectoryPixels = new ArrayList<Point2D>();
		Point2D currentPoint;
		for(Iterator<Point2D> iter = new LineIterator(mTrajectory); iter.hasNext();)
		{
			currentPoint = iter.next();
			mTrajectoryPixels.add(currentPoint);
		}
		mCurrentTrajectoryIndex = 30;
		mBulletEntered = false;
		
	}
	
	public boolean update(BufferedImage currentScreen, int offsetX, int offsetY)
	{
		boolean result = false;
		try{
		if (mTrajectoryPixels.size() < 30) return true;
		mCurrentX = (int) mTrajectoryPixels.get(mCurrentTrajectoryIndex).getX();
		mCurrentY = (int) mTrajectoryPixels.get(mCurrentTrajectoryIndex).getY();
		
		if(checkForCollision(currentScreen,mTrajectoryPixels.get(mCurrentTrajectoryIndex),offsetX,offsetY))
		 result = true;
		mCurrentTrajectoryIndex++;
		if(mCurrentTrajectoryIndex >= mTrajectoryPixels.size())
			result = true;
		}
		catch(Exception e)
		{
			result = true;
		}
		return result;
	}
	
	public boolean checkForCollision(BufferedImage currentScreen, Point2D currentPoint, int offsetX
			,int offsetY)
	{
		boolean result = false;

		for(int i = 0; i<zh.getNumberOfZombies();i++)
		{

			if(!mBulletEntered) z = zh.getZombie(i);
			else z = zh.getZombie(mHitZombie);
			if(z!=null && z.isAlive())
			{
				if(new Rectangle.Double(z.getX()+offsetX, z.getY()+offsetY, 64, 64).intersectsLine(mTrajectory))
				{
					//System.out.println("test 1");
/*					System.out.print("if ((" + (mCurrentX) + " > " + (z.getX()));
					System.out.println(") && ( " + (mCurrentX) + " < " + (z.getX()+64)+ ")");
					
					System.out.print(" && ( " + (mCurrentY) + " > " + (z.getY()));
					System.out.println(") && (" + (mCurrentY) + " < " + (z.getY()+64 ) + "))");*/
				//	System.out.println("If: " + mCurrentX + " > " + z.getX() + " && " + mCurrentX + " < " + z.getX() + 32);
					if(mCurrentX > z.getX()+offsetX && mCurrentX < z.getX()+64+offsetX 
							&& mCurrentY > z.getY()+offsetY  && mCurrentY < z.getY()+64+offsetY )
					{
						//System.out.println("test 2");
						if(currentScreen.getRGB((int)currentPoint.getX(),(int)currentPoint.getY()) == -16777216)
						{
							System.out.println("hit");

								z.takeHit(findExitWound(currentScreen,z,offsetX,offsetY),offsetX,offsetY);
								result = true;	
						}
					}
				}
			}
			if(result == true) i = zh.getNumberOfZombies(); // if there is a hit, break 
															//(prevents a bullet hitting multiple).
		}
		return result;
	}
	
	private Point2D findExitWound(BufferedImage currentScreen, Zombie z, int offsetX, int offsetY)
	{
		Point2D exitWound = new Point();
		mCurrentTrajectoryIndex = mTrajectoryPixels.size();
		for(int i = mTrajectoryPixels.size()-1;  i >= 0 || exitWound == null; i--)
		{
			Point2D currentPoint = mTrajectoryPixels.get(i);
			mCurrentX = (int) currentPoint.getX();
			mCurrentY = (int) currentPoint.getY();
			if(mCurrentX > z.getX() + offsetX && mCurrentX < z.getX() + offsetX + 64
					&& mCurrentY > z.getY() + offsetY && mCurrentY < z.getY() + offsetY + 64)
			{
				if(currentScreen.getRGB((int)currentPoint.getX(),(int)currentPoint.getY()) == -16777216)
				{
					exitWound = currentPoint;
					break;
				}
			}
		}
		return exitWound;
	}
	
	public void render(Graphics2D g, int offsetX, int offsetY)
	{
		try
		{
		g.drawImage(bulletImage, (int)mTrajectoryPixels.get(mCurrentTrajectoryIndex).getX(),
				(int)mTrajectoryPixels.get(mCurrentTrajectoryIndex).getY(),null);
		}
		catch(Exception e)
		{}
		

		for(int i = 0; i<zh.getNumberOfZombies();i++)
		{
			z = zh.getZombie(i);
			if(z!=null && z.isAlive())
			{
				//System.out.println("rect");
				//g.drawRect((int)z.getX()+offsetX, (int)z.getY()+offsetY, 64, 64);
				//mCurrentX > z.getX() && mCurrentX < z.getX()+64 
				//&& mCurrentY > z.getY()  && mCurrentY < z.getY()+64
			}	
		}		
		int size = mTrajectoryPixels.size();
			//g.drawLine((int)mTrajectoryPixels.get(0).getX(),(int) mTrajectoryPixels.get(0).getY(),
				//	(int)mTrajectoryPixels.get(size-1).getX(),(int) mTrajectoryPixels.get(size-1).getY());
		
		//if(z != null )g.fillRect((int)z.getX()+offsetX, (int)z.getY()+offsetY, 64, 64);
/*		if(mCurrentX > z.getX() && mCurrentX < z.getX() + 64
				&& mCurrentY > z.getY() && mCurrentY < z.getY() + 64)*/
	}
	
}
