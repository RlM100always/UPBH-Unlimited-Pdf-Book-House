package com.techtravelcoder.educationalbooks.fragmentactivity;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.List;


public class BlankCategoryFragment extends Fragment {


    private EditText editText;
    private RecyclerView recyclerView;
    private ArrayList<BookCategoryModel> bookCategoryList;
    private BookCategoryAdapter bookCategoryAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private LinearLayout layout;
    public BlankCategoryFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =inflater.inflate(R.layout.fragment_blank_category, container, false);
        recyclerView=view.findViewById(R.id.new_category_recyclerview_id);
        progressBar=view.findViewById(R.id.progressBar_category);
        layout=view.findViewById(R.id.category_no_item);
        editText=view.findViewById(R.id.book_categoyr_searhc_edtext);

        swipeRefreshLayout=view.findViewById(R.id.swipe_refresh_layout_category);
        retriveBookCategoryDetailsData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                editText.setText("");
                retriveBookCategoryDetailsData();

            }
        });
        editText.addTextChangedListener(new TextWatcher() {
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


        return view;
    }

    private void retriveBookCategoryDetailsData() {
        progressBar.setVisibility(View.VISIBLE);
        bookCategoryList=new ArrayList<>();
        bookCategoryAdapter=new BookCategoryAdapter(getContext(),bookCategoryList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        recyclerView.setAdapter(bookCategoryAdapter);
        recyclerView.setHasFixedSize(true);

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

    private void searchList(String query) {
        List<BookCategoryModel> filteredList = new ArrayList<>();
        String queryWithoutSpaces = query.replaceAll("[/><:{}`+=*.||?()$#%!\\-,@&_\\n\\s]", "").toLowerCase();

        for (BookCategoryModel obj : bookCategoryList) {
            String objStringWithoutSpaces = obj.toString().replaceAll("[/><:{}`+=*.||?()$#%!\\-,@&_\\n\\s]", "").toLowerCase();

            if (objStringWithoutSpaces.contains(queryWithoutSpaces)) {
                filteredList.add(obj);
            }
        }
        if(filteredList.size()==0){
            layout.setVisibility(View.VISIBLE);
        }else {
            layout.setVisibility(View.GONE);

        }

        // Update your UI with the filtered list
        bookCategoryAdapter.searchLists((ArrayList<BookCategoryModel>) filteredList);
        bookCategoryAdapter.notifyDataSetChanged();


    }


}