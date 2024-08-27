package com.techtravelcoder.educationalbooks.fragmentactivity;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.educationalbooks.R;
import com.techtravelcoder.educationalbooks.adapter.BookAdapter;
import com.techtravelcoder.educationalbooks.model.BookCategoryModel;
import com.techtravelcoder.educationalbooks.model.BookModel;

import java.util.ArrayList;
import java.util.List;


public class BookMarkFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookAdapter bookPostAdapter;
    private ArrayList<BookModel>bookList;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private androidx.appcompat.widget.SearchView searchView;

    public BookMarkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_book_mark, container, false);

        recyclerView=view.findViewById(R.id.bookmark_recyclerview_id);
        progressBar=view.findViewById(R.id.progressBar_bookmark);
        linearLayout=view.findViewById(R.id.bookmark_book_visible);
        swipeRefreshLayout=view.findViewById(R.id.swipe_refresh_lay_bookmark);
        searchView=view.findViewById(R.id.bookmark_searchview_id);

        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.threeitembackcolor), PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrivebookMark();
            }
        });
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
        retrivebookMark();
        return view;
    }


    private void searchList(String query) {
        List<BookModel> filteredList = new ArrayList<>();
        String queryWithoutSpaces = query.replaceAll("\\s+", "").toLowerCase(); // Remove spaces from query

        for (BookModel obj : bookList) {
            String objStringWithoutSpaces = obj.toString().replaceAll("\\s+", "").toLowerCase(); // Remove spaces from object

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
        bookPostAdapter.notifyDataSetChanged();
    }

    private void retrivebookMark(){
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        bookList=new ArrayList<>();
        bookPostAdapter=new BookAdapter(getContext(),bookList,3);
        bookPostAdapter.setViewTypeToShow(3);
        recyclerView.setAdapter(bookPostAdapter );
        if(FirebaseAuth.getInstance().getUid()!=null){
            FirebaseDatabase.getInstance().getReference("Book Details").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    bookList.clear();
                    if(snapshot.exists()){
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            BookModel bookPostModel;

                            bookPostModel = dataSnapshot.getValue(BookModel.class);
                            if (bookPostModel != null) {

                                checkFavorite(bookPostModel);
                            }else {
                                if(bookList.size()==0){
                                    linearLayout.setVisibility(View.VISIBLE);

                                }else {
                                    linearLayout.setVisibility(View.GONE);

                                }
                            }
                        }

                        bookPostAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);


                    }
                    else {

                        Toast.makeText(getContext(), "No", Toast.LENGTH_SHORT).show();

                        linearLayout.setVisibility(View.VISIBLE);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Failed to fetch categories: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            Toast.makeText(getContext(), "Login First", Toast.LENGTH_SHORT).show();
            linearLayout.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);

        }
    }
    private void checkFavorite(BookModel bookPostModel) {
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            FirebaseDatabase.getInstance().getReference("Book Details").child(bookPostModel.getBookKey())
                    .child("bookmark")
                    .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if(snapshot.exists()){
                                if ((snapshot.getValue(Boolean.class) != null) && (snapshot.getValue(Boolean.class))) {
                                    bookList.add(bookPostModel);

                                }
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(bookList.size()==0){
                                           linearLayout.setVisibility(View.VISIBLE);

                                        }else {
                                            linearLayout.setVisibility(View.GONE);

                                        }
                                    }
                                },1000);

                                bookPostAdapter.notifyDataSetChanged();


                            }else {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(bookList.size()==0){
                                            linearLayout.setVisibility(View.VISIBLE);

                                        }else {
                                            linearLayout.setVisibility(View.GONE);

                                        }
                                    }
                                },1000);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }

    }

}