package com.example.rahul.telly;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CustomerHome extends AppCompatActivity {

    ViewPager vp1;
    ActionBar ab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        vp1=(ViewPager)findViewById(R.id.vp1);
        FragmentManager fragmentManager=getSupportFragmentManager();
        mypageradapter mypageradapter=new mypageradapter(fragmentManager);
        vp1.setAdapter(mypageradapter);
        ab=getSupportActionBar();
        mytabListener t1=new mytabListener();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ab.addTab(ab.newTab().setText("HOME").setTabListener(t1));
        ab.addTab(ab.newTab().setText("MOVIES").setTabListener(t1));
        ab.addTab(ab.newTab().setText("TVSHOWS").setTabListener(t1));

        vp1.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ab.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }
    class mypageradapter extends FragmentPagerAdapter
    {
        public mypageradapter(FragmentManager fm){super(fm);}

        @Override
        public Fragment getItem(int position) {

            if(position==0)
            {
                return new HomeFragment();
            }
            else if(position==1)
            {
             return new MoviesFragment();
            }
            else
            {
                return new FragmentTvShows();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
    class mytabListener implements ActionBar.TabListener
    {

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            vp1.setCurrentItem(tab.getPosition(),true);
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }
    }
}
