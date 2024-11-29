package com.techtravelcoder.educationalbooks.ads;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.firebase.database.FirebaseDatabase;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;
import com.onesignal.notifications.INotificationClickEvent;
import com.onesignal.notifications.INotificationClickListener;

public class App extends Application {
    //37742494-b237-4ef9-9061-db4b6a76d8eb

    private static final String ONESIGNAL_APP_ID="37742494-b237-4ef9-9061-db4b6a76d8eb";


    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE);
        // OneSignal Initialization
        OneSignal.initWithContext(this, ONESIGNAL_APP_ID);


    }
}
