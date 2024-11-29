package com.techtravelcoder.educationalbooks.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.educationalbooks.R;
import com.techtravelcoder.educationalbooks.adapter.BookAdapter;
import com.techtravelcoder.educationalbooks.model.BookModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AuthorPostActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText searchView;
    private String name,newname;

    private ArrayList<BookModel> bookList;
    private BookAdapter bookPostAdapter;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private AdView adView;

    private LinearLayout  linearLayout,adContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_post);

        adContainer=findViewById(R.id.author_banner_container);
        recyclerView=findViewById(R.id.author_post_recyclerview_id);
        linearLayout=findViewById(R.id.author_post_linearlayout);
        progressBar=findViewById(R.id.author_progressBar);
        searchView=findViewById(R.id.author_search);

        name=getIntent().getStringExtra("name");
        newname="*"+name+"*";

        loadsBannerAds();


        toolbar=findViewById(R.id.custom_toolbar);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(name);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.threeitembackcolor), PorterDuff.Mode.SRC_IN);


        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                searchList(s.toString());
            }

        });


        retriveBookDetailsData();
    }

    public void searchList(String query) {
        List<BookModel> filteredList = new ArrayList<>();
        String queryWithoutSpaces = query.replaceAll("[/><:{}`+=*.||?()$#%!\\-,@&_\\n\\s]", "").toLowerCase();

        for (BookModel obj : bookList) {
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


    private void retriveBookDetailsData() {
        bookList=new ArrayList<>();
        bookPostAdapter=new BookAdapter(AuthorPostActivity.this,bookList,1);
        bookPostAdapter.setViewTypeToShow(2);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
        recyclerView.setAdapter(bookPostAdapter);
        FirebaseDatabase.getInstance().getReference("Book Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                bookList.clear();
                BookModel bookPostModel;
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        bookPostModel = dataSnapshot.getValue(BookModel.class);
                        if(bookPostModel != null){
                            if(bookPostModel.getBookKeyWord().contains(newname)){
                                bookList.add(0,bookPostModel);
                            }
                        }

                    }

                }
                Collections.shuffle(bookList);
                bookPostAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                if(bookList.size()==0){
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

                                adView = new AdView(AuthorPostActivity.this, "1051671613095414_1057857532476822", AdSize.BANNER_HEIGHT_50);

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



    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }


}