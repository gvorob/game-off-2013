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
    float x, y; 
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
    //public boolean isIso()
    //{return iso;}
    
    
    //public Point fromIso()
    //{return fromIso(x, y , z);}
    
    //public static Point fromIso(float x, float y, float z)
    //{
    //    int xcor = (int)(8 * x + 8 * y);
    //    int ycor = (int)((4 * x) + (-4 * y) + (-8 * z));
    //    return new Point(xcor, ycor);
    //}
    
    //public static Vector2 toIso(int x, int y)
    //{
    //    return new Vector2(x/16.0f + y/8.0f, x/16.0f - y/8.0f);
    //}

    void move(Vector2 location) {
        x = location.x;
        y = location.y;
    }
    
    public void remove()
    {
        World.w.remove(this);
    }
}
