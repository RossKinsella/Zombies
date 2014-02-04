package Entites;

import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;

import Engine.CollisionDetector;
import Graphics.Animation;

/**
A Creature is a Sprite that is affected by gravity and can
die. It has four Animations: moving left, moving right,
dying on the left, and dying on the right.
*/
public abstract class Creature extends Sprite {

/**
    Amount of time to go from STATE_DYING to STATE_DEAD.
*/
private static final int DIE_TIME = 1000;

public static final int STATE_NORMAL = 0;
public static final int STATE_DYING = 1;
public static final int STATE_DEAD = 2;

private Animation uppperBody, attacking;
private int state, rotationMod;
private long stateTime;

// new
//protected boolean isZombie;

private boolean isAttacking;
private double currentAttackDuration;


/**
    Creates a new Creature with the specified Animations.
*/

public Creature(Animation upperBody, Animation attacking, Animation lowerBody)
{
	super(upperBody,lowerBody);
	this.attacking = attacking;
	this.uppperBody = upperBody;
	isAttacking = false;
	state = STATE_NORMAL;
}

public Creature(Animation upperBody, Animation attacking)
{
	super(upperBody);
	this.attacking = attacking;
	isAttacking = false;
	state = STATE_NORMAL;
}

public Object clone() {
    // use reflection to create the correct subclass
    Constructor constructor = getClass().getConstructors()[0];
    try {
        return constructor.newInstance(new Object[] {
            (Animation)upperBody.clone(),
            (Animation)attacking.clone(),
        });
    }
    catch (Exception ex) {
        // should never happen
        ex.printStackTrace();
        return null;
    }
}


/**
    Gets the maximum speed of this Creature.
*/
public float getMaxSpeed() {
    return 0;
}


/**
    Wakes up the creature when the Creature first appears
    on screen. Normally, the creature starts moving left.
*/
public void wakeUp() {
    if (getState() == STATE_NORMAL && getVelocityX() == 0) {
        setVelocityX(-getMaxSpeed());
    }
}


/**
    Gets the state of this Creature. The state is either
    STATE_NORMAL, STATE_DYING, or STATE_DEAD.
*/
public int getState() {
    return state;
}


/**
    Sets the state of this Creature to STATE_NORMAL,
    STATE_DYING, or STATE_DEAD.
*/
public void setState(int state) {
    if (this.state != state) {
        this.state = state;
        stateTime = 0;
        if (state == STATE_DYING) {
            setVelocityX(0);
            setVelocityY(0);
        }
    }
}


/**
    Checks if this creature is alive.
*/
public boolean isAlive() {
    return (state == STATE_NORMAL);
}


/**
    Checks if this creature is flying.
*/
public boolean isFlying() {
    return false;
}


/**
    Called before update() if the creature collided with a
    tile horizontally.
*/
public void collideHorizontal() {
    setVelocityX(-getVelocityX());
}


/**
    Called before update() if the creature collided with a
    tile vertically.
*/
public void collideVertical() {
    setVelocityY(0);
}

/**
Sets the animation back to still position.
*/
public void resetAnimation()
{
	if(anim != null)anim.start();
	else 
	{
		upperBody.start();
		lowerBody.start();
	}
}


/**
    Updates the animation for this creature.
*/
public void updateAnimation(long elapsedTime, CollisionDetector cd, int zero) 
{

	super.update(elapsedTime);
	// select the correct Animation
    Animation newAnim = upperBody;
    if(isAttacking == true && currentAttackDuration <= attacking.getDuration())
    {
    	newAnim = attacking;
    	currentAttackDuration += elapsedTime;
    }
    else
    {
    	isAttacking = false;
    	newAnim = uppperBody;
    }

    // update the Animation
    if (upperBody != newAnim) {
        upperBody = newAnim;
        upperBody.start();
    }
    else if(isAttacking == false && isZombie == false)
    {
        if(getVelocityX() != 0 || getVelocityY() != 0)
        	upperBody.update(elapsedTime);
    }
    else
    	upperBody.update(elapsedTime);
    // update to "dead" state
    stateTime += elapsedTime;
    if (state == STATE_DYING && stateTime >= DIE_TIME) {
        setState(STATE_DEAD);
    }
    if(getVelocityX() != 0 || getVelocityY() != 0)
    {
    	lowerBody.update(elapsedTime);
    }
    else lowerBody.start();
    
}

public void updateAnimation(long elapsedTime, CollisionDetector cd) 
{

	super.update(elapsedTime, cd);
	// select the correct Animation
    Animation newAnim = upperBody;
    if(isAttacking == true && currentAttackDuration <= attacking.getDuration())
    {
    	newAnim = attacking;
    	currentAttackDuration += elapsedTime;
    }
    else
    {
    	isAttacking = false;
    	newAnim = uppperBody;
    }

    // update the Animation
    if (upperBody != newAnim) {
        upperBody = newAnim;
        upperBody.start();
    }
    else if(isAttacking == false && isZombie == false)
    {
        if(getVelocityX() != 0 || getVelocityY() != 0)
        	upperBody.update(elapsedTime);
    }
    else
    	upperBody.update(elapsedTime);
    // update to "dead" state
    stateTime += elapsedTime;
    if (state == STATE_DYING && stateTime >= DIE_TIME) {
        setState(STATE_DEAD);
    }
    if(getVelocityX() != 0 || getVelocityY() != 0)
    {
    	lowerBody.update(elapsedTime);
    }
    else lowerBody.start();
    
}


public void attack()
{
	
}


public void setIsAttacking()
{
	isAttacking = true;
	currentAttackDuration = 0;
}

public int getRotationMod()
{
	return rotationMod;
}

public void setZombie()
{
	isZombie = true;
}

}
