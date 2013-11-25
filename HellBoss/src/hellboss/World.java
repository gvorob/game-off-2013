/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author George Vorobyev <quaffle97@gmail.com>
 */
public class World implements UIListener{
    public static World w;
    
    public static boolean DEBUG = false;
    
    public enum editMode
    {
        WALL,CANSPAWN,REMOVEWALL
    }
    
    public Mouse mouse;
    public ArrayList<DrawComp> drawComps;
    public ArrayList<ObjectController> controllers;
    public ArrayList<UIRegion> ui;
    public ArrayList<Attackable> attackables;
    public ArrayList<Collider> colliders;
    
    
    public ArrayList<ObjectController>addQueue;//so that things added during the update loop are added at the end
    boolean objectsLocked;
    
    public Player player;
    public GooCan can;
    public DrawComp map;
    
    public ArrayList<Collider> mapColliders;//used for level saving
    
    
    public float collSize = 5;//used for the map-editor collider placement
    public editMode edit;
    
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
        objectsLocked = false;
        edit = editMode.WALL;
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
        
        w = this;
        
        init();
    }
    
    private void init()
    {
        drawComps = new ArrayList<DrawComp>();
        ui = new ArrayList<UIRegion>();
        controllers = new ArrayList<ObjectController>();
        addQueue = new ArrayList<ObjectController>();
        attackables = new ArrayList<Attackable>();
        colliders = new ArrayList<Collider>();
        mapColliders = new ArrayList<Collider>();
        Collider.colliders = colliders;
        
        
        map = new DrawComp(new SpriteData(2, 0, 0, 1920, 1280), 0, 0);
        drawComps.add(map);
        player = new Player(new Vector2(60,60));
        can = new GooCan(new Vector2(-100,-100));
        controllers.add(can);
        controllers.add(new Spawner(new Vector2(20,5), 3));
        controllers.add(new BanditSpawner(new Vector2(60,5), 15));
        controllers.add(player);
        Attackable.setPlayer(player);
        
        
        colliders.add(new Collider(new Vector2(-10000,40), 10000, Collider.density.HARD));
        colliders.add(new Collider(new Vector2(60,-10000), 10000, Collider.density.HARD));
        colliders.add(new Collider(new Vector2(60,10080), 10000, Collider.density.HARD));
        colliders.add(new Collider(new Vector2(10120,40), 10000, Collider.density.HARD));
        
        
        loadLevel("in.txt");
    }
    
    private void loadLevel(String fileName)
    {
        BufferedReader b = null;
        try {
            File f = new File(fileName);
            b = new BufferedReader(new FileReader(f));
            
            float length = Integer.parseInt(b.readLine());
            for(int i = 0;i< length;i++)//reads in all the walls
            {
                String[] line = b.readLine().split(",");
                Collider temp = new Collider(
                        new Vector2(Float.parseFloat(line[0]),Float.parseFloat(line[1])),
                        Float.parseFloat(line[2]),
                        Collider.density.WALL);
                colliders.add(temp);
                mapColliders.add(temp);
            }
            length = Integer.parseInt(b.readLine());
            for(int i = 0;i < length; i++)
            {
                String[] line = b.readLine().split(",");
                Vector2 temp = new Vector2(Float.parseFloat(line[0]),Float.parseFloat(line[1]));
                can.canSpawns.add(temp);
            }
            
        }
        catch(Exception ex)
        {
            Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                b.close();
            } catch (IOException ex) {
                Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    private void saveLevel()
    {
        BufferedWriter out = null;
        try //the opposite of parsetiles
        {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("out.txt")));
            out.write(String.valueOf(mapColliders.size()) + "\n");
            for(Collider c : mapColliders)
            {
                out.write(
                        String.valueOf(c.location.x) + "," +
                        String.valueOf(c.location.y) + "," +
                        String.valueOf(c.size) + "\n");
            }
            out.write(String.valueOf(can.canSpawns.size()) + "\n");
            for(Vector2 v: can.canSpawns)
            {
                out.write(
                        String.valueOf(v.x) + "," +
                        String.valueOf(v.y) + "\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void saveWorldImage()
    {
        BufferedImage b = new BufferedImage(1920, 1280, BufferedImage.TYPE_4BYTE_ABGR);
        draw(b, new Point(0,0));
        try {
    // retrieve image
            File outputfile = new File("saved.png");
            ImageIO.write(b, "png", outputfile);
        } catch (IOException e) {
            Misc.prln("error writing world image to file");
        }
    }
    
    public void update(float time, Keyboard keys, Mouse m)//per-frame game updates
    {
        Point view = player.getView();
        
        boolean flag = false;//used for click blocking for ui
        if(mode == MODE_PLAY)
        {
            if(DEBUG)
            {
                if(keys.getKeyPressed(KeyEvent.VK_BACK_SLASH))
                {
                    if(edit == editMode.CANSPAWN)edit = editMode.REMOVEWALL;
                    else if(edit == editMode.REMOVEWALL)edit = editMode.WALL;
                    else if(edit == editMode.WALL)edit = editMode.CANSPAWN;
                }
                if(keys.getKeyPressed(KeyEvent.VK_O))
                {
                    if(edit == editMode.WALL)
                    {
                        Collider temp = new Collider(player.coll.location.clone(), collSize, Collider.density.WALL);
                        colliders.add(temp);
                        mapColliders.add(temp);
                    }
                    if(edit == editMode.CANSPAWN)
                    {can.canSpawns.add(player.coll.location.clone());}
                    if(edit == editMode.REMOVEWALL)
                    {
                        Collider first = null;
                        for(Collider c: mapColliders)
                        {
                            if(player.coll.checkCollide(c))
                            {
                                first = c;
                                break;
                            }
                        }
                        if(first != null)
                        {
                            mapColliders.remove(first);
                            first.remove();
                        }
                    }
                }
                if(keys.getKeyPressed(KeyEvent.VK_N))
                {player.swapNoClip();}
                if(keys.getKeyPressed(KeyEvent.VK_OPEN_BRACKET))
                {collSize -= 0.5; if(collSize <= 0)collSize = 0.1f;}
                if(keys.getKeyPressed(KeyEvent.VK_CLOSE_BRACKET))
                {collSize += 0.5;}
                if(keys.getKeyPressed(KeyEvent.VK_MINUS))
                {collSize -= 0.1; if(collSize <= 0)collSize = 0.1f;}
                if(keys.getKeyPressed(KeyEvent.VK_EQUALS))
                {collSize += 0.1;}
                if(keys.getKeyPressed(KeyEvent.VK_0))
                {saveLevel();}
                if(keys.getKeyPressed(KeyEvent.VK_9))
                {saveWorldImage();}
            }
            
            
            if(keys.getKeyPressed(KeyEvent.VK_P))
            {DEBUG = !DEBUG;}
            if(!player.att.alive() && keys.getKeyPressed(KeyEvent.VK_R))
            {
                init();
            }
            
            player.processKeys(keys);
            objectsLocked = true;
            for(ObjectController o : controllers)
            {
                o.update(time);
            }
            objectsLocked = false;
            for(ObjectController o : addQueue)
            {add(o);}
            if(addQueue.size() > 0)
                addQueue = new ArrayList<ObjectController>();
            
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
                    //System.out.println("I AM DEAD");
                    controllers.get(i).remove();//run destructors
                    controllers.remove(i);
                }
            }
        }
    }
    
    public void draw(BufferedImage b, Point view)
    {
        Graphics2D g = b.createGraphics();
        g.translate(-1 * view.x, -1 * view.y);
        for(DrawComp d : drawComps)
        {
            g.setColor(Color.red);
            Point p = d.getPoint();
            g.translate(p.x, p.y);
            g.rotate(d.getRotate());
            g.translate(-1 * p.x, -1 * p.y);
            d.draw().drawSprite(g, p.x + d.refx, p.y + d.refy, sprites);
            g.translate(p.x, p.y);
            g.rotate(-1 * d.getRotate());
            g.translate(-1 * p.x, -1 * p.y);
        }
        g.drawString(String.valueOf(player.canCount), view.x  + 400, view.y  + 16);
        if(DEBUG)
        {
            switch(edit)
            {
                case WALL:
                    g.drawString("Adding walls", view.x, view.y + 20);
                    g.drawOval(
                            (int)((player.coll.location.x - collSize) * 16),
                            (int)((player.coll.location.y - collSize) * 16), 
                            (int)(collSize * 2 * 16), 
                            (int)(collSize * 2 * 16));
                    break;
                case REMOVEWALL:
                    g.drawString("Removing walls", view.x, view.y + 20);
                    break;
                case CANSPAWN:
                    g.drawString("Placing Can Spawn Locations", view.x, view.y + 20);
                    break;
            }
            for(Collider c : colliders)
            {
                g.drawOval(
                        (int)(c.location.x * 16 - c.size * 16),
                        (int)(c.location.y * 16 - c.size * 16),
                        (int)(c.size * 2 * 16),
                        (int)(c.size * 2 * 16)
                        );
            }
            for(Vector2 v: can.canSpawns)
            {
                g.setColor(Color.blue);
                g.drawLine((int)(v.x * 16) - 16,(int)(v.y * 16), (int)(v.x * 16) + 16, (int)(v.y * 16));
                g.drawLine((int)(v.x * 16),(int)(v.y * 16) - 16, (int)(v.x * 16), (int)(v.y * 16) + 16);
            }
            
        }
        for(UIRegion r:ui)
        {
            r.draw(g);
        }
        //entities = new ArrayList<Entity>();
        if(!player.att.alive())
            {
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.setColor(Color.BLACK);
                FontMetrics f = g.getFontMetrics();
                
                g.drawString(
                        "SHIT, YOU DEAD", 
                        view.x + 250 - f.stringWidth("SHIT, YOU DEAD") / 2, 
                        view.y + 250 - f.getHeight() / 2 );
                g.setFont(new Font("Arial",Font.BOLD,12));
                f = g.getFontMetrics();
                g.drawString(
                        "'R' to restart", 
                        view.x + 250 - f.stringWidth("'R' to restart") / 2, 
                        view.y + 250 - f.getHeight() / 2  + 40);
                
            }
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
    {if(objectsLocked)addQueue.add(o);else controllers.add(o);}
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
