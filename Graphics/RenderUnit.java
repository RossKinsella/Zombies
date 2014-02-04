package Graphics;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.geom.Line2D.Double;
import java.awt.image.BufferedImage;
import java.io.IOException;

import Engine.Engine;
import Entites.Sprite;
import Entites.ZombieHandler;
import Map.Field;

public class RenderUnit 
{
	BufferedImageLoader loader;
	SpriteSheet playerSS,zombieSS,bulletWoundSS;
	ImageStorage storage;
	private BufferedImage base,target,playerFace,deadZombie, nineMillMagazineSS, deadPlayer;
	private double targetAngle;
	
	float mod;
	
	private Engine engine;
	
	Field theMap;
	
	private Animation pWalking, pShootingPistol, pLegs;
	private Animation nineMillMagazine, health;
	private Animation zWalking, zAttacking;
	private Animation bulletWound;
	private Animation[] zBleeding;
	
	// temp?
	ZombieHandler zh;
	
	//TEMP
	BufferedImage searchBox;
	boolean hasBox,hasLine;
	Point pp, tp;
	
	
	public RenderUnit(Engine engine)
	{
		///
		zBleeding = new Animation[100];
		
		///
		mod = 0;
		this.engine = engine;
		loader = new BufferedImageLoader();
		
		
		
		theMap = new Field(engine.getWidth(), engine.getHeight(), loader);
		
		
		base = new BufferedImage(engine.getWidth(),engine.getHeight(),BufferedImage.TYPE_INT_RGB);
		loadPlayer();
		loadZombie();
		
		targetAngle = 0;
		
		try {
			target = loader.loadImage("/27x27 target.png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public BufferedImage getBase()
	{
		return base;
	}
	
	public void render(Graphics2D g, long elapsedTime, double mouseX,double mouseY)
	{
		g.drawImage(base,0,0,null);
		
		int offsetX = engine.getWidth()/ 2 -
			    Math.round(engine.getPlayer().getX()+30);
		offsetX = Math.min(offsetX, 0);
		offsetX = Math.max(offsetX, engine.getWidth() - theMap.getMap().getWidth());
		
		int offsetY = engine.getHeight()/ 2 -
			    Math.round(engine.getPlayer().getY()+30);
		offsetY = Math.min(offsetY, 0);
		offsetY = Math.max(offsetY, engine.getHeight() - theMap.getMap().getHeight());

		
		g.drawImage(theMap.getMap(), 0+offsetX,0+offsetY,null);
				
		zh.renderDead(g,offsetX,offsetY);	
		
		engine.getPlayer().render(g,offsetX,offsetY,(int)mouseX,(int)mouseY);
			
		zh.renderAlive(g,offsetX,offsetY);
		
		if(engine.getPlayer().isAlive())
		{
			g.rotate(targetAngle+=.005,(int)mouseX,(int)mouseY);
			g.drawImage(target, (int)mouseX-13,(int)mouseY-13,null);
			g.rotate(-targetAngle,(int)mouseX,(int)mouseY);
		}
		
		g.drawImage(playerFace,50,580,null);
		
		
		if(!engine.getPlayer().isAlive())
		{
			

		}
	}	
	
	
	
	private void loadPlayer()
	{
		playerFace = new BufferedImage(80,80,BufferedImage.TYPE_INT_RGB);
		nineMillMagazineSS = new BufferedImage(800,800,BufferedImage.TYPE_INT_RGB);
		BufferedImage healthSheet = new BufferedImage(800,800,BufferedImage.TYPE_INT_RGB);
		BufferedImage playerSheet = new BufferedImage(64*8,64*8,BufferedImage.TYPE_INT_RGB);
		try 
		{
			healthSheet = loader.loadImage("/health B.png");
			playerSheet = loader.loadImage("/64x64 human new.png");
			playerFace = loader.loadImage("/PlayerFace.png");
			nineMillMagazineSS = loader.loadImage("/80x80 9mm magazine.png");
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		playerSS = new SpriteSheet(playerSheet);
		pWalking = new Animation();
		pWalking.addFrame(playerSS.grabImage(1,1,64,64), 20);
		pWalking.addFrame(playerSS.grabImage(2,1,64,64), 15);
		pWalking.addFrame(playerSS.grabImage(3,1,64,64), 15);
		pWalking.addFrame(playerSS.grabImage(4,1,64,64), 15);
		pWalking.addFrame(playerSS.grabImage(5,1,64,64), 25);
		pWalking.addFrame(playerSS.grabImage(6,1,64,64), 15);
		pWalking.addFrame(playerSS.grabImage(7,1,64,64), 15);
		pWalking.addFrame(playerSS.grabImage(8,1,64,64), 15);
		
		pShootingPistol = new Animation();
		pShootingPistol.addFrame(playerSS.grabImage(1, 2, 64, 64), 2);
		pShootingPistol.addFrame(playerSS.grabImage(2, 2, 64, 64), 2);
		pShootingPistol.addFrame(playerSS.grabImage(3, 2, 64, 64), 2);
		pShootingPistol.addFrame(playerSS.grabImage(4, 2, 64, 64), 2);
		pShootingPistol.addFrame(playerSS.grabImage(5, 2, 64, 64), 4);	
		
		pLegs = new Animation();
		pLegs.addFrame(playerSS.grabImage(1, 3, 64, 64), 6);
		pLegs.addFrame(playerSS.grabImage(2, 3, 64, 64), 6);
		pLegs.addFrame(playerSS.grabImage(3, 3, 64, 64), 12);
		pLegs.addFrame(playerSS.grabImage(2, 3, 64, 64), 6);
		pLegs.addFrame(playerSS.grabImage(1, 3, 64, 64), 6);

		
		pLegs.addFrame(playerSS.grabImage(1, 4, 64, 64), 6);
		pLegs.addFrame(playerSS.grabImage(2, 4, 64, 64), 6);
		pLegs.addFrame(playerSS.grabImage(3, 4, 64, 64), 12);
		pLegs.addFrame(playerSS.grabImage(2, 4, 64, 64), 6);
		pLegs.addFrame(playerSS.grabImage(1, 4, 64, 64), 6);
		
		nineMillMagazine = new Animation();
		SpriteSheet nineMill = new SpriteSheet(nineMillMagazineSS);
		nineMillMagazine.addFrame(nineMill.grabImage(1,1,80,80),(long).9);
		nineMillMagazine.addFrame(nineMill.grabImage(2,1,80,80),1);
		nineMillMagazine.addFrame(nineMill.grabImage(3,1,80,80),1);
		nineMillMagazine.addFrame(nineMill.grabImage(4,1,80,80),1);
		nineMillMagazine.addFrame(nineMill.grabImage(5,1,80,80),1);
		nineMillMagazine.addFrame(nineMill.grabImage(6,1,80,80),1);
		nineMillMagazine.addFrame(nineMill.grabImage(7,1,80,80),1);
		nineMillMagazine.addFrame(nineMill.grabImage(8,1,80,80),1);
		nineMillMagazine.addFrame(nineMill.grabImage(9,1,80,80),1);
		nineMillMagazine.addFrame(nineMill.grabImage(10,1,80,80),1);
		nineMillMagazine.addFrame(nineMill.grabImage(1,2,80,80),1);
		nineMillMagazine.addFrame(nineMill.grabImage(2,2,80,80),1);
		nineMillMagazine.addFrame(nineMill.grabImage(3,2,80,80),2);	
		
		health = new Animation();
		SpriteSheet healthSS = new SpriteSheet(healthSheet);
		health.addFrame(healthSS.grabImage(1,1,80,80), (long).9);
		health.addFrame(healthSS.grabImage(2,1,80,80), 1);
		health.addFrame(healthSS.grabImage(3,1,80,80), 1);
		health.addFrame(healthSS.grabImage(4,1,80,80), 1);
		health.addFrame(healthSS.grabImage(5,1,80,80), 1);
		health.addFrame(healthSS.grabImage(6,1,80,80), 1);
		health.addFrame(healthSS.grabImage(7,1,80,80), 1);
		health.addFrame(healthSS.grabImage(8,1,80,80), 1);
		
		health.addFrame(healthSS.grabImage(1,2,80,80), 1);
		health.addFrame(healthSS.grabImage(2,2,80,80), 1);
		health.addFrame(healthSS.grabImage(3,2,80,80), 1);
		health.addFrame(healthSS.grabImage(4,2,80,80), 1);
		health.addFrame(healthSS.grabImage(5,2,80,80), 1);
		health.addFrame(healthSS.grabImage(6,2,80,80), 1);
		health.addFrame(healthSS.grabImage(7,2,80,80), 1);
		health.addFrame(healthSS.grabImage(8,2,80,80), 1);
		
		health.addFrame(healthSS.grabImage(1,3,80,80), 1);
		health.addFrame(healthSS.grabImage(2,3,80,80), 1);
		health.addFrame(healthSS.grabImage(3,3,80,80), 1);
		health.addFrame(healthSS.grabImage(4,3,80,80), 1);
		health.addFrame(healthSS.grabImage(5,3,80,80), 1);
		health.addFrame(healthSS.grabImage(6,3,80,80), 1);
		health.addFrame(healthSS.grabImage(7,3,80,80), 1);
		health.addFrame(healthSS.grabImage(8,3,80,80), 1);
	
		health.addFrame(healthSS.grabImage(1,4,80,80), 1);
		health.addFrame(healthSS.grabImage(2,4,80,80), 1);
		health.addFrame(healthSS.grabImage(3,4,80,80), 1);
		health.addFrame(healthSS.grabImage(4,4,80,80), 1);
		health.addFrame(healthSS.grabImage(5,4,80,80), 1);
		health.addFrame(healthSS.grabImage(6,4,80,80), 1);
		health.addFrame(healthSS.grabImage(7,4,80,80), 1);
		health.addFrame(healthSS.grabImage(8,4,80,80), 1);
		
		health.addFrame(healthSS.grabImage(1,5,80,80), 1);
		health.addFrame(healthSS.grabImage(2,5,80,80), 1);
		health.addFrame(healthSS.grabImage(3,5,80,80), 1);
		health.addFrame(healthSS.grabImage(4,5,80,80), 1);
		health.addFrame(healthSS.grabImage(5,5,80,80), 1);
		health.addFrame(healthSS.grabImage(6,5,80,80), 1);
		
		deadPlayer = playerSS.grabImage(5,4,64,64);
	}
	
	private void loadZombie()
	{
		BufferedImage zombieSheet = new BufferedImage(64*8,64*8,BufferedImage.TYPE_INT_RGB);
		BufferedImage bulletWounds = new BufferedImage(20*8,20*8,BufferedImage.TYPE_INT_RGB);
		BufferedImage zBleedingSheet = new BufferedImage(64*8,64*8,BufferedImage.TYPE_INT_RGB);
		deadZombie = new BufferedImage(64*2,64*2,BufferedImage.TYPE_INT_RGB);
		try {
			zombieSheet = loader.loadImage("/64x64 zombie.png");
			bulletWounds = loader.loadImage("/20x20 wound.png");
			zBleedingSheet = loader.loadImage("/64x64 circle bleeding with inner dark.png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		zombieSS = new SpriteSheet(zombieSheet);
		
		zWalking = new Animation();
		zWalking.addFrame(zombieSS.grabImage(1,1,64,64), 30);
		zWalking.addFrame(zombieSS.grabImage(2,1,64,64), 20);
		zWalking.addFrame(zombieSS.grabImage(3,1,64,64), 30);
		zWalking.addFrame(zombieSS.grabImage(4,1,64,64), 20);
		zWalking.addFrame(zombieSS.grabImage(5,1,64,64), 30);
		zWalking.addFrame(zombieSS.grabImage(6,1,64,64), 20);
		zWalking.addFrame(zombieSS.grabImage(7,1,64,64), 30);
		zWalking.addFrame(zombieSS.grabImage(8,1,64,64), 20);
		//pSS.grabImage(1,1,32,32);
		
		bulletWoundSS = new SpriteSheet(bulletWounds);
		bulletWound = new Animation();
		bulletWound.addFrame(bulletWoundSS.grabImage
				(1,1,20,20), 5);
		bulletWound.addFrame(bulletWoundSS.grabImage
				(2,1,20,20), 5);
		bulletWound.addFrame(bulletWoundSS.grabImage
				(3,1,20,20), 5);
		bulletWound.addFrame(bulletWoundSS.grabImage
				(4,1,20,20), 3);
		bulletWound.addFrame(bulletWoundSS.grabImage
				(5,1,20,20), 3);
		
		
		SpriteSheet zBleedingSS = new SpriteSheet(zBleedingSheet);
		for(int i = 0 ; i < zBleeding.length ; i++)
		{
			zBleeding[i] = new Animation();
			zBleeding[i].addFrame(zBleedingSS.grabImage(1, 1, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(2, 1, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(3, 1, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(4, 1, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(5, 1, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(6, 1, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(7, 1, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(8, 1, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(1, 2, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(2, 2, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(3, 2, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(4, 2, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(5, 2, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(6, 2, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(7, 2, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(8, 2, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(1, 3, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(2, 3, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(3, 3, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(4, 3, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(5, 3, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(6, 3, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(7, 3, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(8, 3, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(1, 4, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(2, 4, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(3, 4, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(4, 4, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(5, 4, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(6, 4, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(7, 4, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(8, 4, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(1, 5, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(2, 5, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(3, 5, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(4, 5, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(5, 5, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(6, 5, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(7, 5, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(8, 5, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(1, 6, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(2, 6, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(3, 6, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(4, 6, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(5, 6, 64, 64), 5);
			zBleeding[i].addFrame(zBleedingSS.grabImage(6, 6, 64, 64), 5);	
		}
		
		deadZombie = zombieSS.grabImage(192, 64, 64, 64*2,0);
	}
	
	public Animation getBulletWound()
	{
		return bulletWound;
	}
	
	public Animation getPlayerUpperBody()
	{
		return pWalking;
	}
	
	public Animation getPlayerLowerBody()
	{
		return pLegs;
	}
	
	public Animation getPistolShot()
	{
		return pShootingPistol;
	}
	
	public Animation getZombieWalking()
	{
		return zWalking;
	}
	
	public Animation getZombieAttacking()
	{
		return zAttacking;
	}
	
	public Animation getNineMillMag()
	{
		return nineMillMagazine;
	}
	
	public Animation getHealth()
	{
		return health;
	}
	
	public Animation getZombieBleeding(int index)
	{
		return zBleeding[index];
	}
	
	public void importZombieHandler(ZombieHandler zh)
	{
		this.zh = zh;
	}
	
    public void getColor(int mouseX,int mouseY) {
        try {
            Robot robot = new Robot();
 
            //
            // The the pixel color information at 20, 20
            //
            Color color = robot.getPixelColor(mouseX, mouseY);
            Rectangle r = new Rectangle(100, 100, 100, 100);
            BufferedImage bi = robot.createScreenCapture(r);
            System.out.println(bi.getRGB(5, 5));
            
            //
            // Print the RGB information of the pixel color
            //
            System.out.println("Red   = " + color.getRed());
            System.out.println("Green = " + color.getGreen());
            System.out.println("Blue  = " + color.getBlue());
 
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
    
    public void setSearchBox(BufferedImage searchBox)
    {
    	hasBox = true;
    	this.searchBox = searchBox;
    }

    public void updateLine(Point pp,Point tp)
    {
    	hasLine = true;
    	this.pp = pp;
    	this.tp = tp;
    }
    
    public BufferedImage getDeadZombieImage()
    {
    	return deadZombie;
    }

	public BufferedImage getPistolBullet() {
		BufferedImage pistolBullet;
		try {
			pistolBullet = loader.loadImage("/pistolBullet.png");
			return pistolBullet;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public BufferedImage getMap()
	{
		return theMap.getMap();
	}
	
	public BufferedImage getDeadPlayer()
	{
		return deadPlayer;
	}
	
}
