package com.crivo.android.myapplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;
import java.util.Random;


public class Spaceship extends View{
    ArrayList<SpaceJunk> spaceJunkList = new ArrayList<SpaceJunk>(); 
    ArrayList<Missile> missileList = new ArrayList<Missile>();     
    ArrayList<Missile> destroyedMessileList = new ArrayList<Missile>();     
    long timePassed = SystemClock.uptimeMillis();         
    boolean doitonetimeyall = true;                       
    static public boolean dahFlag = false;   
    static boolean boomPlay = false;         
    static public int coordinate_x = 0;                
    static public int coordinate_y = 0;                
    int junkSelector = 0;                    
    Paint paint = new Paint();
    Point pt = new Point();
    Paint p = new Paint();
    static int score;

    
    int maxDecayRate = 6000, minDecayRate = 4000; 
    static public Direction shipDirection = Direction.NORTH; 
    boolean spaceshipShadow = true; 
    int shadowColor = Color.BLACK;   
    int missileColor = Color.WHITE; 
    boolean dualFire = false;       
    static public int shipVelocity = 20;      
    int junkDecayRate = 5000;       
    int maxJunk = 2;                
    int shipRadius = 100;                    


    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);

        int w = getWidth();
        int h = getHeight();

        if (doitonetimeyall){
            coordinate_x = (w/2); 
            coordinate_y = h-100;     
            doitonetimeyall = false; 
        }

        if (spaceshipShadow){
            pt.set(coordinate_x +3, coordinate_y +4);
            p.setStyle(Paint.Style.FILL);
            p.setColor(shadowColor);
            Path shadow = tri(pt, shipRadius);
            c.drawPath(shadow, p);
        }

        pt.set(coordinate_x, coordinate_y);
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.RED);
        Path path = tri(pt, shipRadius);
        c.drawPath(path, p);


        while (maxJunk !=0){
            spaceJunkList.add(new SpaceJunk());
            maxJunk--;
        }
        
        if (SystemClock.uptimeMillis() - timePassed > junkDecayRate){
            
            SpaceJunk j = spaceJunkList.get(junkSelector);
            spaceJunkList.remove(j);            
            spaceJunkList.add(new SpaceJunk()); 
            if (junkSelector >= spaceJunkList.size()-1){junkSelector = 0;}else {junkSelector++;}
            timePassed = SystemClock.uptimeMillis(); 
            junkDecayRate = new Random().nextInt(maxDecayRate - minDecayRate) + minDecayRate;
        }

        
        for (SpaceJunk j: spaceJunkList){
            paint.setColor(j.color);                
            c.drawCircle(j.cx, j.cy, j.r, paint);   
        }

        fireInTheHole();

        missileList.removeAll(destroyedMessileList);

        for (Missile m: missileList) {
            m.w = w; 
            m.h = h; 

            for (SpaceJunk j: spaceJunkList){
                
                if (inCircle(m.mx,m.my,j.cx,j.cy,j.r)){
                    
                    m.destroyME = true;                 
                    spaceJunkList.remove(j);                       
                    maxJunk++;                          
                    boomPlay = true;                    
                    score += j.scoreMply;               
                    break;
                }
            }

            paint.setColor(missileColor);           
            c.drawCircle(m.mx, m.my, m.mr, paint);  
            if (dualFire && (shipDirection == Direction.NORTH | shipDirection == Direction.SOUTH)){c.drawCircle(m.mx+ shipRadius, m.my, m.mr, paint);}
            if (dualFire && (shipDirection == Direction.EAST | shipDirection == Direction.WEST)){c.drawCircle(m.mx, m.my+ shipRadius, m.mr, paint);}

            m.lightSpeed();

            if (m.destroyME){
                destroyedMessileList.add(m);
            }
        }

        for (SpaceJunk j: spaceJunkList){
            switch (shipDirection){
                case NORTH:
                    if (inCircle(j.cx,j.cy, coordinate_x, coordinate_y, shipRadius)){
                        Log.d("INFO","YOU DEAD!");
                        nooooooooo();
                    }
                    break;
                case SOUTH:
                    if (inCircle(j.cx,j.cy, coordinate_x, coordinate_y, shipRadius)){
                        Log.d("INFO","YOU DEAD!");
                        nooooooooo();
                    }
                    break;
                case EAST:
                    if (inCircle(j.cx,j.cy, coordinate_x, coordinate_y, shipRadius)){
                        Log.d("INFO","YOU DEAD!");
                        nooooooooo();
                    }
                    break;
                case WEST:
                    if (inCircle(j.cx,j.cy, coordinate_x, coordinate_y, shipRadius)){
                        Log.d("INFO","YOU DEAD!");
                        nooooooooo();
                    }
                    break;
            }
        }

        
        if (coordinate_x + shipRadius > w) {       
            coordinate_x -= w - shipVelocity;
        }
        if (coordinate_x + shipRadius < shipRadius) {       
            coordinate_x += w - shipVelocity;
        }
        if (coordinate_y < shipRadius) {           
            coordinate_y += h - shipVelocity;
        }
        if (coordinate_y + shipRadius > h + shipRadius) {   
            coordinate_y -= h - shipVelocity;
        }
    }

    
    void nooooooooo(){
        shipDirection = Direction.NORTH;
        boomPlay = true;
        score=0;
        doitonetimeyall = true;
    }

    
    private boolean inCircle(float x, float y, float circleCenterX, float circleCenterY, float circleRadius) {
        double dx = Math.pow(x - circleCenterX, 2);
        double dy = Math.pow(y - circleCenterY, 2);
        if ((dx + dy) < Math.pow(circleRadius, 2)) {
            return true;
        } else {
            return false;
        }
    }

    
    public Path tri (Point p1, int width){
        Point p2 = null, p3 = null;

        if (shipDirection == Direction.NORTH) {
            p1.x = p1.x-width/2;
            p1.y = p1.y+width/2-13;
            p2 = new Point(p1.x + width, p1.y);
            p3 = new Point(p1.x + (width / 2), p1.y - width);
        }
        else if (shipDirection == Direction.SOUTH) {
            p1.x = p1.x-width/2;
            p1.y = p1.y-width/2+13;
            p2 = new Point(p1.x + width,p1.y);
            p3 = new Point(p1.x + (width / 2), p1.y + width);
        }
        else if (shipDirection == Direction.WEST) {
            p1.x = p1.x+width/2-13;
            p1.y = p1.y-width/2;
            p2 = new Point(p1.x, p1.y + width);
            p3 = new Point(p1.x - width, p1.y + (width / 2));
        }
        else if (shipDirection == Direction.EAST) {
            p1.x = p1.x-width/2+13;
            p1.y = p1.y-width/2;
            p2 = new Point(p1.x, p1.y + width);
            p3 = new Point(p1.x + width, p1.y + (width / 2));
        }
        Path path = new Path();
        path.moveTo(p1.x, p1.y);
        path.lineTo(p2.x, p2.y);
        path.lineTo(p3.x, p3.y);

        return path;
    }

    
    public void fireInTheHole(){
        if (dahFlag) {
            int oldR = shipRadius;
            if (dualFire){
                shipRadius =0;}
            Missile m = new Missile();
            switch (shipDirection) {
                case EAST:
                    m.my = coordinate_y;
                    m.mx = coordinate_x + shipRadius /2+13;
                    m.mr = 5;
                    m.warpDr = Missile.Direction.EAST;
                    dahFlag = false;
                    break;
                case WEST:
                    m.my = coordinate_y;
                    m.mx = coordinate_x - shipRadius /2-13;
                    m.mr = 5;
                    m.warpDr = Missile.Direction.WEST;
                    dahFlag = false;
                    break;
                case NORTH:
                    m.mx = coordinate_x;
                    m.my = coordinate_y - shipRadius /2-13;
                    m.mr = 5;
                    m.warpDr = Missile.Direction.NORTH;
                    dahFlag = false;
                    break;
                case SOUTH:
                    m.mx = coordinate_x;
                    m.my = coordinate_y + shipRadius /2+13;
                    m.mr = 5;
                    m.warpDr = Missile.Direction.SOUTH;
                    dahFlag = false;
                    break;
            }
            missileList.add(m);
            shipRadius =oldR;
        }
    }

    public enum Direction {
        NORTH, SOUTH, EAST, WEST, NONE;
    }

    public Spaceship(Context context) {
        super(context);
    }

    public Spaceship(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



}

