package com.techtravelcoder.educationalbooks.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.educationalbooks.R;
import com.techtravelcoder.educationalbooks.activity.AuthorPostActivity;
import com.techtravelcoder.educationalbooks.model.AdsModel;
import com.techtravelcoder.educationalbooks.model.AuthorModel;
import com.techtravelcoder.educationalbooks.model.BookCategoryModel;

import java.util.ArrayList;

public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.AuthorViewHolder> {

    Context context;
    ArrayList<AuthorModel> aulist;
    String adsStat="off";
    int val=9;

    private static final int TYPE_CATEGORY = 0;
    private static final int TYPE_ALTERNATE = 1;
    ArrayList<AdsModel> alternateDataList;

    public AuthorAdapter(Context context, ArrayList<AuthorModel> aulist) {
        this.context = context;
        this.aulist = aulist;
        alternateDataList = new ArrayList<>();
        fetchAlternateData();
    }

    private void fetchAlternateData() {
        FirebaseDatabase.getInstance().getReference().child("Ads Control").child("Author")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                AdsModel adsModel;
                                adsModel = dataSnapshot.getValue(AdsModel.class);
                                if (adsModel != null) {
                                    alternateDataList.add(adsModel);

                                }
                            }
                            notifyDataSetChanged(); // Notify adapter to refresh after loading ads
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error if needed
                    }
                });
    }


    @NonNull
    @Override
    public AuthorAdapter.AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == TYPE_ALTERNATE) {
            view = LayoutInflater.from(context).inflate(R.layout.ads_design, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.author_design, parent, false);
        }
        return new AuthorViewHolder(view, viewType);


    }


    private int itemGapNo(){

        FirebaseDatabase.getInstance().getReference("Ads Control").child("an")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            val=  snapshot.getValue(Integer.class);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        return val;
    }


    @Override
    public int getItemViewType(int position) {
        if (checkAdsStatus().equals("on") && !alternateDataList.isEmpty()) {
        return (position + 1) % itemGapNo() == 0 ? TYPE_ALTERNATE : TYPE_CATEGORY;
        }
        return TYPE_CATEGORY;
    }

    private String checkAdsStatus(){
        FirebaseDatabase.getInstance().getReference("Ads Control").child("pStatus")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            adsStat=  snapshot.getValue(String.class);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        return adsStat;

    }

    @Override
    public void onBindViewHolder(@NonNull AuthorAdapter.AuthorViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_ALTERNATE && !alternateDataList.isEmpty()) {

            // Calculate which ad to display from the alternateDataList
            int adIndex = (position / 9) % alternateDataList.size();
            AdsModel adsModel=alternateDataList.get(adIndex);
            Glide.with(context).load(adsModel.getImage()).into(holder.ads);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String siteUrl = adsModel.getSiteUrl();

                    if (siteUrl != null && !siteUrl.isEmpty()) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(siteUrl));

                        try {
                            context.startActivity(intent);
                        } catch (android.content.ActivityNotFoundException e) {
                            Toast.makeText(context, "No browser found to open this link", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "oh...Something Problem", Toast.LENGTH_SHORT).show();
                    }
                }
            });



        }else {
            AuthorModel authorModel=aulist.get(position);
            holder.name.setText(authorModel.getName());
            Glide.with(context).load(authorModel.getImage()).into(holder.imageView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, AuthorPostActivity.class);
                    intent.putExtra("name",authorModel.getName());
                    context.startActivity(intent);

                }
            });
        }

    }

    public void searchLists(ArrayList<AuthorModel> filterlist) {
        aulist=filterlist;
    }


    @Override
    public int getItemCount() {
        return aulist.size();
    }

    public class AuthorViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imageView,ads;
        TextView name;
        public AuthorViewHolder(@NonNull View itemView,int viewType) {
            super(itemView);

            if(viewType==TYPE_CATEGORY){
                imageView=itemView.findViewById(R.id.author_iamge);
                name=itemView.findViewById(R.id.author_name);
            }else {
                ads=itemView.findViewById(R.id.ads_image);
            }


        }
    }
}
