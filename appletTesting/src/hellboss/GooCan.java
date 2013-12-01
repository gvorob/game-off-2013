/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author George
 */
public class GooCan extends ObjectController {
    public static Random r = new Random();
    
    public SpriteDrawer drawer;
    public Collider coll;
    
    public ArrayList<Vector2> canSpawns;
    
    
    public GooCan(Vector2 loc)
    {
        drawer = new SpriteDrawer(new SpriteData(1, 64, 0, 64, 64), -32, -32);
        coll = new Collider(null, 1, Collider.density.NONE);
        canSpawns = new ArrayList<Vector2>();
        
        World.w.add(drawer);
        World.w.add(coll);
    }
    
    public void spawn()
    {
        if(canSpawns.size() > 0)
        {
            int i =r.nextInt(canSpawns.size());
            if(coll.location != null && coll.location.equals(canSpawns.get(i)))
            {
                spawn();
                return;
            }
            coll.location = canSpawns.get(i).clone();
            drawer.move(coll.location);
        }
    }
    
    public void update(float t)
    {
        if(coll.location == null)
        {spawn();}
        if(World.w.player.coll.checkCollide(coll))
        {
            spawn();
            World.w.player.getCan();
        }
    }
    
    public void remove()
    {
        World.w.remove(drawer);
        World.w.remove(coll);
    }

    boolean checkRemove() {
        return false;
    }
}
