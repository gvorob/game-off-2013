/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 *
 * @author George
 */
public class DrawComp extends Component{
    float x, y, theta; 
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
    
    public DrawComp(SpriteData s)
    {
        init(s,0,0);
    }
    
    public DrawComp(SpriteData s, int rx, int ry)
    {
        init(s,rx,ry);
    }
    
    public SpriteData draw()
    {
        return sprite;
    }
    
    public Point getPoint()
    {
        return new Point((int)(16 * x),(int)(16 * y));
    }

    void move(Vector2 location) {
        x = location.x;
        y = location.y;
    }
    
    public void remove()
    {
        World.w.remove(this);
    }
    
    public void setRotate(float t)
    {theta = t;}
    
    public float getRotate()
    {return theta;}
}
