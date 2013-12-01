/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

/**
 *
 * @author George
 */
public class Golem extends Enemy{
    Vector2 target;
    float timeLeft;
    
    float cooldown;
    
    
    protected void init(Vector2 loc)
    {
        drawer = new SpriteDrawer(new SpriteData(0,128,0,64,64), -32, -32);
        drawer.move(loc);
        att = new Attackable(400);
        coll = new Collider(loc.clone(), 1.6f, att, Collider.density.SOFT, 25, 80, 3, 2);
        World.w.add(att);
        World.w.add(drawer);
        World.w.add(coll);
        enemies.add(this);
    }
    
    public Golem(Vector2 loc)
    {
        super(loc);
    }
    
    public void update(float t)
    {
        att.update(t);
        if(target != null)
        {
            //Misc.prln("target not null");
            timeLeft -= t;
            Vector2 moveTarget = Vector2.vecSubt(target, coll.location);
            if(moveTarget.length() < 1f || timeLeft < 0)
            {
            //Misc.prln("arrived");
                target = null;
                coll.physMove(Vector2.Zero(), t);
                timeLeft = r.nextFloat() * 3 + 2;
            }
            else
            {
            //Misc.prln("moving");
                moveTarget.setLength(1);
                coll.physMove(moveTarget, t);
            }
        }
        else
        {
            //Misc.prln("standing");
            timeLeft -= t;
            coll.physMove(Vector2.Zero(), t);
            if(timeLeft < 0)
            {
            //Misc.prln("done standing");
                target = Vector2.fromAngle(r.nextFloat() * 2 * (float)Math.PI, r.nextFloat() * 2 + 3);
                drawer.setRotate(Angles.getAngle(target));
                target.vecAdd(coll.location);
                timeLeft = r.nextFloat() * 3 + 2;
            }
        }
        
            //Misc.prln("done");
            att.move(coll.location);
            drawer.move(coll.location);
            
        if(Vector2.dist(coll.location, World.w.player.coll.location) < 30)
        {
            cooldown -= t;
            if(cooldown < 0)
                if(r.nextFloat() < 0.5f)
                    fire();
                else
                    cooldown = 1;
        }
    }
    
    void fire()
    {
        World.w.add(Projectile.createShockwave(coll.location, 2));
//        Vector2 temp = Vector2.vecSubt(World.w.player.coll.location, coll.location);
//        temp.setLength(-15);
//        coll.doImpulse(temp);
//        //vel.add(temp);
//        temp.setLength(-15);
//        Projectile p = Projectile.BanditBullet(temp, coll.location.clone());//new Projectile(temp, 100, 0, coll.location.clone(),0.5f, 1, 2);
//        World.w.add(p);
        
        cooldown = 3;
    }
}
