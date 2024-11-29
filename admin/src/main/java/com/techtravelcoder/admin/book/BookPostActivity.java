package com.techtravelcoder.admin.book;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BookPostActivity extends AppCompatActivity {

    private RecyclerView recyclerView,subcatarecycler;
    private FloatingActionButton floatingActionButton,subcatafloat;
    private SearchView searchView;
    private String bCataKey,bCataNAme,bCataKeyWord;

    private ArrayList<BookPostModel> bookList;
    private ArrayList<SubCataModel> sublist;
    private BookPostAdapter bookPostAdapter;
    private SubCataAdapter subCataAdapter;
    SubCataModel subCataModel;
    BookPostModel bookPostModel;
    private String bookCategoryTypeS,bookPriceTypeS,bookLanguageTypeS;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_post);

        bCataKey="";
        bCataKey=getIntent().getStringExtra("key");
        bCataNAme=getIntent().getStringExtra("bCategory");
        bCataKeyWord=getIntent().getStringExtra("bCategoryKeyword");



        //initialize
        searchView=findViewById(R.id.book_post_search);
        recyclerView=findViewById(R.id.book_post_recyclerview_id);
        floatingActionButton=findViewById(R.id.book_post_float_id);

        subcatarecycler=findViewById(R.id.book_sub_recycler_id);
        subcatafloat=findViewById(R.id.book_sub_cata_id);



        retriveBookDetailsData();
        retrieveSubCata();


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


        subcatafloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogue();
            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputBookFromAdmin();
            }
        });
    }


    private void retrieveSubCata() {
        sublist = new ArrayList<>();
        subCataAdapter = new SubCataAdapter(BookPostActivity.this, sublist);

        // Set the RecyclerView to display items horizontally
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.HORIZONTAL, false);
        subcatarecycler.setLayoutManager(gridLayoutManager);
        subcatarecycler.setAdapter(subCataAdapter);

        FirebaseDatabase.getInstance().getReference("SubCata").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sublist.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                         subCataModel = dataSnapshot.getValue(SubCataModel.class);
                        if (subCataModel != null && subCataModel.getK() != null && subCataModel.getK().equals(bCataKey)) {
                            sublist.add(0, subCataModel);
                        }                    }
                }
                subCataAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error here if needed
            }
        });
    }

    private void openDialogue() {
        AlertDialog.Builder builder=new AlertDialog.Builder(BookPostActivity.this);
        final View view=getLayoutInflater().inflate(R.layout.sub_cata_design,null);

        EditText aName=view.findViewById(R.id.enter_sub_cata_title);



        TextView addAuthor=view.findViewById(R.id.sub_cata_ads_submit_id);
        TextView cancel=view.findViewById(R.id.sub_cata_ads_cancel);



        builder.setView(view);
        AlertDialog alertDialog= builder.create();
        Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.back);
        alertDialog.getWindow().setBackgroundDrawable(drawable);
        alertDialog.show();
        alertDialog.setCancelable(false);
        addAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(aName.getText().toString())  ){

                    String entryKey = FirebaseDatabase.getInstance().getReference("SubCata").push().getKey();

                    try {
                        FirebaseDatabase.getInstance().getReference("SubCata").child(entryKey).child("n").setValue(aName.getText().toString());
                        FirebaseDatabase.getInstance().getReference("SubCata").child(entryKey).child("k").setValue(bCataKey);
                        FirebaseDatabase.getInstance().getReference("SubCata").child(entryKey).child("mk").setValue(entryKey);
                        Toast.makeText(BookPostActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }catch (Exception e){
                        Toast.makeText(BookPostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }






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


    private void searchList(String newText) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<BookPostModel> fList = new ArrayList<>();
                for (BookPostModel obj : bookList) {
                    if (obj.getBookName().toLowerCase().replaceAll("\\s","").contains(newText.toLowerCase().trim().replaceAll("\\s",""))) {
                        fList.add(obj);
                    }
                }

                // Update the UI on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bookPostAdapter.searchListsFunc((ArrayList<BookPostModel>) fList);
                        bookPostAdapter.notifyDataSetChanged();

                    }
                });
            }
        }).start();
    }


    private void retriveBookDetailsData() {
        bookList=new ArrayList<>();
        bookPostAdapter=new BookPostAdapter(BookPostActivity.this,bookList,bCataNAme,bCataKeyWord);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        recyclerView.setAdapter(bookPostAdapter);

        FirebaseDatabase.getInstance().getReference("Book Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookList.clear();
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        bookPostModel = dataSnapshot.getValue(BookPostModel.class);
                        if(bookPostModel != null && bookPostModel.getBookCategoryKey().equals(bCataKey)){
                            bookList.add(0,bookPostModel);

                        }

                    }

                }
                bookPostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void inputBookFromAdmin() {
        AlertDialog.Builder builder=new AlertDialog.Builder(BookPostActivity.this);
        final View view=getLayoutInflater().inflate(R.layout.book_post_design,null);


        EditText bookName=view.findViewById(R.id.ed_book_post_name_id);
        EditText bookImageLink=view.findViewById(R.id.ed_book_post_image_link_id);
        EditText bookDriveUrl=view.findViewById(R.id.ed_book_post_drive_url_id);
        EditText bookMbSize=view.findViewById(R.id.mb_size_id);
        EditText bookPageNo=view.findViewById(R.id.page_no_id);

        //key_word_id_book
        EditText bookKeyWord=view.findViewById(R.id.key_word_id_book);


        RadioGroup bookCategoryType=view.findViewById(R.id.book_type_rg_id);
        RadioGroup bookPriceType=view.findViewById(R.id.price_type_rg_id);
        RadioGroup bookLanguageType=view.findViewById(R.id.book_language_rg);


        bookCategoryType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = view.findViewById(checkedId);
                bookCategoryTypeS = checkedRadioButton.getText().toString();

            }
        });
        bookPriceType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = view.findViewById(checkedId);
                bookPriceTypeS = checkedRadioButton.getText().toString();

            }
        });
        bookLanguageType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = view.findViewById(checkedId);
                bookLanguageTypeS = checkedRadioButton.getText().toString();

            }
        });






        TextView addBook=view.findViewById(R.id.book_post_add_id);
        TextView cancel=view.findViewById(R.id.book_post_cancel_settings_id);



        builder.setView(view);
        AlertDialog alertDialog= builder.create();
        Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.back);
        alertDialog.getWindow().setBackgroundDrawable(drawable);
        alertDialog.show();
        alertDialog.setCancelable(false);
        Drawable drawables = ContextCompat.getDrawable(getApplicationContext(), R.drawable.alert_back);
        alertDialog.getWindow().setBackgroundDrawable(drawables);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(bookName.getText().toString()) && !TextUtils.isEmpty(bookImageLink.getText().toString()) && !TextUtils.isEmpty(bookDriveUrl.getText().toString()
                ) && !TextUtils.isEmpty(bookPageNo.getText().toString()) && !TextUtils.isEmpty(bookMbSize.getText().toString())   ){

                    uploadBooKDetailsInFirebase(bookName,bookImageLink,bookDriveUrl,bookMbSize,bookPageNo,bookKeyWord,alertDialog);



                }

            }
        });
    }




    private void uploadBooKDetailsInFirebase(EditText bookName, EditText bookImageLink, EditText bookDriveUrl,EditText bookMbSize,EditText bookPageNo,EditText bookKeyWord ,AlertDialog alertDialog) {
        String entryKey = FirebaseDatabase.getInstance().getReference("Book Details").push().getKey();

        Calendar calendar = Calendar.getInstance();
        Date times=calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy,EEEE", Locale.getDefault());
        String date = sdf.format(times);



        Map<String,Object> map=new HashMap<>();

        map.put("bookName",bookName.getText().toString());//60
        map.put("bookImageLink",bookImageLink.getText().toString());//200
        map.put("bookKey",entryKey);//20
        map.put("bookDriveurl",bookDriveUrl.getText().toString());//50
        map.put("bookCategoryKey",bCataKey);//25
        map.put("bookPostDate",date);//20
        map.put("bookCategoryName",bCataNAme);//50

        map.put("bookMbSize",bookMbSize.getText().toString());//15
        map.put("bookPageNo",bookPageNo.getText().toString());//15
        map.put("bookCategoryType",bookCategoryTypeS);//15
        map.put("bookPriceType",bookPriceTypeS);//15
        map.put("bookLanguageType",bookLanguageTypeS);//15

        map.put("bookKeyWord",bookKeyWord.getText().toString());//100

        if(bookImageLink.getText().toString().length()<=200 && bookKeyWord.getText().toString().length()<=100 ){
            FirebaseDatabase.getInstance().getReference("Book Details").child(entryKey).setValue(map).
                    addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(BookPostActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                    });

        }
        else {
            Toast.makeText(this, "Reduce Size", Toast.LENGTH_SHORT).show();
        }





    }

}