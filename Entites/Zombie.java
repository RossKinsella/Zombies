package Entites;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import Engine.CollisionDetector;
import Engine.Engine;
import Graphics.Animation;
import Graphics.RenderUnit;

public class Zombie extends Creature
{
	Engine theGame;
	Player p;
	Random r;
	private Wound[] wounds;
	private int health, playerCenterXonDeath, playerCenterYonDeath, bleedAnimationCurrentTime;
	private boolean alive;
	private Animation woundAnimation, bleedOutOnDeath;
	BufferedImage deadImage;
	double deathAngle;
	double maxMoveX, xMoved;
	private boolean stuck, fast;
	private int stuckAdjustment,adjustmentDirection;
	private float speed;
	private int ticksSinceLastAttack;
	
	
	public Zombie(Animation walking, Animation attacking,Animation woundAnimation,
			Animation bleedOutOnDeath, BufferedImage dead, Player p, RenderUnit ru)
	{
		super (walking,attacking);
		this.p = p;
		setZombie();
		this.woundAnimation = new Animation();
		this.woundAnimation = woundAnimation.clone();
		this.bleedOutOnDeath = new Animation();
		this.bleedOutOnDeath = bleedOutOnDeath;
		

		alive = true;
		wounds = new Wound[1000];
		health = 30;
		maxMoveX = 20;
		xMoved = 0;
		bleedAnimationCurrentTime = 0;
		
		deadImage = new BufferedImage(64,128,BufferedImage.TYPE_INT_RGB);
		deadImage = dead;
		r = new Random();
		ticksSinceLastAttack = 0;
		
		int chance = r.nextInt(2);
		if(chance == 0)
		{
		setX(r.nextInt(300)+400);
		setY(-r.nextInt(200)-100);
		}
		else
		{
			setX(r.nextInt(300)+400);
			setY(r.nextInt(200)+1276);
		}
		isZombie = true;
		
		if(r.nextInt(8) == 5) 
			{
			System.out.println("fast one");
			fast = true;
			}
		
		speed = r.nextFloat() + r.nextFloat();
		if(fast) speed += r.nextFloat() + r.nextFloat();
		
		adjustmentDirection = -4;
		//this.theGame = theGame;
	}
	
	public void update(long elapsedTime, CollisionDetector cd)
	{
		if(alive)
		{
			this.updateAnimation(elapsedTime, cd);
			updatePathfinding();
		}
		else if(xMoved < maxMoveX) xMoved+=2;
	}
	
	
	public void updateAnimation(long elapsedTime, CollisionDetector cd)
	{
		anim.update(elapsedTime);
    	super.update(elapsedTime,cd);
    	
    	
    	for(int i = 0; i<wounds.length;i++)
    	{
    		if(wounds[i] != null)
    		{
    			if(wounds[i].update((int)elapsedTime,getVelocityX(),getVelocityY()) == -1)
    				wounds[i] = null;
    		}
    	}
	}
	
	public void updatePathfinding()
	{
		if(!isCloseToPlayer(p))
		{
			
			if(getX() < p.getX() - 30 ) setVelocityX(speed);
			else if(getX() > p.getX() + 30 ) setVelocityX(-speed);
			else setVelocityX(0);
			
			if(getY() < p.getY() - 30 ) setVelocityY(speed);
			else if(getY() > p.getY() +30 ) setVelocityY(-speed);
			else setVelocityY(0);
		
		
			if(stuck)
			{

				
				if(adjustmentDirection == 1) setVelocityX(speed);
				if(adjustmentDirection == 2) setVelocityX(-speed);
				if(adjustmentDirection == -1) setVelocityY(speed);
				if(adjustmentDirection == -2) setVelocityY(-speed);
				if(adjustmentDirection == 0) 
				{
					if(getVelocityX() != 0 && getY() < p.getY() - 30) y+=speed ;
					else if (getVelocityX() != 0 && getY() > p.getY() + 30) y-=speed ;
					else if(getVelocityY() != 0 && getX() < p.getX() - 30) x+=speed ;
					else if (getVelocityY() != 0 && getX() > p.getX() + 30) x-=speed ;
				}
				
				stuckAdjustment--;
				if(stuckAdjustment < 0) stuck = false;
			}
		}
		else
		{
			if(ticksSinceLastAttack >= 120)
				{
				System.out.println("player hit");
				p.takeHit(4);
				ticksSinceLastAttack = 0;
				}
		}
		if(ticksSinceLastAttack < 120) ticksSinceLastAttack++;
	}
	
	public void render(Graphics2D g, int offsetX, int offsetY)
	{
		int centerZX = (int)getCenterX();
		int centerZY = (int)getCenterY();
		if(alive)
		{

			double angleZ = Math.atan2(centerZY - (int)p.getCenterY()
					, centerZX - (int)p.getCenterX()) - Math.PI / 2;
			g.rotate(angleZ, centerZX+offsetX, centerZY+offsetY);
			g.drawImage(getImage(),(int)getX()+offsetX,(int)getY()+offsetY,null);

			g.rotate(-angleZ,centerZX+offsetX,centerZY+offsetY);
			deathAngle = angleZ;
	
			for(int i = 0 ; i< wounds.length;i++)
			{
				if(wounds[i] != null)
					wounds[i].render(g,offsetX,offsetY);
			}
		}
		else if(!alive)
		{
			
			double angleZ = Math.atan2(centerZY - playerCenterYonDeath
					, centerZX - playerCenterXonDeath) - Math.PI / 2;
			
			angleZ += 3.14; // adjustment to rotate 180 degrees
		//	System.out.println(((angleZ/Math.PI)*180)-90 + " = " + angleZ);
			g.rotate(angleZ, centerZX+offsetX, centerZY+offsetY);
			if( bleedAnimationCurrentTime < bleedOutOnDeath.getDuration()-1)
			{
				bleedOutOnDeath.update(1);
				bleedAnimationCurrentTime++;
			}
			g.drawImage(bleedOutOnDeath.getImage(),(int)getX()+offsetX, (int)getY()-64-(int)xMoved+offsetY,null);
    		
			g.drawImage(deadImage, (int)getX()+offsetX, (int)getY()-64-(int)xMoved+offsetY,null);
			g.rotate(-angleZ, centerZX+offsetX, centerZY+offsetY);
		}
		
	}
	
	private boolean isCloseToPlayer(Player p)
	{
		boolean result = false;
		if(Math.abs(p.getCenterX() - getCenterX()) < 32 &&
				Math.abs(p.getCenterY() - getCenterY()) < 32)
		{
			result = true;
			setVelocityX(0);
			setVelocityY(0);
		}
		
		
		return result;
	}
	
	private int getDeltaX(Player p)
	{
		int dx = (int) (p.getX() - getX());
		if(dx < 0) dx = -dx;
		return dx;
	}
	
	private int getDeltaY(Player p)
	{
		int dy = (int) (p.getY() - getY());
		if(dy < 0) dy = -dy;
		return dy;
	}

	
	/**
	* this method is fed an image of the screen 
	* and a list of points for each pixel from the gun barrel to the center of the target icon
	* 
	* it works its way back(see 1) from the target icon and looks for a black pixel which is within 
	* the 64x64 image of the zombie.
	* 
	* the first pixel found has a wound sprite created on it, which has its dx & dy set to that of the zombie.
	* 
	* if a wound sprite is created, break() is called to save resources and prevent multiple sprites being made
	* 
	* (1) it works backward to that an animation is made to represent the exit wound, not the entry wound
	*/
	/*public void takeHit(BufferedImage screenImage, List<Point2D> bulletPath, int offsetX) 
	{
		for(int j = bulletPath.size()-1; j > 0;j--)
		{
			Point2D currentPoint = bulletPath.get(j);
			int currentX = (int) (currentPoint.getX());
			int currentY = (int) (currentPoint.getY());
			if(currentX > getX() && currentX < getX() + getImage().getWidth(null)
					&& currentY > getY() && currentY < getY() + getImage().getHeight(null))
			{
				if(screenImage.getRGB((int)currentPoint.getX(),(int)currentPoint.getY()) == -16777216)
				{
					System.out.println("hit");
					int vacantWoundIndex = findVacantWoundIndex();
					wounds[vacantWoundIndex] = new Wound(woundAnimation);
					wounds[vacantWoundIndex].setX((float) bulletPath.get(j).getX()-13+offsetX);
					wounds[vacantWoundIndex].setY((float) bulletPath.get(j).getY()-13);
					
					
					if(getVelocityX() < 0)
						wounds[vacantWoundIndex].setVelocityX((float) (getVelocityX() + .5));
					else wounds[vacantWoundIndex].setVelocityX(getVelocityX() );


					if(getVelocityY() < 0)
						wounds[vacantWoundIndex].setVelocityY((float) (getVelocityY() + .5));
					else wounds[vacantWoundIndex].setVelocityY(getVelocityY() );
					
					adjustHealth();
					break;
				}
			}
		}	
	}*/
	
	/**
	 * looks through the wounds array and returns an index of a null space.
	 * 
	 * if there is no empty space, -1 is returned.
	 */
	private int findVacantWoundIndex()
	{
		int result = -1;
		for(int i = 0;i<wounds.length;i++)
		{
			if(wounds[i]==null) 
			{
				result = i;
				return result;
			}
		}
		return result;
	}
	
	private void adjustHealth()
	{
		health -= 10;
		if(health <= 0) 
		{
			alive = false;
			playerCenterXonDeath = (int) p.getCenterX();
			playerCenterYonDeath = (int) p.getCenterY();
			
		}
	}
	
	public boolean isAlive()
	{
		return alive;
	}

	
	/**
	* this method is fed a point which represents the exitWound of a bullet and the offset
	* the point which is fed has an offset set within it which is canceled by subtracting the offset
	* before setting the coordinates of the wound
	* 
	*  this prevents wounds spazzing out when the camera is moving
	*/
	public void takeHit(Point2D currentPoint, int offsetX, int offsetY) 
	{
		int vacantWoundIndex = findVacantWoundIndex();
		wounds[vacantWoundIndex] = new Wound(woundAnimation);
		wounds[vacantWoundIndex].setX((float) currentPoint.getX()-13-offsetX);
		wounds[vacantWoundIndex].setY((float) currentPoint.getY()-13-offsetY);
		
		
		if(getVelocityX() < 0)
			wounds[vacantWoundIndex].setVelocityX((float) (getVelocityX() + .5));
		else wounds[vacantWoundIndex].setVelocityX(getVelocityX() );


		if(getVelocityY() < 0)
			wounds[vacantWoundIndex].setVelocityY((float) (getVelocityY() + .5));
		else wounds[vacantWoundIndex].setVelocityY(getVelocityY() );
		
		adjustHealth();
	}
	
	public void setStuck()
	{
		stuck = true;
		stuckAdjustment = 40;
		if(getVelocityX() != 0) adjustmentDirection = -r.nextInt(3);
		else adjustmentDirection = r.nextInt(3);
	}
	
	public void unsetStuck()
	{
		stuck = false;
	}
	
	public boolean isStuck()
	{
		return stuck;
	}
	
}
