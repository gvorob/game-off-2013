/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

/**
 *
 * @author George Vorobyev <quaffle97@gmail.com>
 */
public class HellBoss implements TimerListener, KeyEventListener, MouseEventListener
{
    
    public static HellBoss s;
    
    public static void main(String[] args) {
        
        s = new HellBoss();
        long time = System.currentTimeMillis() - 1;
        while(true)
        {
            long temp = System.currentTimeMillis() - time;
            time = System.currentTimeMillis();
            s.run((float)temp / 1000f);
        }
        //s.timer.start();
    }
    
    public Screen screen;
    public Timer timer;
    public Keyboard keys;
    public Mouse mouse;
    public World world;
    
    public HellBoss()
    {
        screen = new Screen(500, 500, "HellBoss");
        timer = new Timer(1);
        timer.addListener(this);
        keys = new Keyboard();
        mouse = new Mouse();
        screen.c.addKeyListener(keys);
        screen.c.addMouseListener(mouse);
        screen.c.addMouseMotionListener(mouse);
        mouse.addListener(this);
        
        world = new World(mouse);
        
    }
    
    public void run(float t)
    {
        screen.clear();
        world.update(t, keys, mouse);
        world.draw(screen.buffer);
        screen.flushBuffer();
    }

    @Override
    public void timerEvent() {
        run((float)timer.interval / 1000);
    }

    @Override
    public boolean KeyChange(int index, boolean down) {
        
        return true;
    }

    @Override
    public boolean mouseClicked(int x, int y, boolean left, boolean down) {
        
        return true;
    }

    @Override
    public boolean mouseMoved(int oldX, int oldY, int x, int y, boolean left, boolean right) {
        
        return true;
    }
}
