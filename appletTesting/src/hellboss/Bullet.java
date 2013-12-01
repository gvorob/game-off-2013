/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

/**
 *
 * @author George
 */
public class Bullet extends Projectile{
    
    Vector2 vel;
    Vector2 tickVel;//distance travelled in a tick
    
    boolean hit; //set to true if projectile has hit its mark;
    
    public Bullet(Vector2 vel, int dam, int damt, Vector2 loc, float size, float mass, int team, SpriteDrawer d)
    {
        super( dam, damt, loc, size, team, d);
        
        //coll = new Collider(loc, size, null,Collider.density.NONE, mass, 0, 999, team);
        this.vel = vel;
        tickVel = vel.clone();
        Vector2 temp = vel.clone();
        temp.setLength(2f);
        coll.manualMove(temp);
        drawer.move(coll.location);
        drawer.setRotate(Angles.getAngle(vel));
    }
    
    public void update(float t)
    {
        tickVel.setLength(vel.length() * t);
        coll.manualMove(tickVel);
        Collider temp = coll.getColliderHere();
        {
            if(temp != null && temp.dense != Collider.density.NONE && temp.team != team)
            {
                if(temp.vulnerable())
                {
                    vel.vecMult(coll.mass);
                    temp.doImpulse(vel);
                }
                if(temp.att != null)
                {
                    temp.att.takeDamage(damageType, damage);
                }
                
                hit = true;
            }
        }
        drawer.move(coll.location);
        
        
    }
    
    public boolean checkRemove()
    {return hit;}
}
