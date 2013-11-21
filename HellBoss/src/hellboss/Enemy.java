/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

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
    static Random r = new Random();
    
    private static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    
    private void init(Vector2 loc)
    {
        drawer = new DrawComp(new SpriteData(0,0,0,64,64), -30, -31);
        att = new Attackable(200);
        coll = new Collider(loc.clone(), 0.5f, att, Collider.density.SOFT, 5, 30, 5, 2);
        World.w.add(att);
        World.w.add(drawer);
        World.w.add(coll);
        enemies.add(this);
        target =  new Vector2(10,25);//location.clone();
    }
    
    public Enemy(Vector2 loc)
    {init(loc);}
    
    public void update(float t)
    {
        Vector2 moveTarget = new Vector2(0,0);
        for(Enemy e: enemies)
        {
            Vector2 temp = Vector2.vecSubt(coll.location, e.coll.location);
            temp.setLength((float)(Math.sin(temp.length()) / (Math.exp(Math.abs(temp.length())))));
            moveTarget.add(temp);
        }
        
        moveTarget.setLength(1);
        Vector2 temp = new Vector2(5f,0);
        temp.setAngle(r.nextFloat() * 2 * (float)Math.PI);
        moveTarget.add(temp);
        moveTarget.setLength(1);
        
        coll.physMove(moveTarget, t);
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
        enemies.remove(this);
    }
    
    public Enemy clone()
    {
        return new Enemy(coll.location);
    }
}
