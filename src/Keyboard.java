
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Keyboard implements KeyListener
{
	Game instance;
	
	public Keyboard(Game instance)
	{
		this.instance = instance;
		instance.addKeyListener(this);
	}
	
	@Override
	public void keyPressed(KeyEvent ke)
	{
		int key = ke.getKeyCode();
		
		System.out.println(key);
	}

	@Override
	public void keyReleased(KeyEvent ke)
	{
		
	}

	@Override
	public void keyTyped(KeyEvent ke)
	{
		
	}

}
