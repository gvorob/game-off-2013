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
    boolean hit; //set to true if projectile has hit its mark;
    
    DrawComp drawer;
    
    public Projectile(Vector2 vel, int dam, int damt, Vector2 loc, float size, float mass)
    {
        coll = new Collider(loc, size, null,Collider.density.NONE, mass, 0, 999);
        drawer = new DrawComp(new SpriteData(3,0,0,64,64), -32, -32);
        damage = dam;
        damageType = damt;
        this.vel = vel;
        tickVel = vel.clone();
        drawer.move(loc);
        //doBounce();
        World.w.add(drawer);
        World.w.add(coll);
    }
    
    public void update(float t)
    {
        tickVel.setLength(vel.length() * t);
        coll.manualMove(tickVel);
        Collider temp = coll.getColliderHere();
        {
            if(temp != null)
            {
                if(temp.att != null)
                {
                    vel.vecMult(coll.mass);
                    temp.doImpulse(vel);
                    hit = true;
                }
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
