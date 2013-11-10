/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

import java.util.ArrayList;

/**
 *
 * @author George
 */
public class Collider {//handles collisions, also basic movement
    enum density{NONE,SOFT,HARD,WALL}//soft vs hard enemies, soft ones can be pierced
    
    
    public static ArrayList<Collider> colliders;
    Vector2 location;
    float size;
    Attackable att;
    density dense;
    float maxForce = 50;
    float topSpeed = 15;
    float mass;
    Vector2 vel;
    
    
    private void init(Vector2 loc, float s, Attackable a,density d, float mass, float force, float speed)
    {
        vel = Vector2.Zero();
        location = loc;
        size = s;
        att = a;
        dense = d;
        this.mass = mass;
        maxForce = force;
        topSpeed = speed;
    }
    
    public Collider( Vector2 loc, float s, density d)//stationary constructor
    {init(loc,s,null,d, -1, 0, 0);}
    
    public Collider(Vector2 loc, float s, Attackable a, float speed)//basic enemy
     {init(loc,s,a,density.SOFT, 1, speed, speed);}
    
    public Collider(Vector2 loc, float s, Attackable a,density d, float mass, float force, float speed)//full control
    {init(loc,s,a,d, mass, force, speed);}
    
    
    public static void moveTowards(Vector2 loc, Vector2 tar, float distance)
    {
        Vector2 temp = Vector2.vecSubt(tar, loc);
        if(temp.length() < distance)
        {
            loc.add(temp);
            
        }
        else
        {
            
            temp.setLength(distance);
            loc.add(temp);
        }
    }
    
    public void manualMove(Vector2 dir)
    {location.vecAdd(dir);}
    
    public Collider getColliderHere()
    {//returns only tangible collider which collide with this one
        for(Collider c : colliders)
        {
            if(!c.equals(this) && c.dense!=density.NONE && checkCollide(c))
                return c;
        }
        return null;
    }
    
    public void physMove(Vector2 dir, float t)//moves using physics, target velocity at dir.length out of 1
    {
        float oldSpeed = vel.length();
        dir.setLength(topSpeed);
        dir.vecSubt(vel);
        if(dir.length() > maxForce * t / mass)
        {
            dir.setLength(maxForce * t / mass);
            vel.add(dir);
        }
        else
            vel.add(dir);
        
        if(vel.length() > topSpeed) 
            if(oldSpeed > topSpeed)
                if(vel.length() > oldSpeed)
                    vel.setLength(oldSpeed);
                else
                    ;
            else
                vel.setLength(topSpeed);
        
        move(Vector2.vecMult(t, vel));
    }
    
    public void doImpulse(Vector2 impulse)
    {
        impulse.vecMult(1 / mass);
        vel.add(impulse);
    }
    
    public void move(Vector2 dir)//
    {
        location.vecAdd(dir);
        Collider temp = getColliderHere();
        if(temp != null)
        {
            location = closestPointTo(temp);
        }
    }
    
    public boolean checkCollide(Collider c)
    {
        return Vector2.vecSubt(location, c.location).length() < (size + c.size);
    }
    
    
    public Vector2 closestPointTo(Collider c)//finds the closest you can be to c;
    {
        Vector2 temp = Vector2.vecSubt(location, c.location);
        temp.setLength(c.size + size);
        temp.add(c.location);
        return temp;
    }
    
    
    public void remove()
    {
        World.w.remove(this);
    }
}
