package com.techtravelcoder.educationalbooks.fragmentactivity;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.educationalbooks.R;
import com.techtravelcoder.educationalbooks.adapter.BookCategoryAdapter;
import com.techtravelcoder.educationalbooks.model.BookCategoryModel;

import java.util.ArrayList;
import java.util.Collections;


public class BlankCategoryFragment extends Fragment {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private ArrayList<BookCategoryModel> bookCategoryList;
    private BookCategoryAdapter bookCategoryAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    public BlankCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =inflater.inflate(R.layout.fragment_blank_category, container, false);
        recyclerView=view.findViewById(R.id.new_category_recyclerview_id);
        progressBar=view.findViewById(R.id.progressBar_category);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.threeitembackcolor), PorterDuff.Mode.SRC_IN);

        swipeRefreshLayout=view.findViewById(R.id.swipe_refresh_layout_category);
        retriveBookCategoryDetailsData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retriveBookCategoryDetailsData();

            }
        });
        return view;
    }

    private void retriveBookCategoryDetailsData() {
        progressBar.setVisibility(View.VISIBLE);
        bookCategoryList=new ArrayList<>();
        bookCategoryAdapter=new BookCategoryAdapter(getContext(),bookCategoryList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        recyclerView.setAdapter(bookCategoryAdapter);
        FirebaseDatabase.getInstance().getReference("Book Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookCategoryList.clear();
                BookCategoryModel bookCategoryModel;
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        bookCategoryModel = dataSnapshot.getValue(BookCategoryModel.class);
                        if(bookCategoryModel != null){
                            bookCategoryList.add(0,bookCategoryModel);

                        }

                    }
                }
                Collections.shuffle(bookCategoryList);
                bookCategoryAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);

            }
        });
        swipeRefreshLayout.setRefreshing(false);

    }

}