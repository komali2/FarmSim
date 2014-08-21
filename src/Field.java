

public class Field
{
	public Plug[][] plots;
	
	public static final int GRASS 			= 0,
							DIRT			= 1,
							WHEAT_SEED		= 2,
							POTATO_SEED		= 3,
							WHEAT_SAPLING	= 4,
							POTATO_SAPLING	= 5,
							WHEAT			= 6,
							POTATO			= 7,
							WELL			= 8,
							DEAD			= 9;
							
	
	public Field(SpriteSheet ss)
	{
		plots = new Plug[8][8];
		
		for(int x = 0; x < 8; x++)
		{
			for(int y = 0; y < 8; y++)
			{
				plots[x][y] = new Plug(0, (int)(x*Game.SCALE), (int)(y*Game.SCALE), ss);
			}
		}
		
		plots[4][4].update(WELL);
	}
}
