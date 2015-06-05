import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;

//Main class, ties everything together
public class Main {
	//Colony
	static Colony colony;
	
	//JFrame
	static JFrame frame;
	
	//Content pane
	static JPanel pane;
	
	//Buttons pane
	static JPanel buttons;
	static JPanel create;
	
	//JSlider
	static JSlider iterationsPerSecond;
	static JSlider radius;
	
	//JTextField
	static JTextField fileName;
	static JTextField density;
	static JTextField width;
	static JTextField length;
	
	//JButtons
	static JButton save;
	static JButton load;
	static JButton generate;
	
	//RadioButtons
	static JRadioButton populate;
	static JRadioButton eradicate;
	
	//JFileChooser
	static JFileChooser fileFinder;
	
	//Min
	static final int min = 0;
	static final int radiusMin = 0;
	
	//Max
	static final int max = 100;
	static final int radiusMax = 10;
	
	//Initial
	static final int init = 50;
	static final int radiusInit = 0;
	
	//Creates GUI
	public static void create()
	{
		frame = new JFrame("Conway's Life");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		colony = new Colony(0.5);
		
		//Initialize pane using BorderLayout
		pane = new JPanel();
		pane.setLayout(new BorderLayout());
		
		//Initialize buttons pane
		buttons = new JPanel();
		
		//Text
		buttons.add(new JLabel("Iterations per second"));
		
		//Slider
		iterationsPerSecond = new JSlider(min, max, init);
		iterationsPerSecond.setMajorTickSpacing(20);
		iterationsPerSecond.setMinorTickSpacing(5);
		iterationsPerSecond.setPaintTicks(true);
		iterationsPerSecond.setPaintLabels(true);
		buttons.add(iterationsPerSecond);
		
		//Populate
		populate = new JRadioButton("Populate");
		populate.setMnemonic(KeyEvent.VK_P);
		populate.setActionCommand("populate");
		populate.setSelected(true);
		
		//Eradicate
		eradicate = new JRadioButton("Eradicate");
		eradicate.setMnemonic(KeyEvent.VK_E);
		eradicate.setActionCommand("eradicate");
		eradicate.setSelected(false);
		
		//Group the buttons
		ButtonGroup group = new ButtonGroup();
		group.add(populate);
		group.add(eradicate);
		
		//Add to pane
		buttons.add(populate);
		buttons.add(eradicate);
		
		//Label
		buttons.add(new JLabel("Radius: "));
		
		//Radius
		radius = new JSlider(radiusMin, radiusMax, radiusInit);
		radius.setMajorTickSpacing(2);
		radius.setMinorTickSpacing(1);
		radius.setPaintTicks(true);
		radius.setPaintLabels(true);
		buttons.add(radius);
		
		//Initialize create pane
		create = new JPanel();
		
		//Generate
		generate = new JButton("Generate");
		generate.setMnemonic(KeyEvent.VK_G);
		generate.addActionListener(colony);
		generate.setActionCommand("generate");
		create.add(generate);
		
		//Density
		create.add(new JLabel("Density (0-100): "));
		
		//Density text field
		density = new JTextField(3);
		density.setText("50");
		create.add(density);
		
		//Length
		create.add(new JLabel("Length: "));
		
		//Length textfield
		length = new JTextField(4);
		length.setText(colony.size + "");
		create.add(length);
		
		//Width
		create.add(new JLabel("Width: "));
		
		//Width text field
		width = new JTextField(4);
		width.setText(colony.size + "");
		create.add(width);
		
		//Save
		save = new JButton("Save");
		save.setMnemonic(KeyEvent.VK_S);
		save.addActionListener(colony);
		save.setActionCommand("save");
		create.add(save);
				
		//filename to save to
		fileName = new JTextField(10);
		fileName.setText("default");
		create.add(fileName);
				
		//Txt extension
		create.add(new JLabel(".txt"));
				
		//Load
		load = new JButton("Load");
		load.setMnemonic(KeyEvent.VK_L);
		load.addActionListener(colony);
		load.setActionCommand("load");
		create.add(load);
		
		//Add all to pane
		pane.add(buttons, BorderLayout.PAGE_START);
		pane.add(colony, BorderLayout.CENTER);
		pane.add(create, BorderLayout.PAGE_END);
		
		//Set frame
		frame.setContentPane(pane);
		frame.pack();
		frame.setSize(800, 800);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
	
	//Main
	public static void main(String[] args) 
	{
		//Create gui
		create();
		
		//Run simulation
		while(true)
		{	
			//If value is above 0
			if(iterationsPerSecond.getValue() != 0)
			{
				colony.process();
				
				//Delay
				try 
				{
					Thread.sleep(1000/iterationsPerSecond.getValue());
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
			
			//Otherwise, delay for 100ms before rechecking
			try 
			{
				Thread.sleep(100);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}

}
