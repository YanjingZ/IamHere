package yanjing.iamhere;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;

import java.util.ArrayList;
import java.util.List;


public class MyActivity extends FragmentActivity {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static EditText nameView;
    public static TextView textView;
    public static EditText floorView;
    public static int http_request_type;
    public static String Userid;
    public static alarmManagerSetup alarm;







    private static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;
    private static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
            GEOFENCE_EXPIRATION_IN_HOURS * DateUtils.HOUR_IN_MILLIS;

    // Store the current request
    private GeofenceUtils.REQUEST_TYPE mRequestType;

    // Persistent storage for geofences
    private SimpleGeofenceStore mPrefs;

    // Store a list of geofences to add
    List<Geofence> mCurrentGeofences;

    // Add geofences handler
    private GeofenceRequester mGeofenceRequester;

    // Handle to geofence 1 latitude in the UI
    private String mLatitude1;

    // Handle to geofence 1 longitude in the UI
    private String mLongitude1;

    // Handle to geofence 1 radius in the UI
    private String mRadius1;

    /*
     * Internal lightweight geofence objects for geofence 1 and 2
     */
    private SimpleGeofence mUIGeofence1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        nameView=(EditText)findViewById(R.id.name);
        floorView=(EditText)findViewById(R.id.floor);
        textView = (TextView) findViewById(R.id.result);
        mLatitude1 = "40.7411873";//"40.7411873";//
        mLongitude1 = "-74.0026933";//"-74.0026933";
        mRadius1 ="100" ;

        // Instantiate a new geofence storage area
        mPrefs = new SimpleGeofenceStore(this);

        // Instantiate the current List of geofences
        mCurrentGeofences = new ArrayList<Geofence>();

        // Instantiate a Geofence requester



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /** Called when the user clicks the Send button */
    public void sendMessage(View view) throws InterruptedException {
        // Do something in response to button
        // TODO Auto-generated method stub
        //Record the request as an add.
        mGeofenceRequester = new GeofenceRequester(this);
        mRequestType = GeofenceUtils.REQUEST_TYPE.ADD;

        //Check for Google Play services.
        if (!servicesConnected()) {
            return;
        }

         /*
         * Create a version of geofence 1 that is "flattened" into individual fields. This
         * allows it to be stored in SharedPreferences.
         */
        mUIGeofence1 = new SimpleGeofence(
                "1",
                // Get latitude, longitude, and radius from the UI
                Double.valueOf(mLatitude1),
                Double.valueOf(mLongitude1),
                Float.valueOf(mRadius1),
                // Set the expiration time
                GEOFENCE_EXPIRATION_IN_MILLISECONDS,
                // Only detect entry transitions
                Geofence.GEOFENCE_TRANSITION_ENTER|Geofence.GEOFENCE_TRANSITION_EXIT);

        // Store this flat version in SharedPreferences
        mPrefs.setGeofence("1", mUIGeofence1);
        mCurrentGeofences.add(mUIGeofence1.toGeofence());

        // Start the request. Fail if there's already a request in progress
        try {
            // Try to add geofences
            mGeofenceRequester.addGeofences(mCurrentGeofences);
        } catch (UnsupportedOperationException e) {
            // Notify user that previous request hasn't finished.
            Toast.makeText(this, R.string.add_geofences_already_requested_error,
                    Toast.LENGTH_LONG).show();
        }


    }
    public void sendWhoInCT(View view) {
        // Do something in response to button
        // TODO Auto-generated method stub
        // Gets the URL from the UI's text field.
        http_request_type=0;
        System.out.println("requesttype"+http_request_type);
        String stringUrl = "http://iamhere.smalldata.io/occupancy";
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new SendHTTPRequestTask(getApplicationContext()).execute(stringUrl);
        } else {
            Toast.makeText(this, "No network connection available.",
                    Toast.LENGTH_LONG).show();
        }

    }







    private boolean servicesConnected() {

        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {

            // In debug mode, log the status
            Log.d(GeofenceUtils.APPTAG, getString(R.string.play_services_available));

            // Continue
            return true;

            // Google Play services was not available for some reason
        } else {

            // Display an error dialog
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
            if (dialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(dialog);
                errorFragment.show(getSupportFragmentManager(), GeofenceUtils.APPTAG);
            }
            return false;
        }
    }
    public static class ErrorDialogFragment extends DialogFragment {

        // Global field to contain the error dialog
        private Dialog mDialog;

        /**
         * Default constructor. Sets the dialog field to null
         */
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        /**
         * Set the dialog to display
         *
         * @param dialog An error dialog
         */
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        /*
         * This method must return a Dialog to the DialogFragment.
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

}
