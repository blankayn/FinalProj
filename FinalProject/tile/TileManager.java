package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;

public class TileManager {
    GamePanel gp;
    Tile[] tile;
   int mapTileNum[][];
    public TileManager(GamePanel gp) {
        this.gp = gp; // Assign the GamePanel instance to the field
        tile = new Tile[10];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        getTileImages();
        loadMap("/src/maps/worldmap.txt");
    }

    public void getTileImages() {
        try {
            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/src/tiles/wall.png"));
            
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/src/tiles/carpet.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 public void loadMap(String filePath){
	 try {
		 InputStream is = getClass().getResourceAsStream(filePath);
		 BufferedReader br = new BufferedReader(new InputStreamReader(is)); 
		 int col = 0;
		 int row = 0;
		 
		while(col < gp.maxWorldCol && row < gp.maxWorldRow){
			
			String line = br.readLine();
			
			while(col < gp.maxWorldCol) {
				String numbers[] = line.split(" ");
				int num = Integer.parseInt(numbers[col]);
				mapTileNum[col][row] = num;
				col++;
			}
			if(col == gp.maxWorldCol) {
				col = 0;
				row++;
			}
		}
		br.close();
	 }catch(Exception e) {
		 
	 }
	 
 }
    public void draw(Graphics2D g2) {
    	int worldcol= 0;
      int worldrow = 0;
     
      
      while( worldcol < gp.maxWorldCol &&  worldrow  < gp.maxWorldRow){
       int tileNum = mapTileNum[worldcol][worldrow];
       
       int worldx  = worldcol * gp.tileSize;
       int worldy = worldrow * gp.tileSize;
       int screenx = worldx - gp.player.worldx + gp.player.screenX;
       int screeny = worldy - gp.player.worldy + gp.player.screenY;
       
       g2.drawImage(tile[tileNum].image,screenx,screeny, gp.tileSize,gp.tileSize,null);
      
       worldcol++;
       
       if(worldcol == gp.maxWorldCol){
       worldcol = 0;
       worldrow++;
 
        }
       
     }
            
    }
}
