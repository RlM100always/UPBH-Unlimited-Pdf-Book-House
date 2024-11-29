package com.techtravelcoder.educationalbooks.fragmentactivity;

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
import com.techtravelcoder.educationalbooks.adapter.AuthorAdapter;
import com.techtravelcoder.educationalbooks.adapter.BookCategoryAdapter;
import com.techtravelcoder.educationalbooks.model.AuthorModel;
import com.techtravelcoder.educationalbooks.model.BookCategoryModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class AuthorFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditText editText;
    private ArrayList<AuthorModel>authorlist;
    private AuthorAdapter authorAdapter;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final int TYPE_ALTERNATE = 1;


    public AuthorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_author, container, false);

        recyclerView=view.findViewById(R.id.author_recycler_id);
        editText=view.findViewById(R.id.author_search_ed);
        linearLayout=view.findViewById(R.id.author_id_lin);
        progressBar=view.findViewById(R.id.author_progressBar_category);
        swipeRefreshLayout=view.findViewById(R.id.author_swipe_refreshs);
        retriveAuthorDetailsData();

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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                editText.setText("");
                retriveAuthorDetailsData();

            }
        });



        return view ;
    }


    private void retriveAuthorDetailsData() {
        progressBar.setVisibility(View.VISIBLE);
        authorlist=new ArrayList<>();
        authorAdapter=new AuthorAdapter(getContext(),authorlist);
        GridLayoutManager gridLayoutManager= new GridLayoutManager(getContext(),2);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                // Use your adapter's getItemViewType to check if it's an ad or regular item
                return authorAdapter.getItemViewType(position) == TYPE_ALTERNATE ? 2: 1;
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(authorAdapter);
        recyclerView.setHasFixedSize(true);

        FirebaseDatabase.getInstance().getReference("Author").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                authorlist.clear();
                AuthorModel authorModel;
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        authorModel = dataSnapshot.getValue(AuthorModel.class);
                        if(authorModel != null){
                            authorlist.add(0,authorModel);
                        }

                    }
                }
                Collections.shuffle(authorlist);
                authorAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);

            }
        });

    }

    private void searchList(String query) {
        List<AuthorModel> filteredList = new ArrayList<>();
        String queryWithoutSpaces = query.replaceAll("[/><:{}`+=*.||?()$#%!\\-,@&_\\n\\s]", "").toLowerCase();

        for (AuthorModel obj : authorlist) {
            String objStringWithoutSpaces = obj.toString().replaceAll("[/><:{}`+=*.||?()$#%!\\-,@&_\\n\\s]", "").toLowerCase();

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
        authorAdapter.searchLists((ArrayList<AuthorModel>) filteredList);
        authorAdapter.notifyDataSetChanged();


    }


}