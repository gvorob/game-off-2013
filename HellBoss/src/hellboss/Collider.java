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
    
    
    public static ArrayList<Collider> colliders;
    Vector2 location;
    float size;
    boolean tangible;
    Attackable att;
    
    public Collider(boolean tan, Vector2 loc, float s)
    {
        location = loc;
        size = s;
        tangible = tan;
    }
    
    public Collider(Vector2 loc, float s, Attackable a)
     {
        location = loc;
        size = s;
        att = a;
        tangible = true;
    }
    
    
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
    
    public void move(Vector2 dir)
    {
        location.vecAdd(dir); 
        for(Collider c : colliders)
        {
            if(c.equals(this) || !c.tangible() || !checkCollide(c));
            else
            {
                location = closestPointTo(c);
                return;
            }
        }
    }
    
    public boolean checkCollide(Collider c)
    {
        return Vector2.vecSubt(location, c.location).length() < (size + c.size);
    }
    
    public boolean tangible()
    {return tangible;}
    
    public Vector2 closestPointTo(Collider c)//finds the closest place you can be to me
    {
        Vector2 temp = Vector2.vecSubt(location, c.location);
        temp.setLength(c.size + size);
        temp.add(c.location);
        return temp;
    }
    
    
}
