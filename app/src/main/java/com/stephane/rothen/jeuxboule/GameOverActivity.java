package com.stephane.rothen.jeuxboule;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

/**
 * Created by stéphane on 08/12/2014.
 */
public class GameOverActivity extends ActionBarActivity implements View.OnClickListener {

    final static public String EXTRA_CHOIX = "choix";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over_layout);
        findViewById(R.id.btnOui).setOnClickListener(this);
        findViewById(R.id.btnNon).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        Intent i = getIntent();
        if ( v.getId() == R.id.btnOui)
        {

            i.putExtra(EXTRA_CHOIX,"oui");
        }
        else if (v.getId() == R.id.btnNon)
        {
            i.putExtra(EXTRA_CHOIX,"non");

        }
        setResult(RESULT_OK,i);
        finish();
    }
}
