package Graphics;

import java.awt.Color;
import java.awt.Graphics2D;

public class CustomMenuComponent 
{

	private int mX,mY,mWidth,mHeight;
	private int mBorderX,mBorderY,mBorderWidth,mBorderHeight, mBorderThickness;
	private Color mBorderColor,mBodyColor;
	
	public CustomMenuComponent(int x, int y, int width, int height, int borderThickness, Color borderColor, Color bodyColor)
	{
		mX = x;
		mY = y;
		mWidth = width;
		mHeight = height;
		
		mBorderX = x - borderThickness;
		mBorderY = y - borderThickness;
		mBorderWidth = width + borderThickness ;
		mBorderHeight = height + borderThickness ;
		
		mBorderColor = borderColor;
		mBodyColor = bodyColor;
		
		mBorderThickness = borderThickness;
	}
	
	public void render(Graphics2D g)
	{
		drawBorder(g);
		drawBody(g);
	}
	
	private void drawBorder(Graphics2D g)
	{
		g.setColor(mBorderColor);
		g.fillRect(mBorderX,mBorderY,mBorderThickness,mBorderHeight); // topLeft going down
		g.fillRect(mBorderX,mBorderY,mBorderWidth,mBorderThickness); // topLeft going right
		g.fillRect(mBorderX+mBorderWidth,mBorderY,mBorderThickness,mBorderHeight); // topRight going down
		g.fillRect(mBorderX,mBorderY+mBorderHeight,mBorderWidth+mBorderThickness,mBorderThickness); // botLeft going right
	}
	
	private void drawBody(Graphics2D g)
	{
		g.setColor(mBodyColor);
		g.fillRect(mX, mY, mWidth, mHeight);
	}
	
}
