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
    protected SpriteDrawer drawer;
    protected Attackable att;
    protected Collider coll;
    protected final float speed = 2;
    protected static Random r = new Random();
    
    protected static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    
    protected void init(Vector2 loc)
    {}
    
    public Enemy(Vector2 loc)
    {
        init(loc);
    }

    public boolean checkRemove()
    {
        return !att.alive();
    }
    
    public void remove()
    {
        att.remove();
        drawer.remove();
        coll.remove();
        enemies.remove(this);
    }
}
