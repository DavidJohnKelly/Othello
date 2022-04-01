package othello;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JLabel;

public class Piece extends JLabel
{
	Color pieceColor;
	Color borderColor;
	ArrayList<Integer> position = new ArrayList<Integer>();
	
	public Piece(int x, int y)
	{
		setMinimumSize(new Dimension(60, 60));
		setPreferredSize(new Dimension(60, 60));
		position.add(x);
		position.add(y);
		pieceColor = new Color(50,50,50,0); // Initially Transparent
		borderColor = new Color(50,50,50,0);
	}
	
	public Color getColor() {
		return pieceColor;
	}
	
	public ArrayList<Integer> getPosition() {
		return position;
	}
	
	public void setColor(Color pieceColor) {
		this.pieceColor = pieceColor;
		if(this.pieceColor == Color.WHITE){
			borderColor = Color.BLACK;
		}
		else if(this.pieceColor == Color.BLACK){
			borderColor = Color.WHITE;
		}
		else {
			borderColor = new Color(50,50,50,0);
		}
		this.repaint();
	}

	protected void paintComponent(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.GREEN);
		g.fillRect(1, 1, getWidth()-1*2, getHeight()-1*2);
		g.setColor(borderColor);
		g.fillOval(5,5,getWidth()-5*2, getHeight()-5*2);
		g.setColor(pieceColor);
		g.fillOval(8,8,getWidth()-8*2, getHeight()-8*2);
			
		}
		
	}