package main;
import tile.TileManager;

import javax.swing.JPanel;


import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;


public class GamePanel extends JPanel implements Runnable {

   // Screen settings
   int originalTileSize = 16; 
   int scale = 3;
   
   public int tileSize = originalTileSize * scale; 
    public int maxScreenCol = 16;
   public int maxScreenRow = 12;
   public int screenWidth = tileSize * maxScreenCol; 
  public  final int screenHeight = tileSize * maxScreenRow;
//world settings
  public final int maxWorldCol = 50;
  public final int maxWorldRow = 50;
  public final int worldWidth  = tileSize * maxWorldCol;
  public final int worldHeight = tileSize * maxWorldRow;
  
   int FPS = 60;

   
   TileManager tileM  = new TileManager(this);
   KeyHandler keyH = new KeyHandler();
   public Player player = new Player(this, keyH);
   Thread gameThread;

   // Constructor
   public GamePanel() {
      this.setPreferredSize(new Dimension(screenWidth, screenHeight));
      this.setBackground(Color.black);
      this.setDoubleBuffered(true);
      this.addKeyListener(keyH);
      this.setFocusable(true);
   }

   // Start the game loop
   public void startGameThread() {
      gameThread = new Thread(this);
      gameThread.start();
   }

   @Override
   public void run() {
      double drawInterval = 1000000000 / FPS; // Nanoseconds per frame
      double nextDrawTime = System.nanoTime() + drawInterval;
   
      while (gameThread != null) {
        
         update();
      
        
         repaint();
      
         try {
            double remainingTime = nextDrawTime - System.nanoTime();
            remainingTime = remainingTime / 1000000; // Convert to milliseconds
         
            if (remainingTime < 0) {
               remainingTime = 0;
            }
         
            Thread.sleep((long) remainingTime);
            nextDrawTime += drawInterval;
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }
   }

   public void update() {
      player.update();
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
   
      Graphics2D g2 = (Graphics2D) g;
      tileM.draw(g2);
      player.draw(g2);
      g2.dispose();
   }

         public class KeyHandler implements KeyListener {
      public boolean upPressed, downPressed, leftPressed, rightPressed;
   
      @Override
      public void keyTyped(KeyEvent e) {}
   
      @Override
      public void keyPressed(KeyEvent e) {
         int code = e.getKeyCode();
      
         if (code == KeyEvent.VK_W) {
            upPressed = true;
         }
         if (code == KeyEvent.VK_S) {
            downPressed = true;
         }
         if (code == KeyEvent.VK_A) {
            leftPressed = true;
         }
         if (code == KeyEvent.VK_D) {
            rightPressed = true;
         }
      }
   
      @Override
      public void keyReleased(KeyEvent e) {
         int code = e.getKeyCode();
      
         if (code == KeyEvent.VK_W) {
            upPressed = false;
         }
         if (code == KeyEvent.VK_S) {
            downPressed = false;
         }
         if (code == KeyEvent.VK_A) {
            leftPressed = false;
         }
         if (code == KeyEvent.VK_D) {
            rightPressed = false;
         }
      }
   }
         //Player & Entitty
public class Player {
    GamePanel gp;
    KeyHandler keyH;

  
    public int worldx;
	public int worldy;
	int speed;
    String direction;

    
    BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;

 
    int spriteCounter = 0;
    int spriteNum = 1;
    public final int screenY;
    public final int screenX;


    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        
     screenX = gp.screenWidth/2 - (gp.tileSize/2);
     screenY = gp.screenHeight/2 - (gp.tileSize/2);
     
        setDefaultValues();
        loadPlayerImages();
    }

    public void setDefaultValues() {
        worldx = 100;
        worldy = 100;
        speed = 4;
        direction = "down";
    }

    public void loadPlayerImages() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/src/sprite/up.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/src/sprite/up1.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/src/sprite/down.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/src/sprite/down1.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/src/sprite/left.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/src/sprite/left1.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/src/sprite/right.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/src/sprite/right1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            if (keyH.upPressed) {
                direction = "up";
                worldy -= speed;
            } else if (keyH.downPressed) {
                direction = "down";
                worldy += speed;
            } else if (keyH.leftPressed) {
                direction = "left";
                worldx -= speed;
            } else if (keyH.rightPressed) {
                direction = "right";
                worldx += speed;
            }

            spriteCounter++;
            if (spriteCounter > 10) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch (direction) {
            case "up":
                image = (spriteNum == 1) ? up1 : up2;
                break;
            case "down":
                image = (spriteNum == 1) ? down1 : down2;
                break;
            case "left":
                image = (spriteNum == 1) ? left1 : left2;
                break;
            case "right":
                image = (spriteNum == 1) ? right1 : right2;
                break;
        }

        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
   }
  }
