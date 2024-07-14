package com.techtravelcoder.educationalbooks.fragmentactivity;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.techtravelcoder.educationalbooks.model.BookModel;

import java.util.ArrayList;


public class BookMarkFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookAdapter bookPostAdapter;
    private ArrayList<BookModel>bookList;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

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
        linearLayout=view.findViewById(R.id.bookmark_book);
        swipeRefreshLayout=view.findViewById(R.id.swipe_refresh_lay_bookmark);

        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.threeitembackcolor), PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrivebookMark();
            }
        });
        retrivebookMark();



        return view;
    }

    private void retrivebookMark(){
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
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
                            }
                        }

                        bookPostAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);


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


                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }

    }

}