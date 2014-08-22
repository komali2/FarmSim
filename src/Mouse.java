import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

public class Mouse implements MouseListener, MouseMotionListener, MouseWheelListener
{
	
	Game instance;
	final public static int HOE = 0, SICKLE = 1, SEED = 2, WATER_CAN = 3, HAMMER = 4;
	private int cursorState;
	private BufferedImage toolImg = null, hoeImg, sickleImg, seedsImg, waterCanImg, hammerImg;
	private int seedID;
	private int waterLevel = 0, waterCap = 3;
	int xMouseLocation, yMouseLocation;
	public Mouse(Game instance)
	{
		this.instance = instance;
		instance.addMouseListener(this);
		instance.addMouseWheelListener(this);
		instance.addMouseMotionListener(this);
		hoeImg = instance.gfx.load("Hoe.png");
		sickleImg = instance.gfx.load("Sickle.png");
		seedsImg = instance.gfx.load("Seeds.png");
		waterCanImg = instance.gfx.load("WaterCan.png");
		hammerImg = instance.gfx.load("Hammer.png");
	}

	public void setSeedID(int id)
	{
		seedID = id;
	}
	
	public double getWaterLevel()
	{
		return waterLevel;
	}
	
	public void spendWater()
	{
		waterLevel--;
	}
	
	public void setCursor(int id)
	{
		switch(id)
		{
		case HOE:
			toolImg = hoeImg;
			break;
			
		case SICKLE:
			toolImg = sickleImg;
			break;
			
		case SEED:
			toolImg = seedsImg;
			break;
			
		case WATER_CAN:
			toolImg = waterCanImg;
			default:
			break;
			
		case HAMMER:
			toolImg = hammerImg;
		}
		cursorState = id;
	}
	
	public int getCursorState()
	{
		return cursorState;
	}
	
	public int getWaterCap()
	{
		return waterCap;
	}
	
	public void setWaterCap(int cap)
	{
		waterCap = cap;
	}
	
	public BufferedImage getToolImg()
	{
		return toolImg;
	}
	
	@Override
	public void mouseClicked(MouseEvent me)
	{
		int x = me.getX() - Game.xOffset;
		xMouseLocation = me.getX();
		x = (int)((x-(x%Game.TILE_SIZE))/(Game.TILE_SIZE*Game.SCALE));
		int y = me.getY();
		yMouseLocation = me.getY();
		y = (int)((y-(y%Game.TILE_SIZE))/(Game.TILE_SIZE*Game.SCALE));
		
		switch(cursorState)
		{
		case HOE:
			if(!instance.field.plots[x][y].isStructure())
			{
				instance.field.plots[x][y].update(Field.DIRT);
			}
			break;
			
		case SICKLE:
			if(instance.field.plots[x][y].value != 0.0 && !instance.field.plots[x][y].isStructure())
			{
				sell(instance.field.plots[x][y]);
				instance.field.plots[x][y].update(Field.GRASS);
			}
			
			break;
			
		case WATER_CAN:
			if(instance.field.plots[x][y].id == Field.WELL)
			{
				waterLevel = waterCap;
				System.out.println(waterLevel);
				Game.bank -= 1.00;
			}
			else if(waterLevel > 0 && !instance.field.plots[x][y].watered && !instance.field.plots[x][y].isStructure())
			{
				instance.field.plots[x][y].water();
				spendWater();
			}
			break;
		
		case SEED:
			if(instance.field.plots[x][y].id == Field.DIRT && Game.bank >= instance.field.plots[x][y].price) //gotta change this to equal BEFORE price instead of after price so you cant go negative.
			{
				instance.field.plots[x][y].update(seedID);
				buy(instance.field.plots[x][y]);
			}
			break;
			
		case HAMMER:
			if(instance.field.plots[x][y].isStructure())
			{
				sell(instance.field.plots[x][y]);
				instance.field.plots[x][y].update(0);
			}
			else if(instance.field.plots[x][y].id == Field.GRASS)
			{
				System.out.println("BUILD SOMETHING!!!");
			}
		}
	}
	
	public void sell(Plug plug)
	{
		Game.bank += plug.value;
	}
	
	public void buy(Plug plug)
	{
		Game.bank -= plug.price;
	}

	@Override
	public void mouseEntered(MouseEvent me)
	{
		
	}

	@Override
	public void mouseExited(MouseEvent me)
	{
		
	}

	@Override
	public void mousePressed(MouseEvent me)
	{
		
	}

	@Override
	public void mouseReleased(MouseEvent me)
	{
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent me)		// this code works but is annoying atm
	{
		
		if(me.getWheelRotation() == 1)
		{
			instance.inventory.setVisible(true);
		}
	/*	{
			if(me.getWheelRotation() == 1)
			{
				Game.SCALE += 0.1;
			}
			else if(me.getWheelRotation() == -1)
			{
				Game.SCALE -= 0.1;
			}
		}
		if(Game.SCALE < 1.0)
		{
			Game.SCALE = 1.0;
		}
		if(Game.SCALE > 4.0)
		{
			Game.SCALE = 4.0;
		}*/
	}

	@Override
	public void mouseDragged(MouseEvent me)  // this code works but is annoying atm
	{
		/*int xNewLocation = me.getX();
		int yNewLocation = me.getY();
		
		if(xMouseLocation > xNewLocation)
		{
			Game.xOffset--;
		}
		else if(xMouseLocation < xNewLocation)
		{
			Game.xOffset++;
		}
		
		if(yMouseLocation > yNewLocation)
		{
			Game.yOffset--;
		}
		else if(yMouseLocation < yNewLocation)
		{
			Game.yOffset++;
		}
		
		if(Game.yOffset > 0)
		{
			Game.yOffset = 0;
		}
		if(Game.xOffset > 0)
		{
			Game.xOffset = 0;
		}
		xMouseLocation = xNewLocation;
		yMouseLocation = yNewLocation;*/
	}

	@Override
	public void mouseMoved(MouseEvent me)
	{
		
	}	
}

