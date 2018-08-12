package com.example.rahul.telly;


import android.app.ProgressDialog;
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
import android.widget.ProgressBar;
import android.widget.Toast;

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
public class MoviesFragment extends Fragment {


    CarouselView carouselView;
    ArrayList<String> carimages;
    ArrayList<Integer> comedymovieid;
    ArrayList<String> comedyimages;
    ArrayList<Integer> actionmovieid;
    ArrayList<String> actionimages;
    ArrayList<Integer> horrormovieid;
    ArrayList<String> horrorimages;
    ArrayList<Integer> romanticmovieid;
    ArrayList<String> romanticmovieimages;
    ProgressBar pbcomedy;
    LinearLayout dynamiclinearlayout;
    LinearLayout dynamiclinearlayoutaction;
    LinearLayout dynamiclinearlayouthorror;
    LinearLayout dynamiclinearlayoutromance;

String username="";
//    ProgressDialog  progressDialog;
//    boolean flag1=false,flag2=false,flag3=false,flag4=false;
    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sp=getActivity().getSharedPreferences("MyPref",MODE_PRIVATE);
        username=sp.getString("username","");
//        progressDialog=new ProgressDialog(getActivity());
        pbcomedy= (ProgressBar) getActivity().findViewById(R.id.pbcomedy);
//        progressDialog.setMessage("loading");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
        carouselView=(CarouselView)(getActivity().findViewById(R.id.carouselmoviefrag));
        dynamiclinearlayout=(LinearLayout)(getActivity().findViewById(R.id.dynamiclinearlayout));
        dynamiclinearlayoutaction=(LinearLayout)(getActivity().findViewById(R.id.dynamiclinearlayoutaction));
        dynamiclinearlayouthorror=(LinearLayout)getActivity().findViewById(R.id.dynamiclinearlayouthorror);
        dynamiclinearlayoutromance=(LinearLayout)getActivity().findViewById(R.id.dynamiclinearlayoutromance);
        dynamiclinearlayout.removeAllViews();
        dynamiclinearlayoutaction.removeAllViews();
        dynamiclinearlayouthorror.removeAllViews();
        dynamiclinearlayoutromance.removeAllViews();
        carimages=new ArrayList<>();
        comedymovieid=new ArrayList<>();
        actionmovieid=new ArrayList<>();
        actionimages=new ArrayList<>();
        horrormovieid=new ArrayList<>();
        horrorimages=new ArrayList<>();
        romanticmovieid=new ArrayList<>();
        romanticmovieimages=new ArrayList<>();
        Log.d("HF", "Hellooooo " + "start");
        comedyimages=new ArrayList<>();
        Log.d("HF", "start " + "start");
        mycarousel obj=new mycarousel();
        Thread th=new Thread(obj);
        comedy com=new comedy();
        Thread th1=new Thread(com);
        action act=new action();
        Thread th2=new Thread(act);
        horror hor=new horror();
        Thread th3=new Thread(hor);
        romance rom=new romance();
        Thread th4=new Thread(rom);
        th.start();
        th1.start();
        th2.start();
        th3.start();
        th4.start();
        Log.d("HF", "after thread" + "after thread");
        Log.d("Second", "Second Thread" + "Second thread");

    }

    class mycarousel implements Runnable
    {

        @Override
        public void run() {
            try {
                Log.d("HF", "entered " + "entered");
                URL url=new URL(Global.global+"CustomerPictures?opr=common"+"&Username="+username);
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
//                    flag1=true;
//                    progress();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    class comedy implements Runnable
    {

        @Override
        public void run() {
            try {
                comedymovieid.clear();
                comedyimages.clear();
                URL url=new URL(Global.global+"CustomerPictures?opr=Comedy"+"&Username="+username);
                HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                int rescode=connection.getResponseCode();
                Log.d("second", "rescode " + rescode);
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
                    JSONObject jsonObject=new JSONObject(sb.toString());
                    JSONArray jsonArray=jsonObject.getJSONArray("ans");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject js=jsonArray.getJSONObject(i);
                        comedymovieid.add(js.getInt("Movieid"));
                        comedyimages.add(js.getString("Poster"));
                    }
                    for(int m=0;m<comedyimages.size();m++)
                    {
                        final ImageView imageView=new ImageView(getActivity().getApplicationContext());
                        imageView.setLayoutParams(new LinearLayout.LayoutParams(300,300));
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageView.setPadding(5,2,5,2);
                        final int finalM2 = m;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent in=new Intent(getActivity(),CustomerMovieDetail.class);
                                in.putExtra("movieid",comedymovieid.get(finalM2));
                                startActivity(in);

                            }
                        });
                        final int finalM = m;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dynamiclinearlayout.addView(imageView);
                                Picasso.with(getActivity().getApplicationContext()).load(Global.global+comedyimages.get(finalM)).resize(500,500).centerInside().into(imageView);
                            }
                        });
//                        flag2=true;
//                        progress();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    class action implements Runnable
    {

        @Override
        public void run() {
            actionmovieid.clear();
            actionimages.clear();
            try {
                URL url=new URL(Global.global+"CustomerPictures?opr=Action"+"&Username="+username);
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
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
                    JSONArray jsonArray=jsonObject.getJSONArray("ans");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject js=jsonArray.getJSONObject(i);
                        actionmovieid.add(js.getInt("Movieid"));
                        actionimages.add(js.getString("Poster"));
                    }
                    for(int m=0;m<actionimages.size();m++)
                    {
                        final ImageView imageView=new ImageView(getActivity().getApplicationContext());
                        imageView.setLayoutParams(new LinearLayout.LayoutParams(300,300));
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageView.setPadding(5,2,5,2);
                        final int finalM2 = m;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent in=new Intent(getActivity(),CustomerMovieDetail.class);
                                in.putExtra("movieid",actionmovieid.get(finalM2));
                                startActivity(in);

                            }
                        });
                        final int finalM = m;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dynamiclinearlayoutaction.addView(imageView);
                                Picasso.with(getActivity().getApplicationContext()).load(Global.global+actionimages.get(finalM)).resize(500,500).centerInside().into(imageView);
                            }
                        });
//                        flag3=true;
//                        progress();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    class romance implements Runnable
    {

        @Override
        public void run() {
            romanticmovieid.clear();
            romanticmovieimages.clear();
            try {
                URL url=new URL(Global.global+"CustomerPictures?opr=Romance"+"&Username="+username);
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
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
                    JSONArray jsonArray=jsonObject.getJSONArray("ans");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject js=jsonArray.getJSONObject(i);
                        romanticmovieid.add(js.getInt("Movieid"));
                        romanticmovieimages.add(js.getString("Poster"));
                    }
                    for(int m=0;m<romanticmovieimages.size();m++)
                    {
                        final ImageView imageView=new ImageView(getActivity().getApplicationContext());
                        imageView.setLayoutParams(new LinearLayout.LayoutParams(300,300));
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageView.setPadding(5,2,5,2);
                        final int finalM2 = m;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent in=new Intent(getActivity(),CustomerMovieDetail.class);
                                in.putExtra("movieid",romanticmovieid.get(finalM2));
                                startActivity(in);

                            }
                        });
                        final int finalM = m;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dynamiclinearlayoutromance.addView(imageView);
                                Picasso.with(getActivity().getApplicationContext()).load(Global.global+romanticmovieimages.get(finalM)).resize(500,500).centerInside().into(imageView);
                            }
                        });
//                        flag4=true;
//                        progress();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    class horror implements Runnable
    {

        @Override
        public void run() {
            horrorimages.clear();
            horrormovieid.clear();
            try {
                URL url=new URL(Global.global+"CustomerPictures?opr=Horror"+"&Username="+username);
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();


                int rescode=connection.getResponseCode();
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
                    JSONObject jsonObject=new JSONObject(sb.toString());
                    JSONArray jsonArray=jsonObject.getJSONArray("ans");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject js=jsonArray.getJSONObject(i);
                        horrormovieid.add(js.getInt("Movieid"));
                        horrorimages.add(js.getString("Poster"));
                    }
                    for(int m=0;m<horrorimages.size();m++)
                    {
                        final ImageView imageView=new ImageView(getActivity().getApplicationContext());
                        imageView.setLayoutParams(new LinearLayout.LayoutParams(300,300));
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageView.setPadding(5,2,5,2);
                        final int finalM = m;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dynamiclinearlayouthorror.addView(imageView);
                                Picasso.with(getActivity().getApplicationContext()).load(Global.global+horrorimages.get(finalM)).resize(500,500).centerInside().into(imageView);
                            }
                        });
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
//    public void progress()
//    {
//        if(flag1==true && flag2==true && flag3==true && flag4==true)
//        {
//            progressDialog.dismiss();
//        }
//    }
}
