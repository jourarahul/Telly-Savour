package com.example.rahul.telly;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OtpMatch extends AppCompatActivity {

    EditText et1;
    TextView tv1;
    Intent in;
    String Username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_match);
        et1=(EditText)findViewById(R.id.et1);
        tv1=(TextView)findViewById(R.id.tv1);
        in=getIntent();
        Username=in.getStringExtra("Username");
        tv1.setText("Otp Confirmation for"+Username);
    }
    public void OtpConfirm(View view)
    {
        final String otp=et1.getText().toString().trim();
        if(otp.equals(""))
        {
            Toast.makeText(this, "Please Enter Otp", Toast.LENGTH_SHORT).show();
        }
        else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try
                    {
                        URL url=new URL(Global.global+"UserOtpConfirmServlet?Username="+Username+"&Otp="+otp);
                        HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                       int rescode= connection.getResponseCode();
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
                                final String temp=sb.toString();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(temp.equals("success"))
                                    {
                                        Toast.makeText(OtpMatch.this, "Otp Match:Go to Login", Toast.LENGTH_SHORT).show();
                                        Intent in=new Intent(getApplicationContext(),Home.class);
                                        in.putExtra("from","OTP");
                                        startActivity(in);
                                    }
                                    else
                                    {
                                        Toast.makeText(OtpMatch.this, "Otp doesnt match", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });



                        }

                        else
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(OtpMatch.this, "NetWork Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
    }
}
