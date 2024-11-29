package com.techtravelcoder.educationalbooks.fragmentactivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.educationalbooks.R;
import com.techtravelcoder.educationalbooks.adapter.BookAdapter;
import com.techtravelcoder.educationalbooks.model.BookModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class AllBookFragment extends Fragment {


    private RecyclerView recyclerView,searchRecyclerView;
    private ArrayList<BookModel> bookList;
    private BookAdapter bookPostAdapter;
    private NestedScrollView nestedScrollView;
    private String lastitemid;
    private static int PAGE_SIZE=7;
    private Query query;
    private DatabaseReference databaseReference;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    private EditText editText;
    private Button button;


    public AllBookFragment() {
        databaseReference=FirebaseDatabase.getInstance().getReference("Book Details");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_all_book, container, false);
        recyclerView=view.findViewById(R.id.all_recyclerview_id);
        nestedScrollView=view.findViewById(R.id.all_item_nested_id);
        editText=view.findViewById(R.id.allbook_search_ed);
        button=view.findViewById(R.id.search_btn);
        searchRecyclerView=view.findViewById(R.id.all_search_recyclerview_id);
        swipeRefreshLayout=view.findViewById(R.id.all_swipe_refresh_id);
        progressBar=view.findViewById(R.id.home_progressbar);

        linearLayout=view.findViewById(R.id.home_ll_id);

        int searchPlateId = editText.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = editText.findViewById(searchPlateId);

        if (searchPlate != null) {
            searchPlate.setBackgroundResource(android.R.color.transparent);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                editText.setText("");
                Random random=new Random();
                int num=random.nextInt(7);
                fetchPostData(num);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchList(editText.getText().toString());
                progressBar.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);

            }
        });

        progressBar.setVisibility(View.VISIBLE);
        Random random=new Random();
        int num=random.nextInt(21);
        fetchPostData(num);

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                retrivePaginateData();

            }
        });


        return view;
    }

    private void fetchPostData(int num) {

        recyclerView.setVisibility(View.VISIBLE);
        searchRecyclerView.setVisibility(View.GONE);

        bookList = new ArrayList<>();
        bookPostAdapter = new BookAdapter(getContext(),bookList,1);
        bookPostAdapter.setViewTypeToShow(2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(bookPostAdapter);


        if(num==0){
            query=databaseReference.limitToFirst(15);
        }else if(num==1){
            query = databaseReference.orderByChild("bookName")
                    .startAt("A").endAt("A\uf8ff");
        }
        else if(num==2){
            query = databaseReference.orderByChild("bookName")
                    .startAt("B").endAt("C\uf8ff");
        }
        else if(num==3){
            query = databaseReference.orderByChild("bookName")
                    .startAt("C").endAt("C\uf8ff");
        }

        else if(num==4){
            query = databaseReference.orderByChild("bookName")
                    .startAt("H").endAt("H\uf8ff");
        }
        else if(num==5){
            query = databaseReference.orderByChild("bookName")
                    .startAt("M").endAt("M\uf8ff");
        }
        else if(num==6){
            query = databaseReference.orderByChild("bookName")
                    .startAt("P").endAt("P\uf8ff");
        }
        else if(num==7){
            query = databaseReference.orderByChild("bookName")
                    .startAt("D").endAt("D\uf8ff");
        }
        else if(num==8){
            query = databaseReference.orderByChild("bookName")
                    .startAt("E").endAt("E\uf8ff");
        }
        else if(num==9){
            query = databaseReference.orderByChild("bookName")
                    .startAt("F").endAt("F\uf8ff");
        }
        else if(num==10){
            query = databaseReference.orderByChild("bookName")
                    .startAt("G").endAt("G\uf8ff");
        }
        else if(num==11){
            query = databaseReference.orderByChild("bookName")
                    .startAt("I").endAt("I\uf8ff");
        }
        else if(num==12){
            query = databaseReference.orderByChild("bookName")
                    .startAt("J").endAt("J\uf8ff");
        }
        else if(num==13){
            query = databaseReference.orderByChild("bookName")
                    .startAt("K").endAt("K\uf8ff");
        }
        else if(num==14){
            query = databaseReference.orderByChild("bookName")
                    .startAt("L").endAt("L\uf8ff");
        }
        else if(num==15){
            query = databaseReference.orderByChild("bookName")
                    .startAt("N").endAt("N\uf8ff");
        }
        else if(num==16){
            query = databaseReference.orderByChild("bookName")
                    .startAt("O").endAt("O\uf8ff");
        }
        else if(num==17){
            query = databaseReference.orderByChild("bookName")
                    .startAt("Q").endAt("Q\uf8ff");
        }
        else if(num==18){
            query = databaseReference.orderByChild("bookName")
                    .startAt("R").endAt("R\uf8ff");
        }
        else if(num==19){
            query = databaseReference.orderByChild("bookName")
                    .startAt("T").endAt("T\uf8ff");
        }
        else if(num==20){
            query = databaseReference.orderByChild("bookName")
                    .startAt("T").endAt("T\uf8ff");
        }




        if(query!=null){
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BookModel bookPostModel;
                if(snapshot.exists()){
                    bookList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        bookPostModel = dataSnapshot.getValue(BookModel.class);
                        if (bookPostModel != null) {
                                bookList.add(bookPostModel);
                                lastitemid=bookPostModel.getBookKey();
                        }

                    }

                }
                if(bookList.isEmpty()){
                    linearLayout.setVisibility(View.VISIBLE);
                }else {
                    linearLayout.setVisibility(View.GONE);

                }
                Collections.shuffle(bookList);
                bookPostAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        }
    }
    public void retrivePaginateData(){
        Query nextPageQuery = databaseReference.orderByKey().startAfter(lastitemid).limitToFirst(PAGE_SIZE);
        nextPageQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BookModel bookPostModel;
                if(snapshot.exists()){

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        bookPostModel = dataSnapshot.getValue(BookModel.class);
                        if (bookPostModel != null) {
                            bookList.add(bookPostModel);
                            lastitemid=bookPostModel.getBookKey();
                        }
                    }

                }


                bookPostAdapter.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to fetch categories: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void searchList(String query) {
       // nestedScrollView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        searchRecyclerView.setVisibility(View.VISIBLE);

        // Initialize a new list to store the search results
        List<BookModel> searchResults = new ArrayList<>();
        List<BookModel> searchResultsAll = new ArrayList<>();
        bookPostAdapter = new BookAdapter(getContext(), (ArrayList<BookModel>) searchResults,1);
        bookPostAdapter.setViewTypeToShow(2);


        // Create a new instance of LinearLayoutManager
        LinearLayoutManager searchLayoutManager = new LinearLayoutManager(getContext());

        searchRecyclerView.setAdapter(bookPostAdapter);
        searchRecyclerView.setLayoutManager(searchLayoutManager);

        // Filter the data from Firebase
        String queryWithoutSpaces =  query.replaceAll("[/><:{}`+=*.||?()$#%!\\-,@&_\\n\\s]", "").toLowerCase();
        FirebaseDatabase.getInstance().getReference("Book Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                searchResults.clear();
                searchResultsAll.size();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BookModel bookModel = dataSnapshot.getValue(BookModel.class);
                    if (bookModel != null) {

                        String searchText=bookModel.getBookName()+bookModel.getBookCategoryName()+bookModel.getBookKeyWord()+bookModel.getBookCategoryType()+bookModel.getBookPriceType()+bookModel.getBookLanguageType()+bookModel.getBookPageNo();

                        String objStringWithoutSpaces = searchText.replaceAll("[/><:{}`+=*.||?()$#%!\\-,@&_\\n\\s]", "").toLowerCase();
                        if (objStringWithoutSpaces.contains(queryWithoutSpaces)) {

                            searchResultsAll.add(bookModel);

                        }
                    }
                }

                for (int i = 0; i < Math.min(150, searchResultsAll.size()); i++) {
                    searchResults.add(searchResultsAll.get(i));
                }

                if(searchResults.size()==0){
                    linearLayout.setVisibility(View.VISIBLE);
                }else {
                    linearLayout.setVisibility(View.GONE);

                }

                progressBar.setVisibility(View.GONE);
                bookPostAdapter.notifyDataSetChanged(); // Notify adapter about data changes
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                linearLayout.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Failed to fetch book: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}