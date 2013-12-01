/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author George
 */
public class Shockwave extends Projectile{
    float duration;
    float timeAlive;
    float minSize, maxSize;
    float impulse;
    
    
    public Shockwave(int dam, int damt, float impulse, Vector2 loc, int team, float duration, float minSize, float maxSize)
    {
        super(dam,damt, loc, 0, team, new CircleDrawer(loc.x, loc.y, minSize, new Color(0, 100, 50),true,8));
        
        this.duration = duration;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.impulse = impulse;
    }
    
    public void update(float t)
    {
        ArrayList<Collider> temp = coll.getCollidersHere();
        {
            for(Collider c: temp)
            {
                if(c != null && c.dense != Collider.density.NONE && c.team != team)
                {
                    if(c.vulnerable())
                    {
                        Vector2 tempImpulse = Vector2.vecSubt(c.location, coll.location);
                        tempImpulse.setLength(impulse);
                        c.doImpulse(tempImpulse);
                    }
                    if(c.att != null)
                    {
                        c.att.takeDamage(damageType, damage);
                    }
                    
                }
            }
        }
        timeAlive += t;
        
        coll.size = minSize + (maxSize - minSize) * timeAlive / duration;
        drawer.setSize(coll.size);
    }
    
    public boolean checkRemove()
    {return timeAlive > duration;}
    
}
