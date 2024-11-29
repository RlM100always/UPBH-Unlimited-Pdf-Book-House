package com.techtravelcoder.admin.ads;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.admin.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdsNotifiActivity extends AppCompatActivity {

    private EditText etTitle;
    private TextView tvSubmit;
    private TextView tvAddAds,updateAds;
    private ImageSlider imageSlider;
    private RadioGroup headline,premium,faceAdmob;
    private RadioButton headlineOn,headliineOff,premimumOn,premiumOff,faceAdmobOn,faceAdmobOff;
    private String headChecked,premiumChecked,faceAdmobChecked;
    private TextView s1,s2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ads_notifi);


        headChecked="";
        premiumChecked="";
        faceAdmobChecked="";

        updateAds=findViewById(R.id.update_ads_status);

        etTitle=findViewById(R.id.notification_headline_id);
        tvSubmit=findViewById(R.id.notification_submit_id);
        tvAddAds=findViewById(R.id.ads_control_id);
        headline=findViewById(R.id.group_radio_headline);
        premium=findViewById(R.id.radio_group_premium_ads);
        faceAdmob=findViewById(R.id.radio_group_face_add_id);
        headlineOn=findViewById(R.id.headline_on_id);
        headliineOff=findViewById(R.id.headline_off_id);
        premimumOn=findViewById(R.id.premium_on_id);
        premiumOff=findViewById(R.id.premium_off_id);
        faceAdmobOn=findViewById(R.id.face_add_on_id);
        faceAdmobOff=findViewById(R.id.face_add_off_id);
        s1=findViewById(R.id.Small_Ads1);
        s2=findViewById(R.id.small_ads2);


        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(AdsNotifiActivity.this, AdsManageActivity.class);
                intent.putExtra("id","1");
                startActivity(intent);
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(AdsNotifiActivity.this, AdsManageActivity.class);
                intent.putExtra("id","2");
                startActivity(intent);
            }
        });





        FirebaseDatabase.getInstance().getReference("Ads Control").child("hStatus").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String ans=snapshot.getValue(String.class);
                    if(ans.equals("on")){
                        headlineOn.setChecked(true);
                    }else {
                        headliineOff.setChecked(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference("Ads Control").child("pStatus").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String ans=snapshot.getValue(String.class);
                    if(ans.equals("on")){
                        premimumOn.setChecked(true);
                    }else {
                        premiumOff.setChecked(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference("Ads Control").child("faStatus").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String ans=snapshot.getValue(String.class);
                    if(ans.equals("on")){
                        faceAdmobOn.setChecked(true);
                    }else {
                        faceAdmobOff.setChecked(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        headline.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = findViewById(checkedId);
                headChecked = checkedRadioButton.getText().toString();


            }
        });

        premium.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = findViewById(checkedId);
                premiumChecked = checkedRadioButton.getText().toString();


            }
        });


        faceAdmob.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = findViewById(checkedId);
                faceAdmobChecked = checkedRadioButton.getText().toString();


            }
        });


        updateAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!TextUtils.isEmpty(headChecked)){
                    FirebaseDatabase.getInstance().getReference("Ads Control").child("hStatus").setValue(headChecked);
                }
                if(!TextUtils.isEmpty(premiumChecked)){
                    FirebaseDatabase.getInstance().getReference("Ads Control").child("pStatus").setValue(premiumChecked);
                }
                if(!TextUtils.isEmpty(faceAdmobChecked)){
                    FirebaseDatabase.getInstance().getReference("Ads Control").child("faStatus").setValue(faceAdmobChecked);
                }
                Toast.makeText(AdsNotifiActivity.this, "Success", Toast.LENGTH_SHORT).show();


            }
        });










        sliderSupport();


        //retrive
        FirebaseDatabase.getInstance().getReference("Ads Control").child("marque")
                .child("text").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String val= (String) snapshot.getValue();
                            etTitle.setText(val);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(etTitle.getText().toString())){
                    FirebaseDatabase.getInstance().getReference("Ads Control").child("marque")
                            .child("text")
                            .setValue(etTitle.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(AdsNotifiActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                }
                            });
                }else {
                    Toast.makeText(AdsNotifiActivity.this, "Fillup Text", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvAddAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postCustomAds();
            }
        });


    }

    private void postCustomAds() {
        AlertDialog.Builder builder=new AlertDialog.Builder(AdsNotifiActivity.this);
        final View view=getLayoutInflater().inflate(R.layout.ads_input,null);


        EditText edTitle=view.findViewById(R.id.enter_ads_title);
        EditText edKey=view.findViewById(R.id.enter_ads_key_link);

        EditText edImage=view.findViewById(R.id.enter_ads_image_link);
        EditText edKeyNumber=view.findViewById(R.id.enter_key_number);

        TextView postAds=view.findViewById(R.id.ads_submit_id);

        //ads_cancel
        TextView cancel=view.findViewById(R.id.ads_cancel);



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
                if(!TextUtils.isEmpty(edTitle.getText().toString()) && !TextUtils.isEmpty(edImage.getText().toString()) && !TextUtils.isEmpty(edKey.getText().toString())
                && !TextUtils.isEmpty(edKeyNumber.getText().toString())){

                    String entryKey = FirebaseDatabase.getInstance().getReference("Ads Control").child("Premium ads").push().getKey();

                    HashMap<String,Object> hashMap=new HashMap();
                    hashMap.put("title",edTitle.getText().toString());
                    hashMap.put("image",edImage.getText().toString());
                    hashMap.put("siteUrl",edKey.getText().toString());
                    hashMap.put("id",entryKey);
                    hashMap.put("key",edKeyNumber.getText().toString());

                    FirebaseDatabase.getInstance().getReference("Ads Control").child("Premium ads")
                            .child(entryKey)
                            .setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(AdsNotifiActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                }
                            });
                    alertDialog.dismiss();

                }
                else {
                    Toast.makeText(AdsNotifiActivity.this, "Fillup all Info", Toast.LENGTH_SHORT).show();

                }
            }
        });



    }

    public void sliderSupport() {
        imageSlider = findViewById(R.id.image_slider);
        final List<SlideModel> remoteimages = new ArrayList<>(); // SlideModel is an inbuilt model class made by the GitHub library provider
        List<String> remoteimages1 = new ArrayList<>();
        List<String> remoteimages2 = new ArrayList<>();
        List<String> remoteimages3 = new ArrayList<>();



      FirebaseDatabase.getInstance().getReference().child("Ads Control").child("Premium ads")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot data : snapshot.getChildren()) {
                                remoteimages1.add(data.child("siteUrl").getValue(String.class));
                                remoteimages2.add(data.child("id").getValue(String.class));
                                remoteimages3.add(data.child("key").getValue(String.class));


                                remoteimages.add(new SlideModel(
                                        data.child("image").getValue().toString(),
                                        data.child("title").getValue().toString(),
                                        ScaleTypes.FIT));

                            }
                            // Set the image list and click listener outside the loop
                            imageSlider.setImageList(remoteimages, ScaleTypes.FIT);
                            imageSlider.setItemClickListener(new ItemClickListener() {
                                @Override
                                public void onItemSelected(int i) {
                                    AlertDialog.Builder builder=new AlertDialog.Builder(AdsNotifiActivity.this);
                                    final View view=getLayoutInflater().inflate(R.layout.ads_input,null);


                                    EditText edTitle=view.findViewById(R.id.enter_ads_title);
                                    EditText edKey=view.findViewById(R.id.enter_ads_key_link);

                                    EditText edImage=view.findViewById(R.id.enter_ads_image_link);
                                    EditText edKeyNumber=view.findViewById(R.id.enter_key_number);
                                    edKeyNumber.setVisibility(View.GONE);
                                    TextView postAds=view.findViewById(R.id.ads_submit_id);
                                    TextView cancel=view.findViewById(R.id.ads_cancel);


                                    edTitle.setText(remoteimages.get(i).getTitle());
                                    edImage.setText(remoteimages.get(i).getImageUrl());
                                    edKey.setText(remoteimages1.get(i));

                                    //Toast.makeText(AdsNotifiActivity.this, ""+remoteimages.get(i).getTitle()+"    "+remoteimages.get(i).getImageUrl()+"  "+remoteimages1.get(i), Toast.LENGTH_SHORT).show();



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
                                            if(!TextUtils.isEmpty(edTitle.getText().toString()) && !TextUtils.isEmpty(edImage.getText().toString()) && !TextUtils.isEmpty(edKey.getText().toString())){


                                                HashMap<String,Object> hashMap=new HashMap();
                                                hashMap.put("title",edTitle.getText().toString());
                                                hashMap.put("image",edImage.getText().toString());
                                                hashMap.put("siteUrl",edKey.getText().toString());
                                                hashMap.put("id",remoteimages2.get(i));
                                                hashMap.put("key",remoteimages3.get(i));


                                                FirebaseDatabase.getInstance().getReference("Ads Control").child("Premium ads")
                                                        .child(remoteimages2.get(i))
                                                        .setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Toast.makeText(AdsNotifiActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                alertDialog.dismiss();

                                            }
                                            else {
                                                Toast.makeText(AdsNotifiActivity.this, "Fillup all Info", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });


                                }

                                @Override
                                public void doubleClick(int i) {
                                    // Handle double click if needed
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle the error if needed
                    }
                });
    }

}


















