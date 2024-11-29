package com.techtravelcoder.admin.book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.admin.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AuthorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private SearchView searchView;
    private ArrayList<AuthorModel>authorlist;
    private AuthorAdapter authorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);


        floatingActionButton=findViewById(R.id.author_float_button);
        recyclerView=findViewById(R.id.author_recyclerview);
        retriveAuthorDetailsData();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputAUthorFromAdmin();
            }
        });



    }


    private void retriveAuthorDetailsData() {
        authorlist=new ArrayList<>();
        authorAdapter=new AuthorAdapter(AuthorActivity.this,authorlist);
        recyclerView.setLayoutManager(new GridLayoutManager(AuthorActivity.this,3));
        recyclerView.setAdapter(authorAdapter);
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

                authorAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    private void inputAUthorFromAdmin() {
        AlertDialog.Builder builder=new AlertDialog.Builder(AuthorActivity.this);
        final View view=getLayoutInflater().inflate(R.layout.author_alert_dialogue_design,null);

        EditText aName=view.findViewById(R.id.author_name_id);
        EditText aImage=view.findViewById(R.id.author_iamge_link);
        EditText aKeyword=view.findViewById(R.id.author_keyword);


        TextView addAuthor=view.findViewById(R.id.auhtor_apply);
        TextView cancel=view.findViewById(R.id.author_cancel);



        builder.setView(view);
        AlertDialog alertDialog= builder.create();
        Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.back);
        alertDialog.getWindow().setBackgroundDrawable(drawable);
        alertDialog.show();
        alertDialog.setCancelable(false);
        addAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(aName.getText().toString()) && !TextUtils.isEmpty(aImage.getText().toString()) ){

                    uploadAuthorDetailsInFirebase(aName,aImage,aKeyword,alertDialog);


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
    private void uploadAuthorDetailsInFirebase(EditText bookCategoryName, EditText bookCategoryImageLink,EditText bookCategoryKeyWord ,AlertDialog alertDialog ) {

        String entryKey = FirebaseDatabase.getInstance().getReference("Author").push().getKey();

        Map<String,Object> map=new HashMap<>();

        map.put("name",bookCategoryName.getText().toString());
        map.put("image",bookCategoryImageLink.getText().toString());
        map.put("keyword",bookCategoryKeyWord.getText().toString());
        map.put("key",entryKey);

        if(bookCategoryImageLink.getText().toString().length()<=200){
            Toast.makeText(this, ""+bookCategoryKeyWord.getText().toString(), Toast.LENGTH_SHORT).show();
            FirebaseDatabase.getInstance().getReference("Author").child(entryKey).setValue(map).
                    addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AuthorActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                    });
        }else {
            Toast.makeText(this, "Reduce Size", Toast.LENGTH_SHORT).show();
        }



    }

}