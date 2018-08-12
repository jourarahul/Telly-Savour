package com.example.rahul.telly;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TvShowEpisodeActivity extends AppCompatActivity {
    ProgressBar pbar;

    MyFileDownloadReceiver broadcastreceiver;
    String season="";
    int tvshowid;
    String episodeno="";
    ImageView largebackground,smallimage;
    ArrayList<singleEpisodedata> al;
    View alphaColorView;
    TextView detail;
    Button bt1,bt2,bt4;
    String videopath="";
    boolean flag=false;
    IntentFilter inf;
    Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show_episode);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        bt1=(Button)findViewById(R.id.bt1);
        bt2=(Button)findViewById(R.id.bt2);
        bt4=(Button)findViewById(R.id.bt4);
        pbar = (ProgressBar) findViewById(R.id.pbar);

        broadcastreceiver = new MyFileDownloadReceiver();
        inf = new IntentFilter();
        inf.addAction("my.download.broadcast");
        Intent in=getIntent();
        tvshowid=in.getIntExtra("tvshowid",0);
        season=in.getStringExtra("season");
        episodeno=in.getStringExtra("episodeno");
        largebackground=(ImageView)findViewById(R.id.largebackground);
        detail=(TextView)findViewById(R.id.detail);
        al=new ArrayList<>();
        smallimage=(ImageView)findViewById(R.id.smallimage);
        alphaColorView=(View)findViewById(R.id.alphaColorView);

        alphaColorView.setBackgroundColor(getColorWithAlpha(Color.WHITE, 0.5f));
        thread=new Thread(new mymovies());
        thread.start();
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent=new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, Global.global+videopath);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getApplicationContext(), DownloadService.class);
                in.putExtra("path",videopath);
                startService(in);
            }
        });

        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(TvShowEpisodeActivity.this,PlayVideoActivity.class);
                in.putExtra("videopath",videopath);
                startActivity(in);
            }
        });
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(TvShowEpisodeActivity.this,tvshowid+season+episodeno, Toast.LENGTH_SHORT).show();
//            }
//        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MYBR","per in customer view"+"receiver registered");

        registerReceiver(broadcastreceiver, inf);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MYBR","per in customer view"+"receiver unregistered");
        unregisterReceiver(broadcastreceiver);

    }

    public static int getColorWithAlpha(int color, float ratio) {
        int newColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }
    class mymovies implements Runnable
    {

        @Override
        public void run() {
            try {
                URL url=new URL(Global.global+"CustomerEpisodeDetail?tvshowid="+tvshowid+"&episodeno="+episodeno+"&season="+season);
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                int rescode=connection.getResponseCode();
                Log.d("hello", String.valueOf(+rescode));
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
                        //Log.d("hello","msg " +message);
                    }
                    Log.d("hello","sb.tostring "+sb.toString());

                    JSONObject jsonObject=new JSONObject(sb.toString());
                    JSONArray jsa=jsonObject.getJSONArray("ans");
                    for(int i=0;i<jsa.length();i++)
                    {
                        JSONObject js=jsa.getJSONObject(i);
                        String vp=js.getString("Videopath");
                        for(int j=0;j<vp.length();j++)
                        {
                            char ch=vp.charAt(j);
                            if(ch==' ')
                            {
                                videopath+="%20";
                            }
                            else
                            {
                                videopath+=ch;
                            }
                        }

                        al.add(new singleEpisodedata(js.getString("Title"),js.getString("Season"),js.getString("Duration"),js.getString("Description"),js.getString("Poster")));
                    }
                    for(int j=0;j<al.size();j++)
                    {
                        final int finalJ = j;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Picasso.with(TvShowEpisodeActivity.this).load(Global.global+al.get(finalJ).poster).into(largebackground);
                                Picasso.with(TvShowEpisodeActivity.this).load(Global.global+al.get(finalJ).poster).into(smallimage);
                                Log.d("hello",Global.global+al.get(finalJ).poster);
                                detail.append("Title: "+al.get(finalJ).title+"\n");
                                detail.append("Season: "+ al.get(finalJ).season+"\n");
                                detail.append("Duration: "+ al.get(finalJ).duration+"\n");
                                detail.append("Desciption: "+ al.get(finalJ).description+"\n");
                            }
                        });


                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    class singleEpisodedata {

        String title,season,duration,description,poster;
        singleEpisodedata(String title,String season,String duration,String description,String poster)
        {
            this.title= title;
            this. season=season;
            this.duration=duration;
            this.description=description;
            this.poster=poster;
        }
    }
    public class MyFileDownloadReceiver extends BroadcastReceiver {
        public MyFileDownloadReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {


            if (intent.getAction().equals("my.download.broadcast")) {
                Log.d("MYBR","per in customer view"+"broadcast received");
                int per = intent.getIntExtra("value", 0);
                Log.d("MYBR","per in customer view"+per);
                if(flag==false){

                    flag=true;
                }

                popUpNotification(context, per);
                pbar.setProgress(per);


            }


        }


        public void popUpNotification(Context context, int per) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentTitle("Movie File Downloading.....");
            builder.setContentText("downloading in progress");
            builder.setContentInfo(per + "%");
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setProgress(100, per, false);
            //builder.setAutoCancel(true);
            Notification n = builder.build();

            NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            nm.notify(1, n);
        }


    }
}
