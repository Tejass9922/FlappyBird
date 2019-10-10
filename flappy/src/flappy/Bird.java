package flappy;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.*;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JOptionPane;

public class Bird{
 static int right, left, up, down = 0;
    int i=0;
    int xPos = 380;
    int yPos = 200;
    //FallDownBall ball = new FallDownBall();
	public Bird(){

	}

	public Bird(int x, int y) {
		this.xPos = x;
		this.yPos = y;
	}
   public void paint(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.RED);
        g2d.fillOval(30, yPos, 30, 30);


    }

    public void move(){


	       xPos += left + right;



	        if (xPos<=0)
	        	xPos= 0;
	        if (xPos>=401)
	        	xPos= 401;

	}

public void moveUp(){
	yPos--;
}
   public void gravity(){
   	yPos++;
   }

   public int getY(){
   	return yPos;
   }
   public int getX(){
   	return xPos;
   }
   public int getCenter(){
	   	return yPos+xPos;
	 }

	 public void keyPressed(KeyEvent e) {


	        if (e.getKeyCode() == KeyEvent.VK_DOWN)

	            down = 1;

	        if (e.getKeyCode() == KeyEvent.VK_RIGHT)

	            right = 2;

	        if (e.getKeyCode() == KeyEvent.VK_UP)

	            up = -1;

	        if (e.getKeyCode() == KeyEvent.VK_LEFT)

	            left = -2;

	    }

	public void setX(int x){
		xPos = x;
	}
	public void setY(int y){
		yPos= y;
	}

	    public void keyReleased(KeyEvent e) {

	        if (e.getKeyCode() == KeyEvent.VK_DOWN)

	            down = 0;

	        if (e.getKeyCode() == KeyEvent.VK_RIGHT)

	            right = 0;

	        if (e.getKeyCode() == KeyEvent.VK_UP)

	            up = 0;

	        if (e.getKeyCode() == KeyEvent.VK_LEFT)

	            left = 0;

	    }

	    public Rectangle getBorder() {
	        return new Rectangle(xPos, yPos, 28, 28);
	    }


}