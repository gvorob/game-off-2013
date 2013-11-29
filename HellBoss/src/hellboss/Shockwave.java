/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

/**
 *
 * @author George
 */
public class Shockwave extends Projectile{
    
    public Shockwave(Vector2 vel, int dam, int damt, Vector2 loc, float size, float mass, int team, DrawComp d)
    {
        super(vel,dam,damt,loc,size,mass,team,d);
    }
}
