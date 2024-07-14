package com.techtravelcoder.educationalbooks.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.techtravelcoder.educationalbooks.R;
import com.techtravelcoder.educationalbooks.fragmentactivity.BookMarkFragment;
import com.techtravelcoder.educationalbooks.model.BookModel;
import com.techtravelcoder.educationalbooks.pdf.PDFShowActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<BookModel> list;
    private int viewTypeToShow;
    private static final int VIEW_TYPE_1 = 1;
    private static final int VIEW_TYPE_2 = 2;
    private static final int VIEW_TYPE_3 = 3;



    private int checker;

    public BookAdapter(Context context, ArrayList<BookModel> list, int checker) {
        this.context = context;
        this.list = list;
        this.viewTypeToShow = VIEW_TYPE_1;
        this.checker = checker;


    }

    public void setViewTypeToShow(int viewType) {
        this.viewTypeToShow = viewType;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return viewTypeToShow; // Return the current view type to show
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView;

        switch (viewType) {
            case VIEW_TYPE_1:
                itemView = inflater.inflate(R.layout.new_book_design, parent, false);
                return new BookViewHolder1(itemView);
            case VIEW_TYPE_2:
                itemView = inflater.inflate(R.layout.specific_book_design, parent, false);
                return new BookViewHolder2(itemView);
            case VIEW_TYPE_3:
                itemView = inflater.inflate(R.layout.specific_book_design, parent, false);
                return new BookViewHolder3(itemView);
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BookModel book = list.get(position);

        if (holder instanceof BookViewHolder1) {
            // Bind data for ViewHolder1
            ((BookViewHolder1) holder).bookDate.setText("Publish : "+book.getBookPostDate());
            ((BookViewHolder1) holder).bookName.setText(book.getBookName());
            Glide.with(context).load(book.getBookImageLink()).into(((BookViewHolder1) holder).bookImage);

            Calendar calendar = Calendar.getInstance();
            Date times=calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy,EEEE", Locale.getDefault());
            String date = sdf.format(times);

            // Get the previous date
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            Date previousDay = calendar.getTime();
            String previousDate = sdf.format(previousDay);

            //onweak

            // Get the date one week ago
            calendar.add(Calendar.DAY_OF_YEAR, -7);
            Date oneWeekAgo = calendar.getTime();
            String oneWeekAgoDate = sdf.format(oneWeekAgo);

            if (book.getBookPostDate().equals(date)) {
                ((BookViewHolder1) holder).bookDate.setText("Publish : "+"Today");
                ((BookViewHolder1) holder).bookDate.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.c1));
            }
            else if(book.getBookPostDate().equals(previousDate)){
                ((BookViewHolder1) holder).bookDate.setText("Publish : "+"Yesterday");
                ((BookViewHolder1) holder).bookDate.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.c2));
            }
            else if (book.getBookPostDate().equals(oneWeekAgoDate)) {
                ((BookViewHolder1) holder).bookDate.setText("Publish : "+"1 Weak Ago");
                ((BookViewHolder1) holder).bookDate.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.c3));
            }
            else {
                ((BookViewHolder1) holder).bookDate.setText("Publish : "+book.getBookPostDate());
                ((BookViewHolder1) holder).bookDate.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.c4));

            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                      if(FirebaseAuth.getInstance().getUid()!=null){
                          Intent intent=new Intent(context, PDFShowActivity.class);
                          intent.putExtra("fUrl",book.getBookDriveurl());
                          intent.putExtra("fName",book.getBookKey());
                          intent.putExtra("bookName",book.getBookName());
                          intent.putExtra("iUrl",book.getBookImageLink());

                          context.startActivity(intent);
                      }else {
                          Toast.makeText(context, "Please Login First", Toast.LENGTH_SHORT).show();
                      }



                }
            });



        }
        else if (holder instanceof BookViewHolder2) {

            ((BookViewHolder2) holder).bookName1.setText(book.getBookName());
            Glide.with(context).load(book.getBookImageLink()).into(((BookViewHolder2) holder).bookImage1);
            ((BookViewHolder2) holder).read1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(FirebaseAuth.getInstance().getUid()!=null){
                        Intent intent=new Intent(context, PDFShowActivity.class);
                        intent.putExtra("fUrl",book.getBookDriveurl());
                        intent.putExtra("fName",book.getBookKey());
                        intent.putExtra("bookName",book.getBookName());
                        intent.putExtra("iUrl",book.getBookImageLink());

                        context.startActivity(intent);
                    }else {
                        Toast.makeText(context, "Please Login First", Toast.LENGTH_SHORT).show();
                    }



                }
            });


            // Bind data for ViewHolder2
        } else if (holder instanceof BookViewHolder3) {
            // Bind data for ViewHolder3
            ((BookViewHolder3) holder).pdf2.setVisibility(View.GONE);
            ((BookViewHolder3) holder).delete2.setVisibility(View.VISIBLE);
            ((BookViewHolder3) holder).category2.setVisibility(View.VISIBLE);
            ((BookViewHolder3) holder).bookName2.setText(book.getBookName());
            ((BookViewHolder3) holder).category2.setText(book.getBookCategoryName());
            Glide.with(context).load(book.getBookImageLink()).into(((BookViewHolder3) holder).bookImage2);
            ((BookViewHolder3) holder).read2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(FirebaseAuth.getInstance().getUid()!=null){
                        Intent intent=new Intent(context, PDFShowActivity.class);
                        intent.putExtra("fUrl",book.getBookDriveurl());
                        intent.putExtra("fName",book.getBookKey());
                        intent.putExtra("bookName",book.getBookName());
                        intent.putExtra("iUrl",book.getBookImageLink());

                        context.startActivity(intent);
                    }else {
                        Toast.makeText(context, "Please Login First", Toast.LENGTH_SHORT).show();
                    }



                }
            });

            ((BookViewHolder3) holder).delete2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertObj = new AlertDialog.Builder(context);
                    alertObj.setTitle(Html.fromHtml("Confirm Removal...!!"));
                    alertObj.setMessage(Html.fromHtml("Do you want to remove this Book from the BookMark list??"));

                    alertObj.setPositiveButton(Html.fromHtml("<font color='#FF00FF'>✅Yes</font>"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            FirebaseDatabase.getInstance().getReference("Book Details").child(book.getBookKey()).child("bookmark")
                                    .child(FirebaseAuth.getInstance().getUid()).setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context, "Successfully remove from the BookMark List", Toast.LENGTH_SHORT).show();


                                        }
                                    });

                        }
                    });
                    alertObj.setNegativeButton(Html.fromHtml("<font color='#FF00FF'>❌No</font>"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = alertObj.create();
                    dialog.show();

                    Drawable drawable = ContextCompat.getDrawable(context, R.drawable.alert_back);
                    dialog.getWindow().setBackgroundDrawable(drawable);
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void searchLists(ArrayList<BookModel> filteredList) {
        list=filteredList;
    }




    public static class BookViewHolder1 extends RecyclerView.ViewHolder {
        private ImageView bookImage;
        private TextView bookName ;
        private TextView bookDate;
        public BookViewHolder1(@NonNull View itemView) {
            super(itemView);
            // Initialize views for ViewHolder1
            bookImage=itemView.findViewById(R.id.new_book_image_id);
            bookName=itemView.findViewById(R.id.new_book_name_id);
            bookDate=itemView.findViewById(R.id.new_book_date_id);


        }
    }

    public static class BookViewHolder2 extends RecyclerView.ViewHolder {
        private ImageView bookImage1;
        private TextView bookName1,read1 ;
        public BookViewHolder2(@NonNull View itemView) {
            super(itemView);

            bookImage1=itemView.findViewById(R.id.book_image_id);
            bookName1=itemView.findViewById(R.id.book_name_id);
            read1=itemView.findViewById(R.id.read_button_id);


            // Initialize views for ViewHolder2
        }
    }

    public static class BookViewHolder3 extends RecyclerView.ViewHolder {

        private ImageView bookImage2;
        private TextView bookName2,read2,category2,delete2,pdf2 ;
        public BookViewHolder3(@NonNull View itemView) {
            super(itemView);
            // Initialize views for ViewHolder3

            bookImage2=itemView.findViewById(R.id.book_image_id);
            bookName2=itemView.findViewById(R.id.book_name_id);
            read2=itemView.findViewById(R.id.read_button_id);
            category2=itemView.findViewById(R.id.book_category_id);
            delete2=itemView.findViewById(R.id.delete_id);
            pdf2=itemView.findViewById(R.id.pdf_book_id);


        }
    }
}
