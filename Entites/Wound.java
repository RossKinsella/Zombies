package Entites;

import java.awt.Graphics2D;

import Graphics.Animation;


/**
 * A class to manage the creation, upating and termination of a 
 * wound
 * 
 * @author ross
 *
 */
public class Wound extends Sprite 
{
	int currentDuration;
	int maxDuration;
	
	public Wound(Animation woundAnimation) 
	{
		super(woundAnimation);
		currentDuration = 0;
		maxDuration = (int)woundAnimation.getDuration();
	}
	
	/**
	 * This is fed the elapsedTime since the last tick and updates
	 * the sprite's coordinants and animation accordingly
	 * 
	 * it also sets the sprites dx&dy to that of the wound victim's
	 * 
	 * once the animation has had a full play through, a -1 is returned to signal 
	 * the parent to set the object to null. Otherwise a 0 is returned.
	 */
	public int update(int elapsedTime,float dx, float dy)
	{
		int status = 0;
		anim.update(elapsedTime);
		super.update(elapsedTime);
		setVelocityX(dx);
		setVelocityY(dy);
			
		if(currentDuration == maxDuration) status = -1;
		else currentDuration+= elapsedTime;
		return status;
	}
	
	public void render(Graphics2D g, int offsetX, int offsetY)
	{
		g.drawImage(getImage(),(int)getX()+offsetX,(int)getY()+offsetY,null);
	}
	

}
