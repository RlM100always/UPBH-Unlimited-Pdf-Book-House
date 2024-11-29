package com.techtravelcoder.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.admin.ads.AdsNotifiActivity;
import com.techtravelcoder.admin.book.AuthorActivity;
import com.techtravelcoder.admin.book.BookCategoryActivity;

import java.security.PrivateKey;

public class MainActivity extends AppCompatActivity {

    private TextView textView,ads,author;
    DatabaseReference mbase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=findViewById(R.id.book_id);
        ads=findViewById(R.id.ads_id);
        author=findViewById(R.id.author_id);
        //Book Category

        //important modify

//        FirebaseDatabase.getInstance().getReference("Book Category").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    // Loop through all children of "Book Details"
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        // Get the key of each book
//                        String bookKey = dataSnapshot.getKey();
//
//                        // Set "bookKeyWord" field to an empty string for each child
//                        FirebaseDatabase.getInstance()
//                                .getReference("Book Category")
//                                .child(bookKey)
//                                .child("bCategoryKeyword")
//                                .setValue("")
//                                .addOnCompleteListener(task -> {
//                                    if (task.isSuccessful()) {
//                                        System.out.println("Successfully updated 'bookKeyWord' for book: " + bookKey);
//                                    } else {
//                                        System.err.println("Failed to update 'bookKeyWord' for book: " + bookKey + ". Error: " + task.getException().getMessage());
//                                    }
//                                });
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle the error
//                System.err.println("Error: " + error.getMessage());
//            }
//        });



        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        mbase = FirebaseDatabase.getInstance().getReference("Book Details");
        mbase.keepSynced(true);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, BookCategoryActivity.class);
                startActivity(intent);
            }
        });


        ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, AdsNotifiActivity.class);
                startActivity(intent);
            }
        });
        author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, AuthorActivity.class);
                startActivity(intent);
            }
        });
    }
}