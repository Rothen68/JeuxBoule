package com.stephane.rothen.jeuxboule;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Définition d'un acteur présent sur l'ecran
 *     protected double x:
 *              position sur x
 *     protected double y:
 *              position sur y
 *     protected int image:
 *              index de l'image de l'objet dans le tableau de gestion des ressources
 *              @see : GameView#AjouterActeur()
 *     protected Paint p:
 *              classe paint lié à l'acteur
 *
 *
 * Created by rothen on 01/12/2014.
 */
public  class Acteur {

    protected double x;
    protected double y;
    protected int image;
    protected Paint p;

    /**
     * Constructeur
     * @param x
     *      position sur x
     * @param y
     *      position sur y
     * @param image
     *      index de l'image dans le tableau de ressources
     */
    public Acteur(double x, double y, int image) {
        this.x = x;
        this.y = y;
        this.image = image;
        p = new Paint();
    }

    /**
     * dessine l'objet
     * @param c
     *      canvas sur lequel dessiner l'objet
     * @param b
     *      bitmap correspondant à l'index l'image de l'acteur
     */
    public  void draw(Canvas c,Bitmap b){
        c.save();
        c.translate((float)x,(float)y);
        c.drawBitmap(b,0,0,p);
        c.restore();
    };

    /**
     * Gestion par défaut du timeout ( actualisation de l'écran )
     * @param gv
     * @return
     */
    public  boolean timeout(GameView gv){
        return false;
    };

    /**
     * retourne l'index de l'image de l'acteur dans le tableau de ressources
     * @return
     *      index de l'image
     */
    public int getImage()
    {
        return image;
    }

}
