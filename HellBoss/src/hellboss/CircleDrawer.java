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
    public boolean fill;
    public boolean visible;
    
    
    public CircleDrawer(float x, float y, float size , Color c, boolean fill, int zlevel)
    {
        this.x = x;
        this.y = y;
        this.size = size;
        color = c;
        this.fill = fill;
        visible = true;
        z = zlevel;
    }
    
    public void draw(BufferedImage[] sprites, Point view, Graphics2D g)
    {
        //g.setColor(Color.red);
        if(visible)
        {
            g.setColor(color);
            Point p = getPoint();
            int r = (int)(size * 16);
            if(fill)
                g.fillOval(p.x - r , p.y - r, 2 * r, 2 * r);
            else
                g.drawOval(p.x - r , p.y - r, 2 * r, 2 * r);
        }
    }
    
    public void setSize(float s)
    {
        size = s;
    }
}
