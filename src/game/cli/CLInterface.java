package game.cli;

import game.logic.Game;
import game.ui.GameInterface;
import game.ui.GameOptions;
import game.ui.GameOutput;
import general_utilities.MazeInput;
import general_utilities.WaitTime;

public class CLInterface extends GameInterface {

	public void startGame() {

		GameOptions options = new GameOptions();
		game = new Game(options);
		GameOutput.clearScreen();          
		GameOutput.printGame(game);
		mainLoop();

	}

	private void mainLoop() {

		boolean goOn = true;
		char input = ' ';

		while(goOn){

			try {
				GameOutput.printAskForMove();
				input = MazeInput.getChar();
			}
			catch(Exception e) {
				System.err.println("Problem reading user input!");
			}

			goOn = game.heroTurn(input);

			GameOutput.clearScreen();
			GameOutput.printGame(game);
			WaitTime.wait(250);

			goOn = game.dragonTurn(goOn);
			GameOutput.clearScreen();
			GameOutput.printGame(game);
			WaitTime.wait(250);

			goOn = game.checkState(goOn);

			GameOutput.clearScreen();
			GameOutput.printEventQueue(game.getEvents() );
			GameOutput.printGame(game);
			WaitTime.wait(250);

		}
	}

}
