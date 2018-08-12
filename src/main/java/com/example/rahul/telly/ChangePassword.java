package com.example.rahul.telly;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChangePassword extends AppCompatActivity {

    EditText op,np,cp;
    String old,newp,confirm;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        op= (EditText) findViewById(R.id.op);
        np=(EditText)findViewById(R.id.np);
        cp= (EditText) findViewById(R.id.cp);
        SharedPreferences sp=getSharedPreferences("MyPref",MODE_PRIVATE);
        username=sp.getString("username","");
    }
    public void go(View view) {
        old = op.getText().toString();
        newp = np.getText().toString();
        confirm = cp.getText().toString();
        if (old.equals("") || newp.equals("") || confirm.equals("")) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
        }
        else if(!newp.equals(confirm))
        {
            Toast.makeText(this, "New Password and Confirm Password Must Match", Toast.LENGTH_SHORT).show();
        }
        else {


            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(Global.global +"ChangeCustomerPassword?Username="+username+"&oldpassword="+old+"&newpassword="+newp);
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
                            String s="Password Changed";
                            String f="old Password does Not Match";
                            if(temp.equals(s))
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder abc=new AlertDialog.Builder(ChangePassword.this);
                                        abc.setTitle("Password Changed Successfully");
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
                                        AlertDialog.Builder abc=new AlertDialog.Builder(ChangePassword.this);
                                        abc.setTitle("Old password not Match");
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
