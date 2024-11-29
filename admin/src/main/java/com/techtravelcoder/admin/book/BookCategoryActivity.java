package com.techtravelcoder.admin.book;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.admin.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookCategoryActivity extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private ArrayList<BookCategoryModel>bookCategoryList;
    private BookCategoryAdapter bookCategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_category);

        //initialize all attributes/variable

        searchView=findViewById(R.id.book_categoyr_searhc);
        recyclerView=findViewById(R.id.book_category_recycler_view_id);
        floatingActionButton=findViewById(R.id.book_category_float_id);

        //set onclick listener

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputCategoryFromAdmin();
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


        //retrive category data

        retriveBookCategoryDetailsData();

    }




    private void searchList(String newText) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<BookCategoryModel> fList = new ArrayList<>();
                for (BookCategoryModel obj : bookCategoryList) {
                    if (obj.getbCategoryName().toLowerCase().replaceAll("\\s","").contains(newText.toLowerCase().trim().replaceAll("\\s",""))) {
                        fList.add(obj);
                    }
                }

                // Update the UI on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bookCategoryAdapter.searchListsFunc((ArrayList<BookCategoryModel>) fList);
                        bookCategoryAdapter.notifyDataSetChanged();

                    }
                });
            }
        }).start();
    }


    private void retriveBookCategoryDetailsData() {
        bookCategoryList=new ArrayList<>();
        bookCategoryAdapter=new BookCategoryAdapter(BookCategoryActivity.this,bookCategoryList);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
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
                bookCategoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void inputCategoryFromAdmin() {
        AlertDialog.Builder builder=new AlertDialog.Builder(BookCategoryActivity.this);
        final View view=getLayoutInflater().inflate(R.layout.book_category_design,null);

        EditText bookCategoryName=view.findViewById(R.id.ed_book_category_id);
        EditText bookCategoryImageLink=view.findViewById(R.id.ed_book_category_image_link);
        EditText bookCategoryKeyWord=view.findViewById(R.id.key_word_id);


        TextView addCategoyry=view.findViewById(R.id.book_category_add_id);
        TextView cancel=view.findViewById(R.id.category_cancel_settings_id);



        builder.setView(view);
        AlertDialog alertDialog= builder.create();
        Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.back);
        alertDialog.getWindow().setBackgroundDrawable(drawable);
        alertDialog.show();
        alertDialog.setCancelable(false);
        addCategoyry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(bookCategoryName.getText().toString()) && !TextUtils.isEmpty(bookCategoryImageLink.getText().toString()) ){

                    uploadBooKCategoryDetailsInFirebase(bookCategoryName,bookCategoryImageLink,bookCategoryKeyWord);

                    alertDialog.dismiss();

                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        Drawable drawables = ContextCompat.getDrawable(getApplicationContext(), R.drawable.alert_back);
        alertDialog.getWindow().setBackgroundDrawable(drawables);
    }

    private void uploadBooKCategoryDetailsInFirebase(EditText bookCategoryName, EditText bookCategoryImageLink,EditText bookCategoryKeyWord ) {

        String entryKey = FirebaseDatabase.getInstance().getReference("Book Category").push().getKey();

        Map<String,Object> map=new HashMap<>();

        map.put("bCategoryName",bookCategoryName.getText().toString());
        map.put("bCategoryImageLink",bookCategoryImageLink.getText().toString());
        map.put("bCategoryKeyword",bookCategoryKeyWord.getText().toString());
        map.put("bCategoryKey",entryKey);

        if(bookCategoryImageLink.getText().toString().length()<=200){
            Toast.makeText(this, ""+bookCategoryKeyWord.getText().toString(), Toast.LENGTH_SHORT).show();
            FirebaseDatabase.getInstance().getReference("Book Category").child(entryKey).setValue(map).
                    addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(BookCategoryActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else {
            Toast.makeText(this, "Reduce Size", Toast.LENGTH_SHORT).show();
        }



    }

}