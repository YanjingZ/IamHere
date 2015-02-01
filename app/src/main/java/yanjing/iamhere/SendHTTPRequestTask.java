package yanjing.iamhere;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Yanjing on 9/15/14.
 */
public class SendHTTPRequestTask extends AsyncTask<String, Void, String> {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static final String PREFS_NAME = "MyPrefsFile";

    Context context;
    public SendHTTPRequestTask(Context context) {
        this.context = context.getApplicationContext();
    }
    @Override
    protected String doInBackground(String... urls) {

        // params comes from the execute() call: params[0] is the url.
        try {
            switch (MyActivity.http_request_type) {
                case 0: //get who's in CT
                    return new SendHttpRequest().GETUrl(urls[0]);
                case 1: //post occupancy in cT
                    return new SendHttpRequest().POSTUrl(urls[0]);
                case 2: //leave CT/update CT
                    return new SendHttpRequest().PUTUrl(urls[0]);
                default:
                    return new SendHttpRequest().GETUrl(urls[0]);
            }


        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Userid", MyActivity.Userid);
        editor.commit();

        if (MyActivity.http_request_type==2) {
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            return;
        }
            Intent intent = new Intent(context, DisplayMessageActivity.class);
            intent.putExtra(EXTRA_MESSAGE, result);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);



    }


}
