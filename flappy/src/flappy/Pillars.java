package flappy;

import java.awt.Rectangle;

public class Pillars {

	public  int bottomY;
	public  int topY;
	public int x;
	
	public Pillars(int bottomY,int x) {
		this.bottomY = bottomY;
		this.x = x;
	}
	
	public void movePillars() {
		
		x-=1;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	public int getTopY() {
		return topY;
	}
	
	public int getBottomY() {
		return bottomY;
	}
	
	public int getX() {
		return x;
	}
	 public Rectangle getBorderTop() {
	        return new Rectangle(this.x, 0, 50, this.bottomY);
	   }
	 public Rectangle getBorderTopLedge() {
		 return new Rectangle(this.x-10, this.bottomY, 70, 20);
	   }
	 public Rectangle getBorderBottom() {
	        return new Rectangle(this.x, this.bottomY+150, 50,225-this.bottomY);
	   }
	 public Rectangle getBorderBottomLedge() {
		return new Rectangle(this.x-10, this.bottomY+140, 70, 20);
	   }
	
	
	
	
}

