package com.stephane.rothen.jeuxboule;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.SensorEvent;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Random;

/**
 * Classe principale de JeuxBoule
 *
 * Created by Stephane Rothen on 01/12/2014.
 */
public class GameView extends View {
    /**
     * SparseArray contenant les acteurs du jeux
     */
    private SparseArray<Acteur> lesActeurs;
    /**
     * SparseArray contenant les différentes images des acteurs du jeux
     */
    private SparseArray<Bitmap> lstRessources;
    /**
     * Instance de la classe Context lié à l'application
     */
    private Context c;
    /**
     * Paint spécifique à la classe
     */
    private Paint p;
    /**
     * permet de faire le lien entre l'accéléromètre et la classe
     */
    private double vecteurAcc[]={0.0,0.0};

    /**
     * Constante, état du bord du jeux, augmente le score et le temps
     */
    public static final int POSITIF =1;
    /**
     * Constante, état du bord du jeux, pas d'action significative
     */
    public static final int INACTIF =3;
    /**
     * Constante, état du bord du jeux, diminue le temps
     */
    public static final int NEGATIF =2;

    /**
     * Constante, identifie les bords du jeux
     */
    public static final int HAUT =0;
    /**
     * Constante, identifie les bords du jeux
     */
    public static final int BAS =1;
    /**
     * Constante, identifie les bords du jeux
     */
    public static final int GAUCHE =2;
    /**
     * Constante, identifie les bords du jeux
     */
    public static final int DROITE =3;

    /**
     * Constante, définit la couleur du texte
     */
    private static final int COULEUR_TEXTE = Color.rgb(0,0,255);
    /**
     * Constante, définit la couleur d'un bord lorsqu'il est positif
     */
    private static final int COULEUR_BARRE_POSITIF = Color.rgb(255,0,0);
    /**
     * Constante, définit la couleur d'un bord lorsqu'il est negatif
     */
    private static final int COULEUR_BARRE_NEGATIF = Color.rgb(0,255,0);
    /**
     * Constante, définit la couleur d'un bord lorsqu'il est inactif
     */
    private static final int COULEUR_BARRE_INACTIF = Color.rgb(0,0,0);

    /**
     * Constante, définit le facteur de délais pour le changement d'état des bords
     */
    private static final int DELAIS_CHGMT_BARRE = 100;
    /**
     * Délais actuel avant changement d'état des bords
     */
    private int delaisBarre;
    /**
     * Coefficient multipliant le facteur de délais pour le changement d'état des bords
     */
    private int coefDelaisBarre;
    /**
     * Objet Random pour les fonctions aléatoires
     */
    private Random rand;

    /**
     * Constante, facteur de malus de temps
     */
    public static final int SCORE_COEF_MALUS_TEMPS = 100;
    /**
     * Constante, facteur de bonus de temps
     */
    public static final int SCORE_COEF_BONUS_TEMPS = 20;
    /**
     * Constante, facteur de bonus de score
     */
    public static final int SCORE_COEF_BONUS_SCORE = 10;

    /**
     * Contient les états des bords du jeux
     */
    private int barre[];

    /**
     * Score actuel
     */
    private int score;
    /**
     * Temps restant avant game over
     */
    private int tempsRestant;
    /**
     * permet de compter le nombre de frames avant la décrémentation du temps
     */
    private int topTemps;
    /**
     * Si Game Over
     */
    private boolean gameOver;

    /**
     * Chaine de caractere permettant d'afficher le score et le temps restant
     */
    private String texte;

    /**
     * Permet d'acceder au vibreur
     */
    private Vibrator vibreur;
    /**
     * constante, durées de vibration
     */
    public static final int VIBREUR_POSITIF = 50;
    public static final int VIBREUR_NEGATIF = 100;
    public static final int VIBREUR_ALERTE = 10;

    /**
     * nombre d'alerte vibreur avant changement d'état des barres
     */
    private static final int VIBREUR_NBRE_ALERTE = 3;
    /**
     * etat actuel du nombre de repetition d'alerte avant changement d'état des barres
     */
    private int posVibreurAlerte;

    /**
     * spécifie les différentes valeurs pour l'ajout de boule en fonction du score
     */
    private static final int MULTIBOULES []=new int[] {1000,5000,10000,20000,40000};


    /**
     * Constructeur
     * @param context
     */
    public GameView(Context context) {
        super(context);
        initView(context);
    }

    /**
     * Constructeur
     * @param context
     * @param attrs
     */
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    /**
     * Constructeur
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    /**
     * initialise les données membres de la classe
     * @param context
     */
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


        vibreur = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        posVibreurAlerte=VIBREUR_NBRE_ALERTE;


    }

    /**
     * Ajoute un acteur au jeux
     * @param x
     *      position de l'acteur sur l'axe x
     * @param y
     *      position de l'acteur sur l'axe y
     * @param image
     *      index de l'image de l'acteur dans les ressources
     */
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

    /**
     * Permet d'ajouter un acteur animé
     * @param x
     *      position de l'acteur sur l'axe x
     * @param y
     *      position de l'acteur sur l'axe y
     * @param image
     *      index de l'image de l'acteur dans les ressources
     * @param vX
     *      vitesse de l'acteur sur l'axe x
     * @param vY
     *      vitesse de l'acteur sur l'axe y
     * @param aX
     *      accélération de l'acteur sur l'axe x
     * @param aY
     *      accélération de l'acteur sur l'axe y
     */
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


    /**
     * Gère le dessin du jeux sur le canvas
     * @param canvas
     *      canvas sur lequel le jeux est dessiné
     */
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
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("Game Over");
            alert.setMessage("Voulez-vous rejouer ?");
            alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Do something with value!
                    resetJeux();
                }
            });
            alert.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    System.exit(1);
                }
            });
            alert.show();

        }


    }

    /**
     * permet de définir l'état et affiche le bord dont l'index est passé en paramètre
     * @param barre
     *      index du bord
     * @param etat
     *      etat de la barre
     * @param c
     *      canvas sur lequel imprimer la barre
     */
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


    /**
     * Permet de gérer l'actualisation des paramètres des acteurs
     * @return
     */
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

            if(delaisBarre>DELAIS_CHGMT_BARRE*coefDelaisBarre - 50*posVibreurAlerte) {

                vibrer(VIBREUR_ALERTE);
                if(posVibreurAlerte>0)
                    posVibreurAlerte--;
                else
                    posVibreurAlerte=VIBREUR_NBRE_ALERTE+1;

            }

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

    /**
     * Renvois le tableau des vecteurs accélérations
     * @return
     */
    public double[] getVecteurAcc() {
        return vecteurAcc;
    }

    /**
     * Permet de définir les vecteurs accélération
     * @param vecteurAcc
     *      tableau contenant les nouvelles valeurs
     */
    public void setVecteurAcc(double[] vecteurAcc) {
        this.vecteurAcc = vecteurAcc;
    }

    /**
     * Permet d'ajouter le temps passé en parameètre au temps du jeux
     * @param temps
     *      quantité de temps à ajouter
     */
    public void ajouterTemps(int temps)
    {
        tempsRestant+=temps;
    }

    /**
     * Soustrait le temps passé en parametre au temps du jeux
     * @param temps
     *      quantité de temps à soustraire
     */
    public void soustraireTemps(int temps)
    {
        tempsRestant-=temps;
        if (tempsRestant<0)
        {
            tempsRestant=0;
            gameOver=true;
        }
    }

    /**
     * Renvois le tableau d'état des bords
     * @return
     */
    int[] getBarre()
    {
        return barre;
    }

    /**
     * Ajoute le score passé en parametre au score du jeux
     * @param s
     *      valeur de score à ajouter
     */
    void ajouterScore(int s)
    {
        score +=s;
    }

    /**
     * permet de faire vibrer le téléphone
     * @param valeur
     *      temps de vibration du téléphone
     */
    void vibrer(int valeur)
    {

        vibreur.vibrate(valeur);
    }

    /**
     * renvois le tableau des acteurs
     * @return
     *      tableau des acteurs
     */
    public SparseArray<Acteur> getLesActeurs()
    {
        return lesActeurs;
    }

    /**
     * permet de réinitialiser le jeux
     */
    public void resetJeux()
    {
        score = 0;
        tempsRestant=1000;
        delaisBarre=0;
        coefDelaisBarre=rand.nextInt(5);
        topTemps = 0;
        gameOver=false;
        posVibreurAlerte=VIBREUR_NBRE_ALERTE;
        p.setARGB(255,0,255,125);
        p.setTextSize(40);
        this.invalidate();
    }
}
