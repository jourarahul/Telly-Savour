package com.example.rahul.telly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CustomerTvShows extends AppCompatActivity {

    int tvshowid;
    CarouselView carouselView;
    String username="";
    ArrayList<String> carimages;
    ArrayList<Integer> comedytvshowid;
    ArrayList<String> comedytvhshowimages;
    LinearLayout dynamiclinearlayoutepisodes;
    ArrayList<String> episodenoarray;

    ArrayList<String> episodeseasonarray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_tv_shows);
        Intent in=getIntent();
        tvshowid=in.getIntExtra("tvshowid",0);
        Toast.makeText(this, "tvshowid"+tvshowid, Toast.LENGTH_SHORT).show();
        carouselView=(CarouselView)findViewById(R.id.carouseltvepisodes);
        dynamiclinearlayoutepisodes=(LinearLayout)findViewById(R.id.dynamiclinearlayoutepisodes);
        dynamiclinearlayoutepisodes.removeAllViews();
        SharedPreferences sp=this.getSharedPreferences("MyPref",MODE_PRIVATE);
        username=sp.getString("username","");
        carimages=new ArrayList<>();
        comedytvshowid=new ArrayList<>();
        comedytvhshowimages=new ArrayList<>();
        episodenoarray=new ArrayList<>();

        episodeseasonarray=new ArrayList<>();
        new Thread(new mycarousel()).start();
        new Thread(new comedytvshow()).start();
    }
    class mycarousel implements Runnable
    {

        @Override
        public void run() {
            try {
                carimages.clear();
                Log.d("HF", "entered " + "entered");
                URL url=new URL(Global.global+"CustomerTvShows?opr=common&Username="+username);
                HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                int rescode=connection.getResponseCode();
                if(rescode==200)
                {
                    Log.d("HF", "rescode " + rescode);
                    BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String message="";
                    StringBuffer sb=new StringBuffer();
                    while(true)
                    {
                        message=br.readLine();
                        if(message==null)
                        {
                            break;
                        }
                        sb.append(message);
                    }
                    Log.d("HF", "sb " + sb.toString());
                    JSONObject jsonObject=new JSONObject(sb.toString());
                    JSONArray jsa=jsonObject.getJSONArray("ans");
                    for(int i=0;i<jsa.length();i++)
                    {
                        JSONObject js=jsa.getJSONObject(i);
                        Log.d("HF", "coverphoto " + js.getString("Coverphoto"));
                        carimages.add(js.getString("Coverphoto"));
                    }
                    Log.d("HF", "carousalimage length " + carimages.size());
                    carouselView.setImageListener(new ImageListener() {
                        @Override
                        public void setImageForPosition(int position, ImageView imageView) {
                            Log.d("HF", "image listner " +carimages.get(position).substring(1));
                            Picasso.with(CustomerTvShows.this.getApplicationContext()).load(Global.global+carimages.get(position)).resize(500,500).centerInside().into(imageView);
                        }
                    });
                    CustomerTvShows.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            carouselView.setPageCount(carimages.size());
                        }
                    });

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    class comedytvshow implements Runnable
    {

        @Override
        public void run() {
            comedytvshowid.clear();
            comedytvhshowimages.clear();
            try {
                Log.d("HF", "entered in tv show thread"+"enteredintvshow");
                URL url=new URL(Global.global+"CustomerTvShowsEpisodes?tvshowid="+tvshowid);
                HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                int rescode=connection.getResponseCode();
                Log.d("HF", "start " +rescode);
                if(rescode==200)
                {
                    BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String message="";
                    StringBuffer sb=new StringBuffer();
                    while(true)
                    {
                        message=br.readLine();
                        if(message==null)
                        {
                            break;
                        }
                        sb.append(message);
                        Log.d("HF", "start " +sb.toString());
                    }
                    JSONObject jsonObject=new JSONObject(sb.toString());
                    JSONArray jsonArray=jsonObject.getJSONArray("ans");
                    Log.d("second",jsonArray+"");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject js=jsonArray.getJSONObject(i);
                        comedytvshowid.add(js.getInt("Tvshowid"));
                        comedytvhshowimages.add(js.getString("Poster"));
                        episodenoarray.add(js.getString("Episodeno"));
                        episodeseasonarray.add(js.getString("Season"));
                        Log.d("HD","helloposter" + js.getString("Poster"));
                    }
                    Log.d("HF", "start"+comedytvhshowimages);
                    for(int m=0;m<comedytvhshowimages.size();m++)
                    {
                        final ImageView imageView=new ImageView(CustomerTvShows.this.getApplicationContext());
                        imageView.setLayoutParams(new LinearLayout.LayoutParams(300,300));
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageView.setPadding(5,2,5,2);
                        final int finalM = m;
                        final int finalM1 = m;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent in=new Intent(CustomerTvShows.this,TvShowEpisodeActivity.class);
                                in.putExtra("tvshowid",comedytvshowid.get(finalM1));
                                in.putExtra("episodeno",episodenoarray.get(finalM1));
                                in.putExtra("season",episodeseasonarray.get(finalM1));
                                startActivity(in);
                            }
                        });
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dynamiclinearlayoutepisodes.addView(imageView);
                                Picasso.with(CustomerTvShows.this.getApplicationContext()).load(Global.global+comedytvhshowimages.get(finalM)).resize(500,500).centerInside().into(imageView);
                            }
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}
