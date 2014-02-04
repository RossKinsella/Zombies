package Graphics;

import java.awt.Color;
import java.awt.Graphics2D;



public class CustomMenu extends CustomMenuComponent
{
	CustomButton[] buttons;
	
	public CustomMenu(int x, int y, int width, int height, int borderThickness, Color borderColor, Color bodyColor)
	{
		super(x,y,width,height,borderThickness,borderColor,bodyColor);
		buttons = new CustomButton[10];
	}
	
	public void createButton(int x, int y, int width, int height, int borderThickness, Color borderColor, Color bodyColor
			, String text)
	{
		buttons[0] = new CustomButton(x,y,width,height,borderThickness,borderColor,bodyColor);
	}
	
}
