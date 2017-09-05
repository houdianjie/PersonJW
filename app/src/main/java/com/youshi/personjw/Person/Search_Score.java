package com.youshi.personjw.Person;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.youshi.personjw.R;

/**
 * Created by dianjie on 2017/1/14.
 */

public class Search_Score extends Activity {
    private String COOK,HOST;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_score);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        COOK = bundle.getString("cookieVal");
        HOST = bundle.getString("host");
    }
    public void search_score_way_01(View view){
        Intent intent = new Intent();
        intent.setClass(this,Person_Score.class);
        Bundle bundle = new Bundle();
        bundle.putString("cookieVal",COOK);
        bundle.putString("host",HOST);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
    public void search_score_way_02(View view){
        Intent intent = new Intent();
        intent.setClass(this,Person_Score_nofinal.class);
        Bundle bundle = new Bundle();
        bundle.putString("cookieVal",COOK);
        bundle.putString("host",HOST);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}
