package com.techtravelcoder.educationalbooks.fragmentactivity;

import static android.content.Context.UI_MODE_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.app.UiModeManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.educationalbooks.R;
import com.techtravelcoder.educationalbooks.adapter.BookAdapter;
import com.techtravelcoder.educationalbooks.model.BookModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewBooksFragment extends Fragment  {

    //instance variable
    private RecyclerView recyclerView;
    private String bCataKey,categoryName;


    private ArrayList<BookModel> bookList;
    private BookAdapter bookPostAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseReference mbase;
    private LinearLayout noNewBook;
    private ProgressBar progressBar;
    private ImageSlider imageSlider;
    private CardView cardView;
    private NestedScrollView nestedScrollView;
    private TextView today,yesterday,last7day;
    private int t1=0;
    private int y1=0;
    private int l1=1;



    public NewBooksFragment() {
        // Required empty public constructor
    }

    public static NewBooksFragment newInstance(String key, String category) {
        NewBooksFragment fragment = new NewBooksFragment();
        Bundle args = new Bundle();
        args.putString("key", key);
        args.putString("category", category);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_books, container, false);
        recyclerView=view.findViewById(R.id.new_books_recyclerview_id);
        noNewBook=view.findViewById(R.id.no_new_book);
        imageSlider=view.findViewById(R.id.image_slider);
        today=view.findViewById(R.id.today_id);
        last7day=view.findViewById(R.id.last_7_day_id);
        cardView=view.findViewById(R.id.pimage_card_id);
        nestedScrollView=view.findViewById(R.id.nested_scroll_id);
        progressBar=view.findViewById(R.id.progressBar_new_book);
        yesterday=view.findViewById(R.id.yesterday_id);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.threeitembackcolor), PorterDuff.Mode.SRC_IN);





        swipeRefreshLayout=view.findViewById(R.id.swipe_refresh_layout_new_books);
        bookList=new ArrayList<>();
        bookPostAdapter=new BookAdapter(getContext(),bookList,2);

        // Get the current configuration
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;






        if (getArguments() != null) {
            bCataKey = getArguments().getString("key");
            categoryName = getArguments().getString("category");
        }
        retriveNewbook();


        last7day.setBackgroundResource(R.drawable.button_active);
        last7day.setTextColor(getResources().getColor(R.color.colorSelected));
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                today.setBackgroundResource(R.drawable.button_active);
                today.setTextColor(getResources().getColor(R.color.colorSelected));
                t1=1;
                y1 =0;
                l1=0;

                yesterday.setBackgroundResource(R.drawable.edit_text_background);
                if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                    yesterday.setTextColor(getResources().getColor(R.color.white));
                } else if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
                    yesterday.setTextColor(getResources().getColor(R.color.black));

                }

                last7day.setBackgroundResource(R.drawable.edit_text_background);
                if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                    last7day.setTextColor(getResources().getColor(R.color.white));
                } else if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
                    last7day.setTextColor(getResources().getColor(R.color.black));

                }
                retriveTodayData(0);
            }
        });

        yesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                yesterday.setBackgroundResource(R.drawable.button_active);
                yesterday.setTextColor(getResources().getColor(R.color.colorSelected));
                t1=0;
                y1 =1;
                l1=0;

                today.setBackgroundResource(R.drawable.edit_text_background);
                if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                    today.setTextColor(getResources().getColor(R.color.white));
                } else if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
                    today.setTextColor(getResources().getColor(R.color.black));

                }

                last7day.setBackgroundResource(R.drawable.edit_text_background);
                if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                    last7day.setTextColor(getResources().getColor(R.color.white));
                } else if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
                    last7day.setTextColor(getResources().getColor(R.color.black));

                }
                retriveTodayData(1);
            }
        });

        last7day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.GONE);
                last7day.setBackgroundResource(R.drawable.button_active);
                last7day.setTextColor(getResources().getColor(R.color.colorSelected));

                t1=0;
                y1 =0;
                l1=1;

                yesterday.setBackgroundResource(R.drawable.edit_text_background);
                if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                    yesterday.setTextColor(getResources().getColor(R.color.white));
                } else if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
                    yesterday.setTextColor(getResources().getColor(R.color.black));

                }

                today.setBackgroundResource(R.drawable.edit_text_background);
                if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                    today.setTextColor(getResources().getColor(R.color.white));
                } else if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
                    today.setTextColor(getResources().getColor(R.color.black));

                }

                retriveNewbook();
            }
        });






        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                if(t1==1){
                    retriveTodayData(0);

                } else if (y1==1) {
                    retriveTodayData(1);

                }else {
                    retriveNewbook();

                }
            }
        });

        try {
            FirebaseDatabase.getInstance().getReference("Ads Control")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                String ans11=snapshot.child("pStatus").getValue(String.class);
                                if(ans11.equals("on")){
                                    cardView.setVisibility(View.VISIBLE);
                                    sliderSupport();
                                }else {
                                    cardView.setVisibility(View.GONE);

                                }
                            }else {
                                cardView.setVisibility(View.GONE);


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            cardView.setVisibility(View.GONE);


                        }
                    });

        }catch (Exception e){
            cardView.setVisibility(View.GONE);

        }



        // Rest of your code to initialize views or perform other operations

        return view;
    }

    private void retriveTodayData(int num) {
        noNewBook.setVisibility(View.GONE);
        bookPostAdapter.setViewTypeToShow(1);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        recyclerView.setAdapter(bookPostAdapter);
        //start date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -num);
        Date times=calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy,EEEE", Locale.getDefault());
        String date = sdf.format(times);

        FirebaseDatabase.getInstance().getReference("Book Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BookModel bookPostModel;
                if(snapshot.exists()){
                    bookList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        bookPostModel = dataSnapshot.getValue(BookModel.class);
                        if (bookPostModel != null && bookPostModel.getBookPostDate().equals(date)) {
                            if(bookList.size()<100){
                                bookList.add(bookPostModel);
                            }
                        }

                    }

                }
                bookPostAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                Collections.shuffle(bookList);
                recyclerView.setVisibility(View.VISIBLE);

                if(bookList.size()==0){
                    noNewBook.setVisibility(View.VISIBLE);
                }
                else {
                    noNewBook.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);

            }
        });
        swipeRefreshLayout.setRefreshing(false);

    }


    private void retriveNewbook() {
        noNewBook.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        bookPostAdapter.setViewTypeToShow(1);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        recyclerView.setAdapter(bookPostAdapter);

        progressBar.setVisibility(View.VISIBLE);
        //start date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -10);
        Date startDate = calendar.getTime();
        FirebaseDatabase.getInstance().getReference("Book Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BookModel bookPostModel;
                if(snapshot.exists()){
                    bookList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        bookPostModel = dataSnapshot.getValue(BookModel.class);
                        if (bookPostModel != null && isDateInRange(bookPostModel.getBookPostDate(),startDate)) {
                            if(bookList.size()<100){
                                bookList.add(bookPostModel);
                            }
                        }

                    }

                }
                bookPostAdapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Collections.shuffle(bookList);
                if(bookList.size()==0){
                    noNewBook.setVisibility(View.VISIBLE);
                }
                else {
                    noNewBook.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);

            }
        });
        swipeRefreshLayout.setRefreshing(false);


    }



    private boolean isDateInRange(String transactionDate, Date startDate) {
        // Assuming transactionDate is in the format "dd MMMM yyyy,EEEE"
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy,EEEE", Locale.getDefault());

        try {
            Date date = sdf.parse(transactionDate);
            // Check if parsed date is after or equal to startDate
            return date.after(startDate) || date.equals(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }



    public void sliderSupport() {


        final List<SlideModel> remoteimages = new ArrayList<>(); // SlideModel is an inbuilt model class made by the GitHub library provider
        List<String> remoteimages1 = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("Ads Control").child("Premium ads")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot data : snapshot.getChildren()) {
                                remoteimages.add(new SlideModel(
                                        data.child("image").getValue().toString(),
                                        data.child("title").getValue().toString(),
                                        ScaleTypes.FIT));
                                //  Toast.makeText(MainActivity.this, ""+data.child("siteUrl").getValue(String.class),Toast.LENGTH_SHORT).show();
                                remoteimages1.add((String) data.child("siteUrl").getValue());
                            }
                            // Set the image list and click listener outside the loop
                            imageSlider.setImageList(remoteimages, ScaleTypes.FIT);
                            imageSlider.setItemClickListener(new ItemClickListener() {
                                @Override
                                public void onItemSelected(int i) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(String.valueOf(remoteimages1.get(i))));
                                    startActivity(intent);
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