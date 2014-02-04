package Graphics;

import java.awt.image.BufferedImage;

import java.awt.image.BufferedImage;

public class SpriteSheet 
{

	private BufferedImage image;
	
	
	
	public SpriteSheet(BufferedImage ss)
	{
		this.image = ss;
			
	}
	
	public BufferedImage grabImage(int col, int row, int width, int height)
	{
		//System.out.println(col);
		BufferedImage img = image.getSubimage((col*width) -width , (row*height)-height, width, height);
		return img;	
	}
	
	public BufferedImage grabImage(int x, int y, int width, int height, int alt)
	{
		//System.out.println(col);
		BufferedImage img = image.getSubimage(x , y, width, height);
		return img;	
	}
	
}
