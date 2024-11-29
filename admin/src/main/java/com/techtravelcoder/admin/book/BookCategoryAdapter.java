package com.techtravelcoder.admin.book;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.techtravelcoder.admin.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookCategoryAdapter extends RecyclerView.Adapter<BookCategoryAdapter.BookCategoryViewHolder> {

    Context context;
    ArrayList<BookCategoryModel>bookCategoryModelList;

    public BookCategoryAdapter(Context context, ArrayList<BookCategoryModel> bookCategoryModelList) {
        this.context = context;
        this.bookCategoryModelList = bookCategoryModelList;
    }

    @NonNull
    @Override
    public BookCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view =LayoutInflater.from(context).inflate(R.layout.lay_category_design,parent,false);

        return new BookCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookCategoryViewHolder holder, int position) {

        BookCategoryModel bookCategoryModel=bookCategoryModelList.get(position);
        holder.cName.setText(bookCategoryModel.getbCategoryName());
        Glide.with(context).load(bookCategoryModel.getbCategoryImageLink()).into(holder.cImage);
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create AlertDialog builder with the context
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                // Inflate the custom layout view
                LayoutInflater inflater = LayoutInflater.from(context);
                final View view = inflater.inflate(R.layout.book_category_design, null);

                // Find EditTexts and TextView in the custom layout
                EditText name = view.findViewById(R.id.ed_book_category_id);
                EditText link = view.findViewById(R.id.ed_book_category_image_link);
                TextView update = view.findViewById(R.id.book_category_add_id);
                TextView cancel=view.findViewById(R.id.category_cancel_settings_id);
                //key_word_id
                EditText categoryKey=view.findViewById(R.id.key_word_id);



                name.setText(bookCategoryModel.getbCategoryName());
                link.setText(bookCategoryModel.getbCategoryImageLink());
                if(bookCategoryModel.getbCategoryKeyword()!=null){
                    categoryKey.setText(bookCategoryModel.getbCategoryKeyword());
                }


                // Set the custom layout view to the AlertDialog builder
                builder.setView(view);

                // Create and show the AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                Drawable drawables = ContextCompat.getDrawable(context, R.drawable.alert_back);
                alertDialog.getWindow().setBackgroundDrawable(drawables);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });


                // Set OnClickListener for the update TextView
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Validate input fields
                        if (!TextUtils.isEmpty(name.getText().toString()) && !TextUtils.isEmpty(link.getText().toString())) {
                            // Create a map with the updated details
                            Map<String, Object> map = new HashMap<>();
                            map.put("bCategoryName", name.getText().toString());
                            map.put("bCategoryImageLink", link.getText().toString());
                            map.put("bCategoryKey", bookCategoryModel.getbCategoryKey());
                            map.put("bCategoryKeyword", categoryKey.getText().toString());

                            if(link.getText().toString().length()<=200){
                                FirebaseDatabase.getInstance().getReference("Book Category")
                                        .child(bookCategoryModel.getbCategoryKey())
                                        .setValue(map)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(context, "Update Successful", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, "Update Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                // Dismiss the AlertDialog
                                alertDialog.dismiss();
                            }else {
                                Toast.makeText(context, "Reduce size ", Toast.LENGTH_SHORT).show();

                            }


                            // Update the details in Firebase







                        }
                        else {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,BookPostActivity.class);
                intent.putExtra("key",bookCategoryModel.getbCategoryKey());
                intent.putExtra("bCategory",bookCategoryModel.getbCategoryName());
                intent.putExtra("bCategoryKeyword",bookCategoryModel.getbCategoryKeyword());


                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookCategoryModelList.size();
    }

    public void searchListsFunc(ArrayList<BookCategoryModel> fList) {
        bookCategoryModelList=fList;
    }

    public class BookCategoryViewHolder extends RecyclerView.ViewHolder {
        //decalare global attribute

        private ImageView cImage;
        private TextView cName,update;
        public BookCategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            cImage=itemView.findViewById(R.id.lay_category_iamge_id);
            cName=itemView.findViewById(R.id.lay_category_name_id);
            update=itemView.findViewById(R.id.book_category_update_ids);

        }
    }
}
