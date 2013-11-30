/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 *
 * @author George
 */
public class SpriteDrawer extends DrawComp{
    float theta; 
    int refx, refy;
    SpriteData sprite;
    //boolean iso;
    
    private void init(SpriteData s, int rx, int ry)
    {
        sprite = s;
        refx = rx;
        refy = ry;
        //iso = true;
    }
    
    public SpriteDrawer(SpriteData s)
    {
        init(s,0,0);
    }
    
    public SpriteDrawer(SpriteData s, int rx, int ry)
    {
        init(s,rx,ry);
    }
    
    //public SpriteData draw()
    //{return sprite;}
    
    public void draw(BufferedImage[] sprites, Point view, Graphics2D g)
    {
        //g.setColor(Color.red);
        float r = getRotate();
        Point p = getPoint();
        if(r == 0)
        {
            sprite.drawSprite(g, p.x + refx, p.y + refy, sprites);
        }
        else
        {
            g.translate(p.x, p.y);
            g.rotate(getRotate());
            g.translate(-1 * p.x, -1 * p.y);
            sprite.drawSprite(g, p.x + refx, p.y + refy, sprites);
            g.translate(p.x, p.y);
            g.rotate(-1 * getRotate());
            g.translate(-1 * p.x, -1 * p.y);
        }
    }
    
    

    
    
    
    
    public void setRotate(float t)
    {theta = t;}
    
    public float getRotate()
    {return theta;}
}
