package com.example.rahul.telly;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

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


import static java.lang.System.load;




public class MyFavourites extends AppCompatActivity {

    LinearLayout dynamicFavoriteLinearLayout;

    CarouselView carouselView;
    ArrayList<String> carouselimages;

    ArrayList<Integer> favmovieid;
    ArrayList<String> favmovieimages;

    Thread th,th1;
    String Username="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favourites);

        SharedPreferences sp=getSharedPreferences("MyPref",MODE_PRIVATE);
        Username=sp.getString("username","");


        dynamicFavoriteLinearLayout=(LinearLayout)findViewById(R.id.dynamicFavoriteLinearlayout);
        carouselView=(CarouselView)(findViewById(R.id.favcarousel));

        dynamicFavoriteLinearLayout.removeAllViews();

        carouselimages=new ArrayList<>();

        favmovieid=new ArrayList<>();
        favmovieimages=new ArrayList<>();

        th=new Thread(new mycarousel());
        th.start();

        th1=new Thread(new favmovies());
        th1.start();


    }

    //     Thie Thread shows Movies Carousal
    class mycarousel implements Runnable
    {

        @Override
        public void run() {
            try {
                carouselimages.clear();
                URL url=new URL(Global.global+"CustomerPictures?opr=common"+"&Username="+Username);
                HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                int rescode=connection.getResponseCode();
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
                    }
                    JSONObject jsonObject=new JSONObject(sb.toString());
                    JSONArray jsa=jsonObject.getJSONArray("ans");
                    for(int i=0;i<jsa.length();i++)
                    {
                        JSONObject js=jsa.getJSONObject(i);
                        String path=js.getString("Coverphoto");
                        String encodedpath="";
                        for(int j=0;j<path.length();j++){
                            char ch=path.charAt(j);

                            if(ch==' '){
                                encodedpath+="%20";
                            }
                            else{
                                encodedpath+=ch;
                            }
                        }

                        carouselimages.add(encodedpath);

                    }
                    carouselView.setImageListener(new ImageListener() {
                        @Override
                        public void setImageForPosition(int position, ImageView imageView) {

                            Picasso.with(getApplicationContext()).load(Global.global+carouselimages.get(position)).resize(500,500).centerInside().into(imageView);

                        }
                    });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            carouselView.setPageCount(carouselimages.size());
                        }
                    });

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    //    This Thread shows favorite movies Poster
    class favmovies implements Runnable
    {

        @Override
        public void run() {
            try {
                favmovieimages.clear();
                favmovieid.clear();
                URL url=new URL(Global.global+"AndroidFavourites?Username="+Username);
                HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                int rescode=connection.getResponseCode();
                Log.d("COMEDY", "rescode " + rescode);
                if(rescode==200)
                {

                    BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String message="";

                    StringBuffer sb=new StringBuffer();
                    while (true)
                    {
                        message=br.readLine();
                        if(message==null)
                        {
                            break;
                        }
                        sb.append(message);
                    }
                    Log.d("COMEDY", "sb.toString() " + sb.toString());
                    JSONObject jsonObject=new JSONObject(sb.toString());
                    JSONArray jsonArray=jsonObject.getJSONArray("ans");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject js=jsonArray.getJSONObject(i);
                        String path=js.getString("Poster");
                        String encodedpath="";

                        for(int j=0;j<path.length();j++){
                            char ch=path.charAt(j);

                            if(ch==' '){
                                encodedpath+="%20";
                            }
                            else{
                                encodedpath+=ch;
                            }
                            Log.d("COMEDY", "encodeed path " + encodedpath);
                        }
                        favmovieid.add(js.getInt("Movieid"));
                        favmovieimages.add(encodedpath);
                    }
                    for(int m=0;m<favmovieimages.size();m++)
                    {
                        final ImageView imageView=new ImageView(MyFavourites.this);
                        imageView.setLayoutParams(new LinearLayout.LayoutParams(350,350));
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageView.setPadding(5,2,5,2);
                        final int finalM = m;
                        final int finalM1 = m;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent in=new Intent(MyFavourites.this,CustomerMovieDetail.class);
                                in.putExtra("movieid",favmovieid.get(finalM1));
                                startActivity(in);
                            }
                        });
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dynamicFavoriteLinearLayout.addView(imageView);
                                Picasso.with(MyFavourites.this).load(Global.global+favmovieimages.get(finalM)).resize(500,500).centerInside().into(imageView);
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
