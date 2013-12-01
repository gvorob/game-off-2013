/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author George
 */
public class SpriteData {
    public int spriteS, spriteX, spriteY, spriteW, spriteH;//sprite sheet, x , y, width, height
    
    public SpriteData(int s, int x, int y, int w, int h)
    {
        spriteS = s;
        spriteX = x;
        spriteY = y;
        spriteW = w;
        spriteH = h;
    }
    
    public void drawSprite(Graphics2D g, int x, int y, BufferedImage[] b)
    {
        g.drawImage(b[spriteS], x, y, x + spriteW, y + spriteH, spriteX, spriteY, spriteX + spriteW, spriteY + spriteH, null);
    }
    
    public SpriteData clone()
    {
        return new SpriteData(spriteS, spriteX, spriteY, spriteW, spriteH);
    }
}
