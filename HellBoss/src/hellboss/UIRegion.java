/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author George Vorobyev <quaffle97@gmail.com>
 */
public class UIRegion {
    public Rectangle region;
    public int uiid;
    public Color[] colors;//0 is default, 1 is mouseover, 2 is mousedown;
    public int state;//0 is default, 1 is mouseover, 
    public boolean blocksClick;//defines whether it lets events through
    public boolean visible;//whether it is drawn or not
    //2 is mouse clicked and not yet released
    
    public UIListener parent;
    
    private void init(Rectangle rect, int id, Color[] c, UIListener listener, boolean block, boolean vis)
    {
        region = rect;
        uiid = id;
        colors = c;
        parent = listener;
        visible = vis; 
        blocksClick = block;
    }
    
    public UIRegion(Rectangle rect, int id, Color[] c)
    {init(rect,id,c,null,true,true);}
    public UIRegion(Rectangle rect, int id, Color[] c, UIListener listener)
    {init(rect,id,c,listener,true,true);}
    public UIRegion(Rectangle rect, int id, UIListener listener)
    {init(rect,id,null,listener,true,false);}
    
    public boolean update(Mouse m)
    {
        Point p = m.get();
        if(region.contains(p))
        {
            if(m.getL())
                state = 2;
            else 
            {
                if(state == 2)
                informClicked(m);
                state = 1;
            }
            return blocksClick;
        }
        else
            state = 0;
            return false;
    }
    
    public void draw(Graphics2D g)
    {
        if(visible)
        {
            g.setColor(colors[state]);
            g.fillRect(region.x, region.y, region.width, region.height);
        }
    }
    
    public void informClicked(Mouse m)
    {
        if(parent != null)
            parent.informClicked(uiid, m);
    }
    
    public void remove()
    {
        World.w.remove(this);
    }
}
