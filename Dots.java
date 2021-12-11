//hiiiiiiiiiiiiiii tiffany here making changes
import java.awt.event.*;
import java.awt.*;

import javax.swing.*;
public class Dots extends JPanel implements MouseMotionListener, MouseListener {
	
	JButton restart;
	JFrame frame;
	JButton exit;
	JPanel p1;
     
    public int DOT_NUMBER=0;	//	The number of dots on each side of the square game board
    public static final int DOT_GAP=40;		//	The space between each dot					
    public static final int DOT_SIZE=8;		//	The length of the sides of the square dot
    
    public static final int PLAYER_ONE=1;
    public static final int PLAYER_TWO=2;
    
    public static final Color PLAYER_ONE_COLOR=Color.ORANGE;	//	The color of player1's boxes
    public static final Color PLAYER_TWO_COLOR=Color.GREEN;		// 	The color of player2's boxes
    
    private ConnectionSprite[] horizontalConnections;	//	Array for all the ConnectionSprites that horizontally connect dots
    private ConnectionSprite[] verticalConnections;		//	Array for all the ConnectionSprites that vertically connect dots
    private BoxSprite[] boxes;	//	Array for all the BoxSprites
    private Sprite[] dots;		//	Array for all the dots
    
    private Dimension dim;		//	Window dimensions
    
    private int clickx;		//	Holds the x coordinate of mouse click
    private int clicky;		// 	Holds the y coordinate of mouse click
    
    private int mousex;		// 	Holds the x coordinate of the mouse location
    private int mousey; 	// 	Holds the y coordinate of the mouse location
    	
    private int centerx;	//	x coordinate of the center of the gameboard
    private int centery; 	// 	y coordinate of the center of the gameborad 
    	
    private int side;	//	Length of the sides of the square gameboard
    private int space;	// Length of 1 dot + 1 connection
    	
    private int activePlayer;	// 	Holds the current player

    public Dots() {

        frame=new JFrame("Dots & Boxes");
        frame.setSize(600, 650);
       	frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addMouseListener(this);
        addMouseMotionListener(this);
        restart= new JButton("Reset");
        exit=new JButton("Exit");
		p1=new JPanel();
        p1.add(exit);
        p1.add(restart);
		setSize(600, 600);
		frame.add(this,BorderLayout.CENTER);
        frame.add(p1,BorderLayout.SOUTH);
        restart.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){  
        		NewGame();
        		repaint();
            }  
        });
        exit.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){  
                System.exit(0);
            }  
        });
    
        
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        NewGame();
    }

    private void loadProperties() {
    	//	Initialize fields
    	
        clickx=0;
        clicky=0;
        mousex=0;
        mousey=0;
        
        dim=getSize();
        centerx=dim.width/2;
        centery=(dim.height - 100) /2;
        
        side=DOT_NUMBER * DOT_SIZE + (DOT_NUMBER - 1) * DOT_GAP;	//	There is one less connection than dot per side
    	space=DOT_SIZE + DOT_GAP;  
    }

	private void loadDots() {

        dots=new Sprite[DOT_NUMBER * DOT_NUMBER];
        for(int rows=0; rows<DOT_NUMBER; rows++) {
            for(int cols=0; cols<DOT_NUMBER; cols++) {
                Sprite dot=new Sprite();
                dot.width=DOT_SIZE;
                dot.height=DOT_SIZE;
                dot.x=centerx - side/2 + cols * space;
                dot.y=centery - side/2 + rows * space;
                dot.shape.addPoint(-DOT_SIZE/2, -DOT_SIZE/2);
                dot.shape.addPoint(-DOT_SIZE/2, DOT_SIZE/2);
                dot.shape.addPoint(DOT_SIZE/2, DOT_SIZE/2);
                dot.shape.addPoint(DOT_SIZE/2, -DOT_SIZE/2);
                int index=rows * DOT_NUMBER + cols;
                dots[index]=dot;
            }
        }
    }
    
    private void loadConnections() {
    	
        horizontalConnections=new ConnectionSprite[(DOT_NUMBER-1) * DOT_NUMBER];
        verticalConnections=new ConnectionSprite[(DOT_NUMBER-1) * DOT_NUMBER];
        
        for(int i=0; i<horizontalConnections.length; i++) {
        	int colsx=i % (DOT_NUMBER-1);
        	int rowsx=i / (DOT_NUMBER-1);
        	int horx=centerx - side / 2 + DOT_SIZE + colsx * space;
        	int hory=centery - side / 2 + rowsx * space;
        	horizontalConnections[i]=ConnectionSprite.createConnection(ConnectionSprite.HORZ_CONN, horx, hory);
        	
        	int colsy=i % DOT_NUMBER;
        	int rowsy=i / DOT_NUMBER;
        	int vertx=centerx - side / 2 + colsy * space;
        	int verty=centery - side / 2 + DOT_SIZE + rowsy * space;
        	verticalConnections[i]=ConnectionSprite.createConnection(ConnectionSprite.VERT_CONN, vertx, verty);
        }
    } 
    	
    private void loadBoxes() {
    	
    	boxes=new BoxSprite[(DOT_NUMBER-1) * (DOT_NUMBER-1)];
    	
    	for(int i=0; i<boxes.length; i++) {
    		int cols=i % (DOT_NUMBER-1);
    		int rows=i / (DOT_NUMBER-1);
    		
    		int boxx=centerx - side / 2 + DOT_SIZE + cols * space;
    		int boxy=centery - side / 2 + DOT_SIZE + rows * space;
    		
    		ConnectionSprite[] horConn=new ConnectionSprite[2];
    		horConn[0]=horizontalConnections[i];
    		horConn[1]=horizontalConnections[i + (DOT_NUMBER - 1)];
    		
    		ConnectionSprite[] verConn=new ConnectionSprite[2];		//	This only works if the verticalConnections were put into the array rows then columns
    		verConn[0]=verticalConnections[i + rows];
    		verConn[1]=verticalConnections[i + rows + 1]; 		
    		
    		boxes[i]=BoxSprite.createBox(boxx, boxy, horConn, verConn);
    	}
    }

	
	private void NewGame(){
		String dot = JOptionPane.showInputDialog( "Enter Number of dots in a row/column (3-8)" );
		if (dot==null)
			System.exit(0);
		DOT_NUMBER = Integer.parseInt(dot);
		loadProperties();
        loadDots();
		startGame();
	}
    
    
    private void startGame() {
		
    	activePlayer=(double)Math.random()*1 >0.5?PLAYER_ONE:PLAYER_TWO;
    	loadConnections();
        loadBoxes();
    }
    
    private ConnectionSprite getConnection(int x, int y) {
    	
    	// Get the connection that encloses point (x, y) or return null if there isn't one
    	
    	for(int i=0; i<horizontalConnections.length; i++) {
    		if(horizontalConnections[i].containsPoint(x, y)) {
    			return horizontalConnections[i];			
    		}
    	}
    	
    	for(int i=0; i<verticalConnections.length; i++) {
    		if(verticalConnections[i].containsPoint(x, y)) {
    			return verticalConnections[i];
    		}
    	}
    	
    	return null;
    }
    
    private boolean[] getBoxStatus() {
    	boolean[] status=new boolean[boxes.length];
    	
    	for(int i=0; i<status.length; i++) {
    		status[i]=boxes[i].isBoxed();
    	}
    	
    	return status;
    }
    
    private boolean makeConnection(ConnectionSprite connection) {
    	boolean newBox=false;
    	
    	boolean[] boxStatusBeforeConnection=getBoxStatus();	//	The two boolean arrays are used to see if a new box was created after the connection was made
    	
    	connection.connectionMade=true;
    	
    	boolean[] boxStatusAfterConnection=getBoxStatus();
    	
    	for(int i=0; i<boxes.length; i++) {
    		if(boxStatusAfterConnection[i]!=boxStatusBeforeConnection[i]) {
    			newBox=true;
    			boxes[i].player=activePlayer;
    		}
    	}
    	
    	if(!newBox) {	//	Allow the current player to go again if he made a box
    		if(activePlayer==PLAYER_ONE)
    			activePlayer=PLAYER_TWO;
    		else
    			activePlayer=PLAYER_ONE;
    	} 	
    	
    	checkForGameOver();
    	
    	return newBox;
    }

    private int[] calculateScores() {
    	int[] scores={0, 0};
    	
    	for(int i=0; i<boxes.length; i++) {
    		if(boxes[i].isBoxed() && boxes[i].player!=0) {
    			scores[boxes[i].player - 1]++;
    		}
    	}
    	
    	return scores;
    }

    private void checkForGameOver() {
    	int[] scores=calculateScores();
    	if((scores[0] + scores[1])==((DOT_NUMBER - 1) * (DOT_NUMBER - 1))) {
    		JOptionPane.showMessageDialog(this, "Player1: " + scores[0] + "\nPlayer2: " + scores[1], "Game Over", JOptionPane.PLAIN_MESSAGE);
    		NewGame();
    		repaint();
    	}
    }
	
    private void handleClick() {
    	ConnectionSprite connection=getConnection(clickx, clicky);
    	if(connection==null)
    		return;
    	
    	if(!connection.connectionMade) {
    		makeConnection(connection);
    		
    	}    		
    	
    	repaint();
    }
    
    public void mouseMoved(MouseEvent event) {
    	mousex=event.getX();
    	mousey=event.getY();
		// System.out.println(mousex+":"+mousey);
    	repaint();
    }
    
    public void mouseDragged(MouseEvent event) {
    	mouseMoved(event);
    }
    
    public void mouseClicked(MouseEvent event) {
    	clickx=event.getX();
    	clicky=event.getY();
    	handleClick();
    }
    
    public void mouseEntered(MouseEvent event) {	
    }
    
    public void mouseExited(MouseEvent event) {	
    }
    
    public void mousePressed(MouseEvent event) {
    }
    
    public void mouseReleased(MouseEvent event) {
    }
    
    private void paintBackground(Graphics g) {
    	
    	g.setColor(Color.WHITE);
    	g.fillRect(0, 0, dim.width, dim.height-100);
    }
    
    private void paintDots(Graphics g) {
    	for(int i=0; i<dots.length; i++) {
    		dots[i].render(g);
    	}
    }
    
    private void paintConnections(Graphics g) {
    	for(int i=0; i<horizontalConnections.length; i++) {
    		
    		if(!horizontalConnections[i].connectionMade) {
    			if(horizontalConnections[i].containsPoint(mousex, mousey)) {
    				horizontalConnections[i].color=Color.RED;
    			} else {
    				horizontalConnections[i].color=Color.WHITE;
    			}
    		} else {
    			horizontalConnections[i].color=Color.BLUE;
    		}
    		
    		horizontalConnections[i].render(g);
    	}
    	
    	for(int i=0; i<verticalConnections.length; i++) {
    		
    		if(!verticalConnections[i].connectionMade) {
    			if(verticalConnections[i].containsPoint(mousex, mousey)) {
    				verticalConnections[i].color=Color.RED;
    			} else {
    				verticalConnections[i].color=Color.WHITE;
    			}
    		} else {
    			verticalConnections[i].color=Color.BLUE;
    		}
    		
    		verticalConnections[i].render(g);
    	}
    }
    
    public void paintBoxes(Graphics g) {
    	for(int i=0; i<boxes.length; i++) {
    		if(boxes[i].isBoxed()) {
    			if(boxes[i].player==PLAYER_ONE) {
    				boxes[i].color=PLAYER_ONE_COLOR;
    			} else if(boxes[i].player==PLAYER_TWO) {
    				boxes[i].color=PLAYER_TWO_COLOR;
    			}
    		} else {
    			boxes[i].color=Color.WHITE;
    		}
    		
    		boxes[i].render(g);
    	}
    }
    
    public void paintStatus(Graphics g) {
    	int[] scores=calculateScores();
    	String status="It is player" + activePlayer + "'s turn";
    	String status2="Player 1: " + scores[0];
    	String status3="Player 2: " + scores[1];
    	
    	g.setColor(Color.BLACK);
    	g.drawString(status, 10, dim.height-80);
		g.fillRect(0, dim.height-100, dim.width, 2);
    	
    	g.setColor(PLAYER_ONE_COLOR);
    	g.drawString(status2, 10, dim.height-65);
    	
    	g.setColor(PLAYER_TWO_COLOR);
    	g.drawString(status3, 10, dim.height-50);
    }
	
    public void paint(Graphics g) {
 
    	Image bufferImage=createImage(dim.width, dim.height);
    	Graphics bufferGraphics=bufferImage.getGraphics();
    	
    	paintBackground(bufferGraphics);    	
    	paintDots(bufferGraphics);   	
    	paintConnections(bufferGraphics);
    	paintBoxes(bufferGraphics);
    	paintStatus(bufferGraphics);
    	
    	g.drawImage(bufferImage, 0, 0, null);
    }
    
    public void update(Graphics g) {
    	paint(g);
    }

    public static void main(String[] args) {
    	new Dots();
    }
}

