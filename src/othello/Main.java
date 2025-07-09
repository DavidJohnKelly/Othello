package othello;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException, CloneNotSupportedException {

		Model model = new Model();
		Board white = new Board("White", model);
		Board black = new Board("Black", model);
		white.initialise();
		black.initialise();
		model.initialise();
	}
}
