import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Inventory extends JFrame
{
	private static final long serialVersionUID = 1L;

	Game instance;
	
	JPanel panel;
	
	JButton hoe, sickle, waterCan, wheat, potato;
	
	public Inventory(Game instance)
	{
		this.instance = instance;
		
		this.setTitle("Inventory");
		this.setSize(32*10, 32*3);
		this.setResizable(false);
		
		GridLayout layout = new GridLayout(0,2);
		panel = new JPanel();
		panel.setLayout(layout);
		
		hoe = new JButton("Hoe");
		hoe.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)		//Set Mouse cursor to HOE
			{
				instance.mouse.setCursor(0);
			}
		});
		panel.add(hoe);
		
		sickle = new JButton("Sickle");
		sickle.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)		//Set Mouse cursor to SICKLE
			{
				instance.mouse.setCursor(1);
			}
		});
		panel.add(sickle);
		
		waterCan = new JButton("Watering Can");
		waterCan.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				instance.mouse.setCursor(3);
			}
			
		});
		panel.add(waterCan);
		
		wheat = new JButton("Wheat - $3");
		wheat.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)		//Set Mouse cursor to SEED BAG (wheat)
			{
				instance.mouse.setCursor(2);
				instance.mouse.setSeedID(Field.WHEAT_SEED);
			}
		});
		panel.add(wheat);
		
		potato = new JButton("Potato - $50");
		potato.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)		//Set Mouse cursor to SEED BAG (potatos)
			{
				instance.mouse.setCursor(2);
				instance.mouse.setSeedID(Field.POTATO_SEED);
			}
		});
		panel.add(potato);
		
		add(panel);
	}
}