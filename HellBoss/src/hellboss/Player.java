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
    Mutation mutation;
    
    Attackable att;
    
    boolean noClip = false;
    
    //Vector2 location;
    
    final float speed = 6;
    public int canCount;
    public Player(Vector2 loc)
    {
	mutation = new Mutation();
        dir = Vector2.Zero();
        SpriteData temp = new SpriteData(1, 0, 0, 64, 64);
        drawer = new DrawComp(temp,-32,-32);
        interactRegion = new UIRegion(new Rectangle(-50000, -50000, 100000, 100000), 0, this);
        att = new Attackable(100);
        coll = new Collider(loc,0.7f,att,Collider.density.HARD, 10f,500f,15f,1);
        World.w.add(att);
        World.w.add(drawer);
        World.w.add(interactRegion);
        World.w.add(coll);
    }
    
    public void update(float t)
    {
        if(dir.length() != 0)
        {drawer.setRotate(Angles.getAngle(dir));}
        if(!noClip)
            coll.physMove(dir, t);
        else
        {
            coll.noClipPhysMove(dir, t);
        }
        //if(target != null)
        //{Collider.moveTowards(location, target, t * speed);}
        drawer.move(coll.location);
        
        if(! att.alive())
        {
            drawer.sprite.spriteX = 128;
            drawer.setRotate(0);
        }
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
        if(!att.alive())
            return;
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
	mutation.fire(click, coll);

    }
    
    public void processKeys(Keyboard keys)
    {
        dir = Vector2.Zero();
        if(!att.alive())
            return;
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
    
    public void swapNoClip()
    {noClip = !noClip;}
    
    public void getCan()
    {canCount++;}
}