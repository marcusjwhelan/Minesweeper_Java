/*
		Marcus Whelan
		Section 1
		Program 5 problem 1
		Minesweeper
*/
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Scanner;
import java.util.Random;
import java.lang.Object;
import javax.swing.border.BevelBorder;

public class Minesweeper extends JFrame
{

	public static Minesweeper Game;
	static int ROWS,COLS;
	public int MINES;
	public int tilesLeft;
	public int Gtemp =0;
	private int TotalMines;
	static boolean MineArray[][];
	static boolean IsPressed [][];
	static JToggleButton grid[];
	int X = 0;
	int Y = 0;
	static int adjacentGrid[][];
	static int type = 0;
	static int HEIGHT = 500;
	static int WIDTH = 500;
	JPanel gameContentPane = null;

	
	public static void main(String[] args)
	{
		Game = new Minesweeper();
	}
	public Minesweeper()
	{
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		if(type == 0)
		{
			ROWS = 10;
			COLS = 10;
			MINES = 20;
			WIDTH = 500;
			HEIGHT = 500;
		}
		else if(type == 1)
		{
			ROWS = 15;
			COLS = 15;
			MINES = 60;
			WIDTH = 700;
			HEIGHT = 700;
		}
		else if(type == 2)
		{
			ROWS = 20;
			COLS = 20;
			MINES = 120;
			WIDTH = 900;
			HEIGHT = 900;
		}
		buildJFrame();
	}
	
	//----------Building the frame and menu bar --------------------------
	private void buildJFrame()
	{
		this.setSize(WIDTH,HEIGHT);
		this.setTitle("Minesweeper	       Total Mines: "+ TotalMines);
		MineArray = new boolean[ROWS][COLS];
		IsPressed = new boolean[ROWS][COLS];
		adjacentGrid = new int[ROWS][COLS];
		
		grid = new JToggleButton[ROWS*COLS];
		GridLayout gridLayout = new GridLayout();
		gridLayout.setRows(ROWS);
		gridLayout.setColumns(COLS);
		gameContentPane = new JPanel();
		gameContentPane.setLayout(gridLayout);
		this.setContentPane(gameContentPane);
		
		JMenuBar menuBar;
		JMenu gameMenu;
		JMenuItem loadGame,saveGame,exitGame,newGame;
		JMenuItem hardGame,mediumGame,easyGame;
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		gameMenu = new JMenu("Game");
		menuBar.add(gameMenu);
		
//---------------------------Menu items for first list -----------------------------------
		saveGame = new JMenuItem("Save Game");
		saveGame.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SaveGame(JOptionPane.showInputDialog("Save game as."));
			}
		});
		gameMenu.add(saveGame);
		// if you press on the load game 
		loadGame = new JMenuItem("Load Game");
		loadGame.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser ch = new JFileChooser();
				ch.setCurrentDirectory(new java.io.File("."));
				FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES","txt");
				ch.setFileFilter(filter);
				// window name
				ch.setDialogTitle("Choose game");
				if(ch.showOpenDialog(loadGame) == JFileChooser.APPROVE_OPTION)
				{
					String f = ch.getSelectedFile().getAbsolutePath();
					int index = f.lastIndexOf("\\");
					String fnew = f.substring(index +1);
					LoadGame(fnew);
				}
			}
		});
		gameMenu.add(loadGame);
		//----------------------Menu items for new game--------------------------------------	
		newGame = new JMenu("New Game");
		gameMenu.add(newGame);
		hardGame = new JMenuItem("Hard");
		hardGame.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Game.setVisible(false);
				Game = null;
				Minesweeper.type = 2;
				Game = new Minesweeper();
			}
		});
		newGame.add(hardGame);
		
		mediumGame = new JMenuItem("Medium");
		mediumGame.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Game.setVisible(false);
				Game = null;
				Minesweeper.type = 1;
				Game = new Minesweeper();
			}
		});
		newGame.add(mediumGame);
		
		easyGame = new JMenuItem("Easy");
		easyGame.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Game.setVisible(false);
				Game = null;
				Minesweeper.type = 0;
				Game = new Minesweeper();
			}
		});
		newGame.add(easyGame);
		//---------------------------------------------------------------------------------------------------
		// if you press on the exit game
		exitGame = new JMenuItem("Close");
		exitGame.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		gameMenu.add(exitGame);
		StartGame();
		this.setVisible(true);
	}
	// Loading game from a text file
	private static void LoadGame(String fileName)
	{	
		Game.setVisible(false);
		Game = null;
		try
		{
			File file = new File(fileName);
			Scanner scan = new Scanner(file);
			// Get all of the constructor data types of ints
			String[] tempRows = (scan.nextLine()).split(":");
			printArrayS(tempRows);
			System.out.println("\n");
			Minesweeper.ROWS = Integer.parseInt(tempRows[1]);
			String[] tempCols = (scan.nextLine()).split(":");
			Minesweeper.COLS = Integer.parseInt(tempCols[1]);
			String[] tempType = (scan.nextLine()).split(":");
			Minesweeper.type = Integer.parseInt(tempType[1]);
			String[] tempHeight = (scan.nextLine()).split(":");
			Minesweeper.HEIGHT = Integer.parseInt(tempHeight[1]);
			String[] tempWidth = (scan.nextLine()).split(":");
			Minesweeper.WIDTH = Integer.parseInt(tempWidth[1]);
			// Get all of the Strings of data lenght
			String[] adj = (scan.nextLine()).split(":");
			printArrayS(adj);
			System.out.println("\n");
			String[] Mines = (scan.nextLine()).split(":");
			printArrayS(Mines);
			System.out.println("\n");
			String[] Pressed = (scan.nextLine()).split(":");
			printArrayS(Pressed);
			System.out.println("\n");
			
			
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	public static void printArrayI(int[][] a)
	{
		for(int i = 0; i < a.length; i++)
		{
			System.out.println(a[i]);
		}
	}
	public static void printArrayS(String[] a)
	{
		for(int i = 0; i < a.length; i++)
		{
			System.out.println(a[i]);
		}
	}
	// save game from a text file
	private static void SaveGame(String fileName)
	{
		try
		{
			BufferedWriter w = new BufferedWriter(new FileWriter(fileName+".txt"));
			w.write("Rows:"+ROWS+"\n");
			w.write("Columns:"+COLS+"\n");
			w.write("Type:"+type+"\n");
			w.write("Height:"+HEIGHT+"\n");
			w.write("Width:"+WIDTH+"\n");
			
			w.write("adjacent:");
			for(int x =0;x<adjacentGrid.length;x++)
			{
				for(int y = 0; y<adjacentGrid[x].length;y++)
				{
					w.write(adjacentGrid[x][y]+",");
				}
				w.write("end");
			}
			w.write("\n");
			
			w.write("Mine Array:");
			for(int x =0;x<MineArray.length;x++)
			{
				for(int y = 0; y<MineArray[x].length;y++)
				{
					w.write(MineArray[x][y]+",");
				}
				w.write("end");
			}
			w.write("\n");
			
			w.write("Is Pressed:");
			for(int x =0;x<IsPressed.length;x++)
			{
				for(int y = 0; y<IsPressed[x].length;y++)
				{
					w.write(IsPressed[x][y]+",");
				}
				w.write("end");
			}
			w.close();
		}
		catch(IOException e)
		{
			JOptionPane.showMessageDialog(null,"Could not save.");
			e.printStackTrace();
		}
	}
	private void StartGame()
	{
		BuildGrid();
		PlaceMines(MINES);
	}
	// sets up all the grids quares adds listeners for left and right click
	private void BuildGrid()
	{
		int t = 0;
		for(int x=0;x<ROWS;x++)
		{
			for(int y = 0;y<COLS;y++)
			{
				X = x;
				Y = y;
				grid[t] = new JToggleButton("");
				grid[t].addMouseListener(new java.awt.event.MouseAdapter()
				{
					public void mouseReleased(java.awt.event.MouseEvent e)
					{
						if(e.getModifiers() == InputEvent.BUTTON3_MASK)
						{
							RightClick(e);
						}
						else if(e.getModifiers() == InputEvent.BUTTON1_MASK)
						{
							LeftClick(e);
						}
					}
				});
				gameContentPane.add(grid[t]);
				t++;
			}
		}
	}
	// Places all the mines into the array
	private void PlaceMines(int mines)
	{
		// set all grids to false for start
		for(int x = 0; x<ROWS;x++)
		{
			for(int y = 0; y<COLS;y++)
			{
				MineArray[x][y] = false;
			}
		}
		int t,z,count = 0,Mines =mines;
		while(count<=Mines)
		{
			t = (int)(Math.random()*ROWS);
			z = (int)(Math.random()*COLS);
			if(MineArray[t][z] == false)
			{
				MineArray[t][z] = true;
				count++;
			}
		}
		for(int x = 0; x<ROWS;x++)
		{
			for(int y = 0; y<COLS;y++)
			{
				adjacentGrid[x][y] = getMines(x,y);
				IsPressed[x][y] = false;
			}
		}
		totalMines();
		gameContentPane.setEnabled(true);
	}
	private void LeftClick(java.awt.event.MouseEvent e)
	{
		JToggleButton mineButton = (JToggleButton)e.getSource();
		if(mineButton.isEnabled())
		{
			mineButton.setEnabled(false);
			if(isMine(mineButton))
			{
				showMines();
				JOptionPane.showMessageDialog(null,"Game Over");
			}
			else
			{
				mineButton.setBackground(null);
				if(mineButton.getText() == "|>")
				{
					tilesLeft = tilesLeft-1;
					//IsPressed[X][Y] = true;
					mineButton.setText("");
				}
				if(adjacentGrid[X][Y] > 0)
				{
					mineButton.setText(Integer.toString(adjacentGrid[X][Y]));
					tilesLeft = tilesLeft - 1;
					//IsPressed[X][Y] = true;
				}
				else if(adjacentGrid[X][Y] ==0)
				{
					clearGrid(X,Y);
					tilesLeft = tilesLeft - Gtemp;
					Gtemp = 0;
				}
				
				if(tilesLeft == MINES +1)
				{
					JOptionPane.showMessageDialog(null,"You Win");
				}
			}
		}
	}
	// Shows all the mines at the end of the game.
	private void showMines()
	{
		for(int x=0;x<ROWS;x++)
		{
			for(int y=0;y<COLS;y++)
			{
				if(isMine(grid[x*ROWS+y]))
				{
					grid[x*ROWS+y].setText("*");
					grid[x*ROWS+y].setBackground(Color.RED);
				}
			}
		}
	}
	// This clears all adjacent buttons making them go blank if no mine
	private void clearGrid(int i, int j)
	{
		if(isAround(i,j))
		{
			if(!IsPressed[i][j] && !MineArray[i][j])
			{
				Gtemp = Gtemp +1;
				IsPressed[i][j] = true;
				JToggleButton mineButton = check(i,j);
				if(adjacentGrid[i][j]>0)
				{
					mineButton.setText(Integer.toString(adjacentGrid[i][j]));
				}
				else
				{
					mineButton.setText("");
				}
				mineButton.setSelected(true);
				mineButton.setEnabled(false);
				if(adjacentGrid[i][j] == 0)
				{
					for(int x = -1; x<= 1; x++)
					{
						for(int y = -1;y<=1; y++)
						{
							clearGrid(i+x,j+y);
						}
					}
				}
			}
		}
	}
	private JToggleButton check(int x, int y)
	{
		return grid[(x*ROWS+y)];
	}
	private void RightClick(java.awt.event.MouseEvent e)
	{
		JToggleButton mineButton = (JToggleButton)e.getSource();
		if(mineButton.isEnabled())
		{
			if(mineButton.getText() != "|>")
			{
				mineButton.setText("|>");
				mineButton.setBackground(Color.RED);
			}
			else if(mineButton.getText() == "|>")
			{
				mineButton.setText("");
				mineButton.setBackground(null);
			}
			else mineButton.setText("");
		}
	}

	private int getMines(int x,int y)
	{
		int mines = 0;
		for(int i = -1;i<=1;i++)
		{
			for(int j = -1;j<=1;j++)
			{
				int new_x = x+i;
				int new_y = y+j;
				if(isAround(new_x,new_y)&&MineArray[new_x][new_y])
				{
					mines++;
				}
			}
		}
		return mines;
	}
	// checks to see if there is a mine in the grid coordinate and returns true
	// or false for that coordinate
	private boolean isAround(int x, int y)
	{
		if(x >= 0 && x< adjacentGrid.length && y >=0 && y < adjacentGrid[x].length)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	// gets the number of mines in the field and shows them on the grid
	private void totalMines()
	{
		for(int x = 0; x<ROWS;x++)
		{
			for(int y = 0; y<COLS;y++)
			{
				if(isMine(grid[x*ROWS+y]))
				{
					TotalMines = TotalMines + 1;
				}
			}
		}
		tilesLeft = ROWS*COLS;
		this.setTitle("Minesweeper	       Total Mines: "+ TotalMines);
	}
	// checks if there is a mine at that point and returns the result of the point
	// else it returns false
	private boolean isMine(JToggleButton mB)
	{
		int i= 0;
		for(int x = 0; x<ROWS;x++)
		{
			for(int y = 0; y<COLS;y++)
			{
				if(mB == grid[i])
				{
					X = x;
					Y = y;
					return MineArray[x][y];
				}
				i++;
			}
		}
		return false;
	}
}






