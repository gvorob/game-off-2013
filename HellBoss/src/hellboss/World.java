/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author George Vorobyev <quaffle97@gmail.com>
 */
public class World implements UIListener{
    public static World w;
    
    public Mouse mouse;
    public ArrayList<DrawComp> drawComps;
    public ArrayList<ObjectController> controllers;
    public ArrayList<UIRegion> ui;
    public ArrayList<Attackable> attackables;
    public ArrayList<Collider> colliders;
    
    public Player player;
    public DrawComp map;
    
    
    
    public static final int MODE_PLAY = 0;
    //public static final int MODE_EDITOR = 1;
    //public static final int MODE_ASSEMBLE = 2;
    
    
    //public BufferedImage assembleBG;
    //public ArrayList<UIRegion> ui;
    
    public BufferedImage[] sprites;
    //public ArrayList<Entity> entities;
    //public Tile[][][] tiles;//x,y,z
    public int mode;
    int[] xyz;//holds the map size
    
    public int viewX = 250;
    public int viewY = 250;
    
    //private int editx,edity,editz;
    //private Tile editTile,editAlt;//editAlt is the tile currently under the cursor, used for swapping-out reasons
    
    public World(Mouse m)
    {
        mode = MODE_PLAY;
        //createUI(MODE_ASSEMBLE);
        //editTile = new Tile(0,0);
        try {
            sprites = new BufferedImage[4];
            sprites[0] = ImageIO.read(getClass().getResourceAsStream("/images/drones.png"));
            sprites[1] = ImageIO.read(getClass().getResourceAsStream("/images/entities.png"));
            sprites[2] = ImageIO.read(getClass().getResourceAsStream("/images/map.png"));
            sprites[3] = ImageIO.read(getClass().getResourceAsStream("/images/projectiles.png"));
            //assembleBG = ImageIO.read(new File("assemble.png"));
        } catch (IOException ex) {
            Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        mouse = m;
        
        drawComps = new ArrayList<DrawComp>();
        ui = new ArrayList<UIRegion>();
        controllers = new ArrayList<ObjectController>();
        attackables = new ArrayList<Attackable>();
        colliders = new ArrayList<Collider>();
        Collider.colliders = colliders;
        
        w = this;
        
        map = new DrawComp(new SpriteData(2, 0, 0, 640, 640), 0, 0);
        drawComps.add(map);
        player = new Player();
        controllers.add(new Drone(true, player));
        controllers.add(player);
        Attackable.setPlayer(player);
        
    }
    
    private void createUI(int mode)
    {
    }
    
    private void loadLevel()
    {
        
    }
    
    public void update(float time, Keyboard keys, Mouse m)//per-frame game updates
    {
        Point view = player.getView();
        
        boolean flag = false;//used for click blocking for ui
        if(mode == MODE_PLAY)
        {
            player.processKeys(keys);
            for(ObjectController o : controllers)
            {
                o.update(time);
            }
            for(UIRegion r : ui)
            {
                if(!flag)
                {
                    mouse.setShift(view.x, view.y);
                    if(r.update(mouse))
                        flag = true;
                }
                
            }
            
            for(int i = controllers.size() - 1; i >= 0; i--)
            {
                if(controllers.get(i).checkRemove())
                {
                    System.out.println("I AM DEAD");
                    controllers.get(i).remove();//run destructors
                    controllers.remove(i);
                }
            }
        }
    }
    
    public void draw(BufferedImage b)
    {
        Graphics2D g = b.createGraphics();
        Point view = player.getView();
        AffineTransform at = new AffineTransform();
        at.translate(-1 * view.x, -1 * view.y);
        g.setTransform(at);
        for(DrawComp d : drawComps)
        {
            g.setColor(Color.red);
            Point p = d.getPoint();
            d.draw().drawSprite(g, p.x + d.refx, p.y + d.refy, sprites);
        }
        for(UIRegion r:ui)
        {
            r.draw(g);
        }
        //entities = new ArrayList<Entity>();
        if(mode == MODE_PLAY)
        {
        }
         
    }
    
    public void add(DrawComp d)
    {drawComps.add(d);}
    public void add(UIRegion r)
    {ui.add(r);Collections.sort(ui, new Comparator<UIRegion>(){
        public int compare(UIRegion a, UIRegion b)
        {return b.uiid- a.uiid;}
    });}
    public void add(Attackable a)
    {attackables.add(a);}
    public void add(ObjectController o)
    {controllers.add(o);}
    public void add(Collider c)
    {colliders.add(c);}
    public void remove(DrawComp d)
    {while(drawComps.remove(d));}
    public void remove(UIRegion r)
    {while(ui.remove(r));}
    public void remove(Attackable a)
    {while(attackables.remove(a));}
    public void remove(Collider c)
    {while(colliders.remove(c));}
    
    public void informClicked(int i, Mouse m2)
    {
    }
}
