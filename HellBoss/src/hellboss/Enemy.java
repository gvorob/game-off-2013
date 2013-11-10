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
public class Enemy extends ObjectController{
    DrawComp drawer;
    Attackable att;
    Collider coll;
    final float speed = 2;
    Vector2 target;
    
    private void init(Vector2 loc)
    {
        drawer = new DrawComp(new SpriteData(0,0,0,64,64), -30, -31);
        att = new Attackable(500);
        coll = new Collider(loc.clone(), 0.5f, att, Collider.density.SOFT, 5, 30, 1);
        World.w.add(att);
        World.w.add(drawer);
        World.w.add(coll);
        target =  new Vector2(10,25);//location.clone();
    }
    
    public Enemy(Vector2 loc)
    {init(loc);}
    
    public void update(float t)
    {
        coll.physMove(new Vector2(0,1), t);
        Point UICorner = drawer.getPoint();
        att.move(UICorner,coll.location);
        drawer.move(coll.location);
        
    }

    public boolean checkRemove()
    {
        return att.alive();
    }
    
    public void remove()
    {
        att.remove();
        drawer.remove();
        coll.remove();
    }
    
    public Enemy clone()
    {
        return new Enemy(coll.location);
    }
}
