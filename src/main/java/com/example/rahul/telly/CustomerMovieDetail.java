package com.example.rahul.telly;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class CustomerMovieDetail extends AppCompatActivity {
    ProgressBar pbar;
    RatingBar rb1,rb2;
    MyFileDownloadReceiver broadcastreceiver;
View alphaColorView;
    int movieid;
    ArrayList<singlemoviedata> al;
    Thread thread;
    ImageView largebackground,smallimage;
    TextView detail;
    Button bt1,bt2,bt3,bt4,bt5;
    String videopath="";
    boolean flag=false;
    String trailerpath="";
    IntentFilter inf;
    String username="";
    int rating=0;
    float ratess=0;
    float averagerating=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customer_movie_detail);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        bt1=(Button)findViewById(R.id.bt1);
        bt2=(Button)findViewById(R.id.bt2);
        bt3=(Button)findViewById(R.id.bt3);
        bt4=(Button)findViewById(R.id.bt4);
        bt5=(Button)findViewById(R.id.bt5);
        pbar = (ProgressBar) findViewById(R.id.pbar);
        rb1= (RatingBar) findViewById(R.id.rb1);
        rb2= (RatingBar) findViewById(R.id.rb2);
        broadcastreceiver = new MyFileDownloadReceiver();
         inf = new IntentFilter();
        inf.addAction("my.download.broadcast");

        largebackground=(ImageView)findViewById(R.id.largebackground);
        smallimage=(ImageView)findViewById(R.id.smallimage);
        alphaColorView=(View)findViewById(R.id.alphaColorView);
        detail=(TextView)findViewById(R.id.detail);
        alphaColorView.setBackgroundColor(getColorWithAlpha(Color.WHITE, 0.5f));
        al=new ArrayList<>();
        Intent in=getIntent();
        movieid=in.getIntExtra("movieid",0);
        Log.d("hello",movieid+"");
        thread=new Thread(new mymovies());
        thread.start();
        new Thread(new rate()).start();
        new Thread(new Averagerate()).start();
        SharedPreferences sp=this.getSharedPreferences("MyPref",MODE_PRIVATE);
        username=sp.getString("username","");
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
                in.putExtra("path", videopath);
                startService(in);
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(CustomerMovieDetail.this,PlayVideoActivity.class);
                in.putExtra("videopath",trailerpath);
                startActivity(in);
            }
        });
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(CustomerMovieDetail.this,PlayVideoActivity.class);
                in.putExtra("videopath",videopath);
                startActivity(in);
            }
        });
        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url=new URL(Global.global+"Favourites?movieid="+movieid+"&username="+username);
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
                                String s=sb.toString();
                                if(s.equals("success"))
                                {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(CustomerMovieDetail.this, "Add to Favourites", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else if(s.equals("failed"))
                                {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(CustomerMovieDetail.this, "Already Added", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        rb1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, final float rating, boolean fromUser) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url=new URL(Global.global+"RatingServlet?Username="+username+"&Movieid="+movieid+"&Rating="+rating);
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
                                String s=sb.toString();
                                if(s.equals("success"))
                                {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(CustomerMovieDetail.this, "Done", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
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
                URL url=new URL(Global.global+"CustomerMovieDetail?movieid="+movieid);
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
                        String vt=js.getString("Trailerpath");
                        for(int j=0;j<vt.length();j++)
                        {
                            char ch=vt.charAt(j);
                            if(ch==' ')
                            {
                                trailerpath+="%20";
                            }
                            else
                            {
                                trailerpath+=ch;
                            }
                        }
                        al.add(new singlemoviedata(js.getString("Title"),js.getString("Director"),js.getString("Producer"),js.getString("Cast"),js.getString("Releaseyear"),js.getString("Coverphoto"),js.getString("Poster")));
                    }
                    for(int j=0;j<al.size();j++)
                    {
                        final int finalJ = j;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Picasso.with(CustomerMovieDetail.this).load(Global.global+al.get(finalJ).coverphoto).into(largebackground);
                                Picasso.with(CustomerMovieDetail.this).load(Global.global+al.get(finalJ).poster).into(smallimage);
                                Log.d("hello",Global.global+al.get(finalJ).coverphoto);
                                detail.append("Title: "+al.get(finalJ).title+"\n");
                                detail.append("Release Year: "+ al.get(finalJ).releaseyear+"\n");
                                detail.append("Director: "+ al.get(finalJ).director+"\n");
                                detail.append("Producer: "+ al.get(finalJ).producer+"\n");
                                detail.append("Starring: "+ al.get(finalJ).cast+"\n");
                            }
                        });


                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    class rate implements Runnable
    {

        @Override
        public void run() {
            try
            {
                URL url=new URL(Global.global+"ShowRating?Username="+username+"&Movieid="+movieid);
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                int rescode=connection.getResponseCode();
                if(rescode==200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    String message = "";
                    StringBuffer sb = new StringBuffer();
                    while (true) {
                        message = br.readLine();
                        if (message == null) {
                            break;
                        }
                        sb.append(message);
                    }
                    JSONObject jsonObject=new JSONObject(sb.toString());
                    JSONArray jsa=jsonObject.getJSONArray("ans");
                    for(int i=0;i<jsa.length();i++)
                    {
                        JSONObject js=jsa.getJSONObject(i);
                        ratess= Float.parseFloat(js.getString("Rating"));

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CustomerMovieDetail.this, ratess+"", Toast.LENGTH_SHORT).show();
                            rb1.setRating(ratess);
                        }
                    });
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    class Averagerate implements Runnable
    {

        @Override
        public void run() {
            try
            {
                URL url=new URL(Global.global+"avgrating?movieid="+movieid);
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                int rescode=connection.getResponseCode();
                if(rescode==200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    String message = "";
                    StringBuffer sb = new StringBuffer();
                    while (true) {
                        message = br.readLine();
                        if (message == null) {
                            break;
                        }
                        sb.append(message);
                    }
                    averagerating= Float.parseFloat(sb.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CustomerMovieDetail.this, averagerating+"", Toast.LENGTH_SHORT).show();
                            rb2.setRating(averagerating);
                        }
                    });
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    class singlemoviedata {

        String title,director,producer,cast,releaseyear,coverphoto,poster;
        singlemoviedata(String title,String director,String producer,String cast,String releaseyear,String coverphoto,String poster)
        {
            this.title= title;
            this. director=director;
            this.producer=producer;
            this.cast=cast;
            this.releaseyear=releaseyear;
            this.coverphoto=coverphoto;
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

