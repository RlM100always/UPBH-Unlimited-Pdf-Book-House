package com.techtravelcoder.educationalbooks.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techtravelcoder.educationalbooks.R;

public class GuideLineDetailsActivity extends AppCompatActivity {

    private LinearLayout ll1,ll2,ll3,ll4,ll5;
    private TextView see;
    private int numCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_line_details);

        ll1=findViewById(R.id.llg1);
        ll2=findViewById(R.id.llg2);
        ll3=findViewById(R.id.llg3);
        ll4=findViewById(R.id.llg4);

        ll5=findViewById(R.id.llg5);
        see=findViewById(R.id.see_here_id);

        numCheck=getIntent().getIntExtra("num",0);

        if(numCheck==1){
            ll1.setVisibility(View.VISIBLE);

        } else if (numCheck==2) {
            ll2.setVisibility(View.VISIBLE);

        } else if (numCheck==3) {
            ll3.setVisibility(View.VISIBLE);

        } else if (numCheck==4) {
            ll4.setVisibility(View.VISIBLE);

        } else if (numCheck==5) {
            ll5.setVisibility(View.VISIBLE);

        }else {

        }

        see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll4.setVisibility(View.GONE);
                ll5.setVisibility(View.VISIBLE);


            }
        });


    }
}