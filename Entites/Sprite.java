package Entites;

import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import Engine.CollisionDetector;
import Engine.Engine;
import Graphics.Animation;

public class Sprite {

    protected Animation anim;
    protected Animation upperBody,lowerBody;
    // position (pixels)
    protected float x;
    protected float y;
    
    // velocity (pixels per millisecond)
    private float dx;
    private float dy;

    protected boolean isPlayer,isZombie;
    
    /**
        Creates a new Sprite object with the specified Animation.
    */
    public Sprite(Animation anim) {
        this.anim = anim;
        anim.start();
    }
    
    /**
    Alt constructor for creature with separate walking/body anim
     */
    public Sprite(Animation upperBody, Animation lowerBody) {
    this.upperBody = upperBody;
    this.lowerBody = lowerBody;
    }

    /**
        Updates this Sprite's Animation and its position based
        on the velocity.
    */
    public void update(long elapsedTime, CollisionDetector cd) 
    {
        if(isPlayer)
        {
	    	if(!cd.playerCollides(new Rectangle2D.Double(getX()+(dx*elapsedTime),getY(),64,64)))
	        {
	        	x += dx*elapsedTime;
	        }	
	        
	        if(!cd.playerCollides(new Rectangle2D.Double(getX(),getY()+(dy*elapsedTime),64,64)))
	        {
	        	y += dy* elapsedTime;
	        }
        }	
        
        else
        {
	    	if(!cd.zombieCollides(new Rectangle2D.Double(getX()+32+(dx*elapsedTime),getY(),32,32),this))
	        {
	        	x += dx*elapsedTime;
	        }	
	        
	        if(!cd.zombieCollides(new Rectangle2D.Double(getX(),getY()+32+(dy*elapsedTime),32,32),this))
	        {
	        	y += dy* elapsedTime;
	        }	
        	
        }
    	
    	
    	//System.out.println(elapsedTime);
    	//if(validMoveX(theMap))
    	//	x += dx * elapsedTime;
       // if(validMoveY(theMap))
        //	y += dy * elapsedTime;
        //anim.update(elapsedTime);
    }
    
    public void update(long elapsedTime) 
    {
        //System.out.println(elapsedTime);
    
    		x += dx * elapsedTime;
       
        	y += dy * elapsedTime;
        //anim.update(elapsedTime);
    }
    
    private boolean validMoveX(BufferedImage theMap)
    {
    	boolean result = true;
    	if(dx<0)
    	{
    		if(theMap.getRGB((int)(x+dx), (int)getCenterY()) == -16777216	
    			&&  theMap.getRGB((int)(x+dx+dx), (int)getCenterY()) == -16777216
    			&& theMap.getRGB((int)(x+dx+dx+dx), (int)getCenterY()) == -16777216)
    			result = false;
    		
        	if(theMap.getRGB((int)(x+32+dx), (int)getCenterY()) == -16777216	
            			&&  theMap.getRGB((int)(x+32+dx+dx), (int)getCenterY()) == -16777216
            			&& theMap.getRGB((int)(x+32+dx+dx+dx), (int)getCenterY()) == -16777216)
        			result = false;
    		
    				
    	}
    	else
    	{
    		if(theMap.getRGB((int)(x+64+dx), (int)y) == -16777216	
        			&&  theMap.getRGB((int)(x+64+dx+dx), (int)y) == -16777216
        			&& theMap.getRGB((int)(x+64+dx+dx+dx), (int)y) == -16777216)
        			result = false;
    	}
    		return result;
    }
    
    private boolean validMoveY(BufferedImage theMap)
    {
    	boolean result = true;
    	if(dy < 0)
    	{
    	if(theMap.getRGB((int)getCenterX(),(int)(y+dy)) == -16777216
    			&& theMap.getRGB((int)getCenterX(),(int)(y+dy+dy)) == -16777216
    			&& theMap.getRGB((int)getCenterX(),(int)(y+dy+dy)) == -16777216)
    			result = false;
    	}
    	else
    	{
        	if(theMap.getRGB((int)getCenterX(),(int)(y+64+dy)) == -16777216
        			&& theMap.getRGB((int)getCenterX(),(int)(y+64+dy+dy)) == -16777216
        			&& theMap.getRGB((int)getCenterX(),(int)(y+64+dy+dy)) == -16777216)
        			result = false;
    	}
    	return result;
    }

    /**
        Gets this Sprite's current x position.
    */
    public float getX() {
        return x;
    }
    
    /**
    Gets the Sprite's center x position.
     */
    public float getCenterX()
    {
    	float result;
    	if(anim == null) result = getX() + (getUpperBodyImage().getWidth(null)/2);
    	else  result = getX() + (getImage().getWidth(null)/2);
    	return result;
    }
    
    /**
    Gets the Sprite's center y position.
     */
    public float getCenterY()
    {
    	float result;
    	if(anim == null) result = getY() + (getUpperBodyImage().getHeight(null)/2);
    	else result = getY() + (getImage().getHeight(null)/2);
    	return result;
    }

    /**
        Gets this Sprite's current y position.
    */
    public float getY() {
        return y;
    }

    /**
        Sets this Sprite's current x position.
    */
    public void setX(float x) {
        this.x = x;
    }

    /**
        Sets this Sprite's current y position.
    */
    public void setY(float y) {
        this.y = y;
    }
    

    /**
        Gets this Sprite's width, based on the size of the
        current image.
    */
    public int getWidth() {
        return anim.getImage().getWidth(null);
    }

    /**
        Gets this Sprite's height, based on the size of the
        current image.
    */
    public int getHeight() {
        return anim.getImage().getHeight(null);
    }

    /**
        Gets the horizontal velocity of this Sprite in pixels
        per millisecond.
    */
    public float getVelocityX() {
        return dx;
    }

    /**
        Gets the vertical velocity of this Sprite in pixels
        per millisecond.
    */
    public float getVelocityY() {
        return dy;
    }

    /**
        Sets the horizontal velocity of this Sprite in pixels
        per millisecond.
    */
    public void setVelocityX(float dx) {
        this.dx = dx;
    }

    /**
        Sets the vertical velocity of this Sprite in pixels
        per millisecond.
    */
    public void setVelocityY(float dy) {
        this.dy = dy;
    }

    /**
        Gets this Sprite's current image if it has a single animation.
    */
    public Image getImage() {
        return anim.getImage();
    }
    
    /**
    Gets the upperBody animation
     */
	public Image getUpperBodyImage() {
	    return upperBody.getImage();
	}
	
    /**
    Gets the lowerBody animation
     */
	public Image getLowerBodyImage() {
	    return lowerBody.getImage();
	}
    
    
    public Animation getAni()
    {
    	return anim;
    }
}


