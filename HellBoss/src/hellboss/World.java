/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author George Vorobyev <quaffle97@gmail.com>
 */
public class World implements UIListener{
    public static World w;
    
    public static final float worldWidth = 120;
    public static final float worldHeight = 80;
    public static boolean DEBUG = false;
    
    public static Font font;
    
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
    public SpriteDrawer map;
    
    public ArrayList<Collider> mapColliders;//used for level saving
    
    
    public float collSize = 5;//used for the map-editor collider placement
    public editMode edit;
    
    public static final int MODE_PLAY = 0;
    public static final int MODE_MENU = 1;
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
    
    public static int frames;
    public static float time;
    public static int fps;
    
    public World(Mouse m)
    {
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/images/prstartk.ttf"));
        } catch (Exception e)
        {
            Misc.prln("error loading font");
        }
        
        
        mode = MODE_PLAY;
        objectsLocked = false;
        edit = editMode.WALL;
        //createUI(MODE_ASSEMBLE);
        //editTile = new Tile(0,0);
        try {
            sprites = new BufferedImage[5];
            sprites[0] = ImageIO.read(getClass().getResourceAsStream("/images/drones.png"));
            sprites[1] = ImageIO.read(getClass().getResourceAsStream("/images/entities.png"));
            sprites[2] = ImageIO.read(getClass().getResourceAsStream("/images/map.png"));
            sprites[3] = ImageIO.read(getClass().getResourceAsStream("/images/projectiles.png"));
            sprites[4] = ImageIO.read(getClass().getResourceAsStream("/images/menu.png"));
            //assembleBG = ImageIO.read(new File("assemble.png"));
        } catch (IOException ex) {
            Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        mouse = m;
        
        w = this;
        
        startMenu();
    }
    
    private void startMenu()
    {
        mode = MODE_MENU;
        drawComps = new ArrayList<DrawComp>();
        drawComps.add(new SpriteDrawer(new SpriteData(4, 0, 0, 500, 500)));
        ui = new ArrayList<UIRegion>();
        SpriteData[] tempSprite = new SpriteData[3];
        tempSprite[0] = new SpriteData(4,   0, 500, 300, 100);
        tempSprite[1] = new SpriteData(4, 300, 500, 300, 100);
        tempSprite[2] = new SpriteData(4, 600, 500, 300, 100);
        SpriteData[] tempSprite2 = new SpriteData[3];
        tempSprite2[0] = new SpriteData(4,   0, 600, 300, 100);
        tempSprite2[1] = new SpriteData(4, 300, 600, 300, 100);
        tempSprite2[2] = new SpriteData(4, 600, 600, 300, 100);
        ui.add(new UIButton(new Rectangle(100, 250, 300, 100), 0, tempSprite, this, true, true));
        ui.add(new UIButton(new Rectangle(100, 375, 300, 100), 1, tempSprite2, this, true, true));
    }
    
    private void startGame()
    {
        mode = MODE_PLAY;
        
        drawComps = new ArrayList<DrawComp>();
        ui = new ArrayList<UIRegion>();
        controllers = new ArrayList<ObjectController>();
        addQueue = new ArrayList<ObjectController>();
        attackables = new ArrayList<Attackable>();
        colliders = new ArrayList<Collider>();
        mapColliders = new ArrayList<Collider>();
        Collider.colliders = colliders;
        
        
        map = new SpriteDrawer(new SpriteData(2, 0, 0, 1920, 1280), 0, 0);
        drawComps.add(map);
        player = new Player(new Vector2(60,60));
        can = new GooCan(new Vector2(-100,-100));
        controllers.add(can);
        controllers.add(new Spawner(new Vector2(26,24),  3,Spawner.SLIME));
        controllers.add(new Spawner(new Vector2(65,57),  3,Spawner.SLIME));
        controllers.add(new Spawner(new Vector2(46,71), 15, Spawner.BANDIT));
        controllers.add(new Spawner(new Vector2(60, 5), 15, Spawner.BANDIT));
        controllers.add(new Spawner(new Vector2(118, 51), 15, Spawner.BANDIT));
        controllers.add(new Spawner(new Vector2(15,45), 15, Spawner.GOLEM));
        controllers.add(new Spawner(new Vector2(87,69), 15, Spawner.GOLEM));
        controllers.add(new Spawner(new Vector2(111,27), 15, Spawner.GOLEM));
        controllers.add(new Spawner(new Vector2( 8, 7), 15, Spawner.RAT));
        controllers.add(new Spawner(new Vector2(19,76), 15, Spawner.RAT));
        controllers.add(new Spawner(new Vector2(84,18), 15, Spawner.RAT));
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
        drawWorld(b, new Point(0,0));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        World.time += time;
        frames += 1;
        if(World.time > 1)
        {
            World.time -= 1;
            fps = frames;
            frames = 0;
        }
        if(mode == MODE_PLAY)
        {
            updateWorld(time, keys, m);
        }
        if(mode == MODE_MENU)
        {
            for(UIRegion r: ui)
            {
                r.update(m);
            }
        }
    }
    
    public void updateWorld(float time, Keyboard keys, Mouse m)
    {
        Point view = player.getView();

        if(!DEBUG)
        {
            Dimension dim = HellBoss.s.screen.c.getSize();
            if(view.x < 0) view.x = 0;
            if(view.y < 0) view.y = 0;
            if(view.x + dim.width > worldWidth * 16) view.x = (int)(worldWidth * 16) - dim.width;
            if(view.y + dim.height > worldHeight * 16) view.y = (int)(worldHeight * 16) - dim.height;
        }

        boolean flag = false;//used for click blocking for ui

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
            startGame();
        }

        player.processKeys(keys);
        objectsLocked = true;
        for(ObjectController o : controllers)
        {o.update(time);}
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
    
    public void draw(BufferedImage b)
    {
        if(mode == MODE_PLAY)
        {   
            Point view = player.getView();
            drawWorld(b, view);
        }
        else if(mode == MODE_MENU)
        {
            Graphics2D g = b.createGraphics();
            for(DrawComp d : drawComps)
            {
                d.draw(sprites,new Point(0,0),g);
                //g.setColor(Color.red);
                //Point p = d.getPoint();
                //d.draw().drawSprite(g, p.x + d.refx, p.y + d.refy, sprites);
            }
            for(UIRegion r: ui)
            {
                r.draw(g);
            }
        }
         
    }
    
    public void drawWorld(BufferedImage b, Point view)
    {
        if(!DEBUG)
            {
                Dimension dim = HellBoss.s.screen.c.getSize();
                if(view.x < 0) view.x = 0;
                if(view.y < 0) view.y = 0;
                if(view.x + dim.width > worldWidth * 16) view.x = (int)(worldWidth * 16) - dim.width;
                if(view.y + dim.height > worldHeight * 16) view.y = (int)(worldHeight * 16) - dim.height;
            }

            Graphics2D g = b.createGraphics();
            g.translate(-1 * view.x, -1 * view.y);
            for(DrawComp d : drawComps)
            {
                d.draw(sprites,view,g);
                //g.setColor(Color.red);
                //Point p = d.getPoint();
                //g.translate(p.x, p.y);
                //g.rotate(d.getRotate());
                //g.translate(-1 * p.x, -1 * p.y);
                //d.draw().drawSprite(g, p.x + d.refx, p.y + d.refy, sprites);
                //g.translate(p.x, p.y);
                //g.rotate(-1 * d.getRotate());
                //g.translate(-1 * p.x, -1 * p.y);
            }
            //player.canCount = 123;
            g.setFont(font.deriveFont(Font.PLAIN, 12f));
            int tempWidth = g.getFontMetrics().stringWidth(String.valueOf(player.canCount));
            g.setColor(Color.lightGray);
            g.fillRect(view.x + 494 - tempWidth, view.y, tempWidth + 6, 18);
            g.setColor(Color.BLACK);
            g.drawString(String.valueOf(player.canCount), view.x  + 497 - tempWidth, view.y  + 14);
            if(DEBUG)
            {
                g.setColor(Color.red);
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
                g.drawString(player.coll.location.toString(), view.x, view.y + 34);
                g.drawString("FPS:" + fps, view.x, view.y + 50);

            }
            for(UIRegion r:ui)
            {
                r.draw(g);
            }
            //entities = new ArrayList<Entity>();
            if(!player.att.alive())
                {
                    g.setFont(font.deriveFont(24f));
                    
                    g.setColor(Color.BLACK);
                    FontMetrics f = g.getFontMetrics();

                    g.drawString(
                            "SHIT, YOU DEAD", 
                            view.x + 250 - f.stringWidth("SHIT, YOU DEAD") / 2, 
                            view.y + 250 - f.getHeight() / 2 );
                    g.setFont(font.deriveFont(18f));
                    f = g.getFontMetrics();
                    g.drawString(
                            "'R' to restart", 
                            view.x + 250 - f.stringWidth("'R' to restart") / 2, 
                            view.y + 250 - f.getHeight() / 2  + 40);

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
        if(mode == MODE_MENU)
        {
            if(i == 0)
            {
                startGame();
            }
            else
            {
                System.exit(0);
            }
        }
    }
}
