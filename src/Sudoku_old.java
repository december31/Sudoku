import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Sudoku_old {
	private static final int GRID_SIZE = 9;
	private int[][] board = new int[GRID_SIZE][GRID_SIZE];
	private int[][] currentPlay = new int[GRID_SIZE][GRID_SIZE];
	private static Scanner scanner;

	Sudoku_old() {
		try {
			scanner = new Scanner(new FileReader("data.txt"));
			
			for(int i = 0; i < GRID_SIZE; i++) {
				for(int j = 0; j < GRID_SIZE; j++) {
					board[i][j] = scanner.nextInt();
					currentPlay[i][j] = board[i][j];
				}
			}

			// generate answer for sudoku game
			final int[][] ANSWER = answer();
			if(ANSWER == null) {
				System.out.println("this game does not has solution :(");
				return;
			}
		
			int choice = 0;
			scanner.close();
			scanner = new Scanner(System.in);
			do {
				System.out.println(" ------ SUDOKU ------");
				printBoard(currentPlay);
				System.out.println("1 - continue playing");
				System.out.println("2 - test");
				System.out.println("3 - display answer");
				System.out.println("0 - exit game :(");
				System.out.print("enter function: ");
				choice = scanner.nextInt();
				switch (choice) {
					case 1: {
						int x = 0, y = 0;
						while(true) {
							System.out.print("enter x coordinate: ");
							x = scanner.nextInt();
							
							System.out.print("enter y coordinate: ");
							y = scanner.nextInt();							

							if(x <= 0){
								System.out.println("coordinate must be greater than 0!\nLet enter again:\n");
								continue;
							} else if(y <= 0) {
								System.out.println("coordinate must be greater than 0!\nLet enter again:\n");
								continue;
							}
							
							if(board[x - 1][y - 1] != 0) {
								System.out.println("this place already had a value!!!");
								continue;
							}
							break;
						}

						// enter an value for square which user chose
						System.out.print("enter an integer in range [0,9]: ");
						int num = scanner.nextInt();
						while (num < 0 || 9 < num) {
							System.out.print("please enter an integer in range [0,9]: ");
							num = scanner.nextInt();
						}

						currentPlay[x - 1][y - 1] = num;
						break;
					}
					
					case 2: {
						System.out.println("square coordinates has wrong value: ");
						int fl1 = 0;
						int fl2 = 0;
						for(int i = 0; i < GRID_SIZE; i++) {
							for(int j = 0; j < GRID_SIZE; j++) {
								// if the square is not entered any value then continue
								if(currentPlay[i][j] == 0) {
									fl1++;
									continue;
								}

								// if the square value is not equals to answer at the same coordinates then display this coordinates and set value of this square to default (0)
								if(currentPlay[i][j] != ANSWER[i][j]) {
									System.out.println("(" + (i + 1) + ", " + (j + 1) + ")");
									currentPlay[i][j] = 0;
									fl2++;
								}
							}
						}

						if(fl1 == 0 && fl2 == 0) {
							System.out.println("you won".toUpperCase());
						}
						break;
					}

					case 3: {
						System.out.println("\nANSWER");
						printBoard(ANSWER);
						break;
					}
					default:
						break;
				}
				System.out.print("\npress any key to continue =>>> ");
				scanner.nextLine();
				scanner.nextLine();
				clearScreen();

			} while(choice != 0);
			scanner.close();

		} catch (FileNotFoundException e) {
			System.err.println("input file not found :(");
			e.printStackTrace();
		}
	}

	public static void clearScreen() {  
		System.out.print("\033[H\033[2J");  
		System.out.flush();
	}  

	private static void printBoard(int[][] board) {
		for(int i = 0; i < GRID_SIZE; i++) {
			if(i % 3 == 0 && i != 0) System.out.println("------+-------+------");
			for(int j = 0; j < GRID_SIZE; j++) {
				if(j % 3 == 0 && j != 0) System.out.print("| ");
				if(board[i][j] == 0) {
					System.out.print("- ");
				} else {
					System.out.print(board[i][j] + " ");
				}
			}
			System.out.println();
		}
	}

	private static boolean isNumberInRow(int[][] board, int number, int row) {
		for(int i = 0; i < GRID_SIZE; i++)
			if(board[row][i] == number)
				return true;
		return false;
	}

	private static boolean isNumberInColumn(int[][] board, int number, int column) {
		for(int i = 0; i < GRID_SIZE; i++)
			if(board[i][column] == number)
				return true;
		return false;
	}

	private static boolean isNumberInbox(int[][] board, int number, int row, int column) {
		int localBoxRow = row - row % 3 + 3;
		int localBoxColumn = column - column % 3 + 3;

		for(int i = localBoxRow- 3; i < localBoxRow; i++)
			for(int j = localBoxColumn - 3; j < localBoxColumn; j++)
				if(number == board[i][j])
					return true;
		return false;
	}

	private static boolean isValidPlace(int[][] board, int number, int row, int column) {
		return !isNumberInRow(board, number, row) &&
			!isNumberInColumn(board, number, column) &&
			!isNumberInbox(board, number, row, column);
	}

	private static boolean sudokuSolver(int[][] board) {
		// iterate thought all square in board
		for(int row = 0; row < GRID_SIZE; row++) {
			for(int column = 0; column < GRID_SIZE; column++) {
		
				// if square has not have a value yet
				if(board[row][column] == 0) {
					for(int number = 1; number <= GRID_SIZE; number++) {
						if(isValidPlace(board, number, row, column)) {
							board[row][column] = number;
		
							// if all the after square has it's own value then turn true
							if(sudokuSolver(board)) {
								return true;
							} else {	// if the square after this square have not any value to be validated then set this value to default (0)
								board[row][column] = 0;
							}
						}
					}
		
					// if this square have not any value to be validated then return false
					return false;
				}
			}
		}
		return true;
	}

	private int[][] answer() {
		int [][] answer = new int[GRID_SIZE][GRID_SIZE];
		
		// copy value from board to answer
		for(int i = 0; i < GRID_SIZE; i++)
			for(int j = 0; j < GRID_SIZE; j++)
				answer[i][j] = board[i][j];

		if(sudokuSolver(answer) == true) {
			return answer;
		} else {
			return null;
		}
	}
}
