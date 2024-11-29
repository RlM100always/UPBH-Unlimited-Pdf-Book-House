package com.techtravelcoder.educationalbooks.connection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.techtravelcoder.educationalbooks.R;

public class NetworkChangeListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
          if(!ConnectionClass.isConnectedToInternet(context)){
              AlertDialog.Builder builder = new AlertDialog.Builder(context);
              View layout= LayoutInflater.from(context).inflate(R.layout.connection_checker_alert_dialogue_design,null);
              builder.setView(layout);

              TextView retry = layout.findViewById(R.id.retry_id);

              AlertDialog alertDialog=builder.create();
              alertDialog.show();
              alertDialog.setCancelable(false);
              alertDialog.getWindow().setGravity(Gravity.CENTER);
              Drawable drawables = ContextCompat.getDrawable(context, R.drawable.alert_back);
              alertDialog.getWindow().setBackgroundDrawable(drawables);

              retry.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      onReceive(context,intent);
                      alertDialog.dismiss();

                  }
              });

          }
    }
}
