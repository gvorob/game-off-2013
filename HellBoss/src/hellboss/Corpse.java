/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

/**
 *
 * @author George
 */
public class Corpse extends ObjectController{
    SpriteDrawer drawer;
    float timeLeft;
    
    public Corpse(SpriteDrawer d)
    {
        timeLeft = 30;
        drawer = new SpriteDrawer(d.sprite.clone(), d.refx, d.refy, 8);
        drawer.sprite.spriteY = 64;
        drawer.setRotate(Enemy.r.nextFloat() * 2 * (float)Math.PI);
        drawer.move(new Vector2(d.x,d.y));
        World.w.add(drawer);
    }
    
    public void update(float t)
    {
        timeLeft -= t;
    }
    
    public boolean checkRemove()
    {
        return timeLeft <= 0 && Vector2.dist(World.w.player.coll.location, new Vector2(drawer.x,drawer.y)) > 25;
    }
    
    public void remove()
    {
        drawer.remove();
    }
    
}
