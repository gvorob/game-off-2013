/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author George
 */
public class Enemy extends ObjectController{
    protected SpriteDrawer drawer;
    protected Attackable att;
    protected Collider coll;
    protected final float speed = 2;
    protected static Random r = new Random();
    
    public CircleDrawer hitFlash;
    
    protected static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    
    protected void init(Vector2 loc)
    {}
    
    public Enemy(Vector2 loc)
    {
        init(loc);
        hitFlash = new CircleDrawer(loc.x, loc.y, 0, Color.white, true,5);
        hitFlash.visible = false;
        World.w.add(hitFlash);
    }
    
    public void update(float t)
    {
        hitFlash.move(coll.location);
        hitFlash.setSize(coll.size * 1.2f);
        if(!att.vulnerable())
            hitFlash.visible = true;
        else
            hitFlash.visible = false;
    }

    public boolean checkRemove()
    {
        return !att.alive();
    }
    
    public void remove()
    {
        World.w.add(new Corpse(drawer));
        att.remove();
        drawer.remove();
        coll.remove();
        enemies.remove(this);
        World.w.remove(hitFlash);
    }
}
