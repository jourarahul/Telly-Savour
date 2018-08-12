package com.example.rahul.telly;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTvShows extends Fragment {


    CarouselView carouselView;
    ArrayList<String> carimages;
    ArrayList<Integer> comedytvshowid;
    ArrayList<String> comedytvhshowimages;
    LinearLayout dynamiclinearlayoutcomedyshows;
    String username="";
    public FragmentTvShows() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_tv_shows, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sp=getActivity().getSharedPreferences("MyPref",MODE_PRIVATE);
        username=sp.getString("username","");
        carouselView=(CarouselView)(getActivity().findViewById(R.id.carouseltvshowfrag));
        carimages=new ArrayList<>();
        dynamiclinearlayoutcomedyshows=(LinearLayout)getActivity().findViewById(R.id.dynamiclinearlayoutcomedyshows);
        dynamiclinearlayoutcomedyshows.removeAllViews();
        comedytvshowid=new ArrayList<>();
        comedytvhshowimages=new ArrayList<>();
        Log.d("HF", "start " + "start");
        mycarousel obj=new mycarousel();
        Thread th=new Thread(obj);
        th.start();
        Log.d("HF", "after thread" + "after thread");
        comedytvshow comedytvshow=new comedytvshow();
        Thread th1=new Thread(comedytvshow);
        th1.start();

    }

    class mycarousel implements Runnable
    {

        @Override
        public void run() {
            try {
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
                            Picasso.with(getActivity().getApplicationContext()).load(Global.global+carimages.get(position)).resize(500,500).centerInside().into(imageView);
                        }
                    });
                    getActivity().runOnUiThread(new Runnable() {
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
                URL url=new URL(Global.global+"CustomerTvShows?opr=Comedy&Username="+username);
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
                    }
                    Log.d("HF", "start"+comedytvhshowimages);
                    for(int m=0;m<comedytvhshowimages.size();m++)
                    {
                        final ImageView imageView=new ImageView(getActivity().getApplicationContext());
                        imageView.setLayoutParams(new LinearLayout.LayoutParams(300,300));
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageView.setPadding(5,2,5,2);
                        final int finalM = m;
                        final int finalM1 = m;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent in=new Intent(getActivity(),CustomerTvShows.class);
                                in.putExtra("tvshowid",comedytvshowid.get(finalM1));
                                startActivity(in);
                            }
                        });
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dynamiclinearlayoutcomedyshows.addView(imageView);
                                Picasso.with(getActivity().getApplicationContext()).load(Global.global+comedytvhshowimages.get(finalM)).resize(500,500).centerInside().into(imageView);
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
