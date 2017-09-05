package com.youshi.personjw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by dianjie on 2016/7/26.
 */
public class Start extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent();
                i.setClass(Start.this,HomePage.class);
                startActivity(i);
                finish();
            }
        },2000);

    }
}
