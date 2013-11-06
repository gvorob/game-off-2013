/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;


import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.ListIterator;
import javax.swing.event.MouseInputAdapter;


/**
 *
 * @author George
 */
public class Mouse extends MouseInputAdapter{
    private ArrayList<MouseEventListener> listeners;
    private int x;
    private int y;
    private int sx;//shift, for purposes of scrolling with the world
    private int sy;
    private boolean l;
    private boolean r;
    
    public Mouse()
    {
        listeners = new ArrayList<MouseEventListener>();
    }
    
    public void setShift(int tempx, int tempy)
    {
        sx = tempx;
        sy = tempy;
    }
    
    public void resetShift()
    {sx = sy = 0;}
    
    public void addListener(MouseEventListener e){listeners.add(e);}    
    public void removeListener(MouseEventListener e){listeners.remove(e);}
    
    public void mouseMoved(MouseEvent e){
        informMoved(x + sx,y + sy,e.getX(),e.getY(),l,r);
        x=e.getX();
        y=e.getY();
    }
    
    public void mouseDragged(MouseEvent e){
        mouseMoved(e);
    }
    
    public void mousePressed(MouseEvent e){
        if(e.getButton()==MouseEvent.BUTTON1)
        {
            l=true;
            informClicked(x + sx,y + sy,true,true);
        }
        if(e.getButton()==MouseEvent.BUTTON3)
        {
            r=true;
            informClicked(x + sx,y + sy,false,true);
        }
    }
    
    public void mouseReleased(MouseEvent e){
        if(e.getButton()==MouseEvent.BUTTON1)
        {
            l=false;
            informClicked(x + sx,y + sy,true,false);
        }
        if(e.getButton()==MouseEvent.BUTTON3)
        {
            r=false;
            informClicked(x + sx,y + sy,false,false);
        }
    }
    
    public void informClicked(int x, int y, boolean left, boolean down)
    {
        ListIterator<MouseEventListener> i = listeners.listIterator();
         while(i.hasNext())
         {
             i.next().mouseClicked(x, y, left, down);
         }
    }
    
    public void informMoved(int oldX, int oldY, int x, int y, boolean left, boolean right)
    {
        ListIterator<MouseEventListener> i = listeners.listIterator();
         while(i.hasNext())
         {
             i.next().mouseMoved(oldX, oldY, x, y, left, right);
         }
    }
    
    public int getX()    {return x + sx;}
    public int getY()    {return y + sy;}
    public Point get()   {return new Point(x + sx,y + sy);}
    public boolean getL(){return l;}
    public boolean getR(){return r;}
}
