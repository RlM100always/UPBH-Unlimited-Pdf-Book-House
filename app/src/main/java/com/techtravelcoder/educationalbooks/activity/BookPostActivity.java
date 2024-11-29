package com.techtravelcoder.educationalbooks.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAdListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.educationalbooks.R;
import com.techtravelcoder.educationalbooks.adapter.BookAdapter;
import com.techtravelcoder.educationalbooks.adapter.SubCataAdapter;
import com.techtravelcoder.educationalbooks.model.BookModel;
import com.techtravelcoder.educationalbooks.model.SubCataModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BookPostActivity extends AppCompatActivity  {

    private RecyclerView recyclerView,subcatarecycler;
    private EditText searchView;
    private String bCataKey,categoryName;

    private ArrayList<BookModel> bookList,bookListAll;
    private BookAdapter bookPostAdapter;
    private ProgressBar progressBar;

    SwipeRefreshLayout swipeRefreshLayout ;

    private LinearLayout linearLayout,adContainer;
    private Toolbar toolbar;
    private AdView adView;
    private Switch swi;

    private ArrayList<SubCataModel> sublist;

    private SubCataAdapter subCataAdapter;
    SubCataModel subCataModel;
    private String name;
    int selectedPosition;
    SharedPreferences sharedPreferences1;
    private TextView t1,t2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_post);

        //initialize
        toolbar=findViewById(R.id.custom_toolbar);
        swi=findViewById(R.id.custom_switch_mode);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        searchView=findViewById(R.id.book_post_search);
        recyclerView=findViewById(R.id.book_post_recyclerview_id);
        progressBar=findViewById(R.id.book_progressBar);
        linearLayout=findViewById(R.id.book_post_linearlayout);
        subcatarecycler=findViewById(R.id.horizontal_recyclerview);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout1);
        t1=findViewById(R.id.sub_cata_textview1);
        t2=findViewById(R.id.sub_cata_textview2);

        bCataKey=getIntent().getStringExtra("key");
        categoryName=getIntent().getStringExtra("category");
        adContainer = findViewById(R.id.banner_container);
        name=getIntent().getStringExtra("subcategory");

        SharedPreferences sharedPreferences = getSharedPreferences("selected_item_pref", Context.MODE_PRIVATE);
        selectedPosition = sharedPreferences.getInt("selected_position", -1);  // Default to -1 if no position is saved

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                SharedPreferences sharedPreferences1 = getSharedPreferences("AppPrefs1", Context.MODE_PRIVATE);
                String savedCategory = sharedPreferences1.getString("stored_category", "All");
                toolbarTitle.setText(savedCategory+"");
                selectedPosition=1000000000;
                searchView.setText("");
                retrieveBookDetailsData();
                retrieveSubCata();
                t1.setVisibility(View.VISIBLE);
                t2.setText("All Book List -->");
                linearLayout.setVisibility(View.GONE);
                if (swi.isChecked()) {
                    swi.setChecked(false);
                }

                swipeRefreshLayout.setRefreshing(false);
            }
        });

        swi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    t1.setVisibility(View.GONE);
                    subcatarecycler.setVisibility(View.GONE);
                    Toast.makeText(BookPostActivity.this, "Sub Category Visibility Off", Toast.LENGTH_SHORT).show();

                } else {
                    t1.setVisibility(View.VISIBLE);
                    subcatarecycler.setVisibility(View.VISIBLE);
                    Toast.makeText(BookPostActivity.this, "Sub Category Visibility On", Toast.LENGTH_SHORT).show();

                }


            }
        });

        if(categoryName!=null){
            sharedPreferences1 = getSharedPreferences("AppPrefs1", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
            editor1.putString("stored_category", categoryName);
            editor1.apply();
        }





        if(!name.equals("-/(.<10}")){

            SharedPreferences sharedPreferences1 = getSharedPreferences("AppPrefs1", Context.MODE_PRIVATE);
            String savedCategory = sharedPreferences1.getString("stored_category", "All");
            toolbarTitle.setText(savedCategory+" --> "+name+"");
            t1.setVisibility(View.VISIBLE);
            t2.setVisibility(View.VISIBLE);
            t2.setText(name+" Book List -->");
            retrive();
            retrieveSubCata();
        }
        else {

            toolbarTitle.setText(categoryName+"");
            selectedPosition=1000000000;
            retrieveSubCata();
            retrieveBookDetailsData();

        }



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable newText) {
                t1.setVisibility(View.GONE);
                if (newText.toString().isEmpty()) {
                    if (swi.isChecked()) {
                        swi.setChecked(false);
                    }
                    subcatarecycler.setVisibility(View.VISIBLE);
                    searchList(newText.toString());
                } else {
                    subcatarecycler.setVisibility(View.GONE);
                    searchList(newText.toString());
                }
            }

        });
        loadsBannerAds();


    }


    private void retrieveBookDetailsData() {
        bookList = new ArrayList<>();
        bookListAll = new ArrayList<>();
        bookPostAdapter = new BookAdapter(BookPostActivity.this, bookList, 1);
        bookPostAdapter.setViewTypeToShow(2);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        recyclerView.setAdapter(bookPostAdapter);
        FirebaseDatabase.getInstance().getReference("Book Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookListAll.clear();
                bookList.clear();
                BookModel bookPostModel;

                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        bookPostModel = dataSnapshot.getValue(BookModel.class);
                        if (bookPostModel != null && bookPostModel.getBookCategoryKey().equals(bCataKey)) {
                            bookListAll.add(bookPostModel);
                        }
                    }
                }

                Collections.shuffle(bookListAll);
                for (int i = 0; i < Math.min(150, bookListAll.size()); i++) {
                    bookList.add(bookListAll.get(i));
                }
                progressBar.setVisibility(View.GONE);

                // Show the linearLayout if no books were added to bookList
                if (bookListAll.isEmpty()) {
                    linearLayout.setVisibility(View.VISIBLE);
                }

                // Notify the adapter that data has changed
                bookPostAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void loadsBannerAds() {

        FirebaseDatabase.getInstance().getReference("Ads Control").child("faStatus")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            String ans = (String) snapshot.getValue();
                            if (ans.equals("on")) {

                                adView = new AdView(BookPostActivity.this, "1051671613095414_1057857532476822", AdSize.BANNER_HEIGHT_50);

                                adContainer.addView(adView);
                                adView.loadAd();
                            }else {
                                adContainer.setVisibility(View.GONE);
                            }
                        }else {
                            adContainer.setVisibility(View.GONE);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        adContainer.setVisibility(View.GONE);

                    }
                });



    }

    public void searchList(String query) {
        List<BookModel> filteredList = new ArrayList<>();
        String queryWithoutSpaces = query.replaceAll("[/><:{}`+=*.||?()$#%!\\-,@&_\\n\\s]", "").toLowerCase();

        for (BookModel obj : bookListAll) {
            String objStringWithoutSpaces = obj.toString().replaceAll("[/><:{}`+=*.||?()$#%!\\-,@&_\\n\\s]", "").toLowerCase();

            // Perform search based on bCategoryName without spaces and case-insensitive
            if (objStringWithoutSpaces.contains(queryWithoutSpaces)) {
                filteredList.add(obj);
            }
        }

        if(filteredList.size()==0){
            linearLayout.setVisibility(View.VISIBLE);
        }else {
            linearLayout.setVisibility(View.GONE);
        }
        // Update your UI with the filtered list
        bookPostAdapter.searchLists((ArrayList<BookModel>) filteredList);
        bookPostAdapter .notifyDataSetChanged();
    }


    private void retrieveSubCata() {

        sublist = new ArrayList<>();
        subCataAdapter = new SubCataAdapter(BookPostActivity.this, sublist,selectedPosition);

        // Set the RecyclerView to display items horizontally
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3, GridLayoutManager.VERTICAL, false);
        subcatarecycler.setLayoutManager(gridLayoutManager);
        subcatarecycler.setAdapter(subCataAdapter);

        FirebaseDatabase.getInstance().getReference("SubCata").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sublist.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        SubCataModel subCataModel = dataSnapshot.getValue(SubCataModel.class);
                        if (subCataModel != null && subCataModel.getK() != null && subCataModel.getK().equals(bCataKey)) {
                            sublist.add(0, subCataModel);
                        }
                    }
                }

                if(sublist.isEmpty()){
                    subcatarecycler.setVisibility(View.GONE);
                    t1.setVisibility(View.GONE);
                    t2.setVisibility(View.VISIBLE);
                    t2.setText("All Book List -->");
                }else {
                    swi.setVisibility(View.VISIBLE);
                    t1.setVisibility(View.VISIBLE);
                    t2.setVisibility(View.VISIBLE);
                }
                subCataAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error here if needed
            }
        });
    }


    private void retrive() {
        progressBar.setVisibility(View.VISIBLE);
        bookList=new ArrayList<>();
        bookListAll = new ArrayList<>();
        bookPostAdapter=new BookAdapter(BookPostActivity.this,bookList,1);
        bookPostAdapter.setViewTypeToShow(2);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
        recyclerView.setAdapter(bookPostAdapter);
        FirebaseDatabase.getInstance().getReference("Book Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                bookListAll.clear();
                bookList.clear();
                BookModel bookPostModel;
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String ans="*"+name+"*";

                        bookPostModel = dataSnapshot.getValue(BookModel.class);

                        if(bookPostModel != null){
                            if( bookPostModel.getBookKeyWord()!=null &&  bookPostModel.getBookCategoryKey().equals(bCataKey) && bookPostModel.getBookKeyWord().contains(ans)){
                                bookListAll.add(0,bookPostModel);
                            }
                        }

                    }

                }
                Collections.shuffle(bookListAll);

                for (int i = 0; i < Math.min(150, bookListAll.size()); i++) {
                    bookList.add(bookListAll.get(i));
                }


                progressBar.setVisibility(View.GONE);
                if(bookListAll.isEmpty()){
                    linearLayout.setVisibility(View.VISIBLE);
                }
                bookPostAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }


}