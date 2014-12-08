package com.stephane.rothen.jeuxboule;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.SensorEvent;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by rcdsm11 on 01/12/2014.
 */
public class GameView extends View {
    private SparseArray<Acteur> lesActeurs;
    private SparseArray<Bitmap> lstRessources;
    private Context c;
    private Paint p;
    private double vecteurAcc[]={0.0,0.0};

    public static final int POSITIF =1;
    public static final int INACTIF =3;
    public static final int NEGATIF =2;
    public static final int HAUT =0;
    public static final int BAS =1;
    public static final int GAUCHE =2;
    public static final int DROITE =3;

    private static final int COULEUR_TEXTE = Color.rgb(0,0,255);
    private static final int COULEUR_BARRE_POSITIF = Color.rgb(255,0,0);
    private static final int COULEUR_BARRE_NEGATIF = Color.rgb(0,255,0);
    private static final int COULEUR_BARRE_INACTIF = Color.rgb(0,0,0);

    private static final int DELAIS_CHGMT_BARRE = 100;
    private int delaisBarre;
    private int coefDelaisBarre;
    private Random rand;


    public static final int SCORE_COEF_MALLUS_TEMPS = 100;
    public static final int SCORE_COEF_BONUS_TEMPS = 20;
    public static final int SCORE_COEF_BONUS_SCORE = 10;

    private int barre[];

    private int score;
    private int tempsRestant;
    private int topTemps;
    private boolean gameOver;
    private int fonduGameOver;
    private int coefFonduGameOver;
    private String texte;

    public GameView(Context context) {
        super(context);
        initView(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context)
    {
        lesActeurs= new SparseArray<Acteur>();
        lstRessources = new SparseArray<Bitmap>();
        c= context;
        lstRessources.append(lstRessources.size(), BitmapFactory.decodeResource(c.getResources(), R.drawable.bille));
        p= new Paint();
        p.setARGB(255,0,255,125);
        p.setTextSize(40);
        score = 0;
        tempsRestant=1000;
        delaisBarre=0;
        barre = new int[] {1,1,1,1};
        rand= new Random();
        coefDelaisBarre=rand.nextInt(5);
        topTemps = 0;
        gameOver=false;
        fonduGameOver=100;
        coefFonduGameOver=-1;


    }

    public void ajouterActeur(double x, double y, int image)
    {
        //recherche si l'image est déjà référencée
        boolean dejaReference=false;
        int index=0;
        for (int i = 0; i<lstRessources.size();i++)
        {
            if (lstRessources.get(i).sameAs(BitmapFactory.decodeResource(c.getResources(), image)))
            {
                dejaReference=true;
                index = i;
            }
        }
        if(!dejaReference)
        {
            lstRessources.append(lstRessources.size(), BitmapFactory.decodeResource(c.getResources(), image));
            index = lstRessources.size();
        }
        lesActeurs.append(lesActeurs.size(), new Acteur(x, y, index));

    }
    public void ajouterActeurAnime(double x, double y, int image,double vX,double vY,double aX, double aY)
    {
        //recherche si l'image est déjà référencée
        boolean dejaReference=false;
        int index=0;
        for (int i = 0; i<lstRessources.size();i++)
        {
            if (lstRessources.get(i).sameAs(BitmapFactory.decodeResource(c.getResources(), image)))
            {
                dejaReference=true;
                index = i;
            }
        }
        if(!dejaReference)
        {
            lstRessources.append(lstRessources.size(), BitmapFactory.decodeResource(c.getResources(), image));
            index = lstRessources.size();
        }
        lesActeurs.append(lesActeurs.size(), new ActeurAnimé(x, y, index,vX,vY,aX,aY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!gameOver){
            canvas.drawColor(Color.argb(125,125,125,125));

            for( int i=0;i<4;i++)
            {
                setBarre(i,barre[i],canvas);
            }
            for(int i = 0 ; i < lesActeurs.size();i++)
            {
                lesActeurs.valueAt(i).draw(canvas, lstRessources.get(lesActeurs.valueAt(i).getImage()));
            }
            texte = "Score : " + String.valueOf(score);


            texte = texte + " - Temps restant : " + String.valueOf(tempsRestant);
            p.setColor(COULEUR_TEXTE);
            canvas.drawText(texte,20,60,p);
        }
        else
        {
            canvas.drawColor(Color.argb(255,255,0,0));
            texte = "Score : " + String.valueOf(score);
            p.setColor(COULEUR_TEXTE);
            canvas.drawText(texte,20,60,p);
            p.setTextSize(100);
            canvas.drawText("GAME OVER",0,9,canvas.getWidth()/2-280,canvas.getHeight()/2,p);


        }


    }

    private void setBarre(int barre, int etat, Canvas c)
    {
        switch (etat)
        {
            case POSITIF:
                p.setARGB(255,0,255,0);
                break;
            case NEGATIF:
                p.setARGB(255,255,0,0);
                break;
            default:
                p.setARGB(0,0,0,0);
                break;

        }
        float f=0;
        switch(barre)
        {
            case HAUT:
                f = c.getWidth()/40;
                for (int i = 0 ; i < 20;i++)
                {
                    c.drawLine(f*i,i,c.getWidth()-f*i,i,p);
                }
                break;
            case BAS:
                f = c.getWidth()/40;
                for (int i = 0 ; i < 20;i++)
                {
                    c.drawLine(f*i,c.getHeight()-i,c.getWidth()-f*i,c.getHeight()-i,p);
                }
                break;
            case GAUCHE:
                f=c.getHeight()/40;
                for (int i = 0 ; i < 20;i++)
                {
                    c.drawLine(i,f*i,i,c.getHeight()-i*f,p);
                }
                break;
            case DROITE:
                f=c.getHeight()/40;
                for (int i = 0 ; i < 20;i++)
                {
                    c.drawLine(c.getWidth()-i,f*i,c.getWidth()-i,c.getHeight()-i*f,p);
                }
                break;
        }
    }



    public Boolean onTimeOut()
    {
        boolean update = false;
        if (!gameOver) {

            for (int i = 0; i < lesActeurs.size(); i++) {
                if (lesActeurs.valueAt(i).timeout(this)) {
                    update = true;
                }
            }
            if (update)
                this.invalidate();

            delaisBarre++;
            if (delaisBarre > DELAIS_CHGMT_BARRE * coefDelaisBarre) {
                delaisBarre = 0;
                for (int i = 0; i < 4; i++) {
                    barre[i] = rand.nextInt(3);
                }
                this.invalidate();
                coefDelaisBarre = rand.nextInt(10);
            }

            topTemps++;
            if (topTemps > 3)
            {
                topTemps = 0;
                tempsRestant--;
                if (tempsRestant <= 0 && !gameOver)
                {
                    gameOver = true;
                }
                this.invalidate();
            }
        }

        return update;
    }

    public double[] getVecteurAcc() {
        return vecteurAcc;
    }

    public void setVecteurAcc(double[] vecteurAcc) {
        this.vecteurAcc = vecteurAcc;
    }
    
    
    public void ajouterTemps(int temps)
    {
        tempsRestant+=temps;
    }
    public void soustraireTemps(int temps)
    {
        tempsRestant-=temps;
        if (tempsRestant<0)
        {
            tempsRestant=0;
            gameOver=true;
        }
    }
    int[] getBarre()
    {
        return barre;
    }
    void ajouterScore(int s)
    {
        score +=s;
    }
}
