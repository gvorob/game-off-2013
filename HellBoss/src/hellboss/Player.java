/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

/**
 *
 * @author George
 */
public class Player extends ObjectController implements UIListener{
    DrawComp drawer;
    Collider coll;
    UIRegion interactRegion;
    Vector2 dir;
    float drag = 25f;
    float accel = 50;
    float topSpeed = 15;
    Vector2 vel;
    
    //Vector2 location;
    
    final float speed = 6;
    
    public Player()
    {
        dir = Vector2.Zero();
        vel = Vector2.Zero();
        SpriteData temp = new SpriteData(1, 0, 0, 64, 64);
        drawer = new DrawComp(temp,-32,-32);
        interactRegion = new UIRegion(new Rectangle(-50000, -50000, 100000, 100000), 0, this);
        coll = new Collider(true, new Vector2(0,0),1);
        World.w.add(drawer);
        World.w.add(interactRegion);
        World.w.add(coll);
    }
    
    public void update(float t)
    {
        vel.add(Vector2.vecMult(t * accel, dir));
        if(vel.length() < drag * t) 
            vel.setLength(0);
        else
            vel.setLength(vel.length() - drag * t);
        if(vel.length() > topSpeed) vel.setLength(topSpeed);
        //if(target != null)
        //{Collider.moveTowards(location, target, t * speed);}
        coll.move(Vector2.vecMult(t, vel));
        drawer.move(coll.location);
    }
    
    public Point getView()
    {
        Point view =  drawer.getPoint();
        view.y -= 250;
        view.x -= 250;
        return view;
    }

    @Override
    public void informClicked(int i, Mouse m) {
        switch(i)
        {
            case 0:
                createProjectile(m.get());
                //target = new Vector2((float)m.getX() / 16, (float)m.getY() / 16);
                break;
        }
    }
    
    private void createProjectile(Point click)
    {
        Vector2 temp = Vector2.fromPoint(click);
        temp.vecMult(1f/16f);
        temp.vecSubt(coll.location);
        temp.setLength(-10);
        vel.add(temp);
        temp.setLength(-15);
        Projectile p = new Projectile(temp, 100, 0, coll.location.clone(),0.5f);
        World.w.add(p);
    }
    
    public void processKeys(Keyboard keys)
    {
        dir = Vector2.Zero();
        if(keys.getKey(KeyEvent.VK_W))
        {dir.y -= 1;}
        if(keys.getKey(KeyEvent.VK_A))
        {dir.x -= 1;}
        if(keys.getKey(KeyEvent.VK_S))
        {dir.y += 1;}
        if(keys.getKey(KeyEvent.VK_D))
        {dir.x += 1;}
        dir.normalize();
    }
}