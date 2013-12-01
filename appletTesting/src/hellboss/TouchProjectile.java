/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

import java.util.ArrayList;

/**
 *
 * @author George
 */
public class TouchProjectile extends Projectile{//Used for when just touching an enemy will damage you
    float recoilVel;
    boolean isDead;
    
    public TouchProjectile(int dam, int damt, float v, Collider coll)//This is the collider of its parent, NOT TO BE CHANGED
    {
        super(dam, damt, coll.location, coll.size * 1.2f, coll.team, null);
        this.recoilVel = v;
        team = coll.team;
        isDead = false;
    }
    
    public void updateColl(Collider parent)
    {
        coll.location = parent.location;
        coll.size = parent.size * 1.05f;
    }
    
    public void update(float t)
    {
        if(isDead)return;
        ArrayList<Collider> temp = coll.getCollidersHere();
        for(Collider c: temp)
        {
            if(c != null && c.dense != Collider.density.NONE && c.team != team)
            {
                if(c.vulnerable())
                {
                    Vector2 tempVel = Vector2.vecSubt(c.location, coll.location);
                    tempVel.setLength(recoilVel);
                    c.setSpeed(tempVel);
                    //c.doImpulse(tempImpulse);
                }
                if(c.att != null)
                {
                    c.att.takeDamage(damageType, damage);
                }

            }
        }
        
    }
    
    public void kill()
    {isDead = true;}
    
    public boolean checkRemove()
    {
        return isDead;
    }
    
    public void remove()
    {
        World.w.remove(coll);
    }
}
