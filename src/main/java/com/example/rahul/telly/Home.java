package com.example.rahul.telly;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    ImageView imv1,imv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        imv1=(ImageView)(findViewById(R.id.imv1));
        imv2=(ImageView)(findViewById(R.id.imv2));
        Intent in=getIntent();
        String from = in.getStringExtra("from");
        if(from!=null && from.equals("OTP"))
        {
            imv1.setVisibility(View.GONE);
            imv2.setVisibility(View.GONE);
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction frt=fm.beginTransaction();
            frt.replace(R.id.list1,new Frag1());
            frt.commit();
        }
    }
    public void login(View view)
    {

        imv1.setVisibility(View.GONE);
        imv2.setVisibility(View.GONE);
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction frt=fm.beginTransaction();
        frt.replace(R.id.list1,new Frag1());
        frt.commit();
    }
}
