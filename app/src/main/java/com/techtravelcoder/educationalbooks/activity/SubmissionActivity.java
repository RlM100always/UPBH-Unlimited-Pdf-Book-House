package com.techtravelcoder.educationalbooks.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.techtravelcoder.educationalbooks.R;

public class SubmissionActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);

        webView=findViewById(R.id.webView_submission);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Enable JavaScript if needed

        // Load a URL
        webView.loadUrl("https://forms.gle/DURnU6DukPNgtjZ69");
        webView.setWebViewClient(new WebViewClient());

    }
}