package com.example.covislot;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import android.app.NotificationChannel;
import android.app.NotificationManager ;

import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import static androidx.core.content.ContextCompat.getSystemService;

public class CheckSessionWorker extends Worker {
    private static final String CHANNEL_ID = "channel";
    protected Context context;
    SharedPreferences sp;
    ArrayList<session> avlSession;

   public CheckSessionWorker(
           @NonNull Context context,
           @NonNull WorkerParameters params) {
       super(context, params);
   }

   @Override
   public Result doWork() {
       try {
           context = getApplicationContext();
           sp = getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);

           createNotificationChannel();
           NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                   .setSmallIcon(R.drawable.tick_art)
                   .setContentTitle("Slots Available")
                   .setContentText("Quick! Head over to CoWIN to book your slot")
                   .setPriority(NotificationCompat.PRIORITY_HIGH);

           NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);


           String pin= sp.getString("pinCode", null);
           Calendar cDate = Calendar.getInstance();
           cDate.getTime();
           String dt = cDate.get(Calendar.DAY_OF_MONTH) + "-" + cDate.get(Calendar.MONTH) + "-" + cDate.get(Calendar.YEAR);

           avlSession = QueryUtils.checkSlotAvailability(pin, dt);
           if(avlSession != null && !avlSession.isEmpty()) {
               // notificationId is a unique int for each notification that you must define
               notificationManager.notify(1, builder.build());
           }

           avlSession = QueryUtils.checkSlotAvailability(pin, dt);
           if(avlSession != null && !avlSession.isEmpty()) {
               // notificationId is a unique int for each notification that you must define
               notificationManager.notify(1, builder.build());
           }
       } catch (Throwable throwable) {
           return Result.failure();
       }

       return Result.success();
   }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_ID;
            //String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            //channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager;
            notificationManager = (NotificationManager) getSystemService(context, NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
