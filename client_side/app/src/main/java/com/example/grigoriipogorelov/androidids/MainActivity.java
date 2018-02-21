package com.example.grigoriipogorelov.androidids;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String IP = "http://10.91.83.115:5000";
    public String number_from_edittext;

    public class GET extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) /*throws IOException MalformedURLException*/ {

            URL url;
            String response = null;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                Scanner s = new Scanner(inputStream).useDelimiter("\\A");
                response = s.hasNext() ? s.next() : "NO RESPONSE";
            } catch (IOException e) {
                Log.e(TAG, "IOException URLException caught : " + e);
            }
            finally {
                urlConnection.disconnect();
            }
            return response;
        }

        protected void onPostExecute(String result) {
            EditText et = (EditText)findViewById(R.id.editText);
            et.setGravity(Gravity.CENTER);
            et.setText(result);
        }

    }

    public class POST extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) /*throws IOException MalformedURLException*/ {

            URL url;
            String response = null;
            HttpURLConnection urlConnection;
            InputStream inputStream;
            byte[] data;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);

                urlConnection.setRequestProperty("Content-Length", "" + Integer.toString(number_from_edittext.getBytes().length));
                OutputStream os = urlConnection.getOutputStream();
                data = number_from_edittext.getBytes("UTF-8");
                os.write(data);

                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();

                if (responseCode == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    Scanner s = new Scanner(inputStream).useDelimiter("\\A");
                    response = s.hasNext() ? s.next() : "NO RESPONSE";
                } else {
                }

                // Nice idea with a Map

//                Map<String,Object> params = new LinkedHashMap<>();
//                params.put("name", "Freddie the Fish");
//                params.put("email", "fishie@seamail.example.com");
//                params.put("reply_to_thread", 10394);
//                params.put("message", "Shark attacks in Botany Bay have gotten out of control. We need more defensive dolphins to protect the schools here, but Mayor Porpoise is too busy stuffing his snout with lobsters. He's so shellfish.");
//
//                StringBuilder postData = new StringBuilder();
//                for (Map.Entry<String,Object> param : params.entrySet()) {
//                    if (postData.length() != 0) postData.append('&');
//                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
//                    postData.append('=');
//                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
//                }
//                byte[] postDataBytes = postData.toString().getBytes("UTF-8");
//
//                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//                conn.setRequestMethod("POST");
//                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
//                conn.setDoOutput(true);
//                conn.getOutputStream().write(postDataBytes);

//                Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//                StringBuilder sb = new StringBuilder();
//                for (int c; (c = in.read()) >= 0;)
//                    sb.append((char)c);
//                String response = sb.toString();

            } catch (IOException e) {

            }

            return response;
        }

        protected void onPostExecute(String result) {
            EditText et = (EditText)findViewById(R.id.editText);
            et.setGravity(Gravity.CENTER);
            et.setText(result);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendRequest(View view) {
//        new GET().execute(IP + "/hello");
        EditText et = (EditText)findViewById(R.id.editText2);
        number_from_edittext = et.getText().toString();
        new POST().execute(IP + "/sqrt");
    }
}
