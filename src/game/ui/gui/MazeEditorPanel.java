package game.ui.gui;

import game.logic.Game;
import game.maze.Maze;
import game.maze.Tile;
import game.objects.Dragon;
import game.objects.Hero;
import game.objects.Movable;
import game.objects.Sword;
import game.ui.GameOptions;
import game.ui.GameOutput;
import game.ui.MazeInput;
import game.ui.MazePictures;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;


/**
 * The Class MazeEditorPanel, extending JDialog, implements a dialog that allows the user to create
 * a custom maze, positioning tiles selected from a toolbar, using the mouse.
 */
public class MazeEditorPanel extends JDialog {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -981182364507201188L;

	/** The current object selected. */
	public MazeObjectToDraw currentObject = new MazeObjectToDraw();
	
	/** The game that is being created. */
	public Game game;
	
	/** The game options specified by the user. */
	public GameOptions options = new GameOptions(false);
	
	/** The image files to be used in the maze drawing. */
	public MazePictures pictures;

	/** The newDragon boolean indicates if the user clicked on a new dragon or is still placing
	 * a previous one.
	 */
	public boolean newDragon; 

	/** Tracks the number of exits currently placed on the maze. */
	public int numberOfExits = 0;
	
	/** This boolean indicates if there's a hero on the maze. */
	public boolean createdHero;
	
	/** This boolean indicates if there's a sword on the maze. */
	public boolean createdSword;

	/** The current maze number of rows. */
	private int maze_rows;
	
	/** The current maze number of columns. */
	private int maze_columns;
	
	/** The dragon type that's being placed on the maze. */
	private int dragonType;

	/**
	 * Instantiates a new maze editor panel.
	 *
	 * @param parent the parent frame
	 * @param game the game variable that's going to be used
	 * @param pictures the images to be used on the game drawing
	 */
	public MazeEditorPanel(Frame parent, final Game game, MazePictures pictures) {
		super(parent, Messages.getString("MazeEditorPanel.0"), true); //$NON-NLS-1$
		setLayout(new GridBagLayout());
		this.game = game;
		this.pictures = pictures;

		createMenuBar();

		createToolBar();

		if(askNewGameOptions() == 1)
			return;

		initializeNewGame();

		MazePainterPanel mazePainter = new MazePainterPanel(this);
		
		GridBagConstraints mazePainter_constraints = new GridBagConstraints();

		mazePainter_constraints.weightx = 1;
		mazePainter_constraints.weighty = 1;
		mazePainter_constraints.gridx = 0;
		mazePainter_constraints.gridy = 1;
		mazePainter_constraints.fill = GridBagConstraints.BOTH;

		getContentPane().add(mazePainter, mazePainter_constraints);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(true);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Initializes a new game with the specified options.
	 */
	private void initializeNewGame() {
		this.game = new Game(options);
		this.game.setMaze(new Maze(options.rows, options.columns, true));
		this.game.getHero().print = false;
		this.game.getSword().print = false;
	}

	/**
	 * Asks for the options to use on the new game being created.
	 *
	 * @return 0 on success, 1 otherwise
	 */
	private int askNewGameOptions() {
		String rows;
		String columns;

		do {
			rows = JOptionPane.showInputDialog(this, Messages.getString("MazeEditorPanel.1")); //$NON-NLS-1$
		}
		while(!MazeInput.isInteger(rows) && rows != null);

		if(rows == null)
			return 1;

		do {
			columns = JOptionPane.showInputDialog(this, Messages.getString("MazeEditorPanel.2")); //$NON-NLS-1$
		}
		while(!MazeInput.isInteger(columns) && columns != null);

		if(columns == null)
			return 1;

		if(Integer.parseInt(rows)  < 6 || Integer.parseInt(columns) < 6
				|| Integer.parseInt(columns) > 500 || Integer.parseInt(columns) > 500) {
			JOptionPane.showMessageDialog(this,
					Messages.getString("MazeEditorPanel.3"), //$NON-NLS-1$
					Messages.getString("MazeEditorPanel.4"), //$NON-NLS-1$
					JOptionPane.ERROR_MESSAGE);

			maze_rows = 10;
			maze_columns = 10;
		}
		else {
			maze_rows = Integer.parseInt(rows);
			maze_columns = Integer.parseInt(columns);
		}

		String[] possibilities = {Messages.getString("MazeEditorPanel.5"), Messages.getString("MazeEditorPanel.6"), Messages.getString("MazeEditorPanel.7")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String dragonOption = (String)JOptionPane.showInputDialog(
				this,
				Messages.getString("MazeEditorPanel.8"), //$NON-NLS-1$
				Messages.getString("MazeEditorPanel.9"), //$NON-NLS-1$
				JOptionPane.QUESTION_MESSAGE,
				null,
				possibilities,
				possibilities[0]);

		if(dragonOption == null)
			return 1;
		if(dragonOption.equals( Messages.getString("MazeEditorPanel.10") )) //$NON-NLS-1$
			dragonType = Dragon.SLEEPING;
		else if(dragonOption.equals( Messages.getString("MazeEditorPanel.11") )) //$NON-NLS-1$
			dragonType = Dragon.NORMAL;
		else
			dragonType = Dragon.STATIC;

		updateOptions();
		return 0;
	}

	/**
	 * Creates the tool bar with the game elements.
	 */
	private void createToolBar() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setLayout(new FlowLayout(FlowLayout.CENTER));

		JButton btnFloor = new JButton(""); //$NON-NLS-1$
		btnFloor.setIcon(new ImageIcon(InfoPanel.class.getResource("/images/empty.png"))); //$NON-NLS-1$
		toolBar.add(btnFloor);

		btnFloor.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				currentObject.set(Tile.empty);
			}
		});

		JButton btnWall = new JButton(""); //$NON-NLS-1$
		btnWall.setIcon(new ImageIcon(InfoPanel.class.getResource("/images/wall.png"))); //$NON-NLS-1$
		toolBar.add(btnWall);

		btnWall.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				currentObject.set(Tile.wall);
			}
		});

		JButton btnExit = new JButton(""); //$NON-NLS-1$
		btnExit.setIcon(new ImageIcon(InfoPanel.class.getResource("/images/exit.png"))); //$NON-NLS-1$
		toolBar.add(btnExit);

		btnExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				currentObject.set(Tile.exit);
			}
		});

		JButton btnDragon = new JButton(""); //$NON-NLS-1$
		btnDragon.setIcon(new ImageIcon(InfoPanel.class.getResource("/images/dragon.png"))); //$NON-NLS-1$
		toolBar.add(btnDragon);

		btnDragon.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				currentObject.set(new Dragon(0, 0, options.dragonType));
				newDragon = true;
			}
		});

		JButton btnSword = new JButton(""); //$NON-NLS-1$
		btnSword.setIcon(new ImageIcon(InfoPanel.class.getResource("/images/sword.png"))); //$NON-NLS-1$
		toolBar.add(btnSword);

		btnSword.addActionListener(new SetSword());

		JButton btnHero = new JButton(""); //$NON-NLS-1$
		btnHero.setIcon(new ImageIcon(InfoPanel.class.getResource("/images/hero.png"))); //$NON-NLS-1$
		toolBar.add(btnHero);

		btnHero.addActionListener(new SetHero());
		
		GridBagConstraints toolBar_constraints = new GridBagConstraints();

		toolBar_constraints.weightx = 1;
		toolBar_constraints.weighty = 1;
		toolBar_constraints.gridx = 0;
		toolBar_constraints.gridy = 0;
		toolBar_constraints.fill = GridBagConstraints.BOTH;
		toolBar_constraints.anchor = GridBagConstraints.PAGE_START;
		
		getContentPane().add(toolBar, toolBar_constraints);
	}

	/**
	 * Creates the menu bar for the dialog with a File and a Help menu.
	 */
	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();


		JMenu fileMenu = new JMenu(Messages.getString("MazeEditorPanel.24")); //$NON-NLS-1$
		fileMenu.setMnemonic(KeyEvent.VK_F);
		fileMenu.getAccessibleContext().setAccessibleDescription(
				Messages.getString("MazeEditorPanel.25")); //$NON-NLS-1$
		menuBar.add(fileMenu);

		JMenuItem saveGameMenuItem = new JMenuItem(Messages.getString("MazeEditorPanel.26"), KeyEvent.VK_S); //$NON-NLS-1$
		saveGameMenuItem.getAccessibleContext().setAccessibleDescription(
				Messages.getString("MazeEditorPanel.27")); //$NON-NLS-1$
		fileMenu.add(saveGameMenuItem);

		saveGameMenuItem.addActionListener(new SaveMaze());
		
		JMenuItem exitGameMenuItem = new JMenuItem(Messages.getString("MazeEditorPanel.28"), //$NON-NLS-1$
				KeyEvent.VK_E);
		exitGameMenuItem.getAccessibleContext().setAccessibleDescription(
				Messages.getString("MazeEditorPanel.29")); //$NON-NLS-1$
		fileMenu.add(exitGameMenuItem);

		exitGameMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				int option = JOptionPane.showConfirmDialog(
						MazeEditorPanel.this,
						Messages.getString("MazeEditorPanel.30"), //$NON-NLS-1$
						Messages.getString("MazeEditorPanel.31"), //$NON-NLS-1$
						JOptionPane.YES_NO_OPTION);
				if(option == JOptionPane.YES_OPTION)
					dispose();
			}
		});
		
		JMenu helpMenu = new JMenu(Messages.getString("MazeEditorPanel.32")); //$NON-NLS-1$
		helpMenu.setMnemonic(KeyEvent.VK_H);
		helpMenu.getAccessibleContext().setAccessibleDescription(
				Messages.getString("MazeEditorPanel.33")); //$NON-NLS-1$
		menuBar.add(helpMenu);

		JMenuItem keysHelpMenuItem = new JMenuItem(Messages.getString("MazeEditorPanel.34"), KeyEvent.VK_H); //$NON-NLS-1$
		keysHelpMenuItem.getAccessibleContext().setAccessibleDescription(
				Messages.getString("MazeEditorPanel.35")); //$NON-NLS-1$
		helpMenu.add(keysHelpMenuItem);

		keysHelpMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MazeEditorPanel.this, Messages.getString("MazeEditorPanel.36") + //$NON-NLS-1$
						Messages.getString("MazeEditorPanel.37") + //$NON-NLS-1$
						Messages.getString("MazeEditorPanel.38") + //$NON-NLS-1$
						Messages.getString("MazeEditorPanel.39") + //$NON-NLS-1$
						Messages.getString("MazeEditorPanel.40"), //$NON-NLS-1$
								Messages.getString("MazeEditorPanel.41"), //$NON-NLS-1$
								JOptionPane.PLAIN_MESSAGE);
			}
		});

		setJMenuBar(menuBar);
	}

	/**
	 * Updates the game options with the user given parameters.
	 */
	private void updateOptions() {

		options.randomMaze = true;
		options.rows = maze_rows;
		options.columns = maze_columns;

		options.dragonType = dragonType;

		options.multipleDragons = true;

		options.randomSpawns = false;
	}

	/**
	 * This class implements an ActionListen whose action is to set the currentObject variable to the
	 * game's hero.
	 */
	class SetHero implements ActionListener {

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			currentObject.set(game.getHero());
		}

	}

	/**
	 * This class implements an ActionListen whose action is to set the currentObject variable to the
	 * game's sword.
	 */
	class SetSword implements ActionListener {

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			currentObject.set(game.getSword());
		}

	}

	/**
	 * This class implements an ActionListener whose action is to call a save game dialog
	 * if the required conditions are met.
	 * It shows instead a specific error message if there is not at least one exit on the map,
	 * if the hero has not been placed yet or if the sword has not been placed either.
	 */
	class SaveMaze implements ActionListener {

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			if(numberOfExits == 0) {
				JOptionPane.showMessageDialog(MazeEditorPanel.this,
						Messages.getString("MazeEditorPanel.42"), //$NON-NLS-1$
						Messages.getString("MazeEditorPanel.43"), //$NON-NLS-1$
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			if(!createdHero) {
				JOptionPane.showMessageDialog(MazeEditorPanel.this,
						Messages.getString("MazeEditorPanel.44"), //$NON-NLS-1$
						Messages.getString("MazeEditorPanel.45"), //$NON-NLS-1$
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			if(!createdSword) {
				JOptionPane.showMessageDialog(MazeEditorPanel.this,
						Messages.getString("MazeEditorPanel.46"), //$NON-NLS-1$
						Messages.getString("MazeEditorPanel.47"), //$NON-NLS-1$
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			GameOutput.showSaveGameDialog(game);
		}

	}
}

/**
 * The Class MazePainterPanel, extending JPanel, implements a MouseListener to respond to user clicks
 * by interpreting the clicked tile and changing it accordingly to the element currently selected in the 
 * element toolbar.
 */
class MazePainterPanel extends JPanel implements MouseListener {

	private static final long serialVersionUID = -5533602405577612408L;

	/** The parent MazeEditorPanel. */
	MazeEditorPanel parent;

	/** An array containing the current Movable objects placed on the maze. */
	private ArrayList<Movable> movableObjects = new ArrayList<Movable>();

	/**
	 * Instantiates a new MazePainterPanel, setting up the needed configurations and the size options
	 * according to the size of the maze to be created.
	 *
	 * @param parent the parent MazeEditorPanel
	 */
	public MazePainterPanel(MazeEditorPanel parent) {
		
		
		this.parent = parent;

		setFocusable(true);
		addMouseListener(this);

		Dimension defaultPreferred = GUInterface.getFormattedPreferredDimension(new Dimension(parent.options.columns * GUInterface.SPRITESIZE,
				parent.options.rows * GUInterface.SPRITESIZE), GUInterface.MAXIMUM_WINDOW_SIZE);

		setMaximumSize(GUInterface.MAXIMUM_WINDOW_SIZE);
		setMinimumSize(defaultPreferred);
		setPreferredSize(defaultPreferred);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Dimension size = this.getSize();
		GameOutput.printGame(parent.game, g, parent.pictures, size);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		return;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		return;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		return;

	}

	@Override
	public void mousePressed(MouseEvent e) {
		double currentXScale =  this.getSize().getWidth() / ( parent.game.getMaze().getColumns() * GUInterface.SPRITESIZE );
		double currentYScale =  this.getSize().getHeight() / ( parent.game.getMaze().getRows() * GUInterface.SPRITESIZE );
		
		int currentW = (int)(GUInterface.SPRITESIZE * currentXScale);
        int currentH = (int)(GUInterface.SPRITESIZE * currentYScale);
        
        int dx = (int)  this.getSize().getWidth() - ( parent.game.getMaze().getColumns() * currentW );
        dx = dx/2;
        
        int dy = (int)  this.getSize().getHeight() - ( parent.game.getMaze().getRows() * currentH );
        dy = dy/2;
		
		int maxRow = parent.game.getMaze().getRows() - 1;
		int maxCol = parent.game.getMaze().getColumns() - 1;
        
        
		int printRow = (e.getY() - dy) / currentH;
		
		if(printRow > maxRow || printRow < 0)
			return;
		
		int printColumn = (e.getX() - dx) / currentW;
		
		if(printColumn > maxCol || printColumn < 0)
			return;

		if( checkIfAtCorner(printRow, printColumn)
				|| ( checkIfAtMargin(printRow, printColumn) && checkIfNotWallOrExitTile() ) )
			return;

		if(parent.game.getMaze().getPositions()[printRow][printColumn] == Tile.exit)
			parent.numberOfExits--;

		deleteObjectOn(printRow, printColumn);

		if(parent.currentObject.isMovable) {
			movableObjects.remove(parent.currentObject.movable);

			if( (parent.currentObject.movable instanceof Dragon) && !parent.newDragon )
				parent.game.removeDragon((Dragon) parent.currentObject.movable);

			parent.currentObject.movable.setRow(printRow);
			parent.currentObject.movable.setColumn(printColumn);
			parent.currentObject.movable.print = true;
			movableObjects.add(parent.currentObject.movable);

			if(parent.currentObject.movable instanceof Dragon) {
				parent.game.addDragon((Dragon) parent.currentObject.movable);
				parent.newDragon = false;
			}
		}
		else {
			parent.game.getMaze().getPositions()[printRow][printColumn] = parent.currentObject.tile;
			if(parent.currentObject.tile == Tile.exit)
				parent.numberOfExits++;
		}

		checkIfCreatedHeroAndSword();

		repaint();
	}

	private boolean checkIfNotWallOrExitTile() {
		return parent.currentObject.isMovable
				|| ( parent.currentObject.tile != Tile.exit
				&& parent.currentObject.tile != Tile.wall );
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		return;
	}

	/**
	 * Deletes the object on the given row and column, updating any game related variables
	 * or maze editor variables.
	 *
	 * @param row the row of the object
	 * @param column the column of the object
	 */
	private void deleteObjectOn(int row, int column) {

		parent.game.getMaze().getPositions()[row][column] = Tile.empty;

		Iterator<Movable> iter = movableObjects.iterator(); 

		if(movableObjects != null)
			while(iter.hasNext()) {
				Movable movable = iter.next();

				if(movable.getRow() == row && movable.getColumn() == column) {

					if(movable instanceof Dragon) {
						Iterator<Dragon> dragonIter = parent.game.getDragons().iterator(); 

						while(dragonIter.hasNext()) {
							Dragon dragon = dragonIter.next();

							if(dragon.getRow() == row && dragon.getColumn() == column) {
								boolean dragonWasDead;

								if(dragon.getState() == Dragon.DEAD)
									dragonWasDead = true;
								else
									dragonWasDead = false;

								dragonIter.remove();

								parent.game.removeDragon(dragonWasDead);
							}
						}
					}

					movable.setRow(0);
					movable.setColumn(0);
					movable.print = false;
					iter.remove();
				}
			}

	}

	private void checkIfCreatedHeroAndSword() {
		parent.createdHero = false;
		parent.createdSword = false;

		for(Movable movable : movableObjects) {
			if(movable instanceof Hero)
				parent.createdHero = true;
			else if(movable instanceof Sword)
				parent.createdSword = true;
		}
	}

	private boolean checkIfAtMargin(int printRow, int printColumn) {
		return printRow <= 0 || printRow >= (parent.options.rows - 1) || printColumn <= 0 || printColumn >= (parent.options.columns - 1);
	}

	private boolean checkIfAtCorner(int printRow, int printColumn) {
		if(printRow == 0)
			if(printColumn == 0 || printColumn == (parent.options.columns - 1))
				return true;

		if(printRow == (parent.options.rows - 1))
			if(printColumn == 0 || printColumn == (parent.options.columns - 1))
				return true;

		return false;
	}

}
