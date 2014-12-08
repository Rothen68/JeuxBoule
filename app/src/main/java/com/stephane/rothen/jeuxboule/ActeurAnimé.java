package com.stephane.rothen.jeuxboule;

/**
 *
 */
public class ActeurAnimé extends Acteur {

    //parametre de vitesse et d'accélération
    private double vX;
    private double vY;
    private double aX;
    private double aY;
    private static final int TAILLEX = 40;
    private static final int TAILLEY = 40;
    private static final double COEFF_REBOND = 0.7;
    private static final double COEFF_ACCELERATION = 0.07;

    private boolean repetition[];

    public ActeurAnimé(double x, double y, int image) {
        super(x, y, image);
        vX=0;
        vY=0;
        aX=0;
        aY=0;
    }
    public ActeurAnimé(double x, double y, int image, double vitesseX, double vitesseY,double accelerationX, double accelerationY) {
        super(x, y, image);
        vX=vitesseX;
        vY=vitesseY;
        aX=accelerationX;
        aY=accelerationY;
        repetition= new boolean[]{false,false,false,false};
    }


    public boolean timeout(GameView gv) {
        double acc[]= gv.getVecteurAcc();
        int barre[]=gv.getBarre();

        aX=-acc[0]*COEFF_ACCELERATION;
        aY=acc[1]*COEFF_ACCELERATION;
        vX+=aX;
        vY+=aY;
        x+=vX;
        y+=vY;

        if (x>gv.getWidth()-TAILLEX-1) {
            vX = -vX*COEFF_REBOND;
            x=gv.getWidth()-TAILLEX;
            if(!repetition[GameView.DROITE]) {
                if (barre[GameView.DROITE] == GameView.POSITIF) {
                    gv.ajouterTemps((int) Math.abs(vX * GameView.SCORE_COEF_BONUS_TEMPS));
                    gv.ajouterScore((int) Math.abs(GameView.SCORE_COEF_BONUS_SCORE * vX));
                } else if (barre[GameView.DROITE] == GameView.NEGATIF) {
                    gv.soustraireTemps((int) Math.abs(vX * GameView.SCORE_COEF_MALLUS_TEMPS));
                }
                repetition[GameView.DROITE]=true;
            }
        }else if(x<0)
        {
            vX = -vX*COEFF_REBOND;
            x=0;
            if(!repetition[GameView.GAUCHE]) {
                if (barre[GameView.GAUCHE] == GameView.POSITIF) {
                    gv.ajouterTemps((int) Math.abs(vX * GameView.SCORE_COEF_BONUS_TEMPS));
                    gv.ajouterScore((int) Math.abs(GameView.SCORE_COEF_BONUS_SCORE * vX));
                } else if (barre[GameView.GAUCHE] == GameView.NEGATIF) {
                    gv.soustraireTemps((int) Math.abs(vX * GameView.SCORE_COEF_MALLUS_TEMPS));
                }
                repetition[GameView.GAUCHE]=true;
            }
        }else if (y>gv.getHeight()-TAILLEY) {
            vY = -vY*COEFF_REBOND ;
            y=gv.getHeight()-TAILLEY;
            if(!repetition[GameView.BAS]) {
                if (barre[GameView.BAS] == GameView.POSITIF) {
                    gv.ajouterTemps((int) Math.abs(vY * GameView.SCORE_COEF_BONUS_TEMPS));
                    gv.ajouterScore((int) Math.abs(GameView.SCORE_COEF_BONUS_SCORE * vY));
                } else if (barre[GameView.BAS] == GameView.NEGATIF) {
                    gv.soustraireTemps((int) Math.abs(vY * GameView.SCORE_COEF_MALLUS_TEMPS));
                }
                repetition[GameView.BAS]=true;
            }
        }else if(y<0) {
            vY = -vY *COEFF_REBOND;
            y=0;
            if(!repetition[GameView.HAUT]) {
                if (barre[GameView.HAUT] == GameView.POSITIF) {
                    gv.ajouterTemps((int) Math.abs(vY * GameView.SCORE_COEF_BONUS_TEMPS));
                    gv.ajouterScore((int) Math.abs(GameView.SCORE_COEF_BONUS_SCORE * vY));
                } else if (barre[GameView.HAUT] == GameView.NEGATIF) {
                    gv.soustraireTemps((int) Math.abs(vY * GameView.SCORE_COEF_MALLUS_TEMPS));
                }
                repetition[GameView.HAUT]=true;
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
