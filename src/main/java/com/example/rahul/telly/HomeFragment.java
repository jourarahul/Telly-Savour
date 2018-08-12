package com.example.rahul.telly;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    CarouselView carouselView;
    ArrayList<String> carimages;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        carouselView=(CarouselView)(getActivity().findViewById(R.id.carouselhomefrag));
        carimages=new ArrayList<>();
        Log.d("HF", "start " + "start");
        mycarousel obj=new mycarousel();
        Thread th=new Thread(obj);
        th.start();
        Log.d("HF", "after thread" + "after thread");

    }

    class mycarousel implements Runnable
    {

        @Override
        public void run() {
            try {
                carimages.clear();
                Log.d("HF", "entered " + "entered");
                URL url=new URL(Global.global+"CustomerRandomPictures");
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
}
