package com.techtravelcoder.admin.ads;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.techtravelcoder.admin.R;

import java.util.HashMap;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private Context context;
    private List<Item> itemList;
    private int num ;

    public ItemAdapter(Context context, List<Item> itemList,int num) {
        this.context = context;
        this.itemList = itemList;
        this.num=num;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.titleTextView.setText(item.getTitle());

        Glide.with(context)
                .load(item.getImage())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                final View view=LayoutInflater.from(context).inflate(R.layout.new_ad_design,null);


                EditText edTitle=view.findViewById(R.id.enter_newads_title);
                EditText sitelink=view.findViewById(R.id.enter_newads_key_link);
                EditText edImage=view.findViewById(R.id.enter_newads_image_link);

                edTitle.setText(item.getTitle());
                sitelink.setText(item.getSiteUrl());
                edImage.setText(item.getImage());

                TextView postAds=view.findViewById(R.id.newads_submit_id);

                //ads_cancel
                TextView cancel=view.findViewById(R.id.newads_cancel);



                builder.setView(view);
                AlertDialog alertDialog= builder.create();
                Drawable drawable= ContextCompat.getDrawable(context,R.drawable.alert_back);
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

                                HashMap<String,Object> hashMap=new HashMap();
                                hashMap.put("title",edTitle.getText().toString());
                                hashMap.put("image",edImage.getText().toString());
                                hashMap.put("siteUrl",sitelink.getText().toString());
                                hashMap.put("id",item.getId());


                                FirebaseDatabase.getInstance().getReference("Ads Control").child("Category")
                                        .child(item.getId())
                                        .setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                alertDialog.dismiss();
                            }
                            if(num==2){


                                HashMap<String,Object> hashMap=new HashMap();
                                hashMap.put("title",edTitle.getText().toString());
                                hashMap.put("image",edImage.getText().toString());
                                hashMap.put("siteUrl",sitelink.getText().toString());
                                hashMap.put("id",item.getId());


                                FirebaseDatabase.getInstance().getReference("Ads Control").child("Author")
                                        .child(item.getId())
                                        .setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                alertDialog.dismiss();
                            }










                        }
                        else {
                            Toast.makeText(context, "Fillup all Info", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}

