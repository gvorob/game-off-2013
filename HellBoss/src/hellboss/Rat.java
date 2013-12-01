/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

/**
 *
 * @author George
 */
public class Rat extends Enemy{
    Vector2 target;
    float timeLeft;
    
    float cooldown;
    boolean charging;
    
    TouchProjectile touchAttack;
    
    protected void init(Vector2 loc)
    {
        drawer = new SpriteDrawer(new SpriteData(0,192,0,64,64), -32, -32,6);
        drawer.move(loc);
        att = new Attackable(1);
        coll = new Collider(loc.clone(), 0.5f, att, Collider.density.SOFT, 2, 20, 7, 2);
        touchAttack = new TouchProjectile(30, 0, 10, coll);
        World.w.add(touchAttack);
        World.w.add(att);
        World.w.add(drawer);
        World.w.add(coll);
        enemies.add(this);
    }
    
    public Rat(Vector2 loc)
    {
        super(loc);
        charging = false;
    }
    
    public void update(float t)
    {
        super.update(t);
        if(target!=null)drawer.setRotate(Angles.getAngle(Vector2.vecSubt(target, coll.location)));
        touchAttack.updateColl(coll);
        att.update(t);
        if(target != null)
        {
            //Misc.prln("target not null");
            timeLeft -= t;
            Vector2 moveTarget = Vector2.vecSubt(target, coll.location);
            if(moveTarget.length() < 2f || timeLeft < 0)
            {
            //Misc.prln("arrived");
                target = null;
                coll.physMove(Vector2.Zero(), t);
                timeLeft = r.nextFloat() * 1 + 1;
                if(charging)
                {
                    coll.topSpeed /= 2;
                    charging = false;
                }
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
                target = Vector2.fromAngle(r.nextFloat() * 2 * (float)Math.PI, r.nextFloat() * 4 + 6);
                target.vecAdd(coll.location);
                timeLeft = r.nextFloat() *3 + 4;
            }
        }
        
            //Misc.prln("done");
            att.move(coll.location);
            drawer.move(coll.location);
            
        if(Vector2.dist(coll.location, World.w.player.coll.location) < 15 && !charging)
        {
            cooldown -= t;
            if(cooldown < 0)
                if(r.nextFloat() < 0.5f)
                    charge();
                else
                    cooldown = 1;
        }
    }
    
    void charge()
    {
        target = World.w.player.coll.location.clone();
        charging = true;
        coll.topSpeed *= 2;
        //Vector2 temp = Vector2.vecSubt(World.w.player.coll.location, coll.location);
        //temp.setLength(-15);
        //coll.doImpulse(temp);
        //vel.add(temp);
        //temp.setLength(-15);
        //Projectile p = Projectile.BanditBullet(temp, coll.location.clone());//new Projectile(temp, 100, 0, coll.location.clone(),0.5f, 1, 2);
        //World.w.add(p);
        
        //cooldown = 3;
    }
    
    public void remove()
    {
        super.remove();
        touchAttack.kill();
    }
}
