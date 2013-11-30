package hellboss;

import java.awt.Point;

public class Mutation{
    
    public Player p;
    int lastMutation;

    public Mutation(Player p)
    {
        this.p = p;
    }
    
    public void fire(Point click, Collider coll){
        Vector2 temp = Vector2.fromPoint(click);
        temp.vecMult(1f/16f);
        temp.vecSubt(coll.location);
        temp.setLength(-60);
        coll.doImpulse(temp);
        //vel.add(temp);
        temp.setLength(-30);
        Projectile p = Projectile.PlayerBullet(temp, coll.location.clone());
        World.w.add(p);
        //Projectile p = Projectile.createShockwave(coll.location.clone(), 1);
        //World.w.add(p);
    }
    
    public void mutate()
    {
        lastMutation++;
        if(lastMutation > 2) lastMutation = 0;
        
        switch(lastMutation)
        {
            case 0:
                p.coll.mass = 50;
                p.coll.maxForce = 600;
                p.coll.topSpeed = 10;
                p.coll.size = 1.2f;
                p.drawer.sprite.spriteX = 64 * 3;
                break;
            case 1:
                p.coll.mass = 8;
                p.coll.maxForce = 250;
                p.coll.topSpeed = 18;
                p.coll.size = 0.7f;
                p.drawer.sprite.spriteX = 0;
                break;
            case 2:
                p.coll.mass = 1;
                p.coll.maxForce = 150;
                p.coll.topSpeed = 30;
                p.coll.size = 0.25f;
                p.drawer.sprite.spriteX = 64 * 4;
                break;
        }
        
    }
}
