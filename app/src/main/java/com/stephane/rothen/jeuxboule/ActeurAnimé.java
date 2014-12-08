package com.stephane.rothen.jeuxboule;

/**
 * Gestion d'un acteur animé
 *
 * Created by rothen on 01/12/2014.
 */
public class ActeurAnimé extends Acteur {

    //parametre de vitesse et d'accélération
    /**
     * vitesse de l'acteur sur l'axe X
     */
    private double vX;
    /**
     * vitesse de l'acteur sur l'axe Y
     */
    private double vY;
    /**
     * accélération de l'acteur sur l'axe X
     */
    private double aX;
    /**
     * accélération de l'acteur sur l'axe Y
     */
    private double aY;
    /**
     * Constante, taille de l'acteur sur X
     */
    private static final int TAILLEX = 40;
    /**
     * Constante, taille de l'acteur sur Y
     */
    private static final int TAILLEY = 40;
    /**
     * Constante, coéfficient d'ammortissement  du rebond
     */
    private static final double COEFF_REBOND = 0.7;
    /**
     * Constante, coéfficient d'accélération appliqué au retour de l'accélérotmetre
     */
    private static final double COEFF_ACCELERATION = 0.07;

    /**
     * tableau repetition, permet de detecter si la bille roule sur un bord pour la gestion des scores
     */
    private boolean repetition[];

    /**
     * Constructeur
     *
     * @param x
     *      position de l'acteur sur l'axe x
     * @param y
     *      position de l'acteur sur l'axe y
     * @param image
     *      index de l'image de l'acteur
     */
    public ActeurAnimé(double x, double y, int image) {
        super(x, y, image);
        vX=0;
        vY=0;
        aX=0;
        aY=0;
    }

    /**
     * Constructeur
     * @param x
     *      position de l'acteur sur l'axe x
     * @param y
     *      position de l'acteur sur l'axe y
     * @param image
     *  index de l'image de l'acteur
     * @param vitesseX
     *      vitesse de l'acteur sur l'axe x
     * @param vitesseY
     *      vitesse de l'acteur sur l'axe y
     * @param accelerationX
     *      accélération de l'acteur sur l'axe x
     * @param accelerationY
     *      accélération de l'acteur sur l'axe y
     */
    public ActeurAnimé(double x, double y, int image, double vitesseX, double vitesseY,double accelerationX, double accelerationY) {
        super(x, y, image);
        vX=vitesseX;
        vY=vitesseY;
        aX=accelerationX;
        aY=accelerationY;
        repetition= new boolean[]{false,false,false,false};
    }

    /**
     * Met à jour la position de l'acteur
     * @param gv
     *      Instance de la classe GameView qui contient l'objet, pour la détection des collisions
     * @return
     *      true : l'acteur à été mis à jour
     */
    public boolean timeout(GameView gv) {
        double acc[]= gv.getVecteurAcc();
        int barre[]=gv.getBarre();

        aX=-acc[0]*COEFF_ACCELERATION;
        aY=acc[1]*COEFF_ACCELERATION;
        vX+=aX;
        vY+=aY;
        x+=vX;
        y+=vY;

        for (int i=0; i < gv.getLesActeurs().size();i++)
        {
            ActeurAnimé a =(ActeurAnimé) gv.getLesActeurs().get(i);
            double dx = x-a.x;
            double dy = y-a.y;
            if(Math.abs(dx)<TAILLEX && Math.abs(dy)<TAILLEY)
            {

            }
        }

        if ( (x>gv.getWidth()-TAILLEX)||(x<0)||(y>gv.getHeight()-TAILLEY)||(y<0)) {
            if (x > gv.getWidth() - TAILLEX) {
                vX = -vX * COEFF_REBOND;
                x = gv.getWidth() - TAILLEX;
                if (!repetition[GameView.DROITE]) {
                    if (barre[GameView.DROITE] == GameView.POSITIF) {
                        gv.ajouterTemps((int) Math.abs(vX * GameView.SCORE_COEF_BONUS_TEMPS));
                        gv.ajouterScore((int) Math.abs(GameView.SCORE_COEF_BONUS_SCORE * vX));
                        gv.vibrer(GameView.VIBREUR_POSITIF);
                    } else if (barre[GameView.DROITE] == GameView.NEGATIF) {
                        gv.vibrer(GameView.VIBREUR_NEGATIF);
                        gv.soustraireTemps((int) Math.abs(vX * GameView.SCORE_COEF_MALUS_TEMPS));
                    }
                    repetition[GameView.DROITE] = true;
                }
            }
            if (x < 0) {
                vX = -vX * COEFF_REBOND;
                x = 0;
                if (!repetition[GameView.GAUCHE]) {
                    if (barre[GameView.GAUCHE] == GameView.POSITIF) {
                        gv.ajouterTemps((int) Math.abs(vX * GameView.SCORE_COEF_BONUS_TEMPS));
                        gv.ajouterScore((int) Math.abs(GameView.SCORE_COEF_BONUS_SCORE * vX));
                        gv.vibrer(GameView.VIBREUR_POSITIF);
                    } else if (barre[GameView.GAUCHE] == GameView.NEGATIF) {
                        gv.vibrer(GameView.VIBREUR_NEGATIF);
                        gv.soustraireTemps((int) Math.abs(vX * GameView.SCORE_COEF_MALUS_TEMPS));
                    }
                    repetition[GameView.GAUCHE] = true;

                }
            }
            if (y > gv.getHeight() - TAILLEY) {
                vY = -vY * COEFF_REBOND;
                y = gv.getHeight() - TAILLEY;
                if (!repetition[GameView.BAS]) {
                    if (barre[GameView.BAS] == GameView.POSITIF) {
                        gv.ajouterTemps((int) Math.abs(vY * GameView.SCORE_COEF_BONUS_TEMPS));
                        gv.ajouterScore((int) Math.abs(GameView.SCORE_COEF_BONUS_SCORE * vY));
                        gv.vibrer(GameView.VIBREUR_POSITIF);
                    } else if (barre[GameView.BAS] == GameView.NEGATIF) {
                        gv.vibrer(GameView.VIBREUR_NEGATIF);
                        gv.soustraireTemps((int) Math.abs(vY * GameView.SCORE_COEF_MALUS_TEMPS));
                    }
                    repetition[GameView.BAS] = true;
                }
            }
            if (y < 0) {
                vY = -vY * COEFF_REBOND;
                y = 0;
                if (!repetition[GameView.HAUT]) {
                    if (barre[GameView.HAUT] == GameView.POSITIF) {
                        gv.ajouterTemps((int) Math.abs(vY * GameView.SCORE_COEF_BONUS_TEMPS));
                        gv.ajouterScore((int) Math.abs(GameView.SCORE_COEF_BONUS_SCORE * vY));
                        gv.vibrer(GameView.VIBREUR_POSITIF);
                    } else if (barre[GameView.HAUT] == GameView.NEGATIF) {
                        gv.vibrer(GameView.VIBREUR_NEGATIF);
                        gv.soustraireTemps((int) Math.abs(vY * GameView.SCORE_COEF_MALUS_TEMPS));
                    }
                    repetition[GameView.HAUT] = true;
                }
            }
        }
        else
        {
            for (int i = 0 ; i < 4; i++)
                repetition[i]=false;
        }
        return true;


    }
}
