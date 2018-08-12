package com.example.rahul.telly;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class PaymentActivity extends AppCompatActivity {
WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        SharedPreferences sharedPreferences=getSharedPreferences("MyPref",MODE_PRIVATE);
        String Username=sharedPreferences.getString("username","");
        webView=(WebView)(findViewById(R.id.webview));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(Global.global+"payment.jsp?Username="+Username);
    }
}
