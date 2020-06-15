package com.apjcompany.apjcomputer.proyecto;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.apjcompany.apjcomputer.proyecto.model.MyNotification;
import com.apjcompany.apjcomputer.proyecto.ui.MainActivity;
import com.apjcompany.apjcomputer.proyecto.ui.ViewNotificacion;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;



public class MyServiceFirebase extends FirebaseMessagingService {
    private String TAG="mitoken";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(TAG, "mi token es: " + s);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        // sendRegistrationToServer(token);
    }

    public void onMessageReceived(RemoteMessage remoteMessage) {
        String mensaje,titulo;
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        mensaje=remoteMessage.getData().get("mensaje");
        titulo=remoteMessage.getData().get("titulo");
        if (remoteMessage.getData().size() == 0) {
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            titulo = notification.getTitle();
            mensaje = notification.getBody();
        }
        crearNotificacion(titulo,mensaje);

    }



    private void crearNotificacion(String titulo,String mensaje){
        Intent notifyIntent = new Intent(this, ViewNotificacion.class);
        notifyIntent.putExtra("Titulo",titulo);
        notifyIntent.putExtra("Mensaje",mensaje);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        //Intent intent=new Intent(getApplicationContext(), MainActivity.class);
        //PendingIntent pendingIntent=PendingIntent.
          //      getActivities(this,0, new Intent[]{intent},PendingIntent.FLAG_ONE_SHOT);
        MyNotification mn=new MyNotification(this,MyNotification.CHANNEL_ID_NOTIFICATIONS);
        mn.build(R.drawable.ic_launcher_foreground, titulo, mensaje, pendingIntent);
        mn.addChannel("Notificaciones", NotificationManager.IMPORTANCE_DEFAULT);
        mn.createChannelGroup(MyNotification.CHANNEL_GROUP_GENERAL,
                R.string.default_notification_channel_id);
        mn.show(MyNotification.NOTIFICATION_ID);

    }
}
