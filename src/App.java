import java.io.File;
import java.util.Scanner;

public class App {
	public static void main(String[] args) throws Exception {
		// Sudoku sudoku = new Sudoku();
		// sudoku.toString();

		Scanner scanner = new Scanner(new File("data.txt"));
		Node[][] input = new Node[9][9];
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				input[i][j] = new Node(i,j,scanner.nextInt());
			}
		}
		new Game(input);
	}
}