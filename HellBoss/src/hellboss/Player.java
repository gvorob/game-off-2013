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
    //Vector2 location;
    
    final float speed = 6;
    
    public Player()
    {
        Vector2 dir = Vector2.Zero();
        SpriteData temp = new SpriteData(1, 0, 0, 64, 128);
        drawer = new DrawComp(temp,-32,-116);
        interactRegion = new UIRegion(new Rectangle(-50000, -50000, 100000, 100000), 0, this);
        coll = new Collider(new Vector2(0,0),1);
        World.w.add(drawer);
        World.w.add(interactRegion);
    }
    
    public void update(float t)
    {
        //if(target != null)
        //{Collider.moveTowards(location, target, t * speed);}
        coll.move(Vector2.vecMult(t * speed, dir));
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
                //target = new Vector2((float)m.getX() / 16, (float)m.getY() / 16);
                break;
        }
    }

    public void clickedOn(Attackable att, Mouse m) {
        if(!m.getL())
        {
            createProjectile(att);
        }
    }
    
    private void createProjectile(Attackable att)
    {
        Projectile p = new Projectile(att, 100, 0, 50, coll.location.clone());
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