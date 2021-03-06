package game.ui;

import game.objects.Dragon;

import java.util.ArrayList;


/**
 * The Class GameOptions holds the various settings necessary to initialize a game
 */
public class GameOptions implements java.io.Serializable {

	private static final long serialVersionUID = 5808918369509311374L;
	
	public int rows;
	public int columns;

	public boolean randomMaze;

	public int dragonType;

	public boolean multipleDragons;

	/*** Spawning Parameters ***/
	public boolean randomSpawns;

	public int heroRow;
	public int heroColumn;

	public int swordRow;
	public int swordColumn;

	public ArrayList<Dragon> dragons;

	private void askOptions() {

		int size[] = {rows, columns};

		//Get Maze options from user
		GameOutput.printStartMessage();
		randomMaze = GameInput.receiveMazeOptions(size);

		rows = size[0];
		columns = size[1];

		//Get Dragon options from user
		dragonType = GameInput.receiveDragonOptions();

		//Get Multiple dragon options
		multipleDragons = GameInput.receiveMultipleDragonOptions();
		
		randomSpawns = true;

	}

	public GameOptions() {
		askOptions();
	}
	
	public GameOptions(boolean askOptions) {
		if(askOptions)
			askOptions();
	}

	/**
	 * Constructor for a random maze.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 * @param dragonType the dragon type
	 * @param multipleDragons if there are multiple dragons
	 * @param heroRow the hero row
	 * @param heroColumn the hero column
	 * @param swordRow the sword row
	 * @param swordColumn the sword column
	 * @param dragons the dragons
	 */
	public GameOptions(int rows, int columns, int dragonType, boolean multipleDragons, int heroRow, int heroColumn,
			int swordRow, int swordColumn, ArrayList<Dragon> dragons) { 

		this.rows = rows;
		this.columns = columns;
		randomMaze = true;

		this.dragonType = dragonType;

		this.multipleDragons = multipleDragons;

		randomSpawns = false;

		this.heroRow = heroRow;
		this.heroColumn = heroColumn;

		this.swordRow = swordRow;
		this.swordColumn = swordColumn;

		this.dragons = dragons;

	}

	/**
	 * Constructor for a predefined maze
	 *
	 * @param dragonType the dragon type
	 * @param multipleDragons if there are multiple dragons
	 * @param heroRow the hero row
	 * @param heroColumn the hero column
	 * @param swordRow the sword row
	 * @param swordColumn the sword column
	 * @param dragons the dragons
	 */
	public GameOptions(int dragonType, boolean multipleDragons, int heroRow, int heroColumn,
			int swordRow, int swordColumn, ArrayList<Dragon> dragons) {

		this.rows = 0;
		this.columns = 0;
		randomMaze = false;

		this.dragonType = dragonType;

		this.multipleDragons = multipleDragons;

		randomSpawns = false;

		this.heroRow = heroRow;
		this.heroColumn = heroColumn;

		this.swordRow = swordRow;
		this.swordColumn = swordColumn;

		this.dragons = dragons;
	}


}
