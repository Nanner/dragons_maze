package game.ui;


import static org.junit.Assert.fail;
import game.logic.Game;
import game.maze.Maze;
import game.maze.MazeSymbol;
import game.maze.Tile;
import game.objects.Dragon;
import game.objects.Eagle;
import game.objects.Hero;
import game.objects.Sword;
import game.ui.gui.GUInterface;
import game.ui.gui.InfoPanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * The Class GameOutput is responsible for every printing operation, including printing the graphics for the game.
 * It also handles the basic file input/output operations such as saving and loading.
 */
public class GameOutput {
	private static String PROMPT = "> "; //$NON-NLS-1$
	
	public static String SAVE_EXTENSION = ".dmaze"; //$NON-NLS-1$
	public static String SAVE_EXTENSION_TYPE = "dmaze"; //$NON-NLS-1$
	public static String SAVE_EXTENSION_DESCRIPTION = "DragonMaze files"; //$NON-NLS-1$

	/**
	 * Gets the immovable maze symbols from a given maze, such as walls and empty tiles.
	 * @param m A maze
	 * @return An array with the symbols of the corresponding maze tiles
	 */
	public static char[][] getMazeSymbols(Maze m) { 
		char[][] mazePositions = new char[m.getRows()][m.getColumns()];

		for(int r = 0; r < m.getRows(); r++)
			for(int c = 0; c < m.getColumns(); c++) {
				Tile currentTile = m.getPositions()[r][c];
				switch(currentTile) {
				case wall:
					mazePositions[r][c] = MazeSymbol.wall;
					break;
				case empty:
					mazePositions[r][c] = MazeSymbol.empty;
					break;
				case exit:
					mazePositions[r][c] = MazeSymbol.exit;
					break;
				}
			}
		return mazePositions;
	}
	
	/**
	 * Shows the save game dialog window with only the files of the predefined extension and type.
	 * @param game the game
	 */
	public static void showSaveGameDialog(Game game) {
		JFileChooser fileChooser = new JFileChooser();
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter(SAVE_EXTENSION_DESCRIPTION, SAVE_EXTENSION_TYPE);
		fileChooser.setFileFilter(filter);
		
		if (fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
			
			File file = fileChooser.getSelectedFile();
			
			String filePath = file.getAbsolutePath();
			
			if(!filePath.endsWith(SAVE_EXTENSION)) {
			    file = new File(filePath + SAVE_EXTENSION);
			}
			
			JOptionPane.showMessageDialog(null,
					Messages.getString("GameOutput.4"), //$NON-NLS-1$
					Messages.getString("GameOutput.5"), //$NON-NLS-1$
					JOptionPane.INFORMATION_MESSAGE);
			
			save(game, file);
		}
	}

	/**
	 * Gets the immovable maze sprites from a given maze, such as walls and empty tiles.
	 * @param m The maze
	 * @param pictures The picture sprites to put on the array
	 * @return An array with the symbols of the corresponding maze tiles
	 */
	public static BufferedImage[][] getMazePictures(Maze m, MazePictures pictures) { 
		BufferedImage[][] mazePositions = new BufferedImage[m.getRows()][m.getColumns()];

		for(int r = 0; r < m.getRows(); r++)
			for(int c = 0; c < m.getColumns(); c++) {
				Tile currentTile = m.getPositions()[r][c];
				switch(currentTile) {
				case wall:
					mazePositions[r][c] = pictures.wall;
					break;
				case empty:
					mazePositions[r][c] = pictures.empty;
					break;
				case exit:
					mazePositions[r][c] = pictures.exit;
					break;
				}
			}
		return mazePositions;
	}

	/**
	 * Prints the maze in a command line environment
	 * @param m The maze
	 */
	public static void printMaze(Maze m) {

		char[][] mazePositions = getMazeSymbols(m);

		for(int i = 0; i < 100; i++)
			System.out.println();
		for (int x = 0; x < m.getRows(); x++) {
			for (int y = 0; y < m.getColumns(); y++) {
				System.out.print(mazePositions[x][y]);
				System.out.print(MazeSymbol.space);
			}
			System.out.print('\n');
		}
	}

	/**
	 * Prints the game in a command line interface.
	 * @param g The game to print.
	 */
	public static void printGame(Game g) {

		Maze m = g.getMaze();
		Hero h = g.getHero();
		ArrayList<Dragon> d = g.getDragons();
		Sword s = g.getSword();
		Eagle e = g.getEagle();

		char[][] mazePositions = getMazeSymbols(m);

		// hero printing
		if(h.print)
			if(h.getState() == Hero.ARMED || h.getState() == Hero.EXITED_MAZE)
				mazePositions[h.getRow()][h.getColumn()] = MazeSymbol.armedHero;
			else
				mazePositions[h.getRow()][h.getColumn()] = MazeSymbol.hero;

		// dragon printing
		for(int i = 0; i < g.getNumberOfDragons(); i++) {
			if(d.get(i).getState() == Dragon.ALIVE && d.get(i).getHasSword())
				mazePositions[d.get(i).getRow()][d.get(i).getColumn()] = MazeSymbol.guardedSword;
			else if(d.get(i).getState() == Dragon.ALIVE && !d.get(i).getHasSword())
				mazePositions[d.get(i).getRow()][d.get(i).getColumn()] = MazeSymbol.dragon;
			else if(d.get(i).getState () == Dragon.ASLEEP && d.get(i).getHasSword())
				mazePositions[d.get(i).getRow()][d.get(i).getColumn()] = MazeSymbol.sleepingGuardedSword;
			else if(d.get(i).getState() == Dragon.ASLEEP && !d.get(i).getHasSword())
				mazePositions[d.get(i).getRow()][d.get(i).getColumn()] = MazeSymbol.sleepingDragon;

			if(!s.isTaken())
				mazePositions[s.getRow()][s.getColumn()] = MazeSymbol.sword;
		}

		// eagle printing
		if ( !e.isWithHero() && e.getState() != Eagle.DEAD){
			switch (mazePositions[ e.getRow() ][e.getColumn() ] ) {
			case MazeSymbol.hero:
				mazePositions[ e.getRow() ][e.getColumn() ] = MazeSymbol.eagleOnHero;
				break;
			case MazeSymbol.dragon:
				mazePositions[ e.getRow() ][e.getColumn() ] = MazeSymbol.eagleOnDragon;
				break;
			case MazeSymbol.wall:
				mazePositions[ e.getRow() ][e.getColumn() ] = MazeSymbol.eagleOnWall;
				break;
			case MazeSymbol.empty:
				mazePositions[ e.getRow() ][e.getColumn() ] = MazeSymbol.eagle;
				break;
			case MazeSymbol.sleepingDragon:
				mazePositions[ e.getRow() ][e.getColumn() ] = MazeSymbol.eagleOnSleepingDragon;
				break;
			case MazeSymbol.sword:
				mazePositions[ e.getRow() ][e.getColumn() ] = MazeSymbol.eagleWithSword;
				break;
			default:
				break;
			}
		}

		for (int x = 0; x < m.getRows(); x++) {
			for (int y = 0; y < m.getColumns(); y++) {
				System.out.print(mazePositions[x][y]);
				System.out.print(MazeSymbol.space);
			}
			System.out.println();
		}
		System.out.println();

	}

	/**
	 * Prints the game in a graphical interface.
	 * @param g The game to print.
	 * @param graphs Graphics object to print on.
	 * @param pictures The sprites used to print the game.
	 * @param size The size of the panel the game's being printed on
	 */
	public static void printGame(Game g, Graphics graphs, MazePictures pictures, Dimension size) {

		Maze m = g.getMaze();
		Hero h = g.getHero();
		ArrayList<Dragon> d = g.getDragons();
		Sword s = g.getSword();
		Eagle e = g.getEagle();

		BufferedImage[][] mazePositions = getMazePictures(m, pictures);

		// dragon printing
		for(int i = 0; i < d.size(); i++) {
			if(d.get(i).getState() == Dragon.ALIVE && d.get(i).getHasSword())
				mazePositions[d.get(i).getRow()][d.get(i).getColumn()] = pictures.guardedSword;
			else if(d.get(i).getState() == Dragon.ALIVE && !d.get(i).getHasSword())
				mazePositions[d.get(i).getRow()][d.get(i).getColumn()] = pictures.dragon;
			else if(d.get(i).getState () == Dragon.ASLEEP && d.get(i).getHasSword())
				mazePositions[d.get(i).getRow()][d.get(i).getColumn()] = pictures.sleepingGuardedSword;
			else if(d.get(i).getState() == Dragon.ASLEEP && !d.get(i).getHasSword())
				mazePositions[d.get(i).getRow()][d.get(i).getColumn()] = pictures.sleepingDragon;
			else if(d.get(i).getState() == Dragon.DEAD)
				mazePositions[d.get(i).getRow()][d.get(i).getColumn()] = pictures.deadDragon;
		}
		
		if(!s.isTaken() && s.print)
			mazePositions[s.getRow()][s.getColumn()] = pictures.sword;

		// hero printing
		if(h.print)
			if(h.getState() == Hero.ARMED || h.getState() == Hero.EXITED_MAZE)
				mazePositions[h.getRow()][h.getColumn()] = pictures.armedHero;
			else if (h.getState() == Hero.DEAD)
				mazePositions[h.getRow()][h.getColumn()] = pictures.deadHero;
			else
				mazePositions[h.getRow()][h.getColumn()] = pictures.hero;

		// eagle printing
		if ( !e.isWithHero() && e.getState() != Eagle.DEAD){
			if(g.checkIfEagle(h.getRow(), h.getColumn()))
				mazePositions[ e.getRow() ][e.getColumn() ] = pictures.eagleOnHero;
			else if(g.checkIfOnAwakeDragon(e.getRow(), e.getColumn()))
				mazePositions[ e.getRow() ][e.getColumn() ] = pictures.eagleOnDragon;
			else if(g.checkIfOnSleepingDragon(e.getRow(), e.getColumn()))
				mazePositions[ e.getRow() ][e.getColumn() ] = pictures.eagleOnSleepingDragon;
			else if(g.checkIfSword(e.getRow(), e.getColumn()))
				mazePositions[ e.getRow() ][e.getColumn() ] = pictures.eagleWithSword;
			else if(m.checkIfWall(e.getRow(), e.getColumn()))
				mazePositions[ e.getRow() ][e.getColumn() ] = pictures.eagleOnWall;
			else if(m.checkIfEmpty(e.getRow(), e.getColumn()))
				mazePositions[ e.getRow() ][e.getColumn() ] = pictures.eagle;
		}
		
		double xScale =  size.getWidth() / ( mazePositions[0].length * GUInterface.SPRITESIZE );
		double yScale =  size.getHeight() / ( mazePositions.length * GUInterface.SPRITESIZE );

		for (int x = 0; x < m.getRows(); x++) {
			for (int y = 0; y < m.getColumns(); y++) {
				
				BufferedImage originalSprite = mazePositions[x][y];
				
				Graphics2D graphsTo2D = (Graphics2D)graphs;
				
		        int newW = (int)(originalSprite.getWidth() * xScale);
		        int newH = (int)(originalSprite.getHeight() * yScale);
		        
		        int dx = (int) size.getWidth() - ( mazePositions[0].length * newW );
		        dx = dx/2;
		        
		        int dy = (int) size.getHeight() - ( mazePositions.length * newH );
		        dy = dy/2;
		        
		        graphsTo2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		        graphsTo2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		        graphsTo2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		        
		        graphsTo2D.drawImage(originalSprite, y * newW + dx, x*newH + dy, newW, newH, null);
			}
		}
	}

	public static void printAskForMove() {
		System.out.print(Messages.getString("GameOutput.6")); //$NON-NLS-1$
	}

	/**
	 * Prints the events on the event queue given
	 * @param events The game events
	 */
	public static void printEventQueue(LinkedList<GameEvent> events) {
		while(!events.isEmpty())
			printEvent(events.removeFirst());
	}

	/**
	 * Prints the events on the event queue given to the infopanel
	 * @param events The game events
	 * @param infoPanel The infoPanel element to write to
	 */
	public static void printEventQueue(LinkedList<GameEvent> events, InfoPanel infoPanel) {
		String info = new String();

		while(!events.isEmpty())
			info += (events.removeFirst().getMessage()) + '\n';

		infoPanel.textPane.setText(info);
	}

	/**
	 * Prints the message field of the GameEvent given
	 * @param ev The game event
	 */
	public static void printEvent(GameEvent ev) {
		System.out.println(ev.getMessage());
	}

	/**
	 * Prints a welcome message for the command line interface.
	 */
	public static void printStartMessage() {
		System.out.println(Messages.getString("GameOutput.7")); //$NON-NLS-1$
	}

	/**
	 * Asks player for the maze options wanted. n = 0 to ask for maze type, n = 1 to ask for rows, n = 2 to ask for columns
	 * @param n The question
	 */
	public static void printOptions(int n) { 

		switch(n) {
		case 0:
			System.out.println(Messages.getString("GameOutput.8")); //$NON-NLS-1$
			System.out.print(PROMPT);
			break;
		case 1:
			System.out.println(Messages.getString("GameOutput.9")); //$NON-NLS-1$
			System.out.println(Messages.getString("GameOutput.10")); //$NON-NLS-1$
			System.out.print(PROMPT);
			break;
		case 2:
			System.out.println(Messages.getString("GameOutput.11")); //$NON-NLS-1$
			System.out.print(PROMPT);
			break;
		}

	}

	public static void printMazeSizeError() {
		System.out.println(Messages.getString("GameOutput.12")); //$NON-NLS-1$
	}

	public static void printDragonOptions() { //Prints dragon type related options
		System.out.println(Messages.getString("GameOutput.13")); //$NON-NLS-1$
		System.out.println(Messages.getString("GameOutput.14")); //$NON-NLS-1$
		System.out.println(Messages.getString("GameOutput.15")); //$NON-NLS-1$
		System.out.print(PROMPT);
	}

	public static void printMultipleDragonOptions() {
		System.out.println(Messages.getString("GameOutput.16")); //$NON-NLS-1$
		System.out.println(Messages.getString("GameOutput.17")); //$NON-NLS-1$
		System.out.print(PROMPT);
	}

	public static void clearScreen(){
		for(int i = 0; i < 100; i++)
			System.out.println();
	}

	/**
	 * Saves a given game to a file.
	 * @param game The game.
	 * @param file The specified file.
	 */
	public static void save(Game game, File file){
		try
		{
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(game);
			oos.close();
		}
		catch (Exception ex)
		{
			fail(Messages.getString("GameOutput.18") + ex.toString()); //$NON-NLS-1$
		}
	}

	/**
	 * Load a game from a specified file.
	 * @param file The file.
	 * @return The loaded game.
	 */
	public static Game load(File file){
		try
		{
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Game game = (Game) ois.readObject();
			ois.close();
			return game;
		}
		catch (Exception ex)
		{
			fail(Messages.getString("GameOutput.19") + ex.toString()); //$NON-NLS-1$
		}
		return null;
	}
}
