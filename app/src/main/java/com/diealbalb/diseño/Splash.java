package com.diealbalb.diseño;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.diealbalb.R;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        ImageView mySubtitle = (ImageView) findViewById(R.id.logoSplash);
        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.fadein);
        mySubtitle.startAnimation(myanim);

        openApp(true);

    }

    private void openApp(boolean b) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this, Login.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}