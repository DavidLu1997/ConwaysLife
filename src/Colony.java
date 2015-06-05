import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

//Colony class, stores the colony
public class Colony extends JPanel implements ActionListener, MouseListener
{
	private static final long serialVersionUID = 1L;

	//2D array to store life
	private boolean[][] life;
	
	//BufferedReader
	private BufferedReader reader;
	
	//Predefined maximum size
	public final  int size = 120;
	
	//Extension for save file
	private final String extension = ".txt";
	
	//Length of life
	private final int length = 5;
	
	//Default constructor, fully random with default
	public Colony()
	{
		life = new boolean[size][size];
		
		//Loop through assign random life
		for(int i = 0; i < life.length; i++)
		{
			for(int j = 0; j < life[i].length; j++)
			{
				life[i][j] = Math.random() < 0.5;
			}
		}
		
		addMouseListener(this);
	}
	
	//Constructor with predefined size and given
	public Colony(double density)
	{
		life = new boolean[size][size];
		
		//Loop through assign random life
		for(int i = 0; i < life.length; i++)
		{
			for(int j = 0; j < life[i].length; j++)
			{
				life[i][j] = Math.random() < density;
			}
		}
		
		addMouseListener(this);
	}
	
	//Colony with rows and columns, randomized with standard density
	public Colony(int r, int c)
	{
		life = new boolean[r][c];
		
		//Loop through assign random life
		for(int i = 0; i < life.length; i++)
		{
			for(int j = 0; j < life[i].length; j++)
			{
				life[i][j] = Math.random() < 0.5;
			}
		}
		
		addMouseListener(this);
	}
	
	//Colony with rows and columns, but randomized with given density
	public Colony(double density, int r, int c)
	{
		life = new boolean[r][c];
		
		//Loop through assign random life
		for(int i = 0; i < life.length; i++)
		{
			for(int j = 0; j < life[i].length; j++)
			{
				life[i][j] = Math.random() < density;
			}
		}
		
		addMouseListener(this);
	}
	
	//Column reading from file with sizes
	public Colony(String location)
	{
		load(location);
		
		addMouseListener(this);
	}
	
	//Getter for life
	public boolean[][] getLife()
	{
		return life;
	}
	
	//Processes life checking for life and death
	public void process()
	{
		//Temp array
		boolean[][] temp = new boolean[life.length][life[0].length];
		
		//Loop through each life
		for(int i = 0; i < life.length; i++)
		{
			for(int j = 0; j < life[i].length; j++)
			{
				temp[i][j] = live(i, j);
			}
		}
		
		//Update life
		life = temp;
		
		//Update frame
		validate();
		repaint();
	}
	
	//Loads from file
	public void load(String location)
	{
		int r = 0, c = 0;
		
		//Load file
		FileReader file = null;
		
		try
		{
			file = new FileReader(location);
			reader = new BufferedReader(file);
		}
		catch (FileNotFoundException e)
		{
			System.out.println(location + " not found");
		}
		
		//Get r and c from file
		try
		{
			r = Integer.parseInt(reader.readLine());
			c = Integer.parseInt(reader.readLine());
		}
		catch (IOException e)
		{
			System.out.println(location + " invalid.");
		}
		
		life = new boolean[r][c];
		
		//Loop through assign random life
		for(int i = 0; i < life.length; i++)
		{
			//Get string
			String str = null;
			try
			{
				str = reader.readLine();
			}
			catch (IOException e)
			{
				System.out.println(location + " invalid.");
			}
			
			for(int j = 0; j < life[i].length; j++)
			{	
				int temp = 0;
				
				temp = str.charAt(j)-'0';
				
				//Parse to boolean
				if(temp == 1)
					life[i][j] = true;
				else
					life[i][j] = false;
			}
		}
	}
	
	//Saves current to file
	public void save(String location)
	{
		//Create new file
		FileWriter output = null;
		BufferedWriter writer = null;
		
		try
		{
			output = new FileWriter(location);
			writer = new BufferedWriter(output);
		}
		
		catch(IOException e)
		{
			System.out.println(location + " invalid.");
		}
		
		//Write rows and columns
		try
		{
			writer.write(life.length + "");
			writer.newLine();
			writer.write(life[0].length + "");
			writer.newLine();
		} 
		catch (IOException e) 
		{
			System.out.println(location + " invalid.");
		}
		
		//Write array
		for(int i = 0; i < life.length; i++)
		{
			for(int j = 0; j < life[i].length; j++)
			{
				//Convert to int
				int temp;
				
				if(life[i][j])
					temp = 1;
				else
					temp = 0;
				
				//Write to file
				try
				{
					writer.write(temp + "");
				} 
				catch (IOException e) 
				{
					System.out.println(location + " invalid.");
				}
			}
			
			try
			{
				writer.newLine();
			}
			catch (IOException e)
			{
				System.out.println(location + " invalid.");
			}
		}
		
		try
		{
			writer.flush();
			writer.close();
		}
		catch (IOException e)
		{
			
		}
	}
	
	//Paintcomponent for displaying life
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		//Draw border
		g.drawRect(0, 0, life[0].length * length, life.length * length);
		
		//Locations to print next life
		int x = 0, y = 0;
		
		//Loop through life
		for(int i = 0; i < life.length; i++)
		{
			for(int j = 0; j < life[i].length; j++)
			{
				//If is alive
				if(life[i][j])
				{
					//Draw circle
					g.fillOval(x, y, length, length);
				}
				
				//Increment x
				x += length;
			}
			
			//Increment y
			y += length;
			
			//Reset x
			x = 0;
		}
	}
	
	//Live, determines whether an organism lives or dies, true if alive, false otherwise
	private boolean live(int r, int c)
	{
		//Get number of adjacent
		int adjacent = adjacent(r, c);
		
		//If current is alive
		if(life[r][c])
		{
			//Dies if less than 2 neighbours
			if(adjacent < 2)
				return false;
			
			//Lives if 2-3 neighbours
			else if(adjacent >= 2 && adjacent <= 3)
				return true;
			
			//Dies if more than 3 neighbours
			else
				return false;
		}
		
		//If current is not alive
		else
		{
			//If exactly 3 neighbours, becomes live
			if(adjacent == 3)
				return true;
			
			//Otherwise remains dead
			else
				return false;
		}
	}
	
	//Get number of live neighbours
	private int adjacent(int r, int c)
	{
		int num = 0;
		
		//Upper left
		if(r-1 >= 0 && c-1 >= 0 && life[r-1][c-1])
			num ++;
		//Upper middle
		if(r-1 >= 0 && life[r-1][c])
			num ++;
		
		//Upper right
		if(r-1 >= 0 && c+1 < life[r-1].length && life[r-1][c+1])
			num ++;
		
		//Left
		if(c-1 >= 0 && life[r][c-1])
			num ++;
		
		//Right
		if(c+1 < life[r].length && life[r][c+1])
			num ++;
		
		//Lower left
		if(r+1 < life.length && c-1 >= 0 && life[r+1][c-1])
			num ++;
		
		//Lower middle
		if(r+1 < life.length && life[r+1][c])
			num ++;
		
		//Lower right
		if(r+1 < life.length && c+1 < life[r+1].length && life[r+1][c+1])
			num ++;
		
		return num;
	}
	
	//Gets the next boolean from file
	private boolean nextBoolean(String location)
	{
		//Get int
		int temp = 0;
		
		try
		{
			//Get next line
			String result = reader.readLine();
			
			//If valid
			if(result != null && !result.isEmpty())
				temp = Integer.parseInt(result);
		}
		catch (IOException e)
		{
			System.out.println(location + " invalid.");
		}
		
		//Convert to bool
		if(temp == 1)
			return true;
		else
			return false;
	}
	
	//Returns larger integer
	private int max(int i, int j)
	{
		if(i > j)
			return i;
		else
			return j;
	}
		
	//Returns smaller integer
	private int min(int i, int j)
	{
		if(i < j)
			return i;
		else
			return j;
	}

	//ActionListener
	public void actionPerformed(ActionEvent e)
	{
		//Switch each one
		switch(e.getActionCommand())
		{
		//Saves current
		case "save":
			
			//Get location
			String location = Main.fileName.getText();
			
			//Check for empty
			if(location.isEmpty())
				location = "default";
			
			//Add 
			location += extension;
			
			//Saves file
			save(location);
			
			break;
			
		//Loads from file
		case "load":
			
			//Pause simulation
			Main.iterationsPerSecond.setValue(0);
			
			//Launch file chooser
			Main.fileFinder = new JFileChooser();
			
			//Get return value
			int returnValue = Main.fileFinder.showOpenDialog(this);
			
			//If approved
			if(returnValue == JFileChooser.APPROVE_OPTION)
			{
				//Load file
				load(Main.fileFinder.getSelectedFile().getPath());
			}
			
			//Redraw
			validate();
			repaint();
			
			break;
		case "generate":
			
			//Get density
			double density = 0.5;
			
			//Check validity
			if(!Main.density.getText().isEmpty()) 
				density = Double.parseDouble(Main.density.getText()) / 100;
			
			//Truncate density
			if(density < 0)
				density = 0;
			
			//Get length
			int length = size;
			
			//Check validity
			if(!Main.length.getText().isEmpty())
				length = Integer.parseInt(Main.length.getText());
			
			//Truncate length
			if(length <= 0 || length > size)
				length = size;
			
			//Get width
			int width = size;
			
			//Check validity
			if(!Main.width.getText().isEmpty())
				width = Integer.parseInt(Main.width.getText());
			
			//Truncate length
			if(width <= 0 || width > size)
				width = size;
			
			//Update life
			Colony c = new Colony(density, length, width);
			
			this.life = c.getLife();
			
			//Pause simulation
			Main.iterationsPerSecond.setValue(0);
			
			//Redraw
			validate();
			repaint();
			
			break;
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0)
	{
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	//When mouse is pressed
	public void mousePressed(MouseEvent arg0)
	{
		//Get whether current is eradicate or populate, populate true, eradicate false
		boolean action = Main.populate.isSelected();
		
		//Get radius
		int radius = Main.radius.getValue();
		
		//Get x clicked
		int x = arg0.getX();
		
		//Get y
		int y = arg0.getY();
		
		//Integer division on both to truncate to nearest x and y
		x = (x/length) * length;
		y = (y/length) * length;
		
		//X
		for(int i = max(y - radius * length, 0); i < life.length * length && i <= y + radius * length; i+= length)
		{
			//Y
			for(int j = max(x - radius * length, 0); j < life.length * length && j <= x + radius * length; j+= length)
			{
				life[ i/length ][ j/length ] = action;
			}
		}
		
		//Pause simulation
		Main.iterationsPerSecond.setValue(0);
		
		//Redraw
		validate();
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}
}
