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
    Vector2 vel;
    Vector2 tickVel;//distance travelled in a tick
    float speed;
    int damage;
    int damageType;
    int team;//1 is player, 2 is enemy, 0 is neither
    boolean hit; //set to true if projectile has hit its mark;
    
    DrawComp drawer;
    
    public Projectile(Vector2 vel, int dam, int damt, Vector2 loc, float size, float mass, int team, DrawComp d)
    {
        coll = new Collider(loc, size, null,Collider.density.NONE, mass, 0, 999, team);
        drawer = d;
        damage = dam;
        damageType = damt;
        this.vel = vel;
        tickVel = vel.clone();
        Vector2 temp = vel.clone();
        temp.setLength(2f);
        coll.manualMove(temp);
        drawer.move(coll.location);
        drawer.setRotate(Angles.getAngle(vel));
        //doBounce();
        World.w.add(drawer);
        World.w.add(coll);
        this.team = team;
    }
    
    public static Projectile BanditBullet(Vector2 vel, Vector2 loc)
    {
        return new Projectile(vel, 100, 0, loc,0.1f, 1, 2,new DrawComp(new SpriteData(3,64,0,64,64), -32, -32));
    }
    
    public static Projectile PlayerBullet(Vector2 vel, Vector2 loc)
    {
        return new Projectile(vel, 100, 0, loc ,1f, 2, 1,new DrawComp(new SpriteData(3,0,0,64,64), -32, -32));
    }
    
    public void update(float t)
    {
        tickVel.setLength(vel.length() * t);
        coll.manualMove(tickVel);
        Collider temp = coll.getColliderHere();
        {
            if(temp != null && temp.dense != Collider.density.NONE && temp.team != team)
            {
                if(temp.att != null)
                {
                    temp.att.takeDamage(damageType, damage);
                }
                vel.vecMult(coll.mass);
                temp.doImpulse(vel);
                hit = true;
            }
        }
        drawer.move(coll.location);
        
        
    }
    
    public boolean checkRemove()
    {return hit;}
    
    public void remove()
    {
        drawer.remove();
        coll.remove();
    }
    
}
