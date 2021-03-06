package game.ui;

import java.util.Scanner;

import game.ui.GameOutput;

/**
 * The Class GameInput is mainly useful for the command line interface
 */
public class GameInput {

	/**
	 * Receives user input for maze size.
	 * @param size the size
	 * @return If user wants predefined maze, return false, true otherwise
	 */
	public static boolean receiveMazeOptions(int size[]) { 

		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		
		char c = 'a';


		do {
			GameOutput.printOptions(0);
			String s = in.nextLine();
			if(!s.isEmpty())
				c = s.charAt(0);
		}
		while(c != 'Y' && c != 'y' && c != 'N' && c != 'n');

		if(c == 'Y' || c == 'y') {
			GameOutput.printOptions(1);
			size[0] = in.nextInt();

			GameOutput.printOptions(2);
			size[1] = in.nextInt();

			return true;
		}

		return false;

	}
	
	public static int receiveDragonOptions() {
		
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		
		int dragonType;
		
		do {
			GameOutput.printDragonOptions();
			
			String s = in.nextLine();
			dragonType = -1;
			try{
			    dragonType = Integer.parseInt(s);
			}
			catch(NumberFormatException ex){}
		}
		while(dragonType != 0 && dragonType != 1 && dragonType != 2);
		
		return dragonType;
	}
	
	public static boolean receiveMultipleDragonOptions() {
		
		boolean spawnMultiple;
		
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		
		char c = 'a';

		do {
			GameOutput.printMultipleDragonOptions();
			String s = in.nextLine();
			if(!s.isEmpty())
				c = s.charAt(0);
		}
		while(c != 'Y' && c != 'y' && c != 'N' && c != 'n');
		
		if(c == 'Y' || c == 'y')
			spawnMultiple = true;
		else
			spawnMultiple = false;
		
		return spawnMultiple;
	}

}
