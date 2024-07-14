package com.techtravelcoder.educationalbooks.fragmentactivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.educationalbooks.R;
import com.techtravelcoder.educationalbooks.activity.GoogleSignInHelper;
import com.techtravelcoder.educationalbooks.adapter.BookAdapter;
import com.techtravelcoder.educationalbooks.model.BookModel;
import com.techtravelcoder.educationalbooks.pdf.PDFShowActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
        progressBar=view.findViewById(R.id.progressBar_new_book);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.threeitembackcolor), PorterDuff.Mode.SRC_IN);



        swipeRefreshLayout=view.findViewById(R.id.swipe_refresh_layout_new_books);
        bookList=new ArrayList<>();
        bookPostAdapter=new BookAdapter(getContext(),bookList,2);


        if (getArguments() != null) {
            bCataKey = getArguments().getString("key");
            categoryName = getArguments().getString("category");
        }

        retriveNewbook();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retriveNewbook();
            }
        });


        // Rest of your code to initialize views or perform other operations

        return view;
    }




    private void retriveNewbook() {
        progressBar.setVisibility(View.VISIBLE);
        //start date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -10);
        Date startDate = calendar.getTime();

        bookPostAdapter.setViewTypeToShow(1);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(bookPostAdapter);
        FirebaseDatabase.getInstance().getReference("Book Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookList.clear();
                BookModel bookPostModel;
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        bookPostModel = dataSnapshot.getValue(BookModel.class);
                        if (bookPostModel != null && isDateInRange(bookPostModel.getBookPostDate(),startDate)) {
                            bookList.add(0,bookPostModel);
                        }

                    }

                }
                bookPostAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                if(bookList.size()==0){
                   noNewBook.setVisibility(View.VISIBLE);
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

}