/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

import java.awt.image.BufferedImage;

/**
 *
 * @author George
 */
public class Component {
    public ObjectController parent;
    
    public Component()
    {
        
    }
    
    public void registerParent(ObjectController p)
    {
        if(parent == null)
            parent = p;
    }
    
    //public void remove(){World.w.remove(this);};
}
