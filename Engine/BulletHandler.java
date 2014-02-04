package Engine;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Entites.Bullet;
import Entites.Player;
import Entites.ZombieHandler;
import Graphics.RenderUnit;

public class BulletHandler 
{
	ZombieHandler zh;
	Player p;
	Bullet[] bullets;
	
	BufferedImage pistolBullet;
	
	public BulletHandler(ZombieHandler zh, Player p,RenderUnit renderer)
	{
		this.zh = zh;
		this.p = p;
		bullets = new Bullet[10];
		
		pistolBullet = renderer.getPistolBullet();
	}
	
	public void update(BufferedImage currentScreen, int offsetX, int offsetY)
	{
		for(int i = 0; i<bullets.length;i++)
		{
			if(bullets[i] != null)
				if(bullets[i].update(currentScreen, offsetX, offsetY))
					bullets[i] = null;
		}
	}
	
	public void render(Graphics2D g, int offsetX, int offsetY)
	{
		for(int i = 0; i< bullets.length;i++)
		{
			if(bullets[i] != null)bullets[i].render(g, offsetX, offsetY);
		}
	}
	
	public void firePistolBullet(int mouseX, int mouseY, int offsetX, int offsetY)
	{
		bullets[findVacantBulletIndex()] = new Bullet(p,zh,mouseX,mouseY,pistolBullet,offsetX, offsetY);	
	}
	
	private int findVacantBulletIndex()
	{
		int result = -1;
		for(int i = 0; result == -1;i++)
		{
			if(bullets[i]  == null) result = i;
		}
		return result;
	}
	
	
	
}
