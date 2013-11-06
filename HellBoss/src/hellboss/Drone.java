/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author George
 */
public class Drone extends ObjectController{
    DrawComp drawer;
    Vector2 location;
    Attackable att;
    final float speed = 2;
    Vector2 target;
    
    private void init(boolean blueTeam, UIListener l, Vector2 loc)
    {
        drawer = new DrawComp(new SpriteData(0,0,0,32,64), -15, -57);
        att = new Attackable(-8, -33, 19, 38, 500);
        World.w.add(att);
        World.w.add(drawer);
        
        location = loc.clone();
        target =  new Vector2(10,25);//location.clone();
    }
    
    public Drone(boolean blueTeam, UIListener l)
    {init(blueTeam,l,new Vector2(10, blueTeam?0.5f:49.5f));}
    
    public Drone(boolean blueTeam, UIListener l, Vector2 loc)
    {init(blueTeam,l,loc);}
    
    public void update(float t)
    {
        drawer.move(location);
        if(target != null)
        {
            Collider.moveTowards(location, target,t * speed);
        }
        Point UICorner = drawer.getPoint();
        att.move(UICorner,location);
        
    }

    public boolean checkRemove()
    {
        return att.alive();
    }
    
    public void remove()
    {
        att.remove();
        drawer.remove();
    }
}
