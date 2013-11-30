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
public class DrawComp extends Component{
    float x, y;
    
    
    public void draw(BufferedImage[] sprites, Point view, Graphics2D g)
    {}
    
    public Point getPoint()
    {
        return new Point((int)(16 * x),(int)(16 * y));
    }
    
    public void remove()
    {
        World.w.remove(this);
    }
    
    public void setRotate(float r)
    {}
    
    public void setSize(float s)
    {}
    
    void move(Vector2 location) {
        x = location.x;
        y = location.y;
    }
    
}
