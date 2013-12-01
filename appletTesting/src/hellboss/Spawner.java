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
    int type;
    
    public static final int SLIME = 0;
    public static final int BANDIT = 1;
    public static final int GOLEM = 2;
    public static final int RAT = 3;
    
    public Spawner(Vector2 loc, float interv, int type)
    {
        this.loc = loc;
        interval = interv;
        this.type = type;
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
        switch(type)
        {
            case SLIME:
                World.w.add(new Slime(loc));                
                break;
            case BANDIT:
                World.w.add(new Bandit(loc));
                break;
            case GOLEM:
                World.w.add(new Golem(loc));
                break;
            case RAT:
                World.w.add(new Rat(loc));
                break;
        }
    }
}
