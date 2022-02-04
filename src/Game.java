import java.util.Scanner;

public class Game {
	private static final int GRID_SIZE = 9;
	Node[][] currentPlay = new Node[GRID_SIZE][GRID_SIZE];
	Node[][] board = new Node[GRID_SIZE][GRID_SIZE];;

	Game(Node[][] _board) {
		for(int i = 0; i < GRID_SIZE; i++) {
			for(int j = 0; j < GRID_SIZE; j++) {
				this.board[i][j] = new Node(i,j,_board[i][j].getValue());
				this.currentPlay[i][j] = new Node(i,j,_board[i][j].getValue());
			}
		}

		// generate answer for sudoku game
		final Node[][] ANSWER = answer();
		if(ANSWER == null) {
			System.out.println("this game does not has solution :(");
			return;
		}

		Scanner scanner = new Scanner(System.in);
		int choice = 0;
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
						System.out.print("enter x corrdinate: ");
						x = scanner.nextInt();
						
						System.out.print("enter y corrdinate: ");
						y = scanner.nextInt();							

						if(x <= 0){
							System.out.println("cordinate must be greater than 0!\nLet enter again:\n");
							continue;
						} else if(y <= 0) {
							System.out.println("cordinate must be greater than 0!\nLet enter again:\n");
							continue;
						}
						
						if(board[x - 1][y - 1].getValue() != 0) {
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

					currentPlay[x - 1][y - 1].setValue(num);
					break;
				}
				
				case 2: {
					System.out.println("square corrdinates has wrong value: ");
					int fl1 = 0;
					int fl2 = 0;
					for(int i = 0; i < GRID_SIZE; i++) {
						for(int j = 0; j < GRID_SIZE; j++) {
							// if the square is not entered any value then continue
							if(currentPlay[i][j].getValue() == 0) {
								fl1++;
								continue;
							}

							// if the square value is not equals to answer at the same corrdinates then display this corrdinates and set value of this square to default (0)
							if(currentPlay[i][j].getValue() != ANSWER[i][j].getValue()) {
								System.out.println("(" + (i + 1) + ", " + (j + 1) + ")");
								currentPlay[i][j].setValue(0);
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
	}

	public static void clearScreen() {  
		System.out.print("\033[H\033[2J");  
		System.out.flush();
	}

	private static void printBoard(Node[][] board) {
		for(int i = 0; i < GRID_SIZE; i++) {
			if(i % 3 == 0 && i != 0) System.out.println("------+-------+------");
			for(int j = 0; j < GRID_SIZE; j++) {
				if(j % 3 == 0 && j != 0) System.out.print("| ");
				if(board[i][j].getValue() == 0) {
					System.out.print("- ");
				} else {
					System.out.print(board[i][j] + " ");
				}
			}
			System.out.println();
		}
	}

	private static boolean isNumberInRow(Node[][] board, int number, int row) {
		for(int i = 0; i < GRID_SIZE; i++)
			if(board[row][i].getValue() == number)
				return true;
		return false;
	}

	private static boolean isNumberInColumn(Node[][] board, int number, int column) {
		for(int i = 0; i < GRID_SIZE; i++)
			if(board[i][column].getValue() == number)
				return true;
		return false;
	}

	private static boolean isNumberInbox(Node[][] board, int number, int row, int column) {
		int localBoxRow = row - row % 3 + 3;
		int localBoxColumn = column - column % 3 + 3;

		for(int i = localBoxRow- 3; i < localBoxRow; i++)
			for(int j = localBoxColumn - 3; j < localBoxColumn; j++)
				if(number == board[i][j].getValue())
					return true;
		return false;
	}

	private static boolean isValidPlace(Node[][] board, int number, int row, int column) {
		return !isNumberInRow(board, number, row) &&
			!isNumberInColumn(board, number, column) &&
			!isNumberInbox(board, number, row, column);
	}

	private static boolean sudokuSolver(Node[][] board) {
		// itetare thought all square in board
		for(int row = 0; row < GRID_SIZE; row++) {
			for(int column = 0; column < GRID_SIZE; column++) {
		
				// if square has not have a value yet
				if(board[row][column].getValue() == 0) {
					for(int number = 1; number <= GRID_SIZE; number++) {
						if(isValidPlace(board, number, row, column)) {
							board[row][column].setValue(number);
		
							// if all the affter square has it's own value then turn true
							if(sudokuSolver(board)) {
								return true;
							} else {	// if the square affter this square have not any value tobe valided then set this value to default (0)
								board[row][column].setValue(0);
							}
						}
					}
		
					// if this square have not any value tobe valided then return false
					return false;
				}
			}
		}
		return true;
	}

	private Node[][] answer() {
		Node[][] answer = new Node[GRID_SIZE][GRID_SIZE];
		
		// coppy value from board to answer
		for(int i = 0; i < GRID_SIZE; i++)
			for(int j = 0; j < GRID_SIZE; j++)
				answer[i][j] = new Node(i,j,board[i][j].getValue());

		if(sudokuSolver(answer) == true) {
			return answer;
		} else {
			return null;
		}
	}
	
}
