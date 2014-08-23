import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JWindow;

public class Game extends Canvas implements Runnable
{
	private static final long serialVersionUID = 1L;
	public int xPlot = 8, yPlot = 8;		// size of farm (8x8 plots)
	public static int WIDTH = 16*32, HEIGHT = 16*32, TILE_SIZE = 32, xOffset=0, yOffset=0; // field view
	public static double SCALE = 1.0;		//zoom value
	public static boolean running = false;	//game running
	public Thread gameThread;				// ???
	public static double bank = 10.00;						// money owned
	
	
	public int tickCounter;					// counts seconds
	public static double priceMod = 1.00;			// price modifier for supply/demand (not implemented yet!)
	
	private BufferedImage spriteSheet;		//sheet of sprites (lol)
	
	Inventory inventory;
	Graphix gfx;
	SpriteSheet ss;
	Field field;
	Mouse mouse;
	Keyboard keyboard;
	long startTime;
	JWindow toolTip;
	
	public void init()			//Initialized (runs ONCE at beginning of program)
	{
		startTime = System.currentTimeMillis();
		gfx = new Graphix();
		inventory = new Inventory(this);
		spriteSheet = gfx.load("SpriteSheet.png");
		ss = new SpriteSheet(spriteSheet);
		toolTip = new JWindow();
		toolTip.setOpacity(0.5f);
		field = new Field(ss);
		mouse = new Mouse(this);
		keyboard = new Keyboard(this);

	}
	
	public synchronized void start()			//starts game
	{
		if(running)return;
		
		running = true;
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public synchronized void stop()				//stops game
	{
		if(!running)return;
		
		running = false;
		try
		{
		gameThread.join();
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void run()						// main game loop
	{
		long lastTime = System.nanoTime();
		final double amountOfTicks = 60D;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		
		while(running)
		{
			long now = System.nanoTime();
			delta += (now-lastTime)/ns;
			lastTime = now;
			if(delta >= 1)
			{
				tick();
				delta--;
			}
			render();
		}
		stop();
	}
	
	public void tick()						// ticks per 1/60 seconds
	{
		tickCounter++;
		
		if(tickCounter >= 1000)
		{
			tickCounter = 0;
			priceMod += 0.01;
			if(priceMod > 1.1)
			{
				priceMod = 1.1;
			}
		}
		
		for(int x = 0; x < xPlot; x++)		// updates plots if ready for update
		{
			for(int y = 0; y < yPlot; y++)
			{
				if(timesUp(75, field.plots[x][y].waterTime))		// plots drys out after some time
				{
					field.plots[x][y].dry();
				}
				
				switch(field.plots[x][y].id)
				{
				case Field.GRASS:
					break;
					
				case Field.DIRT:
					if(timesUp(120, field.plots[x][y].plantTime))
					{
						field.plots[x][y].update(0);
					}
					break;
					
				case Field.WHEAT_SEED:
					if(timesUp(15, field.plots[x][y].plantTime))
					{
						if(field.plots[x][y].watered)
						{
							field.plots[x][y].update(Field.WHEAT_SAPLING);
						}
						else
						{
							field.plots[x][y].update(Field.DEAD);
						}
					}
					break;
					
				case Field.POTATO_SEED:
					if(timesUp(60, field.plots[x][y].plantTime))
					{
						if(field.plots[x][y].watered)
						{
							field.plots[x][y].update(Field.POTATO_SAPLING);
						}
						else
						{
							field.plots[x][y].update(Field.DEAD);
						}
					}
					break;
					
				case Field.WHEAT_SAPLING:
					if(timesUp(15, field.plots[x][y].plantTime))
					{
						if(field.plots[x][y].watered)
						{
							field.plots[x][y].update(Field.WHEAT);
						}
						else
						{
							field.plots[x][y].update(Field.DEAD);
						}
					}
					break;
					
				case Field.POTATO_SAPLING:
					if(timesUp(60, field.plots[x][y].plantTime))
					{
						if(field.plots[x][y].watered)
						{
							field.plots[x][y].update(Field.POTATO);
						}
						else
						{
							field.plots[x][y].update(Field.DEAD);
						}
					}
					break;
				}
			}
		}
	}
	
	public boolean timesUp(int sec, long plantTime)			// checks if a plot is ready for update
	{
		if(System.currentTimeMillis() - plantTime >= 1000*sec)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void render()									//  puts shit on screen
	{
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null)
		{
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		
		//RENDER START
		g.fillRect(0, 0, (int)(WIDTH*SCALE), (int)(WIDTH*SCALE));
		
		for(int x = 0; x < xPlot; x++)
		{
			for(int y = 0; y < yPlot; y++)
			{
				field.plots[x][y].render(g);
				
				if(field.plots[x][y].watered)
				{
					g.setColor(Color.BLUE);
					g.drawRect((x*TILE_SIZE), (y*TILE_SIZE), TILE_SIZE, TILE_SIZE);
				}
			}
		}
		
		g.setColor(Color.ORANGE);
		g.drawString(("$" + Double.toString(bank)), 10, 10);
		
		if(mouse.getToolImg() != null)
		{
			g.drawImage(mouse.getToolImg(), xPlot*TILE_SIZE, 0, null);
			
			if(mouse.getCursorState() == Mouse.WATER_CAN)
			{
				g.setColor(Color.black);
				g.drawRect(260, 40, 20, 32);
				g.setColor(Color.blue);
				int waterHeight = (int)(30/mouse.getWaterCap()*mouse.getWaterLevel());
				g.fillRect(261, 72-waterHeight, 19, waterHeight);
				
			}
		}
		
		//RENDER END
		
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args)
	{
		Game game = new Game();
		game.setPreferredSize(new Dimension((int)(WIDTH*SCALE), (int)(HEIGHT*SCALE)));
		game.setMaximumSize(new Dimension((int)(WIDTH*SCALE), (int)(HEIGHT*SCALE)));
		game.setMinimumSize(new Dimension((int)(WIDTH*SCALE), (int)(HEIGHT*SCALE)));
		
		JFrame frame = new JFrame("Farm Sim");
		frame.setSize((int)(WIDTH*SCALE), (int)(HEIGHT*SCALE));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(true);
		frame.add(game);
		
		game.init();
		
		game.start();
	}
	
}