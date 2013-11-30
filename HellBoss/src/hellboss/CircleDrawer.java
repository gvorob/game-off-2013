/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 *
 * @author George
 */
public class CircleDrawer extends DrawComp{
    Color color;
    public float size;
    
    
    
    public CircleDrawer(float x, float y, float size , Color c)
    {
        this.x = x;
        this.y = y;
        this.size = size;
        color = c;
    }
    
    public void draw(BufferedImage[] sprites, Point view, Graphics2D g)
    {
        //g.setColor(Color.red);
        g.setColor(color);
        Point p = getPoint();
        int r = (int)(size * 16);
        g.drawOval(p.x - r , p.y - r, 2 * r, 2 * r);
    }
    
    public void setSize(float s)
    {
        size = s;
    }
}
