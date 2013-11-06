/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

/**
 *
 * @author George
 */
public class Collider {//handles collisions, also basic movement
    Vector2 location;
    float size;
    
    public Collider(Vector2 loc, float s)
    {
        location = loc;
        size = s;
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
    }
}
