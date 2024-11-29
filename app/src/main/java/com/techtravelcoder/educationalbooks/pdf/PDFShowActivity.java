package com.techtravelcoder.educationalbooks.pdf;

import android.Manifest;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.educationalbooks.R;
import com.techtravelcoder.educationalbooks.activity.AuthorPostActivity;
import com.techtravelcoder.educationalbooks.activity.BookPostActivity;
import com.techtravelcoder.educationalbooks.activity.GoogleSignInHelper;
import com.techtravelcoder.educationalbooks.ads.ADSSetUp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class PDFShowActivity extends AppCompatActivity {

    private PDFView pdfView;
    private DatabaseHelper databaseHelper;
    private Toolbar title;

    private String fileName;
    private static final String PREFS_NAME = "PDFPrefs";
    private int currentPage = 0;
    TextView cancel;

    private ImageView settings, withoutBookMark, share;
    private String theme, mode, fileUrl, fKey, bName, iUrl;
    private LinearLayout pdfsettings, noInternet;
    private WebView webView;
    private int key;
    private LinearLayout s_ll, adContainer;
    private AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfshow);

        pdfView = findViewById(R.id.pdfView1);
        webView = findViewById(R.id.pdf_webview_id);
        title = findViewById(R.id.custom_toolbar);
        setSupportActionBar(title);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        settings = findViewById(R.id.settings_button_id);
        withoutBookMark = findViewById(R.id.without_bookmark_button_id);
        share = findViewById(R.id.share_button_id);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);

        pdfsettings = findViewById(R.id.all_pdf_settings_ll);
        noInternet = findViewById(R.id.no_internet_connect_ll);
        s_ll = findViewById(R.id.settings_ll);

        title.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ADSSetUp.adsType2(PDFShowActivity.this);
                onBackPressed();
            }
        });

        adContainer = findViewById(R.id.pdf_ads);
        loadsBannerAds();


        databaseHelper = new DatabaseHelper(PDFShowActivity.this);


        String fUrl = getIntent().getStringExtra("fUrl");
        fKey = getIntent().getStringExtra("fName");
        iUrl = getIntent().getStringExtra("iUrl");
        bName = getIntent().getStringExtra("bookName");
        key = getIntent().getIntExtra("check", 0);


        fileUrl = "https://drive.google.com/uc?export=download&id=" + fUrl;
        fileName = fKey + fUrl;


        if (key == 2) {
            toolbarTitle.setText("" + bName);//toolbar
            s_ll.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            webView.setWebViewClient(new WebViewClient()); // Prevent opening the PDF in a browser
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true); // Enable JavaScript
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT); // Enable caching
            webSettings.setLoadWithOverviewMode(true); // Optimize content to fit WebView
            webSettings.setUseWideViewPort(true); // Allow web pages to be scalable
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

            if (!databaseHelper.fileExists(fileUrl)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String timestamp = dateFormat.format(new Date());
                databaseHelper.addFile(fileName, fileUrl, "", fKey, timestamp, "false");

            }


            webView.setDownloadListener(new DownloadListener() {
                @Override
                public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                    Toast.makeText(PDFShowActivity.this, "Download Started!!!", Toast.LENGTH_LONG).show();
                    FileDownloader.downloadFile(PDFShowActivity.this, url, fileName, new FileDownloader.DownloadListener() {
                        @Override
                        public void onDownloadComplete(File file) {
                            String stat = databaseHelper.getUpdateStatus(fileName);
                            String time = databaseHelper.getUpdateTime(fileName);


                            databaseHelper.updateFile(fileName, fileUrl, file.getAbsolutePath(), fKey, time, stat);
                            Toast.makeText(PDFShowActivity.this, "Download Complete Successfully", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onDownloadFailed(Exception e) {
                            Toast.makeText(getApplicationContext(), "Internet Problem", Toast.LENGTH_LONG).show();
                        }
                    });


                }
            });
            String googleDriveViewerUrl = "https://drive.google.com/file/d/" + fUrl + "/view?usp=sharing";
            webView.getSettings().setJavaScriptEnabled(true);
            if (isInternetAvailable()) {
                webView.loadUrl(googleDriveViewerUrl);
            } else {
                // Show a toast if there is no internet connection
                Toast.makeText(PDFShowActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        } else {
            pdfView.setVisibility(View.VISIBLE);
            s_ll.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            toolbarTitle.setText("" + bName);//toolbar


            if (databaseHelper.fileExists(fileUrl)) {
                // Retrieving values from SharedPreferences
                SharedPreferences sharedPreferences = PDFShowActivity.this.getSharedPreferences("Stgns", Context.MODE_PRIVATE);
                String v1 = sharedPreferences.getString("v1", "0");
                String v2 = sharedPreferences.getString("v2", "0");


                if (v1.equals("0") && v2.equals("0")) {
                    mode = "vertical";
                    theme = "light";
                    loadPdf(databaseHelper.getFilePath(fileUrl), mode, theme);

                } else {
                    mode = v1;
                    theme = v2;
                    loadPdf(databaseHelper.getFilePath(fileUrl), mode, theme);
                }

            } else {

                Toast.makeText(this, "Please Download this Book From Online First !!!!", Toast.LENGTH_SHORT).show();

            }

        }


        String stat = databaseHelper.getUpdateStatus(fileName);
        if (stat.equals("true")) {
            withoutBookMark.setBackgroundResource(R.drawable.baseline_bookmark_added_24);

        } else {
            withoutBookMark.setBackgroundResource(R.drawable.baseline_bookmark_border_24);


        }

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsHandle();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String imageUrl = iUrl;
                try {
                    if (imageUrl != null) {
                        // Create the text part
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("image/*"); // MIME type for image sharing
                        String subject = "✔✔ Book Name: " + bName + "\n\n" +
                                "✔✔Unlimited Pdf Book House - UPBH  Provides A Huge Collection of Books for University Students , College Students , Learners and Book Lovers ." + "\n\n1. This App Provides daily Books, Also you can request to Upload your desire books to Admin" + "\n\n2.We Have A millions Of Books Collection on Science" +
                                "Arts , Commerce ,Programming , Technology , Economics , Business , Motivation ,Self Study ,Engineering ,Medical all types of Books...and Its Totally Free";
                        String appLink = "\nDownload And Read Your Desire Books: \n\n" + "\uD83D\uDC49 \uD83D\uDC49" + "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();
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
                                            Toast.makeText(PDFShowActivity.this, "Failed to save image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onLoadCleared(Drawable placeholder) {
                                        // Optional: Handle placeholder if needed
                                    }

                                    @Override
                                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                        Toast.makeText(PDFShowActivity.this, "Failed to load image", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    } else {
                        Toast.makeText(PDFShowActivity.this, "Internet Connection Loss", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(PDFShowActivity.this, "Unable to Share!!! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        withoutBookMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //bookBookMarkHandle();
                String stat = databaseHelper.getUpdateStatus(fileName);
                if (stat.equals("true")) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String timestamp = dateFormat.format(new Date());
                    databaseHelper.updateStatusAndTime(fileName, "false", timestamp);
                    withoutBookMark.setBackgroundResource(R.drawable.baseline_bookmark_border_24);
                    Toast.makeText(PDFShowActivity.this, "Successfully removed", Toast.LENGTH_SHORT).show();


                } else {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String timestamp = dateFormat.format(new Date());
                    databaseHelper.updateStatusAndTime(fileName, "true", timestamp);
                    withoutBookMark.setBackgroundResource(R.drawable.baseline_bookmark_added_24);
                    Toast.makeText(PDFShowActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();


                }
            }
        });


    }







    private void pdfSettingsChecker() {

        if (isActivityRunning()) {
            dialogue();
        }
    }

    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void dialogue(){


        AlertDialog.Builder builder = new AlertDialog.Builder(PDFShowActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.pdf_settings, null);
        RadioGroup viewhv = view.findViewById(R.id.group_radio_view);
        RadioGroup modess = view.findViewById(R.id.radio_group_mode);

        RadioButton horizontal = view.findViewById(R.id.horizontal_id);
        RadioButton vertical = view.findViewById(R.id.vertical_id);
        RadioButton light = view.findViewById(R.id.light_id);
        RadioButton night = view.findViewById(R.id.night_id);



        SharedPreferences sharedPreferences1 = PDFShowActivity.this.getSharedPreferences("Stgns", Context.MODE_PRIVATE);
        String v11 = sharedPreferences1.getString("v1", "0");
        String v22 = sharedPreferences1.getString("v2", "0");

        if(v11.equals("horizontal")){
            horizontal.setChecked(true);
        }else {
            vertical.setChecked(true);
        }

        if(v22.equals("light")){
            light.setChecked(true);
        }else {
            night.setChecked(true);
        }


        if(Build.VERSION.SDK_INT >= 21)
        {
            ColorStateList colorStateList = new ColorStateList(
                    new int[][]
                            {
                                    new int[]{-android.R.attr.state_enabled}, // Disabled
                                    new int[]{android.R.attr.state_enabled}   // Enabled
                            },
                    new int[]
                            {
                                    Color.MAGENTA, // disabled
                                    Color.MAGENTA   // enabled
                            }
            );

            horizontal.setButtonTintList(colorStateList);
            horizontal.invalidate();
            vertical.setButtonTintList(colorStateList);
            vertical.invalidate();
            light.setButtonTintList(colorStateList);
            light.invalidate();
            night.setButtonTintList(colorStateList);
            night.invalidate();
        }


        TextView applySettings = view.findViewById(R.id.apply_settings_id);
        cancel = view.findViewById(R.id.cancel_settings_id);


        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.alert_back);
        alertDialog.getWindow().setBackgroundDrawable(drawable);
        alertDialog.show();


        applySettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                int selectedViewModeId = viewhv.getCheckedRadioButtonId();
                int selectedThemeModeId = modess.getCheckedRadioButtonId();

                String viewMode = "";
                String themeMode = "";

                if (selectedViewModeId == horizontal.getId()) {
                    viewMode = "horizontal";
                }
                else if (selectedViewModeId == vertical.getId()) {
                    viewMode = "vertical";
                }

                if (selectedThemeModeId == light.getId()) {
                    themeMode = "light";
                }
                else if (selectedThemeModeId == night.getId()) {
                    themeMode = "night";
                }
                mode=viewMode;
                theme=themeMode;

                SharedPreferences sharedPreferences = PDFShowActivity.this.getSharedPreferences("Stgns", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("v1", mode);
                editor.putString("v2", theme);
                editor.apply();

                loadPdf(databaseHelper.getFilePath(fileUrl),mode,theme);
                alertDialog.dismiss();



            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void settingsHandle() {
        pdfSettingsChecker();

    }


    private void goToAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }



    private void savePageNumber(String filename, int pageNumber) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(filename, pageNumber);
        editor.apply();
    }

    private int getSavedPageNumber(String filename) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedPage = prefs.getInt(filename, 0);
        return savedPage;
    }

    @Override
    protected void onPause() {
        super.onPause();
        savePageNumber(fileName, currentPage);
    }

    private void loadPdf(String filePath,String mode ,String theme) {


        if (filePath != null) {
            File file = new File(filePath);

            if (file.exists()) {

                if(theme.equals("light") && mode.equals("horizontal")){
                    pdfView.fromFile(file)
                            .defaultPage(getSavedPageNumber(fileName))
                            .swipeHorizontal(true) // Enable horizontal swiping
                            .enableDoubletap(true)
                            .onLoad(nbPages -> {})
                            .onPageChange((page, pageCount) -> {})
                            .onError(t -> {})
                            .onPageError((page, t) -> {})
                            .enableAnnotationRendering(false)
                            .password(null)
                            .scrollHandle(null)
                            .onPageChange(new OnPageChangeListener() {
                                @Override
                                public void onPageChanged(int page, int pageCount) {
                                    currentPage = page;
                                    savePageNumber(fileName,page);
                                }
                            })

                            .enableAntialiasing(true)
                            .spacing(0)
                            .scrollHandle(new DefaultScrollHandle(this))
                            .fitEachPage(true) // fit each page to the view, else smaller pages are scaled relative to largest page.
                            .pageSnap(true) // snap pages to screen boundaries
                            .pageFling(true) // make a fling change only a single page like ViewPager
                            .nightMode(false)
                            .fitEachPage(true)
                            .load();

                }
                if(theme.equals("light") && mode.equals("vertical")){
                    pdfView.fromFile(file)
                            .defaultPage(getSavedPageNumber(fileName))
                            .swipeHorizontal(false) // Enable horizontal swiping
                            .enableDoubletap(true)
                            .onLoad(nbPages -> {})
                            .onPageChange((page, pageCount) -> {})
                            .onError(t -> {})
                            .onPageError((page, t) -> {})
                            .enableAnnotationRendering(false)
                            .password(null)
                            .scrollHandle(null)
                            .enableAntialiasing(true)
                            .spacing(0)
                            .onPageChange(new OnPageChangeListener() {
                                @Override
                                public void onPageChanged(int page, int pageCount) {
                                    currentPage = page;
                                    savePageNumber(fileName,page);
                                }
                            })
                            .scrollHandle(new DefaultScrollHandle(this))
                            .fitEachPage(true) // fit each page to the view, else smaller pages are scaled relative to largest page.
                            .nightMode(false)
                            .fitEachPage(true)
                            .load();

                }
                if(theme.equals("night") && mode.equals("vertical")){
                    pdfView.fromFile(file)
                            .defaultPage(getSavedPageNumber(fileName))

                            .swipeHorizontal(false) // Enable horizontal swiping
                            .enableDoubletap(true)
                            .onLoad(nbPages -> {})
                            .onPageChange((page, pageCount) -> {})
                            .onError(t -> {})
                            .onPageError((page, t) -> {})
                            .enableAnnotationRendering(false)
                            .password(null)
                            .onPageChange(new OnPageChangeListener() {
                                @Override
                                public void onPageChanged(int page, int pageCount) {
                                    currentPage = page;
                                    savePageNumber(fileName,page);
                                }
                            })
                            .scrollHandle(null)
                            .enableAntialiasing(true)
                            .spacing(0)
                            .scrollHandle(new DefaultScrollHandle(this))
                            .fitEachPage(true) // fit each page to the view, else smaller pages are scaled relative to largest page.
                            .nightMode(true)
                            .load();

                }
                if(theme.equals("night") && mode.equals("horizontal")){
                    pdfView.fromFile(file)
                            .defaultPage(getSavedPageNumber(fileName))

                            .swipeHorizontal(true) // Enable horizontal swiping
                            .enableDoubletap(true)
                            .onLoad(nbPages -> {})
                            .onPageChange((page, pageCount) -> {})
                            .onError(t -> {})
                            .onPageError((page, t) -> {})
                            .enableAnnotationRendering(false)
                            .password(null)
                            .onPageChange(new OnPageChangeListener() {
                                @Override
                                public void onPageChanged(int page, int pageCount) {
                                    currentPage = page;
                                    savePageNumber(fileName,page);
                                }
                            })
                            .scrollHandle(null)
                            .enableAntialiasing(true)
                            .spacing(0)
                            .scrollHandle(new DefaultScrollHandle(this))
                            .fitEachPage(true) // fit each page to the view, else smaller pages are scaled relative to largest page.
                            .pageSnap(true) // snap pages to screen boundaries
                            .pageFling(true) // make a fling change only a single page like ViewPager
                            .nightMode(true)
                            .load();

                }


            }
            else {
                Toast.makeText(this, "You Delete local storage . Please Again Download Books from Online", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "File path not found in database", Toast.LENGTH_SHORT).show();
        }
    }



    public boolean isActivityRunning() {
        return !isFinishing() && !isDestroyed();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();  // WebView navigates back
        } else {
            ADSSetUp.adsType2(PDFShowActivity.this);
            super.onBackPressed();  // Default back action (e.g., closing the activity)
        }
    }

    private void loadsBannerAds() {

        FirebaseDatabase.getInstance().getReference("Ads Control").child("faStatus")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            String ans = (String) snapshot.getValue();
                            if (ans.equals("on")) {

                                adView = new AdView(PDFShowActivity.this, "1051671613095414_1057857532476822", AdSize.BANNER_HEIGHT_50);

                                adContainer.addView(adView);
                                adView.loadAd();
                            }else {
                                adContainer.setVisibility(View.GONE);
                            }
                        }else {
                            adContainer.setVisibility(View.GONE);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        adContainer.setVisibility(View.GONE);

                    }
                });



    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }


}
