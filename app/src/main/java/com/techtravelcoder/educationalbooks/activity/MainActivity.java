package com.techtravelcoder.educationalbooks.activity;

import android.Manifest;
import android.app.UiModeManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.educationalbooks.R;
import com.techtravelcoder.educationalbooks.ads.ADSSetUp;
import com.techtravelcoder.educationalbooks.connection.NetworkChangeListener;
import com.techtravelcoder.educationalbooks.fragmentactivity.AllBookFragment;
import com.techtravelcoder.educationalbooks.fragmentactivity.AuthorFragment;
import com.techtravelcoder.educationalbooks.fragmentactivity.BlankCategoryFragment;
import com.techtravelcoder.educationalbooks.fragmentactivity.BookMarkFragment;
import com.techtravelcoder.educationalbooks.fragmentactivity.NewBooksFragment;
import com.techtravelcoder.educationalbooks.model.BookModel;
import com.techtravelcoder.educationalbooks.pdf.DatabaseHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;

public class MainActivity extends AppCompatActivity   {


    private BottomNavigationView bottomNavigationView;
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private  ActionBarDrawerToggle actionBarDrawerToggle;
    private LinearLayout layout;
    private DatabaseReference mbase;
    private DatabaseHelper databaseHelper;
    private String rgAns="";
    private static final int STORAGE_PERMISSION_CODE = 101;
    FirebaseAnalytics firebaseAnalytics;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Obtain the FirebaseAnalytics instance.
       checkAndRequestStoragePermission();


        firebaseAnalytics = FirebaseAnalytics.getInstance(this);




        bottomNavigationView=findViewById(R.id.bottom_nav_id);

        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        Switch switchMode = findViewById(R.id.switch_mode);

        drawerLayout = findViewById(R.id.drawer_id);

        navigationView=findViewById(R.id.navigation_view);
        databaseHelper=new DatabaseHelper(MainActivity.this);

        mbase = FirebaseDatabase.getInstance().getReference("Book Details");
        mbase.keepSynced(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.back));
        }










        UiModeManager uiModeManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
        int currentMode = uiModeManager.getNightMode();

        if (currentMode == UiModeManager.MODE_NIGHT_YES) {
            // Night mode is active
            switchMode.setChecked(true);

        } else {
            // Light mode is active
            switchMode.setChecked(false);

        }


        switchMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Set dark mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    // Set light mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }

                // Replace fragment
                //getSupportFragmentManager().beginTransaction().replace(R.id.linear, new NewBooksFragment()).commit();

                MenuItem newBooksMenuItem = bottomNavigationView.getMenu().findItem(R.id.menu_new_book_id);

                if (newBooksMenuItem != null) {
                    newBooksMenuItem.setChecked(true); // Set it as active
                } else {
                    Log.d("MenuItemCheck", "Menu item not found in BottomNavigationView");
                }

            }
        });

        androidx.appcompat.widget.Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Unlimited Pdf Book House");

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Drawable boldNavIcon = getResources().getDrawable(R.drawable.threeline, null);
        if (boldNavIcon != null) {
            //boldNavIcon.setColorFilter(getResources().getColor(android.R.color.holo_blue_light), PorterDuff.Mode.SRC_IN);
            toolbar.setNavigationIcon(boldNavIcon);
        }
        navItemOnClickListener();

        getSupportFragmentManager().beginTransaction().replace(R.id.linear, new NewBooksFragment()).commit();



    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;

        int itemId = item.getItemId();
        if (itemId == R.id.menu_new_book_id) {
            selectedFragment = new NewBooksFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.linear, selectedFragment).commit();
            ADSSetUp.adsType1(getApplicationContext());

        }
        if (itemId == R.id.menu_category_id) {
            selectedFragment = new BlankCategoryFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.linear, selectedFragment).commit();
            ADSSetUp.adsType1(getApplicationContext());

        }
        if (itemId == R.id.menu_bookmark_id) {
            selectedFragment = new BookMarkFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.linear, selectedFragment).commit();
            ADSSetUp.adsType1(getApplicationContext());

        }
        if (itemId == R.id.menu_all_book_id) {
            selectedFragment = new AllBookFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.linear, selectedFragment).commit();
            ADSSetUp.adsType1(getApplicationContext());

        }
        if (itemId == R.id.menu_author_id) {
            selectedFragment = new AuthorFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.linear, selectedFragment).commit();
            ADSSetUp.adsType1(getApplicationContext());

        }




        return true;
    };




    public void navItemOnClickListener(){


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId()==R.id.menu_storage_id){

                    Intent intent=new Intent(MainActivity.this,Guideline.class);

                    startActivity(intent);

                }


                if(item.getItemId()==R.id.menu_bug_id){

                    String[] recipientEmails = {"selfmeteam@gmail.com"};

                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, recipientEmails);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Are You Facing Bug When Using This App??");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Please Write Bug Details : ");

                    startActivity(emailIntent);

                }


                if(item.getItemId()==R.id.menu_privacy_policy_id){

                    String websiteUrl = "https://newsfeed420s.blogspot.com/p/privacy-policy-last-updated-4-9-2024.html";
                    Intent openWebsiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
                    startActivity(openWebsiteIntent);

                }
                if(item.getItemId()==R.id.menu_request_id){
                    Intent intent=new Intent(MainActivity.this, SubmissionActivity.class);
                    startActivity(intent);

                }
                if(item.getItemId()==R.id.menu_rate_id){

                    ratingDialogue();

                }
                if(item.getItemId()==R.id.menu_share_friends_id){
                    String imageUrl="https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEjijXwwcNOVm1Mzsh3fXJLAuuFnsDD0FFFpMVtbLmsXRmr4QZ1_XrBB2Z8p3CNksFMXC9sY7xISD6na-dn7RSDBn6cPzfeeF_9tJHs4hTJA7H6QNEnr6-CI4WP6C_6550N0KR0AUfDvS0W3AfbzMtF9EM3JdKNqBnM1PvTOA7GHw4Ru_rihHG3A1ldotx_f/s16000/Copy%20of%20Copy%20of%20Blue%203D%20Gradient%20Thesis%20Defense%20Presentation.jpg";
                    try {
                        if (imageUrl != null) {
                            // Create the text part
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("image/*"); // MIME type for image sharing
                            String subject =
                                    "✔✔Unlimited Educational Book House :  Provides A Huge Collection of Books for University Students , College Students , and Learners ."+"\n\n1. This App Provides daily Books, Also you can request to Upload your desire books to Admin"+"\n\n2.We Have A millions Of Books Collection on Science" +
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

                    requestBookDialogue();

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



    private void requestBookDialogue(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.request_book, null);
        builder.setView(view);
        TextView cancel=view.findViewById(R.id.book_cancel_id);
        TextView yes=view.findViewById(R.id.yes_id);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.alert_back);
        alertDialog.getWindow().setBackgroundDrawable(drawable);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                   databaseHelper.deleteSQLITE();
                    deleteFileAndFolder();
                   Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {

                    Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }

                // Show a toast message
                alertDialog.dismiss();



            }
        });

        alertDialog.show();

    }

    private void deleteFileAndFolder(){
        File dir= new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "UPBH");

        if (dir.exists()) {
            // Call the recursive delete method
            boolean isDeleted = deleteDirectory(dir);
            Toast.makeText(MainActivity.this, "Successfully Clear Data", Toast.LENGTH_SHORT).show();


        }
    }

    public  boolean deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();

            for (File file : files) {
                file.delete();
                deleteDirectory(file);
            }
        }
        return directory.delete();
    }

    private void ratingDialogue(){

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.rate_design, null);

        RatingBar ratingBar=view.findViewById(R.id.ratingBar);
        TextView submit=view.findViewById(R.id.rating_submit_id);
        TextView cancel=view.findViewById(R.id.rating_cancel_id);

        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.alert_back);
        alertDialog.getWindow().setBackgroundDrawable(drawable);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Double dob= Double.valueOf(ratingBar.getRating());
                if(dob == null){
                    Toast.makeText(MainActivity.this, "Please Rating First then Press Submit Button", Toast.LENGTH_SHORT).show();
                }
                if(dob!=null && dob>=4.00){
                    Toast.makeText(MainActivity.this, "Dear Sir/Madam , Respect and many many Thank you for your good rating.", Toast.LENGTH_LONG).show();
                    Uri uri=Uri.parse("https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName());

                    Intent intent= new Intent(Intent.ACTION_VIEW,uri);
                    try {
                        startActivity(intent);
                        Toast.makeText(MainActivity.this, "Dear Sir/Madam , Now Rate it in PlayStore. ", Toast.LENGTH_LONG).show();

                    }
                    catch (Exception e){
                        Toast.makeText(MainActivity.this, "Unable to ratting !!!"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                if(dob!=null && dob<4.00){
                    Toast.makeText(MainActivity.this, "Dear Sir/Madam , We Respect Your Ratting. ", Toast.LENGTH_LONG).show();

                    String[] recipientEmails = {"selfmeteam@gmail.com"};

                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, recipientEmails);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Sir/Madam , Please tell us why you  Ratting less than 4 star");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Dear Sir/Madam, \nWe are giving  our best for Best User Experience .Please Explain us your Bad Ratting Reason . We are trying our best to solve your issue   : ");

                    startActivity(emailIntent);

                }


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }


    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        showExitConfirmationDialog();


    }

    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.exit_confirmation_design, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        TextView  no=view.findViewById(R.id.exit_no_id);
        TextView  yes=view.findViewById(R.id.exit_yes_id);
        TextView  rate=view.findViewById(R.id.exit_rate_us_id);

        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.alert_back);
        alertDialog.getWindow().setBackgroundDrawable(drawable);
        alertDialog.show();

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingDialogue();
            }
        });

    }





    private void checkAndRequestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show dialog explaining why permission is needed
                showPermissionDialog();
            } else {
                // Request permission directly
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        STORAGE_PERMISSION_CODE);
            }
        }
    }

    private void showPermissionDialog() {
       AlertDialog dialog= new AlertDialog.Builder(this)
                .setTitle("Storage Permission Needed")
                .setMessage("This app needs storage access to function correctly. Please grant the permission.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                STORAGE_PERMISSION_CODE);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "Permission Denied. App may not work correctly.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.c1));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.c4));


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                // Permission denied
                boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (!showRationale) {
                    // User selected "Don't ask again" - Show settings dialog
                    showSettingsDialog();
                } else {
                    Toast.makeText(this, "Storage Permission Denied. App may not work correctly.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showSettingsDialog() {
       AlertDialog dialog= new AlertDialog.Builder(this)
                .setTitle("Permission Required")
                .setMessage("Storage permission is required to use this feature. Please enable it in the app settings.")
                .setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Open app settings
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("package:" + getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.c1));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.c4));
    }


    @Override
    protected void onStart() {
        IntentFilter intentFilter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,intentFilter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }



}