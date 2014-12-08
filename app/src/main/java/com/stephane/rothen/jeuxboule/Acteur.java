package com.stephane.rothen.jeuxboule;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by rcdsm11 on 01/12/2014.
 */
public  class Acteur {
    protected double x;
    protected double y;
    protected int image;
    Paint p;

    public Acteur(double x, double y, int image) {
        this.x = x;
        this.y = y;
        this.image = image;
        p = new Paint();
    }

    public  void draw(Canvas c,Bitmap b){
        c.save();
        c.translate((float)x,(float)y);
        c.drawBitmap(b,0,0,p);
        c.restore();
    };
    public  boolean timeout(GameView gv){
        return false;
    };
    public int getImage()
    {
        return image;
    }

}
