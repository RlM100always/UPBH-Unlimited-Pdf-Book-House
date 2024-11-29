package com.techtravelcoder.educationalbooks.ads;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class ADSSetUp {

    private static InterstitialAd interstitialAd;


    public static void adsType1(Context context){
        interstitialAd = new InterstitialAd(context, "1051671613095414_1051678146428094");



        String userId = FirebaseAuth.getInstance().getUid();

        // Check if the user is signed in
        if (userId == null) {
            return;
        }

        FirebaseDatabase.getInstance().getReference("Ads Control").child("faStatus")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            String ans = (String) snapshot.getValue();
                            if (ans.equals("on")) {
                                Random random = new Random();
                                int num = random.nextInt(6);
                                if (num == 3) {
                                    InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
                                        @Override
                                        public void onInterstitialDisplayed(Ad ad) {

                                        }

                                        @Override
                                        public void onInterstitialDismissed(Ad ad) {

                                        }

                                        @Override
                                        public void onError(Ad ad, AdError adError) {

                                        }

                                        @Override
                                        public void onAdLoaded(Ad ad) {
                                            if(interstitialAd !=null && interstitialAd.isAdLoaded()){
                                                interstitialAd.show();
                                            }
                                        }

                                        @Override
                                        public void onAdClicked(Ad ad) {

                                        }

                                        @Override
                                        public void onLoggingImpression(Ad ad) {

                                        }
                                    };

                                    interstitialAd.loadAd(
                                            interstitialAd.buildLoadAdConfig()
                                                    .withAdListener(interstitialAdListener)
                                                    .build());

                                }

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




    }

    public static void adsType2(Context context){
        interstitialAd = new InterstitialAd(context, "1051671613095414_1051678146428094");

        FirebaseDatabase.getInstance().getReference("Ads Control").child("faStatus")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            String ans = (String) snapshot.getValue();
                            if (ans.equals("on")) {
                                InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
                                    @Override
                                    public void onInterstitialDisplayed(Ad ad) {

                                    }

                                    @Override
                                    public void onInterstitialDismissed(Ad ad) {

                                    }

                                    @Override
                                    public void onError(Ad ad, AdError adError) {

                                    }

                                    @Override
                                    public void onAdLoaded(Ad ad) {
                                        if(interstitialAd !=null && interstitialAd.isAdLoaded()){
                                            interstitialAd.show();
                                        }
                                    }

                                    @Override
                                    public void onAdClicked(Ad ad) {

                                    }

                                    @Override
                                    public void onLoggingImpression(Ad ad) {

                                    }
                                };

                                interstitialAd.loadAd(
                                        interstitialAd.buildLoadAdConfig()
                                                .withAdListener(interstitialAdListener)
                                                .build());
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }



}
