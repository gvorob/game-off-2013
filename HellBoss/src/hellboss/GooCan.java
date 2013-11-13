/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

/**
 *
 * @author George
 */
public class GooCan extends ObjectController {
    public DrawComp drawer;
    public Collider coll;
    
    
    public GooCan(Vector2 loc)
    {
        drawer = new DrawComp(new SpriteData(2, 64, 0, 64, 64), -32, -32);
    }
    
    public void update(float t)
    {}
    
    public void remove()
    {}

    boolean checkRemove() {
        return false;
    }
}
