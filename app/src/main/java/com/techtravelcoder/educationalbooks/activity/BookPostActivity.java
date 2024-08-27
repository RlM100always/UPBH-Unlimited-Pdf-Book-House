package com.techtravelcoder.educationalbooks.activity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class BookPostActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private String bCataKey,categoryName;

    private ArrayList<BookModel> bookList;
    private BookAdapter bookPostAdapter;
    private ProgressBar progressBar;
    private TextView textView;
    private Toolbar condition;
    private ImageView imageView;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_post);

        bCataKey=getIntent().getStringExtra("key");
        categoryName=getIntent().getStringExtra("category");




        //initialize
        searchView=findViewById(R.id.book_post_search);
        recyclerView=findViewById(R.id.book_post_recyclerview_id);
        progressBar=findViewById(R.id.book_progressBar);
        textView=findViewById(R.id.book_text);
        imageView=findViewById(R.id.book_image);
        condition=findViewById(R.id.text_id);
        linearLayout=findViewById(R.id.book_post_linearlayout);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });
        //progressbar color
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.threeitembackcolor), PorterDuff.Mode.SRC_IN);
        condition.setTitle(categoryName+"");
        condition.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });





        retriveBookDetailsData();
    }

    public void searchList(String query) {
        List<BookModel> filteredList = new ArrayList<>();
        String queryWithoutSpaces = query.replaceAll("\\s+", "").toLowerCase(); // Remove spaces from query

        for (BookModel obj : bookList) {
            String objStringWithoutSpaces = obj.toString().replaceAll("\\s+", "").toLowerCase(); // Remove spaces from object

            // Perform search based on bCategoryName without spaces and case-insensitive
            if (obj.getBookName().replaceAll("\\s+", "").toLowerCase().contains(queryWithoutSpaces)) {
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
        bookPostAdapter=new BookAdapter(BookPostActivity.this,bookList,1);
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
                            if(bookPostModel.getBookCategoryKey().equals(bCataKey)){
                                bookList.add(0,bookPostModel);
                            }
                        }

                    }

                }
                Collections.shuffle(bookList);
                bookPostAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                if(bookList.size()==0){
                    textView.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}