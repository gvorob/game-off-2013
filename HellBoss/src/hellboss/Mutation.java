package hellboss;

import java.awt.Point;

public class Mutation{

    public void fire(Point click, Collider coll){
        Vector2 temp = Vector2.fromPoint(click);
        temp.vecMult(1f/16f);
        temp.vecSubt(coll.location);
        temp.setLength(-100);
        coll.doImpulse(temp);
        //vel.add(temp);
        temp.setLength(-15);
        Projectile p = new Projectile(temp, 100, 0, coll.location.clone(),0.25f, 1, 1);
        World.w.add(p);
    }
        
}
