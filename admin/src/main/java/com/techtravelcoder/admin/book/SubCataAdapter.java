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

import com.google.firebase.database.FirebaseDatabase;
import com.techtravelcoder.admin.R;

import java.util.List;

public class SubCataAdapter extends RecyclerView.Adapter<SubCataAdapter.SubCataViewHolder> {

    private Context context;
    private List<SubCataModel> subCataList;

    // Constructor
    public SubCataAdapter(Context context, List<SubCataModel> subCataList) {
        this.context = context;
        this.subCataList = subCataList;
    }

    public static class SubCataViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        public SubCataViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.sub_cata_name);

        }
    }

    @NonNull
    @Override
    public SubCataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        View view = LayoutInflater.from(context).inflate(R.layout.sub_design, parent, false);
        return new SubCataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCataViewHolder holder, int position) {
        SubCataModel subCata = subCataList.get(position);

        holder.tvName.setText(subCata.getN());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                final View view=LayoutInflater.from(context).inflate(R.layout.sub_cata_design,null);

                EditText aName=view.findViewById(R.id.enter_sub_cata_title);
                aName.setText(subCata.getN());



                TextView addAuthor=view.findViewById(R.id.sub_cata_ads_submit_id);
                TextView cancel=view.findViewById(R.id.sub_cata_ads_cancel);



                builder.setView(view);
                AlertDialog alertDialog= builder.create();
                Drawable drawable= ContextCompat.getDrawable(context,R.drawable.back);
                alertDialog.getWindow().setBackgroundDrawable(drawable);
                alertDialog.show();
                alertDialog.setCancelable(false);
                addAuthor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!TextUtils.isEmpty(aName.getText().toString())  ){

                            String entryKey = subCata.getMk() ;

                            try {
                                if(subCata.getK() != null && subCata.getMk()!=null){
                                    FirebaseDatabase.getInstance().getReference("SubCata").child(entryKey).child("n").setValue(aName.getText().toString());
                                    FirebaseDatabase.getInstance().getReference("SubCata").child(entryKey).child("k").setValue(subCata.getK());
                                    FirebaseDatabase.getInstance().getReference("SubCata").child(entryKey).child("mk").setValue(subCata.getMk());
                                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                }else {
                                    Toast.makeText(context, "Some Key Null", Toast.LENGTH_SHORT).show();
                                }

                            }catch (Exception e){
                                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                Drawable drawables = ContextCompat.getDrawable(context, R.drawable.alert_back);
                alertDialog.getWindow().setBackgroundDrawable(drawables);
            }
        });
    }



    @Override
    public int getItemCount() {
        return subCataList.size();
    }
}
