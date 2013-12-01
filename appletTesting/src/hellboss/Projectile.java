/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

/**
 *
 * @author George
 */
public class Projectile extends ObjectController{
    Collider coll;
    int damage;
    int damageType;
    int team;//1 is player, 2 is enemy, 0 is neither
    
    DrawComp drawer;
    
    public Projectile(int dam, int damt, Vector2 loc, float size, int team, DrawComp d)
    {
        coll = new Collider(loc, size, null,Collider.density.NONE, 0, 0, 999, team);
        drawer = d;
        damage = dam;
        damageType = damt;
        //doBounce();
        if(drawer!= null)
        {
            drawer.move(coll.location);
            World.w.add(drawer);
        }
        World.w.add(coll);
        this.team = team;
    }
    
    public static Bullet BanditBullet(Vector2 vel, Vector2 loc)
    {
        return new Bullet(vel, 100, 0, loc,0.1f, 1, 2,new SpriteDrawer(new SpriteData(3,64,0,64,64), -32, -32));
    }
    
    public static Bullet PlayerBullet(Vector2 vel, Vector2 loc)
    {
        return new Bullet(vel, 100, 0, loc ,1f, 2, 1,new SpriteDrawer(new SpriteData(3,0,0,64,64), -32, -32));
    }
    
    public static Shockwave createShockwave(Vector2 loc, int team)
    {
        return new Shockwave(40, 0, 40,loc, team, 0.2f, 1.6f, 5f);
    }
    
    public static Shockwave createExplosionShockwave(Vector2 loc)
    {
        return new Shockwave(9999, 0, 0, loc, 1, 0.1f, 0, 20);
    }
    
    public void update(float t)
    {
        drawer.move(coll.location);
    }
    
    public boolean checkRemove()
    {return true;}
    
    public void remove()
    {
        drawer.remove();
        coll.remove();
    }
    
}
