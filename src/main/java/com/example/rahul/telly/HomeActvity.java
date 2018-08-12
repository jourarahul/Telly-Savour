package com.example.rahul.telly;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;


public class HomeActvity extends AppCompatActivity {
    TabLayout tl;
    ViewPager vp;
    MyPagerAdapter myAdapter;
    DrawerLayout dl;
    NavigationView nv;
    FragmentManager fm;
    ArrayList<Fragment> al=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_actvity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tl = (TabLayout) findViewById(R.id.tl);
        vp = (ViewPager) findViewById(R.id.vp);
        dl=(DrawerLayout)findViewById(R.id.dl);
        nv=(NavigationView)findViewById(R.id.nv);
        al.add(new HomeFragment());
        al.add(new MoviesFragment());
        al.add(new FragmentTvShows());

        fm=getSupportFragmentManager();
        myAdapter=new MyPagerAdapter(fm);

        tl.setupWithViewPager(vp);
        vp.setAdapter(myAdapter);

        ActionBarDrawerToggle abdt=new ActionBarDrawerToggle(HomeActvity.this,dl,R.string.app_name,R.string.app_name);
        dl.addDrawerListener(abdt);
        abdt.syncState();
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.menuhome)
                {vp.setCurrentItem(0);}
                else if(item.getItemId()==R.id.movies)
                {vp.setCurrentItem(1);}
                else if(item.getItemId()==R.id.tvshows){
                    vp.setCurrentItem(2);
                }
                else if(item.getItemId()==R.id.upgrade){
                    Intent in=new Intent(HomeActvity.this,PaymentActivity.class);
                    startActivity(in);
                }
                else if(item.getItemId()==R.id.changepassword){
                        Intent in=new Intent(HomeActvity.this,ChangePassword.class);
                        startActivity(in);
                }
                else if(item.getItemId()==R.id.myFavour){
                    Intent in=new Intent(HomeActvity.this,MyFavourites.class);
                    startActivity(in);
                }
                else
                {

                }
                dl.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            if(dl.isDrawerOpen(GravityCompat.START))
            {dl.closeDrawer(GravityCompat.START);}
            else
            {dl.openDrawer(GravityCompat.START);}
        }

        return super.onOptionsItemSelected(item);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        String s[] = {"Home","Movies", "Tv Shows"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }




        @Override
        public Fragment getItem(int position) {
            return al.get(position);
        }

        @Override
        public int getCount() {
            return al.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return s[position];
        }
    }
}

