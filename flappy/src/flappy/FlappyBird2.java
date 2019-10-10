// i did not know sound was a requirement; that was something pretty stupid. 

// I also 

package flappy;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.*;
import java.math.*;
import java.awt.image.*;
import java.applet.*;
import javax.swing.border.*;
import javax.imageio.ImageIO;
import java.util.*;
//import java.util.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class FlappyBird2 implements ActionListener, MouseListener, KeyListener
{

		static boolean hasIncreased = false;
	    private final int BUFFER_SIZE = 128000;
	    private File soundFile;
	    private AudioInputStream audioStream;
	    private AudioFormat audioFormat;
	    private SourceDataLine sourceLine;
	    static boolean released; 
	    static boolean endgame2=true;
	    
	static int ll; 
	public static FlappyBird2 flappyBird;

	Bird bird; 
	public ArrayList<Pillars> pillars;
	
	BufferedImage guy;	
	
	public final int W = 800, H = 500;

	public Renderer renderer;

	static boolean endgame; 

	public ArrayList<Rectangle> columns;

	public int ticks, yMotion, score;

	

	public Random rand;
	
	static boolean hasBegun = false;

	public FlappyBird2()
	{
		JFrame jframe = new JFrame();
		Timer timer = new Timer(20, this);

		renderer = new Renderer();
		rand = new Random();

		jframe.add(renderer);
		
		jframe.setTitle("Flappy Bird");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(W, H);
		jframe.addMouseListener(this);
		jframe.addKeyListener(this);
		jframe.setResizable(false);
		jframe.setVisible(true);

		try {
			guy = ImageIO.read(new File("FinalSprite.png"));
		}	
		catch (IOException e) {
		}
		
		bird= new Bird();
		pillars = new ArrayList<Pillars>();
		//columns = new ArrayList<Rectangle>();

		addPillars(true); 
		addPillars(true); 
		addPillars(true); 
		addPillars(true);

		timer.start();
	}
	
	public void playSound(String filename){

        String strFilename = filename;

        try {
            soundFile = new File(strFilename);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            audioStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        audioFormat = audioStream.getFormat();

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        sourceLine.start();

        int nBytesRead = 0;
        byte[] abData = new byte[BUFFER_SIZE];
        while (nBytesRead != -1) {
            try {
                nBytesRead = audioStream.read(abData, 0, abData.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (nBytesRead >= 0) {
                @SuppressWarnings("unused")
                int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
            }
        }

        sourceLine.drain();
        sourceLine.close();
    }
	
	public void repaint(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.CYAN);
		g2d.fillRect(0, 0, W, H);
		g2d.setColor(Color.orange); //sand
		g2d.fillRect(0, 400, W, 100);
		g2d.setColor(Color.GREEN); //grass
		g2d.fillRoundRect(0, 375, W, 25, 10, 10);
		
		
		
		g2d.drawImage(getSprites(guy,1,3).get(ll),bird.xPos-22,bird.yPos,null);
		//g2d.fillRect(bird.xPos, bird.yPos, 28, 28);
		g2d.setColor(Color.white);
		//g2d.fill(bird.getBorder());
		//System.out.println(ll);
		g2d.setColor(Color.RED);
		//g2d.fillRect(bird.xPos, bird.yPos, 28, 28);
		 
		for (Pillars column : pillars)
		{
			paintPillars(g, column);
		}
		g.setColor(Color.white);
		//g.setFont(new Font("Arial", 1, 100));
		//g2d.setColor(Color.WHITE);
		g.setFont(new Font("Ariel",1, 50)); 
		if (!hasBegun) {
			
			g2d.drawString("CLICK ANYWHERE TO START!", W/2-370, 120);
			//g.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 	
			g.setFont(new Font("Ariel",1, 15)); 
			g2d.drawString("Use the space bar to survive the deadly pipes! (Use space bar to restart game as well!) ", W/2 -350, bird.yPos+100);
			
		}if (endgame) {
			g.setFont(new Font("Ariel", 1, 100)); 
				g2d.drawString("GAME OVER!", 80, H/2);
			if (endgame2) {
				playSound("sfx_die.wav");
			}
			endgame2=false;
		}
		
		if ((!endgame) &&(hasBegun)) {
			
			g.setFont(new Font("Ariel", 1, 100)); 
			if ((score/3)<10)
			g2d.drawString((score/3)+"", W/2-30, 90);
			else if (score>10)
				g2d.drawString((score/3)+"", W/2-40, 90);
			//test for score
			
		}
		
		
		
		
	}
	
	
	public void paintPillars(Graphics g, Pillars x) {
		Graphics2D g2d = (Graphics2D)g;
		g.setColor(Color.green.darker());
		g2d.setColor(Color.GREEN.darker());
		// top pillar 
			g2d.fill3DRect(x.getX(), 0, 50, x.getBottomY(),true);
			g2d.fill3DRect(x.getX()-10, x.getBottomY(), 70, 20,true);
		//bottom pillar
			g2d.fill3DRect(x.getX(), x.getBottomY()+150, 50,225-x.getBottomY(),true );
			g2d.fill3DRect(x.getX()-10, x.getBottomY()+140, 70, 20,true);
			
			
			//g2d.fill3DRect(x, y, width, height, raised);
			
			//g2d.fill3DRect(x, y, width, height, raised);
	
		
	}
	
	public void addPillars(boolean ifStarted) {
		
		int rand =(int)(Math.random()*175)+50;
		
		if (ifStarted)
			pillars.add(new Pillars((int)(Math.random()*175)+50, 900+(pillars.size()*500)));
		else
			pillars.add(new Pillars((int)(Math.random()*175)+50,pillars.get(pillars.size() - 1).x + 500));
	}
	
	public void jump() {
		if (endgame)
		{
			bird = new Bird(380,100);
			pillars.clear(); 
			
			
				
			yMotion = 0;
				score = 0;

			addPillars(true);
			addPillars(true);
			addPillars(true);
			addPillars(true);

			endgame=false;
			endgame2=true;
		}

		if (!hasBegun)
		{
			hasBegun = true;
		}
		else if (!endgame)
		{
			
			ll = 2;
			if (yMotion > 0)
			{
				yMotion = 0;
			}

			yMotion -= 10;
			renderer.repaint();
		}
		
		
	}
	public void actionPerformed(ActionEvent arg0) {
		
		ticks++;
		
		
		if (hasBegun) {
		for (int i=0; i<pillars.size(); i++) {
			
			Pillars j = pillars.get(i);
			j.x-=3;
		}
		
		
		if (ticks % 2 == 0 && yMotion < 15)
		{
			yMotion += 2;
			
		}
		

		for (int i=0; i<pillars.size(); i++) {
			
			Pillars l = pillars.get(i);
			
			if ((l.x+70)<0) {
				
				pillars.remove(l);
				
				
				addPillars(false);
				
			}
			
		}
		bird.yPos+=yMotion;
	
			if ((bird.yPos>=347) ||(bird.yPos<0) ) {
				int counter=0;
			endgame=true;
			bird.setY(347);
			if (counter==0)
			//playSound("sfx_die.wav");
			counter++;
		
		}
	
		for(Pillars x: pillars) 
		{
			
			if (hasIncreased==false && bird.xPos + bird.getBorder().width > x.x+10  && bird.xPos + bird.getBorder().width < x.x + 20)
			{
				int n=2;
				score++;
				
				
			}
			hasIncreased=true;
			

			if	((bird.getBorder().intersects(x.getBorderBottom())) 	|| (bird.getBorder().intersects(x.getBorderBottomLedge())) || (bird.getBorder().intersects(x.getBorderTop())) || (bird.getBorder().intersects(x.getBorderTopLedge()))	) 
			{
					endgame=true;
					if (bird.xPos>x.x+ 20)
					bird.xPos = x.x+30;
					else if (bird.xPos<x.x+ 20)
						bird.xPos = x.x-30;
					
				 
			}
		
			

		}
		
		
	}
			hasIncreased = false;
			renderer.repaint();
	

}
	
	
	public static ArrayList<BufferedImage> getSprites( BufferedImage sheet, int cols, int rows) {
        int w = sheet.getWidth();
        int h = sheet.getHeight();
        int wT = w / cols;
        int hT = h / rows;
        ArrayList<BufferedImage> sprites = new ArrayList<BufferedImage>();
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                    sprites.add(sheet.getSubimage(x * wT, y * hT, wT, hT));

            }
        }
        return sprites;
}
	
	public static void main (String args[])
	{
		flappyBird = new FlappyBird2();
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		
	//hasBegun=true;
		
		jump();
		//playSound();
		
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	
			ll=0;
			renderer.repaint();
		
	}

	
	

	@Override
	public void keyPressed(KeyEvent arg0) {
		
		if((arg0.getKeyCode()==32) &&(released)) {
				ll=2;
				jump();
				//playSound("sfx_wing.wav");
				released = false;
		}
		
		if((arg0.getKeyCode()==65)) {
			System.out.println(pillars.size());
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if((arg0.getKeyCode()==32)) {
			ll=1;
			renderer.repaint();
			released= true;
		}
		
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
//try a var, endgamev2 to see if the endgame==true;
