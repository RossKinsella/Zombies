package Map;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import Graphics.BufferedImageLoader;

public class Field 
{
	private int width,height;
	private BufferedImage[] normalTiles;
	private BufferedImage[] specialTiles, treeTiles;
	private BufferedImage theMap, grass;
	private Random random;
	
	public Field(int width, int height, BufferedImageLoader loader)
	{
		this.width = (int) (width*2.5) ;
		this.height = (int) (height*2.5) ;
		
		System.out.println(this.width);
		System.out.println(this.height);
		
		random = new Random();
		
		normalTiles = new BufferedImage[5];
		specialTiles = new BufferedImage[1];
		treeTiles = new BufferedImage[3];
		try {
			theMap = loader.loadImage("/Zombie map.png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.width = theMap.getWidth();
		this.height = theMap.getHeight();
		//initTileImages(loader);
		//generateMap();
		
	}
	
	private void initTileImages(BufferedImageLoader loader)
	{
		try {
			grass = loader.loadImage("/grass from tutorial.png");
			normalTiles[0] = loader.loadImage("/normal 1.png");
			normalTiles[1] = loader.loadImage("/normal 2.png");
			normalTiles[2] = loader.loadImage("/normal 3.png");
			normalTiles[3] = loader.loadImage("/normal 4.png");
			normalTiles[4] = loader.loadImage("/normal 5.png");
			specialTiles[0] =  loader.loadImage("/blooded 1.png");
			treeTiles[0] =  loader.loadImage("/tree 1.png");
			treeTiles[1] =  loader.loadImage("/tree 2.png");
			treeTiles[2] =  loader.loadImage("/tree 3.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void generateMap()
	{
		theMap = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		boolean bloodedUsed = false;
		int treesUsed = 0;
		Random r = new Random();
		for(int i= -55; i< width; i+= 48)
		{
			for(int j = -55; j < height; j+= 48)
			{
				theMap.createGraphics().drawImage(grass,i+random.nextInt(50),j+random.nextInt(50),null);
/*				int chance = random.nextInt(50);
				if(!bloodedUsed) 
				{
					if(chance == 10 && i != 0)
					{
						theMap.createGraphics().drawImage(specialTiles[0],i,j,null);
						bloodedUsed = true;
					}
					else
						theMap.createGraphics().drawImage(normalTiles[random.nextInt(5)],i,j,null);
				}
				else if(treesUsed != 4 && chance == 15)
				{
					theMap.createGraphics().drawImage(treeTiles[random.nextInt(3)],i,j,null);
					treesUsed++;
				}
				else
					theMap.createGraphics().drawImage(normalTiles[random.nextInt(5)],i,j,null);*/
			}
		}
	}
	
	public BufferedImage getMap()
	{
		return theMap;
	}
	
}
