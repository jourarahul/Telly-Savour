package com.example.rahul.telly;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.SharedPreferencesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.MODE_WORLD_READABLE;


public class Frag1 extends Fragment
{
    TextView tv101;
    EditText et1;
    EditText et2;
    Button bt1,bt2,bt3;
    public Frag1()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment1, container, false);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        et1=(EditText)(getActivity().findViewById(R.id.et1));
        et2=(EditText)(getActivity().findViewById(R.id.et2));
        bt1=(Button)(getActivity().findViewById(R.id.bt1));
        bt2=(Button)(getActivity().findViewById(R.id.bt2));
        bt3=(Button)(getActivity().findViewById(R.id.bt3));
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent in=new Intent(getActivity(),ForgotPassword.class);
                    startActivity(in);

            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String username=et1.getText().toString();
                        String password=et2.getText().toString();
                            if(username.equals(""))
                            {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "Please enter Username", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else if(password.equals(""))
                            {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "Please enter Password", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else {
                                try {
                                    URL url = new URL(Global.global + "CustomerLogin?Username=" + username + "&Password=" + password);
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
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                String s = "success";
                                                String f = "failed";
                                                String status="pending";
                                                Log.d("TEST", "msg " + temp);
                                                if (temp.equals(s)) {
                                                    Toast.makeText(getActivity(), "LoginSuccess", Toast.LENGTH_SHORT).show();
                                                    SharedPreferences sharedPreferences=getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
                                                    SharedPreferences.Editor editor=sharedPreferences.edit();
                                                    editor.putString("username",username);
                                                    editor.commit();
                                                    Intent in=new Intent(getActivity(),HomeActvity.class);
                                                    startActivity(in);
                                                }
                                                else if(temp.equals(status))
                                                {
                                                    Intent in=new Intent(getActivity(),OtpMatch.class);
                                                    in.putExtra("Username",username);
                                                    startActivity(in);

                                                }

                                                else {
                                                    Toast.makeText(getActivity(), "User doesn't exist", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    } else {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                    }
                }).start();
            }
        });
            bt3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm=getActivity().getSupportFragmentManager();
                    FragmentTransaction ftr=fm.beginTransaction();
                    ftr.replace(R.id.list1,new Frag2());
                    ftr.commit();

                }
            });


    }
}
