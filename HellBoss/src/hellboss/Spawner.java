/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

/**
 *
 * @author George
 */
public class Spawner extends ObjectController{
    float interval;
    float timeToSpawn;
    Vector2 loc;
    
    public Spawner(Vector2 loc, float interv)
    {
        this.loc = loc;
        interval = interv;
    }
    
    public void update(float t)
    {
        timeToSpawn -= t;
        if(timeToSpawn < 0)
        {
            timeToSpawn += interval;
            spawn();
        }
    }
    
    public void spawn()
    {
        World.w.add(new Enemy(loc));
    }
}
