package com.example.chhua.safetynotification;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextClock;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AlarmPageActivity extends AppCompatActivity {

//    Tools tools = new Tools();
    String targetID = this.getClass().getSimpleName();

    ArrayList<Transaction> warningMessage = new ArrayList<>();
    boolean broadcastAction = true;

    ImageButton menuButton, settingButton, logoutButton;
    Button alarmButton;
    TextClock clock;
    WebView mWebView;

//    Timer timer = null;
//    TimerTask task;

    Context mContext = this;
//    WifiManager wifi;

    private int m_nTime = 0;
    private Handler mHandlerTime = new Handler();
    boolean timerStop = false;


    private WebSocketListenService.ServiceController mControl;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            mControl = (WebSocketListenService.ServiceController)iBinder;
            System.out.println(name.getClassName()+" connected.");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            System.out.println(name.getClassName()+" disconnect.");
//            content.clearComposingText();
        }
    };





    //一開始Activity執行的順序是onCreate->onResume
    //以下的兩個有關於螢幕Rotate時會跑的Activity順序, 是在沒有相關程式處理螢幕Rotate, 以致於系統作了Reset的動作
    //旋轉螢幕其Activity動作的順序是onPause->onSaveInstanceState->onCreate->onRestoreInstanceState->onResume
    //先把螢幕關掉後再把螢幕轉向, 再打開螢幕後, 其Activity動作的順序是onCreate->onRestoreInstanceState>onResume->onPause
    //關掉螢幕其Activity動作的順序是onPause->onSaveInstanceState
    //打開螢幕其Activity動作的順序是onResume

    //若是要偵測螢幕轉動的變化可用以下的步驟:
    //1.在Manifests的想要偵測的Activity加入android:configChanges="screenSize|orientation|screenLayout|navigation"
    //2.Activity的code多一個oveeeide function, 這樣就可以用來攔截螢幕Rotate的變化, 取代上面的旋轉螢幕的Activity動作
    //會有上述的類似重新啟動的行為是因為沒有處理螢幕作Rotate, 以致於出現了reset的動作
        //    @Override中
        //    public void onConfigurationChanged(Configuration newConfig) {
        //        super.onConfigurationChanged(newConfig);
        //        Log.println(Log.INFO, targetID, "System onMonitor Rotate");
        //    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_page);

        Log.println(Log.INFO, targetID, "System onCreate");
        Log.println(Log.DEBUG, targetID, "System onCreate->"+broadcastAction);
//        Log.println(Log.DEBUG, targetID, "System ontimer status ->"+timer);

//        mHandlerTime.postDelayed(timerRun, 1000);

//        ActivityManager mngr = (ActivityManager) getSystemService( ACTIVITY_SERVICE );
//        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);
//
//        String xxx = taskList.get(0).topActivity.getClassName();
//        Log.println(Log.DEBUG, targetID, "System onxxx ->"+xxx);

//        if(taskList.get(0).numActivities == 1 && taskList.get(0).topActivity.getClassName().equals(this.getClass().getName()))
//        {
//            Log.i(targetID, "This is Last activity in the stack");
//        }




//        ComponentName callingApplication = this.getCallingActivity();
//        Log.println(Log.DEBUG, targetID, "callingApplication->"+callingApplication);
//        String lastActivity = "";
//        if(callingApplication != null) {
//            lastActivity = callingApplication.toString();
//            lastActivity = lastActivity.replace("ComponentInfo{com.example.chhua.safetynotification/com.example.chhua.safetynotification.", "")
//                    .replace("}", "");
//        }
//
//        Log.println(Log.DEBUG, targetID, "lastActivity->"+lastActivity);
//        mWebView = (WebView)findViewById(R.id.webview);
//        Intent intent = getIntent();
//
//        boolean notificationFlag = intent.getExtras().getBoolean("notification");
//
//        if(notificationFlag) {
//        }


//                ActivityManager am = (ActivityManager)getBaseContext().getSystemService(ACTIVITY_SERVICE);
//        List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1);
//        String  currentActivity = taskInfo.get(0).topActivity.getClassName();
//        currentActivity = currentActivity.replace("com.example.chhua.safetynotification.","");
//        Log.i( "CURRENT Activity ",  currentActivity);

//        Log.println(Log.DEBUG, targetID, "notificationFlag->"+notificationFlag);
//        if(notificationFlag) {
//            finish();
//        }


        //隱藏ActionBar(App最上面的工具欄)
        android.support.v7.app.ActionBar m_myActionBar = getSupportActionBar();
        m_myActionBar.hide();

        //把螢幕固定為直立式
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        wifi = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);

        alarmButton = (Button) findViewById(R.id.alarmButton);
        alarmButton.setTextColor(mContext.getResources().getColor(R.color.red));
//        logoutButton = (ImageButton) findViewById(R.id.logoutButton);
//        logoutButton.setVisibility(View.VISIBLE);
//        clock = (TextClock) findViewById(R.id.textClock);
//        clock.setVisibility(View.VISIBLE);
//        settingButton = (ImageButton) findViewById(R.id.settingButton);
//        settingButton.setVisibility(View.VISIBLE);
//        menuButton = (ImageButton) findViewById(R.id.imageButton);
//        menuButton.setVisibility(View.VISIBLE);

//        mWebView = (WebView)findViewById(R.id.webview);
////        mWebView.setBackgroundColor(Color.parseColor("#d9f1d8"));
//        mWebView.setWebViewClient(mWebViewClient);
//
//        mWebView.setWebChromeClient(new WebChromeClient());
////
////        mWebView.setWebViewClient(new WebViewClient());
//
////        mWebView.getSettings().setJavaScriptEnabled(true);
//
//
////        mWebView.loadUrl("https://xsize.net/air/enviornment.php?MonitorDEVID=0og29hvpvsm9y7vv");
////        mWebView.loadUrl("https://account.ziellon.net/chatroom.html");
////        mWebView.loadUrl("http://vorder.net/demo/update.php");
////        mWebView.loadUrl("http://tools.ziellon.net/sdbis_demo.html");
//
////        mWebView.loadUrl("http://tools.ziellon.net/sdbis_demo2.html");
////        mWebView.loadUrl("http://g1.misa.com.tw/python/webview.php");
//        mWebView.loadUrl("http://g1.misa.com.tw/python/rfid_warrning_list.php");
//
////        mWebView.loadDataWithBaseURL("same://ur/l/tat/does/not/work", "data", "text/html", "utf-8", null);
//
////        mWebView.addJavascriptInterface(new JavaScriptInterface(this, mWebView), "sdbisAppHandler");
//
//        WebViewJavascriptConsole console = new WebViewJavascriptConsole(this, mWebView);
//        console.register("notice", new WebViewJavascriptConsole.Listener() { public void run(JSONObject data){
//
//            Log.println(Log.DEBUG, targetID, "JSONObject --> "+data);
////            System.out.println("JSONObject --> "+data);
//            try {
//                JSONArray alarmArrary = data.getJSONArray("data");
//                warningMessage = tools.parseJSONArray(alarmArrary);
//
////                JSONObject getData = data.getJSONObject("data");
////                warningMessage = tools.parseJSONObject(getData);
//                String dateTimeString = tools.timeStampMS2dateTimeString(warningMessage.get(0).time_stamp * 1000L);
//
//                Intent intent = new Intent();
//                // Set an action for the Intent
//                intent.setAction("WARNING_MESSAGE_INTENT");
//                String[] warningMsg = {warningMessage.get(0).priority, warningMessage.get(0).type, warningMessage.get(0).subject,
//                        warningMessage.get(0).detail, dateTimeString};
//                intent.putExtra("warningMsg", warningMsg);
//                sendBroadcast(intent);
//                Notification(warningMessage.get(0).detail);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Log.println(Log.ERROR, targetID, "JSONObject error");
//            }
//
//        }});
//
//        mWebView.addJavascriptInterface(console,"sdbisAppHandler");
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            WebView.setWebContentsDebuggingEnabled(true);
//        }
//
//        mWebView.setWebViewClient(new WebViewClient() {
//            //Waiting Webview Page Loading Finish
//            public void onPageFinished(WebView view, String url) {
//                // do your stuff here
//            }
//        });


//        task = new TimerTask() {
//            public void run() {
//                //每次需要执行的代码放到这里面。
//                Log.println(Log.DEBUG, targetID, "System onNetwork Online Status = "+tools.isNetworkOnline(mContext));
//                if(!tools.isNetworkOnline(mContext)) {
//                    Log.println(Log.INFO, targetID, "Wifi is Turn-Off");
//                    wifi.setWifiEnabled(true);
//                    tools.delayMS(2000);
//                }
//
//            }
//        };
//        timer.schedule(task, 1000, 5000);
//        Log.println(Log.DEBUG, targetID, "System ontimer status ->"+timer);

        Log.println(Log.INFO, targetID, "System onCreate End");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.println(Log.INFO, targetID, "System onStart");
        Intent it = new Intent(this,WebSocketListenService.class);
        bindService(it, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        Log.println(Log.INFO, targetID, "System onStop");
        mWebView.getSettings().setJavaScriptEnabled(false);
//        timer.cancel();
//        timer = null;
//        Log.println(Log.DEBUG, targetID, "System ontimer status ->"+timer);
        timerStop = true;
        unbindService(mServiceConnection);
        super.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.println(Log.INFO, targetID, "System onResume");
        Log.println(Log.DEBUG, targetID, "System onResume->"+broadcastAction);

        mWebView = (WebView)findViewById(R.id.webview);
//        mWebView.setBackgroundColor(Color.parseColor("#d9f1d8"));
        mWebView.setWebViewClient(mWebViewClient);

        mWebView.setWebChromeClient(new WebChromeClient());

        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        mWebView.loadUrl("http://g1.misa.com.tw/python/rfid_warrning_list.php");

//        WebViewJavascriptConsole console = new WebViewJavascriptConsole(this, mWebView);
//        console.register("notice", new WebViewJavascriptConsole.Listener() { public void run(JSONObject data){
//
//            Log.println(Log.DEBUG, targetID, "JSONObject --> "+data);
////            System.out.println("JSONObject --> "+data);
//            try {
//                JSONArray alarmArrary = data.getJSONArray("data");
//                warningMessage = tools.parseJSONArray(alarmArrary);
//
////                JSONObject getData = data.getJSONObject("data");
////                warningMessage = tools.parseJSONObject(getData);
//                String dateTimeString = tools.timeStampMS2dateTimeString(warningMessage.get(0).time_stamp * 1000L);
//
//                Intent intent = new Intent();
//                // Set an action for the Intent
//                intent.setAction("WARNING_MESSAGE_INTENT");
//                String[] warningMsg = {warningMessage.get(0).priority, warningMessage.get(0).type, warningMessage.get(0).subject,
//                        warningMessage.get(0).detail, dateTimeString};
//                intent.putExtra("warningMsg", warningMsg);
//                sendBroadcast(intent);
//                Notification(warningMessage.get(0).detail);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Log.println(Log.ERROR, targetID, "JSONObject error");
//            }
//
//        }});
//
////        mWebView.addJavascriptInterface(console,"sdbisAppHandler");
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            WebView.setWebContentsDebuggingEnabled(true);
//        }
//
//        mWebView.setWebViewClient(new WebViewClient() {
//            //Waiting Webview Page Loading Finish
//            public void onPageFinished(WebView view, String url) {
//                // do your stuff here
//            }
//        });

        mWebView.getSettings().setJavaScriptEnabled(true);






//        if(broadcastAction) {
//            mWebView.getSettings().setJavaScriptEnabled(true);
//            mWebView.reload();
//        }


//        if(timer == null) {
//
//            Log.println(Log.INFO, targetID, "Excute New Timer");
//            timer = new Timer();
//            task = new TimerTask() {
//                public void run() {
//                    //每次需要执行的代码放到这里面。
//                    Log.println(Log.DEBUG, targetID, "System onNetwork Online Status = " + tools.isNetworkOnline(mContext));
//                    if (!tools.isNetworkOnline(mContext)) {
//                        Log.println(Log.INFO, targetID, "Wifi is Turn-Off");
//                        wifi.setWifiEnabled(true);
//                        tools.delayMS(2000);
//                    }
//
//                }
//            };
//            timer.schedule(task, 1000, 60000);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //目前onPause是把broadcast & timer
        Log.println(Log.INFO, targetID, "System onPause");
        Log.println(Log.DEBUG, targetID, "System onPause->" + broadcastAction);

//        //偵測螢幕是否為Turn On
//        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        boolean isScreenOn = pm.isScreenOn();
//        Log.println(Log.DEBUG, targetID, "System onisScreenOn ->"+isScreenOn);
//
//        if(broadcastAction && isScreenOn) {
//            mWebView.getSettings().setJavaScriptEnabled(false);
//            mWebView.reload();
//        }
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        Log.println(Log.INFO, targetID, "System onSaveInstanceState");
        Log.println(Log.DEBUG, targetID, "System onSaveInstanceState->" + broadcastAction);
        outState.putBoolean("roadcastAction", broadcastAction);
    }

    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState) {
        Log.println(Log.INFO, targetID, "System onRestoreInstanceState");
        broadcastAction = savedInstanceState.getBoolean("roadcastAction");
        Log.println(Log.DEBUG, targetID, "System onRestoreInstanceState->" + broadcastAction);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.println(Log.INFO, targetID, "System onMonitor Rotate");
    }

    @Override
    public void onBackPressed() {
//        mWebView.getSettings().setJavaScriptEnabled(false);
//        super.onBackPressed();
        // your code.
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);

    }

//    @Override
//    public void onStop(){
//        Log.println(Log.INFO, targetID, "System onStop");
//        mWebView.getSettings().setJavaScriptEnabled(false);
//        timer.cancel();
//        timer = null;
//        Log.println(Log.DEBUG, targetID, "System ontimer status ->"+timer);
//        timerStop = true;
//        super.onStop();
//
//    }

    @Override
    public void onDestroy(){
        Log.println(Log.INFO, targetID, "System onDestory");
        super.onDestroy();

    }

    public void alarmClick(View v) {
        Log.println(Log.INFO, targetID, "alarm Button Click");

        if(broadcastAction) {
            Log.println(Log.INFO, targetID, "停止廣播");
            broadcastAction = false;
            mWebView.getSettings().setJavaScriptEnabled(false);
        }
        else {
            Log.println(Log.INFO, targetID, "開始廣播");
            broadcastAction = true;
            mWebView.getSettings().setJavaScriptEnabled(true);
        }
        mWebView.reload();

    }

    public void logoutClick(View v) {
        Log.println(Log.INFO, targetID, "logout Button Click");
        onBackPressed();
//        mWebView.getSettings().setJavaScriptEnabled(false);
////        mWebView.removeJavascriptInterface("sdbisAppHandler");
//        Intent i = new Intent(this, MainActivity.class);
//        startActivity(i);
//        finish();
//        overridePendingTransition(0, 0);

    }

    public void transportClick(View v) {
        Log.println(Log.INFO, targetID, "transport Button Click");

//        Intent i = new Intent(this, TransportPageActivity.class);
//        startActivity(i);
//        finish();
//        overridePendingTransition(0, 0);

    }

    public void recordClick(View v) {
        Log.println(Log.INFO, targetID, "record Button Click");

//        Intent i = new Intent(this, RecordPageActivity.class);
//        startActivity(i);
//        finish();
//        overridePendingTransition(0, 0);

    }

    public void positionClick(View v) {
        Log.println(Log.INFO, targetID, "position Button Click");

//        Intent i = new Intent(this, PositionPageActivity.class);
//        startActivity(i);
//        finish();
//        overridePendingTransition(0, 0);

    }

    WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, final String url) {

            view.loadUrl(url);
            return true;
        }
    };


    public void Notification(String warningTitle) {
//        // Set Notification Title
//        String strtitle = getString(R.string.notificationtitle);
//        // Set Notification Text
//        String strtext = getString(R.string.notificationtext);


        ActivityManager mActivityManager =(ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);


//        ActivityManager am = (ActivityManager)getBaseContext().getSystemService(ACTIVITY_SERVICE);
//        List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1);
//        String  currentActivity = taskInfo.get(0).topActivity.getClassName();
//        currentActivity = currentActivity.replace("com.example.chhua.safetynotification.","");
//        Log.i( "CURRENT Activity ",  currentActivity);


//        Intent resultIntent = new Intent(this, AlarmPageActivity.class);
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//// Adds the back stack
//        stackBuilder.addParentStack(MainActivity.class);
//// Adds the Intent to the top of the stack
//        stackBuilder.addNextIntent(resultIntent);
//// Gets a PendingIntent containing the entire back stack
//        PendingIntent resultPendingIntent =
//                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);



        // Open NotificationView Class on Notification Click
        Intent intent = new Intent(this, AlarmPageActivity.class);
//        intent.setAction(Intent.ACTION_MAIN);
        //以下的兩個項目可以在執行Notification時, 把之前的Avtivity清除, 再以Notification指定的Intent開啟Activity
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        intent.setAction("finish();");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        // Send data to NotificationView Class
//        intent.putExtra("title", strtitle);
//        intent.putExtra("text", strtext);
//        intent.putExtra("notification", true);
        // Open NotificationView.java Activity
//        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //Create Notification using NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.n_logo)
//                .setColor(getResources().getColor(R.color.red))
                // Set Ticker Message
                .setTicker("Notification Ticker")
                // Set Title
                .setContentTitle("安全通報系統警示....")
                // Set Text
                .setContentText(warningTitle)
                // Add an Action Button below Notification
//				.addAction(R.drawable.n_logo, "Action Button", pIntent)
                // Set PendingIntent into Notification
                .setContentIntent(pIntent)
                // Dismiss Notification
                .setAutoCancel(true)
                //設定Notification發生時會有聲音與振動產生
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            builder.setSmallIcon(R.drawable.logo);
            builder.setColor(getResources().getColor(R.color.red));
        }




        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(0, builder.build());

    }

    private final Runnable timerRun = new Runnable()
    {
        public void run() {
            ++m_nTime; // 經過的秒數 + 1
            if (!timerStop) {
                Log.println(Log.DEBUG, targetID, "system onm_nTime -->" + m_nTime);
                mHandlerTime.postDelayed(this, 1000);
                // 若要取消可以寫一個判斷在這決定是否啟動下一次即可
            }
            else {
                Log.println(Log.INFO, targetID, "system onTimer Stop");
            }

        }
    };





}
