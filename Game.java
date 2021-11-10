package JavaMP;

import java.awt.*;
import java.awt.geom.*;

import javax.swing.plaf.LabelUI;

import java.awt.event.*;

public class Game {
	Frame f = new Frame("Dots & Boxes");
	int Width = 400;
	int Height = 600;
	int GridSize =5;
	String ColorBackground="#F1F7ED";
	String ColorPlayer1="#91C7B1";
	String ColorPlayer2="#B33951";
	String ColorHighlightPlayer1="#465650";
	String ColorHighlightPlayer2="#351017";
	float Cell = Width/(GridSize+2);
	float Stroke = Cell/10;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();




	void Game(){
		f.setVisible(true);
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		f.setSize(Width, Height);
		f.setResizable(false);
		f.setLocation(((int)screenSize.getWidth()-Width)/2, ((int)screenSize.getHeight()-Height)/2);
		f.setLayout(new FlowLayout());
		f.setBackground(Color.decode(ColorBackground));
		GRID();
	}
	public void Info(){
		//TODO: all player info
	}
	public void GRID(){
		for(int i=0;i<=GridSize;i++){
			for(int j=0;j<=GridSize;j++){
				DrawDot(getGa);
			}
		}
	}
	@Override
    public void DrawDots(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setBackground(Color.gray);
		g2d.drawArc(30, 30, 20, 20, 0, 360);
    }


	public float getGridX(int col) {
		return Cell*(col+1);
	}
	public float getGridY(int row) {
		return Cell*(row+1);
	}
	public static void main(String args[]) {
		Game g1 = new Game();
		g1.Game();
	}
}
