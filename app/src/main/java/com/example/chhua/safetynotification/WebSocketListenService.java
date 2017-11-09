package com.example.chhua.safetynotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by falsewinds on 2017/10/17.
 */

public class WebSocketListenService extends Service implements AsyncHttpClient.WebSocketConnectCallback, WebSocket.StringCallback {
    private Future<WebSocket> fws;
    private NotificationCompat.Builder nb;
    private String WebSocketURL = "ws://tools.ziellon.net:9000";

    private String targetID = this.getClass().getSimpleName();
    Tools tools = new Tools();
    ArrayList<Transaction> warningMessage = new ArrayList<>();

    boolean broadcastAction;

    public class ServiceController extends Binder {
        private ArrayList<String> messages = new ArrayList<String>();
        protected void save(String msg) { messages.add(msg); }
        public String[] load() {
            String[] msgs = new String[messages.size()];
            messages.toArray(msgs);
            messages.clear();
            return msgs;
        }
    }
    private ServiceController ms = new ServiceController();

    @Override
    public void onCreate() {
        super.onCreate();
        //nb = new NotificationCompat.Builder(this,"demoNotification");

        Intent it = new Intent(this,AlarmPageActivity.class);
        it.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, it, PendingIntent.FLAG_UPDATE_CURRENT);

        nb = new NotificationCompat.Builder(this)
    //        .setSmallIcon(android.R.drawable.ic_menu_info_details);
            .setSmallIcon(R.drawable.n_logo)
            .setAutoCancel(true)
            .setContentIntent(pIntent)
            //設定Notification發生時會有聲音與振動產生
            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);

        fws = AsyncHttpClient.getDefaultInstance().websocket(WebSocketURL,null,this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.println(Log.DEBUG, targetID, "System onStartCommand --> ");

        broadcastAction = intent.getBooleanExtra("broadcast",
                true);
        Log.println(Log.DEBUG, targetID, "System onStartCommand --> "+broadcastAction);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        Log.println(Log.DEBUG, targetID, "System onBind --> ");
        broadcastAction = intent.getBooleanExtra("broadcast",
                false);
        Log.println(Log.DEBUG, targetID, "System onBind --> "+broadcastAction);
        return ms;
    }

    @Override
    public void onCompleted(Exception ex, WebSocket webSocket) {
        if (ex != null) {
            ex.printStackTrace();
            return;
        }

        webSocket.setStringCallback(this);
    }

    @Override
    public void onStringAvailable(String s) {

        Log.println(Log.DEBUG, targetID, "JSON String --> "+s);

        try {
            JSONObject obj = new JSONObject(s);
            warningMessage = tools.parseJSONObject(obj);
        } catch(JSONException e) {
            Log.println(Log.INFO, targetID, "Fail to parse JSON.");
            return;
        }

        if(broadcastAction) {
            if (warningMessage.get(0).type.equals("SW")) {
                nb.setWhen(System.currentTimeMillis())
                        .setContentTitle("測試用通知")
                        .setContentText(warningMessage.get(0).detail);

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            builder.setSmallIcon(R.drawable.logo);
                    nb.setColor(getResources().getColor(R.color.red));
                }

                NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                nm.notify(0, nb.build());
            }
        }
        ms.save(s);
    }
}
