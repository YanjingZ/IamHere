package yanjing.iamhere;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Yanjing on 9/18/14.
 */
public class UpdateEventService extends Service {


        private static final String APP_TAG = "yanjing.iamhere";
        public static final String PREFS_NAME = "MyPrefsFile";


    @Override
        public IBinder onBind(final Intent intent) {
            return null;
        }

        @Override
        public int onStartCommand(final Intent intent, final int flags,
                                  final int startId) {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            String id=settings.getString("Userid", "");
            Log.d(APP_TAG, "event received in service:" + new Date().toString()+"ID:"+id);
            String myurl ="http://iamhere.smalldata.io/occupancy/"+id + "/update";
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                new UpdateTask(getApplicationContext()).execute(myurl);
            } else {
                Toast.makeText(this, "No network connection available.",
                        Toast.LENGTH_LONG).show();
            }

            return Service.START_NOT_STICKY;
        }





}
