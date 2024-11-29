package com.techtravelcoder.educationalbooks.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.techtravelcoder.educationalbooks.R;
import com.techtravelcoder.educationalbooks.activity.BookPostActivity;
import com.techtravelcoder.educationalbooks.model.SubCataModel;

import java.util.List;

public class SubCataAdapter extends RecyclerView.Adapter<SubCataAdapter.SubCataViewHolder> {

    private Context context;
    private List<SubCataModel> subCataList;
    private int selectedPosition = -1;


    // Constructor
    public SubCataAdapter(Context context, List<SubCataModel> subCataList, int selectedPosition) {
        this.context = context;
        this.subCataList = subCataList;
        this.selectedPosition = selectedPosition;
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

        if (position == selectedPosition) {
            holder.tvName.setBackgroundResource(R.drawable.button_active);
            holder.tvName.setTextColor(context.getResources().getColor(R.color.colorSelected));
        } else {
            // Reset background and text color for non-selected items
            holder.tvName.setBackgroundResource(R.drawable.edit_text_background); // default background
            holder.tvName.setTextColor(context.getResources().getColor(R.color.icon_color));
        }
        holder.tvName.setText(subCata.getN());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                SharedPreferences sharedPreferences = context.getSharedPreferences("selected_item_pref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("selected_position", selectedPosition);
                editor.apply();

                notifyDataSetChanged();

                Intent intent = new Intent(context, BookPostActivity.class);
                intent.putExtra("subcategory", subCata.getN());
                intent.putExtra("key", subCata.getK());


                // Ensure no animation and clear the current instance
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subCataList.size();
    }
}
