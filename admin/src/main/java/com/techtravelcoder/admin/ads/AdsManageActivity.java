package com.techtravelcoder.admin.ads;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.admin.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdsManageActivity extends AppCompatActivity {

    private String id;
    private FloatingActionButton f1,f2;

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private List<Item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_manage);

        f1=findViewById(R.id.ads_control_post_ads_id);
        f2=findViewById(R.id.ads_control_gap_no_id);
        recyclerView=findViewById(R.id.ads_control_recyclerview_id);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemList = new ArrayList<>();







        id=getIntent().getStringExtra("id");



        if(id.equals("1")){
            fetchItemsFromFirebase(1);
        }
        else if(id.equals("2")){
            fetchItemsFromFirebase(2);
        }



        f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id.equals("1")){
                    addAds(1);
                }
                else if(id.equals("2")){
                    addAds(2);
                }


            }
        });

        f2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id.equals("1")){
                    addAdsGapNo(1);
                }
                else if(id.equals("2")){
                    addAdsGapNo(2);
                }

            }
        });






    }

    private void fetchItemsFromFirebase(int i) {
        DatabaseReference databaseReference = null;
        if(i==1){
            adapter = new ItemAdapter(this, itemList,1);
            recyclerView.setAdapter(adapter);
            databaseReference = FirebaseDatabase.getInstance().getReference("Ads Control").child("Category");
        }else if(i==2){
            adapter = new ItemAdapter(this, itemList,2);
            recyclerView.setAdapter(adapter);
            databaseReference = FirebaseDatabase.getInstance().getReference("Ads Control").child("Author");
        }


        if(databaseReference!=null){
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    itemList.clear(); // Clear the list to avoid duplicates
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Item item = dataSnapshot.getValue(Item.class);
                        if (item != null) {
                            itemList.add(0,item);
                        }
                    }
                    adapter.notifyDataSetChanged(); // Notify the adapter to update the UI
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });


        }
    }

    private void addAdsGapNo(int num) {
        AlertDialog.Builder builder=new AlertDialog.Builder(AdsManageActivity.this);
        final View view=getLayoutInflater().inflate(R.layout.number_design,null);


        EditText edno=view.findViewById(R.id.gap_number_ed);



        TextView postAds=view.findViewById(R.id.gap_no_submit_id);

        //ads_cancel
        TextView cancel=view.findViewById(R.id.gap_cancel);

        if(num==1){
            FirebaseDatabase.getInstance().getReference("Ads Control").child("cn").
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                int val =snapshot.getValue(Integer.class);
                                edno.setText(String.valueOf(val));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
        if(num==2){
            FirebaseDatabase.getInstance().getReference("Ads Control").child("an").
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                int val =snapshot.getValue(Integer.class);

                                edno.setText(String.valueOf(val));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }







        builder.setView(view);
        AlertDialog alertDialog= builder.create();
        Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.alert_back);
        alertDialog.getWindow().setBackgroundDrawable(drawable);
        alertDialog.show();
        alertDialog.setCancelable(false);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        postAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(edno.getText().toString()) ){

                    if(num==1){

                        int num= Integer.parseInt(edno.getText().toString());

                        FirebaseDatabase.getInstance().getReference("Ads Control").child("cn")
                                .setValue(num).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(AdsManageActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        alertDialog.dismiss();
                    }
                    if(num==2){

                        int num= Integer.parseInt(edno.getText().toString());

                        FirebaseDatabase.getInstance().getReference("Ads Control").child("an")
                                .setValue(num).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(AdsManageActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        alertDialog.dismiss();

                    }









                }
                else {
                    Toast.makeText(AdsManageActivity.this, "Fillup all Info", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void addAds(int num) {
            AlertDialog.Builder builder=new AlertDialog.Builder(AdsManageActivity.this);
            final View view=getLayoutInflater().inflate(R.layout.new_ad_design,null);


            EditText edTitle=view.findViewById(R.id.enter_newads_title);
            EditText sitelink=view.findViewById(R.id.enter_newads_key_link);
            EditText edImage=view.findViewById(R.id.enter_newads_image_link);


            TextView postAds=view.findViewById(R.id.newads_submit_id);

            //ads_cancel
            TextView cancel=view.findViewById(R.id.newads_cancel);



            builder.setView(view);
            AlertDialog alertDialog= builder.create();
            Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.alert_back);
            alertDialog.getWindow().setBackgroundDrawable(drawable);
            alertDialog.show();
            alertDialog.setCancelable(false);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            postAds.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!TextUtils.isEmpty(edImage.getText().toString()) ){

                        if(num==1){

                            String entryKey = FirebaseDatabase.getInstance().getReference("Ads Control").child("Category").push().getKey();

                            HashMap<String,Object> hashMap=new HashMap();
                            hashMap.put("title",edTitle.getText().toString());
                            hashMap.put("image",edImage.getText().toString());
                            hashMap.put("siteUrl",sitelink.getText().toString());
                            hashMap.put("id",entryKey);


                            FirebaseDatabase.getInstance().getReference("Ads Control").child("Category")
                                    .child(entryKey)
                                    .setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(AdsManageActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            alertDialog.dismiss();
                        }
                        if(num==2){

                            String entryKey = FirebaseDatabase.getInstance().getReference("Ads Control").child("Author").push().getKey();

                            HashMap<String,Object> hashMap=new HashMap();
                            hashMap.put("title",edTitle.getText().toString());
                            hashMap.put("image",edImage.getText().toString());
                            hashMap.put("siteUrl",sitelink.getText().toString());
                            hashMap.put("id",entryKey);


                            FirebaseDatabase.getInstance().getReference("Ads Control").child("Author")
                                    .child(entryKey)
                                    .setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(AdsManageActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            alertDialog.dismiss();
                        }










                    }
                    else {
                        Toast.makeText(AdsManageActivity.this, "Fillup all Info", Toast.LENGTH_SHORT).show();

                    }
                }
            });



        }


}