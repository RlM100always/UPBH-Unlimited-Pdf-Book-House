package com.techtravelcoder.admin.book;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.techtravelcoder.admin.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookPostAdapter extends RecyclerView.Adapter<BookPostAdapter.NewViewHolder> {


    Context context;
    ArrayList<BookPostModel>bookList;
    String bCataNAme,bCategoryKeyword;
    String bct,bpt,blt;

    public BookPostAdapter(Context context, ArrayList<BookPostModel> bookList,String bCataNAme,String bCategoryKeyword) {
        this.context = context;
        this.bookList = bookList;
        this.bCataNAme=bCataNAme;
        this.bCategoryKeyword=bCategoryKeyword;

    }

    @NonNull
    @Override
    public NewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.lay_book_post_design,parent,false);
        return new NewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewViewHolder holder, int position) {
        BookPostModel bookPostModel=bookList.get(position);
        holder.bName.setText(bookPostModel.getBookName());
        Glide.with(context).load(bookPostModel.getBookImageLink()).into(holder.bImage);
        ExecutorService executorService = Executors.newSingleThreadExecutor();

// Declare a Handler to update the UI from the background task
        Handler handler = new Handler(Looper.getMainLooper());
        holder.upadte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                                final View view=LayoutInflater.from(context).inflate(R.layout.book_post_design,null);

                                EditText bookName=view.findViewById(R.id.ed_book_post_name_id);
                                EditText bookImageLink=view.findViewById(R.id.ed_book_post_image_link_id);
                                EditText bookDriveUrl=view.findViewById(R.id.ed_book_post_drive_url_id);
                                EditText bookMbSize=view.findViewById(R.id.mb_size_id);
                                EditText bookPageNo=view.findViewById(R.id.page_no_id);
                                EditText bookKeyWord=view.findViewById(R.id.key_word_id_book);


                                RadioGroup bookCategoryType=view.findViewById(R.id.book_type_rg_id);
                                RadioGroup bookPriceType=view.findViewById(R.id.price_type_rg_id);
                                RadioGroup bookLanguageType=view.findViewById(R.id.book_language_rg);
                                RadioButton book,note,guide,free,premium,english,hindi,bengali,arabic,chinese,spanish,french,russian,urdu,german,japanese,indonesian,portuguese;

                                book = view.findViewById(R.id.rb_book_id);
                                note = view.findViewById(R.id.rb_notes_id);
                                guide = view.findViewById(R.id.rb_guide_notes_id);

                                bct=bookPostModel.getBookCategoryType();
                                bpt= bookPostModel.getBookPriceType();
                                blt=bookPostModel.getBookLanguageType();


                                if(bookPostModel.getBookCategoryType()!=null){
                                    if(bookPostModel.getBookCategoryType().equals("Book")){
                                        book.setChecked(true);
                                    }
                                    if(bookPostModel.getBookCategoryType().equals("Notes")){
                                        note.setChecked(true);

                                    }
                                    if(bookPostModel.getBookCategoryType().equals("Guide")){
                                        guide.setChecked(true);

                                    }
                                }


                                free = view.findViewById(R.id.rb_free_id);
                                premium = view.findViewById(R.id.rb_premium_id);

                                if(bookPostModel.getBookPriceType()!=null){
                                    if(bookPostModel.getBookPriceType().equals("Free")){
                                        free.setChecked(true);
                                    }
                                    if(bookPostModel.getBookPriceType().equals("Premium")){
                                        premium.setChecked(true);
                                    }
                                }



                                english = view.findViewById(R.id.english_id);
                                hindi = view.findViewById(R.id.hindi_id);
                                bengali = view.findViewById(R.id.bengali_id);
                                arabic = view.findViewById(R.id.arabic_id);
                                chinese = view.findViewById(R.id.chinese_id);
                                spanish = view.findViewById(R.id.spanish_id);
                                french = view.findViewById(R.id.french_id);
                                russian = view.findViewById(R.id.russian_id);
                                urdu = view.findViewById(R.id.urdu_id);
                                german = view.findViewById(R.id.german_id);
                                japanese = view.findViewById(R.id.japanese_id);
                                indonesian = view.findViewById(R.id.indonesian_id);
                                portuguese = view.findViewById(R.id.portuguese_id);


                                if(bookPostModel.getBookLanguageType()!=null){
                                    if(bookPostModel.getBookLanguageType().equals("English")){
                                        english.setChecked(true);
                                    }
                                    if(bookPostModel.getBookLanguageType().equals("Hindi")){
                                        hindi.setChecked(true);
                                    }
                                    if(bookPostModel.getBookLanguageType().equals("Bengali")){
                                        bengali.setChecked(true);
                                    }
                                    if(bookPostModel.getBookLanguageType().equals("Arabic")){
                                        arabic.setChecked(true);
                                    }
                                    if(bookPostModel.getBookLanguageType().equals("Chinese")){
                                        chinese.setChecked(true);
                                    }
                                    if(bookPostModel.getBookLanguageType().equals("Spanish")){
                                        spanish.setChecked(true);
                                    }
                                    if(bookPostModel.getBookLanguageType().equals("French")){
                                        french.setChecked(true);
                                    }
                                    if(bookPostModel.getBookLanguageType().equals("Russian")){
                                        russian.setChecked(true);
                                    }
                                    if(bookPostModel.getBookLanguageType().equals("Urdu")){
                                        urdu.setChecked(true);
                                    }
                                    if(bookPostModel.getBookLanguageType().equals("German")){
                                        german.setChecked(true);
                                    }
                                    if(bookPostModel.getBookLanguageType().equals("Japanese")){
                                        japanese.setChecked(true);
                                    }
                                    if(bookPostModel.getBookLanguageType().equals("Indonesian")){
                                        indonesian.setChecked(true);
                                    }
                                    if(bookPostModel.getBookLanguageType().equals("Portuguese")){
                                        portuguese.setChecked(true);
                                    }

                                }

                                bookName.setText(bookPostModel.getBookName());
                                bookImageLink.setText(bookPostModel.getBookImageLink());
                                bookDriveUrl.setText(bookPostModel.getBookDriveurl());
                                bookMbSize.setText(bookPostModel.getBookMbSize());
                                bookPageNo.setText(bookPostModel.getBookPageNo());
                                bookKeyWord.setText(bookPostModel.getBookKeyWord());

                                bookCategoryType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                                        RadioButton checkedRadioButton = view.findViewById(checkedId);
                                        bct = checkedRadioButton.getText().toString();

                                    }
                                });
                                bookPriceType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                                        RadioButton checkedRadioButton = view.findViewById(checkedId);
                                        bpt = checkedRadioButton.getText().toString();

                                    }
                                });
                                bookLanguageType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                                        RadioButton checkedRadioButton = view.findViewById(checkedId);
                                        blt = checkedRadioButton.getText().toString();

                                    }
                                });


                                TextView addBook=view.findViewById(R.id.book_post_add_id);
                                TextView cancel=view.findViewById(R.id.book_post_cancel_settings_id);



                                builder.setView(view);
                                AlertDialog alertDialog= builder.create();
                                Drawable drawable= ContextCompat.getDrawable(context,R.drawable.back);
                                alertDialog.getWindow().setBackgroundDrawable(drawable);
                                alertDialog.show();
                                Drawable drawables = ContextCompat.getDrawable(context, R.drawable.alert_back);
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
                                        Toast.makeText(context, ""+bCataNAme, Toast.LENGTH_SHORT).show();
                                        if(!TextUtils.isEmpty(bookName.getText().toString()) && !TextUtils.isEmpty(bookImageLink.getText().toString()) && !TextUtils.isEmpty(bookDriveUrl.getText().toString())){

                                            Calendar calendar = Calendar.getInstance();
                                            Date times=calendar.getTime();
                                            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy,EEEE", Locale.getDefault());
                                            String date = sdf.format(times);
                                            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Book Details").child(bookPostModel.getBookKey());

                                            if(bookImageLink.getText().toString().length()<=200 && bookKeyWord.getText().toString().length()<=100 ){
                                                databaseReference.child("bookName").setValue(bookName.getText().toString());
                                                databaseReference.child("bookImageLink").setValue(bookImageLink.getText().toString());
                                                databaseReference.child("bookDriveurl").setValue(bookDriveUrl.getText().toString());
                                                databaseReference.child("bookCategoryName").setValue(bCataNAme);
                                                databaseReference.child("bookPostDate").setValue(date);
                                                databaseReference.child("bookMbSize").setValue(bookMbSize.getText().toString());
                                                databaseReference.child("bookPageNo").setValue(bookPageNo.getText().toString());
                                                databaseReference.child("bookCategoryType").setValue(bct);
                                                databaseReference.child("bookPriceType").setValue(bpt);
                                                databaseReference.child("bookLanguageType").setValue(blt);
                                                databaseReference.child("bookKeyWord").setValue(bookKeyWord.getText().toString());
                                                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                                                alertDialog.dismiss();


                                            }else {
                                                Toast.makeText(context, "Reduce Size", Toast.LENGTH_SHORT).show();
                                            }












                                        }

                                    }
                                });
                            }
                        });
                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public void searchListsFunc(ArrayList<BookPostModel> fList) {
        bookList=fList;
    }

    public class NewViewHolder extends RecyclerView.ViewHolder {
        private TextView bName;
        private ImageView bImage;
        private TextView upadte;
        public NewViewHolder(@NonNull View itemView) {
            super(itemView);
            bName=itemView.findViewById(R.id.lay_book_post_name_id);
            bImage=itemView.findViewById(R.id.lay_book_post_iamge_id);
            upadte=itemView.findViewById(R.id.book_post_update_id);


        }
    }
}
