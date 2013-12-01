/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 *
 * @author George
 */
public class UIButton extends UIRegion {
    SpriteData[] images;
    
    
    public UIButton(Rectangle rect, int id, SpriteData[] i,UIListener listener, boolean block, boolean vis)
    {
        super(rect,id,null,listener,block,vis);
        images = i;
    }
    
    public void draw(Graphics2D g)
    {
        images[state].drawSprite(g, region.x, region.y, World.w.sprites);
                
    }
}
