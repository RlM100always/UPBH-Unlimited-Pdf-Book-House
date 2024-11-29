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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.techtravelcoder.educationalbooks.pdf.DatabaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookMarkFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookAdapter bookPostAdapter;
    private ArrayList<BookModel> bookList;

    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    DatabaseHelper databaseHelper;
    private EditText editText;


    public BookMarkFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_mark, container, false);

        recyclerView = view.findViewById(R.id.bookmark_recyclerview_id);
        progressBar = view.findViewById(R.id.progressBar_bookmark);
        linearLayout = view.findViewById(R.id.bookmark_book_visible);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_lay_bookmark);
        databaseHelper = new DatabaseHelper(getContext());
        editText=view.findViewById(R.id.bookmark_search_ed);

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



        progressBar.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                editText.setText("");
                retrieveBookMark();
            }
        });
        retrieveBookMark();
        return view;
    }

    private void searchList(String query) {
        List<BookModel> filteredList = new ArrayList<>();
        String queryWithoutSpaces = query.replaceAll("[/><:{}`+=*.||?()$#%!\\-,@&_\\n\\s]", "").toLowerCase();

        for (BookModel obj : bookList) {
            String objStringWithoutSpaces = obj.toString().replaceAll("[/><:{}`+=*.||?()$#%!\\-,@&_\\n\\s]", "").toLowerCase();

            if (objStringWithoutSpaces.contains(queryWithoutSpaces)) {
                filteredList.add(obj);
            }
        }

        if (filteredList.size() == 0) {
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.GONE);
        }
        // Update your UI with the filtered list
        bookPostAdapter.searchLists((ArrayList<BookModel>) filteredList);
        bookPostAdapter.notifyDataSetChanged();
    }

    private void retrieveBookMark() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setHasFixedSize(true);

        bookList = new ArrayList<>();
        bookPostAdapter = new BookAdapter(getContext(), bookList, 3);
        bookPostAdapter.setViewTypeToShow(3);
        recyclerView.setAdapter(bookPostAdapter);

        // Retrieve the keys sorted as per the database order
        List<String> newlist = databaseHelper.checkStatusAndReturnKeys();




        FirebaseDatabase.getInstance().getReference("Book Details").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookList.clear();
                if (snapshot.exists()) {
                    // Create a map to store Firebase data by key for quick lookup
                    Map<String, BookModel> firebaseBooksMap = new HashMap<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        BookModel bookPostModel = dataSnapshot.getValue(BookModel.class);
                        String bookKey = dataSnapshot.getKey();
                        if (bookKey != null) {
                            firebaseBooksMap.put(bookKey, bookPostModel);
                        }
                    }

                    // Iterate through newlist to maintain the order and add matching books to bookList
                    for (String key : newlist) {
                        BookModel bookPostModel = firebaseBooksMap.get(key);
                        if (bookPostModel != null) {
                            bookList.add(bookPostModel);
                        }
                    }
                    Collections.reverse(bookList);


                    bookPostAdapter.notifyDataSetChanged(); // Notify adapter of data change
                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (bookList.isEmpty()) {
                                linearLayout.setVisibility(View.VISIBLE);
                            } else {
                                linearLayout.setVisibility(View.GONE);
                            }
                        }
                    }, 500);
                } else {
                    Toast.makeText(getContext(), "No books found.", Toast.LENGTH_SHORT).show();
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Failed to fetch books: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



}
