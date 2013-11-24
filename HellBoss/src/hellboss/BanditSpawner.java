/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

/**
 *
 * @author George
 */
public class BanditSpawner extends Spawner{
    
    public BanditSpawner(Vector2 loc, float interv)
    {
        super(loc,interv);
    }
    
    public void spawn()
    {
        World.w.add(new Bandit(loc));
    }
}
