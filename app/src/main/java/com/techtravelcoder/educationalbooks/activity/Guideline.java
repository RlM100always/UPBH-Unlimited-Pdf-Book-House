package com.techtravelcoder.educationalbooks.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techtravelcoder.educationalbooks.R;
import com.techtravelcoder.educationalbooks.ads.ADSSetUp;

public class Guideline extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView textView;
    private LinearLayout ll1,ll2,ll3,ll4,ll5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guideline);

        toolbar=findViewById(R.id.custom_toolbar);
        textView=findViewById(R.id.toolbar_title);
        ll1=findViewById(R.id.id1);
        ll2=findViewById(R.id.id2);
        ll3=findViewById(R.id.id3);
        ll4=findViewById(R.id.id4);
        ll5=findViewById(R.id.id5);

        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ADSSetUp.adsType1(Guideline.this);

                Intent intent=new Intent(Guideline.this, GuideLineDetailsActivity.class);
                intent.putExtra("num",1);
                startActivity(intent);
            }
        });

        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ADSSetUp.adsType1(Guideline.this);

                Intent intent=new Intent(Guideline.this, GuideLineDetailsActivity.class);
                intent.putExtra("num",2);
                startActivity(intent);
            }
        });
        ll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ADSSetUp.adsType1(Guideline.this);

                Intent intent=new Intent(Guideline.this, GuideLineDetailsActivity.class);
                intent.putExtra("num",3);
                startActivity(intent);
            }
        });
        ll4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ADSSetUp.adsType1(Guideline.this);

                Intent intent=new Intent(Guideline.this, GuideLineDetailsActivity.class);
                intent.putExtra("num",4);
                startActivity(intent);
            }
        });


        ll5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ADSSetUp.adsType1(Guideline.this);

                Intent intent=new Intent(Guideline.this, GuideLineDetailsActivity.class);
                intent.putExtra("num",5);
                startActivity(intent);
            }
        });



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        textView.setText("Settings And Guidelines");


    }
}