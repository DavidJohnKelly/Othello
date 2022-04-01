package othello;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Model {

	ArrayList<Board> views = new ArrayList<Board>();
	ArrayList<ArrayList<Piece>> pieces = new ArrayList<ArrayList<Piece>>();
	Color currentPlayer;

	public ArrayList<ArrayList<Piece>> getPieces() {
		return pieces;
	}

	public void initialise() {
		currentPlayer = Color.WHITE;
		for (int i = 0; i < 8; i++) {
			pieces.add(new ArrayList<Piece>());
			for (int y = 0; y < 8; y++) {
				pieces.get(i).add(new Piece(i, y));
			}
		}
		pieces.get(3).get(3).setColor(Color.WHITE);
		pieces.get(3).get(4).setColor(Color.BLACK);
		pieces.get(4).get(3).setColor(Color.BLACK);
		pieces.get(4).get(4).setColor(Color.WHITE);
		updateViews();
	}

	public void reset() {
		Color transparent = new Color(50, 50, 50, 0);
		currentPlayer = Color.WHITE;
		for (int i = 0; i < 8; i++) {
			for (int y = 0; y < 8; y++) {
				pieces.get(i).get(y).setColor(transparent);
			}
		}
		pieces.get(3).get(3).setColor(Color.WHITE);
		pieces.get(3).get(4).setColor(Color.BLACK);
		pieces.get(4).get(3).setColor(Color.BLACK);
		pieces.get(4).get(4).setColor(Color.WHITE);
		updateViews();
	}

	public int countPieces(int x, int y, Color playerColor) {
		int count = 0;
		if (pieces.get(x).get(y).getColor() != Color.WHITE && pieces.get(x).get(y).getColor() != Color.BLACK) {
			count += validDirection(x, y, playerColor, 1, 1);
			count += validDirection(x, y, playerColor, 1, -1);
			count += validDirection(x, y, playerColor, -1, 1);
			count += validDirection(x, y, playerColor, -1, -1);
			count += validDirection(x, y, playerColor, 0, 1);
			count += validDirection(x, y, playerColor, 0, -1);
			count += validDirection(x, y, playerColor, 1, 0);
			count += validDirection(x, y, playerColor, -1, 0);

		}
		return count;
	}

	public Color oppositeColor(Color color) {
		if (color == Color.WHITE) {
			return Color.BLACK;
		}
		return Color.WHITE;
	}

	public void replaceDirection(int x, int y, Color playerColor, int xMod, int yMod, int replaceCount) {
		for (int i = 0; i < replaceCount; i++) {
			x += xMod;
			y += yMod;
			pieces.get(x).get(y).setColor(playerColor);
		}
	}

	public void setPiece(int x, int y, Color color) {
		pieces.get(x).get(y).setColor(color);
	}

	public void storeView(Board view) {
		views.add(view);
	}

	public boolean checkOver() {
		int countOver = 0;
		for (int i = 0; i < views.size(); i++) {
			if (!checkPlay(views.get(i).getPlayerColor(), views.get(i).getReverse())) {
				countOver++;
			}
		}
		if (countOver == 2) {
			return true;
		}
		return false;
	}

	public void update(int x, int y, Color playerColor, boolean reverse) {
		if (valid(x, y, playerColor, reverse)) {
			if (reverse) {
				pieces.get(7 - x).get(7 - y).setColor(playerColor);
			} else {
				pieces.get(x).get(y).setColor(playerColor);
			}
			updateViews();
			updateTags();
		}

	}

	public void makeMove(Color playerColor, boolean reverse) {
		int maxPieces = 0;
		ArrayList<Integer> position = new ArrayList<Integer>();
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (pieces.get(x).get(y).getColor() != Color.WHITE && pieces.get(x).get(y).getColor() != Color.BLACK) {
					int tempPieces = countPieces(x, y, playerColor);
					if (tempPieces > maxPieces) {
						maxPieces = tempPieces;
						position = pieces.get(x).get(y).getPosition();
					}
				}
			}
		}
		if (maxPieces != 0) {
			if (reverse) {
				update(7 - position.get(0), 7 - position.get(1), playerColor, reverse);
			} else {
				update(position.get(0), position.get(1), playerColor, reverse);
			}

		}
	}

	public void setCurrentPlayer(Color color) {
		currentPlayer = color;
		for (int i = 0; i < views.size(); i++) {
			Board view = views.get(i);
			if (view.getPlayerColor() == currentPlayer) {
				view.setTag(" player - click place to put piece");
			} else {
				view.setTag(" player - not your turn");
			}
		}
	}

	public void swapCurrentPlayer() {
		if (currentPlayer == Color.WHITE) {
			currentPlayer = Color.BLACK;
		} else {
			currentPlayer = Color.WHITE;
		}
		setCurrentPlayer(currentPlayer);
	}

	public void updateTags() {
		if (checkOver()) {
			int colorWhite = countColor(Color.WHITE);
			int colorBlack = countColor(Color.BLACK);
			if (colorWhite < colorBlack) {
				JOptionPane.showMessageDialog(null, "Black wins: " + colorWhite + ":" + colorBlack);
			} else if (colorBlack < colorWhite) {
				JOptionPane.showMessageDialog(null, "White wins: " + colorWhite + ":" + colorBlack);
			} else {
				JOptionPane.showMessageDialog(null, "Draw: " + colorWhite + ":" + colorBlack);
			}
			if ((JOptionPane.showConfirmDialog(null, "Start a new game?", "Reset",
					JOptionPane.YES_NO_OPTION)) == JOptionPane.YES_OPTION) {
				reset();
			} else {
				System.exit(0);
			}
		} else {
			if (Color.WHITE == currentPlayer && checkPlay(Color.BLACK, true)) {
				swapCurrentPlayer();
			} else if (Color.BLACK == currentPlayer && checkPlay(Color.WHITE, false)) {
				swapCurrentPlayer();
			}
		}
	}

	public int countColor(Color color) {
		int count = 0;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (pieces.get(x).get(y).getColor() == color) {
					count++;
				}
			}
		}
		return count;
	}

	public boolean checkPlay(Color playerColor, boolean reverse) {
		boolean valid = false;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (reverse) {
					if (countPieces(7 - x, 7 - y, playerColor) != 0) {
						valid = true;
					}
				} else {
					if (countPieces(x, y, playerColor) != 0) {
						valid = true;
					}
				}
			}
		}
		return valid;
	}

	public void updateViews() {
		for (int i = 0; i < views.size(); i++) {
			Board view = views.get(i);
			ArrayList<ArrayList<Piece>> viewPieces = view.getPieces();
			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					if (view.getReverse()) {
						viewPieces.get(7 - x).get(7 - y).setColor(pieces.get(x).get(y).getColor());
					} else {
						viewPieces.get(x).get(y).setColor(pieces.get(x).get(y).getColor());
					}
				}
			}
		}
	}

	public boolean valid(int originalX, int originalY, Color playerColor, boolean reverse) {
		boolean valid = false;
		int replaceCount;
		if (reverse) {
			originalX = 7 - originalX;
			originalY = 7 - originalY;
		}
		if ((replaceCount = validDirection(originalX, originalY, playerColor, 1, 1)) != 0) { // Down right diagonal
			replaceDirection(originalX, originalY, playerColor, 1, 1, replaceCount);
			valid = true;
		}
		if ((replaceCount = validDirection(originalX, originalY, playerColor, 1, -1)) != 0) { // Down left diagonal
			replaceDirection(originalX, originalY, playerColor, 1, -1, replaceCount);
			valid = true;
		}
		if ((replaceCount = validDirection(originalX, originalY, playerColor, -1, -1)) != 0) { // Up left diagonal
			replaceDirection(originalX, originalY, playerColor, -1, -1, replaceCount);
			valid = true;
		}
		if ((replaceCount = validDirection(originalX, originalY, playerColor, -1, 1)) != 0) { // Up right diagonal
			replaceDirection(originalX, originalY, playerColor, -1, 1, replaceCount);
			valid = true;
		}
		if ((replaceCount = validDirection(originalX, originalY, playerColor, -1, 0)) != 0) { // Up
			replaceDirection(originalX, originalY, playerColor, -1, 0, replaceCount);
			valid = true;
		}
		if ((replaceCount = validDirection(originalX, originalY, playerColor, 1, 0)) != 0) { // Down
			replaceDirection(originalX, originalY, playerColor, 1, 0, replaceCount);
			valid = true;
		}
		if ((replaceCount = validDirection(originalX, originalY, playerColor, 0, 1)) != 0) { // Right
			replaceDirection(originalX, originalY, playerColor, 0, 1, replaceCount);
			valid = true;
		}
		if ((replaceCount = validDirection(originalX, originalY, playerColor, 0, -1)) != 0) { // Left
			replaceDirection(originalX, originalY, playerColor, 0, -1, replaceCount);
			valid = true;
		}
		return valid;
	}

	public Color getCurrentPlayer() {
		return currentPlayer;
	}

	public int validDirection(int originalX, int originalY, Color playerColor, int xMod, int yMod) {
		int x = originalX;
		int y = originalY;
		int xBound = x;
		int yBound = y;
		int count = 0;
		Color opposingColor = oppositeColor(playerColor);
		if (xMod == 1) {
			xBound = 8;
		} else if (xMod == -1) {
			xBound = -1;
		}
		if (yMod == 1) {
			yBound = 8;
		} else if (yMod == -1) {
			yBound = -1;
		}
		if (xBound != x && yBound != y && (x += xMod) != xBound && (y += yMod) != yBound
				&& pieces.get(x).get(y).getColor() == opposingColor) {
			while ((x += xMod) != xBound && (y += yMod) != yBound) {
				count++;
				Color pieceColor = pieces.get(x).get(y).getColor();
				if (pieceColor != playerColor && pieceColor != opposingColor) {
					return 0;
				}
				if (pieces.get(x).get(y).getColor() == playerColor) {
					return count;
				}
			}
		}
		x = originalX;
		y = originalY;
		if (xBound == x && yBound != y && (y += yMod) != yBound && pieces.get(x).get(y).getColor() == opposingColor) {
			while ((y += yMod) != yBound) {
				count++;
				Color pieceColor = pieces.get(x).get(y).getColor();
				if (pieceColor != playerColor && pieceColor != opposingColor) {
					return 0;
				}
				if (pieceColor == playerColor) {
					return count;
				}
			}
		}
		x = originalX;
		y = originalY;
		if (xBound != x && yBound == y && (x += xMod) != xBound && pieces.get(x).get(y).getColor() == opposingColor) {
			while ((x += xMod) != xBound) {
				Color pieceColor = pieces.get(x).get(y).getColor();
				count++;
				if (pieceColor != playerColor && pieceColor != opposingColor) {
					return 0;
				}
				if (pieces.get(x).get(y).getColor() == playerColor) {
					return count;
				}
			}
		}
		return 0;
	}

}
