import java.awt.Graphics;
import java.awt.Rectangle;

public class Plug
{
	protected int id;					// plug identification
	private int lastID;
	public boolean watered = false;
	protected int x, y;					// which plot the plug exists on
	protected int sx, sy;				// where to look on the sprite sheet for plug sprite
	protected SpriteSheet ss;
	protected Rectangle hitbox;			// not useful yet
	protected long plantTime;			// time the plug was created/updated
	protected double value;				// money value of plot
	public double price;
	
	public Plug(int id, int x, int y, SpriteSheet ss)
	{
		this.plantTime = 0;
		this.id = id;
		this.x = x*Game.TILE_SIZE;
		this.y = y*Game.TILE_SIZE;
		this.ss = ss;	
	}
	
	public void saveID()
	{
		lastID = id;
	}
	
	public int getLastID()
	{
		return lastID;
	}
	
	public void water()
	{
		watered = true;
	}
	
	protected void update(int id)
	{
		this.id = id;
		plantTime = System.currentTimeMillis();
		value = 0.0;
		price = 0.0;
		watered = false;
		
		switch(id)
		{
		case Field.GRASS:
			sx=0;
			sy=0;
			plantTime = 0;
			break;
			
		case Field.DIRT:
			sx=1;
			sy=0;
			break;
			
		case Field.WHEAT_SEED:
			sx=2;
			sy=0;
			price = 3.00;
			break;
			
		case Field.POTATO_SEED:
			sx=2;
			sy=0;
			price = 50.00;
			break;
			
		case Field.WHEAT_SAPLING:
			sx=3;
			sy=0;
			break;
			
		case Field.POTATO_SAPLING:
			sx=5;
			sy=0;
			break;
			
		case Field.WHEAT:
			sx=4;
			sy=0;
			value = 5.00;
			break;
			
		case Field.POTATO:
			sx=6;
			sy=0;
			value = 100.00;
			break;
			
		case Field.WELL:
			sx=7;
			sy=0;
			break;
			
		case Field.DEAD:
			sx=8;
			sy=0;
			value = 0.0;
			break;

		default:
			break;
		}
	}
	
	protected void render(Graphics g)		//CUTS the image out of the sprite sheet based on sx & sy.
	{
		g.drawImage(ss.crop(sx, sy, Game.TILE_SIZE, Game.TILE_SIZE), (int)((x+Game.xOffset)*Game.SCALE), (int)((y+Game.yOffset)*Game.SCALE), (int)(Game.TILE_SIZE*Game.SCALE), (int)(Game.TILE_SIZE*Game.SCALE), null);
	}
	
}
