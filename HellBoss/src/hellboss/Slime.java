/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

/**
 *
 * @author George
 */
public class Slime extends Enemy{
    private TouchProjectile touchAttack;
    
    protected void init(Vector2 loc)
    {
        drawer = new SpriteDrawer(new SpriteData(0,0,0,64,64), -30, -31);
        att = new Attackable(200);
        coll = new Collider(loc.clone(), 0.8f, att, Collider.density.SOFT, 5, 30, 5, 2);
        touchAttack = new TouchProjectile(30, 0, 10, coll);
        World.w.add(touchAttack);
        World.w.add(att);
        World.w.add(drawer);
        World.w.add(coll);
        enemies.add(this);
        //target =  new Vector2(10,25);//location.clone();
    }
    
    public Slime(Vector2 loc)
    {super(loc);}
    
    public void update(float t)
    {
        touchAttack.updateColl(coll);
        att.update(t);
        Vector2 moveTarget = new Vector2(0,0);
        for(Enemy e: enemies)
        {
            Vector2 temp = Vector2.vecSubt(coll.location, e.coll.location);
            temp.setLength((float)(Math.sin(temp.length()) / (Math.exp(Math.abs(temp.length())))));
            moveTarget.add(temp);
        }
        
        moveTarget.setLength(1);
        Vector2 temp = new Vector2(5f,0);
        temp.setAngle(r.nextFloat() * 2 * (float)Math.PI);
        moveTarget.add(temp);
        moveTarget.setLength(1);
        
        coll.physMove(moveTarget, t);
        att.move(coll.location);
        drawer.move(coll.location);
        
    }
    
    public void remove()
    {
        super.remove();
        touchAttack.kill();
    }
}
