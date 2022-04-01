package othello;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Board {
	JFrame guiFrame;
	JLabel msgCurrentPlayer = new JLabel();
	JPanel playingSpace = new JPanel();
	Color playerColor;
	String playerColorLbl;
	String currentTurn;
	Model model;
	boolean reverse;
	ArrayList<ArrayList<Piece>> pieces = new ArrayList<ArrayList<Piece>>();

	public Board(String player, Model m) {
		model = m;
		model.storeView(this);
		guiFrame = new JFrame();
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrame.setTitle("Reversi - " + player + " player");
		playerColorLbl = player;
		if (playerColorLbl == "White") {
			playerColor = Color.WHITE;
			currentTurn = " player - click place to put piece";
			reverse = false;
		} else if (player == "Black") {
			playerColor = Color.BLACK;
			currentTurn = " player - not your turn";
			reverse = true;
		}
		playingSpace.setLayout(new GridLayout(8, 8));
		JButton greedyAI = new JButton("GreedyAI (play " + player + ")");
		greedyAI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(model.getCurrentPlayer() == playerColor) {
					model.makeMove(playerColor, reverse);
				}
			}
		});

		msgCurrentPlayer.setText(playerColorLbl + currentTurn);
		guiFrame.setLayout(new BorderLayout());
		guiFrame.add(msgCurrentPlayer, BorderLayout.NORTH);
		guiFrame.add(greedyAI, BorderLayout.SOUTH);
		guiFrame.add(playingSpace, BorderLayout.CENTER);
		guiFrame.setVisible(true);

	}

	public Color getPlayerColor() {
		return playerColor;
	}

	public ArrayList<ArrayList<Piece>> getPieces() {
		return pieces;
	}

	public boolean getReverse() {
		return reverse;
	}

	public String getTag() {
		return currentTurn;
	}

	public void initialise() {
		for (int x = 0; x < 8; x++) {
			pieces.add(new ArrayList<Piece>());
			for (int y = 0; y < 8; y++) {
				Piece piece = new Piece(x, y);
				piece.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent me) {
						ArrayList<Integer> position = ((Piece) me.getSource()).getPosition();
						updatePieces(position.get(0), position.get(1));
					}
				});
				pieces.get(x).add(piece);
				playingSpace.add(piece);
			}
		}
		guiFrame.pack();
	}

	public void setTag(String tag) {
		currentTurn = tag;
		msgCurrentPlayer.setText(playerColorLbl + currentTurn);
		msgCurrentPlayer.repaint();
	}

	public void updatePieces(int x, int y) {
		if (playerColor == model.getCurrentPlayer() && pieces.get(x).get(y).getColor() != Color.BLACK
				&& pieces.get(x).get(y).getColor() != Color.WHITE) {
			model.update(x, y, playerColor, reverse);
		}
	}

}
