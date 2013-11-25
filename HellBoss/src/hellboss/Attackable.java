/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author George
 */
public class Attackable extends Component{
    public static Player player;
    public Vector2 loc;
    public float size;
    
    public float health;
    public float maxHealth;
    
    
    public Attackable(int maxH)
    {
        
        Color[] colors = new Color[3];
        colors[0] = new Color(0, 0, 0, 128);
        colors[1] = new Color(100, 100, 100, 128);
        colors[2] = new Color(100, 0, 0, 128);
        //ui = new UIRegion(new Rectangle(w, h), 10, colors, this);
        maxHealth = health = maxH;
        //parent = p;
    }
    
    public void move(Vector2 worldP)
    {
        //ui.region.setLocation(screenP.x + xOffset, screenP.y + yOffset);
        loc = worldP;
    }
    
    public float takeDamage(int damageType, float damage)
    {
        //System.out.println("Ow");
        //System.out.println("-" + String.valueOf(damage));
        //System.out.println(maxHealth);
        //System.out.println(health);

        switch(damageType)
        {
            case 0:
                health -= damage;
                return damage;
            default:
                return 0;
        }
    }
    public Vector2 getLoc()
    {
        return loc.clone();
    }
    
    public static void setPlayer(Player p)
    {player =  p;}

    public boolean alive() 
    {
        return health > 0;
    }
    
    public void remove()
    {
        //ui.remove();
        World.w.remove(this);
    }
}
