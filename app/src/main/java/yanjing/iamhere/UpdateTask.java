package yanjing.iamhere;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Yanjing on 9/15/14.
 */
public class UpdateTask extends AsyncTask<String, Void, String> {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static final String PREFS_NAME = "MyPrefsFile";

    Context context;
    public UpdateTask(Context context) {
        this.context = context.getApplicationContext();
    }
    @Override
    protected String doInBackground(String... urls) {

        // params comes from the execute() call: params[0] is the url.
        try {

             return new SendHttpRequest().PUTUrl(urls[0]);

        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {

    /*    if (MyActivity.http_request_type==2) {
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            return;
        }*/

    }


}
