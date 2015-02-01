package yanjing.iamhere;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Yanjing on 9/15/14.
 */
public class SendHttpRequest {
    private static final String DEBUG_TAG = "HTTPSExample";
    public String GETUrl(String myurl) throws IOException {
        InputStream is = null;
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();
            JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
            return readMessagesArray(reader);

        } finally {
            if (is != null) {
                is.close();
            }
        }

    }



    public String readMessagesArray(JsonReader reader) throws IOException {
        String names="Name List:\n";
        reader.beginArray();
        while (reader.hasNext()) {
            names=names+readMessage(reader);
        }
        reader.endArray();
        Log.d(DEBUG_TAG, "The names2: " + names);
        return names;
    }
    public String readMessage(JsonReader reader) throws IOException {
        String names="";
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("name")) {
                names = names+reader.nextString()+"\n";
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        Log.d(DEBUG_TAG, "The names1: " + names);
        return names ;
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
// the web page content as a InputStream, which it returns as
// a string.

    public String POSTUrl(String myurl) throws IOException {
        InputStream is = null;
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();
            JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
            return  readOneLog(reader) + "logged in successfully!";

        } finally {
            if (is != null) {
                is.close();
            }
        }

    }



    public String readOneLog(JsonReader reader) throws IOException {
        String names="";
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("name")) {
                names = names+"name:"+reader.nextString()+"\n";
            }
            else if(name.equals("id")){
                MyActivity.Userid= String.valueOf(reader.nextInt());
                names=names+"id:"+MyActivity.Userid+"\n";
            }
            else if(name.equals("floor")){
                names=names+"floor:"+reader.nextString()+"\n";
            }
            else {
                reader.skipValue();
            }
        }
        reader.endObject();
        Log.d(DEBUG_TAG, "The names1: " + names);
        return names ;
    }
    public String PUTUrl(String myurl) throws IOException {
        InputStream is = null;
        int len = 10;
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("PUT");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            //Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

        } finally {
            if (is != null) {
                is.close();
            }
        }

    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }


}
