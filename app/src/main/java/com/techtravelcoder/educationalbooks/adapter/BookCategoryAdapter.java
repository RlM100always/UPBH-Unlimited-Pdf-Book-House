package com.techtravelcoder.educationalbooks.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.educationalbooks.R;
import com.techtravelcoder.educationalbooks.activity.BookPostActivity;
import com.techtravelcoder.educationalbooks.ads.ADSSetUp;
import com.techtravelcoder.educationalbooks.model.AdsModel;
import com.techtravelcoder.educationalbooks.model.BookCategoryModel;
import com.techtravelcoder.educationalbooks.model.BookModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookCategoryAdapter extends RecyclerView.Adapter<BookCategoryAdapter.BookCategoryViewHolder> {

    Context context;
    int val=9;
    String adsStat="off";

    ArrayList<BookCategoryModel>bookCategoryModelList;
    ArrayList<BookCategoryModel>bookNumber;

    private static final int TYPE_CATEGORY = 0;
    private static final int TYPE_ALTERNATE = 1;
    ArrayList<AdsModel> alternateDataList;


    public BookCategoryAdapter(Context context, ArrayList<BookCategoryModel> bookCategoryModelList) {
        this.context = context;
        this.bookCategoryModelList = bookCategoryModelList;
        bookNumber=new ArrayList<>();
        alternateDataList = new ArrayList<>();

        fetchAlternateData();


    }

    @NonNull
    @Override
    public BookCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == TYPE_ALTERNATE) {
            view = LayoutInflater.from(context).inflate(R.layout.ads_design, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.lay_category_design, parent, false);
        }
        return new BookCategoryViewHolder(view, viewType);


    }

    public void searchLists(ArrayList<BookCategoryModel> filterlist) {
        bookCategoryModelList=filterlist;
    }

    private int itemGapNo(){

        FirebaseDatabase.getInstance().getReference("Ads Control").child("cn")
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
    public void onBindViewHolder(@NonNull BookCategoryViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_ALTERNATE && !alternateDataList.isEmpty()) {

                int adIndex = (position / itemGapNo()) % alternateDataList.size();
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

        }
        else {
            BookCategoryModel bookCategoryModel=bookCategoryModelList.get(position);
            holder.cName.setText(bookCategoryModel.getbCategoryName());
            Glide.with(context).load(bookCategoryModel.getbCategoryImageLink()).into(holder.cImage);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ADSSetUp.adsType1(context);

                    Intent intent=new Intent(context, BookPostActivity.class);

                    intent.putExtra("key",bookCategoryModel.getbCategoryKey());
                    intent.putExtra("category",bookCategoryModel.getbCategoryName());
                    intent.putExtra("subcategory","-/(.<10}");

                    context.startActivity(intent);
                }
            });
        }




    }

    private void fetchAlternateData() {
        FirebaseDatabase.getInstance().getReference().child("Ads Control").child("Category")
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
                            Collections.shuffle(alternateDataList);
                            notifyDataSetChanged(); // Notify adapter to refresh after loading ads
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error if needed
                    }
                });
    }

    @Override
    public int getItemCount() {
        return bookCategoryModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (checkAdsStatus().equals("on") && !alternateDataList.isEmpty()) {
            return (position + 1) % itemGapNo() == 0 ? TYPE_ALTERNATE : TYPE_CATEGORY;
        }
        return TYPE_CATEGORY;


    }


    public class BookCategoryViewHolder extends RecyclerView.ViewHolder {
        //decalare global attribute

        private ImageView cImage,ads;
        private TextView cName;
        private LinearLayout ll;
        public BookCategoryViewHolder(@NonNull View itemView,int viewType) {
            super(itemView);

            if(viewType==TYPE_CATEGORY){
                cImage=itemView.findViewById(R.id.lay_category_image_id);
                cName=itemView.findViewById(R.id.lay_category_name_id);
            }else {
                ads=itemView.findViewById(R.id.ads_image);
                ll=itemView.findViewById(R.id.ads_ll_id);
            }


        }
    }
}
