package Engine;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import Entites.Sprite;
import Entites.Zombie;
import Entites.ZombieHandler;


public class CollisionDetector 
{
	Rectangle2D.Double[] thePlayerWalls, theZombieWalls;
	boolean [][] usedPixels;
	ZombieHandler zh;
	
	public CollisionDetector(BufferedImage theMap, ZombieHandler zh)
	{
		//loadCollionRectangles(theMap);
		loadCollisionRectangles();
		this.zh = zh;
	}
	
	/**
	*	This method searches for black pixels:
	*	- When one is found, it then counts how many black pixels are to the right of 
	*	  said pixel and how many black pixels go down from it.
	*
	*	- A new rectangle is added(if the area of black pixels is greater than 7*7)
	*     to the collection with the initial x and y of the first
	*	  pixel found and then its width and height set to how many are to the right and down.	
	*
	*	- to speed loading times a boolean[][] represents which pixels are used already for a 
	*	  rectangle.
	*
	*	@author Ross.
	*/
	private void loadCollionRectangles(BufferedImage theMap)
	{
		int wallThickness = 7;
		int mapWidthInPixels = theMap.getWidth();
		int mapHeightInPixels = theMap.getHeight();
		int blackInRGB = -16777216;
		int currentRectIndex = 0;
		
		thePlayerWalls = new Rectangle2D.Double[100000];
		
		usedPixels = new boolean[mapHeightInPixels][mapWidthInPixels];
		for(int i = 0; i< usedPixels.length; i++)
		{
			for(int j = 0; j < usedPixels[0].length; j++)
			{
				System.out.println("test 1");
				usedPixels[i][j] = false;
			}
		}
		
		
		for(int i = 0; i < mapWidthInPixels; i++)
		{
			for(int j = 0; j < mapHeightInPixels; j++)
			{
				if(theMap.getRGB(i, j) == blackInRGB && usedPixels[i][j] == false)
				{
					int widthOfProspectiveRect;
					for(widthOfProspectiveRect = 1; theMap.getRGB(i,j+widthOfProspectiveRect) == blackInRGB
							&& (j+widthOfProspectiveRect+1) < mapWidthInPixels;widthOfProspectiveRect++)
					{	System.out.println("test 2 col = " + (j+widthOfProspectiveRect) + " row = " + i );	}
					int HeightOfProspectiveRect;
					for(HeightOfProspectiveRect = 1; theMap.getRGB(i+HeightOfProspectiveRect,j) == blackInRGB
							&& (i+HeightOfProspectiveRect+1) < mapHeightInPixels;HeightOfProspectiveRect++)
					{	System.out.println("test 3");	}
					
					if(widthOfProspectiveRect >= wallThickness && HeightOfProspectiveRect >= wallThickness)
					{
						System.out.println("Rect at i = "+i+" , j = " +j + " is " + widthOfProspectiveRect + " wide and " + HeightOfProspectiveRect + " high.");
						thePlayerWalls[currentRectIndex++] = new Rectangle2D.Double(j, i, widthOfProspectiveRect, HeightOfProspectiveRect);
						updateUsedPixels(i,j,HeightOfProspectiveRect,widthOfProspectiveRect);
					}
				}
			}
		}
	}

	private void updateUsedPixels(int i, int j, int heightOfProspectiveRect,
			int widthOfProspectiveRect) 
	{
		for(int r = i; r < heightOfProspectiveRect; r++)
		{
			for(int c = j; c< widthOfProspectiveRect; c++)
			{
				usedPixels[r][c] = true;
				System.out.println("test 4");
			}
		}
		
	}

	private void loadCollisionRectangles()
	{
		thePlayerWalls = new Rectangle2D.Double[11];
		thePlayerWalls[0] = new Rectangle2D.Double(0,0,176,348);
		thePlayerWalls[1] = new Rectangle2D.Double(34,348,11,290);
		thePlayerWalls[2] = new Rectangle2D.Double(0,638,176,348);
		thePlayerWalls[3] = new Rectangle2D.Double(35,986,11,281);
		thePlayerWalls[4] = new Rectangle2D.Double(1336,0,11,188);
		thePlayerWalls[5] = new Rectangle2D.Double(1104,187,451,415);
		thePlayerWalls[6] = new Rectangle2D.Double(1328,602,11,108);
		thePlayerWalls[7] = new Rectangle2D.Double(1105,710,450,414);
		thePlayerWalls[8] = new Rectangle2D.Double(1370,1124,11,143);
		thePlayerWalls[9] = new Rectangle2D.Double(0,0,1680,5);
		thePlayerWalls[10] = new Rectangle2D.Double(0,1262,1680,5);
		
		
		theZombieWalls = new Rectangle2D.Double[9];
		theZombieWalls[0] = new Rectangle2D.Double(0,0,176,348);
		theZombieWalls[1] = new Rectangle2D.Double(34,348,11,290);
		theZombieWalls[2] = new Rectangle2D.Double(0,638,176,348);
		theZombieWalls[3] = new Rectangle2D.Double(35,986,11,281);
		theZombieWalls[4] = new Rectangle2D.Double(1336,0,11,188);
		theZombieWalls[5] = new Rectangle2D.Double(1104,187,451,415);
		theZombieWalls[6] = new Rectangle2D.Double(1328,602,11,108);
		theZombieWalls[7] = new Rectangle2D.Double(1105,710,450,414);
		theZombieWalls[8] = new Rectangle2D.Double(1370,1124,11,143);
	}
	
	public boolean playerCollides(Rectangle2D.Double sprite)
	{
		boolean result = false;
		for(int i = 0; i<thePlayerWalls.length;i++)
		{
			if(sprite.intersects(thePlayerWalls[i]))
				result = true;
		}
		return result;
	}
	
	public boolean zombieCollides(Rectangle2D.Double sprite, Sprite z)
	{
		boolean result = false;
		for(int i = 0; i<theZombieWalls.length;i++)
		{
			if(sprite.intersects(theZombieWalls[i]))
				result = true;
		}
		
		int currentZombieIndex = zh.getIndex(z.getX(),z.getY());
		double currentZombieX;
		double currentZombieY;
		Rectangle2D.Double currentZombieRect;
		for(int i = 0; i< zh.getNumberOfZombies() ;i++)
		{
			if(i != currentZombieIndex && zh.getZombie(i)!=null && zh.getZombie(i).isAlive())
			{
				currentZombieX = zh.getZombie(i).getX();
				currentZombieY = zh.getZombie(i).getY();
				currentZombieRect = new Rectangle2D.Double(currentZombieX,currentZombieY,25,25);
				if(sprite.intersects(currentZombieRect))
				{
					if(zh.getZombie(currentZombieIndex).getVelocityX() != 0
							||	zh.getZombie(currentZombieIndex).getVelocityY() != 0 )
						result = true;
					if(!zh.getZombie(currentZombieIndex).isStuck())
						zh.getZombie(currentZombieIndex).setStuck();
					
				}
			}
		}
		return result;
	}
	
}
