package com.techtravelcoder.admin.book;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.FirebaseDatabase;
import com.techtravelcoder.admin.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.AuthorViewHolder> {

    Context context;
    ArrayList<AuthorModel> aulist;

    public AuthorAdapter(Context context, ArrayList<AuthorModel> aulist) {
        this.context = context;
        this.aulist = aulist;
    }

    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.author_design,parent,false);

        return new AuthorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorViewHolder holder, int position) {
        AuthorModel authorModel=aulist.get(position);
        holder.name.setText(authorModel.getName());
        Glide.with(context).load(authorModel.getImage()).into(holder.imageView);

        holder.upadte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                final View view = inflater.inflate(R.layout.author_alert_dialogue_design, null);
                EditText aName=view.findViewById(R.id.author_name_id);
                EditText aImage=view.findViewById(R.id.author_iamge_link);
                EditText aKeyword=view.findViewById(R.id.author_keyword);


                TextView addAuthor=view.findViewById(R.id.auhtor_apply);
                TextView cancel=view.findViewById(R.id.author_cancel);

                aName.setText(authorModel.getName());
                aImage.setText(authorModel.getImage());
                if(authorModel.getKeyword()!=null){
                    aKeyword.setText(authorModel.getKeyword());
                }



                builder.setView(view);
                AlertDialog alertDialog= builder.create();
                Drawable drawable= ContextCompat.getDrawable(context,R.drawable.back);
                alertDialog.getWindow().setBackgroundDrawable(drawable);
                alertDialog.show();
                alertDialog.setCancelable(false);
                addAuthor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(aImage.getText().toString()) && !TextUtils.isEmpty(aImage.getText().toString())) {
                            // Create a map with the updated details
                            Map<String, Object> map = new HashMap<>();
                            map.put("name", aName.getText().toString());
                            map.put("image", aImage.getText().toString());
                            map.put("keyword", aKeyword.getText().toString());
                            map.put("key",authorModel.getKey() );

                            if(aImage.getText().toString().length()<=200){
                                FirebaseDatabase.getInstance().getReference("Author")
                                        .child(authorModel.getKey())
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
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                Drawable drawables = ContextCompat.getDrawable(context, R.drawable.alert_back);
                alertDialog.getWindow().setBackgroundDrawable(drawables);
            }
        });
    }

    @Override
    public int getItemCount() {
        return aulist.size();
    }

    public class AuthorViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imageView;
        TextView name,upadte;
        public AuthorViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.author_iamge);
            name=itemView.findViewById(R.id.author_name);
            upadte=itemView.findViewById(R.id.author_update);

        }
    }
}
