package com.example.rahul.telly;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Frag2 extends Fragment {
    EditText name,username,password,confirmpassword,email,phoneno;
    Button bt1;

    public Frag2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment2, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        name=(EditText)getActivity().findViewById(R.id.name);
        username=(EditText)getActivity().findViewById(R.id.username);
        password=(EditText)getActivity().findViewById(R.id.password);
        confirmpassword=(EditText)getActivity().findViewById(R.id.confirmpassword);
        email=(EditText)getActivity().findViewById(R.id.email);
        phoneno=(EditText)getActivity().findViewById(R.id.phoneno);
        bt1 = (Button)getActivity().findViewById(R.id.bt1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  final String Name=name.getText().toString().trim();
                final String Username=username.getText().toString().trim();
                final String Password=password.getText().toString().trim();
                String Confirmpassword=confirmpassword.getText().toString().trim();
                final String Email=email.getText().toString().trim();
                final String Phoneno=phoneno.getText().toString().trim();
                if(Name.equals(""))
                {
                    Toast.makeText(getActivity(), "Please Enter Name", Toast.LENGTH_SHORT).show();
                }
                else if(Username.equals(""))
                {
                    Toast.makeText(getActivity(), "Please Enter Username", Toast.LENGTH_SHORT).show();
                }
                else if(Email.equals(""))
                {
                    Toast.makeText(getActivity(), "Please Enter Email", Toast.LENGTH_SHORT).show();
                }
                else if(Password.equals(""))
                {
                    Toast.makeText(getActivity(), "Please Enter Password", Toast.LENGTH_SHORT).show();
                }
                else if(Confirmpassword.equals(""))
                {
                    Toast.makeText(getActivity(), "Please Enter Confirm Password", Toast.LENGTH_SHORT).show();
                }

                else if(Phoneno.equals(""))
                {
                    Toast.makeText(getActivity(), "Please Enter Phone No For Confirmation", Toast.LENGTH_SHORT).show();
                }
                else if(!Password.equals(Confirmpassword))
                {
                    Toast.makeText(getActivity(), "Password and Confirm Password Not Same", Toast.LENGTH_SHORT).show();
                }
                else if(Phoneno.length()!=10)
                {
                    Toast.makeText(getActivity(), "Phoneno. Mustbe of 10 digits", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                URL url = new URL(Global.global + "CustomerSignUp?name=" + Name + "&username=" + Username + "&email=" + Email + "&password=" + Password + "&phoneno=" + Phoneno);
                                Log.d("Test","onStURL url = new URLart"+Name+Username+Email+Password+Phoneno);
                                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                                int rescode=connection.getResponseCode();
                                Log.d("Test",rescode+"");
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
                                 final   String temp=sb.toString();
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d("TES",temp);
                                            String s="success";
                                            String f="failed";
                                            if(!temp.equals(f))
                                            {
//                                                Toast.makeText(getActivity().getApplicationContext(),temp,Toast.LENGTH_SHORT).show();
                                                Intent in=new Intent(getActivity(),OtpMatch.class);
                                                in.putExtra("Username",Username);
                                                startActivity(in);
                                            }
                                            else
                                            {
                                                Toast.makeText(getActivity().getApplicationContext(),"User Already Exist",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                                else
                                {
                                    Log.d("Test","NO Response");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }


            }
        });
    }

}
