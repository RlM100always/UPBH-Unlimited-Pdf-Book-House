package com.techtravelcoder.educationalbooks.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.techtravelcoder.educationalbooks.R;
import com.techtravelcoder.educationalbooks.fragmentactivity.BlankCategoryFragment;
import com.techtravelcoder.educationalbooks.fragmentactivity.BookMarkFragment;
import com.techtravelcoder.educationalbooks.fragmentactivity.NewBooksFragment;
import com.techtravelcoder.educationalbooks.pdf.DatabaseHelper;
import com.techtravelcoder.educationalbooks.pdf.FileDelete;
import com.techtravelcoder.educationalbooks.pdf.PDFShowActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private  ActionBarDrawerToggle actionBarDrawerToggle;
    private LinearLayout layout;
    private GoogleSignInHelper mGoogleSignInHelper;
    private DatabaseReference mbase;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView=findViewById(R.id.bottom_nav_id);

        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        drawerLayout = findViewById(R.id.drawer_id);
        navigationView=findViewById(R.id.navigation_view);
        layout=findViewById(R.id.google_login);
        databaseHelper=new DatabaseHelper(MainActivity.this);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mbase = FirebaseDatabase.getInstance().getReference("Book Details");
        mbase.keepSynced(true);

        if(FirebaseAuth.getInstance().getUid()!=null){
            layout.setVisibility(View.GONE);

        }
        else {
            layout.setVisibility(View.VISIBLE);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mGoogleSignInHelper = new GoogleSignInHelper(MainActivity.this);
                    mGoogleSignInHelper.signIn();


                }
            });

        }



        androidx.appcompat.widget.Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navItemOnClickListener();

        getSupportFragmentManager().beginTransaction().replace(R.id.linear, new NewBooksFragment()).commit();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;

        int itemId = item.getItemId();
        if (itemId == R.id.menu_new_book_id) {
            selectedFragment = new NewBooksFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.linear, selectedFragment).commit();

        }
        if (itemId == R.id.menu_category_id) {
            selectedFragment = new BlankCategoryFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.linear, selectedFragment).commit();
        }
        if (itemId == R.id.menu_bookmark_id) {
            selectedFragment = new BookMarkFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.linear, selectedFragment).commit();
        }



        return true;
    };




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mGoogleSignInHelper != null) {
            mGoogleSignInHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void navItemOnClickListener(){


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {



                if(item.getItemId()==R.id.menu_privacy_policy_id){

                    String websiteUrl = "https://newsfeed420s.blogspot.com/p/privacy-policy.html";
                    Intent openWebsiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
                    startActivity(openWebsiteIntent);

                }
                if(item.getItemId()==R.id.menu_request_id){
                    String[] recipientEmails = {"selfmeteam@gmail.com"};

                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, recipientEmails);
                    startActivity(emailIntent);

                }
                if(item.getItemId()==R.id.menu_rate_id){
                    Uri uri=Uri.parse("https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName());

                    Intent intent= new Intent(Intent.ACTION_VIEW,uri);
                    try {
                        startActivity(intent);
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this, "Unable to ratting !!!"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                if(item.getItemId()==R.id.menu_share_friends_id){
                    String imageUrl="https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEjijXwwcNOVm1Mzsh3fXJLAuuFnsDD0FFFpMVtbLmsXRmr4QZ1_XrBB2Z8p3CNksFMXC9sY7xISD6na-dn7RSDBn6cPzfeeF_9tJHs4hTJA7H6QNEnr6-CI4WP6C_6550N0KR0AUfDvS0W3AfbzMtF9EM3JdKNqBnM1PvTOA7GHw4Ru_rihHG3A1ldotx_f/s16000/Copy%20of%20Copy%20of%20Blue%203D%20Gradient%20Thesis%20Defense%20Presentation.jpg";
                    try {
                        if (imageUrl != null) {
                            // Create the text part
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("image/*"); // MIME type for image sharing
                            String subject =
                                    "✔✔Unlimited Educational Books :  Provide A Huge Collection of Books for University Students , College Students , and Learners ."+"\n\n1. This App Provides daily Books, Also you can request to Upload your desire books to Admin"+"\n\n2.We Have A millions Of Books Collection on Science" +
                                    " , Arts , Commerce ,Programming ,Law,BBA,O Level ,A Level , English Grammar ,IELTS , GRE , Toefl ,Literature Technology ,Physics , Chemistry , Math ,Botany , Zoology ,Microbiology , Biotechnology , Journalism , Genetic Engineering , Medicine  Economics , Business , Motivational ,Story ,Geography , History ,Bangla ,Arabic ,Psychology ,\n" +
                                            "Philosophy , Communication ,Self Study ,All Engineering ,Medical, Bsc and Honors  all types of Books...and Its Totally Free";
                            String appLink = "Download And Read Your Desire Books: \n\n"+"\uD83D\uDC49 \uD83D\uDC49" + "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();
                            String message = subject + "\n\n" + appLink;
                            intent.putExtra(Intent.EXTRA_TEXT, message);

                            // Load image from URL using Glide
                            Glide.with(getApplicationContext())
                                    .asBitmap()
                                    .load(imageUrl)
                                    .into(new CustomTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                                            try {
                                                // Save bitmap to cache directory
                                                File cachePath = new File(getCacheDir(), "images");
                                                cachePath.mkdirs(); // Create directory if not exists
                                                File imageFile = new File(cachePath, "shared_image.png");
                                                FileOutputStream stream = new FileOutputStream(imageFile);
                                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                                stream.close();

                                                // Get content URI using FileProvider
                                                Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.techtravelcoder.educationalbooks.fileprovider", imageFile);
                                                intent.putExtra(Intent.EXTRA_STREAM, contentUri);
                                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Grant temporary read permission

                                                // Show the chooser dialog
                                                startActivity(Intent.createChooser(intent, "Share With"));
                                            } catch (IOException e) {
                                                Toast.makeText(MainActivity.this, "Failed to save image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onLoadCleared(Drawable placeholder) {
                                            // Optional: Handle placeholder if needed
                                        }

                                        @Override
                                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                            Toast.makeText(MainActivity.this, "Failed to load image", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        } else {
                            Toast.makeText(MainActivity.this, "Internet Connection Loss", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Unable to Share!!! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


                if(item.getItemId()==R.id.menu_clear_data_id){

                    FileDelete.deleteAllDownloadedFiles(MainActivity.this, new FileDelete.DeletionListener() {
                        @Override
                        public void onDeletionComplete() {
                            // Delete all records from the database
                            databaseHelper.deleteAllFiles();

                            // Show a toast message
                            Toast.makeText(MainActivity.this, "Successfully Clear Data", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onDeletionFailed(Exception e) {
                            // Show a toast message
                            Toast.makeText(MainActivity.this, "Failed to delete files: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //menu_update_app_id
                if(item.getItemId()==R.id.menu_update_id){
                    Uri uri=Uri.parse("https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName());

                    Intent intent= new Intent(Intent.ACTION_VIEW,uri);
                    try {
                        startActivity(intent);
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this, "Unable to update !!!"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


                return false;
            }
        });

    }





}