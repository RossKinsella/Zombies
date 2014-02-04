package Entites;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Engine.CollisionDetector;
import Graphics.Animation;
import Graphics.RenderUnit;

public class ZombieHandler 
{
	Zombie[] theZombies;
	int currentSpawn;
	RenderUnit r;
	Player p;
	
	public ZombieHandler(RenderUnit renderUnit,Player player)
	{
		r = renderUnit;
		p = player;
		
		currentSpawn = 0;
		theZombies = new Zombie[100];
		spawnZombie();

	}
	
	public void update(int elapsedTime, CollisionDetector cd)
	{
		for(int i =0; i < theZombies.length;i++)
		{
			if(theZombies[i] != null)
			{
				theZombies[i].update(elapsedTime, cd);
			}
		}
	}
	
	public void renderAlive(Graphics2D g,int offsetX,int offsetY)
	{
		for(int i =0; i < theZombies.length;i++)
		{
			if(theZombies[i] != null && theZombies[i].isAlive())
			{
				theZombies[i].render(g,offsetX,offsetY);
			}
		}	
	}
	
	public void renderDead(Graphics2D g, int offsetX, int offsetY)
	{
		for(int i =0; i < theZombies.length;i++)
		{
			if(theZombies[i] != null && !theZombies[i].isAlive())
			{
				theZombies[i].render(g,offsetX,offsetY);
			}
		}	
	}
	
	public void spawnZombie()
	{
		Animation zombieWalking = r.getZombieWalking().clone();
		Animation woundAnimation = r.getBulletWound();
		BufferedImage deadZombieImage = r.getDeadZombieImage();
		Animation bleedOutOnDeath = r.getZombieBleeding(currentSpawn);
		
		
		
		theZombies[currentSpawn++] = new Zombie(zombieWalking,zombieWalking,woundAnimation,
				bleedOutOnDeath
				,deadZombieImage,p, r);
		
		if(currentSpawn == 100) currentSpawn = 0;
		
	}
	
	public int getNumberOfZombies()
	{
		return theZombies.length;
	}
	
	public Zombie getZombie(int i)
	{
		return theZombies[i];
	}
	
	public int getIndex(double zombieX, double zombieY)
	{
		int result= 0;
		for(int i =0; i<theZombies.length;i++)
		{
			if(theZombies[i].isAlive())
			{
				if(theZombies[i].getX() == zombieX && theZombies[i].getY() == zombieY)
				{
					result = i;
					break;
				}
			}
		}
		return result;
	}
	
}
