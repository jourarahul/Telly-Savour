package com.example.rahul.telly;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ForgotPassword extends AppCompatActivity {

    EditText et1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        et1= (EditText) findViewById(R.id.et1);

    }
    public void go(View view)
    {
        final String username=et1.getText().toString();
        if(username.equals(""))
        {
            AlertDialog.Builder abc=new AlertDialog.Builder(this);
            abc.setTitle("Please enter Username");
            AlertDialog def=abc.create();
            def.show();
        }
        else
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(Global.global +"ForgotPassword?Username="+username);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        int rescode = connection.getResponseCode();
                        if (rescode == 200) {
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
                            final String temp = sb.toString();
                            String s="success";
                            String f="failed";
                            if(temp.equals(s))
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder abc=new AlertDialog.Builder(ForgotPassword.this);
                                        abc.setTitle("Password Sent to your Phone");
                                        abc.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        });
                                        AlertDialog def=abc.create();
                                        def.show();
                                    }
                                });
                            }
                            else
                            {
                               runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       AlertDialog.Builder abc=new AlertDialog.Builder(ForgotPassword.this);
                                       abc.setTitle("Username doesnot exist");
                                       AlertDialog def=abc.create();
                                       def.show();
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
    }
}
